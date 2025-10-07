package ecommerce.cart.client;

import ecommerce.cart.dto.ProductStockDTO;
import org.springframework.web.bind.annotation.PathVariable;

// @FeignClient(name = "catalog-service", url = "${service.catalog.url}")
public interface CatalogClient {
  // @GetMapping("/api/products/{productId}/stock")
  static ProductStockDTO getProductStock(@PathVariable String productId) {
    // mocked
    return new ProductStockDTO(true, 3);
  }
}
