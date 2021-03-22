package com.spike.giantdataanalysis.rdfstore.sql;

import java.util.List;

import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import com.spike.giantdataanalysis.rdfstore.sql.MySqlParser.*;

/**
 * MySQL的SQL解析器Visitor的简单实现: 输出至控制台.
 */
public class SimpleMySqlParserVisitor implements MySqlParserVisitor<Void> {

  @Override
  public Void visit(ParseTree tree) {
    throw new UnsupportedOperationException("call more specific methods!");
  }

  @Override
  public Void visitChildren(RuleNode node) {
    throw new UnsupportedOperationException("call more specific methods!");
  }

  @Override
  public Void visitTerminal(TerminalNode node) {
    if (node == null) {
      return null;
    }

    System.err.print(node + " ");
    return null;
  }

  @Override
  public Void visitErrorNode(ErrorNode node) {
    if (node == null) {
      return null;
    }

    System.err.print(node + " ");
    return null;
  }

  @Override
  public Void visitRoot(RootContext ctx) {
    if (ctx == null) {
      return null;
    }

    this.visitSqlStatements(ctx.sqlStatements());
    return null;
  }

  @Override
  public Void visitSqlStatements(SqlStatementsContext ctx) {
    if (ctx == null) {
      return null;
    }

    int sqlStatementCount = ctx.getChildCount();
    if (sqlStatementCount == 0) {
      return null;
    }

    int stmt_count = 1;
    for (int i = 0; i < sqlStatementCount; i++) {

      ParseTree child = ctx.getChild(i);
      if (child instanceof SqlStatementContext) {
        System.err.print("\n语句" + stmt_count++ + ": ");
        this.visitSqlStatement((SqlStatementContext) child);
      } else if (child instanceof TerminalNode) {
        this.visitTerminal((TerminalNode) child);
      } else if (child instanceof EmptyStatementContext) {
        this.visitEmptyStatement((EmptyStatementContext) child);
      } else {
        throw new UnsupportedOperationException();
      }
    }

    return null;
  }

  @Override
  public Void visitSqlStatement(SqlStatementContext ctx) {
    if (ctx == null) {
      return null;
    }

    int childCount = ctx.getChildCount();
    if (childCount == 0) {
      return null;
    }

    ParseTree child0 = ctx.getChild(0);

    if (child0 instanceof DdlStatementContext) {
      this.visitDdlStatement(ctx.ddlStatement());
    } else if (child0 instanceof DmlStatementContext) {
      this.visitDmlStatement(ctx.dmlStatement());
    } else if (child0 instanceof TransactionStatementContext) {
      this.visitTransactionStatement(ctx.transactionStatement());
    } else if (child0 instanceof ReplicationStatementContext) {
      this.visitReplicationStatement(ctx.replicationStatement());
    } else if (child0 instanceof PreparedStatementContext) {
      this.visitPreparedStatement(ctx.preparedStatement());
    } else if (child0 instanceof AdministrationStatementContext) {
      this.visitAdministrationStatement(ctx.administrationStatement());
    } else if (child0 instanceof UtilityStatementContext) {
      this.visitUtilityStatement(ctx.utilityStatement());
    } else {
      throw new UnsupportedOperationException();
    }

    return null;
  }

  @Override
  public Void visitEmptyStatement(EmptyStatementContext ctx) {
    if (ctx == null) {
      return null;
    }

    this.visitTerminal(ctx.SEMI());
    return null;
  }

  @Override
  public Void visitDdlStatement(DdlStatementContext ctx) {
    ParseTree child0 = ctx.getChild(0);

    if (child0 instanceof CreateDatabaseContext) {
      throw new UnsupportedOperationException();
    } else if (child0 instanceof CreateEventContext) {
      throw new UnsupportedOperationException();
    } else if (child0 instanceof CreateIndexContext) {
      throw new UnsupportedOperationException();
    } else if (child0 instanceof CreateLogfileGroupContext) {
      throw new UnsupportedOperationException();
    } else if (child0 instanceof CreateProcedureContext) {
      throw new UnsupportedOperationException();
    } else if (child0 instanceof CreateFunctionContext) {
      throw new UnsupportedOperationException();
    } else if (child0 instanceof CreateServerContext) {
      throw new UnsupportedOperationException();
    } else if (child0 instanceof CreateTableContext) {
      this.visitCreateTable((CreateTableContext) child0);
    } else if (child0 instanceof CreateTablespaceInnodbContext) {
      throw new UnsupportedOperationException();
    } else if (child0 instanceof CreateTablespaceNdbContext) {
      throw new UnsupportedOperationException();
    } else if (child0 instanceof CreateTriggerContext) {
      throw new UnsupportedOperationException();
    } else if (child0 instanceof CreateViewContext) {
      throw new UnsupportedOperationException();
    } else if (child0 instanceof AlterDatabaseContext) {
      throw new UnsupportedOperationException();
    } else if (child0 instanceof AlterEventContext) {
      throw new UnsupportedOperationException();
    } else if (child0 instanceof AlterFunctionContext) {
      throw new UnsupportedOperationException();
    } else if (child0 instanceof AlterInstanceContext) {
      throw new UnsupportedOperationException();
    } else if (child0 instanceof AlterLogfileGroupContext) {
      throw new UnsupportedOperationException();
    } else if (child0 instanceof AlterProcedureContext) {
      throw new UnsupportedOperationException();
    } else if (child0 instanceof AlterServerContext) {
      throw new UnsupportedOperationException();
    } else if (child0 instanceof AlterTableContext) {
      this.visitAlterTable(ctx.alterTable());
    } else if (child0 instanceof AlterTablespaceContext) {
      throw new UnsupportedOperationException();
    } else if (child0 instanceof AlterViewContext) {
      throw new UnsupportedOperationException();
    } else if (child0 instanceof DropDatabaseContext) {
      throw new UnsupportedOperationException();
    } else if (child0 instanceof DropEventContext) {
      throw new UnsupportedOperationException();
    } else if (child0 instanceof DropIndexContext) {
      throw new UnsupportedOperationException();
    } else if (child0 instanceof DropLogfileGroupContext) {
      throw new UnsupportedOperationException();
    } else if (child0 instanceof DropProcedureContext) {
      throw new UnsupportedOperationException();
    } else if (child0 instanceof DropFunctionContext) {
      throw new UnsupportedOperationException();
    } else if (child0 instanceof DropServerContext) {
      throw new UnsupportedOperationException();
    } else if (child0 instanceof DropTableContext) {
      throw new UnsupportedOperationException();
    } else if (child0 instanceof DropTablespaceContext) {
      throw new UnsupportedOperationException();
    } else if (child0 instanceof DropTriggerContext) {
      throw new UnsupportedOperationException();
    } else if (child0 instanceof DropViewContext) {
      throw new UnsupportedOperationException();
    } else if (child0 instanceof RenameTableContext) {
      throw new UnsupportedOperationException();
    } else if (child0 instanceof TruncateTableContext) {
      throw new UnsupportedOperationException();
    } else {
      throw new UnsupportedOperationException();
    }

    return null;
  }

  @Override
  public Void visitDmlStatement(DmlStatementContext ctx) {
    if (ctx == null) {
      return null;
    }

    ParseTree child0 = ctx.getChild(0);

    if (child0 instanceof SelectStatementContext) {
      this.visitSelectStatement((SelectStatementContext) child0);
    } else if (child0 instanceof InsertStatementContext) {
      this.visitInsertStatement((InsertStatementContext) child0);
    } else if (child0 instanceof UpdateStatementContext) {
      this.visitUpdateStatement((UpdateStatementContext) child0);
    } else if (child0 instanceof DeleteStatementContext) {
      this.visitDeleteStatement((DeleteStatementContext) child0);
    } else if (child0 instanceof ReplaceStatementContext) {
      this.visitReplaceStatement((ReplaceStatementContext) child0);
    } else if (child0 instanceof CallStatementContext) {
      this.visitCallStatement((CallStatementContext) child0);
    } else if (child0 instanceof LoadDataStatementContext) {
      this.visitLoadDataStatement((LoadDataStatementContext) child0);
    } else if (child0 instanceof LoadXmlStatementContext) {
      this.visitLoadXmlStatement((LoadXmlStatementContext) child0);
    } else if (child0 instanceof DoStatementContext) {
      this.visitDoStatement((DoStatementContext) child0);
    } else if (child0 instanceof HandlerStatementContext) {
      this.visitHandlerStatement((HandlerStatementContext) child0);
    }

    return null;
  }

  @Override
  public Void visitTransactionStatement(TransactionStatementContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitReplicationStatement(ReplicationStatementContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitPreparedStatement(PreparedStatementContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitCompoundStatement(CompoundStatementContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitAdministrationStatement(AdministrationStatementContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitUtilityStatement(UtilityStatementContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitCreateDatabase(CreateDatabaseContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitCreateEvent(CreateEventContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitCreateIndex(CreateIndexContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitCreateLogfileGroup(CreateLogfileGroupContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitCreateProcedure(CreateProcedureContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitCreateFunction(CreateFunctionContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitCreateServer(CreateServerContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  private Void visitCreateTable(CreateTableContext ctx) {
    if (ctx == null) {
      return null;
    }
    if (ctx instanceof CopyCreateTableContext) {
      this.visitCopyCreateTable((CopyCreateTableContext) ctx);
    } else if (ctx instanceof QueryCreateTableContext) {
      this.visitQueryCreateTable((QueryCreateTableContext) ctx);
    } else if (ctx instanceof ColumnCreateTableContext) {
      this.visitColumnCreateTable((ColumnCreateTableContext) ctx);
    } else {
      throw new UnsupportedOperationException();
    }

    return null;
  }

  @Override
  public Void visitCopyCreateTable(CopyCreateTableContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitQueryCreateTable(QueryCreateTableContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitColumnCreateTable(ColumnCreateTableContext ctx) {
    if (ctx == null) {
      return null;
    }

    this.visitTerminal(ctx.CREATE());
    this.visitTerminal(ctx.TEMPORARY());
    this.visitTerminal(ctx.TABLE());
    this.visitIfNotExists(ctx.ifNotExists());
    this.visitTableName(ctx.tableName());
    this.visitCreateDefinitions(ctx.createDefinitions());
    List<TableOptionContext> tableOptions = ctx.tableOption();
    if (tableOptions != null && tableOptions.size() > 0) {
      int tableOptionSize = tableOptions.size();
      for (int i = 0; i < tableOptionSize; i++) {
        System.err.print(" ");
        this.visitTableOption(tableOptions.get(i));
      }
    }
    this.visitPartitionDefinitions(ctx.partitionDefinitions());

    return null;
  }

  @Override
  public Void visitCreateTablespaceInnodb(CreateTablespaceInnodbContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitCreateTablespaceNdb(CreateTablespaceNdbContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitCreateTrigger(CreateTriggerContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitCreateView(CreateViewContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitCreateDatabaseOption(CreateDatabaseOptionContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitOwnerStatement(OwnerStatementContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitPreciseSchedule(PreciseScheduleContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitIntervalSchedule(IntervalScheduleContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitTimestampValue(TimestampValueContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitIntervalExpr(IntervalExprContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitIntervalType(IntervalTypeContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitEnableType(EnableTypeContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitIndexType(IndexTypeContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitIndexOption(IndexOptionContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitProcedureParameter(ProcedureParameterContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitFunctionParameter(FunctionParameterContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitRoutineComment(RoutineCommentContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitRoutineLanguage(RoutineLanguageContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitRoutineBehavior(RoutineBehaviorContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitRoutineData(RoutineDataContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitRoutineSecurity(RoutineSecurityContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitServerOption(ServerOptionContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitCreateDefinitions(CreateDefinitionsContext ctx) {
    if (ctx == null) {
      return null;
    }

    for (ParseTree child : ctx.children) {
      if (child instanceof TerminalNode) {
        this.visitTerminal((TerminalNode) child);
      } else if (child instanceof CreateDefinitionContext) {
        this.visitCreateDefinition((CreateDefinitionContext) child);
      }
    }

    return null;
  }

  private Void visitCreateDefinition(CreateDefinitionContext ctx) {
    if (ctx == null) {
      return null;
    }

    ParseTree child0 = ctx.getChild(0);
    if (child0 instanceof UidContext) {
      this.visitUid((UidContext) child0);
      ParseTree child1 = ctx.getChild(1);
      this.visitColumnDefinition((ColumnDefinitionContext) child1);
    } else if (child0 instanceof TableConstraintContext) {
      this.visitTableConstraint((TableConstraintContext) child0);
    } else if (child0 instanceof IndexColumnDefinitionContext) {
      this.visitIndexColumnDefinition((IndexColumnDefinitionContext) child0);
    } else {
      throw new UnsupportedOperationException();
    }

    return null;
  }

  @Override
  public Void visitColumnDeclaration(ColumnDeclarationContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitConstraintDeclaration(ConstraintDeclarationContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitIndexDeclaration(IndexDeclarationContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitColumnDefinition(ColumnDefinitionContext ctx) {
    this.visitDataType(ctx.dataType());
    for (ColumnConstraintContext columnConstraint : ctx.columnConstraint()) {
      this.visitColumnConstraint(columnConstraint);
    }
    return null;
  }

  private Void visitColumnConstraint(ColumnConstraintContext ctx) {
    if (ctx instanceof NullColumnConstraintContext) {
      this.visitNullColumnConstraint((NullColumnConstraintContext) ctx);
    } else if (ctx instanceof DefaultColumnConstraintContext) {
      this.visitDefaultColumnConstraint((DefaultColumnConstraintContext) ctx);
    } else if (ctx instanceof AutoIncrementColumnConstraintContext) {
      this.visitAutoIncrementColumnConstraint((AutoIncrementColumnConstraintContext) ctx);
    } else if (ctx instanceof PrimaryKeyColumnConstraintContext) {
      this.visitPrimaryKeyColumnConstraint((PrimaryKeyColumnConstraintContext) ctx);
    } else if (ctx instanceof UniqueKeyColumnConstraintContext) {
      this.visitUniqueKeyColumnConstraint((UniqueKeyColumnConstraintContext) ctx);
    } else if (ctx instanceof CommentColumnConstraintContext) {
      this.visitCommentColumnConstraint((CommentColumnConstraintContext) ctx);
    } else if (ctx instanceof FormatColumnConstraintContext) {
      this.visitFormatColumnConstraint((FormatColumnConstraintContext) ctx);
    } else if (ctx instanceof StorageColumnConstraintContext) {
      this.visitStorageColumnConstraint((StorageColumnConstraintContext) ctx);
    } else if (ctx instanceof ReferenceColumnConstraintContext) {
      this.visitReferenceColumnConstraint((ReferenceColumnConstraintContext) ctx);
    } else if (ctx instanceof CollateColumnConstraintContext) {
      this.visitCollateColumnConstraint((CollateColumnConstraintContext) ctx);
    } else if (ctx instanceof GeneratedColumnConstraintContext) {
      this.visitGeneratedColumnConstraint((GeneratedColumnConstraintContext) ctx);
    } else if (ctx instanceof SerialDefaultColumnConstraintContext) {
      this.visitSerialDefaultColumnConstraint((SerialDefaultColumnConstraintContext) ctx);
    } else {
      if (ctx == null) {
        return null;
      }
      throw new UnsupportedOperationException();
    }

    return null;
  }

  @Override
  public Void visitNullColumnConstraint(NullColumnConstraintContext ctx) {
    if (ctx == null) {
      return null;
    }

    this.visitNullNotnull(ctx.nullNotnull());
    return null;
  }

  @Override
  public Void visitDefaultColumnConstraint(DefaultColumnConstraintContext ctx) {
    if (ctx == null) {
      return null;
    }

    for (ParseTree child : ctx.children) {
      if (child instanceof TerminalNode) {
        this.visitTerminal((TerminalNode) child);
      } else if (child instanceof DefaultValueContext) {
        this.visitDefaultValue((DefaultValueContext) child);
      } else {
        throw new UnsupportedOperationException();
      }
    }
    return null;
  }

  @Override
  public Void visitAutoIncrementColumnConstraint(AutoIncrementColumnConstraintContext ctx) {
    if (ctx == null) {
      return null;
    }

    for (ParseTree child : ctx.children) {
      if (child instanceof TerminalNode) {
        this.visitTerminal((TerminalNode) child);
      } else if (child instanceof CurrentTimestampContext) {
        this.visitCurrentTimestamp((CurrentTimestampContext) child);
      } else {
        throw new UnsupportedOperationException();
      }
    }

    return null;
  }

  @Override
  public Void visitPrimaryKeyColumnConstraint(PrimaryKeyColumnConstraintContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitUniqueKeyColumnConstraint(UniqueKeyColumnConstraintContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitCommentColumnConstraint(CommentColumnConstraintContext ctx) {
    this.visitTerminal(ctx.COMMENT());
    this.visitTerminal(ctx.STRING_LITERAL());

    return null;
  }

  @Override
  public Void visitFormatColumnConstraint(FormatColumnConstraintContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitStorageColumnConstraint(StorageColumnConstraintContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitReferenceColumnConstraint(ReferenceColumnConstraintContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitCollateColumnConstraint(CollateColumnConstraintContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitGeneratedColumnConstraint(GeneratedColumnConstraintContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitSerialDefaultColumnConstraint(SerialDefaultColumnConstraintContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  private Void visitTableConstraint(TableConstraintContext ctx) {
    if (ctx == null) {
      return null;
    }
    if (ctx instanceof PrimaryKeyTableConstraintContext) {
      this.visitPrimaryKeyTableConstraint((PrimaryKeyTableConstraintContext) ctx);
    } else if (ctx instanceof UniqueKeyTableConstraintContext) {
      this.visitUniqueKeyTableConstraint((UniqueKeyTableConstraintContext) ctx);
    } else if (ctx instanceof ForeignKeyTableConstraintContext) {
      this.visitForeignKeyTableConstraint((ForeignKeyTableConstraintContext) ctx);
    } else if (ctx instanceof CheckTableConstraintContext) {
      this.visitCheckTableConstraint((CheckTableConstraintContext) ctx);
    } else {
      throw new UnsupportedOperationException();
    }

    return null;
  }

  @Override
  public Void visitPrimaryKeyTableConstraint(PrimaryKeyTableConstraintContext ctx) {
    if (ctx == null) {
      return null;
    }

    this.visitTerminal(ctx.CONSTRAINT());
    this.visitUid(ctx.name);
    this.visitTerminal(ctx.PRIMARY());
    this.visitTerminal(ctx.KEY());
    this.visitUid(ctx.index);
    this.visitIndexType(ctx.indexType());
    this.visitIndexColumnNames(ctx.indexColumnNames());
    List<IndexOptionContext> indexOptions = ctx.indexOption();
    if (indexOptions != null && indexOptions.size() > 0) {
      for (IndexOptionContext indexOption : indexOptions) {
        this.visitIndexOption(indexOption);
      }
    }

    return null;
  }

  @Override
  public Void visitUniqueKeyTableConstraint(UniqueKeyTableConstraintContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitForeignKeyTableConstraint(ForeignKeyTableConstraintContext ctx) {
    if (ctx == null) {
      return null;
    }

    for (ParseTree child : ctx.children) {
      if (child instanceof TerminalNode) {
        this.visitTerminal((TerminalNode) child);
      } else if (child instanceof UidContext) {
        this.visitUid((UidContext) child);
      } else if (child instanceof IndexColumnNamesContext) {
        this.visitIndexColumnNames((IndexColumnNamesContext) child);
      } else if (child instanceof ReferenceDefinitionContext) {
        this.visitReferenceDefinition((ReferenceDefinitionContext) child);
      } else {
        throw new UnsupportedOperationException();
      }
    }

    return null;
  }

  @Override
  public Void visitCheckTableConstraint(CheckTableConstraintContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitReferenceDefinition(ReferenceDefinitionContext ctx) {
    if (ctx == null) {
      return null;
    }

    for (ParseTree child : ctx.children) {
      if (child instanceof TerminalNode) {
        this.visitTerminal((TerminalNode) child);
      } else if (child instanceof TableNameContext) {
        this.visitTableName((TableNameContext) child);
      } else if (child instanceof IndexColumnNamesContext) {
        this.visitIndexColumnNames((IndexColumnNamesContext) child);
      } else if (child instanceof ReferenceActionContext) {
        this.visitReferenceAction((ReferenceActionContext) child);
      } else {
        throw new UnsupportedOperationException();
      }
    }

    return null;
  }

  @Override
  public Void visitReferenceAction(ReferenceActionContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitReferenceControlType(ReferenceControlTypeContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  private Void visitIndexColumnDefinition(IndexColumnDefinitionContext ctx) {
    if (ctx == null) {
      return null;
    }

    if (ctx instanceof SimpleIndexDeclarationContext) {
      this.visitSimpleIndexDeclaration((SimpleIndexDeclarationContext) ctx);
    } else if (ctx instanceof SpecialIndexDeclarationContext) {
      this.visitSpecialIndexDeclaration((SpecialIndexDeclarationContext) ctx);
    } else {
      throw new UnsupportedOperationException();
    }

    return null;
  }

  @Override
  public Void visitSimpleIndexDeclaration(SimpleIndexDeclarationContext ctx) {
    if (ctx == null) {
      return null;
    }

    for (ParseTree child : ctx.children) {
      if (child instanceof TerminalNode) {
        this.visitTerminal((TerminalNode) child);
      } else if (child instanceof UidContext) {
        this.visitUid((UidContext) child);
      } else if (child instanceof IndexTypeContext) {
        this.visitIndexType((IndexTypeContext) child);
      } else if (child instanceof IndexColumnNamesContext) {
        this.visitIndexColumnNames((IndexColumnNamesContext) child);
      } else if (child instanceof IndexOptionContext) {
        this.visitIndexOption((IndexOptionContext) child);
      } else {
        throw new UnsupportedOperationException();
      }
    }

    return null;
  }

  @Override
  public Void visitSpecialIndexDeclaration(SpecialIndexDeclarationContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  private Void visitTableOption(TableOptionContext ctx) {
    if (ctx == null) {
      return null;
    }

    if (ctx instanceof TableOptionEngineContext) {
      this.visitTableOptionEngine((TableOptionEngineContext) ctx);
    } else if (ctx instanceof TableOptionAutoIncrementContext) {
      this.visitTableOptionAutoIncrement((TableOptionAutoIncrementContext) ctx);
    } else if (ctx instanceof TableOptionAverageContext) {
      throw new UnsupportedOperationException();
    } else if (ctx instanceof TableOptionCharsetContext) {
      this.visitTableOptionCharset((TableOptionCharsetContext) ctx);
    } else if (ctx instanceof TableOptionChecksumContext) {
      throw new UnsupportedOperationException();
    } else if (ctx instanceof TableOptionCollateContext) {
      throw new UnsupportedOperationException();
    } else if (ctx instanceof TableOptionCommentContext) {
      this.visitTableOptionComment((TableOptionCommentContext) ctx);
    } else if (ctx instanceof TableOptionCompressionContext) {
      throw new UnsupportedOperationException();
    } else if (ctx instanceof TableOptionConnectionContext) {
      throw new UnsupportedOperationException();
    } else if (ctx instanceof TableOptionDataDirectoryContext) {
      throw new UnsupportedOperationException();
    } else if (ctx instanceof TableOptionDelayContext) {
      throw new UnsupportedOperationException();
    } else if (ctx instanceof TableOptionEncryptionContext) {
      throw new UnsupportedOperationException();
    } else if (ctx instanceof TableOptionIndexDirectoryContext) {
      throw new UnsupportedOperationException();
    } else if (ctx instanceof TableOptionInsertMethodContext) {
      throw new UnsupportedOperationException();
    } else if (ctx instanceof TableOptionKeyBlockSizeContext) {
      throw new UnsupportedOperationException();
    } else if (ctx instanceof TableOptionMaxRowsContext) {
      throw new UnsupportedOperationException();
    } else if (ctx instanceof TableOptionMinRowsContext) {
      throw new UnsupportedOperationException();
    } else if (ctx instanceof TableOptionPackKeysContext) {
      throw new UnsupportedOperationException();
    } else if (ctx instanceof TableOptionPasswordContext) {
      throw new UnsupportedOperationException();
    } else if (ctx instanceof TableOptionRowFormatContext) {
      throw new UnsupportedOperationException();
    } else if (ctx instanceof TableOptionRecalculationContext) {
      throw new UnsupportedOperationException();
    } else if (ctx instanceof TableOptionPersistentContext) {
      throw new UnsupportedOperationException();
    } else if (ctx instanceof TableOptionSamplePageContext) {
      throw new UnsupportedOperationException();
    } else if (ctx instanceof TableOptionTablespaceContext) {
      throw new UnsupportedOperationException();
    } else if (ctx instanceof TableOptionTablespaceContext) {
      throw new UnsupportedOperationException();
    } else if (ctx instanceof TableOptionUnionContext) {
      throw new UnsupportedOperationException();
    }

    return null;
  }

  @Override
  public Void visitTableOptionEngine(TableOptionEngineContext ctx) {
    if (ctx == null) {
      return null;
    }

    List<ParseTree> children = ctx.children;
    for (ParseTree child : children) {
      if (child instanceof TerminalNode) {
        this.visitTerminal((TerminalNode) child);
      } else if (child instanceof EngineNameContext) {
        this.visitEngineName((EngineNameContext) child);
      } else {
        throw new UnsupportedOperationException();
      }
    }

    return null;
  }

  @Override
  public Void visitTableOptionAutoIncrement(TableOptionAutoIncrementContext ctx) {
    if (ctx == null) {
      return null;
    }

    List<ParseTree> children = ctx.children;
    for (ParseTree child : children) {
      if (child instanceof TerminalNode) {
        this.visitTerminal((TerminalNode) child);
      } else if (child instanceof DecimalLiteralContext) {
        this.visitDecimalLiteral((DecimalLiteralContext) child);
      } else {
        throw new UnsupportedOperationException();
      }
    }

    return null;
  }

  @Override
  public Void visitTableOptionAverage(TableOptionAverageContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitTableOptionCharset(TableOptionCharsetContext ctx) {
    if (ctx == null) {
      return null;
    }

    List<ParseTree> children = ctx.children;
    for (ParseTree child : children) {
      if (child instanceof TerminalNode) {
        this.visitTerminal((TerminalNode) child);
      } else if (child instanceof CharsetNameContext) {
        this.visitCharsetName((CharsetNameContext) child);
      } else {
        throw new UnsupportedOperationException();
      }
    }

    return null;
  }

  @Override
  public Void visitTableOptionChecksum(TableOptionChecksumContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitTableOptionCollate(TableOptionCollateContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitTableOptionComment(TableOptionCommentContext ctx) {
    if (ctx == null) {
      return null;
    }

    List<ParseTree> children = ctx.children;
    for (ParseTree child : children) {
      if (child instanceof TerminalNode) {
        this.visitTerminal((TerminalNode) child);
      } else {
        throw new UnsupportedOperationException();
      }
    }

    return null;
  }

  @Override
  public Void visitTableOptionCompression(TableOptionCompressionContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitTableOptionConnection(TableOptionConnectionContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitTableOptionDataDirectory(TableOptionDataDirectoryContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitTableOptionDelay(TableOptionDelayContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitTableOptionEncryption(TableOptionEncryptionContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitTableOptionIndexDirectory(TableOptionIndexDirectoryContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitTableOptionInsertMethod(TableOptionInsertMethodContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitTableOptionKeyBlockSize(TableOptionKeyBlockSizeContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitTableOptionMaxRows(TableOptionMaxRowsContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitTableOptionMinRows(TableOptionMinRowsContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitTableOptionPackKeys(TableOptionPackKeysContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitTableOptionPassword(TableOptionPasswordContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitTableOptionRowFormat(TableOptionRowFormatContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitTableOptionRecalculation(TableOptionRecalculationContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitTableOptionPersistent(TableOptionPersistentContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitTableOptionSamplePage(TableOptionSamplePageContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitTableOptionTablespace(TableOptionTablespaceContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitTableOptionUnion(TableOptionUnionContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitTablespaceStorage(TablespaceStorageContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitPartitionDefinitions(PartitionDefinitionsContext ctx) {
    if (ctx == null) {
      return null;
    }

    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitPartitionFunctionHash(PartitionFunctionHashContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitPartitionFunctionKey(PartitionFunctionKeyContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitPartitionFunctionRange(PartitionFunctionRangeContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitPartitionFunctionList(PartitionFunctionListContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitSubPartitionFunctionHash(SubPartitionFunctionHashContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitSubPartitionFunctionKey(SubPartitionFunctionKeyContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitPartitionComparision(PartitionComparisionContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitPartitionListAtom(PartitionListAtomContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitPartitionListVector(PartitionListVectorContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitPartitionSimple(PartitionSimpleContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitPartitionDefinerAtom(PartitionDefinerAtomContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitPartitionDefinerVector(PartitionDefinerVectorContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitSubpartitionDefinition(SubpartitionDefinitionContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitPartitionOptionEngine(PartitionOptionEngineContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitPartitionOptionComment(PartitionOptionCommentContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitPartitionOptionDataDirectory(PartitionOptionDataDirectoryContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitPartitionOptionIndexDirectory(PartitionOptionIndexDirectoryContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitPartitionOptionMaxRows(PartitionOptionMaxRowsContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitPartitionOptionMinRows(PartitionOptionMinRowsContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitPartitionOptionTablespace(PartitionOptionTablespaceContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitPartitionOptionNodeGroup(PartitionOptionNodeGroupContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitAlterSimpleDatabase(AlterSimpleDatabaseContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitAlterUpgradeName(AlterUpgradeNameContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitAlterEvent(AlterEventContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitAlterFunction(AlterFunctionContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitAlterInstance(AlterInstanceContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitAlterLogfileGroup(AlterLogfileGroupContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitAlterProcedure(AlterProcedureContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitAlterServer(AlterServerContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitAlterTable(AlterTableContext ctx) {
    this.visitTerminal(ctx.ALTER());
    this.visitTerminal(ctx.ONLINE());
    this.visitTerminal(ctx.OFFLINE());
    this.visitTerminal(ctx.IGNORE());
    this.visitTerminal(ctx.TABLE());
    this.visitTableName(ctx.tableName());
    for (AlterSpecificationContext alterSpecification : ctx.alterSpecification()) {
      this.visitAlterSpecification(alterSpecification);
    }
    this.visitPartitionDefinitions(ctx.partitionDefinitions());
    return null;
  }

  @Override
  public Void visitAlterTablespace(AlterTablespaceContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitAlterView(AlterViewContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  private Void visitAlterSpecification(AlterSpecificationContext ctx) {

    if (ctx instanceof AlterByTableOptionContext) {
      this.visitAlterByTableOption((AlterByTableOptionContext) ctx);
    } else if (ctx instanceof AlterByAddColumnContext) {
      this.visitAlterByAddColumn((AlterByAddColumnContext) ctx);
    } else if (ctx instanceof AlterByAddColumnsContext) {
      this.visitAlterByAddColumns((AlterByAddColumnsContext) ctx);
    } else if (ctx instanceof AlterByAddIndexContext) {
      this.visitAlterByAddIndex((AlterByAddIndexContext) ctx);
    } else if (ctx instanceof AlterByAddPrimaryKeyContext) {
      this.visitAlterByAddPrimaryKey((AlterByAddPrimaryKeyContext) ctx);
    } else if (ctx instanceof AlterByAddUniqueKeyContext) {
      this.visitAlterByAddUniqueKey((AlterByAddUniqueKeyContext) ctx);
    } else if (ctx instanceof AlterByAddSpecialIndexContext) {
      this.visitAlterByAddSpecialIndex((AlterByAddSpecialIndexContext) ctx);
    } else if (ctx instanceof AlterByAddForeignKeyContext) {
      this.visitAlterByAddForeignKey((AlterByAddForeignKeyContext) ctx);
    } else if (ctx instanceof AlterByAddCheckTableConstraintContext) {
      this.visitAlterByAddCheckTableConstraint((AlterByAddCheckTableConstraintContext) ctx);
    } else if (ctx instanceof AlterBySetAlgorithmContext) {
      this.visitAlterBySetAlgorithm((AlterBySetAlgorithmContext) ctx);
    } else if (ctx instanceof AlterByChangeDefaultContext) {
      this.visitAlterByChangeDefault((AlterByChangeDefaultContext) ctx);
    } else if (ctx instanceof AlterByChangeColumnContext) {
      this.visitAlterByChangeColumn((AlterByChangeColumnContext) ctx);
    } else if (ctx instanceof AlterByRenameColumnContext) {
      this.visitAlterByRenameColumn((AlterByRenameColumnContext) ctx);
    } else if (ctx instanceof AlterByLockContext) {
      this.visitAlterByLock((AlterByLockContext) ctx);
    } else if (ctx instanceof AlterByModifyColumnContext) {
      this.visitAlterByModifyColumn((AlterByModifyColumnContext) ctx);
    } else if (ctx instanceof AlterByDropColumnContext) {
      this.visitAlterByDropColumn((AlterByDropColumnContext) ctx);
    } else if (ctx instanceof AlterByDropPrimaryKeyContext) {
      this.visitAlterByDropPrimaryKey((AlterByDropPrimaryKeyContext) ctx);
    } else if (ctx instanceof AlterByRenameIndexContext) {
      this.visitAlterByRenameIndex((AlterByRenameIndexContext) ctx);
    } else if (ctx instanceof AlterByDropIndexContext) {
      this.visitAlterByDropIndex((AlterByDropIndexContext) ctx);
    } else if (ctx instanceof AlterByDropForeignKeyContext) {
      this.visitAlterByDropForeignKey((AlterByDropForeignKeyContext) ctx);
    } else if (ctx instanceof AlterByDisableKeysContext) {
      this.visitAlterByDisableKeys((AlterByDisableKeysContext) ctx);
    } else if (ctx instanceof AlterByEnableKeysContext) {
      this.visitAlterByEnableKeys((AlterByEnableKeysContext) ctx);
    } else if (ctx instanceof AlterByRenameContext) {
      this.visitAlterByRename((AlterByRenameContext) ctx);
    } else if (ctx instanceof AlterByOrderContext) {
      this.visitAlterByOrder((AlterByOrderContext) ctx);
    } else if (ctx instanceof AlterByConvertCharsetContext) {
      this.visitAlterByConvertCharset((AlterByConvertCharsetContext) ctx);
    } else if (ctx instanceof AlterByDefaultCharsetContext) {
      this.visitAlterByDefaultCharset((AlterByDefaultCharsetContext) ctx);
    } else if (ctx instanceof AlterByDiscardTablespaceContext) {
      this.visitAlterByDiscardTablespace((AlterByDiscardTablespaceContext) ctx);
    } else if (ctx instanceof AlterByImportTablespaceContext) {
      this.visitAlterByImportTablespace((AlterByImportTablespaceContext) ctx);
    } else if (ctx instanceof AlterByForceContext) {
      this.visitAlterByForce((AlterByForceContext) ctx);
    } else if (ctx instanceof AlterByValidateContext) {
      this.visitAlterByValidate((AlterByValidateContext) ctx);
    } else if (ctx instanceof AlterByAddPartitionContext) {
      this.visitAlterByAddPartition((AlterByAddPartitionContext) ctx);
    } else if (ctx instanceof AlterByDropPartitionContext) {
      this.visitAlterByDropPartition((AlterByDropPartitionContext) ctx);
    } else if (ctx instanceof AlterByDiscardPartitionContext) {
      this.visitAlterByDiscardPartition((AlterByDiscardPartitionContext) ctx);
    } else if (ctx instanceof AlterByImportPartitionContext) {
      this.visitAlterByImportPartition((AlterByImportPartitionContext) ctx);
    } else if (ctx instanceof AlterByTruncatePartitionContext) {
      this.visitAlterByTruncatePartition((AlterByTruncatePartitionContext) ctx);
    } else if (ctx instanceof AlterByCoalescePartitionContext) {
      this.visitAlterByCoalescePartition((AlterByCoalescePartitionContext) ctx);
    } else if (ctx instanceof AlterByReorganizePartitionContext) {
      this.visitAlterByReorganizePartition((AlterByReorganizePartitionContext) ctx);
    } else if (ctx instanceof AlterByExchangePartitionContext) {
      this.visitAlterByExchangePartition((AlterByExchangePartitionContext) ctx);
    } else if (ctx instanceof AlterByAnalyzePartitionContext) {
      this.visitAlterByAnalyzePartition((AlterByAnalyzePartitionContext) ctx);
    } else if (ctx instanceof AlterByCheckPartitionContext) {
      this.visitAlterByCheckPartition((AlterByCheckPartitionContext) ctx);
    } else if (ctx instanceof AlterByOptimizePartitionContext) {
      this.visitAlterByOptimizePartition((AlterByOptimizePartitionContext) ctx);
    } else if (ctx instanceof AlterByRebuildPartitionContext) {
      this.visitAlterByRebuildPartition((AlterByRebuildPartitionContext) ctx);
    } else if (ctx instanceof AlterByRepairPartitionContext) {
      this.visitAlterByRepairPartition((AlterByRepairPartitionContext) ctx);
    } else if (ctx instanceof AlterByRemovePartitioningContext) {
      this.visitAlterByRemovePartitioning((AlterByRemovePartitioningContext) ctx);
    } else if (ctx instanceof AlterByUpgradePartitioningContext) {
      this.visitAlterByUpgradePartitioning((AlterByUpgradePartitioningContext) ctx);
    } else {
      if (ctx == null) {
        return null;
      }
      throw new UnsupportedOperationException();
    }

    return null;
  }

  @Override
  public Void visitAlterByTableOption(AlterByTableOptionContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitAlterByAddColumn(AlterByAddColumnContext ctx) {
    this.visitTerminal(ctx.ADD());
    this.visitTerminal(ctx.COLUMN());
    this.visitUid(ctx.uid(0));
    this.visitColumnDefinition(ctx.columnDefinition());
    if (ctx.FIRST() != null) {
      this.visitTerminal(ctx.FIRST());
    }
    if (ctx.AFTER() != null) {
      this.visitTerminal(ctx.AFTER());
      this.visitUid(ctx.uid(1));
    }

    return null;
  }

  @Override
  public Void visitAlterByAddColumns(AlterByAddColumnsContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitAlterByAddIndex(AlterByAddIndexContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitAlterByAddPrimaryKey(AlterByAddPrimaryKeyContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitAlterByAddUniqueKey(AlterByAddUniqueKeyContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitAlterByAddSpecialIndex(AlterByAddSpecialIndexContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitAlterByAddForeignKey(AlterByAddForeignKeyContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitAlterByAddCheckTableConstraint(AlterByAddCheckTableConstraintContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitAlterBySetAlgorithm(AlterBySetAlgorithmContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitAlterByChangeDefault(AlterByChangeDefaultContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitAlterByChangeColumn(AlterByChangeColumnContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitAlterByRenameColumn(AlterByRenameColumnContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitAlterByLock(AlterByLockContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitAlterByModifyColumn(AlterByModifyColumnContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitAlterByDropColumn(AlterByDropColumnContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitAlterByDropPrimaryKey(AlterByDropPrimaryKeyContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitAlterByRenameIndex(AlterByRenameIndexContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitAlterByDropIndex(AlterByDropIndexContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitAlterByDropForeignKey(AlterByDropForeignKeyContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitAlterByDisableKeys(AlterByDisableKeysContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitAlterByEnableKeys(AlterByEnableKeysContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitAlterByRename(AlterByRenameContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitAlterByOrder(AlterByOrderContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitAlterByConvertCharset(AlterByConvertCharsetContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitAlterByDefaultCharset(AlterByDefaultCharsetContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitAlterByDiscardTablespace(AlterByDiscardTablespaceContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitAlterByImportTablespace(AlterByImportTablespaceContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitAlterByForce(AlterByForceContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitAlterByValidate(AlterByValidateContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitAlterByAddPartition(AlterByAddPartitionContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitAlterByDropPartition(AlterByDropPartitionContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitAlterByDiscardPartition(AlterByDiscardPartitionContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitAlterByImportPartition(AlterByImportPartitionContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitAlterByTruncatePartition(AlterByTruncatePartitionContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitAlterByCoalescePartition(AlterByCoalescePartitionContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitAlterByReorganizePartition(AlterByReorganizePartitionContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitAlterByExchangePartition(AlterByExchangePartitionContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitAlterByAnalyzePartition(AlterByAnalyzePartitionContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitAlterByCheckPartition(AlterByCheckPartitionContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitAlterByOptimizePartition(AlterByOptimizePartitionContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitAlterByRebuildPartition(AlterByRebuildPartitionContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitAlterByRepairPartition(AlterByRepairPartitionContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitAlterByRemovePartitioning(AlterByRemovePartitioningContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitAlterByUpgradePartitioning(AlterByUpgradePartitioningContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitDropDatabase(DropDatabaseContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitDropEvent(DropEventContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitDropIndex(DropIndexContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitDropLogfileGroup(DropLogfileGroupContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitDropProcedure(DropProcedureContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitDropFunction(DropFunctionContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitDropServer(DropServerContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitDropTable(DropTableContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitDropTablespace(DropTablespaceContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitDropTrigger(DropTriggerContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitDropView(DropViewContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitRenameTable(RenameTableContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitRenameTableClause(RenameTableClauseContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitTruncateTable(TruncateTableContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitCallStatement(CallStatementContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitDeleteStatement(DeleteStatementContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitDoStatement(DoStatementContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitHandlerStatement(HandlerStatementContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitInsertStatement(InsertStatementContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitLoadDataStatement(LoadDataStatementContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitLoadXmlStatement(LoadXmlStatementContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitReplaceStatement(ReplaceStatementContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  private Void visitSelectStatement(SelectStatementContext ctx) {
    if (ctx == null) {
      return null;
    }

    if (ctx instanceof SimpleSelectContext) {
      this.visitSimpleSelect((SimpleSelectContext) ctx);
    } else if (ctx instanceof ParenthesisSelectContext) {
      this.visitParenthesisSelect((ParenthesisSelectContext) ctx);
    } else if (ctx instanceof UnionSelectContext) {
      this.visitUnionSelect((UnionSelectContext) ctx);
    } else if (ctx instanceof UnionParenthesisSelectContext) {
      this.visitUnionParenthesisSelect((UnionParenthesisSelectContext) ctx);
    } else {
      throw new UnsupportedOperationException();
    }

    return null;
  }

  @Override
  public Void visitSimpleSelect(SimpleSelectContext ctx) {
    if (ctx == null) {
      return null;
    }

    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitParenthesisSelect(ParenthesisSelectContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitUnionSelect(UnionSelectContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitUnionParenthesisSelect(UnionParenthesisSelectContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitUpdateStatement(UpdateStatementContext ctx) {
    if (ctx == null) {
      return null;
    }

    ParseTree child0 = ctx.getChild(0);
    if (child0 instanceof SingleUpdateStatementContext) {
      this.visitSingleUpdateStatement((SingleUpdateStatementContext) child0);
    } else if (child0 instanceof MultipleUpdateStatementContext) {
      this.visitMultipleUpdateStatement((MultipleUpdateStatementContext) child0);
    } else {
      throw new UnsupportedOperationException();
    }

    return null;
  }

  @Override
  public Void visitInsertStatementValue(InsertStatementValueContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitUpdatedElement(UpdatedElementContext ctx) {
    if (ctx == null) {
      return null;
    }

    for (ParseTree child : ctx.children) {
      if (child instanceof FullColumnNameContext) {
        this.visitFullColumnName((FullColumnNameContext) child);
      } else if (child instanceof TerminalNode) {
        this.visitTerminal((TerminalNode) child);
      } else if (child instanceof ExpressionContext) {
        this.visitExpression((ExpressionContext) child);
      } else {
        throw new UnsupportedOperationException();
      }
    }

    return null;
  }

  @Override
  public Void visitAssignmentField(AssignmentFieldContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitLockClause(LockClauseContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitSingleDeleteStatement(SingleDeleteStatementContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitMultipleDeleteStatement(MultipleDeleteStatementContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitHandlerOpenStatement(HandlerOpenStatementContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitHandlerReadIndexStatement(HandlerReadIndexStatementContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitHandlerReadStatement(HandlerReadStatementContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitHandlerCloseStatement(HandlerCloseStatementContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitSingleUpdateStatement(SingleUpdateStatementContext ctx) {
    if (ctx == null) {
      return null;
    }

    for (ParseTree child : ctx.children) {
      if (child instanceof TerminalNode) {
        this.visitTerminal((TerminalNode) child);
      } else if (child instanceof TableNameContext) {
        this.visitTableName((TableNameContext) child);
      } else if (child instanceof UidContext) {
        this.visitUid((UidContext) child);
      } else if (child instanceof UpdatedElementContext) {
        this.visitUpdatedElement((UpdatedElementContext) child);
      } else if (child instanceof ExpressionContext) {
        this.visitExpression((ExpressionContext) child);
      } else if (child instanceof OrderByClauseContext) {
        this.visitOrderByClause((OrderByClauseContext) child);
      } else if (child instanceof LimitClauseContext) {
        this.visitLimitClause((LimitClauseContext) child);
      }

      else {
        throw new UnsupportedOperationException();
      }
    }

    return null;
  }

  @Override
  public Void visitMultipleUpdateStatement(MultipleUpdateStatementContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitOrderByClause(OrderByClauseContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitOrderByExpression(OrderByExpressionContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitTableSources(TableSourcesContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitTableSourceBase(TableSourceBaseContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitTableSourceNested(TableSourceNestedContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitAtomTableItem(AtomTableItemContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitSubqueryTableItem(SubqueryTableItemContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitTableSourcesItem(TableSourcesItemContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitIndexHint(IndexHintContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitIndexHintType(IndexHintTypeContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitInnerJoin(InnerJoinContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitStraightJoin(StraightJoinContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitOuterJoin(OuterJoinContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitNaturalJoin(NaturalJoinContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitQueryExpression(QueryExpressionContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitQueryExpressionNointo(QueryExpressionNointoContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitQuerySpecification(QuerySpecificationContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitQuerySpecificationNointo(QuerySpecificationNointoContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitUnionParenthesis(UnionParenthesisContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitUnionStatement(UnionStatementContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitSelectSpec(SelectSpecContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitSelectElements(SelectElementsContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitSelectStarElement(SelectStarElementContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitSelectColumnElement(SelectColumnElementContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitSelectFunctionElement(SelectFunctionElementContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitSelectExpressionElement(SelectExpressionElementContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitSelectIntoVariables(SelectIntoVariablesContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitSelectIntoDumpFile(SelectIntoDumpFileContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitSelectIntoTextFile(SelectIntoTextFileContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitSelectFieldsInto(SelectFieldsIntoContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitSelectLinesInto(SelectLinesIntoContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitFromClause(FromClauseContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitGroupByItem(GroupByItemContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitLimitClause(LimitClauseContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitLimitClauseAtom(LimitClauseAtomContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitStartTransaction(StartTransactionContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitBeginWork(BeginWorkContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitCommitWork(CommitWorkContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitRollbackWork(RollbackWorkContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitSavepointStatement(SavepointStatementContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitRollbackStatement(RollbackStatementContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitReleaseStatement(ReleaseStatementContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitLockTables(LockTablesContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitUnlockTables(UnlockTablesContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitSetAutocommitStatement(SetAutocommitStatementContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitSetTransactionStatement(SetTransactionStatementContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitTransactionMode(TransactionModeContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitLockTableElement(LockTableElementContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitLockAction(LockActionContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitTransactionOption(TransactionOptionContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitTransactionLevel(TransactionLevelContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitChangeMaster(ChangeMasterContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitChangeReplicationFilter(ChangeReplicationFilterContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitPurgeBinaryLogs(PurgeBinaryLogsContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitResetMaster(ResetMasterContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitResetSlave(ResetSlaveContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitStartSlave(StartSlaveContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitStopSlave(StopSlaveContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitStartGroupReplication(StartGroupReplicationContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitStopGroupReplication(StopGroupReplicationContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitMasterStringOption(MasterStringOptionContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitMasterDecimalOption(MasterDecimalOptionContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitMasterBoolOption(MasterBoolOptionContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitMasterRealOption(MasterRealOptionContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitMasterUidListOption(MasterUidListOptionContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitStringMasterOption(StringMasterOptionContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitDecimalMasterOption(DecimalMasterOptionContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitBoolMasterOption(BoolMasterOptionContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitChannelOption(ChannelOptionContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitDoDbReplication(DoDbReplicationContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitIgnoreDbReplication(IgnoreDbReplicationContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitDoTableReplication(DoTableReplicationContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitIgnoreTableReplication(IgnoreTableReplicationContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitWildDoTableReplication(WildDoTableReplicationContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitWildIgnoreTableReplication(WildIgnoreTableReplicationContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitRewriteDbReplication(RewriteDbReplicationContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitTablePair(TablePairContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitThreadType(ThreadTypeContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitGtidsUntilOption(GtidsUntilOptionContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitMasterLogUntilOption(MasterLogUntilOptionContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitRelayLogUntilOption(RelayLogUntilOptionContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitSqlGapsUntilOption(SqlGapsUntilOptionContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitUserConnectionOption(UserConnectionOptionContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitPasswordConnectionOption(PasswordConnectionOptionContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitDefaultAuthConnectionOption(DefaultAuthConnectionOptionContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitPluginDirConnectionOption(PluginDirConnectionOptionContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitGtuidSet(GtuidSetContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitXaStartTransaction(XaStartTransactionContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitXaEndTransaction(XaEndTransactionContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitXaPrepareStatement(XaPrepareStatementContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitXaCommitWork(XaCommitWorkContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitXaRollbackWork(XaRollbackWorkContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitXaRecoverWork(XaRecoverWorkContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitPrepareStatement(PrepareStatementContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitExecuteStatement(ExecuteStatementContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitDeallocatePrepare(DeallocatePrepareContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitRoutineBody(RoutineBodyContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitBlockStatement(BlockStatementContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitCaseStatement(CaseStatementContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitIfStatement(IfStatementContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitIterateStatement(IterateStatementContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitLeaveStatement(LeaveStatementContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitLoopStatement(LoopStatementContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitRepeatStatement(RepeatStatementContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitReturnStatement(ReturnStatementContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitWhileStatement(WhileStatementContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitCloseCursor(CloseCursorContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitFetchCursor(FetchCursorContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitOpenCursor(OpenCursorContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitDeclareVariable(DeclareVariableContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitDeclareCondition(DeclareConditionContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitDeclareCursor(DeclareCursorContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitDeclareHandler(DeclareHandlerContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitHandlerConditionCode(HandlerConditionCodeContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitHandlerConditionState(HandlerConditionStateContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitHandlerConditionName(HandlerConditionNameContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitHandlerConditionWarning(HandlerConditionWarningContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitHandlerConditionNotfound(HandlerConditionNotfoundContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitHandlerConditionException(HandlerConditionExceptionContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitProcedureSqlStatement(ProcedureSqlStatementContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitCaseAlternative(CaseAlternativeContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitElifAlternative(ElifAlternativeContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitAlterUserMysqlV56(AlterUserMysqlV56Context ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitAlterUserMysqlV57(AlterUserMysqlV57Context ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitCreateUserMysqlV56(CreateUserMysqlV56Context ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitCreateUserMysqlV57(CreateUserMysqlV57Context ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitDropUser(DropUserContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitGrantStatement(GrantStatementContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitGrantProxy(GrantProxyContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitRenameUser(RenameUserContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitDetailRevoke(DetailRevokeContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitShortRevoke(ShortRevokeContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitRevokeProxy(RevokeProxyContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitSetPasswordStatement(SetPasswordStatementContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitUserSpecification(UserSpecificationContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitPasswordAuthOption(PasswordAuthOptionContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitStringAuthOption(StringAuthOptionContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitHashAuthOption(HashAuthOptionContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitSimpleAuthOption(SimpleAuthOptionContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitTlsOption(TlsOptionContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitUserResourceOption(UserResourceOptionContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitUserPasswordOption(UserPasswordOptionContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitUserLockOption(UserLockOptionContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitPrivelegeClause(PrivelegeClauseContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitPrivilege(PrivilegeContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitCurrentSchemaPriviLevel(CurrentSchemaPriviLevelContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitGlobalPrivLevel(GlobalPrivLevelContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitDefiniteSchemaPrivLevel(DefiniteSchemaPrivLevelContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitDefiniteFullTablePrivLevel(DefiniteFullTablePrivLevelContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitDefiniteTablePrivLevel(DefiniteTablePrivLevelContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitRenameUserClause(RenameUserClauseContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitAnalyzeTable(AnalyzeTableContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitCheckTable(CheckTableContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitChecksumTable(ChecksumTableContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitOptimizeTable(OptimizeTableContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitRepairTable(RepairTableContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitCheckTableOption(CheckTableOptionContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitCreateUdfunction(CreateUdfunctionContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitInstallPlugin(InstallPluginContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitUninstallPlugin(UninstallPluginContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitSetVariable(SetVariableContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitSetCharset(SetCharsetContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitSetNames(SetNamesContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitSetPassword(SetPasswordContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitSetTransaction(SetTransactionContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitSetAutocommit(SetAutocommitContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitSetNewValueInsideTrigger(SetNewValueInsideTriggerContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitShowMasterLogs(ShowMasterLogsContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitShowLogEvents(ShowLogEventsContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitShowObjectFilter(ShowObjectFilterContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitShowColumns(ShowColumnsContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitShowCreateDb(ShowCreateDbContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitShowCreateFullIdObject(ShowCreateFullIdObjectContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitShowCreateUser(ShowCreateUserContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitShowEngine(ShowEngineContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitShowGlobalInfo(ShowGlobalInfoContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitShowErrors(ShowErrorsContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitShowCountErrors(ShowCountErrorsContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitShowSchemaFilter(ShowSchemaFilterContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitShowRoutine(ShowRoutineContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitShowGrants(ShowGrantsContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitShowIndexes(ShowIndexesContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitShowOpenTables(ShowOpenTablesContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitShowProfile(ShowProfileContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitShowSlaveStatus(ShowSlaveStatusContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitVariableClause(VariableClauseContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitShowCommonEntity(ShowCommonEntityContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitShowFilter(ShowFilterContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitShowGlobalInfoClause(ShowGlobalInfoClauseContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitShowSchemaEntity(ShowSchemaEntityContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitShowProfileType(ShowProfileTypeContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitBinlogStatement(BinlogStatementContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitCacheIndexStatement(CacheIndexStatementContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitFlushStatement(FlushStatementContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitKillStatement(KillStatementContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitLoadIndexIntoCache(LoadIndexIntoCacheContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitResetStatement(ResetStatementContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitShutdownStatement(ShutdownStatementContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitTableIndexes(TableIndexesContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitSimpleFlushOption(SimpleFlushOptionContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitChannelFlushOption(ChannelFlushOptionContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitTableFlushOption(TableFlushOptionContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitFlushTableOption(FlushTableOptionContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitLoadedTableIndexes(LoadedTableIndexesContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitSimpleDescribeStatement(SimpleDescribeStatementContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitFullDescribeStatement(FullDescribeStatementContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitHelpStatement(HelpStatementContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitUseStatement(UseStatementContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitDescribeStatements(DescribeStatementsContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitDescribeConnection(DescribeConnectionContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitFullId(FullIdContext ctx) {
    int childCount = ctx.getChildCount();

    this.visitUid(ctx.uid(0));

    if (childCount > 1) {
      TerminalNode dotId = ctx.DOT_ID();
      if (dotId != null) {
        this.visitTerminal(ctx.DOT_ID());
      } else {
        System.err.print(".");
        this.visitUid(ctx.uid(1));
      }
    }

    return null;
  }

  @Override
  public Void visitTableName(TableNameContext ctx) {
    this.visitFullId(ctx.fullId());
    return null;
  }

  @Override
  public Void visitFullColumnName(FullColumnNameContext ctx) {
    if (ctx == null) {
      return null;
    }

    this.visitUid(ctx.uid());
    List<DottedIdContext> dottedIds = ctx.dottedId();
    if (dottedIds != null && dottedIds.size() > 0) {
      for (DottedIdContext dottedId : dottedIds) {
        this.visitDottedId(dottedId);
      }
    }

    return null;
  }

  @Override
  public Void visitIndexColumnName(IndexColumnNameContext ctx) {
    if (ctx == null) {
      return null;
    }

    List<ParseTree> children = ctx.children;
    for (ParseTree child : children) {
      if (child instanceof TerminalNode) {
        this.visitTerminal((TerminalNode) child);
      } else if (child instanceof UidContext) {
        this.visitUid((UidContext) child);
      } else if (child instanceof DecimalLiteralContext) {
        this.visitDecimalLiteral((DecimalLiteralContext) child);
      } else {
        throw new UnsupportedOperationException();
      }
    }

    return null;
  }

  @Override
  public Void visitUserName(UserNameContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitMysqlVariable(MysqlVariableContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitCharsetName(CharsetNameContext ctx) {
    if (ctx == null) {
      return null;
    }

    this.visitTerminal(ctx.BINARY());
    this.visitCharsetNameBase(ctx.charsetNameBase());
    this.visitTerminal(ctx.STRING_LITERAL());
    this.visitTerminal(ctx.CHARSET_REVERSE_QOUTE_STRING());

    return null;
  }

  @Override
  public Void visitCollationName(CollationNameContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitEngineName(EngineNameContext ctx) {
    System.err.print(ctx.getText() + " ");
    return null;
  }

  @Override
  public Void visitUuidSet(UuidSetContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitXid(XidContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitXuidStringId(XuidStringIdContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitAuthPlugin(AuthPluginContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitUid(UidContext ctx) {
    if (ctx == null) {
      return null;
    }

    this.visitSimpleId(ctx.simpleId());
    this.visitTerminal(ctx.REVERSE_QUOTE_ID());
    this.visitTerminal(ctx.CHARSET_REVERSE_QOUTE_STRING());

    return null;
  }

  @Override
  public Void visitSimpleId(SimpleIdContext ctx) {

    ParseTree child0 = ctx.getChild(0);

    if (child0 instanceof TerminalNode) {
      this.visitTerminal(ctx.ID());
    } else if (child0 instanceof CharsetNameBaseContext) {
      this.visitCharsetNameBase(ctx.charsetNameBase());
    } else if (child0 instanceof TransactionLevelBaseContext) {
      this.visitTransactionLevelBase(ctx.transactionLevelBase());
    } else if (child0 instanceof EngineNameContext) {
      this.visitEngineName(ctx.engineName());
    } else if (child0 instanceof PrivilegesBaseContext) {
      this.visitPrivilegesBase(ctx.privilegesBase());
    } else if (child0 instanceof IntervalTypeBaseContext) {
      this.visitIntervalTypeBase(ctx.intervalTypeBase());
    } else if (child0 instanceof DataTypeBaseContext) {
      this.visitDataTypeBase(ctx.dataTypeBase());
    } else if (child0 instanceof KeywordsCanBeIdContext) {
      this.visitKeywordsCanBeId(ctx.keywordsCanBeId());
    } else if (child0 instanceof FunctionNameBaseContext) {
      this.visitFunctionNameBase(ctx.functionNameBase());
    } else {
      throw new UnsupportedOperationException();
    }

    return null;
  }

  @Override
  public Void visitDottedId(DottedIdContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitDecimalLiteral(DecimalLiteralContext ctx) {
    if (ctx == null) {
      return null;
    }

    this.visitTerminal(ctx.DECIMAL_LITERAL());
    this.visitTerminal(ctx.ZERO_DECIMAL());
    this.visitTerminal(ctx.ONE_DECIMAL());
    this.visitTerminal(ctx.TWO_DECIMAL());

    return null;
  }

  @Override
  public Void visitFileSizeLiteral(FileSizeLiteralContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitStringLiteral(StringLiteralContext ctx) {
    if (ctx == null) {
      return null;
    }

    for (ParseTree child : ctx.children) {
      if (child instanceof TerminalNode) {
        this.visitTerminal((TerminalNode) child);
      } else if (child instanceof CollationNameContext) {
        this.visitCollationName((CollationNameContext) child);
      } else {
        throw new UnsupportedOperationException();
      }
    }

    return null;
  }

  @Override
  public Void visitBooleanLiteral(BooleanLiteralContext ctx) {
    if (ctx == null) {
      return null;
    }

    this.visitTerminal((TerminalNode) ctx.getChild(0));
    return null;
  }

  @Override
  public Void visitHexadecimalLiteral(HexadecimalLiteralContext ctx) {
    if (ctx == null) {
      return null;
    }

    this.visitTerminal((TerminalNode) ctx.getChild(0));
    return null;
  }

  @Override
  public Void visitNullNotnull(NullNotnullContext ctx) {
    if (ctx == null) {
      return null;
    }

    this.visitTerminal(ctx.NOT());
    this.visitTerminal(ctx.NULL_LITERAL());
    this.visitTerminal(ctx.NULL_SPEC_LITERAL());
    return null;
  }

  @Override
  public Void visitConstant(ConstantContext ctx) {
    if (ctx == null) {
      return null;
    }

    for (ParseTree child : ctx.children) {
      if (child instanceof TerminalNode) {
        this.visitTerminal((TerminalNode) child);
      } else if (child instanceof StringLiteralContext) {
        this.visitStringLiteral((StringLiteralContext) child);
      } else if (child instanceof DecimalLiteralContext) {
        this.visitDecimalLiteral((DecimalLiteralContext) child);
      } else if (child instanceof HexadecimalLiteralContext) {
        this.visitHexadecimalLiteral((HexadecimalLiteralContext) child);
      } else if (child instanceof BooleanLiteralContext) {
        this.visitBooleanLiteral((BooleanLiteralContext) child);
      } else {
        throw new UnsupportedOperationException();
      }
    }
    return null;
  }

  private Void visitDataType(DataTypeContext ctx) {
    if (ctx instanceof StringDataTypeContext) {

    } else if (ctx instanceof NationalStringDataTypeContext) {
      this.visitNationalStringDataType((NationalStringDataTypeContext) ctx);
    } else if (ctx instanceof NationalStringDataTypeContext) {
      this.visitNationalStringDataType((NationalStringDataTypeContext) ctx);
    } else if (ctx instanceof NationalVaryingStringDataTypeContext) {
      this.visitNationalVaryingStringDataType((NationalVaryingStringDataTypeContext) ctx);
    } else if (ctx instanceof DimensionDataTypeContext) {
      this.visitDimensionDataType((DimensionDataTypeContext) ctx);
    } else if (ctx instanceof DimensionDataTypeContext) {
      this.visitDimensionDataType((DimensionDataTypeContext) ctx);
    } else if (ctx instanceof DimensionDataTypeContext) {
      this.visitDimensionDataType((DimensionDataTypeContext) ctx);
    } else if (ctx instanceof DimensionDataTypeContext) {
      this.visitDimensionDataType((DimensionDataTypeContext) ctx);
    } else if (ctx instanceof SimpleDataTypeContext) {
      this.visitSimpleDataType((SimpleDataTypeContext) ctx);
    } else if (ctx instanceof DimensionDataTypeContext) {
      this.visitDimensionDataType((DimensionDataTypeContext) ctx);
    } else if (ctx instanceof CollectionDataTypeContext) {
      this.visitCollectionDataType((CollectionDataTypeContext) ctx);
    } else if (ctx instanceof SpatialDataTypeContext) {
      this.visitSpatialDataType((SpatialDataTypeContext) ctx);
    } else {
      if (ctx == null) {
        return null;
      }
      throw new UnsupportedOperationException();
    }

    return null;
  }

  @Override
  public Void visitStringDataType(StringDataTypeContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitNationalStringDataType(NationalStringDataTypeContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitNationalVaryingStringDataType(NationalVaryingStringDataTypeContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitDimensionDataType(DimensionDataTypeContext ctx) {
    if (ctx == null) {
      return null;
    }

    for (ParseTree child : ctx.children) {
      if (child instanceof TerminalNode) {
        this.visitTerminal((TerminalNode) child);
      } else if (child instanceof LengthOneDimensionContext) {
        this.visitLengthOneDimension((LengthOneDimensionContext) child);
      } else if (child instanceof LengthTwoDimensionContext) {
        this.visitLengthTwoDimension((LengthTwoDimensionContext) child);
      } else if (child instanceof LengthTwoOptionalDimensionContext) {
        this.visitLengthTwoOptionalDimension((LengthTwoOptionalDimensionContext) child);
      } else {
        throw new UnsupportedOperationException();
      }
    }

    return null;
  }

  @Override
  public Void visitSimpleDataType(SimpleDataTypeContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitCollectionDataType(CollectionDataTypeContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitSpatialDataType(SpatialDataTypeContext ctx) {
    if (ctx == null) {
      return null;
    }

    this.visitTerminal(ctx.GEOMETRYCOLLECTION());
    this.visitTerminal(ctx.GEOMCOLLECTION());
    this.visitTerminal(ctx.LINESTRING());
    this.visitTerminal(ctx.MULTILINESTRING());
    this.visitTerminal(ctx.MULTIPOINT());
    this.visitTerminal(ctx.MULTIPOLYGON());
    this.visitTerminal(ctx.POINT());
    this.visitTerminal(ctx.POLYGON());
    this.visitTerminal(ctx.JSON());
    this.visitTerminal(ctx.GEOMETRY());

    return null;
  }

  @Override
  public Void visitCollectionOptions(CollectionOptionsContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitConvertedDataType(ConvertedDataTypeContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitLengthOneDimension(LengthOneDimensionContext ctx) {
    if (ctx == null) {
      return null;
    }

    for (ParseTree child : ctx.children) {
      if (child instanceof TerminalNode) {
        this.visitTerminal((TerminalNode) child);
      } else if (child instanceof DecimalLiteralContext) {
        this.visitDecimalLiteral((DecimalLiteralContext) child);
      } else {
        throw new UnsupportedOperationException();
      }
    }

    return null;
  }

  @Override
  public Void visitLengthTwoDimension(LengthTwoDimensionContext ctx) {
    if (ctx == null) {
      return null;
    }

    for (ParseTree child : ctx.children) {
      if (child instanceof TerminalNode) {
        this.visitTerminal((TerminalNode) child);
      } else if (child instanceof DecimalLiteralContext) {
        this.visitDecimalLiteral((DecimalLiteralContext) child);
      } else {
        throw new UnsupportedOperationException();
      }
    }
    return null;
  }

  @Override
  public Void visitLengthTwoOptionalDimension(LengthTwoOptionalDimensionContext ctx) {
    if (ctx == null) {
      return null;
    }

    for (ParseTree child : ctx.children) {
      if (child instanceof TerminalNode) {
        this.visitTerminal((TerminalNode) child);
      } else if (child instanceof DecimalLiteralContext) {
        this.visitDecimalLiteral((DecimalLiteralContext) child);
      } else {
        throw new UnsupportedOperationException();
      }
    }
    return null;
  }

  @Override
  public Void visitUidList(UidListContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitTables(TablesContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitIndexColumnNames(IndexColumnNamesContext ctx) {
    if (ctx == null) {
      return null;
    }

    List<ParseTree> children = ctx.children;
    for (ParseTree child : children) {
      if (child instanceof TerminalNode) {
        this.visitTerminal((TerminalNode) child);
      } else if (child instanceof IndexColumnNameContext) {
        this.visitIndexColumnName((IndexColumnNameContext) child);
      }
    }

    return null;
  }

  @Override
  public Void visitExpressions(ExpressionsContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitExpressionsWithDefaults(ExpressionsWithDefaultsContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitConstants(ConstantsContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitSimpleStrings(SimpleStringsContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitUserVariables(UserVariablesContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitDefaultValue(DefaultValueContext ctx) {
    if (ctx == null) {
      return null;
    }

    ParseTree child0 = ctx.getChild(0);
    if (child0 instanceof TerminalNode) {
      this.visitTerminal(ctx.NULL_LITERAL());
    } else if (child0 instanceof UnaryOperatorContext) {
      this.visitUnaryOperator((UnaryOperatorContext) child0);
      this.visitConstant((ConstantContext) ctx.getChild(1));
    } else if (child0 instanceof ConstantContext) {
      this.visitConstant((ConstantContext) child0);
    } else if (child0 instanceof CurrentTimestampContext) {
      for (ParseTree child : ctx.children) {
        if (child instanceof TerminalNode) {
          this.visitTerminal((TerminalNode) child);
        } else if (child instanceof CurrentTimestampContext) {
          this.visitCurrentTimestamp((CurrentTimestampContext) child);
        } else {
          throw new UnsupportedOperationException();
        }
      }
    } else {
      throw new UnsupportedOperationException();
    }

    return null;
  }

  @Override
  public Void visitCurrentTimestamp(CurrentTimestampContext ctx) {
    if (ctx == null) {
      return null;
    }

    for (ParseTree child : ctx.children) {
      if (child instanceof TerminalNode) {
        this.visitTerminal((TerminalNode) child);
      } else if (child instanceof DecimalLiteralContext) {
        this.visitDecimalLiteral((DecimalLiteralContext) child);
      } else {
        throw new UnsupportedOperationException();
      }
    }

    return null;
  }

  @Override
  public Void visitExpressionOrDefault(ExpressionOrDefaultContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitIfExists(IfExistsContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitIfNotExists(IfNotExistsContext ctx) {
    if (ctx == null) {
      return null;
    }

    this.visitTerminal(ctx.IF());
    this.visitTerminal(ctx.NOT());
    this.visitTerminal(ctx.EXISTS());

    return null;
  }

  @Override
  public Void visitSpecificFunctionCall(SpecificFunctionCallContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitAggregateFunctionCall(AggregateFunctionCallContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitScalarFunctionCall(ScalarFunctionCallContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitUdfFunctionCall(UdfFunctionCallContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitPasswordFunctionCall(PasswordFunctionCallContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitSimpleFunctionCall(SimpleFunctionCallContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitDataTypeFunctionCall(DataTypeFunctionCallContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitValuesFunctionCall(ValuesFunctionCallContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitCaseFunctionCall(CaseFunctionCallContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitCharFunctionCall(CharFunctionCallContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitPositionFunctionCall(PositionFunctionCallContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitSubstrFunctionCall(SubstrFunctionCallContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitTrimFunctionCall(TrimFunctionCallContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitWeightFunctionCall(WeightFunctionCallContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitExtractFunctionCall(ExtractFunctionCallContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitGetFormatFunctionCall(GetFormatFunctionCallContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitCaseFuncAlternative(CaseFuncAlternativeContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitLevelWeightList(LevelWeightListContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitLevelWeightRange(LevelWeightRangeContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitLevelInWeightListElement(LevelInWeightListElementContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitAggregateWindowedFunction(AggregateWindowedFunctionContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitScalarFunctionName(ScalarFunctionNameContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitPasswordFunctionClause(PasswordFunctionClauseContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitFunctionArgs(FunctionArgsContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitFunctionArg(FunctionArgContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitIsExpression(IsExpressionContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  private Void visitExpression(ExpressionContext ctx) {
    if (ctx == null) {
      return null;
    }

    if (ctx instanceof NotExpressionContext) {
      this.visitNotExpression((NotExpressionContext) ctx);
    } else if (ctx instanceof LogicalExpressionContext) {
      this.visitLogicalExpression((LogicalExpressionContext) ctx);
    } else if (ctx instanceof IsExpressionContext) {
      this.visitIsExpression((IsExpressionContext) ctx);
    } else if (ctx instanceof PredicateExpressionContext) {
      this.visitPredicateExpression((PredicateExpressionContext) ctx);
    } else {
      throw new UnsupportedOperationException();
    }

    return null;
  }

  @Override
  public Void visitNotExpression(NotExpressionContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitLogicalExpression(LogicalExpressionContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitPredicateExpression(PredicateExpressionContext ctx) {
    if (ctx == null) {
      return null;
    }

    this.visitPredicate((PredicateContext) ctx.getChild(0));
    return null;
  }

  @Override
  public Void visitSoundsLikePredicate(SoundsLikePredicateContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitExpressionAtomPredicate(ExpressionAtomPredicateContext ctx) {
    if (ctx == null) {
      return null;
    }

    this.visitTerminal(ctx.LOCAL_ID());
    this.visitTerminal(ctx.VAR_ASSIGN());
    this.visitExpressionAtom(ctx.expressionAtom());

    return null;
  }

  private Void visitExpressionAtom(ExpressionAtomContext ctx) {
    if (ctx == null) {
      return null;
    }

    if (ctx instanceof ConstantExpressionAtomContext) {
      this.visitConstantExpressionAtom((ConstantExpressionAtomContext) ctx);
    } else if (ctx instanceof FullColumnNameExpressionAtomContext) {
      this.visitFullColumnNameExpressionAtom((FullColumnNameExpressionAtomContext) ctx);
    } else if (ctx instanceof FunctionCallExpressionAtomContext) {
      this.visitFunctionCallExpressionAtom((FunctionCallExpressionAtomContext) ctx);
    } else if (ctx instanceof CollateExpressionAtomContext) {
      this.visitCollateExpressionAtom((CollateExpressionAtomContext) ctx);
    } else if (ctx instanceof MysqlVariableExpressionAtomContext) {
      this.visitMysqlVariableExpressionAtom((MysqlVariableExpressionAtomContext) ctx);
    } else if (ctx instanceof UnaryExpressionAtomContext) {
      this.visitUnaryExpressionAtom((UnaryExpressionAtomContext) ctx);
    } else if (ctx instanceof BinaryExpressionAtomContext) {
      this.visitBinaryExpressionAtom((BinaryExpressionAtomContext) ctx);
    } else if (ctx instanceof NestedExpressionAtomContext) {
      this.visitNestedExpressionAtom((NestedExpressionAtomContext) ctx);
    } else if (ctx instanceof NestedRowExpressionAtomContext) {
      this.visitNestedRowExpressionAtom((NestedRowExpressionAtomContext) ctx);
    } else if (ctx instanceof ExistsExpessionAtomContext) {
      this.visitExistsExpessionAtom((ExistsExpessionAtomContext) ctx);
    } else if (ctx instanceof SubqueryExpessionAtomContext) {
      this.visitSubqueryExpessionAtom((SubqueryExpessionAtomContext) ctx);
    } else if (ctx instanceof IntervalExpressionAtomContext) {
      this.visitIntervalExpressionAtom((IntervalExpressionAtomContext) ctx);
    } else if (ctx instanceof BitExpressionAtomContext) {
      this.visitBitExpressionAtom((BitExpressionAtomContext) ctx);
    } else if (ctx instanceof MathExpressionAtomContext) {
      this.visitMathExpressionAtom((MathExpressionAtomContext) ctx);
    } else {
      throw new UnsupportedOperationException();
    }

    return null;
  }

  private Void visitPredicate(PredicateContext ctx) {
    if (ctx == null) {
      return null;
    }

    if (ctx instanceof InPredicateContext) {
      this.visitInPredicate((InPredicateContext) ctx);
    } else if (ctx instanceof IsNullPredicateContext) {
      this.visitIsNullPredicate((IsNullPredicateContext) ctx);
    } else if (ctx instanceof BinaryComparasionPredicateContext) {
      this.visitBinaryComparasionPredicate((BinaryComparasionPredicateContext) ctx);
    } else if (ctx instanceof SubqueryComparasionPredicateContext) {
      this.visitSubqueryComparasionPredicate((SubqueryComparasionPredicateContext) ctx);
    } else if (ctx instanceof BetweenPredicateContext) {
      this.visitBetweenPredicate((BetweenPredicateContext) ctx);
    } else if (ctx instanceof SoundsLikePredicateContext) {
      this.visitSoundsLikePredicate((SoundsLikePredicateContext) ctx);
    } else if (ctx instanceof LikePredicateContext) {
      this.visitLikePredicate((LikePredicateContext) ctx);
    } else if (ctx instanceof RegexpPredicateContext) {
      this.visitRegexpPredicate((RegexpPredicateContext) ctx);
    } else if (ctx instanceof ExpressionAtomPredicateContext) {
      this.visitExpressionAtomPredicate((ExpressionAtomPredicateContext) ctx);
    } else {
      throw new UnsupportedOperationException();
    }

    return null;
  }

  @Override
  public Void visitInPredicate(InPredicateContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitSubqueryComparasionPredicate(SubqueryComparasionPredicateContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitBetweenPredicate(BetweenPredicateContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitBinaryComparasionPredicate(BinaryComparasionPredicateContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitIsNullPredicate(IsNullPredicateContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitLikePredicate(LikePredicateContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitRegexpPredicate(RegexpPredicateContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitUnaryExpressionAtom(UnaryExpressionAtomContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitCollateExpressionAtom(CollateExpressionAtomContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitSubqueryExpessionAtom(SubqueryExpessionAtomContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitMysqlVariableExpressionAtom(MysqlVariableExpressionAtomContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitNestedExpressionAtom(NestedExpressionAtomContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitNestedRowExpressionAtom(NestedRowExpressionAtomContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitMathExpressionAtom(MathExpressionAtomContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitIntervalExpressionAtom(IntervalExpressionAtomContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitExistsExpessionAtom(ExistsExpessionAtomContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitConstantExpressionAtom(ConstantExpressionAtomContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitFunctionCallExpressionAtom(FunctionCallExpressionAtomContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitBinaryExpressionAtom(BinaryExpressionAtomContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitFullColumnNameExpressionAtom(FullColumnNameExpressionAtomContext ctx) {
    if (ctx == null) {
      return null;
    }
    this.visitFullColumnName((FullColumnNameContext) ctx.getChild(0));
    return null;
  }

  @Override
  public Void visitBitExpressionAtom(BitExpressionAtomContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitUnaryOperator(UnaryOperatorContext ctx) {
    if (ctx == null) {
      return null;
    }

    this.visitTerminal((TerminalNode) ctx.getChild(0));

    return null;
  }

  @Override
  public Void visitComparisonOperator(ComparisonOperatorContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitLogicalOperator(LogicalOperatorContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitBitOperator(BitOperatorContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitMathOperator(MathOperatorContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Void visitCharsetNameBase(CharsetNameBaseContext ctx) {
    System.err.print(ctx.getText() + " ");
    return null;
  }

  @Override
  public Void visitTransactionLevelBase(TransactionLevelBaseContext ctx) {
    System.err.print(ctx.getText() + " ");
    return null;
  }

  @Override
  public Void visitPrivilegesBase(PrivilegesBaseContext ctx) {
    System.err.print(ctx.getText() + " ");
    return null;
  }

  @Override
  public Void visitIntervalTypeBase(IntervalTypeBaseContext ctx) {
    System.err.print(ctx.getText() + " ");
    return null;
  }

  @Override
  public Void visitDataTypeBase(DataTypeBaseContext ctx) {
    System.err.print(ctx.getText() + " ");
    return null;
  }

  @Override
  public Void visitKeywordsCanBeId(KeywordsCanBeIdContext ctx) {
    System.err.print(ctx.getText() + " ");
    return null;
  }

  @Override
  public Void visitFunctionNameBase(FunctionNameBaseContext ctx) {
    System.err.print(ctx.getText() + " ");
    return null;
  }

}
