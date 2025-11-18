package ecommerce.cart.service;

import ecommerce.cart.model.ShoppingCart;
import java.util.UUID;

public interface CartService {
  /**
   * Add an item to the user's shopping cart. If the cart does not exist, it will be created. If the
   * item already exists in the cart, its quantity will be updated.
   *
   * @param userId the user's unique identifier
   * @param productId the product's unique identifier
   */
  ShoppingCart addItem(UUID userId, UUID productId);

  /**
   * Get the user's shopping cart. If the cart does not exist, an empty cart will be returned.
   *
   * @param userId the user's unique identifier
   * @return the shopping cart
   */
  ShoppingCart getCart(UUID userId);

  /**
   * Update the quantity of an item in the user's shopping cart. If the new quantity is 0, the item
   * will be removed from the cart. If the item does not exist in the cart, no action is taken.
   * Stock availability is checked before updating if the quantity is increased.
   *
   * @param userId id of the cart owner
   * @param productId item to update
   * @param newQuantity new quantity to set
   * @return user's updated shopping cart
   */
  ShoppingCart updateItemQuantity(UUID userId, UUID productId, Integer newQuantity);

  /**
   * Remove an item from the user's shopping cart regardless of its quantity
   *
   * @param userId id of the cart owner
   * @param productId item to remove
   * @return user's updated shopping cart
   */
  ShoppingCart removeItemFromCart(UUID userId, UUID productId);

  /**
   * Checkout the user's shopping cart, triggering order processing and clearing the cart
   *
   * @param userId the user's unique identifier
   */
  void checkout(UUID userId);
}
