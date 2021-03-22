package com.spike.codesnippet.janino;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

import org.codehaus.commons.compiler.CompilerFactoryFactory;
import org.codehaus.commons.compiler.ISimpleCompiler;

/**
 * @see ISimpleCompiler
 */
public class ExampleJaninoSimpleCompiler {
  public static void main(String[] args) {
    String file = "public\n" + //
        "class Foo {\n" + //
        " \n" + //
        "    public static void\n" + //
        "    main(String[] args) {\n" + //
        "        new Bar().meth();\n" + //
        "    }\n" + //
        "}\n" + //
        " \n" + //
        "public\n" + //
        "class Bar {\n" + //
        " \n" + //
        "    public void\n" + //
        "    meth() {\n" + //
        "        System.out.println(\"HELLO!\");\n" + //
        "    }\n" + //
        "}";

    try {
      ISimpleCompiler sc = CompilerFactoryFactory.getDefaultCompilerFactory().newSimpleCompiler();
      sc.cook(file);
      Map<String, byte[]> bytecodeMap = sc.getBytecodes();
      if (bytecodeMap != null) {
        for (String className : bytecodeMap.keySet()) {
          System.out.println(className);
          System.out.println(Arrays.toString(bytecodeMap.get(className)));
        }
      }
      ClassLoader cl = sc.getClassLoader();
      System.out.println(cl);
      Class<?> clazz = cl.loadClass("Foo");

      System.out.println(clazz.getCanonicalName());
      Method m = clazz.getMethod("main", String[].class);
      m.invoke(clazz.newInstance(), (Object) new String[] {});
    } catch (Exception e) {
      e.printStackTrace();
    }

  }
}
