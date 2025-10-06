package ecommerce.cart.controller;

import ecommerce.cart.model.CartItem;
import ecommerce.cart.model.ShoppingCart;
import ecommerce.cart.service.CartService;
import ecommerce.cart.util.Tools;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@Tag(name = "Shopping Cart")
public class ShoppingCartController {
  private final CartService cartService;

  @Operation(
      summary = "Add an item to the user's shopping cart",
      description =
          """
        Adds a specified item to the shopping cart of the requesting user. <br>
        If the cart does not exist, it will be created.
        If the item already exists in the cart, its quantity will be updated.
      """)
  @PostMapping
  public ResponseEntity<ShoppingCart> addItemToCart(
      @RequestHeader(HttpHeaders.AUTHORIZATION) @Parameter(hidden = true) String jwt,
      @RequestBody @Valid CartItem item) {
    String userId = Tools.extractUserId(jwt);
    cartService.addItem(userId, item);
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
