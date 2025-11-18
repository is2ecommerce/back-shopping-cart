package ecommerce.cart.exception;

public final class ErrorMessage {
  public static final String NO_STOCK = "No es posible agregar este producto, no hay stock suficiente";
  public static final String PRODUCT_NOT_IN_SALE = "Este producto no está disponible para la venta";
  public static final String EMPTY_CART = "El carro de compras está vacío";
  private ErrorMessage() {}
}
