package com.spike.giantdataanalysis.rdfstore.sql;

import java.nio.file.Paths;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.IntStream;
import org.junit.Test;

import com.spike.giantdataanalysis.rdfstore.sql.MySqlParser.*;

/**
 * MySQL解析器中规则构造的单元测试.
 * <p>
 * 注意: 被注释的方式中构造必须定义为接口.
 */
public class TestMySqlParserRuleConstructs {
  // ---------------------------------------------------------------------------
  // helper methods
  // ---------------------------------------------------------------------------

  private MySqlParser constructParser(String input) {
    CharStream rawCS = CharStreams.fromString(input);
    CaseChangingCharStream cs = new CaseChangingCharStream(rawCS, true);
    MySqlLexer lexer = new MySqlLexer(cs);
    CommonTokenStream tokens = new CommonTokenStream(lexer);

    MySqlParser parser = new MySqlParser(tokens);
    parser.setErrorHandler(TestMySqlParser.errorStrategy);
    parser.addErrorListener(
      new TestMySqlParser.MyErrorListener(Paths.get(IntStream.UNKNOWN_SOURCE_NAME)));
    return parser;
  }

  // ---------------------------------------------------------------------------
  // unit tests
  // ---------------------------------------------------------------------------
  @Test
  public void testRoot() {
    final String input = "SELECT id, name FROM user.customer;";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    RootContext context = parser.root();
    Object root = visitor.visitRoot(context);
    System.out.print(root);
  }

  @Test
  public void testSqlStatements() {
    final String input = "SELECT id, name FROM user.customer;";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    SqlStatementsContext context = parser.sqlStatements();
    Object sqlStatements = visitor.visitSqlStatements(context);
    System.out.print(sqlStatements);
  }

  @Test
  public void testSqlStatement() {
    final String input = "SELECT id, name FROM user.customer;";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    SqlStatementContext context = parser.sqlStatement();
    Object sqlStatement = visitor.visitSqlStatement(context);
    System.out.print(sqlStatement);
  }

  @Test
  public void testEmptyStatement() {
    final String input = ";";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    EmptyStatementContext context = parser.emptyStatement();
    Object emptyStatement = visitor.visitEmptyStatement(context);
    System.out.print(emptyStatement);
  }

  @Test
  public void testDdlStatement() {
    final String input = "CREATE TABLE `example` (\n"
        + "  `id` bigint(20) NOT NULL AUTO_INCREMENT,\n" + "  `name` varchar(45) DEFAULT NULL,\n"
        + "  PRIMARY KEY (`id`)\n"
        + ") ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    DdlStatementContext context = parser.ddlStatement();
    Object ddlStatement = visitor.visitDdlStatement(context);
    System.out.print(ddlStatement);
  }

  @Test
  public void testDmlStatement() {
    final String input = "SELECT id, name FROM user.customer;";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    DmlStatementContext context = parser.dmlStatement();
    Object dmlStatement = visitor.visitDmlStatement(context);
    System.out.print(dmlStatement);
  }

  @Test
  public void testTransactionStatement() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    TransactionStatementContext context = parser.transactionStatement();
    Object transactionStatement = visitor.visitTransactionStatement(context);
    System.out.print(transactionStatement);
  }

  @Test
  public void testReplicationStatement() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    ReplicationStatementContext context = parser.replicationStatement();
    Object replicationStatement = visitor.visitReplicationStatement(context);
    System.out.print(replicationStatement);
  }

  @Test
  public void testPreparedStatement() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    PreparedStatementContext context = parser.preparedStatement();
    Object preparedStatement = visitor.visitPreparedStatement(context);
    System.out.print(preparedStatement);
  }

  @Test
  public void testCompoundStatement() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    CompoundStatementContext context = parser.compoundStatement();
    Object compoundStatement = visitor.visitCompoundStatement(context);
    System.out.print(compoundStatement);
  }

  @Test
  public void testAdministrationStatement() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    AdministrationStatementContext context = parser.administrationStatement();
    Object administrationStatement = visitor.visitAdministrationStatement(context);
    System.out.print(administrationStatement);
  }

  @Test
  public void testUtilityStatement() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    UtilityStatementContext context = parser.utilityStatement();
    Object utilityStatement = visitor.visitUtilityStatement(context);
    System.out.print(utilityStatement);
  }

  @Test
  public void testCreateDatabase() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    CreateDatabaseContext context = parser.createDatabase();
    Object createDatabase = visitor.visitCreateDatabase(context);
    System.out.print(createDatabase);
  }

  @Test
  public void testCreateEvent() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    CreateEventContext context = parser.createEvent();
    Object createEvent = visitor.visitCreateEvent(context);
    System.out.print(createEvent);
  }

  @Test
  public void testCreateIndex() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    CreateIndexContext context = parser.createIndex();
    Object createIndex = visitor.visitCreateIndex(context);
    System.out.print(createIndex);
  }

  @Test
  public void testCreateLogfileGroup() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    CreateLogfileGroupContext context = parser.createLogfileGroup();
    Object createLogfileGroup = visitor.visitCreateLogfileGroup(context);
    System.out.print(createLogfileGroup);
  }

  @Test
  public void testCreateProcedure() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    CreateProcedureContext context = parser.createProcedure();
    Object createProcedure = visitor.visitCreateProcedure(context);
    System.out.print(createProcedure);
  }

  @Test
  public void testCreateFunction() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    CreateFunctionContext context = parser.createFunction();
    Object createFunction = visitor.visitCreateFunction(context);
    System.out.print(createFunction);
  }

  @Test
  public void testCreateServer() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    CreateServerContext context = parser.createServer();
    Object createServer = visitor.visitCreateServer(context);
    System.out.print(createServer);
  }

  // @Test
  // public void testCreateTable() {
  // final String input = "";
  // MySqlParser parser = this.constructParser(input);
  //
  // RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
  // CreateTableContext context = parser.createTable();
  // Object createTable = visitor.visitCreateTable(context);
  // System.out.print(createTable);
  // }

  @Test
  public void testCreateTablespaceInnodb() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    CreateTablespaceInnodbContext context = parser.createTablespaceInnodb();
    Object createTablespaceInnodb = visitor.visitCreateTablespaceInnodb(context);
    System.out.print(createTablespaceInnodb);
  }

  @Test
  public void testCreateTablespaceNdb() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    CreateTablespaceNdbContext context = parser.createTablespaceNdb();
    Object createTablespaceNdb = visitor.visitCreateTablespaceNdb(context);
    System.out.print(createTablespaceNdb);
  }

  @Test
  public void testCreateTrigger() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    CreateTriggerContext context = parser.createTrigger();
    Object createTrigger = visitor.visitCreateTrigger(context);
    System.out.print(createTrigger);
  }

  @Test
  public void testCreateView() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    CreateViewContext context = parser.createView();
    Object createView = visitor.visitCreateView(context);
    System.out.print(createView);
  }

  @Test
  public void testCreateDatabaseOption() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    CreateDatabaseOptionContext context = parser.createDatabaseOption();
    Object createDatabaseOption = visitor.visitCreateDatabaseOption(context);
    System.out.print(createDatabaseOption);
  }

  @Test
  public void testOwnerStatement() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    OwnerStatementContext context = parser.ownerStatement();
    Object ownerStatement = visitor.visitOwnerStatement(context);
    System.out.print(ownerStatement);
  }

  // @Test
  // public void testScheduleExpression() {
  // final String input = "";
  // MySqlParser parser = this.constructParser(input);
  //
  // RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
  // ScheduleExpressionContext context = parser.scheduleExpression();
  // Object scheduleExpression = visitor.visitScheduleExpression(context);
  // System.out.print(scheduleExpression);
  // }

  @Test
  public void testTimestampValue() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    TimestampValueContext context = parser.timestampValue();
    Object timestampValue = visitor.visitTimestampValue(context);
    System.out.print(timestampValue);
  }

  @Test
  public void testIntervalExpr() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    IntervalExprContext context = parser.intervalExpr();
    Object intervalExpr = visitor.visitIntervalExpr(context);
    System.out.print(intervalExpr);
  }

  @Test
  public void testIntervalType() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    IntervalTypeContext context = parser.intervalType();
    Object intervalType = visitor.visitIntervalType(context);
    System.out.print(intervalType);
  }

  @Test
  public void testEnableType() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    EnableTypeContext context = parser.enableType();
    Object enableType = visitor.visitEnableType(context);
    System.out.print(enableType);
  }

  @Test
  public void testIndexType() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    IndexTypeContext context = parser.indexType();
    Object indexType = visitor.visitIndexType(context);
    System.out.print(indexType);
  }

  @Test
  public void testIndexOption() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    IndexOptionContext context = parser.indexOption();
    Object indexOption = visitor.visitIndexOption(context);
    System.out.print(indexOption);
  }

  @Test
  public void testProcedureParameter() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    ProcedureParameterContext context = parser.procedureParameter();
    Object procedureParameter = visitor.visitProcedureParameter(context);
    System.out.print(procedureParameter);
  }

  @Test
  public void testFunctionParameter() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    FunctionParameterContext context = parser.functionParameter();
    Object functionParameter = visitor.visitFunctionParameter(context);
    System.out.print(functionParameter);
  }

  // @Test
  // public void testRoutineOption() {
  // final String input = "";
  // MySqlParser parser = this.constructParser(input);
  //
  // RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
  // RoutineOptionContext context = parser.routineOption();
  // Object routineOption = visitor.visitRoutineOption(context);
  // System.out.print(routineOption);
  // }

  @Test
  public void testServerOption() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    ServerOptionContext context = parser.serverOption();
    Object serverOption = visitor.visitServerOption(context);
    System.out.print(serverOption);
  }

  @Test
  public void testCreateDefinitions() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    CreateDefinitionsContext context = parser.createDefinitions();
    Object createDefinitions = visitor.visitCreateDefinitions(context);
    System.out.print(createDefinitions);
  }

  // @Test
  // public void testCreateDefinition() {
  // final String input = "";
  // MySqlParser parser = this.constructParser(input);
  //
  // RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
  // CreateDefinitionContext context = parser.createDefinition();
  // Object createDefinition = visitor.visitCreateDefinition(context);
  // System.out.print(createDefinition);
  // }

  @Test
  public void testColumnDefinition() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    ColumnDefinitionContext context = parser.columnDefinition();
    Object columnDefinition = visitor.visitColumnDefinition(context);
    System.out.print(columnDefinition);
  }

  // @Test
  // public void testColumnConstraint() {
  // final String input = "";
  // MySqlParser parser = this.constructParser(input);
  //
  // RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
  // ColumnConstraintContext context = parser.columnConstraint();
  // Object columnConstraint = visitor.visitColumnConstraint(context);
  // System.out.print(columnConstraint);
  // }

  // @Test
  // public void testTableConstraint() {
  // final String input = "";
  // MySqlParser parser = this.constructParser(input);
  //
  // RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
  // TableConstraintContext context = parser.tableConstraint();
  // Object tableConstraint = visitor.visitTableConstraint(context);
  // System.out.print(tableConstraint);
  // }

  @Test
  public void testReferenceDefinition() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    ReferenceDefinitionContext context = parser.referenceDefinition();
    Object referenceDefinition = visitor.visitReferenceDefinition(context);
    System.out.print(referenceDefinition);
  }

  @Test
  public void testReferenceAction() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    ReferenceActionContext context = parser.referenceAction();
    Object referenceAction = visitor.visitReferenceAction(context);
    System.out.print(referenceAction);
  }

  @Test
  public void testReferenceControlType() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    ReferenceControlTypeContext context = parser.referenceControlType();
    Object referenceControlType = visitor.visitReferenceControlType(context);
    System.out.print(referenceControlType);
  }

  // @Test
  // public void testIndexColumnDefinition() {
  // final String input = "";
  // MySqlParser parser = this.constructParser(input);
  //
  // RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
  // IndexColumnDefinitionContext context = parser.indexColumnDefinition();
  // Object indexColumnDefinition = visitor.visitIndexColumnDefinition(context);
  // System.out.print(indexColumnDefinition);
  // }

  // @Test
  // public void testTableOption() {
  // final String input = "";
  // MySqlParser parser = this.constructParser(input);
  //
  // RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
  // TableOptionContext context = parser.tableOption();
  // Object tableOption = visitor.visitTableOption(context);
  // System.out.print(tableOption);
  // }

  @Test
  public void testTablespaceStorage() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    TablespaceStorageContext context = parser.tablespaceStorage();
    Object tablespaceStorage = visitor.visitTablespaceStorage(context);
    System.out.print(tablespaceStorage);
  }

  @Test
  public void testPartitionDefinitions() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    PartitionDefinitionsContext context = parser.partitionDefinitions();
    Object partitionDefinitions = visitor.visitPartitionDefinitions(context);
    System.out.print(partitionDefinitions);
  }

  // @Test
  // public void testPartitionFunctionDefinition() {
  // final String input = "";
  // MySqlParser parser = this.constructParser(input);
  //
  // RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
  // PartitionFunctionDefinitionContext context = parser.partitionFunctionDefinition();
  // Object partitionFunctionDefinition = visitor.visitPartitionFunctionDefinition(context);
  // System.out.print(partitionFunctionDefinition);
  // }

  // @Test
  // public void testSubpartitionFunctionDefinition() {
  // final String input = "";
  // MySqlParser parser = this.constructParser(input);
  //
  // RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
  // SubpartitionFunctionDefinitionContext context = parser.subpartitionFunctionDefinition();
  // Object subpartitionFunctionDefinition = visitor.visitSubpartitionFunctionDefinition(context);
  // System.out.print(subpartitionFunctionDefinition);
  // }

  // @Test
  // public void testPartitionDefinition() {
  // final String input = "";
  // MySqlParser parser = this.constructParser(input);
  //
  // RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
  // PartitionDefinitionContext context = parser.partitionDefinition();
  // Object partitionDefinition = visitor.visitPartitionDefinition(context);
  // System.out.print(partitionDefinition);
  // }

  @Test
  public void testPartitionDefinerAtom() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    PartitionDefinerAtomContext context = parser.partitionDefinerAtom();
    Object partitionDefinerAtom = visitor.visitPartitionDefinerAtom(context);
    System.out.print(partitionDefinerAtom);
  }

  @Test
  public void testPartitionDefinerVector() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    PartitionDefinerVectorContext context = parser.partitionDefinerVector();
    Object partitionDefinerVector = visitor.visitPartitionDefinerVector(context);
    System.out.print(partitionDefinerVector);
  }

  @Test
  public void testSubpartitionDefinition() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    SubpartitionDefinitionContext context = parser.subpartitionDefinition();
    Object subpartitionDefinition = visitor.visitSubpartitionDefinition(context);
    System.out.print(subpartitionDefinition);
  }

  // @Test
  // public void testPartitionOption() {
  // final String input = "";
  // MySqlParser parser = this.constructParser(input);
  //
  // RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
  // PartitionOptionContext context = parser.partitionOption();
  // Object partitionOption = visitor.visitPartitionOption(context);
  // System.out.print(partitionOption);
  // }

  // @Test
  // public void testAlterDatabase() {
  // final String input = "";
  // MySqlParser parser = this.constructParser(input);
  //
  // RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
  // AlterDatabaseContext context = parser.alterDatabase();
  // Object alterDatabase = visitor.visitAlterDatabase(context);
  // System.out.print(alterDatabase);
  // }

  @Test
  public void testAlterEvent() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    AlterEventContext context = parser.alterEvent();
    Object alterEvent = visitor.visitAlterEvent(context);
    System.out.print(alterEvent);
  }

  @Test
  public void testAlterFunction() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    AlterFunctionContext context = parser.alterFunction();
    Object alterFunction = visitor.visitAlterFunction(context);
    System.out.print(alterFunction);
  }

  @Test
  public void testAlterInstance() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    AlterInstanceContext context = parser.alterInstance();
    Object alterInstance = visitor.visitAlterInstance(context);
    System.out.print(alterInstance);
  }

  @Test
  public void testAlterLogfileGroup() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    AlterLogfileGroupContext context = parser.alterLogfileGroup();
    Object alterLogfileGroup = visitor.visitAlterLogfileGroup(context);
    System.out.print(alterLogfileGroup);
  }

  @Test
  public void testAlterProcedure() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    AlterProcedureContext context = parser.alterProcedure();
    Object alterProcedure = visitor.visitAlterProcedure(context);
    System.out.print(alterProcedure);
  }

  @Test
  public void testAlterServer() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    AlterServerContext context = parser.alterServer();
    Object alterServer = visitor.visitAlterServer(context);
    System.out.print(alterServer);
  }

  @Test
  public void testAlterTable() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    AlterTableContext context = parser.alterTable();
    Object alterTable = visitor.visitAlterTable(context);
    System.out.print(alterTable);
  }

  @Test
  public void testAlterTablespace() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    AlterTablespaceContext context = parser.alterTablespace();
    Object alterTablespace = visitor.visitAlterTablespace(context);
    System.out.print(alterTablespace);
  }

  @Test
  public void testAlterView() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    AlterViewContext context = parser.alterView();
    Object alterView = visitor.visitAlterView(context);
    System.out.print(alterView);
  }

  // @Test
  // public void testAlterSpecification() {
  // final String input = "";
  // MySqlParser parser = this.constructParser(input);
  //
  // RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
  // AlterSpecificationContext context = parser.alterSpecification();
  // Object alterSpecification = visitor.visitAlterSpecification(context);
  // System.out.print(alterSpecification);
  // }

  @Test
  public void testDropDatabase() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    DropDatabaseContext context = parser.dropDatabase();
    Object dropDatabase = visitor.visitDropDatabase(context);
    System.out.print(dropDatabase);
  }

  @Test
  public void testDropEvent() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    DropEventContext context = parser.dropEvent();
    Object dropEvent = visitor.visitDropEvent(context);
    System.out.print(dropEvent);
  }

  @Test
  public void testDropIndex() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    DropIndexContext context = parser.dropIndex();
    Object dropIndex = visitor.visitDropIndex(context);
    System.out.print(dropIndex);
  }

  @Test
  public void testDropLogfileGroup() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    DropLogfileGroupContext context = parser.dropLogfileGroup();
    Object dropLogfileGroup = visitor.visitDropLogfileGroup(context);
    System.out.print(dropLogfileGroup);
  }

  @Test
  public void testDropProcedure() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    DropProcedureContext context = parser.dropProcedure();
    Object dropProcedure = visitor.visitDropProcedure(context);
    System.out.print(dropProcedure);
  }

  @Test
  public void testDropFunction() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    DropFunctionContext context = parser.dropFunction();
    Object dropFunction = visitor.visitDropFunction(context);
    System.out.print(dropFunction);
  }

  @Test
  public void testDropServer() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    DropServerContext context = parser.dropServer();
    Object dropServer = visitor.visitDropServer(context);
    System.out.print(dropServer);
  }

  @Test
  public void testDropTable() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    DropTableContext context = parser.dropTable();
    Object dropTable = visitor.visitDropTable(context);
    System.out.print(dropTable);
  }

  @Test
  public void testDropTablespace() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    DropTablespaceContext context = parser.dropTablespace();
    Object dropTablespace = visitor.visitDropTablespace(context);
    System.out.print(dropTablespace);
  }

  @Test
  public void testDropTrigger() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    DropTriggerContext context = parser.dropTrigger();
    Object dropTrigger = visitor.visitDropTrigger(context);
    System.out.print(dropTrigger);
  }

  @Test
  public void testDropView() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    DropViewContext context = parser.dropView();
    Object dropView = visitor.visitDropView(context);
    System.out.print(dropView);
  }

  @Test
  public void testRenameTable() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    RenameTableContext context = parser.renameTable();
    Object renameTable = visitor.visitRenameTable(context);
    System.out.print(renameTable);
  }

  @Test
  public void testRenameTableClause() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    RenameTableClauseContext context = parser.renameTableClause();
    Object renameTableClause = visitor.visitRenameTableClause(context);
    System.out.print(renameTableClause);
  }

  @Test
  public void testTruncateTable() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    TruncateTableContext context = parser.truncateTable();
    Object truncateTable = visitor.visitTruncateTable(context);
    System.out.print(truncateTable);
  }

  @Test
  public void testCallStatement() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    CallStatementContext context = parser.callStatement();
    Object callStatement = visitor.visitCallStatement(context);
    System.out.print(callStatement);
  }

  @Test
  public void testDeleteStatement() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    DeleteStatementContext context = parser.deleteStatement();
    Object deleteStatement = visitor.visitDeleteStatement(context);
    System.out.print(deleteStatement);
  }

  @Test
  public void testDoStatement() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    DoStatementContext context = parser.doStatement();
    Object doStatement = visitor.visitDoStatement(context);
    System.out.print(doStatement);
  }

  @Test
  public void testHandlerStatement() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    HandlerStatementContext context = parser.handlerStatement();
    Object handlerStatement = visitor.visitHandlerStatement(context);
    System.out.print(handlerStatement);
  }

  @Test
  public void testInsertStatement() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    InsertStatementContext context = parser.insertStatement();
    Object insertStatement = visitor.visitInsertStatement(context);
    System.out.print(insertStatement);
  }

  @Test
  public void testLoadDataStatement() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    LoadDataStatementContext context = parser.loadDataStatement();
    Object loadDataStatement = visitor.visitLoadDataStatement(context);
    System.out.print(loadDataStatement);
  }

  @Test
  public void testLoadXmlStatement() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    LoadXmlStatementContext context = parser.loadXmlStatement();
    Object loadXmlStatement = visitor.visitLoadXmlStatement(context);
    System.out.print(loadXmlStatement);
  }

  @Test
  public void testReplaceStatement() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    ReplaceStatementContext context = parser.replaceStatement();
    Object replaceStatement = visitor.visitReplaceStatement(context);
    System.out.print(replaceStatement);
  }

  @Test
  public void testSelectStatement() { // I
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    SelectStatementContext context = parser.selectStatement();
    Object selectStatement = visitor.visitSelectStatement(context);
    System.out.print(selectStatement);
  }

  @Test
  public void testUpdateStatement() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    UpdateStatementContext context = parser.updateStatement();
    Object updateStatement = visitor.visitUpdateStatement(context);
    System.out.print(updateStatement);
  }

  @Test
  public void testInsertStatementValue() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    InsertStatementValueContext context = parser.insertStatementValue();
    Object insertStatementValue = visitor.visitInsertStatementValue(context);
    System.out.print(insertStatementValue);
  }

  @Test
  public void testUpdatedElement() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    UpdatedElementContext context = parser.updatedElement();
    Object updatedElement = visitor.visitUpdatedElement(context);
    System.out.print(updatedElement);
  }

  @Test
  public void testAssignmentField() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    AssignmentFieldContext context = parser.assignmentField();
    Object assignmentField = visitor.visitAssignmentField(context);
    System.out.print(assignmentField);
  }

  @Test
  public void testLockClause() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    LockClauseContext context = parser.lockClause();
    Object lockClause = visitor.visitLockClause(context);
    System.out.print(lockClause);
  }

  @Test
  public void testSingleDeleteStatement() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    SingleDeleteStatementContext context = parser.singleDeleteStatement();
    Object singleDeleteStatement = visitor.visitSingleDeleteStatement(context);
    System.out.print(singleDeleteStatement);
  }

  @Test
  public void testMultipleDeleteStatement() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    MultipleDeleteStatementContext context = parser.multipleDeleteStatement();
    Object multipleDeleteStatement = visitor.visitMultipleDeleteStatement(context);
    System.out.print(multipleDeleteStatement);
  }

  @Test
  public void testHandlerOpenStatement() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    HandlerOpenStatementContext context = parser.handlerOpenStatement();
    Object handlerOpenStatement = visitor.visitHandlerOpenStatement(context);
    System.out.print(handlerOpenStatement);
  }

  @Test
  public void testHandlerReadIndexStatement() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    HandlerReadIndexStatementContext context = parser.handlerReadIndexStatement();
    Object handlerReadIndexStatement = visitor.visitHandlerReadIndexStatement(context);
    System.out.print(handlerReadIndexStatement);
  }

  @Test
  public void testHandlerReadStatement() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    HandlerReadStatementContext context = parser.handlerReadStatement();
    Object handlerReadStatement = visitor.visitHandlerReadStatement(context);
    System.out.print(handlerReadStatement);
  }

  @Test
  public void testHandlerCloseStatement() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    HandlerCloseStatementContext context = parser.handlerCloseStatement();
    Object handlerCloseStatement = visitor.visitHandlerCloseStatement(context);
    System.out.print(handlerCloseStatement);
  }

  @Test
  public void testSingleUpdateStatement() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    SingleUpdateStatementContext context = parser.singleUpdateStatement();
    Object singleUpdateStatement = visitor.visitSingleUpdateStatement(context);
    System.out.print(singleUpdateStatement);
  }

  @Test
  public void testMultipleUpdateStatement() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    MultipleUpdateStatementContext context = parser.multipleUpdateStatement();
    Object multipleUpdateStatement = visitor.visitMultipleUpdateStatement(context);
    System.out.print(multipleUpdateStatement);
  }

  @Test
  public void testOrderByClause() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    OrderByClauseContext context = parser.orderByClause();
    Object orderByClause = visitor.visitOrderByClause(context);
    System.out.print(orderByClause);
  }

  @Test
  public void testOrderByExpression() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    OrderByExpressionContext context = parser.orderByExpression();
    Object orderByExpression = visitor.visitOrderByExpression(context);
    System.out.print(orderByExpression);
  }

  @Test
  public void testTableSources() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    TableSourcesContext context = parser.tableSources();
    Object tableSources = visitor.visitTableSources(context);
    System.out.print(tableSources);
  }

  // @Test
  // public void testTableSource() {
  // final String input = "";
  // MySqlParser parser = this.constructParser(input);
  //
  // RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
  // TableSourceContext context = parser.tableSource();
  // Object tableSource = visitor.visitTableSource(context);
  // System.out.print(tableSource);
  // }

  // @Test
  // public void testTableSourceItem() {
  // final String input = "";
  // MySqlParser parser = this.constructParser(input);
  //
  // RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
  // TableSourceItemContext context = parser.tableSourceItem();
  // Object tableSourceItem = visitor.visitTableSourceItem(context);
  // System.out.print(tableSourceItem);
  // }

  @Test
  public void testIndexHint() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    IndexHintContext context = parser.indexHint();
    Object indexHint = visitor.visitIndexHint(context);
    System.out.print(indexHint);
  }

  @Test
  public void testIndexHintType() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    IndexHintTypeContext context = parser.indexHintType();
    Object indexHintType = visitor.visitIndexHintType(context);
    System.out.print(indexHintType);
  }

  // @Test
  // public void testJoinPart() {
  // final String input = "";
  // MySqlParser parser = this.constructParser(input);
  //
  // RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
  // JoinPartContext context = parser.joinPart();
  // Object joinPart = visitor.visitJoinPart(context);
  // System.out.print(joinPart);
  // }

  @Test
  public void testQueryExpression() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    QueryExpressionContext context = parser.queryExpression();
    Object queryExpression = visitor.visitQueryExpression(context);
    System.out.print(queryExpression);
  }

  @Test
  public void testQueryExpressionNointo() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    QueryExpressionNointoContext context = parser.queryExpressionNointo();
    Object queryExpressionNointo = visitor.visitQueryExpressionNointo(context);
    System.out.print(queryExpressionNointo);
  }

  @Test
  public void testQuerySpecification() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    QuerySpecificationContext context = parser.querySpecification();
    Object querySpecification = visitor.visitQuerySpecification(context);
    System.out.print(querySpecification);
  }

  @Test
  public void testQuerySpecificationNointo() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    QuerySpecificationNointoContext context = parser.querySpecificationNointo();
    Object querySpecificationNointo = visitor.visitQuerySpecificationNointo(context);
    System.out.print(querySpecificationNointo);
  }

  @Test
  public void testUnionParenthesis() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    UnionParenthesisContext context = parser.unionParenthesis();
    Object unionParenthesis = visitor.visitUnionParenthesis(context);
    System.out.print(unionParenthesis);
  }

  @Test
  public void testUnionStatement() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    UnionStatementContext context = parser.unionStatement();
    Object unionStatement = visitor.visitUnionStatement(context);
    System.out.print(unionStatement);
  }

  @Test
  public void testSelectSpec() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    SelectSpecContext context = parser.selectSpec();
    Object selectSpec = visitor.visitSelectSpec(context);
    System.out.print(selectSpec);
  }

  @Test
  public void testSelectElements() {
    final String input = "ID, CONCAT(CODE, 'A')";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    SelectElementsContext context = parser.selectElements();
    Object selectElements = visitor.visitSelectElements(context);
    System.out.print(selectElements);
  }

  // @Test
  // public void testSelectElement() {
  // final String input = "";
  // MySqlParser parser = this.constructParser(input);
  //
  // RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
  // SelectElementContext context = parser.selectElement();
  // Object selectElement = visitor.visitSelectElement(context);
  // System.out.print(selectElement);
  // }

  // @Test
  // public void testSelectIntoExpression() {
  // final String input = "";
  // MySqlParser parser = this.constructParser(input);
  //
  // RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
  // SelectIntoExpressionContext context = parser.selectIntoExpression();
  // Object selectIntoExpression = visitor.visitSelectIntoExpression(context);
  // System.out.print(selectIntoExpression);
  // }

  @Test
  public void testSelectFieldsInto() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    SelectFieldsIntoContext context = parser.selectFieldsInto();
    Object selectFieldsInto = visitor.visitSelectFieldsInto(context);
    System.out.print(selectFieldsInto);
  }

  @Test
  public void testSelectLinesInto() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    SelectLinesIntoContext context = parser.selectLinesInto();
    Object selectLinesInto = visitor.visitSelectLinesInto(context);
    System.out.print(selectLinesInto);
  }

  @Test
  public void testFromClause() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    FromClauseContext context = parser.fromClause();
    Object fromClause = visitor.visitFromClause(context);
    System.out.print(fromClause);
  }

  @Test
  public void testGroupByItem() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    GroupByItemContext context = parser.groupByItem();
    Object groupByItem = visitor.visitGroupByItem(context);
    System.out.print(groupByItem);
  }

  @Test
  public void testLimitClause() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    LimitClauseContext context = parser.limitClause();
    Object limitClause = visitor.visitLimitClause(context);
    System.out.print(limitClause);
  }

  @Test
  public void testLimitClauseAtom() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    LimitClauseAtomContext context = parser.limitClauseAtom();
    Object limitClauseAtom = visitor.visitLimitClauseAtom(context);
    System.out.print(limitClauseAtom);
  }

  @Test
  public void testStartTransaction() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    StartTransactionContext context = parser.startTransaction();
    Object startTransaction = visitor.visitStartTransaction(context);
    System.out.print(startTransaction);
  }

  @Test
  public void testBeginWork() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    BeginWorkContext context = parser.beginWork();
    Object beginWork = visitor.visitBeginWork(context);
    System.out.print(beginWork);
  }

  @Test
  public void testCommitWork() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    CommitWorkContext context = parser.commitWork();
    Object commitWork = visitor.visitCommitWork(context);
    System.out.print(commitWork);
  }

  @Test
  public void testRollbackWork() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    RollbackWorkContext context = parser.rollbackWork();
    Object rollbackWork = visitor.visitRollbackWork(context);
    System.out.print(rollbackWork);
  }

  @Test
  public void testSavepointStatement() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    SavepointStatementContext context = parser.savepointStatement();
    Object savepointStatement = visitor.visitSavepointStatement(context);
    System.out.print(savepointStatement);
  }

  @Test
  public void testRollbackStatement() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    RollbackStatementContext context = parser.rollbackStatement();
    Object rollbackStatement = visitor.visitRollbackStatement(context);
    System.out.print(rollbackStatement);
  }

  @Test
  public void testReleaseStatement() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    ReleaseStatementContext context = parser.releaseStatement();
    Object releaseStatement = visitor.visitReleaseStatement(context);
    System.out.print(releaseStatement);
  }

  @Test
  public void testLockTables() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    LockTablesContext context = parser.lockTables();
    Object lockTables = visitor.visitLockTables(context);
    System.out.print(lockTables);
  }

  @Test
  public void testUnlockTables() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    UnlockTablesContext context = parser.unlockTables();
    Object unlockTables = visitor.visitUnlockTables(context);
    System.out.print(unlockTables);
  }

  @Test
  public void testSetAutocommitStatement() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    SetAutocommitStatementContext context = parser.setAutocommitStatement();
    Object setAutocommitStatement = visitor.visitSetAutocommitStatement(context);
    System.out.print(setAutocommitStatement);
  }

  @Test
  public void testSetTransactionStatement() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    SetTransactionStatementContext context = parser.setTransactionStatement();
    Object setTransactionStatement = visitor.visitSetTransactionStatement(context);
    System.out.print(setTransactionStatement);
  }

  @Test
  public void testTransactionMode() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    TransactionModeContext context = parser.transactionMode();
    Object transactionMode = visitor.visitTransactionMode(context);
    System.out.print(transactionMode);
  }

  @Test
  public void testLockTableElement() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    LockTableElementContext context = parser.lockTableElement();
    Object lockTableElement = visitor.visitLockTableElement(context);
    System.out.print(lockTableElement);
  }

  @Test
  public void testLockAction() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    LockActionContext context = parser.lockAction();
    Object lockAction = visitor.visitLockAction(context);
    System.out.print(lockAction);
  }

  @Test
  public void testTransactionOption() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    TransactionOptionContext context = parser.transactionOption();
    Object transactionOption = visitor.visitTransactionOption(context);
    System.out.print(transactionOption);
  }

  @Test
  public void testTransactionLevel() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    TransactionLevelContext context = parser.transactionLevel();
    Object transactionLevel = visitor.visitTransactionLevel(context);
    System.out.print(transactionLevel);
  }

  @Test
  public void testChangeMaster() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    ChangeMasterContext context = parser.changeMaster();
    Object changeMaster = visitor.visitChangeMaster(context);
    System.out.print(changeMaster);
  }

  @Test
  public void testChangeReplicationFilter() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    ChangeReplicationFilterContext context = parser.changeReplicationFilter();
    Object changeReplicationFilter = visitor.visitChangeReplicationFilter(context);
    System.out.print(changeReplicationFilter);
  }

  @Test
  public void testPurgeBinaryLogs() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    PurgeBinaryLogsContext context = parser.purgeBinaryLogs();
    Object purgeBinaryLogs = visitor.visitPurgeBinaryLogs(context);
    System.out.print(purgeBinaryLogs);
  }

  @Test
  public void testResetMaster() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    ResetMasterContext context = parser.resetMaster();
    Object resetMaster = visitor.visitResetMaster(context);
    System.out.print(resetMaster);
  }

  @Test
  public void testResetSlave() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    ResetSlaveContext context = parser.resetSlave();
    Object resetSlave = visitor.visitResetSlave(context);
    System.out.print(resetSlave);
  }

  @Test
  public void testStartSlave() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    StartSlaveContext context = parser.startSlave();
    Object startSlave = visitor.visitStartSlave(context);
    System.out.print(startSlave);
  }

  @Test
  public void testStopSlave() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    StopSlaveContext context = parser.stopSlave();
    Object stopSlave = visitor.visitStopSlave(context);
    System.out.print(stopSlave);
  }

  @Test
  public void testStartGroupReplication() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    StartGroupReplicationContext context = parser.startGroupReplication();
    Object startGroupReplication = visitor.visitStartGroupReplication(context);
    System.out.print(startGroupReplication);
  }

  @Test
  public void testStopGroupReplication() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    StopGroupReplicationContext context = parser.stopGroupReplication();
    Object stopGroupReplication = visitor.visitStopGroupReplication(context);
    System.out.print(stopGroupReplication);
  }

  // @Test
  // public void testMasterOption() {
  // final String input = "";
  // MySqlParser parser = this.constructParser(input);
  //
  // RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
  // MasterOptionContext context = parser.masterOption();
  // Object masterOption = visitor.visitMasterOption(context);
  // System.out.print(masterOption);
  // }

  @Test
  public void testStringMasterOption() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    StringMasterOptionContext context = parser.stringMasterOption();
    Object stringMasterOption = visitor.visitStringMasterOption(context);
    System.out.print(stringMasterOption);
  }

  @Test
  public void testDecimalMasterOption() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    DecimalMasterOptionContext context = parser.decimalMasterOption();
    Object decimalMasterOption = visitor.visitDecimalMasterOption(context);
    System.out.print(decimalMasterOption);
  }

  @Test
  public void testBoolMasterOption() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    BoolMasterOptionContext context = parser.boolMasterOption();
    Object boolMasterOption = visitor.visitBoolMasterOption(context);
    System.out.print(boolMasterOption);
  }

  @Test
  public void testChannelOption() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    ChannelOptionContext context = parser.channelOption();
    Object channelOption = visitor.visitChannelOption(context);
    System.out.print(channelOption);
  }

  // @Test
  // public void testReplicationFilter() {
  // final String input = "";
  // MySqlParser parser = this.constructParser(input);
  //
  // RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
  // ReplicationFilterContext context = parser.replicationFilter();
  // Object replicationFilter = visitor.visitReplicationFilter(context);
  // System.out.print(replicationFilter);
  // }

  @Test
  public void testTablePair() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    TablePairContext context = parser.tablePair();
    Object tablePair = visitor.visitTablePair(context);
    System.out.print(tablePair);
  }

  @Test
  public void testThreadType() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    ThreadTypeContext context = parser.threadType();
    Object threadType = visitor.visitThreadType(context);
    System.out.print(threadType);
  }

  // @Test
  // public void testUntilOption() {
  // final String input = "";
  // MySqlParser parser = this.constructParser(input);
  //
  // RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
  // UntilOptionContext context = parser.untilOption();
  // Object untilOption = visitor.visitUntilOption(context);
  // System.out.print(untilOption);
  // }

  // @Test
  // public void testConnectionOption() {
  // final String input = "";
  // MySqlParser parser = this.constructParser(input);
  //
  // RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
  // ConnectionOptionContext context = parser.connectionOption();
  // Object connectionOption = visitor.visitConnectionOption(context);
  // System.out.print(connectionOption);
  // }

  @Test
  public void testGtuidSet() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    GtuidSetContext context = parser.gtuidSet();
    Object gtuidSet = visitor.visitGtuidSet(context);
    System.out.print(gtuidSet);
  }

  @Test
  public void testXaStartTransaction() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    XaStartTransactionContext context = parser.xaStartTransaction();
    Object xaStartTransaction = visitor.visitXaStartTransaction(context);
    System.out.print(xaStartTransaction);
  }

  @Test
  public void testXaEndTransaction() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    XaEndTransactionContext context = parser.xaEndTransaction();
    Object xaEndTransaction = visitor.visitXaEndTransaction(context);
    System.out.print(xaEndTransaction);
  }

  @Test
  public void testXaPrepareStatement() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    XaPrepareStatementContext context = parser.xaPrepareStatement();
    Object xaPrepareStatement = visitor.visitXaPrepareStatement(context);
    System.out.print(xaPrepareStatement);
  }

  @Test
  public void testXaCommitWork() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    XaCommitWorkContext context = parser.xaCommitWork();
    Object xaCommitWork = visitor.visitXaCommitWork(context);
    System.out.print(xaCommitWork);
  }

  @Test
  public void testXaRollbackWork() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    XaRollbackWorkContext context = parser.xaRollbackWork();
    Object xaRollbackWork = visitor.visitXaRollbackWork(context);
    System.out.print(xaRollbackWork);
  }

  @Test
  public void testXaRecoverWork() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    XaRecoverWorkContext context = parser.xaRecoverWork();
    Object xaRecoverWork = visitor.visitXaRecoverWork(context);
    System.out.print(xaRecoverWork);
  }

  @Test
  public void testPrepareStatement() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    PrepareStatementContext context = parser.prepareStatement();
    Object prepareStatement = visitor.visitPrepareStatement(context);
    System.out.print(prepareStatement);
  }

  @Test
  public void testExecuteStatement() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    ExecuteStatementContext context = parser.executeStatement();
    Object executeStatement = visitor.visitExecuteStatement(context);
    System.out.print(executeStatement);
  }

  @Test
  public void testDeallocatePrepare() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    DeallocatePrepareContext context = parser.deallocatePrepare();
    Object deallocatePrepare = visitor.visitDeallocatePrepare(context);
    System.out.print(deallocatePrepare);
  }

  @Test
  public void testRoutineBody() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    RoutineBodyContext context = parser.routineBody();
    Object routineBody = visitor.visitRoutineBody(context);
    System.out.print(routineBody);
  }

  @Test
  public void testBlockStatement() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    BlockStatementContext context = parser.blockStatement();
    Object blockStatement = visitor.visitBlockStatement(context);
    System.out.print(blockStatement);
  }

  @Test
  public void testCaseStatement() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    CaseStatementContext context = parser.caseStatement();
    Object caseStatement = visitor.visitCaseStatement(context);
    System.out.print(caseStatement);
  }

  @Test
  public void testIfStatement() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    IfStatementContext context = parser.ifStatement();
    Object ifStatement = visitor.visitIfStatement(context);
    System.out.print(ifStatement);
  }

  @Test
  public void testIterateStatement() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    IterateStatementContext context = parser.iterateStatement();
    Object iterateStatement = visitor.visitIterateStatement(context);
    System.out.print(iterateStatement);
  }

  @Test
  public void testLeaveStatement() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    LeaveStatementContext context = parser.leaveStatement();
    Object leaveStatement = visitor.visitLeaveStatement(context);
    System.out.print(leaveStatement);
  }

  @Test
  public void testLoopStatement() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    LoopStatementContext context = parser.loopStatement();
    Object loopStatement = visitor.visitLoopStatement(context);
    System.out.print(loopStatement);
  }

  @Test
  public void testRepeatStatement() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    RepeatStatementContext context = parser.repeatStatement();
    Object repeatStatement = visitor.visitRepeatStatement(context);
    System.out.print(repeatStatement);
  }

  @Test
  public void testReturnStatement() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    ReturnStatementContext context = parser.returnStatement();
    Object returnStatement = visitor.visitReturnStatement(context);
    System.out.print(returnStatement);
  }

  @Test
  public void testWhileStatement() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    WhileStatementContext context = parser.whileStatement();
    Object whileStatement = visitor.visitWhileStatement(context);
    System.out.print(whileStatement);
  }

  // @Test
  // public void testCursorStatement() {
  // final String input = "";
  // MySqlParser parser = this.constructParser(input);
  //
  // RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
  // CursorStatementContext context = parser.cursorStatement();
  // Object cursorStatement = visitor.visitCursorStatement(context);
  // System.out.print(cursorStatement);
  // }

  @Test
  public void testDeclareVariable() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    DeclareVariableContext context = parser.declareVariable();
    Object declareVariable = visitor.visitDeclareVariable(context);
    System.out.print(declareVariable);
  }

  @Test
  public void testDeclareCondition() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    DeclareConditionContext context = parser.declareCondition();
    Object declareCondition = visitor.visitDeclareCondition(context);
    System.out.print(declareCondition);
  }

  @Test
  public void testDeclareCursor() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    DeclareCursorContext context = parser.declareCursor();
    Object declareCursor = visitor.visitDeclareCursor(context);
    System.out.print(declareCursor);
  }

  @Test
  public void testDeclareHandler() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    DeclareHandlerContext context = parser.declareHandler();
    Object declareHandler = visitor.visitDeclareHandler(context);
    System.out.print(declareHandler);
  }

  // @Test
  // public void testHandlerConditionValue() {
  // final String input = "";
  // MySqlParser parser = this.constructParser(input);
  //
  // RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
  // HandlerConditionValueContext context = parser.handlerConditionValue();
  // Object handlerConditionValue = visitor.visitHandlerConditionValue(context);
  // System.out.print(handlerConditionValue);
  // }

  @Test
  public void testProcedureSqlStatement() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    ProcedureSqlStatementContext context = parser.procedureSqlStatement();
    Object procedureSqlStatement = visitor.visitProcedureSqlStatement(context);
    System.out.print(procedureSqlStatement);
  }

  @Test
  public void testCaseAlternative() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    CaseAlternativeContext context = parser.caseAlternative();
    Object caseAlternative = visitor.visitCaseAlternative(context);
    System.out.print(caseAlternative);
  }

  @Test
  public void testElifAlternative() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    ElifAlternativeContext context = parser.elifAlternative();
    Object elifAlternative = visitor.visitElifAlternative(context);
    System.out.print(elifAlternative);
  }

  // @Test
  // public void testAlterUser() {
  // final String input = "";
  // MySqlParser parser = this.constructParser(input);
  //
  // RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
  // AlterUserContext context = parser.alterUser();
  // Object alterUser = visitor.visitAlterUser(context);
  // System.out.print(alterUser);
  // }

  // @Test
  // public void testCreateUser() {
  // final String input = "";
  // MySqlParser parser = this.constructParser(input);
  //
  // RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
  // CreateUserContext context = parser.createUser();
  // Object createUser = visitor.visitCreateUser(context);
  // System.out.print(createUser);
  // }

  @Test
  public void testDropUser() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    DropUserContext context = parser.dropUser();
    Object dropUser = visitor.visitDropUser(context);
    System.out.print(dropUser);
  }

  @Test
  public void testGrantStatement() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    GrantStatementContext context = parser.grantStatement();
    Object grantStatement = visitor.visitGrantStatement(context);
    System.out.print(grantStatement);
  }

  @Test
  public void testGrantProxy() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    GrantProxyContext context = parser.grantProxy();
    Object grantProxy = visitor.visitGrantProxy(context);
    System.out.print(grantProxy);
  }

  @Test
  public void testRenameUser() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    RenameUserContext context = parser.renameUser();
    Object renameUser = visitor.visitRenameUser(context);
    System.out.print(renameUser);
  }

  // @Test
  // public void testRevokeStatement() {
  // final String input = "";
  // MySqlParser parser = this.constructParser(input);
  //
  // RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
  // RevokeStatementContext context = parser.revokeStatement();
  // Object revokeStatement = visitor.visitRevokeStatement(context);
  // System.out.print(revokeStatement);
  // }

  @Test
  public void testRevokeProxy() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    RevokeProxyContext context = parser.revokeProxy();
    Object revokeProxy = visitor.visitRevokeProxy(context);
    System.out.print(revokeProxy);
  }

  @Test
  public void testSetPasswordStatement() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    SetPasswordStatementContext context = parser.setPasswordStatement();
    Object setPasswordStatement = visitor.visitSetPasswordStatement(context);
    System.out.print(setPasswordStatement);
  }

  @Test
  public void testUserSpecification() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    UserSpecificationContext context = parser.userSpecification();
    Object userSpecification = visitor.visitUserSpecification(context);
    System.out.print(userSpecification);
  }

  // @Test
  // public void testUserAuthOption() {
  // final String input = "";
  // MySqlParser parser = this.constructParser(input);
  //
  // RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
  // UserAuthOptionContext context = parser.userAuthOption();
  // Object userAuthOption = visitor.visitUserAuthOption(context);
  // System.out.print(userAuthOption);
  // }

  @Test
  public void testTlsOption() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    TlsOptionContext context = parser.tlsOption();
    Object tlsOption = visitor.visitTlsOption(context);
    System.out.print(tlsOption);
  }

  @Test
  public void testUserResourceOption() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    UserResourceOptionContext context = parser.userResourceOption();
    Object userResourceOption = visitor.visitUserResourceOption(context);
    System.out.print(userResourceOption);
  }

  @Test
  public void testUserPasswordOption() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    UserPasswordOptionContext context = parser.userPasswordOption();
    Object userPasswordOption = visitor.visitUserPasswordOption(context);
    System.out.print(userPasswordOption);
  }

  @Test
  public void testUserLockOption() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    UserLockOptionContext context = parser.userLockOption();
    Object userLockOption = visitor.visitUserLockOption(context);
    System.out.print(userLockOption);
  }

  @Test
  public void testPrivelegeClause() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    PrivelegeClauseContext context = parser.privelegeClause();
    Object privelegeClause = visitor.visitPrivelegeClause(context);
    System.out.print(privelegeClause);
  }

  @Test
  public void testPrivilege() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    PrivilegeContext context = parser.privilege();
    Object privilege = visitor.visitPrivilege(context);
    System.out.print(privilege);
  }

  // @Test
  // public void testPrivilegeLevel() {
  // final String input = "";
  // MySqlParser parser = this.constructParser(input);
  //
  // RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
  // PrivilegeLevelContext context = parser.privilegeLevel();
  // Object privilegeLevel = visitor.visitPrivilegeLevel(context);
  // System.out.print(privilegeLevel);
  // }

  @Test
  public void testRenameUserClause() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    RenameUserClauseContext context = parser.renameUserClause();
    Object renameUserClause = visitor.visitRenameUserClause(context);
    System.out.print(renameUserClause);
  }

  @Test
  public void testAnalyzeTable() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    AnalyzeTableContext context = parser.analyzeTable();
    Object analyzeTable = visitor.visitAnalyzeTable(context);
    System.out.print(analyzeTable);
  }

  @Test
  public void testCheckTable() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    CheckTableContext context = parser.checkTable();
    Object checkTable = visitor.visitCheckTable(context);
    System.out.print(checkTable);
  }

  @Test
  public void testChecksumTable() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    ChecksumTableContext context = parser.checksumTable();
    Object checksumTable = visitor.visitChecksumTable(context);
    System.out.print(checksumTable);
  }

  @Test
  public void testOptimizeTable() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    OptimizeTableContext context = parser.optimizeTable();
    Object optimizeTable = visitor.visitOptimizeTable(context);
    System.out.print(optimizeTable);
  }

  @Test
  public void testRepairTable() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    RepairTableContext context = parser.repairTable();
    Object repairTable = visitor.visitRepairTable(context);
    System.out.print(repairTable);
  }

  @Test
  public void testCheckTableOption() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    CheckTableOptionContext context = parser.checkTableOption();
    Object checkTableOption = visitor.visitCheckTableOption(context);
    System.out.print(checkTableOption);
  }

  @Test
  public void testCreateUdfunction() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    CreateUdfunctionContext context = parser.createUdfunction();
    Object createUdfunction = visitor.visitCreateUdfunction(context);
    System.out.print(createUdfunction);
  }

  @Test
  public void testInstallPlugin() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    InstallPluginContext context = parser.installPlugin();
    Object installPlugin = visitor.visitInstallPlugin(context);
    System.out.print(installPlugin);
  }

  @Test
  public void testUninstallPlugin() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    UninstallPluginContext context = parser.uninstallPlugin();
    Object uninstallPlugin = visitor.visitUninstallPlugin(context);
    System.out.print(uninstallPlugin);
  }

  // @Test
  // public void testSetStatement() {
  // final String input = "";
  // MySqlParser parser = this.constructParser(input);
  //
  // RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
  // SetStatementContext context = parser.setStatement();
  // Object setStatement = visitor.visitSetStatement(context);
  // System.out.print(setStatement);
  // }

  // @Test
  // public void testShowStatement() {
  // final String input = "";
  // MySqlParser parser = this.constructParser(input);
  //
  // RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
  // ShowStatementContext context = parser.showStatement();
  // Object showStatement = visitor.visitShowStatement(context);
  // System.out.print(showStatement);
  // }

  @Test
  public void testVariableClause() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    VariableClauseContext context = parser.variableClause();
    Object variableClause = visitor.visitVariableClause(context);
    System.out.print(variableClause);
  }

  @Test
  public void testShowCommonEntity() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    ShowCommonEntityContext context = parser.showCommonEntity();
    Object showCommonEntity = visitor.visitShowCommonEntity(context);
    System.out.print(showCommonEntity);
  }

  @Test
  public void testShowFilter() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    ShowFilterContext context = parser.showFilter();
    Object showFilter = visitor.visitShowFilter(context);
    System.out.print(showFilter);
  }

  @Test
  public void testShowGlobalInfoClause() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    ShowGlobalInfoClauseContext context = parser.showGlobalInfoClause();
    Object showGlobalInfoClause = visitor.visitShowGlobalInfoClause(context);
    System.out.print(showGlobalInfoClause);
  }

  @Test
  public void testShowSchemaEntity() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    ShowSchemaEntityContext context = parser.showSchemaEntity();
    Object showSchemaEntity = visitor.visitShowSchemaEntity(context);
    System.out.print(showSchemaEntity);
  }

  @Test
  public void testShowProfileType() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    ShowProfileTypeContext context = parser.showProfileType();
    Object showProfileType = visitor.visitShowProfileType(context);
    System.out.print(showProfileType);
  }

  @Test
  public void testBinlogStatement() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    BinlogStatementContext context = parser.binlogStatement();
    Object binlogStatement = visitor.visitBinlogStatement(context);
    System.out.print(binlogStatement);
  }

  @Test
  public void testCacheIndexStatement() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    CacheIndexStatementContext context = parser.cacheIndexStatement();
    Object cacheIndexStatement = visitor.visitCacheIndexStatement(context);
    System.out.print(cacheIndexStatement);
  }

  @Test
  public void testFlushStatement() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    FlushStatementContext context = parser.flushStatement();
    Object flushStatement = visitor.visitFlushStatement(context);
    System.out.print(flushStatement);
  }

  @Test
  public void testKillStatement() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    KillStatementContext context = parser.killStatement();
    Object killStatement = visitor.visitKillStatement(context);
    System.out.print(killStatement);
  }

  @Test
  public void testLoadIndexIntoCache() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    LoadIndexIntoCacheContext context = parser.loadIndexIntoCache();
    Object loadIndexIntoCache = visitor.visitLoadIndexIntoCache(context);
    System.out.print(loadIndexIntoCache);
  }

  @Test
  public void testResetStatement() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    ResetStatementContext context = parser.resetStatement();
    Object resetStatement = visitor.visitResetStatement(context);
    System.out.print(resetStatement);
  }

  @Test
  public void testShutdownStatement() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    ShutdownStatementContext context = parser.shutdownStatement();
    Object shutdownStatement = visitor.visitShutdownStatement(context);
    System.out.print(shutdownStatement);
  }

  @Test
  public void testTableIndexes() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    TableIndexesContext context = parser.tableIndexes();
    Object tableIndexes = visitor.visitTableIndexes(context);
    System.out.print(tableIndexes);
  }

  // @Test
  // public void testFlushOption() {
  // final String input = "";
  // MySqlParser parser = this.constructParser(input);
  //
  // RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
  // FlushOptionContext context = parser.flushOption();
  // Object flushOption = visitor.visitFlushOption(context);
  // System.out.print(flushOption);
  // }

  @Test
  public void testFlushTableOption() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    FlushTableOptionContext context = parser.flushTableOption();
    Object flushTableOption = visitor.visitFlushTableOption(context);
    System.out.print(flushTableOption);
  }

  @Test
  public void testLoadedTableIndexes() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    LoadedTableIndexesContext context = parser.loadedTableIndexes();
    Object loadedTableIndexes = visitor.visitLoadedTableIndexes(context);
    System.out.print(loadedTableIndexes);
  }

  @Test
  public void testSimpleDescribeStatement() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    SimpleDescribeStatementContext context = parser.simpleDescribeStatement();
    Object simpleDescribeStatement = visitor.visitSimpleDescribeStatement(context);
    System.out.print(simpleDescribeStatement);
  }

  @Test
  public void testFullDescribeStatement() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    FullDescribeStatementContext context = parser.fullDescribeStatement();
    Object fullDescribeStatement = visitor.visitFullDescribeStatement(context);
    System.out.print(fullDescribeStatement);
  }

  @Test
  public void testHelpStatement() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    HelpStatementContext context = parser.helpStatement();
    Object helpStatement = visitor.visitHelpStatement(context);
    System.out.print(helpStatement);
  }

  @Test
  public void testUseStatement() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    UseStatementContext context = parser.useStatement();
    Object useStatement = visitor.visitUseStatement(context);
    System.out.print(useStatement);
  }

  // @Test
  // public void testDescribeObjectClause() {
  // final String input = "";
  // MySqlParser parser = this.constructParser(input);
  //
  // RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
  // DescribeObjectClauseContext context = parser.describeObjectClause();
  // Object describeObjectClause = visitor.visitDescribeObjectClause(context);
  // System.out.print(describeObjectClause);
  // }

  @Test
  public void testFullId() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    FullIdContext context = parser.fullId();
    Object fullId = visitor.visitFullId(context);
    System.out.print(fullId);
  }

  @Test
  public void testTableName() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    TableNameContext context = parser.tableName();
    Object tableName = visitor.visitTableName(context);
    System.out.print(tableName);
  }

  @Test
  public void testFullColumnName() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    FullColumnNameContext context = parser.fullColumnName();
    Object fullColumnName = visitor.visitFullColumnName(context);
    System.out.print(fullColumnName);
  }

  @Test
  public void testIndexColumnName() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    IndexColumnNameContext context = parser.indexColumnName();
    Object indexColumnName = visitor.visitIndexColumnName(context);
    System.out.print(indexColumnName);
  }

  @Test
  public void testUserName() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    UserNameContext context = parser.userName();
    Object userName = visitor.visitUserName(context);
    System.out.print(userName);
  }

  @Test
  public void testMysqlVariable() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    MysqlVariableContext context = parser.mysqlVariable();
    Object mysqlVariable = visitor.visitMysqlVariable(context);
    System.out.print(mysqlVariable);
  }

  @Test
  public void testCharsetName() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    CharsetNameContext context = parser.charsetName();
    Object charsetName = visitor.visitCharsetName(context);
    System.out.print(charsetName);
  }

  @Test
  public void testCollationName() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    CollationNameContext context = parser.collationName();
    Object collationName = visitor.visitCollationName(context);
    System.out.print(collationName);
  }

  @Test
  public void testEngineName() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    EngineNameContext context = parser.engineName();
    Object engineName = visitor.visitEngineName(context);
    System.out.print(engineName);
  }

  @Test
  public void testUuidSet() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    UuidSetContext context = parser.uuidSet();
    Object uuidSet = visitor.visitUuidSet(context);
    System.out.print(uuidSet);
  }

  @Test
  public void testXid() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    XidContext context = parser.xid();
    Object xid = visitor.visitXid(context);
    System.out.print(xid);
  }

  @Test
  public void testXuidStringId() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    XuidStringIdContext context = parser.xuidStringId();
    Object xuidStringId = visitor.visitXuidStringId(context);
    System.out.print(xuidStringId);
  }

  @Test
  public void testAuthPlugin() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    AuthPluginContext context = parser.authPlugin();
    Object authPlugin = visitor.visitAuthPlugin(context);
    System.out.print(authPlugin);
  }

  @Test
  public void testUid() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    UidContext context = parser.uid();
    Object uid = visitor.visitUid(context);
    System.out.print(uid);
  }

  @Test
  public void testSimpleId() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    SimpleIdContext context = parser.simpleId();
    Object simpleId = visitor.visitSimpleId(context);
    System.out.print(simpleId);
  }

  @Test
  public void testDottedId() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    DottedIdContext context = parser.dottedId();
    Object dottedId = visitor.visitDottedId(context);
    System.out.print(dottedId);
  }

  @Test
  public void testDecimalLiteral() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    DecimalLiteralContext context = parser.decimalLiteral();
    Object decimalLiteral = visitor.visitDecimalLiteral(context);
    System.out.print(decimalLiteral);
  }

  @Test
  public void testFileSizeLiteral() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    FileSizeLiteralContext context = parser.fileSizeLiteral();
    Object fileSizeLiteral = visitor.visitFileSizeLiteral(context);
    System.out.print(fileSizeLiteral);
  }

  @Test
  public void testStringLiteral() {
    final String input = "`hello`";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    StringLiteralContext context = parser.stringLiteral();
    Object stringLiteral = visitor.visitStringLiteral(context);
    System.out.print(stringLiteral);
  }

  @Test
  public void testBooleanLiteral() {
    final String input = "true";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    BooleanLiteralContext context = parser.booleanLiteral();
    Object booleanLiteral = visitor.visitBooleanLiteral(context);
    System.out.print(booleanLiteral);
  }

  @Test
  public void testHexadecimalLiteral() {
    final String input = "0x31415926";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    HexadecimalLiteralContext context = parser.hexadecimalLiteral();
    Object hexadecimalLiteral = visitor.visitHexadecimalLiteral(context);
    System.out.print(hexadecimalLiteral);
  }

  @Test
  public void testNullNotnull() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    NullNotnullContext context = parser.nullNotnull();
    Object nullNotnull = visitor.visitNullNotnull(context);
    System.out.print(nullNotnull);
  }

  @Test
  public void testConstant() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    ConstantContext context = parser.constant();
    Object constant = visitor.visitConstant(context);
    System.out.print(constant);
  }

  // @Test
  // public void testDataType() {
  // final String input = "";
  // MySqlParser parser = this.constructParser(input);
  //
  // RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
  // DataTypeContext context = parser.dataType();
  // Object dataType = visitor.visitDataType(context);
  // System.out.print(dataType);
  // }

  @Test
  public void testCollectionOptions() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    CollectionOptionsContext context = parser.collectionOptions();
    Object collectionOptions = visitor.visitCollectionOptions(context);
    System.out.print(collectionOptions);
  }

  @Test
  public void testConvertedDataType() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    ConvertedDataTypeContext context = parser.convertedDataType();
    Object convertedDataType = visitor.visitConvertedDataType(context);
    System.out.print(convertedDataType);
  }

  @Test
  public void testLengthOneDimension() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    LengthOneDimensionContext context = parser.lengthOneDimension();
    Object lengthOneDimension = visitor.visitLengthOneDimension(context);
    System.out.print(lengthOneDimension);
  }

  @Test
  public void testLengthTwoDimension() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    LengthTwoDimensionContext context = parser.lengthTwoDimension();
    Object lengthTwoDimension = visitor.visitLengthTwoDimension(context);
    System.out.print(lengthTwoDimension);
  }

  @Test
  public void testLengthTwoOptionalDimension() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    LengthTwoOptionalDimensionContext context = parser.lengthTwoOptionalDimension();
    Object lengthTwoOptionalDimension = visitor.visitLengthTwoOptionalDimension(context);
    System.out.print(lengthTwoOptionalDimension);
  }

  @Test
  public void testUidList() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    UidListContext context = parser.uidList();
    Object uidList = visitor.visitUidList(context);
    System.out.print(uidList);
  }

  @Test
  public void testTables() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    TablesContext context = parser.tables();
    Object tables = visitor.visitTables(context);
    System.out.print(tables);
  }

  @Test
  public void testIndexColumnNames() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    IndexColumnNamesContext context = parser.indexColumnNames();
    Object indexColumnNames = visitor.visitIndexColumnNames(context);
    System.out.print(indexColumnNames);
  }

  @Test
  public void testExpressions() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    ExpressionsContext context = parser.expressions();
    Object expressions = visitor.visitExpressions(context);
    System.out.print(expressions);
  }

  @Test
  public void testExpressionsWithDefaults() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    ExpressionsWithDefaultsContext context = parser.expressionsWithDefaults();
    Object expressionsWithDefaults = visitor.visitExpressionsWithDefaults(context);
    System.out.print(expressionsWithDefaults);
  }

  @Test
  public void testConstants() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    ConstantsContext context = parser.constants();
    Object constants = visitor.visitConstants(context);
    System.out.print(constants);
  }

  @Test
  public void testSimpleStrings() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    SimpleStringsContext context = parser.simpleStrings();
    Object simpleStrings = visitor.visitSimpleStrings(context);
    System.out.print(simpleStrings);
  }

  @Test
  public void testUserVariables() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    UserVariablesContext context = parser.userVariables();
    Object userVariables = visitor.visitUserVariables(context);
    System.out.print(userVariables);
  }

  @Test
  public void testDefaultValue() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    DefaultValueContext context = parser.defaultValue();
    Object defaultValue = visitor.visitDefaultValue(context);
    System.out.print(defaultValue);
  }

  @Test
  public void testCurrentTimestamp() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    CurrentTimestampContext context = parser.currentTimestamp();
    Object currentTimestamp = visitor.visitCurrentTimestamp(context);
    System.out.print(currentTimestamp);
  }

  @Test
  public void testExpressionOrDefault() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    ExpressionOrDefaultContext context = parser.expressionOrDefault();
    Object expressionOrDefault = visitor.visitExpressionOrDefault(context);
    System.out.print(expressionOrDefault);
  }

  @Test
  public void testIfExists() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    IfExistsContext context = parser.ifExists();
    Object ifExists = visitor.visitIfExists(context);
    System.out.print(ifExists);
  }

  @Test
  public void testIfNotExists() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    IfNotExistsContext context = parser.ifNotExists();
    Object ifNotExists = visitor.visitIfNotExists(context);
    System.out.print(ifNotExists);
  }

  // @Test
  // public void testFunctionCall() {
  // final String input = "";
  // MySqlParser parser = this.constructParser(input);
  //
  // RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
  // FunctionCallContext context = parser.functionCall();
  // Object functionCall = visitor.visitFunctionCall(context);
  // System.out.print(functionCall);
  // }

  // @Test
  // public void testSpecificFunction() {
  // final String input = "";
  // MySqlParser parser = this.constructParser(input);
  //
  // RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
  // SpecificFunctionContext context = parser.specificFunction();
  // Object specificFunction = visitor.visitSpecificFunction(context);
  // System.out.print(specificFunction);
  // }

  @Test
  public void testCaseFuncAlternative() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    CaseFuncAlternativeContext context = parser.caseFuncAlternative();
    Object caseFuncAlternative = visitor.visitCaseFuncAlternative(context);
    System.out.print(caseFuncAlternative);
  }

  // @Test
  // public void testLevelsInWeightString() {
  // final String input = "";
  // MySqlParser parser = this.constructParser(input);
  //
  // RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
  // LevelsInWeightStringContext context = parser.levelsInWeightString();
  // Object levelsInWeightString = visitor.visitLevelsInWeightString(context);
  // System.out.print(levelsInWeightString);
  // }

  @Test
  public void testLevelInWeightListElement() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    LevelInWeightListElementContext context = parser.levelInWeightListElement();
    Object levelInWeightListElement = visitor.visitLevelInWeightListElement(context);
    System.out.print(levelInWeightListElement);
  }

  @Test
  public void testAggregateWindowedFunction() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    AggregateWindowedFunctionContext context = parser.aggregateWindowedFunction();
    Object aggregateWindowedFunction = visitor.visitAggregateWindowedFunction(context);
    System.out.print(aggregateWindowedFunction);
  }

  @Test
  public void testScalarFunctionName() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    ScalarFunctionNameContext context = parser.scalarFunctionName();
    Object scalarFunctionName = visitor.visitScalarFunctionName(context);
    System.out.print(scalarFunctionName);
  }

  @Test
  public void testPasswordFunctionClause() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    PasswordFunctionClauseContext context = parser.passwordFunctionClause();
    Object passwordFunctionClause = visitor.visitPasswordFunctionClause(context);
    System.out.print(passwordFunctionClause);
  }

  @Test
  public void testFunctionArgs() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    FunctionArgsContext context = parser.functionArgs();
    Object functionArgs = visitor.visitFunctionArgs(context);
    System.out.print(functionArgs);
  }

  @Test
  public void testFunctionArg() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    FunctionArgContext context = parser.functionArg();
    Object functionArg = visitor.visitFunctionArg(context);
    System.out.print(functionArg);
  }

  @Test
  public void testExpression() {
    final String input = "Movies.length >= 100";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    ExpressionContext context = parser.expression();
    Object expression = visitor.visitExpression(context);
    System.out.print(expression);
  }

  // @Test
  // public void testPredicate() {
  // final String input = "";
  // MySqlParser parser = this.constructParser(input);
  //
  // RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
  // PredicateContext context = parser.predicate();
  // Object predicate = visitor.visitPredicate(context);
  // System.out.print(predicate);
  // }

  // @Test
  // public void testExpressionAtom() {
  // final String input = "";
  // MySqlParser parser = this.constructParser(input);
  //
  // RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
  // ExpressionAtomContext context = parser.expressionAtom();
  // Object expressionAtom = visitor.visitExpressionAtom(context);
  // System.out.print(expressionAtom);
  // }

  @Test
  public void testUnaryOperator() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    UnaryOperatorContext context = parser.unaryOperator();
    Object unaryOperator = visitor.visitUnaryOperator(context);
    System.out.print(unaryOperator);
  }

  @Test
  public void testComparisonOperator() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    ComparisonOperatorContext context = parser.comparisonOperator();
    Object comparisonOperator = visitor.visitComparisonOperator(context);
    System.out.print(comparisonOperator);
  }

  @Test
  public void testLogicalOperator() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    LogicalOperatorContext context = parser.logicalOperator();
    Object logicalOperator = visitor.visitLogicalOperator(context);
    System.out.print(logicalOperator);
  }

  @Test
  public void testBitOperator() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    BitOperatorContext context = parser.bitOperator();
    Object bitOperator = visitor.visitBitOperator(context);
    System.out.print(bitOperator);
  }

  @Test
  public void testMathOperator() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    MathOperatorContext context = parser.mathOperator();
    Object mathOperator = visitor.visitMathOperator(context);
    System.out.print(mathOperator);
  }

  @Test
  public void testCharsetNameBase() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    CharsetNameBaseContext context = parser.charsetNameBase();
    Object charsetNameBase = visitor.visitCharsetNameBase(context);
    System.out.print(charsetNameBase);
  }

  @Test
  public void testTransactionLevelBase() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    TransactionLevelBaseContext context = parser.transactionLevelBase();
    Object transactionLevelBase = visitor.visitTransactionLevelBase(context);
    System.out.print(transactionLevelBase);
  }

  @Test
  public void testPrivilegesBase() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    PrivilegesBaseContext context = parser.privilegesBase();
    Object privilegesBase = visitor.visitPrivilegesBase(context);
    System.out.print(privilegesBase);
  }

  @Test
  public void testIntervalTypeBase() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    IntervalTypeBaseContext context = parser.intervalTypeBase();
    Object intervalTypeBase = visitor.visitIntervalTypeBase(context);
    System.out.print(intervalTypeBase);
  }

  @Test
  public void testDataTypeBase() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    DataTypeBaseContext context = parser.dataTypeBase();
    Object dataTypeBase = visitor.visitDataTypeBase(context);
    System.out.print(dataTypeBase);
  }

  @Test
  public void testKeywordsCanBeId() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    KeywordsCanBeIdContext context = parser.keywordsCanBeId();
    Object keywordsCanBeId = visitor.visitKeywordsCanBeId(context);
    System.out.print(keywordsCanBeId);
  }

  @Test
  public void testFunctionNameBase() {
    final String input = "";
    MySqlParser parser = this.constructParser(input);

    RelationalAlgebraMySqlParserVisitor visitor = new RelationalAlgebraMySqlParserVisitor();
    FunctionNameBaseContext context = parser.functionNameBase();
    Object functionNameBase = visitor.visitFunctionNameBase(context);
    System.out.print(functionNameBase);
  }

}
