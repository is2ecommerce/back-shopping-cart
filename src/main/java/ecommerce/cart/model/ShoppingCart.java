package ecommerce.cart.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingCart implements Serializable {
  private String userId;
  private Set<CartItem> items = new HashSet<>();
  private String couponCode;

  public ShoppingCart(String userId) {
    this.userId = userId;
  }

  public Optional<CartItem> getItem(String productId) {
    return this.items.stream().filter(item -> item.getProductId().equals(productId)).findFirst();
  }
}
