package ecommerce.cart.client;

import ecommerce.cart.dto.ProductDTO;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.springframework.web.bind.annotation.PathVariable;

// @FeignClient(name = "catalog-service", url = "${service.catalog.url}")
public interface CatalogClient {
  // @GetMapping("/api/products/{productId}/stock")
  static ProductDTO getProductStock(@PathVariable UUID productId) {
    // mocked
    // wait 0.2 seconds to simulate network latency
    try {
      Thread.sleep(200);
      return new ProductDTO(UUID.randomUUID(), BigDecimal.TEN, true, 3);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      return new ProductDTO(UUID.randomUUID(), BigDecimal.TEN, false, 0);
    }
  }

  static List<ProductDTO> getProductsStock(Set<UUID> productIds) {
    // mocked
    // wait 0.5 seconds to simulate network latency
    try {
      Thread.sleep(500);
      return productIds.stream()
          .map(id -> new ProductDTO(id, BigDecimal.TEN, true, 3))
          .toList();
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      return List.of();
    }
  }

  static void updateStock(Map<UUID, Integer> updatedStocks) {
    // mocked
    // wait 0.3 seconds to simulate network latency
    try {
      Thread.sleep(300);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }
}
