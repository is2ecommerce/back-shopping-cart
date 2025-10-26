package ecommerce.cart.util;

import ecommerce.cart.exception.ValidationException;

public final class Validators {

  public static void min(Integer field, int value) {
    if (field == null || field < value) {
      throw new ValidationException("El valor debe ser mayor o igual a " + value);
    }
  }

  private Validators() {}
}
