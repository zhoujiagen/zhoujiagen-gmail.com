package com.spike.codesnippet.janino;

import java.io.File;

import org.codehaus.janino.JavaSourceClassLoader;

/**
 * @see JavaSourceClassLoader
 */
public class ExampleJaninoJavaSourceClassLoader {
  public static void main(String[] args) {
    // 源文件目录
    System.out.println(new File("src/main/resources/srcdir").exists());

    JavaSourceClassLoader cl = new JavaSourceClassLoader(//
        ExampleJaninoJavaSourceClassLoader.class.getClassLoader(), //
        new File[] { new File("src/main/resources/srcdir") }, //
        (String) null//
    );
    cl.setDebuggingInfo(true, true, true);

    try {
      Object a = cl.loadClass("pkg1.A").newInstance();
      ((Runnable) a).run();
    } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
      e.printStackTrace();
    }

  }
}
