package com.spike.codesnippet.janino;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.codehaus.commons.compiler.CompilerFactoryFactory;
import org.codehaus.commons.compiler.IClassBodyEvaluator;

/**
 * @see IClassBodyEvaluator
 */
public class ExampleJaninoClassBodyEvaluator {
  public static void main(String[] args) {

    String classBody = "public static void\n" //
        + "main(String[] args) {\n" //
        + "    System.out.println(java.util.Arrays.asList(args));\n" //
        + "}";

    try {
      IClassBodyEvaluator cbe =
          CompilerFactoryFactory.getDefaultCompilerFactory().newClassBodyEvaluator();
      cbe.cook(classBody);
      Class<?> c = cbe.getClazz();

      Method m = c.getMethod("main", String[].class);
      String[] arguments = { "hello", "janino" };
      Object returnValue = m.invoke(null, (Object) arguments);

      if (m.getReturnType() != void.class) {
        System.out.println(returnValue instanceof Object[] ? Arrays.toString((Object[]) returnValue)
            : String.valueOf(returnValue));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

  }
}
