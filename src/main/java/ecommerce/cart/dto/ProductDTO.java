package ecommerce.cart.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductDTO(UUID productId, BigDecimal price, boolean isAvailable, int stock) {}
