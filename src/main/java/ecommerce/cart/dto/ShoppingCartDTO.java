package ecommerce.cart.dto;

import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para enviar el carrito completo al frontend con información enriquecida
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingCartDTO {
  private UUID userId;
  private List<CartItemDTO> items;
  private Double subtotal;
  private Double tax;
  private Double shipping;
  private Double total;
}
