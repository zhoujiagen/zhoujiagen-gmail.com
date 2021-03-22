package com.spike.codesnippet.janino;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.commons.compiler.CompilerFactoryFactory;
import org.codehaus.commons.compiler.ICompiler;
import org.codehaus.commons.compiler.util.ResourceFinderClassLoader;
import org.codehaus.commons.compiler.util.resource.MapResourceCreator;
import org.codehaus.commons.compiler.util.resource.MapResourceFinder;
import org.codehaus.commons.compiler.util.resource.Resource;
import org.codehaus.commons.compiler.util.resource.ResourceCreator;
import org.codehaus.commons.compiler.util.resource.StringResource;

/**
 * @see ICompiler
 */
public class ExampleJaninoCompiler {
  public static void main(String[] args) {
    try {
      ICompiler c = CompilerFactoryFactory.getDefaultCompilerFactory().newCompiler();

      Map<String, byte[]> map = new HashMap<>();
      ResourceCreator cfc = new MapResourceCreator(map);
      c.setClassFileCreator(cfc);

      c.compile(new Resource[] { //
          new StringResource( //
              "pkg1/A.java", //
              "package pkg1; public class A { public static int meth() { return pkg2.B.meth(); } }" //
          ), //
          new StringResource(//
              "pkg2/B.java", //
              "package pkg2; public class B { public static int meth() { return 77;            } }"//
          ), });

      ClassLoader cl = new ResourceFinderClassLoader(//
          new MapResourceFinder(map), //
          ClassLoader.getSystemClassLoader() //
      );

      Object callMethResult = cl.loadClass("pkg1.A").getDeclaredMethod("meth").invoke(null);
      System.out.println(callMethResult); // 77

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
