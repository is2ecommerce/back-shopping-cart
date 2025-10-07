package ecommerce.cart.model;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItem implements Serializable {
  private String productId;

  private Integer quantity;

  public void increase() {
    this.quantity += 1;
  }

  public void decrease() {
    if (this.quantity > 0) {
      this.quantity -= 1;
    }
  }
}
