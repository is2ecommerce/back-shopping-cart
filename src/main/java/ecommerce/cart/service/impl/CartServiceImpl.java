package ecommerce.cart.service.impl;

import ecommerce.cart.client.CatalogClient;
import ecommerce.cart.dto.ProductDTO;
import ecommerce.cart.exception.ErrorMessage;
import ecommerce.cart.exception.InternalException;
import ecommerce.cart.exception.ValidationException;
import ecommerce.cart.model.CartItem;
import ecommerce.cart.model.ShoppingCart;
import ecommerce.cart.service.CartService;
import ecommerce.cart.service.rabbit.RabbitPublisher;
import ecommerce.cart.util.Validators;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
  private final RedisTemplate<String, Object> redisTemplate;
  private final RabbitPublisher rabbitPublisher;
  private /* final */ CatalogClient catalogClient;
  private static final String CART_KEY = "cart";

  private String getKey(UUID userId) {
    return CART_KEY + ":" + userId;
  }

  @Override
  public ShoppingCart addItem(UUID userId, UUID productId) {
    ProductDTO productStock = CatalogClient.getProductStock(productId);
    validateProductInSale(productStock);

    ShoppingCart cart = getCart(userId);
    Optional<CartItem> existingItem = cart.getItem(productId);

    CartItem cartItem;
    if (existingItem.isPresent()) {
      cartItem = existingItem.get();
      cartItem.increase();
    } else {
      cartItem = new CartItem(productId, 1);
      cart.getItems().add(cartItem);
    }

    validateStock(cartItem.getQuantity(), productStock.stock());
    redisTemplate.opsForValue().set(getKey(userId), cart);
    return cart;
  }

  private void validateStock(int desiredQuantity, int availableStock) {
    if (desiredQuantity > availableStock) throw new ValidationException(ErrorMessage.NO_STOCK);
  }

  private void validateProductInSale(ProductDTO productStock) {
    if (!productStock.isAvailable() || productStock.stock() <= 0) {
      throw new ValidationException(ErrorMessage.PRODUCT_NOT_IN_SALE);
    }
  }

  @Override
  public ShoppingCart getCart(UUID userId) {
    Object o = redisTemplate.opsForValue().get(getKey(userId));
    return o != null ? (ShoppingCart) o : new ShoppingCart(userId);
  }

  @Override
  public ShoppingCart updateItemQuantity(UUID userId, UUID productId, Integer newQuantity) {
    Validators.min(newQuantity, 0);
    ShoppingCart cart = getCart(userId);
    Optional<CartItem> itemOptional = cart.getItem(productId);

    if (itemOptional.isEmpty()) return cart;

    CartItem item = itemOptional.get();
    Integer currentQuantity = item.getQuantity();
    if (newQuantity.equals(currentQuantity)) return cart;

    if (newQuantity.equals(0)) {
      cart.removeItem(productId);
    } else if (newQuantity > currentQuantity) {
      ProductDTO productStock = CatalogClient.getProductStock(productId);
      validateProductInSale(productStock);
      validateStock(newQuantity, productStock.stock());
    }
    item.setQuantity(newQuantity);
    redisTemplate.opsForValue().set(getKey(userId), cart);
    return cart;
  }

  @Override
  public ShoppingCart removeItemFromCart(UUID userId, UUID productId) {
    ShoppingCart cart = getCart(userId);
    if (cart.isEmpty()) return cart;

    cart.removeItem(productId);
    redisTemplate.opsForValue().set(getKey(userId), cart);
    return cart;
  }

  @Override
  public void checkout(UUID userId) {
    ShoppingCart cart = getCart(userId);
    if (cart.isEmpty()) {
      throw new ValidationException(ErrorMessage.EMPTY_CART);
    }
    Map<UUID, ProductDTO> productsStock = getProductsStock(cart);
    Map<UUID, Integer> updatedStocks = new HashMap<>();

    for (CartItem item : cart.getItems()) {
      ProductDTO stock = productsStock.get(item.getProductId());
      validateProductInSale(stock);
      if (item.getQuantity().compareTo(stock.stock()) > 0) {
        throw new ValidationException("No hay stock suficiente");
      }
      updatedStocks.put(item.getProductId(), stock.stock() - item.getQuantity());
    }
    CatalogClient.updateStock(updatedStocks);
    confirmCheckout(cart);
  }

  private void confirmCheckout(ShoppingCart cart) {
    rabbitPublisher.send(cart);
    log.info("Checkout event sent: {}", cart);
    redisTemplate.delete(getKey(cart.getUserId()));
  }

  private static Map<UUID, ProductDTO> getProductsStock(ShoppingCart cart) {
    List<ProductDTO> productsStock =
        CatalogClient.getProductsStock(
            cart.getItems().stream().map(CartItem::getProductId).collect(Collectors.toSet()));
    if (productsStock.size() != cart.getItems().size()) {
      throw new InternalException(
          "Mismatch between cart items and products stock retrieved from catalog service");
    }
    return productsStock.stream()
        .collect(Collectors.toMap(ProductDTO::productId, Function.identity()));
  }
}
