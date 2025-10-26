package ecommerce.cart.service.impl;

import ecommerce.cart.client.CatalogClient;
import ecommerce.cart.dto.ProductStockDTO;
import ecommerce.cart.exception.ErrorMessage;
import ecommerce.cart.exception.ValidationException;
import ecommerce.cart.model.CartItem;
import ecommerce.cart.model.ShoppingCart;
import ecommerce.cart.service.CartService;
import ecommerce.cart.util.Validators;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
  private final RedisTemplate<String, Object> redisTemplate;
  private /* final */ CatalogClient catalogClient;
  private static final String CART_KEY = "cart";

  private String getKey(String userId) {
    return CART_KEY + ":" + userId;
  }

  @Override
  public ShoppingCart addItem(String userId, String productId) {
    ProductStockDTO productStock = CatalogClient.getProductStock(productId);
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

  private void validateProductInSale(ProductStockDTO productStock) {
    if (!productStock.isAvailable() || productStock.stock() <= 0) {
      throw new ValidationException(ErrorMessage.PRODUCT_NOT_IN_SALE);
    }
  }

  @Override
  public ShoppingCart getCart(String userId) {
    Object o = redisTemplate.opsForValue().get(getKey(userId));
    return o != null ? (ShoppingCart) o : new ShoppingCart(userId);
  }

  @Override
  public ShoppingCart updateItemQuantity(String userId, String productId, Integer newQuantity) {
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
      ProductStockDTO productStock = CatalogClient.getProductStock(productId);
      validateProductInSale(productStock);
      validateStock(newQuantity, productStock.stock());
    }
    item.setQuantity(newQuantity);
    redisTemplate.opsForValue().set(getKey(userId), cart);
    return cart;
  }

  @Override
  public ShoppingCart removeItemFromCart(String userId, String productId) {
    ShoppingCart cart = getCart(userId);
    if (cart.isEmpty()) return cart;

    cart.removeItem(productId);
    redisTemplate.opsForValue().set(getKey(userId), cart);
    return cart;
  }
}
