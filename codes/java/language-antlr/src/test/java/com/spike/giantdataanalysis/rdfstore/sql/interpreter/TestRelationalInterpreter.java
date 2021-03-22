package com.spike.giantdataanalysis.rdfstore.sql.interpreter;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.Test;

import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Expression;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.SelectStatement;
import com.spike.giantdataanalysis.model.logic.relational.interpreter.RelationalInterpreter;
import com.spike.giantdataanalysis.model.logic.relational.interpreter.RelationalInterpreterContext;
import com.spike.giantdataanalysis.model.logic.relational.model.tree.RelationalOperationTreeNode;
import com.spike.giantdataanalysis.rdfstore.sql.CaseChangingCharStream;
import com.spike.giantdataanalysis.rdfstore.sql.MySqlLexer;
import com.spike.giantdataanalysis.rdfstore.sql.MySqlParser;
import com.spike.giantdataanalysis.rdfstore.sql.RelationalAlgebraMySqlParserVisitor;
import com.spike.giantdataanalysis.rdfstore.sql.TestConstants;
import com.spike.giantdataanalysis.rdfstore.sql.TestMySqlParser;

/**
 * 单元测试: 解释器实现(Expression => Model).
 */
public class TestRelationalInterpreter {

  final RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
  final RelationalInterpreter interpreter = new RelationalInterpreter();

  private MySqlParser getParser(final String sql) {
    CharStream rawCS = CharStreams.fromString(sql);
    return getParser(rawCS);
  }

  private MySqlParser getParser(final Path path) throws IOException {
    CharStream rawCS = CharStreams.fromPath(path);
    return getParser(rawCS);
  }

  private MySqlParser getParser(CharStream charStream) {
    CaseChangingCharStream cs = new CaseChangingCharStream(charStream, true);
    MySqlLexer lexer = new MySqlLexer(cs);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    MySqlParser parser = new MySqlParser(tokens);

    parser.setErrorHandler(TestMySqlParser.errorStrategy);
    parser.addErrorListener(new TestMySqlParser.MyErrorListener(Paths.get(".")));

    return parser;
  }

  /**
   * 使用字符串形式的SQL.
   */
  @Test
  public void testSelectStatementFromSql() {
    // final String sql = "SELECT 1, 2.0, 'a'";
    final String sql = "SELECT title, year, length FROM Movies WHERE year > 1970";
    System.out.println(sql.toUpperCase());
    MySqlParser parser = this.getParser(sql);
    SelectStatement selectStatement = visitor.visitSelectStatement(parser.selectStatement());

    RelationalInterpreterContext context = new RelationalInterpreterContext();
    RelationalOperationTreeNode treeNode = interpreter.interpreter(context, selectStatement, "");
    System.out.println(context);
    System.out.println(treeNode.literal());
  }

  /**
   * 使用文件形式的SQL.
   * @throws IOException
   */
  @Test
  public void testSelectStatementFromPath() throws IOException {
    MySqlParser parser = this.getParser(Paths.get(TestConstants.test_path));
    SelectStatement selectStatement = visitor.visitSelectStatement(parser.selectStatement());

    RelationalInterpreterContext context = new RelationalInterpreterContext();
    RelationalOperationTreeNode treeNode = interpreter.interpreter(context, selectStatement, "");
    System.out.println(context);
    System.out.println(treeNode.literal());
  }

  // Expression
  @Test
  public void testExpression() throws IOException {
    final String expressionStr =
        "(A, B, C) IN (SELECT AA, BB, CC FROM EXAMPLE WHERE DD = 'DDD') AND E  = 'E' OR NOT F < 12";
    System.out.println(expressionStr);
    MySqlParser parser = this.getParser(expressionStr);
    Expression expression = visitor.visitExpression(parser.expression());

    RelationalInterpreterContext context = new RelationalInterpreterContext();
    interpreter.interpreter(context, expression, "");
    System.out.println(context);
  }

  // more test for 'sample.sql'

  @Test
  public void testSimple_Queries() {
    final String sql = TestConstants.SampleSQL.Simple_Queries();
    System.out.println(sql.toUpperCase());
    MySqlParser parser = this.getParser(sql);
    SelectStatement selectStatement = visitor.visitSelectStatement(parser.selectStatement());

    RelationalInterpreterContext context = new RelationalInterpreterContext();
    RelationalOperationTreeNode treeNode = interpreter.interpreter(context, selectStatement, "");
    System.out.println(context);
    System.out.println(treeNode.literal());
  }

  @Test
  public void testProjection1() {
    final String sql = TestConstants.SampleSQL.Projection1();
    System.out.println(sql.toUpperCase());
    MySqlParser parser = this.getParser(sql);
    SelectStatement selectStatement = visitor.visitSelectStatement(parser.selectStatement());

    RelationalInterpreterContext context = new RelationalInterpreterContext();
    RelationalOperationTreeNode treeNode = interpreter.interpreter(context, selectStatement, "");
    System.out.println(context);
    System.out.println(treeNode.literal());
  }

  @Test
  public void testProjection2() {
    final String sql = TestConstants.SampleSQL.Projection2();
    System.out.println(sql.toUpperCase());
    MySqlParser parser = this.getParser(sql);
    SelectStatement selectStatement = visitor.visitSelectStatement(parser.selectStatement());

    RelationalInterpreterContext context = new RelationalInterpreterContext();
    RelationalOperationTreeNode treeNode = interpreter.interpreter(context, selectStatement, "");
    System.out.println(context);
    System.out.println(treeNode.literal());
  }

  @Test
  public void testProjection3() {
    final String sql = TestConstants.SampleSQL.Projection3();
    System.out.println(sql.toUpperCase());
    MySqlParser parser = this.getParser(sql);
    SelectStatement selectStatement = visitor.visitSelectStatement(parser.selectStatement());

    RelationalInterpreterContext context = new RelationalInterpreterContext();
    RelationalOperationTreeNode treeNode = interpreter.interpreter(context, selectStatement, "");
    System.out.println(context);
    System.out.println(treeNode.literal());
  }

  @Test
  public void testProjection4() {
    final String sql = TestConstants.SampleSQL.Projection4();
    System.out.println(sql.toUpperCase());
    MySqlParser parser = this.getParser(sql);
    SelectStatement selectStatement = visitor.visitSelectStatement(parser.selectStatement());

    RelationalInterpreterContext context = new RelationalInterpreterContext();
    RelationalOperationTreeNode treeNode = interpreter.interpreter(context, selectStatement, "");
    System.out.println(context);
    System.out.println(treeNode.literal());
  }

  @Test
  public void testSelection() {
    final String sql = TestConstants.SampleSQL.Selection();
    System.out.println(sql.toUpperCase());
    MySqlParser parser = this.getParser(sql);
    SelectStatement selectStatement = visitor.visitSelectStatement(parser.selectStatement());

    RelationalInterpreterContext context = new RelationalInterpreterContext();
    RelationalOperationTreeNode treeNode = interpreter.interpreter(context, selectStatement, "");
    System.out.println(context);
    System.out.println(treeNode.literal());
  }

  @Test
  public void testComparson_of_Strings() {
    final String sql = TestConstants.SampleSQL.Comparson_of_Strings();
    System.out.println(sql.toUpperCase());
    MySqlParser parser = this.getParser(sql);
    SelectStatement selectStatement = visitor.visitSelectStatement(parser.selectStatement());

    RelationalInterpreterContext context = new RelationalInterpreterContext();
    RelationalOperationTreeNode treeNode = interpreter.interpreter(context, selectStatement, "");
    System.out.println(context);
    System.out.println(treeNode.literal());
  }

  @Test
  public void testPattern_Matching_in_SQL1() {
    final String sql = TestConstants.SampleSQL.Pattern_Matching_in_SQL1();
    System.out.println(sql.toUpperCase());
    MySqlParser parser = this.getParser(sql);
    SelectStatement selectStatement = visitor.visitSelectStatement(parser.selectStatement());

    RelationalInterpreterContext context = new RelationalInterpreterContext();
    RelationalOperationTreeNode treeNode = interpreter.interpreter(context, selectStatement, "");
    System.out.println(context);
    System.out.println(treeNode.literal());
  }

  @Test
  public void testPattern_Matching_in_SQL2() {
    final String sql = TestConstants.SampleSQL.Pattern_Matching_in_SQL2();
    System.out.println(sql.toUpperCase());
    MySqlParser parser = this.getParser(sql);
    SelectStatement selectStatement = visitor.visitSelectStatement(parser.selectStatement());

    RelationalInterpreterContext context = new RelationalInterpreterContext();
    RelationalOperationTreeNode treeNode = interpreter.interpreter(context, selectStatement, "");
    System.out.println(context);
    System.out.println(treeNode.literal());
  }

  @Test
  public void testOrdering1() {
    final String sql = TestConstants.SampleSQL.Ordering1();
    System.out.println(sql.toUpperCase());
    MySqlParser parser = this.getParser(sql);
    SelectStatement selectStatement = visitor.visitSelectStatement(parser.selectStatement());

    RelationalInterpreterContext context = new RelationalInterpreterContext();
    RelationalOperationTreeNode treeNode = interpreter.interpreter(context, selectStatement, "");
    System.out.println(context);
    System.out.println(treeNode.literal());
  }

  @Test
  public void testOrdering2() {
    final String sql = TestConstants.SampleSQL.Ordering2();
    System.out.println(sql.toUpperCase());
    MySqlParser parser = this.getParser(sql);
    SelectStatement selectStatement = visitor.visitSelectStatement(parser.selectStatement());

    RelationalInterpreterContext context = new RelationalInterpreterContext();
    RelationalOperationTreeNode treeNode = interpreter.interpreter(context, selectStatement, "");
    System.out.println(context);
    System.out.println(treeNode.literal());
  }

  @Test
  public void testDisambiguating_Attributes1() {
    final String sql = TestConstants.SampleSQL.Disambiguating_Attributes1();
    System.out.println(sql.toUpperCase());
    MySqlParser parser = this.getParser(sql);
    SelectStatement selectStatement = visitor.visitSelectStatement(parser.selectStatement());

    RelationalInterpreterContext context = new RelationalInterpreterContext();
    RelationalOperationTreeNode treeNode = interpreter.interpreter(context, selectStatement, "");
    System.out.println(context);
    System.out.println(treeNode.literal());
  }

  @Test
  public void testDisambiguating_Attributes2() {
    final String sql = TestConstants.SampleSQL.Disambiguating_Attributes2();
    System.out.println(sql.toUpperCase());
    MySqlParser parser = this.getParser(sql);
    SelectStatement selectStatement = visitor.visitSelectStatement(parser.selectStatement());

    RelationalInterpreterContext context = new RelationalInterpreterContext();
    RelationalOperationTreeNode treeNode = interpreter.interpreter(context, selectStatement, "");
    System.out.println(context);
    System.out.println(treeNode.literal());
  }

  @Test
  public void testDisambiguating_Attributes3() {
    final String sql = TestConstants.SampleSQL.Disambiguating_Attributes3();
    System.out.println(sql.toUpperCase());
    MySqlParser parser = this.getParser(sql);
    SelectStatement selectStatement = visitor.visitSelectStatement(parser.selectStatement());

    RelationalInterpreterContext context = new RelationalInterpreterContext();
    RelationalOperationTreeNode treeNode = interpreter.interpreter(context, selectStatement, "");
    System.out.println(context);
    System.out.println(treeNode.literal());
  }

  @Test
  public void testTuple_Variables() {
    final String sql = TestConstants.SampleSQL.Tuple_Variables();
    System.out.println(sql.toUpperCase());
    MySqlParser parser = this.getParser(sql);
    SelectStatement selectStatement = visitor.visitSelectStatement(parser.selectStatement());

    RelationalInterpreterContext context = new RelationalInterpreterContext();
    RelationalOperationTreeNode treeNode = interpreter.interpreter(context, selectStatement, "");
    System.out.println(context);
    System.out.println(treeNode.literal());
  }

  @Test
  public void testUnion_Intersection_and_Difference_of_Queries1() {
    final String sql = TestConstants.SampleSQL.Union_Intersection_and_Difference_of_Queries1();
    System.out.println(sql.toUpperCase());
    MySqlParser parser = this.getParser(sql);
    SelectStatement selectStatement = visitor.visitSelectStatement(parser.selectStatement());

    RelationalInterpreterContext context = new RelationalInterpreterContext();
    RelationalOperationTreeNode treeNode = interpreter.interpreter(context, selectStatement, "");
    System.out.println(context);
    System.out.println(treeNode.literal());
  }

  @Test
  public void testUnion_Intersection_and_Difference_of_Queries2() {
    final String sql = TestConstants.SampleSQL.Union_Intersection_and_Difference_of_Queries2();
    System.out.println(sql.toUpperCase());
    MySqlParser parser = this.getParser(sql);
    SelectStatement selectStatement = visitor.visitSelectStatement(parser.selectStatement());

    RelationalInterpreterContext context = new RelationalInterpreterContext();
    RelationalOperationTreeNode treeNode = interpreter.interpreter(context, selectStatement, "");
    System.out.println(context);
    System.out.println(treeNode.literal());
  }

  @Test
  public void testUnion_Intersection_and_Difference_of_Queries3() {
    final String sql = TestConstants.SampleSQL.Union_Intersection_and_Difference_of_Queries3();
    System.out.println(sql.toUpperCase());
    MySqlParser parser = this.getParser(sql);
    SelectStatement selectStatement = visitor.visitSelectStatement(parser.selectStatement());

    RelationalInterpreterContext context = new RelationalInterpreterContext();
    RelationalOperationTreeNode treeNode = interpreter.interpreter(context, selectStatement, "");
    System.out.println(context);
    System.out.println(treeNode.literal());
  }

  @Test
  public void testSubqueries_that_Produce_Scalar_Values1() {
    final String sql = TestConstants.SampleSQL.Subqueries_that_Produce_Scalar_Values1();
    System.out.println(sql.toUpperCase());
    MySqlParser parser = this.getParser(sql);
    SelectStatement selectStatement = visitor.visitSelectStatement(parser.selectStatement());

    RelationalInterpreterContext context = new RelationalInterpreterContext();
    RelationalOperationTreeNode treeNode = interpreter.interpreter(context, selectStatement, "");
    System.out.println(context);
    System.out.println(treeNode.literal());
  }

  @Test
  public void testSubqueries_that_Produce_Scalar_Values2() {
    final String sql = TestConstants.SampleSQL.Subqueries_that_Produce_Scalar_Values2();
    System.out.println(sql.toUpperCase());
    MySqlParser parser = this.getParser(sql);
    SelectStatement selectStatement = visitor.visitSelectStatement(parser.selectStatement());

    RelationalInterpreterContext context = new RelationalInterpreterContext();
    RelationalOperationTreeNode treeNode = interpreter.interpreter(context, selectStatement, "");
    System.out.println(context);
    System.out.println(treeNode.literal());
  }

  @Test
  public void testSubqueries_that_Produce_Scalar_Values3() {
    final String sql = TestConstants.SampleSQL.Subqueries_that_Produce_Scalar_Values3();
    System.out.println(sql.toUpperCase());
    MySqlParser parser = this.getParser(sql);
    SelectStatement selectStatement = visitor.visitSelectStatement(parser.selectStatement());

    RelationalInterpreterContext context = new RelationalInterpreterContext();
    RelationalOperationTreeNode treeNode = interpreter.interpreter(context, selectStatement, "");
    System.out.println(context);
    System.out.println(treeNode.literal());
  }

  @Test
  public void testConditions_Involving_Relations1() {
    final String sql = TestConstants.SampleSQL.Conditions_Involving_Relations1();
    System.out.println(sql.toUpperCase());
    MySqlParser parser = this.getParser(sql);
    SelectStatement selectStatement = visitor.visitSelectStatement(parser.selectStatement());

    RelationalInterpreterContext context = new RelationalInterpreterContext();
    RelationalOperationTreeNode treeNode = interpreter.interpreter(context, selectStatement, "");
    System.out.println(context);
    System.out.println(treeNode.literal());
  }

  @Test
  public void testConditions_Involving_Relations2() {
    final String sql = TestConstants.SampleSQL.Conditions_Involving_Relations2();
    System.out.println(sql.toUpperCase());
    MySqlParser parser = this.getParser(sql);
    SelectStatement selectStatement = visitor.visitSelectStatement(parser.selectStatement());

    RelationalInterpreterContext context = new RelationalInterpreterContext();
    RelationalOperationTreeNode treeNode = interpreter.interpreter(context, selectStatement, "");
    System.out.println(context);
    System.out.println(treeNode.literal());
  }

  @Test
  public void testConditions_Involving_Relations3() {
    final String sql = TestConstants.SampleSQL.Conditions_Involving_Relations3();
    System.out.println(sql.toUpperCase());
    MySqlParser parser = this.getParser(sql);
    SelectStatement selectStatement = visitor.visitSelectStatement(parser.selectStatement());

    RelationalInterpreterContext context = new RelationalInterpreterContext();
    RelationalOperationTreeNode treeNode = interpreter.interpreter(context, selectStatement, "");
    System.out.println(context);
    System.out.println(treeNode.literal());
  }

  @Test
  public void testConditions_Involving_Relations4() {
    final String sql = TestConstants.SampleSQL.Conditions_Involving_Relations4();
    System.out.println(sql.toUpperCase());
    MySqlParser parser = this.getParser(sql);
    SelectStatement selectStatement = visitor.visitSelectStatement(parser.selectStatement());

    RelationalInterpreterContext context = new RelationalInterpreterContext();
    RelationalOperationTreeNode treeNode = interpreter.interpreter(context, selectStatement, "");
    System.out.println(context);
    System.out.println(treeNode.literal());
  }

  @Test
  public void testConditions_Involving_Relations5() {
    final String sql = TestConstants.SampleSQL.Conditions_Involving_Relations5();
    System.out.println(sql.toUpperCase());
    MySqlParser parser = this.getParser(sql);
    SelectStatement selectStatement = visitor.visitSelectStatement(parser.selectStatement());

    RelationalInterpreterContext context = new RelationalInterpreterContext();
    RelationalOperationTreeNode treeNode = interpreter.interpreter(context, selectStatement, "");
    System.out.println(context);
    System.out.println(treeNode.literal());
  }

  @Test
  public void testConditions_Involving_Relations6() {
    final String sql = TestConstants.SampleSQL.Conditions_Involving_Relations6();
    System.out.println(sql.toUpperCase());
    MySqlParser parser = this.getParser(sql);
    SelectStatement selectStatement = visitor.visitSelectStatement(parser.selectStatement());

    RelationalInterpreterContext context = new RelationalInterpreterContext();
    RelationalOperationTreeNode treeNode = interpreter.interpreter(context, selectStatement, "");
    System.out.println(context);
    System.out.println(treeNode.literal());
  }

  @Test
  public void testConditions_Involving_Tuples1() {
    final String sql = TestConstants.SampleSQL.Conditions_Involving_Tuples1();
    System.out.println(sql.toUpperCase());
    MySqlParser parser = this.getParser(sql);
    SelectStatement selectStatement = visitor.visitSelectStatement(parser.selectStatement());

    RelationalInterpreterContext context = new RelationalInterpreterContext();
    RelationalOperationTreeNode treeNode = interpreter.interpreter(context, selectStatement, "");
    System.out.println(context);
    System.out.println(treeNode.literal());
  }

  @Test
  public void testConditions_Involving_Tuples2() {
    final String sql = TestConstants.SampleSQL.Conditions_Involving_Tuples2();
    System.out.println(sql.toUpperCase());
    MySqlParser parser = this.getParser(sql);
    SelectStatement selectStatement = visitor.visitSelectStatement(parser.selectStatement());

    RelationalInterpreterContext context = new RelationalInterpreterContext();
    RelationalOperationTreeNode treeNode = interpreter.interpreter(context, selectStatement, "");
    System.out.println(context);
    System.out.println(treeNode.literal());
  }

  @Test
  public void testCorrelated_Subqueries() {
    final String sql = TestConstants.SampleSQL.Correlated_Subqueries();
    System.out.println(sql.toUpperCase());
    MySqlParser parser = this.getParser(sql);
    SelectStatement selectStatement = visitor.visitSelectStatement(parser.selectStatement());

    RelationalInterpreterContext context = new RelationalInterpreterContext();
    RelationalOperationTreeNode treeNode = interpreter.interpreter(context, selectStatement, "");
    System.out.println(context);
    System.out.println(treeNode.literal());
  }

  @Test
  public void testSubqueries_in_FROM_Clauses() {
    final String sql = TestConstants.SampleSQL.Subqueries_in_FROM_Clauses();
    System.out.println(sql.toUpperCase());
    MySqlParser parser = this.getParser(sql);
    SelectStatement selectStatement = visitor.visitSelectStatement(parser.selectStatement());

    RelationalInterpreterContext context = new RelationalInterpreterContext();
    RelationalOperationTreeNode treeNode = interpreter.interpreter(context, selectStatement, "");
    System.out.println(context);
    System.out.println(treeNode.literal());
  }

  @Test
  public void testSQL_Join_Expressions() {
    final String sql = TestConstants.SampleSQL.SQL_Join_Expressions();
    System.out.println(sql.toUpperCase());
    MySqlParser parser = this.getParser(sql);
    SelectStatement selectStatement = visitor.visitSelectStatement(parser.selectStatement());

    RelationalInterpreterContext context = new RelationalInterpreterContext();
    RelationalOperationTreeNode treeNode = interpreter.interpreter(context, selectStatement, "");
    System.out.println(context);
    System.out.println(treeNode.literal());
  }

  @Test
  public void testNatural_Joins() {
    final String sql = TestConstants.SampleSQL.Natural_Joins();
    System.out.println(sql.toUpperCase());
    MySqlParser parser = this.getParser(sql);
    SelectStatement selectStatement = visitor.visitSelectStatement(parser.selectStatement());

    RelationalInterpreterContext context = new RelationalInterpreterContext();
    RelationalOperationTreeNode treeNode = interpreter.interpreter(context, selectStatement, "");
    System.out.println(context);
    System.out.println(treeNode.literal());
  }

  @Test
  public void testOuterjoins1() {
    final String sql = TestConstants.SampleSQL.Outerjoins1();
    System.out.println(sql.toUpperCase());
    MySqlParser parser = this.getParser(sql);
    SelectStatement selectStatement = visitor.visitSelectStatement(parser.selectStatement());

    RelationalInterpreterContext context = new RelationalInterpreterContext();
    RelationalOperationTreeNode treeNode = interpreter.interpreter(context, selectStatement, "");
    System.out.println(context);
    System.out.println(treeNode.literal());
  }

  @Test
  public void testOuterjoins2() {
    final String sql = TestConstants.SampleSQL.Outerjoins2();
    System.out.println(sql.toUpperCase());
    MySqlParser parser = this.getParser(sql);
    SelectStatement selectStatement = visitor.visitSelectStatement(parser.selectStatement());

    RelationalInterpreterContext context = new RelationalInterpreterContext();
    RelationalOperationTreeNode treeNode = interpreter.interpreter(context, selectStatement, "");
    System.out.println(context);
    System.out.println(treeNode.literal());
  }

  @Test
  public void testEliminating_Duplicates() {
    final String sql = TestConstants.SampleSQL.Eliminating_Duplicates();
    System.out.println(sql.toUpperCase());
    MySqlParser parser = this.getParser(sql);
    SelectStatement selectStatement = visitor.visitSelectStatement(parser.selectStatement());

    RelationalInterpreterContext context = new RelationalInterpreterContext();
    RelationalOperationTreeNode treeNode = interpreter.interpreter(context, selectStatement, "");
    System.out.println(context);
    System.out.println(treeNode.literal());
  }

  @Test
  public void testDuplicates_in_Unions_Intersections_and_Differences1() {
    final String sql =
        TestConstants.SampleSQL.Duplicates_in_Unions_Intersections_and_Differences1();
    System.out.println(sql.toUpperCase());
    MySqlParser parser = this.getParser(sql);
    SelectStatement selectStatement = visitor.visitSelectStatement(parser.selectStatement());

    RelationalInterpreterContext context = new RelationalInterpreterContext();
    RelationalOperationTreeNode treeNode = interpreter.interpreter(context, selectStatement, "");
    System.out.println(context);
    System.out.println(treeNode.literal());
  }

  @Test
  public void testDuplicates_in_Unions_Intersections_and_Differences2() {
    final String sql =
        TestConstants.SampleSQL.Duplicates_in_Unions_Intersections_and_Differences2();
    System.out.println(sql.toUpperCase());
    MySqlParser parser = this.getParser(sql);
    SelectStatement selectStatement = visitor.visitSelectStatement(parser.selectStatement());

    RelationalInterpreterContext context = new RelationalInterpreterContext();
    RelationalOperationTreeNode treeNode = interpreter.interpreter(context, selectStatement, "");
    System.out.println(context);
    System.out.println(treeNode.literal());
  }

  @Test
  public void testDuplicates_in_Unions_Intersections_and_Differences3() {
    final String sql =
        TestConstants.SampleSQL.Duplicates_in_Unions_Intersections_and_Differences3();
    System.out.println(sql.toUpperCase());
    MySqlParser parser = this.getParser(sql);
    SelectStatement selectStatement = visitor.visitSelectStatement(parser.selectStatement());

    RelationalInterpreterContext context = new RelationalInterpreterContext();
    RelationalOperationTreeNode treeNode = interpreter.interpreter(context, selectStatement, "");
    System.out.println(context);
    System.out.println(treeNode.literal());
  }

  @Test
  public void testDuplicates_in_Unions_Intersections_and_Differences4() {
    final String sql =
        TestConstants.SampleSQL.Duplicates_in_Unions_Intersections_and_Differences4();
    System.out.println(sql.toUpperCase());
    MySqlParser parser = this.getParser(sql);
    SelectStatement selectStatement = visitor.visitSelectStatement(parser.selectStatement());

    RelationalInterpreterContext context = new RelationalInterpreterContext();
    RelationalOperationTreeNode treeNode = interpreter.interpreter(context, selectStatement, "");
    System.out.println(context);
    System.out.println(treeNode.literal());
  }

  @Test
  public void testDuplicates_in_Unions_Intersections_and_Differences5() {
    final String sql =
        TestConstants.SampleSQL.Duplicates_in_Unions_Intersections_and_Differences5();
    System.out.println(sql.toUpperCase());
    MySqlParser parser = this.getParser(sql);
    SelectStatement selectStatement = visitor.visitSelectStatement(parser.selectStatement());

    RelationalInterpreterContext context = new RelationalInterpreterContext();
    RelationalOperationTreeNode treeNode = interpreter.interpreter(context, selectStatement, "");
    System.out.println(context);
    System.out.println(treeNode.literal());
  }

  @Test
  public void testDuplicates_in_Unions_Intersections_and_Differences6() {
    final String sql =
        TestConstants.SampleSQL.Duplicates_in_Unions_Intersections_and_Differences6();
    System.out.println(sql.toUpperCase());
    MySqlParser parser = this.getParser(sql);
    SelectStatement selectStatement = visitor.visitSelectStatement(parser.selectStatement());

    RelationalInterpreterContext context = new RelationalInterpreterContext();
    RelationalOperationTreeNode treeNode = interpreter.interpreter(context, selectStatement, "");
    System.out.println(context);
    System.out.println(treeNode.literal());
  }

  @Test
  public void testAggregation_Operators1() {
    final String sql = TestConstants.SampleSQL.Aggregation_Operators1();
    System.out.println(sql.toUpperCase());
    MySqlParser parser = this.getParser(sql);
    SelectStatement selectStatement = visitor.visitSelectStatement(parser.selectStatement());

    RelationalInterpreterContext context = new RelationalInterpreterContext();
    RelationalOperationTreeNode treeNode = interpreter.interpreter(context, selectStatement, "");
    System.out.println(context);
    System.out.println(treeNode.literal());
  }

  @Test
  public void testAggregation_Operators2() {
    final String sql = TestConstants.SampleSQL.Aggregation_Operators2();
    System.out.println(sql.toUpperCase());
    MySqlParser parser = this.getParser(sql);
    SelectStatement selectStatement = visitor.visitSelectStatement(parser.selectStatement());

    RelationalInterpreterContext context = new RelationalInterpreterContext();
    RelationalOperationTreeNode treeNode = interpreter.interpreter(context, selectStatement, "");
    System.out.println(context);
    System.out.println(treeNode.literal());
  }

  @Test
  public void testAggregation_Operators3() {
    final String sql = TestConstants.SampleSQL.Aggregation_Operators3();
    System.out.println(sql.toUpperCase());
    MySqlParser parser = this.getParser(sql);
    SelectStatement selectStatement = visitor.visitSelectStatement(parser.selectStatement());

    RelationalInterpreterContext context = new RelationalInterpreterContext();
    RelationalOperationTreeNode treeNode = interpreter.interpreter(context, selectStatement, "");
    System.out.println(context);
    System.out.println(treeNode.literal());
  }

  @Test
  public void testAggregation_Operators4() {
    final String sql = TestConstants.SampleSQL.Aggregation_Operators4();
    System.out.println(sql.toUpperCase());
    MySqlParser parser = this.getParser(sql);
    SelectStatement selectStatement = visitor.visitSelectStatement(parser.selectStatement());

    RelationalInterpreterContext context = new RelationalInterpreterContext();
    RelationalOperationTreeNode treeNode = interpreter.interpreter(context, selectStatement, "");
    System.out.println(context);
    System.out.println(treeNode.literal());
  }

  @Test
  public void testGrouping1() {
    final String sql = TestConstants.SampleSQL.Grouping1();
    System.out.println(sql.toUpperCase());
    MySqlParser parser = this.getParser(sql);
    SelectStatement selectStatement = visitor.visitSelectStatement(parser.selectStatement());

    RelationalInterpreterContext context = new RelationalInterpreterContext();
    RelationalOperationTreeNode treeNode = interpreter.interpreter(context, selectStatement, "");
    System.out.println(context);
    System.out.println(treeNode.literal());
  }

  @Test
  public void testGrouping2() {
    final String sql = TestConstants.SampleSQL.Grouping2();
    System.out.println(sql.toUpperCase());
    MySqlParser parser = this.getParser(sql);
    SelectStatement selectStatement = visitor.visitSelectStatement(parser.selectStatement());

    RelationalInterpreterContext context = new RelationalInterpreterContext();
    RelationalOperationTreeNode treeNode = interpreter.interpreter(context, selectStatement, "");
    System.out.println(context);
    System.out.println(treeNode.literal());
  }

  @Test
  public void testGrouping3() {
    final String sql = TestConstants.SampleSQL.Grouping3();
    System.out.println(sql.toUpperCase());
    MySqlParser parser = this.getParser(sql);
    SelectStatement selectStatement = visitor.visitSelectStatement(parser.selectStatement());

    RelationalInterpreterContext context = new RelationalInterpreterContext();
    RelationalOperationTreeNode treeNode = interpreter.interpreter(context, selectStatement, "");
    System.out.println(context);
    System.out.println(treeNode.literal());
  }

  @Test
  public void testHAVING_Clauses() {
    final String sql = TestConstants.SampleSQL.HAVING_Clauses();
    System.out.println(sql.toUpperCase());
    MySqlParser parser = this.getParser(sql);
    SelectStatement selectStatement = visitor.visitSelectStatement(parser.selectStatement());

    RelationalInterpreterContext context = new RelationalInterpreterContext();
    RelationalOperationTreeNode treeNode = interpreter.interpreter(context, selectStatement, "");
    System.out.println(context);
    System.out.println(treeNode.literal());
  }

  @Test
  public void testDSI5_1() {
    final String sql = TestConstants.SampleSQL.DSI5_1();
    System.out.println(sql.toUpperCase());
    MySqlParser parser = this.getParser(sql);
    SelectStatement selectStatement = visitor.visitSelectStatement(parser.selectStatement());

    RelationalInterpreterContext context = new RelationalInterpreterContext();
    RelationalOperationTreeNode treeNode = interpreter.interpreter(context, selectStatement, "");
    System.out.println(context);
    System.out.println(treeNode.literal());
  }

  @Test
  public void testDSI5_2() {
    final String sql = TestConstants.SampleSQL.DSI5_2();
    System.out.println(sql.toUpperCase());
    MySqlParser parser = this.getParser(sql);
    SelectStatement selectStatement = visitor.visitSelectStatement(parser.selectStatement());

    RelationalInterpreterContext context = new RelationalInterpreterContext();
    RelationalOperationTreeNode treeNode = interpreter.interpreter(context, selectStatement, "");
    System.out.println(context);
    System.out.println(treeNode.literal());
  }

  // TODO(zhoujiagen) hack this!!!
  @Test
  public void testDSI5_3() {
    final String sql = TestConstants.SampleSQL.DSI5_3();
    System.out.println(sql.toUpperCase());
    MySqlParser parser = this.getParser(sql);
    SelectStatement selectStatement = visitor.visitSelectStatement(parser.selectStatement());

    RelationalInterpreterContext context = new RelationalInterpreterContext();
    RelationalOperationTreeNode treeNode = interpreter.interpreter(context, selectStatement, "");
    System.out.println(context);
    System.out.println(treeNode.literal());
  }

}
