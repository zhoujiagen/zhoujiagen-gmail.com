package com.spike.codesnippet.janino;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.codehaus.commons.compiler.CompileException;
import org.codehaus.janino.Java;
import org.codehaus.janino.Parser;
import org.codehaus.janino.Scanner;
import org.codehaus.janino.util.AbstractTraverser;

/**
 * @see AbstractTraverser
 */
public class ExampleJaninoTraverser extends AbstractTraverser<RuntimeException> {
  public static void main(String[] args) {
    ExampleJaninoTraverser traverser = new ExampleJaninoTraverser();

    final String fileName =
        "src/main/java/com/spike/codesnippet/janino/ExampleJaninoTraverser.java";
    try {
      FileReader r = new FileReader(fileName);

      Java.AbstractCompilationUnit acu;
      try {
        acu = new Parser(new Scanner(fileName, r)).parseAbstractCompilationUnit();

        traverser.visitAbstractCompilationUnit(acu);

        System.out.println("Class declarations:     " + traverser.classDeclarationCount);
        System.out.println("Interface declarations: " + traverser.interfaceDeclarationCount);
        System.out.println("Fields:                 " + traverser.fieldCount);
        System.out.println("Local variables:        " + traverser.localVariableCount);

      } catch (CompileException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      } finally {
        try {
          r.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }

    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  private int classDeclarationCount;
  private int interfaceDeclarationCount;
  private int fieldCount;
  private int localVariableCount;

  @Override
  public void traverseClassDeclaration(Java.AbstractClassDeclaration cd) {
    ++this.classDeclarationCount;
    super.traverseClassDeclaration(cd);
  }

  // Count interface declarations.
  @Override
  public void traverseInterfaceDeclaration(Java.InterfaceDeclaration id) {
    ++this.interfaceDeclarationCount;
    super.traverseInterfaceDeclaration(id);
  }

  // Count fields.
  @Override
  public void traverseFieldDeclaration(Java.FieldDeclaration fd) {
    this.fieldCount += fd.variableDeclarators.length;
    super.traverseFieldDeclaration(fd);
  }

  // Count local variables.
  @Override
  public void
      traverseLocalVariableDeclarationStatement(Java.LocalVariableDeclarationStatement lvds) {
    this.localVariableCount += lvds.variableDeclarators.length;
    super.traverseLocalVariableDeclarationStatement(lvds);
  }

}
