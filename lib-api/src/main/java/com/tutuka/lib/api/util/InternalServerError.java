
package com.tutuka.lib.api.util;

@SuppressWarnings("WeakerAccess")
public class InternalServerError extends RuntimeException {

  public InternalServerError(final String reason) {
    super(reason);
  }
}
