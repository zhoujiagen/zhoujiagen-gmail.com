package com.spike.rtfsc.jena;

import java.io.IOException;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;

/**
 * SPARQL Examples from Book "Learning SPARQL, 2nd Edition".
 */
public class ARQAPITutorial {
  final static String RESOURCE_PREFIX = "src/main/resources/LearningSPARQLExamples/";

  public static void main(String[] args) throws IOException {
    select();
    // construct();
    // describe();
    // ask();
  }

  static void select() {
    System.out.println("=== select");

    Model model = RDFDataMgr.loadModel(RESOURCE_PREFIX + "ex012.ttl", Lang.TTL);
    RDFDataMgr.write(System.out, model, Lang.N3);

    // who in the address book data has the phone number (229) 276-5135
    final String queryString = //
        "PREFIX ab: <http://learningsparql.com/ns/addressbook#>\n" //
            + "SELECT ?person\n" //
            + "WHERE\n" //
            + "{ ?person ab:homeTel \"(229) 276-5135\" . }";
    System.out.println();
    System.out.println(queryString);
    System.out.println();

    final Query query = QueryFactory.create(queryString);
    try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
      ResultSet results = qexec.execSelect(); // 只可遍历一次
      while (results.hasNext()) {
        QuerySolution solution = results.next();
        RDFNode person = solution.get("person");
        processResult(person);
      }
    }

    model.close();
  }

  static void processResult(RDFNode node) {
    if (node == null) {
      return;
    }

    if (node.isLiteral()) {
      Literal nodeLiteral = (Literal) node;
      System.out.println(nodeLiteral.getLexicalForm());
    }

    if (node.isResource()) {
      Resource nodeResource = (Resource) node;
      if (!nodeResource.isAnon()) {
        System.out.println(nodeResource.getURI());
      } else {
        System.out.println("[]");
      }
    }
  }

  static void construct() {
    System.out.println("=== construct");

    Model model = RDFDataMgr.loadModel(RESOURCE_PREFIX + "ex012.ttl", Lang.TTL);
    RDFDataMgr.write(System.out, model, Lang.N3);

    final String queryString = //
        "PREFIX ab: <http://learningsparql.com/ns/addressbook#>\n" //
            + "PREFIX d: <http://learningsparql.com/ns/data#>\n" //
            + "CONSTRUCT { ?person ?p ?o. }\n" //
            + "WHERE \n"//
            + "{\n"//
            + "?person ab:firstName \"Craig\";\n" //
            + "        ab:lastName \"Ellis\";\n" //
            + "        ?p ?o .\n" //
            + "}";
    System.out.println();
    System.out.println(queryString);
    System.out.println();

    final Query query = QueryFactory.create(queryString);
    Model resultModel = null;
    try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
      resultModel = qexec.execConstruct();
    }
    if (resultModel != null) {
      RDFDataMgr.write(System.out, resultModel, Lang.TTL);
    }

    model.close();
  }

  static void describe() {
    System.out.println("=== describe");

    Model model = RDFDataMgr.loadModel(RESOURCE_PREFIX + "ex069.ttl", Lang.TTL);
    RDFDataMgr.write(System.out, model, Lang.N3);

    final String queryString = "DESCRIBE <http://learningsparql.com/ns/data#course59>";
    System.out.println();
    System.out.println(queryString);
    System.out.println();

    final Query query = QueryFactory.create(queryString);
    Model resultModel = null;
    try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
      resultModel = qexec.execDescribe();
    }
    if (resultModel != null) {
      RDFDataMgr.write(System.out, resultModel, Lang.TTL);
    }

    model.close();
  }

  static void ask() {
    System.out.println("=== ask");

    Model model = RDFDataMgr.loadModel(RESOURCE_PREFIX + "ex104.ttl", Lang.TTL);
    RDFDataMgr.write(System.out, model, Lang.N3);

    final String queryString = //
        "PREFIX dm: <http://learningsparql.com/ns/demo#>\n" //
            + "ASK WHERE\n" //
            + "{\n" //
            + "  ?s dm:location ?city.\n" //
            + "  FILTER (!(isURI(?city)))\n" //
            + "}";
    System.out.println();
    System.out.println(queryString);
    System.out.println();

    final Query query = QueryFactory.create(queryString);
    boolean result = false;
    try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
      result = qexec.execAsk();
    }
    System.out.println(result);

    model.close();
  }

}
