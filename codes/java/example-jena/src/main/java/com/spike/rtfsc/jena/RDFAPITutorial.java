package com.spike.rtfsc.jena;

import java.io.IOException;
import java.io.InputStream;

import org.apache.jena.rdf.model.Bag;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.SimpleSelector;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.apache.jena.vocabulary.VCARD;
import org.apache.jena.vocabulary.VCARD4;

import com.github.jsonldjava.shaded.com.google.common.base.Preconditions;

/**
 * https://jena.apache.org/tutorials/rdf_api.html
 * 
 * <pre>
 * statements
 * writing rdf
 * reading rdf
 * controlling prefixes
 * navigating a model
 * querying a model
 * operations on models
 * containers
 * literals and datatypes
 * </pre>
 */
public class RDFAPITutorial {

  static String personURI = "http://somewhere/JohnSmith";
  static String givenName = "John";
  static String familyName = "Smith";

  public static void main(String[] args) throws IOException {

    String fullName = givenName + " " + familyName;
    // 创建空模型
    Model model = ModelFactory.createDefaultModel();

    // 创建资源
    Resource johnSmith = model.createResource(personURI);
    johnSmith.addProperty(VCARD.FN, fullName);
    johnSmith.addProperty(VCARD.N, //
      model.createResource()//
          .addProperty(VCARD.Given, givenName)//
          .addProperty(VCARD.Family, familyName));

    System.out.println(johnSmith);

    // statement
    StmtIterator iter = model.listStatements();
    while (iter.hasNext()) {
      Statement stmt = iter.next();
      Resource subject = stmt.getSubject();
      Property predicate = stmt.getPredicate();
      RDFNode object = stmt.getObject();

      System.out.print(subject.toString());
      System.out.print(" " + predicate.toString() + " ");
      if (object instanceof Resource) {
        System.out.print(object.toString());
      } else {
        System.out.print("\"" + object.toString() + "\"");
      }

      System.out.println(".");
    }
    System.out.println();

    writeRDF(model);
    readRDF();

    controllingPrefix();

    navigateModel(model);

    queryModel(model);
    operationOnModels();
    containers();
    literals();

    model.close();
  }

  // writing rdf
  static void writeRDF(Model model) {
    model.write(System.out);
    System.out.println();
    RDFDataMgr.write(System.out, model, Lang.RDFXML);
    System.out.println();
    RDFDataMgr.write(System.out, model, Lang.NTRIPLES);
    System.out.println();

  }

  // reading rdf
  static void readRDF() throws IOException {
    Model model = ModelFactory.createDefaultModel();
    InputStream is = RDFDataMgr.open("src/main/resources/tutorial.rdf");
    Preconditions.checkArgument(is != null, "文件不存在");
    model.read(is, null);
    RDFDataMgr.write(System.out, model, Lang.N3);
    is.close();
    System.out.println();

    model.close();
  }

  // controlling prefixes
  static void controllingPrefix() {
    Model m = ModelFactory.createDefaultModel();
    String nsA = "http://somewhere/else#";
    String nsB = "http://nowhere/else#";
    Resource root = m.createResource(nsA + "root");
    Property P = m.createProperty(nsA + "P");
    Property Q = m.createProperty(nsB + "Q");
    Resource x = m.createResource(nsA + "x");
    Resource y = m.createResource(nsA + "y");
    Resource z = m.createResource(nsA + "z");
    m.add(root, P, x).add(root, P, y).add(y, Q, z);

    System.out.println("--- no specical prefix defined");
    RDFDataMgr.write(System.out, m, Lang.N3);

    System.out.println("--- nsA defined");
    m.setNsPrefix("nsA", nsA);
    RDFDataMgr.write(System.out, m, Lang.N3);

    System.out.println("--- nsA and cat defined");
    m.setNsPrefix("cat", nsB);
    RDFDataMgr.write(System.out, m, Lang.N3);
    System.out.println();

    m.close();
  }

  // navigating a model
  static void navigateModel(Model model) {
    Resource vcard = model.getResource(personURI);
    Resource name = (Resource) vcard.getProperty(VCARD.N).getObject();
    System.out.println(name.getProperty(VCARD.Given));
    String theFullName = vcard.getProperty(VCARD.FN).getString();
    System.out.println(theFullName);

    vcard.addProperty(VCARD.NICKNAME, "Smithy")//
        .addProperty(VCARD.NICKNAME, "Adman");
    StmtIterator stmtIter = vcard.listProperties(VCARD.NICKNAME);
    while (stmtIter.hasNext()) {
      System.out.println(stmtIter.nextStatement().getObject().toString());
    }
    System.out.println();
  }

  // querying a model
  static void queryModel(Model model) {
    ResIterator resIter = model.listSubjectsWithProperty(VCARD.FN);
    while (resIter.hasNext()) {
      System.out.println(resIter.nextResource());
    }
    System.out.println();

    StmtIterator stmtIterator =
        model.listStatements(new SimpleSelector(null, VCARD.FN, (RDFNode) null) {
          @Override
          public boolean selects(Statement s) {
            return s.getString().endsWith("Smith");
          }
        });
    while (stmtIterator.hasNext()) {
      System.out.println(stmtIterator.next());
    }
    System.out.println();
  }

  // operations on models: union, intersection, difference
  @SuppressWarnings("deprecation")
  static void operationOnModels() {
    Model model1 = ModelFactory.createDefaultModel();
    String johnSmithURI = "http://somedomain/JohnSmith";
    Resource jsResource1 = model1.createResource(johnSmithURI);
    jsResource1.addProperty(VCARD.N, model1.createResource()//
        .addProperty(VCARD.Given, "John")//
        .addProperty(VCARD.Family, "Smith"));
    Model model2 = ModelFactory.createDefaultModel();
    Resource jsResource2 = model2.createResource(johnSmithURI);
    jsResource2.addProperty(VCARD.FN, "John Smith")//
        .addProperty(VCARD.EMAIL, model2.createResource()//
            .addProperty(RDF.type, VCARD4.Internet)//
            .addProperty(RDF.value, "John@xxx"));

    Model unionedModel = model1.union(model2);
    RDFDataMgr.write(System.out, unionedModel, Lang.N3);
    System.out.println();
  }

  // containers: BAG, ALT, SEQ
  static void containers() {
    Model model = ModelFactory.createDefaultModel();
    Bag smiths = model.createBag();
    smiths.add(model.createResource("http://somedomain/BeckySmith")//
        .addProperty(VCARD.FN, "Becky Smith"));
    smiths.add(model.createResource("http://somedomain/JohnSmith")//
        .addProperty(VCARD.FN, "John Smith"));
    RDFDataMgr.write(System.out, model, Lang.N3);
    System.out.println();

    model.close();
  }

  // literals and datatypes
  static void literals() {
    Model model = ModelFactory.createDefaultModel();
    Resource r = model.createResource();
    r.addProperty(RDFS.label, model.createLiteral("chat", "en"));
    r.addProperty(RDFS.label, model.createLiteral("<em>chat</em>", true));

    r.addProperty(RDFS.label, "11")//
        .addProperty(RDFS.label, "11");

    RDFDataMgr.write(System.out, model, Lang.N3);
    System.out.println();

    model.close();
  }

}
