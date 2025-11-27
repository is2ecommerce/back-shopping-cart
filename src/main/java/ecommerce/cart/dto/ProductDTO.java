package ecommerce.cart.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductDTO(
    UUID productId, 
    String name,
    BigDecimal price, 
    String imageUrl,
    Boolean isAvailable, 
    Integer stock
) {}
