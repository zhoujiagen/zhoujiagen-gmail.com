package com.spike.codesnippet.janino;

import java.lang.reflect.InvocationTargetException;

import org.codehaus.commons.compiler.CompileException;
import org.codehaus.commons.compiler.CompilerFactoryFactory;
import org.codehaus.commons.compiler.IExpressionEvaluator;
import org.codehaus.janino.ExpressionEvaluator;

/**
 * @see ExpressionEvaluator
 * @see IExpressionEvaluator
 */
public class ExampleJaninoExpressionEvaluator {
  public static void main(String[] args) {
    ExpressionEvaluator ee = new ExpressionEvaluator();
    try {
      ee.cook("3 + 4");
      Object result = ee.evaluate();
      System.out.println(result); // 7
      System.out.println(result instanceof Integer); // true
      System.out.println(result instanceof Long); // false
      System.out.println(int.class.equals(result.getClass())); // false
    } catch (CompileException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    }

    System.out.println();

    try {
      IExpressionEvaluator iee =
          CompilerFactoryFactory.getDefaultCompilerFactory().newExpressionEvaluator();

      iee.setExpressionType(double.class);
      iee.setParameters(new String[] { "total" }, new Class[] { double.class });
      iee.cook("total > 100.0 ? 0.0 :7.95");

      Object[] arguments = { new Double(200) };
      Object result = iee.evaluate(arguments);
      System.out.println(String.valueOf(result));

      arguments = new Object[] { new Double(20) };
      result = iee.evaluate(arguments);
      System.out.println(String.valueOf(result));
    } catch (Exception e) {
      e.printStackTrace();
    }

  }
}
