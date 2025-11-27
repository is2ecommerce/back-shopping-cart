package ecommerce.cart.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para enviar información completa del item al frontend
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDTO {
  private UUID productId;
  private String name;
  private Double price;
  private Integer quantity;
  private String imageUrl;
}
