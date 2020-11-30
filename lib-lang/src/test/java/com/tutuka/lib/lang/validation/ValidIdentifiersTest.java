
package com.tutuka.lib.lang.validation;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.tutuka.lib.lang.validation.TestHelper.isValid;

public class ValidIdentifiersTest {

  @Test
  public void valid()
  {
    final AnnotatedClass annotatedInstance = new AnnotatedClass(Collections.singletonList("xxxx"));
    Assert.assertTrue(isValid(annotatedInstance));
  }

  @Test
  public void toolong()
  {
    final AnnotatedClass annotatedInstance = new AnnotatedClass(Collections.singletonList("xxxxxx"));
    Assert.assertFalse(isValid(annotatedInstance));
  }

  @Test
  public void nullList()
  {
    final AnnotatedClass annotatedInstance = new AnnotatedClass(null);
    Assert.assertFalse(isValid(annotatedInstance));
  }

  @Test
  public void nullInList()
  {
    final AnnotatedClass annotatedInstance = new AnnotatedClass(Collections.singletonList(null));
    Assert.assertFalse(isValid(annotatedInstance));
  }

  @Test
  public void badApple()
  {
    final AnnotatedClass annotatedInstance = new AnnotatedClass(Arrays.asList("xxxx", null));
    Assert.assertFalse(isValid(annotatedInstance));
  }

  private static class AnnotatedClass {
    List<String> identifiers;

    AnnotatedClass(final List<String> identifiers) {
      this.identifiers= identifiers;
    }
  }
}
