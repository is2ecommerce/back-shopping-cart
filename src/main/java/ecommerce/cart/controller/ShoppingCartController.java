package ecommerce.cart.controller;

import ecommerce.cart.model.ShoppingCart;
import ecommerce.cart.service.CartService;
import ecommerce.cart.util.Tools;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Shopping Cart")
@RequestMapping("/api/cart")
public class ShoppingCartController {
  private final CartService cartService;
  @Operation(
      summary = "Add an item to the user's shopping cart",
      description =
          """
        Adds a specified item to the shopping cart of the requesting user. <br>
        If the cart does not exist, it will be created.
        If the item already exists in the cart, its quantity will be updated by 1. <br>
        Stock availability is checked before adding the item.
    """)
  @PostMapping("/items")
  public ResponseEntity<ShoppingCart> addItemToCart(
      @RequestHeader(HttpHeaders.AUTHORIZATION) @Parameter(hidden = true) String jwt,
      @RequestParam @NotBlank String productId) {
    String userId = Tools.extractUserId(jwt);
    cartService.addItem(userId, productId);
    return ResponseEntity.ok().build();
  }

  @Operation(
      summary = "Get the user's shopping cart",
      description =
          """
        Retrieves the shopping cart for the requesting user. <br>
        If the cart does not exist, an empty cart will be returned.
      """)
  @GetMapping
  public ResponseEntity<ShoppingCart> getCart(
      @RequestHeader(HttpHeaders.AUTHORIZATION) @Parameter(hidden = true) String jwt) {
    String userId = Tools.extractUserId(jwt);
    return ResponseEntity.ok(cartService.getCart(userId));
  }
}
