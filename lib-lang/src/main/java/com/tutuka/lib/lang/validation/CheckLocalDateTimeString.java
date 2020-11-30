
package com.tutuka.lib.lang.validation;

import com.tutuka.lib.lang.validation.date.DateConverter;
import com.tutuka.lib.lang.validation.constraints.ValidLocalDateTimeString;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.DateTimeException;


public class CheckLocalDateTimeString implements ConstraintValidator<ValidLocalDateTimeString, String> {
   public void initialize(ValidLocalDateTimeString constraint) {
   }

   public boolean isValid(final String obj, ConstraintValidatorContext context) {
      if (obj == null)
         return true;
      try {
         DateConverter.fromIsoString(obj);
         return true;
      }
      catch (final DateTimeException e) {
         return false;
      }
   }
}
