package ecommerce.cart.service;

import ecommerce.cart.client.CatalogClient;
import ecommerce.cart.dto.ProductStockDTO;
import ecommerce.cart.exception.ErrorMessage;
import ecommerce.cart.exception.ValidationException;
import ecommerce.cart.model.CartItem;
import ecommerce.cart.model.ShoppingCart;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {
  private final RedisTemplate<String, Object> redisTemplate;

  // add final to autowire
  private CatalogClient catalogClient;
  private static final String CART_KEY = "cart";

  private String getKey(String userId) {
    return CART_KEY + ":" + userId;
  }

  /**
   * Add an item to the user's shopping cart. If the cart does not exist, it will be created. If the
   * item already exists in the cart, its quantity will be updated.
   *
   * @param userId the user's unique identifier
   * @param productId the product's unique identifier
   */
  public void addItem(String userId, String productId) {
    ProductStockDTO productStock = CatalogClient.getProductStock(productId);
    validateProductInSale(productStock);

    ShoppingCart cart = getCart(userId);
    CartItem cartItem = cart.getItem(productId).orElse(new CartItem(productId, 0));
    cartItem.increase();
    validateStock(cartItem.getQuantity(), productStock.stock());
    cart.getItems().add(cartItem);
    redisTemplate.opsForValue().set(getKey(userId), cart);
  }

  private void validateStock(int desiredQuantity, int availableStock) {
    if (desiredQuantity > availableStock) throw new ValidationException(ErrorMessage.NO_STOCK);
  }

  private void validateProductInSale(ProductStockDTO productStock) {
    if (!productStock.isAvailable() || productStock.stock() <= 0) {
      throw new ValidationException(ErrorMessage.PRODUCT_NOT_IN_SALE);
    }
  }

  /**
   * Get the user's shopping cart. If the cart does not exist, an empty cart will be returned.
   *
   * @param userId the user's unique identifier
   * @return the shopping cart
   */
  public ShoppingCart getCart(String userId) {
    Object o = redisTemplate.opsForValue().get(getKey(userId));
    return o != null ? (ShoppingCart) o : new ShoppingCart(userId);
  }
}
