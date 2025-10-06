package ecommerce.cart.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItem implements Serializable {
  @NotNull
  @Schema(description = "The unique identifier of the product", example = "prod-12345")
  private String productId;

  @NotNull
  @Positive
  @Schema(description = "The quantity of the product to add", example = "2")
  private Integer quantity;
}
