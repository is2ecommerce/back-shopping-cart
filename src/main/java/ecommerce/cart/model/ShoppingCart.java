package ecommerce.cart.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingCart implements Serializable {
  private UUID userId;
  private Set<CartItem> items = new HashSet<>();
  private String couponCode;

  public ShoppingCart(UUID userId) {
    this.userId = userId;
  }

  public Optional<CartItem> getItem(UUID productId) {
    return this.items.stream().filter(item -> item.getProductId().equals(productId)).findFirst();
  }

  @JsonIgnore
  public boolean isEmpty() {
    return this.items.isEmpty();
  }

  public void removeItem(UUID productId) {
    this.items.removeIf(i -> i.getProductId().equals(productId));
  }
}
