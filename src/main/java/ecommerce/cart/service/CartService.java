package ecommerce.cart.service;

import ecommerce.cart.model.CartItem;
import ecommerce.cart.model.ShoppingCart;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {
  private final RedisTemplate<String, Object> redisTemplate;
  private static final String CART_KEY = "cart";

  private String getKey(String userId) {
    return CART_KEY + ":" + userId;
  }

  /**
   * Add an item to the user's shopping cart. If the cart does not exist, it will be created. If the
   * item already exists in the cart, its quantity will be updated.
   *
   * @param userId the user's unique identifier
   * @param item the item to add
   */
  public void addItem(String userId, CartItem item) {
    ShoppingCart cart = getCart(userId);
    cart.add(item);
    redisTemplate.opsForValue().set(getKey(userId), cart);
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
