package com.spike.codesnippet.janino;

import java.util.Arrays;

import org.codehaus.commons.compiler.CompilerFactoryFactory;
import org.codehaus.commons.compiler.IScriptEvaluator;
import org.codehaus.janino.ScriptEvaluator;

/**
 * @see ScriptEvaluator
 * @see IScriptEvaluator
 */
public class ExampleJaninoScriptEvaluator {
  public static void main(String[] args) {
    try {
      ScriptEvaluator se = new ScriptEvaluator();
      String script = //
          "static void method1() {\n" //
              + "    System.out.println(1);\n" //
              + "}\n" //
              + "\n" //
              + "method1();\n" //
              + "method2();\n" //
              + "\n" //
              + "static void method2() {\n" //
              + "    System.out.println(2);\n" //
              + "}\n";
      se.cook(script);
      Object result = se.evaluate(null);
      System.out.println(result); // null

      System.out.println();

      script = "static double method(double v) {\n" //
          + " System.out.println(v);\n"//
          + " return v;\n" //
          + "}\n"//
          + "\n" //
          + "method(a);\n";

      Class<?> returnType = void.class;
      String[] parameterNames = { "a" };
      Class<?>[] parameterTypes = { double.class };
      String[] defaultImports = {};
      Class<?>[] thrownExceptions = {};
      IScriptEvaluator ise =
          CompilerFactoryFactory.getDefaultCompilerFactory().newScriptEvaluator();
      ise.setReturnType(returnType);
      ise.setDefaultImports(defaultImports);
      ise.setParameters(parameterNames, parameterTypes);
      ise.setThrownExceptions(thrownExceptions);
      ise.cook(script);

      Object[] arguments = { 2.0 };
      Object res = ise.evaluate(arguments);
      System.out.println("Result = "
          + (res instanceof Object[] ? Arrays.toString((Object[]) res) : String.valueOf(res)));

    } catch (Exception e) {
      e.printStackTrace();
    }

  }
}
