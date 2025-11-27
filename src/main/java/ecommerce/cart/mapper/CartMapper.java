package ecommerce.cart.mapper;

import ecommerce.cart.client.CatalogClient;
import ecommerce.cart.dto.CartItemDTO;
import ecommerce.cart.dto.ProductDTO;
import ecommerce.cart.dto.ShoppingCartDTO;
import ecommerce.cart.model.CartItem;
import ecommerce.cart.model.ShoppingCart;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper para convertir entidades de dominio a DTOs
 */
@Component
public class CartMapper {
  
  private static final double TAX_RATE = 0.08;  // 8%
  private static final double SHIPPING_COST = 15.0;
  private static final double FREE_SHIPPING_THRESHOLD = 250.0;
  
  /**
   * Convierte un ShoppingCart del dominio a un ShoppingCartDTO enriquecido con información del catálogo
   */
  public ShoppingCartDTO toDTO(ShoppingCart cart) {
    List<CartItemDTO> itemDTOs = cart.getItems().stream()
        .map(this::toCartItemDTO)
        .collect(Collectors.toList());
    
    double subtotal = calculateSubtotal(itemDTOs);
    double shipping = subtotal >= FREE_SHIPPING_THRESHOLD ? 0.0 : SHIPPING_COST;
    double tax = subtotal * TAX_RATE;
    double total = subtotal + shipping + tax;
    
    return new ShoppingCartDTO(
        cart.getUserId(),
        itemDTOs,
        subtotal,
        tax,
        shipping,
        total
    );
  }
  
  /**
   * Convierte un CartItem a CartItemDTO enriquecido con información del producto
   */
  private CartItemDTO toCartItemDTO(CartItem item) {
    ProductDTO product = CatalogClient.getProductStock(item.getProductId());
    
    return new CartItemDTO(
        item.getProductId(),
        product.name(),
        product.price().doubleValue(),
        item.getQuantity(),
        product.imageUrl()
    );
  }
  
  /**
   * Calcula el subtotal del carrito
   */
  private double calculateSubtotal(List<CartItemDTO> items) {
    return items.stream()
        .mapToDouble(item -> item.getPrice() * item.getQuantity())
        .sum();
  }
}
