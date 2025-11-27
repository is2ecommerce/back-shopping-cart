package ecommerce.cart.client;

import ecommerce.cart.dto.ProductDTO;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.springframework.web.bind.annotation.PathVariable;

// @FeignClient(name = "catalog-service", url = "${service.catalog.url}")
public interface CatalogClient {
  
  // Mock catalog database - Sincronizado con front-shopping-cart/product.service.ts
  Map<UUID, ProductDTO> MOCK_CATALOG = new HashMap<>() {{
    put(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"), new ProductDTO(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"), "Wireless Bluetooth Headphones", new BigDecimal("89.99"), "https://co.tiendasishop.com/cdn/shop/files/IMG-14858589.jpg?v=1726245557", true, 15));
    put(UUID.fromString("123e4567-e89b-12d3-a456-426614174001"), new ProductDTO(UUID.fromString("123e4567-e89b-12d3-a456-426614174001"), "Premium Cotton T-Shirt", new BigDecimal("29.99"), "https://peopleplays.vtexassets.com/arquivos/ids/511458-800-auto?v=638731773540670000&width=800&height=auto&aspect=true", true, 50));
    put(UUID.fromString("123e4567-e89b-12d3-a456-426614174002"), new ProductDTO(UUID.fromString("123e4567-e89b-12d3-a456-426614174002"), "Leather Wallet", new BigDecimal("49.99"), "https://m.media-amazon.com/images/I/81qlL+JqgEL._AC_SY695_.jpg", true, 30));
    put(UUID.fromString("123e4567-e89b-12d3-a456-426614174003"), new ProductDTO(UUID.fromString("123e4567-e89b-12d3-a456-426614174003"), "Smart Watch Series 8", new BigDecimal("299.99"), "https://m.media-amazon.com/images/I/71uZXFwXQzL._AC_SL1500_.jpg", true, 8));
    put(UUID.fromString("123e4567-e89b-12d3-a456-426614174004"), new ProductDTO(UUID.fromString("123e4567-e89b-12d3-a456-426614174004"), "Running Shoes Ultraboost", new BigDecimal("129.99"), "https://assets.adidas.com/images/w_600,f_auto,q_auto/a284c05789e64be281506dbda3204ed0_9366/Tenis_Ultraboost_5_Turquesa_JQ2911_HM1.jpg", true, 25));
    put(UUID.fromString("123e4567-e89b-12d3-a456-426614174005"), new ProductDTO(UUID.fromString("123e4567-e89b-12d3-a456-426614174005"), "Portable Bluetooth Speaker", new BigDecimal("79.99"), "https://m.media-amazon.com/images/I/81+W5wfGy6L._AC_SL1500_.jpg", true, 20));
    put(UUID.fromString("123e4567-e89b-12d3-a456-426614174006"), new ProductDTO(UUID.fromString("123e4567-e89b-12d3-a456-426614174006"), "Stainless Steel Water Bottle", new BigDecimal("24.99"), "https://m.media-amazon.com/images/I/61nRZOhhGfL._AC_SL1500_.jpg", true, 100));
    put(UUID.fromString("123e4567-e89b-12d3-a456-426614174007"), new ProductDTO(UUID.fromString("123e4567-e89b-12d3-a456-426614174007"), "Wireless Gaming Mouse", new BigDecimal("59.99"), "https://m.media-amazon.com/images/I/61mpMH5TzkL._AC_SL1500_.jpg", true, 12));
    put(UUID.fromString("123e4567-e89b-12d3-a456-426614174008"), new ProductDTO(UUID.fromString("123e4567-e89b-12d3-a456-426614174008"), "Yoga Mat Premium", new BigDecimal("34.99"), "https://m.media-amazon.com/images/I/81Z-F3E8ljL._AC_SL1500_.jpg", true, 40));
    put(UUID.fromString("123e4567-e89b-12d3-a456-426614174009"), new ProductDTO(UUID.fromString("123e4567-e89b-12d3-a456-426614174009"), "Coffee Maker Deluxe", new BigDecimal("89.99"), "https://m.media-amazon.com/images/I/71w8Y6Y8SQL._AC_SL1500_.jpg", true, 18));
  }};
  
  // @GetMapping("/api/products/{productId}/stock")
  static ProductDTO getProductStock(@PathVariable UUID productId) {
    // Simula latencia de red
    try {
      Thread.sleep(200);
      ProductDTO product = MOCK_CATALOG.get(productId);
      if (product != null) {
        return product;
      }
      // Si no existe el producto, retorna como no disponible
      return new ProductDTO(productId, "Unknown Product", BigDecimal.ZERO, "", false, 0);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      return new ProductDTO(productId, "Unknown Product", BigDecimal.ZERO, "", false, 0);
    }
  }

  static List<ProductDTO> getProductsStock(Set<UUID> productIds) {
    // Simula latencia de red
    try {
      Thread.sleep(500);
      return productIds.stream()
          .map(id -> MOCK_CATALOG.getOrDefault(id, 
            new ProductDTO(id, "Unknown Product", BigDecimal.ZERO, "", false, 0)))
          .toList();
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      return List.of();
    }
  }

  static void updateStock(Map<UUID, Integer> updatedStocks) {
    // Simula actualización de stock (en producción esto llamaría al microservicio de catálogo)
    try {
      Thread.sleep(300);
      // En este mock, simplemente registramos la operación
      System.out.println("Mock: Actualizando stocks de productos: " + updatedStocks);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }
}
