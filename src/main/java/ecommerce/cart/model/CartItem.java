package ecommerce.cart.model;

import java.io.Serializable;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItem implements Serializable {
  private UUID productId;
  private Integer quantity;

  public void increase() {
    this.quantity += 1;
  }

  public void setQuantity(int quantity) {
    if (quantity >= 0) {
      this.quantity = quantity;
    }
  }
}
