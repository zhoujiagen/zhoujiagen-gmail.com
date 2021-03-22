package com.spike.giantdataanalysis.rdfstore.sql;

import java.util.List;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.apache.commons.collections4.CollectionUtils;

import com.google.common.collect.Lists;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalBitOperatorEnum;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalComparisonOperatorEnum;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalLogicalOperatorEnum;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalMathOperatorEnum;
import com.spike.giantdataanalysis.model.logic.relational.core.RelationalUnaryOperatorEnum;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.*;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.AdministrationStatement.ChannelFlushOption;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.AdministrationStatement.CurrentSchemaPriviLevel;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.AdministrationStatement.DefiniteFullTablePrivLevel;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.AdministrationStatement.DefiniteSchemaPrivLevel;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.AdministrationStatement.DefiniteTablePrivLevel;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.AdministrationStatement.FlushOption;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.AdministrationStatement.GlobalPrivLevel;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.AdministrationStatement.HashAuthOption;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.AdministrationStatement.LoadedTableIndexes;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.AdministrationStatement.PasswordAuthOption;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.AdministrationStatement.PrivelegeClause;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.AdministrationStatement.PrivilegeLevel;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.AdministrationStatement.RenameUserClause;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.AdministrationStatement.ShowFilter;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.AdministrationStatement.SimpleAuthOption;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.AdministrationStatement.SimpleFlushOption;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.AdministrationStatement.StringAuthOption;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.AdministrationStatement.TableFlushOption;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.AdministrationStatement.TableIndexes;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.AdministrationStatement.TlsOption;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.AdministrationStatement.UserAuthOption;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.AdministrationStatement.UserPasswordOption;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.AdministrationStatement.UserResourceOption;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.AdministrationStatement.UserSpecification;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.AdministrationStatement.VariableClause;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.AlterDatabase.AlterSimpleDatabase;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.AlterDatabase.AlterUpgradeName;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.AlterUser.AlterUserMysqlV56;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.AlterUser.AlterUserMysqlV57;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CommonExpressons.CurrentTimestamp;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CommonExpressons.DefaultValue;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CommonExpressons.ExpressionOrDefault;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CommonExpressons.IfExists;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CommonExpressons.IfNotExists;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CommonLists.Constants;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CommonLists.Expressions;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CommonLists.ExpressionsWithDefaults;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CommonLists.IndexColumnNames;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CommonLists.SimpleStrings;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CommonLists.Tables;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CommonLists.UidList;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CommonLists.UserVariables;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CompoundStatement.CaseAlternative;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CompoundStatement.DeclareCondition;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CompoundStatement.DeclareCursor;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CompoundStatement.DeclareHandler;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CompoundStatement.DeclareVariable;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CompoundStatement.ElifAlternative;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CompoundStatement.HandlerConditionCode;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CompoundStatement.HandlerConditionException;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CompoundStatement.HandlerConditionName;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CompoundStatement.HandlerConditionNotfound;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CompoundStatement.HandlerConditionState;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CompoundStatement.HandlerConditionValue;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CompoundStatement.HandlerConditionWarning;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CompoundStatement.ProcedureSqlStatement;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CompoundStatement.RoutineBody;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CreateTable.ColumnCreateTable;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CreateTable.CopyCreateTable;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CreateTable.QueryCreateTable;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CreateUser.CreateUserMysqlV56;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CreateUser.CreateUserMysqlV57;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CursorStatement.CloseCursor;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CursorStatement.FetchCursor;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.CursorStatement.OpenCursor;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.AuthPlugin;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.CharsetName;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.CollationName;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.DottedId;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.EngineName;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.FullColumnName;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.FullId;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.IndexColumnName;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.MysqlVariable;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.SimpleId;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.TableName;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.Uid;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.UserName;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.UuidSet;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.Xid;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DBObjects.XuidStringId;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DataType.CollectionDataType;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DataType.CollectionOptions;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DataType.ConvertedDataType;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DataType.DimensionDataType;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DataType.LengthOneDimension;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DataType.LengthTwoDimension;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DataType.LengthTwoOptionalDimension;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DataType.NationalStringDataType;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DataType.NationalVaryingStringDataType;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DataType.SimpleDataType;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DataType.SpatialDataType;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DataType.StringDataType;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DdlStatement.*;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DeleteStatement.MultipleDeleteStatement;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DeleteStatement.SingleDeleteStatement;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DmlStatement.AssignmentField;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DmlStatement.AtomTableItem;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DmlStatement.IndexHint;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DmlStatement.InnerJoin;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DmlStatement.InsertStatementValue;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DmlStatement.JoinPart;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DmlStatement.NaturalJoin;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DmlStatement.OrderByClause;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DmlStatement.OrderByExpression;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DmlStatement.OuterJoin;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DmlStatement.StraightJoin;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DmlStatement.SubqueryTableItem;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DmlStatement.TableSource;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DmlStatement.TableSourceBase;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DmlStatement.TableSourceItem;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DmlStatement.TableSourceNested;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DmlStatement.TableSources;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DmlStatement.TableSourcesItem;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.DmlStatement.UpdatedElement;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Expression.BetweenPredicate;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Expression.BinaryComparasionPredicate;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Expression.ExpressionAtomPredicate;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Expression.InPredicate;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Expression.IsExpression;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Expression.IsNullPredicate;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Expression.LikePredicate;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Expression.LogicalExpression;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Expression.NotExpression;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Expression.PredicateExpression;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Expression.RegexpPredicate;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Expression.SoundsLikePredicate;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Expression.SubqueryComparasionPredicate;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ExpressionAtom.BinaryExpressionAtom;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ExpressionAtom.BitExpressionAtom;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ExpressionAtom.Collate;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ExpressionAtom.ExistsExpessionAtom;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ExpressionAtom.IntervalExpressionAtom;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ExpressionAtom.MathExpressionAtom;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ExpressionAtom.NestedExpressionAtom;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ExpressionAtom.NestedRowExpressionAtom;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ExpressionAtom.SubqueryExpessionAtom;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ExpressionAtom.UnaryExpressionAtom;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Functions.AggregateWindowedFunction;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Functions.CaseFuncAlternative;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Functions.CaseFunctionCall;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Functions.CharFunctionCall;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Functions.DataTypeFunctionCall;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Functions.ExtractFunctionCall;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Functions.FunctionArg;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Functions.FunctionArgs;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Functions.FunctionCall;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Functions.GetFormatFunctionCall;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Functions.LevelInWeightListElement;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Functions.LevelWeightList;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Functions.LevelWeightRange;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Functions.LevelsInWeightString;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Functions.PasswordFunctionClause;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Functions.PositionFunctionCall;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Functions.ScalarFunctionCall;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Functions.SimpleFunctionCall;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Functions.SpecificFunction;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Functions.SubstrFunctionCall;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Functions.TrimFunctionCall;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Functions.UdfFunctionCall;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Functions.ValuesFunctionCall;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Functions.WeightFunctionCall;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.HandlerStatement.HandlerCloseStatement;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.HandlerStatement.HandlerOpenStatement;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.HandlerStatement.HandlerReadIndexStatement;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.HandlerStatement.HandlerReadStatement;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Literals.BooleanLiteral;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Literals.Constant;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Literals.DecimalLiteral;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Literals.FileSizeLiteral;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Literals.HexadecimalLiteral;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Literals.NullNotnull;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.Literals.StringLiteral;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ReplicationStatement.ChannelOption;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ReplicationStatement.ConnectionOption;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ReplicationStatement.DoDbReplication;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ReplicationStatement.DoTableReplication;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ReplicationStatement.GtidsUntilOption;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ReplicationStatement.GtuidSet;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ReplicationStatement.IgnoreDbReplication;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ReplicationStatement.IgnoreTableReplication;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ReplicationStatement.MasterBoolOption;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ReplicationStatement.MasterDecimalOption;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ReplicationStatement.MasterLogUntilOption;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ReplicationStatement.MasterOption;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ReplicationStatement.MasterRealOption;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ReplicationStatement.MasterStringOption;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ReplicationStatement.MasterUidListOption;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ReplicationStatement.RelayLogUntilOption;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ReplicationStatement.ReplicationFilter;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ReplicationStatement.RewriteDbReplication;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ReplicationStatement.SqlGapsUntilOption;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ReplicationStatement.TablePair;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ReplicationStatement.UntilOption;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ReplicationStatement.WildDoTableReplication;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ReplicationStatement.WildIgnoreTableReplication;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.RevokeStatement.DetailRevoke;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.RevokeStatement.ShortRevoke;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.SelectStatement.FromClause;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.SelectStatement.GroupByItem;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.SelectStatement.LimitClause;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.SelectStatement.LimitClauseAtom;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.SelectStatement.ParenthesisSelect;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.SelectStatement.QueryExpression;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.SelectStatement.QueryExpressionNointo;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.SelectStatement.QuerySpecification;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.SelectStatement.QuerySpecificationNointo;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.SelectStatement.SelectColumnElement;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.SelectStatement.SelectElement;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.SelectStatement.SelectElements;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.SelectStatement.SelectExpressionElement;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.SelectStatement.SelectFieldsInto;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.SelectStatement.SelectFunctionElement;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.SelectStatement.SelectIntoDumpFile;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.SelectStatement.SelectIntoExpression;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.SelectStatement.SelectIntoTextFile;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.SelectStatement.SelectIntoVariables;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.SelectStatement.SelectLinesInto;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.SelectStatement.SelectStarElement;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.SelectStatement.SimpleSelect;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.SelectStatement.UnionParenthesis;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.SelectStatement.UnionParenthesisSelect;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.SelectStatement.UnionSelect;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.SelectStatement.UnionStatement;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.SetStatement.SetCharset;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.SetStatement.SetNames;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.SetStatement.SetNewValueInsideTrigger;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.SetStatement.SetPasswordStatement;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.SetStatement.SetVariable;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ShowStatement.ShowColumns;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ShowStatement.ShowCountErrors;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ShowStatement.ShowCreateDb;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ShowStatement.ShowCreateFullIdObject;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ShowStatement.ShowCreateUser;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ShowStatement.ShowEngine;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ShowStatement.ShowErrors;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ShowStatement.ShowGlobalInfo;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ShowStatement.ShowGrants;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ShowStatement.ShowIndexes;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ShowStatement.ShowLogEvents;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ShowStatement.ShowMasterLogs;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ShowStatement.ShowObjectFilter;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ShowStatement.ShowOpenTables;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ShowStatement.ShowProfile;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ShowStatement.ShowRoutine;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ShowStatement.ShowSchemaFilter;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.ShowStatement.ShowSlaveStatus;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.TransactionStatement.LockAction;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.TransactionStatement.LockTableElement;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.TransactionStatement.SetAutocommitStatement;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.TransactionStatement.SetTransactionStatement;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.TransactionStatement.TransactionOption;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.UpdateStatement.MultipleUpdateStatement;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.UpdateStatement.SingleUpdateStatement;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.UtilityStatement.DescribeConnection;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.UtilityStatement.DescribeObjectClause;
import com.spike.giantdataanalysis.model.logic.relational.expression.raw.UtilityStatement.DescribeStatements;

/**
 * MySQL的SQL解析器Visitor的实现: 提取关系代数抽象.
 * <p>
 * 说明: 泛型参数选择了{@link Object}, 返回值的具体类型解释留给应用端处理.
 * <p>
 * 规则别名(#xxx) 应该使用类定义.
 */
public class RelationalAlgebraMySqlParserVisitor implements MySqlParserVisitor<Object> {

  @Override
  public Object visit(ParseTree tree) {
    throw ParserError.make("call more specific methods!");
  }

  @Override
  public Object visitChildren(RuleNode node) {
    throw ParserError.make("call more specific methods!");
  }

  @Override
  public String visitTerminal(TerminalNode node) {
    if (node == null) {
      return null;
    }

    return node.getSymbol().getText();
  }

  @Override
  public String visitErrorNode(ErrorNode node) {
    if (node == null) {
      return null;
    }

    return node.getSymbol().getText();
  }

  @Override
  public SqlStatements visitRoot(MySqlParser.RootContext ctx) {
    if (ctx == null) {
      return null;
    }

    return this.visitSqlStatements(ctx.sqlStatements());
  }

  @Override
  public SqlStatements visitSqlStatements(MySqlParser.SqlStatementsContext ctx) {
    if (ctx == null) {
      return null;
    }

    List<MySqlParser.SqlStatementContext> sqlStatementContexts = ctx.sqlStatement();

    if (CollectionUtils.isNotEmpty(sqlStatementContexts)) {
      List<SqlStatement> sqlStatements = Lists.newArrayList();
      for (MySqlParser.SqlStatementContext sqlStatementContext : sqlStatementContexts) {
        SqlStatement sqlStatement = this.visitSqlStatement(sqlStatementContext);
        sqlStatements.add(sqlStatement);
      }
      return RelationalAlgebraExpressionFactory.makeSqlStatements(sqlStatements);
    } else {
      throw ParserError.make(ctx);
    }
  }

  @Override
  public SqlStatement visitSqlStatement(MySqlParser.SqlStatementContext ctx) {
    if (ctx == null) {
      return null;
    }

    if (ctx.ddlStatement() != null) {
      return this.visitDdlStatement(ctx.ddlStatement());
    } else if (ctx.dmlStatement() != null) {
      return this.visitDmlStatement(ctx.dmlStatement());
    } else if (ctx.transactionStatement() != null) {
      return this.visitTransactionStatement(ctx.transactionStatement());
    } else if (ctx.replicationStatement() != null) {
      return this.visitReplicationStatement(ctx.replicationStatement());
    } else if (ctx.preparedStatement() != null) {
      return this.visitPreparedStatement(ctx.preparedStatement());
    } else if (ctx.administrationStatement() != null) {
      return this.visitAdministrationStatement(ctx.administrationStatement());
    } else if (ctx.utilityStatement() != null) {
      return this.visitUtilityStatement(ctx.utilityStatement());
    } else {
      throw ParserError.make(ctx);
    }
  }

  @Override
  public String visitEmptyStatement(MySqlParser.EmptyStatementContext ctx) {
    if (ctx == null) {
      return null;
    }

    return ctx.SEMI().getText();
  }

  @Override
  public DdlStatement visitDdlStatement(MySqlParser.DdlStatementContext ctx) {
    if (ctx == null) {
      return null;
    }

    if (ctx.createDatabase() != null) {
      return this.visitCreateDatabase(ctx.createDatabase());
    } else if (ctx.createEvent() != null) {
      return this.visitCreateEvent(ctx.createEvent());
    } else if (ctx.createIndex() != null) {
      return this.visitCreateIndex(ctx.createIndex());
    } else if (ctx.createLogfileGroup() != null) {
      return this.visitCreateLogfileGroup(ctx.createLogfileGroup());
    } else if (ctx.createProcedure() != null) {
      return this.visitCreateProcedure(ctx.createProcedure());
    } else if (ctx.createFunction() != null) {
      return this.visitCreateFunction(ctx.createFunction());
    } else if (ctx.createServer() != null) {
      return this.visitCreateServer(ctx.createServer());
    } else if (ctx.createTable() != null) {
      return this.visitCreateTable(ctx.createTable());
    } else if (ctx.createTablespaceInnodb() != null) {
      return this.visitCreateTablespaceInnodb(ctx.createTablespaceInnodb());
    } else if (ctx.createTablespaceNdb() != null) {
      return this.visitCreateTablespaceNdb(ctx.createTablespaceNdb());
    } else if (ctx.createTrigger() != null) {
      return this.visitCreateTrigger(ctx.createTrigger());
    } else if (ctx.createView() != null) {
      return this.visitCreateView(ctx.createView());
    } else if (ctx.alterDatabase() != null) {
      return this.visitAlterDatabase(ctx.alterDatabase());
    } else if (ctx.alterEvent() != null) {
      return this.visitAlterEvent(ctx.alterEvent());
    } else if (ctx.alterFunction() != null) {
      return this.visitAlterFunction(ctx.alterFunction());
    } else if (ctx.alterInstance() != null) {
      return this.visitAlterInstance(ctx.alterInstance());
    } else if (ctx.alterLogfileGroup() != null) {
      return this.visitAlterLogfileGroup(ctx.alterLogfileGroup());
    } else if (ctx.alterProcedure() != null) {
      return this.visitAlterProcedure(ctx.alterProcedure());
    } else if (ctx.alterServer() != null) {
      return this.visitAlterServer(ctx.alterServer());
    } else if (ctx.alterTable() != null) {
      return this.visitAlterTable(ctx.alterTable());
    } else if (ctx.alterTablespace() != null) {
      return this.visitAlterTablespace(ctx.alterTablespace());
    } else if (ctx.alterView() != null) {
      return this.visitAlterView(ctx.alterView());
    } else if (ctx.dropDatabase() != null) {
      return this.visitDropDatabase(ctx.dropDatabase());
    } else if (ctx.dropEvent() != null) {
      return this.visitDropEvent(ctx.dropEvent());
    } else if (ctx.dropIndex() != null) {
      return this.visitDropIndex(ctx.dropIndex());
    } else if (ctx.dropLogfileGroup() != null) {
      return this.visitDropLogfileGroup(ctx.dropLogfileGroup());
    } else if (ctx.dropProcedure() != null) {
      return this.visitDropProcedure(ctx.dropProcedure());
    } else if (ctx.dropFunction() != null) {
      return this.visitDropFunction(ctx.dropFunction());
    } else if (ctx.dropServer() != null) {
      return this.visitDropServer(ctx.dropServer());
    } else if (ctx.dropTable() != null) {
      return this.visitDropTable(ctx.dropTable());
    } else if (ctx.dropTablespace() != null) {
      return this.visitDropTablespace(ctx.dropTablespace());
    } else if (ctx.dropTrigger() != null) {
      return this.visitDropTrigger(ctx.dropTrigger());
    } else if (ctx.dropView() != null) {
      return this.visitDropView(ctx.dropView());
    } else if (ctx.renameTable() != null) {
      return this.visitRenameTable(ctx.renameTable());
    } else if (ctx.truncateTable() != null) {
      return this.visitTruncateTable(ctx.truncateTable());
    } else {
      throw ParserError.make(ctx);
    }
  }

  @Override
  public DmlStatement visitDmlStatement(MySqlParser.DmlStatementContext ctx) {
    if (ctx == null) {
      return null;
    }

    if (ctx.selectStatement() != null) {
      return this.visitSelectStatement(ctx.selectStatement());
    } else if (ctx.insertStatement() != null) {
      return this.visitInsertStatement(ctx.insertStatement());
    } else if (ctx.updateStatement() != null) {
      return this.visitUpdateStatement(ctx.updateStatement());
    } else if (ctx.deleteStatement() != null) {
      return this.visitDeleteStatement(ctx.deleteStatement());
    } else if (ctx.replaceStatement() != null) {
      return this.visitReplaceStatement(ctx.replaceStatement());
    } else if (ctx.callStatement() != null) {
      return this.visitCallStatement(ctx.callStatement());
    } else if (ctx.loadDataStatement() != null) {
      return this.visitLoadDataStatement(ctx.loadDataStatement());
    } else if (ctx.loadXmlStatement() != null) {
      return this.visitLoadXmlStatement(ctx.loadXmlStatement());
    } else if (ctx.doStatement() != null) {
      return this.visitDoStatement(ctx.doStatement());
    } else if (ctx.handlerStatement() != null) {
      return this.visitHandlerStatement(ctx.handlerStatement());
    } else {
      throw ParserError.make(ctx);
    }

  }

  @Override
  public TransactionStatement
      visitTransactionStatement(MySqlParser.TransactionStatementContext ctx) {
    if (ctx == null) {
      return null;
    }

    if (ctx.startTransaction() != null) {
      return this.visitStartTransaction(ctx.startTransaction());
    } else if (ctx.beginWork() != null) {
      return this.visitBeginWork(ctx.beginWork());
    } else if (ctx.commitWork() != null) {
      return this.visitCommitWork(ctx.commitWork());
    } else if (ctx.rollbackWork() != null) {
      return this.visitRollbackWork(ctx.rollbackWork());
    } else if (ctx.savepointStatement() != null) {
      return this.visitSavepointStatement(ctx.savepointStatement());
    } else if (ctx.rollbackStatement() != null) {
      return this.visitRollbackStatement(ctx.rollbackStatement());
    } else if (ctx.releaseStatement() != null) {
      return this.visitReleaseStatement(ctx.releaseStatement());
    } else if (ctx.lockTables() != null) {
      return this.visitLockTables(ctx.lockTables());
    } else if (ctx.unlockTables() != null) {
      return this.visitUnlockTables(ctx.unlockTables());
    } else {
      throw ParserError.make(ctx);
    }
  }

  @Override
  public ReplicationStatement
      visitReplicationStatement(MySqlParser.ReplicationStatementContext ctx) {
    if (ctx == null) {
      return null;
    }

    if (ctx.changeMaster() != null) {
      return this.visitChangeMaster(ctx.changeMaster());
    } else if (ctx.changeReplicationFilter() != null) {
      return this.visitChangeReplicationFilter(ctx.changeReplicationFilter());
    } else if (ctx.purgeBinaryLogs() != null) {
      return this.visitPurgeBinaryLogs(ctx.purgeBinaryLogs());
    } else if (ctx.resetMaster() != null) {
      return this.visitResetMaster(ctx.resetMaster());
    } else if (ctx.resetSlave() != null) {
      return this.visitResetSlave(ctx.resetSlave());
    } else if (ctx.startSlave() != null) {
      return this.visitStartSlave(ctx.startSlave());
    } else if (ctx.stopSlave() != null) {
      return this.visitStopSlave(ctx.stopSlave());
    } else if (ctx.startGroupReplication() != null) {
      return this.visitStartGroupReplication(ctx.startGroupReplication());
    } else if (ctx.stopGroupReplication() != null) {
      return this.visitStopGroupReplication(ctx.stopGroupReplication());
    } else if (ctx.xaStartTransaction() != null) {
      return this.visitXaStartTransaction(ctx.xaStartTransaction());
    } else if (ctx.xaEndTransaction() != null) {
      return this.visitXaEndTransaction(ctx.xaEndTransaction());
    } else if (ctx.xaPrepareStatement() != null) {
      return this.visitXaPrepareStatement(ctx.xaPrepareStatement());
    } else if (ctx.xaCommitWork() != null) {
      return this.visitXaCommitWork(ctx.xaCommitWork());
    } else if (ctx.xaRollbackWork() != null) {
      return this.visitXaRollbackWork(ctx.xaRollbackWork());
    } else if (ctx.xaRecoverWork() != null) {
      return this.visitXaRecoverWork(ctx.xaRecoverWork());
    } else {
      throw ParserError.make(ctx);
    }
  }

  @Override
  public PreparedStatement visitPreparedStatement(MySqlParser.PreparedStatementContext ctx) {
    if (ctx == null) {
      return null;
    }
    if (ctx.prepareStatement() != null) {
      return this.visitPrepareStatement(ctx.prepareStatement());
    } else if (ctx.executeStatement() != null) {
      return this.visitExecuteStatement(ctx.executeStatement());
    } else if (ctx.deallocatePrepare() != null) {
      return this.visitDeallocatePrepare(ctx.deallocatePrepare());
    } else {
      throw ParserError.make(ctx);
    }
  }

  @Override
  public CompoundStatement visitCompoundStatement(MySqlParser.CompoundStatementContext ctx) {
    if (ctx == null) {
      return null;
    }

    if (ctx.blockStatement() != null) {
      return this.visitBlockStatement(ctx.blockStatement());
    } else if (ctx.caseStatement() != null) {
      return this.visitCaseStatement(ctx.caseStatement());
    } else if (ctx.ifStatement() != null) {
      return this.visitIfStatement(ctx.ifStatement());
    } else if (ctx.leaveStatement() != null) {
      return this.visitLeaveStatement(ctx.leaveStatement());
    } else if (ctx.loopStatement() != null) {
      return this.visitLoopStatement(ctx.loopStatement());
    } else if (ctx.repeatStatement() != null) {
      return this.visitRepeatStatement(ctx.repeatStatement());
    } else if (ctx.whileStatement() != null) {
      return this.visitWhileStatement(ctx.whileStatement());
    } else if (ctx.iterateStatement() != null) {
      return this.visitIterateStatement(ctx.iterateStatement());
    } else if (ctx.returnStatement() != null) {
      return this.visitReturnStatement(ctx.returnStatement());
    } else if (ctx.cursorStatement() != null) {
      return this.visitCursorStatement(ctx.cursorStatement());
    } else {
      throw ParserError.make(ctx);
    }
  }

  @Override
  public AdministrationStatement
      visitAdministrationStatement(MySqlParser.AdministrationStatementContext ctx) {
    if (ctx == null) {
      return null;
    }

    if (ctx.alterUser() != null) {
      return this.visitAlterUser(ctx.alterUser());
    } else if (ctx.createUser() != null) {
      return this.visitCreateUser(ctx.createUser());
    } else if (ctx.dropUser() != null) {
      return this.visitDropUser(ctx.dropUser());
    } else if (ctx.grantStatement() != null) {
      return this.visitGrantStatement(ctx.grantStatement());
    } else if (ctx.grantProxy() != null) {
      return this.visitGrantProxy(ctx.grantProxy());
    } else if (ctx.renameUser() != null) {
      return this.visitRenameUser(ctx.renameUser());
    } else if (ctx.revokeStatement() != null) {
      return this.visitRevokeStatement(ctx.revokeStatement());
    } else if (ctx.revokeProxy() != null) {
      return this.visitRevokeProxy(ctx.revokeProxy());
    } else if (ctx.analyzeTable() != null) {
      return this.visitAnalyzeTable(ctx.analyzeTable());
    } else if (ctx.checkTable() != null) {
      return this.visitCheckTable(ctx.checkTable());
    } else if (ctx.checksumTable() != null) {
      return this.visitChecksumTable(ctx.checksumTable());
    } else if (ctx.optimizeTable() != null) {
      return this.visitOptimizeTable(ctx.optimizeTable());
    } else if (ctx.repairTable() != null) {
      return this.visitRepairTable(ctx.repairTable());
    } else if (ctx.createUdfunction() != null) {
      return this.visitCreateUdfunction(ctx.createUdfunction());
    } else if (ctx.installPlugin() != null) {
      return this.visitInstallPlugin(ctx.installPlugin());
    } else if (ctx.uninstallPlugin() != null) {
      return this.visitUninstallPlugin(ctx.uninstallPlugin());
    } else if (ctx.setStatement() != null) {
      return this.visitSetStatement(ctx.setStatement());
    } else if (ctx.showStatement() != null) {
      return this.visitShowStatement(ctx.showStatement());
    } else if (ctx.binlogStatement() != null) {
      return this.visitBinlogStatement(ctx.binlogStatement());
    } else if (ctx.cacheIndexStatement() != null) {
      return this.visitCacheIndexStatement(ctx.cacheIndexStatement());
    } else if (ctx.flushStatement() != null) {
      return this.visitFlushStatement(ctx.flushStatement());
    } else if (ctx.killStatement() != null) {
      return this.visitKillStatement(ctx.killStatement());
    } else if (ctx.loadIndexIntoCache() != null) {
      return this.visitLoadIndexIntoCache(ctx.loadIndexIntoCache());
    } else if (ctx.resetStatement() != null) {
      return this.visitResetStatement(ctx.resetStatement());
    } else if (ctx.shutdownStatement() != null) {
      return this.visitShutdownStatement(ctx.shutdownStatement());
    } else {
      throw ParserError.make(ctx);
    }

  }

  @Override
  public UtilityStatement visitUtilityStatement(MySqlParser.UtilityStatementContext ctx) {
    if (ctx == null) {
      return null;
    }

    if (ctx.simpleDescribeStatement() != null) {
      return this.visitSimpleDescribeStatement(ctx.simpleDescribeStatement());
    } else if (ctx.fullDescribeStatement() != null) {
      return this.visitFullDescribeStatement(ctx.fullDescribeStatement());
    } else if (ctx.helpStatement() != null) {
      return this.visitHelpStatement(ctx.helpStatement());
    } else if (ctx.useStatement() != null) {
      return this.visitUseStatement(ctx.useStatement());
    } else {
      throw ParserError.make(ctx);
    }
  }

  @Override
  public CreateDatabase visitCreateDatabase(MySqlParser.CreateDatabaseContext ctx) {
    if (ctx == null) {
      return null;
    }

    DbFormatEnum dbFormat = null;
    if (ctx.DATABASE() != null) {
      dbFormat = DbFormatEnum.DATABASE;
    } else {
      dbFormat = DbFormatEnum.SCHEMA;
    }
    IfNotExists ifNotExists = this.visitIfNotExists(ctx.ifNotExists());
    Uid uid = this.visitUid(ctx.uid());
    List<MySqlParser.CreateDatabaseOptionContext> createDatabaseOptionCtxs =
        ctx.createDatabaseOption();
    List<CreateDatabaseOption> createDatabaseOptions = Lists.newArrayList();
    if (CollectionUtils.isNotEmpty(createDatabaseOptionCtxs)) {
      for (MySqlParser.CreateDatabaseOptionContext createDatabaseOptionCtx : createDatabaseOptionCtxs) {
        createDatabaseOptions.add(this.visitCreateDatabaseOption(createDatabaseOptionCtx));
      }
    }

    return RelationalAlgebraExpressionFactory.makeCreateDatabase(dbFormat, ifNotExists, uid,
      createDatabaseOptions);
  }

  @Override
  public CreateEvent visitCreateEvent(MySqlParser.CreateEventContext ctx) {
    if (ctx == null) {
      return null;
    }
    OwnerStatement ownerStatement = this.visitOwnerStatement(ctx.ownerStatement());
    IfNotExists ifNotExists = this.visitIfNotExists(ctx.ifNotExists());
    FullId fullId = this.visitFullId(ctx.fullId());
    ScheduleExpression scheduleExpression = this.visitScheduleExpression(ctx.scheduleExpression());
    Boolean onCompletion = null;
    if (ctx.COMPLETION() != null) {
      onCompletion = Boolean.TRUE;
    }
    Boolean notPreserve = null;
    if (ctx.NOT() != null) {
      notPreserve = Boolean.TRUE;
    } else if (ctx.PRESERVE() != null) {
      notPreserve = Boolean.FALSE;
    }

    EnableTypeEnum enableType = null;
    if (ctx.enableType() != null) {
      enableType = this.visitEnableType(ctx.enableType());
    }
    String comment = null;
    if (ctx.STRING_LITERAL() != null) {
      comment = ctx.STRING_LITERAL().getText();
    }
    RoutineBody routineBody = this.visitRoutineBody(ctx.routineBody());

    return RelationalAlgebraExpressionFactory.makeCreateEvent(ownerStatement, ifNotExists, fullId,
      scheduleExpression, onCompletion, notPreserve, enableType, comment, routineBody);
  }

  @Override
  public CreateIndex visitCreateIndex(MySqlParser.CreateIndexContext ctx) {
    if (ctx == null) {
      return null;
    }

    DdlStatement.IntimeActionEnum intimeAction = null;
    if (ctx.intimeAction != null) {
      intimeAction =
          DdlStatement.IntimeActionEnum.valueOf(ctx.intimeAction.getText().toUpperCase());
    }
    DdlStatement.IndexCategoryEnum indexCategory = null;
    if (ctx.indexCategory != null) {
      indexCategory =
          DdlStatement.IndexCategoryEnum.valueOf(ctx.indexCategory.getText().toUpperCase());
    }
    Uid uid = this.visitUid(ctx.uid());
    DdlStatement.IndexTypeEnum indexType = this.visitIndexType(ctx.indexType());
    TableName tableName = this.visitTableName(ctx.tableName());
    IndexColumnNames indexColumnNames = this.visitIndexColumnNames(ctx.indexColumnNames());
    List<IndexOption> indexOptions = Lists.newArrayList();
    if (CollectionUtils.isNotEmpty(ctx.indexOption())) {
      for (MySqlParser.IndexOptionContext indexOptionCtx : ctx.indexOption()) {
        indexOptions.add(this.visitIndexOption(indexOptionCtx));
      }
    }
    List<IndexAlgorithmOrLock> algorithmOrLocks = Lists.newArrayList();
    if (CollectionUtils.isNotEmpty(ctx.DEFAULT())) {
      algorithmOrLocks.add(RelationalAlgebraExpressionFactory
          .makeIndexAlgorithmOrLock(DdlStatement.IndexAlgTypeEnum.DEFAULT, null));
    }
    if (CollectionUtils.isNotEmpty(ctx.INPLACE())) {
      algorithmOrLocks.add(RelationalAlgebraExpressionFactory
          .makeIndexAlgorithmOrLock(DdlStatement.IndexAlgTypeEnum.INPLACE, null));
    }
    if (CollectionUtils.isNotEmpty(ctx.COPY())) {
      algorithmOrLocks.add(RelationalAlgebraExpressionFactory
          .makeIndexAlgorithmOrLock(DdlStatement.IndexAlgTypeEnum.COPY, null));
    }

    if (CollectionUtils.isNotEmpty(ctx.DEFAULT())) {
      algorithmOrLocks.add(RelationalAlgebraExpressionFactory.makeIndexAlgorithmOrLock(null,
        DdlStatement.LockTypeEnum.DEFAULT));
    }
    if (CollectionUtils.isNotEmpty(ctx.NONE())) {
      algorithmOrLocks.add(RelationalAlgebraExpressionFactory.makeIndexAlgorithmOrLock(null,
        DdlStatement.LockTypeEnum.NONE));
    }
    if (CollectionUtils.isNotEmpty(ctx.SHARED())) {
      algorithmOrLocks.add(RelationalAlgebraExpressionFactory.makeIndexAlgorithmOrLock(null,
        DdlStatement.LockTypeEnum.SHARED));
    }
    if (CollectionUtils.isNotEmpty(ctx.EXCLUSIVE())) {
      algorithmOrLocks.add(RelationalAlgebraExpressionFactory.makeIndexAlgorithmOrLock(null,
        DdlStatement.LockTypeEnum.EXCLUSIVE));
    }

    return RelationalAlgebraExpressionFactory.makeCreateIndex(intimeAction, indexCategory, uid,
      indexType, tableName, indexColumnNames, indexOptions, algorithmOrLocks);
  }

  @Override
  public CreateLogfileGroup visitCreateLogfileGroup(MySqlParser.CreateLogfileGroupContext ctx) {
    if (ctx == null) {
      return null;
    }

    Uid logFileGroupUid = this.visitUid(ctx.uid(0));
    String undoFile = ctx.undoFile.getText();
    FileSizeLiteral initSize = this.visitFileSizeLiteral(ctx.initSize);
    FileSizeLiteral undoSize = this.visitFileSizeLiteral(ctx.undoSize);
    FileSizeLiteral redoSize = this.visitFileSizeLiteral(ctx.redoSize);
    Uid nodeGroupUid = null;
    if (ctx.uid().size() > 1) {
      nodeGroupUid = this.visitUid(ctx.uid(1));
    }
    Boolean wait = null;
    if (ctx.WAIT() != null) {
      wait = Boolean.TRUE;
    }
    String comment = null;
    if (ctx.comment != null) {
      comment = ctx.comment.getText();
    }
    EngineName engineName = this.visitEngineName(ctx.engineName());

    return RelationalAlgebraExpressionFactory.makeCreateLogfileGroup(logFileGroupUid, undoFile,
      initSize, undoSize, redoSize, nodeGroupUid, wait, comment, engineName);
  }

  @Override
  public CreateProcedure visitCreateProcedure(MySqlParser.CreateProcedureContext ctx) {
    if (ctx == null) {
      return null;
    }

    OwnerStatement ownerStatement = this.visitOwnerStatement(ctx.ownerStatement());
    FullId fullId = this.visitFullId(ctx.fullId());
    List<ProcedureParameter> procedureParameters = Lists.newArrayList();
    if (CollectionUtils.isNotEmpty(ctx.procedureParameter())) {
      for (MySqlParser.ProcedureParameterContext procedureParameterCtx : ctx.procedureParameter()) {
        procedureParameters.add(this.visitProcedureParameter(procedureParameterCtx));
      }
    }
    List<RoutineOption> routineOptions = Lists.newArrayList();
    if (CollectionUtils.isNotEmpty(ctx.routineOption())) {
      for (MySqlParser.RoutineOptionContext routineOptionCtx : ctx.routineOption()) {
        routineOptions.add(this.visitRoutineOption(routineOptionCtx));
      }
    }
    RoutineBody routineBody = this.visitRoutineBody(ctx.routineBody());
    return RelationalAlgebraExpressionFactory.makeCreateProcedure(ownerStatement, fullId,
      procedureParameters, routineOptions, routineBody);
  }

  @Override
  public CreateFunction visitCreateFunction(MySqlParser.CreateFunctionContext ctx) {
    if (ctx == null) {
      return null;
    }

    OwnerStatement ownerStatement = this.visitOwnerStatement(ctx.ownerStatement());
    FullId fullId = this.visitFullId(ctx.fullId());
    List<FunctionParameter> functionParameters = Lists.newArrayList();
    if (CollectionUtils.isNotEmpty(ctx.functionParameter())) {
      for (MySqlParser.FunctionParameterContext functionParameterCtx : ctx.functionParameter()) {
        functionParameters.add(this.visitFunctionParameter(functionParameterCtx));
      }
    }
    DataType dataType = this.visitDataType(ctx.dataType());
    List<RoutineOption> routineOptions = Lists.newArrayList();
    if (CollectionUtils.isNotEmpty(ctx.routineOption())) {
      for (MySqlParser.RoutineOptionContext routineOptionCtx : ctx.routineOption()) {
        routineOptions.add(this.visitRoutineOption(routineOptionCtx));
      }
    }
    RoutineBody routineBody = this.visitRoutineBody(ctx.routineBody());

    return RelationalAlgebraExpressionFactory.makeCreateFunction(ownerStatement, fullId,
      functionParameters, dataType, routineOptions, routineBody);
  }

  @Override
  public CreateServer visitCreateServer(MySqlParser.CreateServerContext ctx) {
    if (ctx == null) {
      return null;
    }

    Uid uid = this.visitUid(ctx.uid());
    String wrapperName = ctx.wrapperName.getText();
    List<ServerOption> serverOptions = Lists.newArrayList();
    for (MySqlParser.ServerOptionContext serverOptionCtx : ctx.serverOption()) {
      serverOptions.add(this.visitServerOption(serverOptionCtx));
    }

    return RelationalAlgebraExpressionFactory.makeCreateServer(uid, wrapperName, serverOptions);
  }

  public CreateTable visitCreateTable(MySqlParser.CreateTableContext ctx) {
    if (ctx == null) {
      return null;
    }

    if (ctx instanceof MySqlParser.CopyCreateTableContext) {
      return this.visitCopyCreateTable((MySqlParser.CopyCreateTableContext) ctx);
    } else if (ctx instanceof MySqlParser.QueryCreateTableContext) {
      return this.visitQueryCreateTable((MySqlParser.QueryCreateTableContext) ctx);
    } else if (ctx instanceof MySqlParser.ColumnCreateTableContext) {
      return this.visitColumnCreateTable((MySqlParser.ColumnCreateTableContext) ctx);
    } else {
      throw ParserError.make(ctx);
    }

  }

  @Override
  public CopyCreateTable visitCopyCreateTable(MySqlParser.CopyCreateTableContext ctx) {
    if (ctx == null) {
      return null;
    }

    Boolean temporary = null;
    if (ctx.TEMPORARY() != null) {
      temporary = Boolean.TRUE;
    }
    IfNotExists ifNotExists = this.visitIfNotExists(ctx.ifNotExists());
    TableName tableName = this.visitTableName(ctx.tableName(0));
    TableName likeTableName = this.visitTableName(ctx.tableName(1));

    return RelationalAlgebraExpressionFactory.makeCopyCreateTable(temporary, ifNotExists, tableName,
      likeTableName);
  }

  @Override
  public QueryCreateTable visitQueryCreateTable(MySqlParser.QueryCreateTableContext ctx) {
    if (ctx == null) {
      return null;
    }

    Boolean temporary = null;
    if (ctx.TEMPORARY() != null) {
      temporary = Boolean.TRUE;
    }
    IfNotExists ifNotExists = this.visitIfNotExists(ctx.ifNotExists());
    TableName tableName = this.visitTableName(ctx.tableName());
    CreateDefinitions createDefinitions = this.visitCreateDefinitions(ctx.createDefinitions());
    List<TableOption> tableOptions = Lists.newArrayList();
    if (CollectionUtils.isNotEmpty(ctx.tableOption())) {
      for (MySqlParser.TableOptionContext tableOptionCtx : ctx.tableOption()) {
        tableOptions.add(this.visitTableOption(tableOptionCtx));
      }
    }
    PartitionDefinitions partitionDefinitions =
        this.visitPartitionDefinitions(ctx.partitionDefinitions());
    QueryCreateTable.KeyViolateEnum keyViolate = null;
    if (ctx.keyViolate != null) {
      keyViolate = QueryCreateTable.KeyViolateEnum.valueOf(ctx.keyViolate.getText().toUpperCase());
    }
    SelectStatement selectStatement = this.visitSelectStatement(ctx.selectStatement());

    return RelationalAlgebraExpressionFactory.makeQueryCreateTable(temporary, ifNotExists,
      tableName, createDefinitions, tableOptions, partitionDefinitions, keyViolate,
      selectStatement);
  }

  @Override
  public ColumnCreateTable visitColumnCreateTable(MySqlParser.ColumnCreateTableContext ctx) {
    if (ctx == null) {
      return null;
    }

    Boolean temporary = null;
    if (ctx.TEMPORARY() != null) {
      temporary = Boolean.TRUE;
    }
    IfNotExists ifNotExists = this.visitIfNotExists(ctx.ifNotExists());
    TableName tableName = this.visitTableName(ctx.tableName());
    CreateDefinitions createDefinitions = this.visitCreateDefinitions(ctx.createDefinitions());
    List<TableOption> tableOptions = Lists.newArrayList();
    if (CollectionUtils.isNotEmpty(ctx.tableOption())) {
      for (MySqlParser.TableOptionContext tableOptionCtx : ctx.tableOption()) {
        tableOptions.add(this.visitTableOption(tableOptionCtx));
      }
    }
    PartitionDefinitions partitionDefinitions =
        this.visitPartitionDefinitions(ctx.partitionDefinitions());

    return RelationalAlgebraExpressionFactory.makeColumnCreateTable(temporary, ifNotExists,
      tableName, createDefinitions, tableOptions, partitionDefinitions);
  }

  @Override
  public CreateTablespaceInnodb
      visitCreateTablespaceInnodb(MySqlParser.CreateTablespaceInnodbContext ctx) {
    if (ctx == null) {
      return null;
    }

    Uid uid = this.visitUid(ctx.uid());
    String datafile = ctx.datafile.getText();
    FileSizeLiteral fileBlockSize = this.visitFileSizeLiteral(ctx.fileBlockSize);
    EngineName engineName = this.visitEngineName(ctx.engineName());

    return RelationalAlgebraExpressionFactory.makeCreateTablespaceInnodb(uid, datafile,
      fileBlockSize, engineName);
  }

  @Override
  public CreateTablespaceNdb visitCreateTablespaceNdb(MySqlParser.CreateTablespaceNdbContext ctx) {
    if (ctx == null) {
      return null;
    }

    Uid uid = this.visitUid(ctx.uid(0));
    String datafile = ctx.datafile.getText();
    Uid logFileGroupUid = this.visitUid(ctx.uid(1));
    FileSizeLiteral extentSize = this.visitFileSizeLiteral(ctx.extentSize);
    FileSizeLiteral initialSize = this.visitFileSizeLiteral(ctx.initialSize);
    FileSizeLiteral autoextendSize = this.visitFileSizeLiteral(ctx.autoextendSize);
    FileSizeLiteral maxSize = this.visitFileSizeLiteral(ctx.maxSize);
    Uid nodeGroupUid = null;
    if (ctx.uid().size() > 2) {
      nodeGroupUid = this.visitUid(ctx.uid(2));
    }
    Boolean wait = null;
    if (ctx.WAIT() != null) {
      wait = Boolean.TRUE;
    }
    String comment = null;
    if (ctx.comment != null) {
      comment = ctx.comment.getText();
    }
    EngineName engineName = this.visitEngineName(ctx.engineName());

    return RelationalAlgebraExpressionFactory.makeCreateTablespaceNdb(uid, datafile,
      logFileGroupUid, extentSize, initialSize, autoextendSize, maxSize, nodeGroupUid, wait,
      comment, engineName);
  }

  @Override
  public CreateTrigger visitCreateTrigger(MySqlParser.CreateTriggerContext ctx) {
    if (ctx == null) {
      return null;
    }

    OwnerStatement ownerStatement = this.visitOwnerStatement(ctx.ownerStatement());
    FullId thisTrigger = this.visitFullId(ctx.thisTrigger);
    CreateTrigger.TriggerTimeEnum triggerTime =
        CreateTrigger.TriggerTimeEnum.valueOf(ctx.triggerTime.getText().toUpperCase());
    CreateTrigger.TriggerEventEnum triggerEvent =
        CreateTrigger.TriggerEventEnum.valueOf(ctx.triggerEvent.getText().toUpperCase());
    TableName tableName = this.visitTableName(ctx.tableName());
    CreateTrigger.TriggerPlaceEnum triggerPlace = null;
    if (ctx.triggerPlace != null) {
      triggerPlace =
          CreateTrigger.TriggerPlaceEnum.valueOf(ctx.triggerPlace.getText().toUpperCase());
    }
    FullId otherTrigger = this.visitFullId(ctx.otherTrigger);
    RoutineBody routineBody = this.visitRoutineBody(ctx.routineBody());

    return RelationalAlgebraExpressionFactory.makeCreateTrigger(ownerStatement, thisTrigger,
      triggerTime, triggerEvent, tableName, triggerPlace, otherTrigger, routineBody);
  }

  @Override
  public CreateView visitCreateView(MySqlParser.CreateViewContext ctx) {
    if (ctx == null) {
      return null;
    }

    Boolean replace = null;
    if (ctx.REPLACE() != null) {
      replace = Boolean.TRUE;
    }
    CreateView.AlgTypeEnum algType = null;
    if (ctx.algType != null) {
      algType = CreateView.AlgTypeEnum.valueOf(ctx.algType.getText().toUpperCase());
    }
    OwnerStatement ownerStatement = this.visitOwnerStatement(ctx.ownerStatement());
    Boolean sqlSecurity = null;
    if (ctx.SECURITY() != null) {
      sqlSecurity = Boolean.TRUE;
    }
    CreateView.SecContextEnum secContext = null;
    if (ctx.secContext != null) {
      secContext = CreateView.SecContextEnum.valueOf(ctx.secContext.getText().toUpperCase());
    }
    FullId fullId = this.visitFullId(ctx.fullId());
    UidList uidList = this.visitUidList(ctx.uidList());
    SelectStatement selectStatement = this.visitSelectStatement(ctx.selectStatement());
    Boolean withCheckOption = null;
    if (ctx.WITH() != null) {
      withCheckOption = Boolean.TRUE;
    }
    CreateView.CheckOptionEnum checkOption = null;
    if (ctx.checkOption != null) {
      checkOption = CreateView.CheckOptionEnum.valueOf(ctx.checkOption.getText().toUpperCase());
    }

    return RelationalAlgebraExpressionFactory.makeCreateView(replace, algType, ownerStatement,
      sqlSecurity, secContext, fullId, uidList, selectStatement, withCheckOption, checkOption);
  }

  @Override
  public CreateDatabaseOption
      visitCreateDatabaseOption(MySqlParser.CreateDatabaseOptionContext ctx) {
    if (ctx == null) {
      return null;
    }
    Boolean isDefault = null;
    if (ctx.DEFAULT() != null) {
      isDefault = Boolean.TRUE;
    }
    CharsetName charsetName = null;
    if (ctx.charsetName() != null) {
      charsetName = this.visitCharsetName(ctx.charsetName());
    }
    CollationName collationName = null;
    if (ctx.collationName() != null) {
      collationName = this.visitCollationName(ctx.collationName());
    }

    return RelationalAlgebraExpressionFactory.makeCreateDatabaseOption(isDefault, charsetName,
      collationName);
  }

  @Override
  public OwnerStatement visitOwnerStatement(MySqlParser.OwnerStatementContext ctx) {
    if (ctx == null) {
      return null;
    }

    UserName userName = this.visitUserName(ctx.userName());
    Boolean currentUser = null;
    if (ctx.CURRENT_USER() != null) {
      currentUser = Boolean.TRUE;
    }

    return RelationalAlgebraExpressionFactory.makeOwnerStatement(userName, currentUser);
  }

  public ScheduleExpression visitScheduleExpression(MySqlParser.ScheduleExpressionContext ctx) {
    if (ctx == null) {
      return null;
    }
    if (ctx instanceof MySqlParser.PreciseScheduleContext) {
      return this.visitPreciseSchedule((MySqlParser.PreciseScheduleContext) ctx);
    } else if (ctx instanceof MySqlParser.IntervalScheduleContext) {
      return this.visitIntervalSchedule((MySqlParser.IntervalScheduleContext) ctx);
    } else {
      throw ParserError.make(ctx);
    }
  }

  @Override
  public PreciseSchedule visitPreciseSchedule(MySqlParser.PreciseScheduleContext ctx) {
    if (ctx == null) {
      return null;
    }

    TimestampValue timestampValue = this.visitTimestampValue(ctx.timestampValue());
    List<IntervalExpr> intervalExprs = Lists.newArrayList();
    List<MySqlParser.IntervalExprContext> intervalExprCtxs = ctx.intervalExpr();
    if (CollectionUtils.isNotEmpty(intervalExprCtxs)) {
      for (MySqlParser.IntervalExprContext intervalExprContext : intervalExprCtxs) {
        intervalExprs.add(this.visitIntervalExpr(intervalExprContext));
      }
    }

    return RelationalAlgebraExpressionFactory.makePreciseSchedule(timestampValue, intervalExprs);
  }

  @Override
  public IntervalSchedule visitIntervalSchedule(MySqlParser.IntervalScheduleContext ctx) {
    if (ctx == null) {
      return null;
    }

    DecimalLiteral decimalLiteral = this.visitDecimalLiteral(ctx.decimalLiteral());
    Expression expression = this.visitExpression(ctx.expression());
    IntervalType intervalType = this.visitIntervalType(ctx.intervalType());
    TimestampValue start = this.visitTimestampValue(ctx.start);
    List<IntervalExpr> startIntervals = Lists.newArrayList();
    if (CollectionUtils.isNotEmpty(ctx.startIntervals)) {
      for (MySqlParser.IntervalExprContext intervalExprContext : ctx.startIntervals) {
        startIntervals.add(this.visitIntervalExpr(intervalExprContext));
      }
    }
    TimestampValue end = this.visitTimestampValue(ctx.end);
    List<IntervalExpr> endIntervals = Lists.newArrayList();
    if (CollectionUtils.isNotEmpty(ctx.endIntervals)) {
      for (MySqlParser.IntervalExprContext intervalExprContext : ctx.endIntervals) {
        endIntervals.add(this.visitIntervalExpr(intervalExprContext));
      }
    }

    return RelationalAlgebraExpressionFactory.makeIntervalSchedule(decimalLiteral, expression,
      intervalType, start, startIntervals, end, endIntervals);

  }

  @Override
  public TimestampValue visitTimestampValue(MySqlParser.TimestampValueContext ctx) {
    if (ctx == null) {
      return null;
    }

    TimestampValue.Type type = null;
    StringLiteral stringLiteral = null;
    DecimalLiteral decimalLiteral = null;
    Expression expression = null;
    if (ctx.CURRENT_TIMESTAMP() != null) {
      type = TimestampValue.Type.CURRENT_TIMESTAMP;
    } else if (ctx.stringLiteral() != null) {
      type = TimestampValue.Type.STRING_LITERAL;
      stringLiteral = this.visitStringLiteral(ctx.stringLiteral());
    } else if (ctx.decimalLiteral() != null) {
      type = TimestampValue.Type.DECIMAL_LITERAL;
      decimalLiteral = this.visitDecimalLiteral(ctx.decimalLiteral());
    } else if (ctx.expression() != null) {
      type = TimestampValue.Type.EXPRESSION;
      expression = this.visitExpression(ctx.expression());
    } else {
      throw ParserError.make(ctx);
    }

    return RelationalAlgebraExpressionFactory.makeTimestampValue(type, stringLiteral,
      decimalLiteral, expression);

  }

  @Override
  public IntervalExpr visitIntervalExpr(MySqlParser.IntervalExprContext ctx) {
    if (ctx == null) {
      return null;
    }
    DecimalLiteral decimalLiteral = this.visitDecimalLiteral(ctx.decimalLiteral());
    Expression expression = this.visitExpression(ctx.expression());
    IntervalType intervalType = this.visitIntervalType(ctx.intervalType());

    return RelationalAlgebraExpressionFactory.makeIntervalExpr(decimalLiteral, expression,
      intervalType);
  }

  @Override
  public IntervalType visitIntervalType(MySqlParser.IntervalTypeContext ctx) {
    if (ctx == null) {
      return null;
    }

    IntervalType.Type type = null;
    SimpleIdSets.IntervalTypeBaseEnum intervalTypeBase = null;
    if (ctx.intervalTypeBase() != null) {
      type = IntervalType.Type.INTERVAL_TYPE_BASE;
      intervalTypeBase = this.visitIntervalTypeBase(ctx.intervalTypeBase());
    } else {
      type = IntervalType.Type.valueOf(ctx.getText().toUpperCase());
    }

    return RelationalAlgebraExpressionFactory.makeIntervalType(type, intervalTypeBase);
  }

  @Override
  public EnableTypeEnum visitEnableType(MySqlParser.EnableTypeContext ctx) {
    if (ctx == null) {
      return null;
    }

    if (ctx.ENABLE() != null) {
      return EnableTypeEnum.ENABLE;
    } else if (ctx.DISABLE() != null && ctx.SLAVE() == null) {
      return EnableTypeEnum.DISABLE;
    } else if (ctx.DISABLE() != null && ctx.SLAVE() != null) {
      return EnableTypeEnum.DISABLE_ON_SLAVE;
    } else {
      throw ParserError.make(ctx);
    }
  }

  @Override
  public DdlStatement.IndexTypeEnum visitIndexType(MySqlParser.IndexTypeContext ctx) {
    if (ctx == null) {
      return null;
    }
    if (ctx.BTREE() != null) {
      return IndexTypeEnum.BTREE;
    } else if (ctx.HASH() != null) {
      return IndexTypeEnum.HASH;
    } else {
      throw ParserError.make(ctx);
    }
  }

  @Override
  public IndexOption visitIndexOption(MySqlParser.IndexOptionContext ctx) {
    if (ctx == null) {
      return null;
    }

    IndexOption.Type type = null;
    if (ctx.KEY_BLOCK_SIZE() != null) {
      type = IndexOption.Type.KEY_BLOCK_SIZE;
    } else if (ctx.indexType() != null) {
      type = IndexOption.Type.INDEX_TYPE;
    } else if (ctx.WITH() != null) {
      type = IndexOption.Type.WITH_PARSER;
    } else if (ctx.COMMENT() != null) {
      type = IndexOption.Type.COMMENT;
    } else {
      throw ParserError.make(ctx);
    }

    FileSizeLiteral fileSizeLiteral = this.visitFileSizeLiteral(ctx.fileSizeLiteral());
    IndexTypeEnum indexType = this.visitIndexType(ctx.indexType());
    Uid uid = this.visitUid(ctx.uid());
    String stringLiteral = null;
    if (ctx.STRING_LITERAL() != null) {
      stringLiteral = ctx.STRING_LITERAL().getText();
    }
    return RelationalAlgebraExpressionFactory.makeIndexOption(type, fileSizeLiteral, indexType, uid,
      stringLiteral);
  }

  @Override
  public ProcedureParameter visitProcedureParameter(MySqlParser.ProcedureParameterContext ctx) {
    if (ctx == null) {
      return null;
    }

    ProcedureParameter.DirectionEnum direction = null;
    if (ctx.direction != null) {
      direction = ProcedureParameter.DirectionEnum.valueOf(ctx.direction.getText().toUpperCase());
    }
    Uid uid = this.visitUid(ctx.uid());
    DataType dataType = this.visitDataType(ctx.dataType());

    return RelationalAlgebraExpressionFactory.makeProcedureParameter(direction, uid, dataType);
  }

  @Override
  public FunctionParameter visitFunctionParameter(MySqlParser.FunctionParameterContext ctx) {
    if (ctx == null) {
      return null;
    }

    Uid uid = this.visitUid(ctx.uid());
    DataType dataType = this.visitDataType(ctx.dataType());
    return RelationalAlgebraExpressionFactory.makeFunctionParameter(uid, dataType);
  }

  public RoutineOption visitRoutineOption(MySqlParser.RoutineOptionContext ctx) {
    if (ctx == null) {
      return null;
    }

    if (ctx instanceof MySqlParser.RoutineCommentContext) {
      return this.visitRoutineComment((MySqlParser.RoutineCommentContext) ctx);
    } else if (ctx instanceof MySqlParser.RoutineLanguageContext) {
      return this.visitRoutineLanguage((MySqlParser.RoutineLanguageContext) ctx);
    } else if (ctx instanceof MySqlParser.RoutineBehaviorContext) {
      return this.visitRoutineBehavior((MySqlParser.RoutineBehaviorContext) ctx);
    } else if (ctx instanceof MySqlParser.RoutineDataContext) {
      return this.visitRoutineData((MySqlParser.RoutineDataContext) ctx);
    } else if (ctx instanceof MySqlParser.RoutineSecurityContext) {
      return this.visitRoutineSecurity((MySqlParser.RoutineSecurityContext) ctx);
    } else {
      throw ParserError.make(ctx);
    }
  }

  @Override
  public RoutineComment visitRoutineComment(MySqlParser.RoutineCommentContext ctx) {
    if (ctx == null) {
      return null;
    }
    String stringLiteral = ctx.STRING_LITERAL().getText();
    return RelationalAlgebraExpressionFactory.makeRoutineComment(stringLiteral);
  }

  @Override
  public RoutineLanguage visitRoutineLanguage(MySqlParser.RoutineLanguageContext ctx) {
    if (ctx == null) {
      return null;
    }
    return RelationalAlgebraExpressionFactory.makeRoutineLanguage();
  }

  @Override
  public RoutineBehavior visitRoutineBehavior(MySqlParser.RoutineBehaviorContext ctx) {
    if (ctx == null) {
      return null;
    }
    Boolean not = null;
    if (ctx.NOT() != null) {
      not = Boolean.TRUE;
    }
    return RelationalAlgebraExpressionFactory.makeRoutineBehavior(not);
  }

  @Override
  public RoutineData visitRoutineData(MySqlParser.RoutineDataContext ctx) {
    if (ctx == null) {
      return null;
    }

    RoutineData.Type type = null;
    if (ctx.CONTAINS() != null) {
      type = RoutineData.Type.CONTAINS_SQL;
    } else if (ctx.NO() != null) {
      type = RoutineData.Type.NO_SQL;
    } else if (ctx.READS() != null) {
      type = RoutineData.Type.READS_SQL_DATA;
    } else if (ctx.MODIFIES() != null) {
      type = RoutineData.Type.MODIFIES_SQL_DATA;
    } else {
      throw ParserError.make(ctx);
    }

    return RelationalAlgebraExpressionFactory.makeRoutineData(type);
  }

  @Override
  public RoutineSecurity visitRoutineSecurity(MySqlParser.RoutineSecurityContext ctx) {
    if (ctx == null) {
      return null;
    }

    RoutineSecurity.ContextType type = null;
    if (ctx.DEFINER() != null) {
      type = RoutineSecurity.ContextType.DEFINER;
    } else if (ctx.INVOKER() != null) {
      type = RoutineSecurity.ContextType.INVOKER;
    } else {
      throw ParserError.make(ctx);
    }

    return RelationalAlgebraExpressionFactory.makeRoutineSecurity(type);
  }

  @Override
  public ServerOption visitServerOption(MySqlParser.ServerOptionContext ctx) {
    if (ctx == null) {
      return null;
    }
    ServerOption.Type type = ServerOption.Type.valueOf(ctx.getChild(0).getText().toUpperCase());
    String stringLiteral = null;
    if (ctx.STRING_LITERAL() != null) {
      stringLiteral = ctx.STRING_LITERAL().getText();
    }
    DecimalLiteral decimalLiteral = this.visitDecimalLiteral(ctx.decimalLiteral());

    return RelationalAlgebraExpressionFactory.makeServerOption(type, stringLiteral, decimalLiteral);
  }

  @Override
  public CreateDefinitions visitCreateDefinitions(MySqlParser.CreateDefinitionsContext ctx) {
    if (ctx == null) {
      return null;
    }

    List<CreateDefinition> createDefinitions = Lists.newArrayList();
    for (MySqlParser.CreateDefinitionContext createDefinitionCtx : ctx.createDefinition()) {
      createDefinitions.add(this.visitCreateDefinition(createDefinitionCtx));
    }

    return RelationalAlgebraExpressionFactory.makeCreateDefinitions(createDefinitions);
  }

  public CreateDefinition visitCreateDefinition(MySqlParser.CreateDefinitionContext ctx) {
    if (ctx == null) {
      return null;
    }

    if (ctx instanceof MySqlParser.ColumnDeclarationContext) {
      return this.visitColumnDeclaration((MySqlParser.ColumnDeclarationContext) ctx);
    } else if (ctx instanceof MySqlParser.ConstraintDeclarationContext) {
      return this.visitConstraintDeclaration((MySqlParser.ConstraintDeclarationContext) ctx);
    } else if (ctx instanceof MySqlParser.IndexDeclarationContext) {
      return this.visitIndexDeclaration((MySqlParser.IndexDeclarationContext) ctx);
    } else {
      throw ParserError.make(ctx);
    }
  }

  @Override
  public ColumnDeclaration visitColumnDeclaration(MySqlParser.ColumnDeclarationContext ctx) {
    if (ctx == null) {
      return null;
    }

    Uid uid = this.visitUid(ctx.uid());
    ColumnDefinition columnDefinition = this.visitColumnDefinition(ctx.columnDefinition());
    return RelationalAlgebraExpressionFactory.makeColumnDeclaration(uid, columnDefinition);
  }

  @Override
  public TableConstraint visitConstraintDeclaration(MySqlParser.ConstraintDeclarationContext ctx) {
    if (ctx == null) {
      return null;
    }

    if (ctx.getChild(0) instanceof MySqlParser.TableConstraintContext) {
      return this.visitTableConstraint((MySqlParser.TableConstraintContext) ctx.getChild(0));
    } else {
      throw ParserError.make(ctx);
    }
  }

  @Override
  public IndexColumnDefinition visitIndexDeclaration(MySqlParser.IndexDeclarationContext ctx) {
    if (ctx == null) {
      return null;
    }

    if (ctx.getChild(0) instanceof MySqlParser.IndexColumnDefinitionContext) {
      return this
          .visitIndexColumnDefinition((MySqlParser.IndexColumnDefinitionContext) ctx.getChild(0));
    } else {
      throw ParserError.make(ctx);
    }
  }

  @Override
  public ColumnDefinition visitColumnDefinition(MySqlParser.ColumnDefinitionContext ctx) {
    if (ctx == null) {
      return null;
    }

    DataType dataType = this.visitDataType(ctx.dataType());
    List<ColumnConstraint> columnConstraints = Lists.newArrayList();
    if (CollectionUtils.isNotEmpty(ctx.columnConstraint())) {
      for (MySqlParser.ColumnConstraintContext columnConstraintCtx : ctx.columnConstraint()) {
        columnConstraints.add(this.visitColumnConstraint(columnConstraintCtx));
      }
    }
    return RelationalAlgebraExpressionFactory.makeColumnDefinition(dataType, columnConstraints);

  }

  public ColumnConstraint visitColumnConstraint(MySqlParser.ColumnConstraintContext ctx) {
    if (ctx == null) {
      return null;
    }

    if (ctx instanceof MySqlParser.NullColumnConstraintContext) {
      return this.visitNullColumnConstraint((MySqlParser.NullColumnConstraintContext) ctx);
    } else if (ctx instanceof MySqlParser.DefaultColumnConstraintContext) {
      return this.visitDefaultColumnConstraint((MySqlParser.DefaultColumnConstraintContext) ctx);
    } else if (ctx instanceof MySqlParser.AutoIncrementColumnConstraintContext) {
      return this.visitAutoIncrementColumnConstraint(
        (MySqlParser.AutoIncrementColumnConstraintContext) ctx);
    } else if (ctx instanceof MySqlParser.PrimaryKeyColumnConstraintContext) {
      return this
          .visitPrimaryKeyColumnConstraint((MySqlParser.PrimaryKeyColumnConstraintContext) ctx);
    } else if (ctx instanceof MySqlParser.UniqueKeyColumnConstraintContext) {
      return this
          .visitUniqueKeyColumnConstraint((MySqlParser.UniqueKeyColumnConstraintContext) ctx);
    } else if (ctx instanceof MySqlParser.CommentColumnConstraintContext) {
      return this.visitCommentColumnConstraint((MySqlParser.CommentColumnConstraintContext) ctx);
    } else if (ctx instanceof MySqlParser.FormatColumnConstraintContext) {
      return this.visitFormatColumnConstraint((MySqlParser.FormatColumnConstraintContext) ctx);
    } else if (ctx instanceof MySqlParser.StorageColumnConstraintContext) {
      return this.visitStorageColumnConstraint((MySqlParser.StorageColumnConstraintContext) ctx);
    } else if (ctx instanceof MySqlParser.ReferenceColumnConstraintContext) {
      return this
          .visitReferenceColumnConstraint((MySqlParser.ReferenceColumnConstraintContext) ctx);
    } else if (ctx instanceof MySqlParser.CollateColumnConstraintContext) {
      return this.visitCollateColumnConstraint((MySqlParser.CollateColumnConstraintContext) ctx);
    } else if (ctx instanceof MySqlParser.GeneratedColumnConstraintContext) {
      return this
          .visitGeneratedColumnConstraint((MySqlParser.GeneratedColumnConstraintContext) ctx);
    } else if (ctx instanceof MySqlParser.SerialDefaultColumnConstraintContext) {
      return this.visitSerialDefaultColumnConstraint(
        (MySqlParser.SerialDefaultColumnConstraintContext) ctx);
    } else {
      throw ParserError.make(ctx);
    }
  }

  @Override
  public NullColumnConstraint
      visitNullColumnConstraint(MySqlParser.NullColumnConstraintContext ctx) {
    if (ctx == null) {
      return null;
    }

    NullNotnull nullNotnull = this.visitNullNotnull(ctx.nullNotnull());
    return RelationalAlgebraExpressionFactory.makeNullColumnConstraint(nullNotnull);
  }

  @Override
  public DefaultColumnConstraint
      visitDefaultColumnConstraint(MySqlParser.DefaultColumnConstraintContext ctx) {
    if (ctx == null) {
      return null;
    }

    DefaultValue defaultValue = this.visitDefaultValue(ctx.defaultValue());
    return RelationalAlgebraExpressionFactory.makeDefaultColumnConstraint(defaultValue);
  }

  @Override
  public AutoIncrementColumnConstraint
      visitAutoIncrementColumnConstraint(MySqlParser.AutoIncrementColumnConstraintContext ctx) {
    if (ctx == null) {
      return null;
    }

    AutoIncrementColumnConstraint.Type type = null;
    if (ctx.AUTO_INCREMENT() != null) {
      type = AutoIncrementColumnConstraint.Type.AUTO_INCREMENT;
    } else if (ctx.UPDATE() != null) {
      type = AutoIncrementColumnConstraint.Type.ON_UPDATE;
    } else {
      throw ParserError.make(ctx);
    }
    CurrentTimestamp currentTimestamp = this.visitCurrentTimestamp(ctx.currentTimestamp());

    return RelationalAlgebraExpressionFactory.makeAutoIncrementColumnConstraint(type,
      currentTimestamp);
  }

  @Override
  public PrimaryKeyColumnConstraint
      visitPrimaryKeyColumnConstraint(MySqlParser.PrimaryKeyColumnConstraintContext ctx) {
    if (ctx == null) {
      return null;
    }

    return RelationalAlgebraExpressionFactory.makePrimaryKeyColumnConstraint();
  }

  @Override
  public UniqueKeyColumnConstraint
      visitUniqueKeyColumnConstraint(MySqlParser.UniqueKeyColumnConstraintContext ctx) {
    if (ctx == null) {
      return null;
    }

    return RelationalAlgebraExpressionFactory.makeUniqueKeyColumnConstraint();
  }

  @Override
  public CommentColumnConstraint
      visitCommentColumnConstraint(MySqlParser.CommentColumnConstraintContext ctx) {
    if (ctx == null) {
      return null;
    }

    return RelationalAlgebraExpressionFactory
        .makeCommentColumnConstraint(ctx.STRING_LITERAL().getText());
  }

  @Override
  public FormatColumnConstraint
      visitFormatColumnConstraint(MySqlParser.FormatColumnConstraintContext ctx) {
    if (ctx == null) {
      return null;
    }

    FormatColumnConstraint.ColformatEnum colformatEnum =
        FormatColumnConstraint.ColformatEnum.valueOf(ctx.colformat.getText().toUpperCase());
    return RelationalAlgebraExpressionFactory.makeFormatColumnConstraint(colformatEnum);
  }

  @Override
  public StorageColumnConstraint
      visitStorageColumnConstraint(MySqlParser.StorageColumnConstraintContext ctx) {
    if (ctx == null) {
      return null;
    }

    StorageColumnConstraint.StoragevalEnum storageval =
        StorageColumnConstraint.StoragevalEnum.valueOf(ctx.storageval.getText().toUpperCase());
    return RelationalAlgebraExpressionFactory.makeStorageColumnConstraint(storageval);
  }

  @Override
  public ReferenceColumnConstraint
      visitReferenceColumnConstraint(MySqlParser.ReferenceColumnConstraintContext ctx) {
    if (ctx == null) {
      return null;
    }

    ReferenceDefinition referenceDefinition =
        this.visitReferenceDefinition(ctx.referenceDefinition());
    return RelationalAlgebraExpressionFactory.makeReferenceColumnConstraint(referenceDefinition);
  }

  @Override
  public CollateColumnConstraint
      visitCollateColumnConstraint(MySqlParser.CollateColumnConstraintContext ctx) {
    if (ctx == null) {
      return null;
    }

    CollationName collationName = this.visitCollationName(ctx.collationName());
    return RelationalAlgebraExpressionFactory.makeCollateColumnConstraint(collationName);
  }

  @Override
  public GeneratedColumnConstraint
      visitGeneratedColumnConstraint(MySqlParser.GeneratedColumnConstraintContext ctx) {
    if (ctx == null) {
      return null;
    }

    Boolean always = null;
    if (ctx.ALWAYS() != null) {
      always = Boolean.TRUE;
    }
    Expression expression = this.visitExpression(ctx.expression());
    GeneratedColumnConstraint.Type type = null;
    if (ctx.VIRTUAL() != null) {
      type = GeneratedColumnConstraint.Type.VIRTUAL;
    } else if (ctx.STORED() != null) {
      type = GeneratedColumnConstraint.Type.STORED;
    }
    return RelationalAlgebraExpressionFactory.makeGeneratedColumnConstraint(always, expression,
      type);
  }

  @Override
  public SerialDefaultColumnConstraint
      visitSerialDefaultColumnConstraint(MySqlParser.SerialDefaultColumnConstraintContext ctx) {
    if (ctx == null) {
      return null;
    }

    return RelationalAlgebraExpressionFactory.makeSerialDefaultColumnConstraint();
  }

  public TableConstraint visitTableConstraint(MySqlParser.TableConstraintContext ctx) {
    if (ctx == null) {
      return null;
    }

    if (ctx instanceof MySqlParser.PrimaryKeyTableConstraintContext) {
      return this
          .visitPrimaryKeyTableConstraint((MySqlParser.PrimaryKeyTableConstraintContext) ctx);
    } else if (ctx instanceof MySqlParser.UniqueKeyTableConstraintContext) {
      return this.visitUniqueKeyTableConstraint((MySqlParser.UniqueKeyTableConstraintContext) ctx);
    } else if (ctx instanceof MySqlParser.ForeignKeyTableConstraintContext) {
      return this
          .visitForeignKeyTableConstraint((MySqlParser.ForeignKeyTableConstraintContext) ctx);
    } else if (ctx instanceof MySqlParser.CheckTableConstraintContext) {
      return this.visitCheckTableConstraint((MySqlParser.CheckTableConstraintContext) ctx);
    } else {
      throw ParserError.make(ctx);
    }
  }

  @Override
  public PrimaryKeyTableConstraint
      visitPrimaryKeyTableConstraint(MySqlParser.PrimaryKeyTableConstraintContext ctx) {
    if (ctx == null) {
      return null;
    }

    Boolean constraint = null;
    if (ctx.CONSTRAINT() != null) {
      constraint = Boolean.TRUE;
    }
    Uid name = this.visitUid(ctx.name);
    Uid index = this.visitUid(ctx.index);
    DdlStatement.IndexTypeEnum indexType = this.visitIndexType(ctx.indexType());
    IndexColumnNames indexColumnNames = this.visitIndexColumnNames(ctx.indexColumnNames());
    List<IndexOption> indexOptions = Lists.newArrayList();
    if (CollectionUtils.isNotEmpty(ctx.indexOption())) {
      for (MySqlParser.IndexOptionContext indexOptionCtx : ctx.indexOption()) {
        indexOptions.add(this.visitIndexOption(indexOptionCtx));
      }
    }

    return RelationalAlgebraExpressionFactory.makePrimaryKeyTableConstraint(constraint, name, index,
      indexType, indexColumnNames, indexOptions);
  }

  @Override
  public UniqueKeyTableConstraint
      visitUniqueKeyTableConstraint(MySqlParser.UniqueKeyTableConstraintContext ctx) {
    if (ctx == null) {
      return null;
    }

    Boolean constraint = null;
    if (ctx.CONSTRAINT() != null) {
      constraint = Boolean.TRUE;
    }
    Uid name = this.visitUid(ctx.name);
    DdlStatement.IndexFormatEnum indexFormat = null;
    if (ctx.INDEX() != null) {
      indexFormat = DdlStatement.IndexFormatEnum.INDEX;
    } else if (ctx.KEY() != null) {
      indexFormat = DdlStatement.IndexFormatEnum.KEY;
    }
    Uid index = this.visitUid(ctx.index);
    DdlStatement.IndexTypeEnum indexType = this.visitIndexType(ctx.indexType());
    IndexColumnNames indexColumnNames = this.visitIndexColumnNames(ctx.indexColumnNames());
    List<IndexOption> indexOptions = Lists.newArrayList();
    if (CollectionUtils.isNotEmpty(ctx.indexOption())) {
      for (MySqlParser.IndexOptionContext indexOptionCtx : ctx.indexOption()) {
        indexOptions.add(this.visitIndexOption(indexOptionCtx));
      }
    }

    return RelationalAlgebraExpressionFactory.makeUniqueKeyTableConstraint(constraint, name,
      indexFormat, index, indexType, indexColumnNames, indexOptions);
  }

  @Override
  public ForeignKeyTableConstraint
      visitForeignKeyTableConstraint(MySqlParser.ForeignKeyTableConstraintContext ctx) {
    if (ctx == null) {
      return null;
    }

    Boolean constraint = null;
    if (ctx.CONSTRAINT() != null) {
      constraint = Boolean.TRUE;
    }
    Uid name = this.visitUid(ctx.name);
    Uid index = this.visitUid(ctx.index);
    IndexColumnNames indexColumnNames = this.visitIndexColumnNames(ctx.indexColumnNames());
    ReferenceDefinition referenceDefinition =
        this.visitReferenceDefinition(ctx.referenceDefinition());

    return RelationalAlgebraExpressionFactory.makeForeignKeyTableConstraint(constraint, name, index,
      indexColumnNames, referenceDefinition);
  }

  @Override
  public CheckTableConstraint
      visitCheckTableConstraint(MySqlParser.CheckTableConstraintContext ctx) {
    if (ctx == null) {
      return null;
    }

    Boolean constraint = null;
    if (ctx.CONSTRAINT() != null) {
      constraint = Boolean.TRUE;
    }
    Uid name = this.visitUid(ctx.name);
    Expression expression = this.visitExpression(ctx.expression());
    return RelationalAlgebraExpressionFactory.makeCheckTableConstraint(constraint, name,
      expression);
  }

  @Override
  public ReferenceDefinition visitReferenceDefinition(MySqlParser.ReferenceDefinitionContext ctx) {
    if (ctx == null) {
      return null;
    }

    TableName tableName = this.visitTableName(ctx.tableName());
    IndexColumnNames indexColumnNames = this.visitIndexColumnNames(ctx.indexColumnNames());
    ReferenceDefinition.MatchTypeEnum matchType = null;
    if (ctx.FULL() != null) {
      matchType = ReferenceDefinition.MatchTypeEnum.FULL;
    } else if (ctx.PARTIAL() != null) {
      matchType = ReferenceDefinition.MatchTypeEnum.PARTIAL;
    } else if (ctx.SIMPLE() != null) {
      matchType = ReferenceDefinition.MatchTypeEnum.SIMPLE;
    }
    ReferenceAction referenceAction = this.visitReferenceAction(ctx.referenceAction());

    return RelationalAlgebraExpressionFactory.makeReferenceDefinition(tableName, indexColumnNames,
      matchType, referenceAction);
  }

  @Override
  public ReferenceAction visitReferenceAction(MySqlParser.ReferenceActionContext ctx) {
    if (ctx == null) {
      return null;
    }

    ReferenceControlTypeEnum onDelete = this.visitReferenceControlType(ctx.onDelete);
    ReferenceControlTypeEnum onUpdate = this.visitReferenceControlType(ctx.onUpdate);
    return RelationalAlgebraExpressionFactory.makeReferenceAction(onDelete, onUpdate);
  }

  @Override
  public DdlStatement.ReferenceControlTypeEnum
      visitReferenceControlType(MySqlParser.ReferenceControlTypeContext ctx) {
    if (ctx == null) {
      return null;
    }

    if (ctx.RESTRICT() != null) {
      return DdlStatement.ReferenceControlTypeEnum.RESTRICT;
    } else if (ctx.CASCADE() != null) {
      return DdlStatement.ReferenceControlTypeEnum.CASCADE;
    } else if (ctx.SET() != null) {
      return DdlStatement.ReferenceControlTypeEnum.SET_NULL;
    } else if (ctx.NO() != null) {
      return DdlStatement.ReferenceControlTypeEnum.NO_ACTION;
    } else {
      throw ParserError.make(ctx);
    }
  }

  public IndexColumnDefinition
      visitIndexColumnDefinition(MySqlParser.IndexColumnDefinitionContext ctx) {
    if (ctx == null) {
      return null;
    }

    if (ctx instanceof MySqlParser.SimpleIndexDeclarationContext) {
      return this.visitSimpleIndexDeclaration((MySqlParser.SimpleIndexDeclarationContext) ctx);
    } else if (ctx instanceof MySqlParser.SpecialIndexDeclarationContext) {
      return this.visitSpecialIndexDeclaration((MySqlParser.SpecialIndexDeclarationContext) ctx);
    } else {
      throw ParserError.make(ctx);
    }
  }

  @Override
  public SimpleIndexDeclaration
      visitSimpleIndexDeclaration(MySqlParser.SimpleIndexDeclarationContext ctx) {
    if (ctx == null) {
      return null;
    }

    DdlStatement.IndexFormatEnum indexFormat = null;
    if (ctx.INDEX() != null) {
      indexFormat = DdlStatement.IndexFormatEnum.INDEX;
    } else if (ctx.KEY() != null) {
      indexFormat = DdlStatement.IndexFormatEnum.KEY;
    } else {
      throw ParserError.make(ctx);
    }
    Uid uid = this.visitUid(ctx.uid());
    IndexTypeEnum indexType = this.visitIndexType(ctx.indexType());
    IndexColumnNames indexColumnNames = this.visitIndexColumnNames(ctx.indexColumnNames());
    List<IndexOption> indexOptions = Lists.newArrayList();
    if (CollectionUtils.isNotEmpty(ctx.indexOption())) {
      for (MySqlParser.IndexOptionContext indexOptionCtx : ctx.indexOption()) {
        indexOptions.add(this.visitIndexOption(indexOptionCtx));
      }
    }

    return RelationalAlgebraExpressionFactory.makeSimpleIndexDeclaration(indexFormat, uid,
      indexType, indexColumnNames, indexOptions);
  }

  @Override
  public SpecialIndexDeclaration
      visitSpecialIndexDeclaration(MySqlParser.SpecialIndexDeclarationContext ctx) {
    if (ctx == null) {
      return null;
    }

    SpecialIndexDeclaration.Type type = null;
    if (ctx.FULLTEXT() != null) {
      type = SpecialIndexDeclaration.Type.FULLTEXT;
    } else if (ctx.SPATIAL() != null) {
      type = SpecialIndexDeclaration.Type.SPATIAL;
    } else {
      throw ParserError.make(ctx);
    }

    DdlStatement.IndexFormatEnum indexFormat = null;
    if (ctx.INDEX() != null) {
      indexFormat = DdlStatement.IndexFormatEnum.INDEX;
    } else if (ctx.KEY() != null) {
      indexFormat = DdlStatement.IndexFormatEnum.KEY;
    }
    Uid uid = this.visitUid(ctx.uid());
    IndexColumnNames indexColumnNames = this.visitIndexColumnNames(ctx.indexColumnNames());
    List<IndexOption> indexOptions = Lists.newArrayList();
    if (CollectionUtils.isNotEmpty(ctx.indexOption())) {
      for (MySqlParser.IndexOptionContext indexOptionCtx : ctx.indexOption()) {
        indexOptions.add(this.visitIndexOption(indexOptionCtx));
      }
    }

    return RelationalAlgebraExpressionFactory.makeSpecialIndexDeclaration(type, indexFormat, uid,
      indexColumnNames, indexOptions);
  }

  public TableOption visitTableOption(MySqlParser.TableOptionContext ctx) {
    if (ctx == null) {
      return null;
    }

    if (ctx instanceof MySqlParser.TableOptionEngineContext) {
      return this.visitTableOptionEngine((MySqlParser.TableOptionEngineContext) ctx);
    } else if (ctx instanceof MySqlParser.TableOptionAutoIncrementContext) {
      return this.visitTableOptionAutoIncrement((MySqlParser.TableOptionAutoIncrementContext) ctx);
    } else if (ctx instanceof MySqlParser.TableOptionAverageContext) {
      return this.visitTableOptionAverage((MySqlParser.TableOptionAverageContext) ctx);
    } else if (ctx instanceof MySqlParser.TableOptionCharsetContext) {
      return this.visitTableOptionCharset((MySqlParser.TableOptionCharsetContext) ctx);
    } else if (ctx instanceof MySqlParser.TableOptionChecksumContext) {
      return this.visitTableOptionChecksum((MySqlParser.TableOptionChecksumContext) ctx);
    } else if (ctx instanceof MySqlParser.TableOptionCollateContext) {
      return this.visitTableOptionCollate((MySqlParser.TableOptionCollateContext) ctx);
    } else if (ctx instanceof MySqlParser.TableOptionCommentContext) {
      return this.visitTableOptionComment((MySqlParser.TableOptionCommentContext) ctx);
    } else if (ctx instanceof MySqlParser.TableOptionCompressionContext) {
      return this.visitTableOptionCompression((MySqlParser.TableOptionCompressionContext) ctx);
    } else if (ctx instanceof MySqlParser.TableOptionConnectionContext) {
      return this.visitTableOptionConnection((MySqlParser.TableOptionConnectionContext) ctx);
    } else if (ctx instanceof MySqlParser.TableOptionDataDirectoryContext) {
      return this.visitTableOptionDataDirectory((MySqlParser.TableOptionDataDirectoryContext) ctx);
    } else if (ctx instanceof MySqlParser.TableOptionDelayContext) {
      return this.visitTableOptionDelay((MySqlParser.TableOptionDelayContext) ctx);
    } else if (ctx instanceof MySqlParser.TableOptionEncryptionContext) {
      return this.visitTableOptionEncryption((MySqlParser.TableOptionEncryptionContext) ctx);
    } else if (ctx instanceof MySqlParser.TableOptionIndexDirectoryContext) {
      return this
          .visitTableOptionIndexDirectory((MySqlParser.TableOptionIndexDirectoryContext) ctx);
    } else if (ctx instanceof MySqlParser.TableOptionInsertMethodContext) {
      return this.visitTableOptionInsertMethod((MySqlParser.TableOptionInsertMethodContext) ctx);
    } else if (ctx instanceof MySqlParser.TableOptionKeyBlockSizeContext) {
      return this.visitTableOptionKeyBlockSize((MySqlParser.TableOptionKeyBlockSizeContext) ctx);
    } else if (ctx instanceof MySqlParser.TableOptionMaxRowsContext) {
      return this.visitTableOptionMaxRows((MySqlParser.TableOptionMaxRowsContext) ctx);
    } else if (ctx instanceof MySqlParser.TableOptionMinRowsContext) {
      return this.visitTableOptionMinRows((MySqlParser.TableOptionMinRowsContext) ctx);
    } else if (ctx instanceof MySqlParser.TableOptionPackKeysContext) {
      return this.visitTableOptionPackKeys((MySqlParser.TableOptionPackKeysContext) ctx);
    } else if (ctx instanceof MySqlParser.TableOptionPasswordContext) {
      return this.visitTableOptionPassword((MySqlParser.TableOptionPasswordContext) ctx);
    } else if (ctx instanceof MySqlParser.TableOptionRowFormatContext) {
      return this.visitTableOptionRowFormat((MySqlParser.TableOptionRowFormatContext) ctx);
    } else if (ctx instanceof MySqlParser.TableOptionRecalculationContext) {
      return this.visitTableOptionRecalculation((MySqlParser.TableOptionRecalculationContext) ctx);
    } else if (ctx instanceof MySqlParser.TableOptionPersistentContext) {
      return this.visitTableOptionPersistent((MySqlParser.TableOptionPersistentContext) ctx);
    } else if (ctx instanceof MySqlParser.TableOptionSamplePageContext) {
      return this.visitTableOptionSamplePage((MySqlParser.TableOptionSamplePageContext) ctx);
    } else if (ctx instanceof MySqlParser.TableOptionTablespaceContext) {
      return this.visitTableOptionTablespace((MySqlParser.TableOptionTablespaceContext) ctx);
    } else if (ctx instanceof MySqlParser.TableOptionUnionContext) {
      return this.visitTableOptionUnion((MySqlParser.TableOptionUnionContext) ctx);
    } else {
      throw ParserError.make(ctx);
    }
  }

  @Override
  public TableOptionEngine visitTableOptionEngine(MySqlParser.TableOptionEngineContext ctx) {
    if (ctx == null) {
      return null;
    }

    EngineName engineName = this.visitEngineName(ctx.engineName());
    return RelationalAlgebraExpressionFactory.makeTableOptionEngine(engineName);
  }

  @Override
  public TableOptionAutoIncrement
      visitTableOptionAutoIncrement(MySqlParser.TableOptionAutoIncrementContext ctx) {
    if (ctx == null) {
      return null;
    }

    return RelationalAlgebraExpressionFactory
        .makeTableOptionAutoIncrement(this.visitDecimalLiteral(ctx.decimalLiteral()));
  }

  @Override
  public TableOptionAverage visitTableOptionAverage(MySqlParser.TableOptionAverageContext ctx) {
    if (ctx == null) {
      return null;
    }

    return RelationalAlgebraExpressionFactory
        .makeTableOptionAverage(this.visitDecimalLiteral(ctx.decimalLiteral()));
  }

  @Override
  public TableOptionCharset visitTableOptionCharset(MySqlParser.TableOptionCharsetContext ctx) {
    if (ctx == null) {
      return null;
    }

    Boolean isDefault = null;
    if (ctx.DEFAULT() != null) {
      isDefault = Boolean.TRUE;
    }
    CharsetName charsetName = this.visitCharsetName(ctx.charsetName());
    return RelationalAlgebraExpressionFactory.makeTableOptionCharset(isDefault, charsetName);
  }

  @Override
  public TableOptionChecksum visitTableOptionChecksum(MySqlParser.TableOptionChecksumContext ctx) {
    if (ctx == null) {
      return null;
    }

    TableOptionChecksum.Type type = null;
    if (ctx.CHECKSUM() != null) {
      type = TableOptionChecksum.Type.CHECKSUM;
    } else if (ctx.PAGE_CHECKSUM() != null) {
      type = TableOptionChecksum.Type.PAGE_CHECKSUM;
    } else {
      throw ParserError.make(ctx);
    }
    DdlStatement.BoolValueEnum boolValue = null;
    if ("0".equals(ctx.boolValue.getText())) {
      boolValue = DdlStatement.BoolValueEnum.ZERO;
    } else if ("1".equals(ctx.boolValue.getText())) {
      boolValue = DdlStatement.BoolValueEnum.ONE;
    } else {
      throw ParserError.make(ctx);
    }

    return RelationalAlgebraExpressionFactory.makeTableOptionChecksum(type, boolValue);
  }

  @Override
  public TableOptionCollate visitTableOptionCollate(MySqlParser.TableOptionCollateContext ctx) {
    if (ctx == null) {
      return null;
    }

    Boolean isDefault = null;
    if (ctx.DEFAULT() != null) {
      isDefault = Boolean.TRUE;
    }
    CollationName collationName = this.visitCollationName(ctx.collationName());

    return RelationalAlgebraExpressionFactory.makeTableOptionCollate(isDefault, collationName);
  }

  @Override
  public TableOptionComment visitTableOptionComment(MySqlParser.TableOptionCommentContext ctx) {
    if (ctx == null) {
      return null;
    }

    return RelationalAlgebraExpressionFactory
        .makeTableOptionComment(ctx.STRING_LITERAL().getText());
  }

  @Override
  public TableOptionCompression
      visitTableOptionCompression(MySqlParser.TableOptionCompressionContext ctx) {
    if (ctx == null) {
      return null;
    }

    String stringLiteralOrId = null;
    if (ctx.STRING_LITERAL() != null) {
      stringLiteralOrId = ctx.STRING_LITERAL().getText();
    } else if (ctx.ID() != null) {
      stringLiteralOrId = ctx.ID().getText();
    } else {
      throw ParserError.make(ctx);
    }

    return RelationalAlgebraExpressionFactory.makeTableOptionCompression(stringLiteralOrId);
  }

  @Override
  public TableOptionConnection
      visitTableOptionConnection(MySqlParser.TableOptionConnectionContext ctx) {
    if (ctx == null) {
      return null;
    }
    return RelationalAlgebraExpressionFactory
        .makeTableOptionConnection(ctx.STRING_LITERAL().getText());
  }

  @Override
  public TableOptionDataDirectory
      visitTableOptionDataDirectory(MySqlParser.TableOptionDataDirectoryContext ctx) {
    if (ctx == null) {
      return null;
    }
    return RelationalAlgebraExpressionFactory
        .makeTableOptionDataDirectory(ctx.STRING_LITERAL().getText());
  }

  @Override
  public TableOptionDelay visitTableOptionDelay(MySqlParser.TableOptionDelayContext ctx) {
    if (ctx == null) {
      return null;
    }

    DdlStatement.BoolValueEnum boolValue = null;
    if ("0".equals(ctx.boolValue.getText())) {
      boolValue = DdlStatement.BoolValueEnum.ZERO;
    } else if ("1".equals(ctx.boolValue.getText())) {
      boolValue = DdlStatement.BoolValueEnum.ONE;
    } else {
      throw ParserError.make(ctx);
    }

    return RelationalAlgebraExpressionFactory.makeTableOptionDelay(boolValue);
  }

  @Override
  public TableOptionEncryption
      visitTableOptionEncryption(MySqlParser.TableOptionEncryptionContext ctx) {
    if (ctx == null) {
      return null;
    }
    return RelationalAlgebraExpressionFactory
        .makeTableOptionEncryption(ctx.STRING_LITERAL().getText());
  }

  @Override
  public TableOptionIndexDirectory
      visitTableOptionIndexDirectory(MySqlParser.TableOptionIndexDirectoryContext ctx) {
    if (ctx == null) {
      return null;
    }
    return RelationalAlgebraExpressionFactory
        .makeTableOptionIndexDirectory(ctx.STRING_LITERAL().getText());
  }

  @Override
  public TableOptionInsertMethod
      visitTableOptionInsertMethod(MySqlParser.TableOptionInsertMethodContext ctx) {
    if (ctx == null) {
      return null;
    }

    TableOptionInsertMethod.InsertMethodEnum insertMethod =
        TableOptionInsertMethod.InsertMethodEnum.valueOf(ctx.insertMethod.getText().toUpperCase());
    return RelationalAlgebraExpressionFactory.makeTableOptionInsertMethod(insertMethod);
  }

  @Override
  public TableOptionKeyBlockSize
      visitTableOptionKeyBlockSize(MySqlParser.TableOptionKeyBlockSizeContext ctx) {
    if (ctx == null) {
      return null;
    }
    return RelationalAlgebraExpressionFactory
        .makeTableOptionKeyBlockSize(this.visitFileSizeLiteral(ctx.fileSizeLiteral()));
  }

  @Override
  public TableOptionMaxRows visitTableOptionMaxRows(MySqlParser.TableOptionMaxRowsContext ctx) {
    if (ctx == null) {
      return null;
    }
    return RelationalAlgebraExpressionFactory
        .makeTableOptionMaxRows(this.visitDecimalLiteral(ctx.decimalLiteral()));
  }

  @Override
  public TableOptionMinRows visitTableOptionMinRows(MySqlParser.TableOptionMinRowsContext ctx) {
    if (ctx == null) {
      return null;
    }
    return RelationalAlgebraExpressionFactory
        .makeTableOptionMinRows(this.visitDecimalLiteral(ctx.decimalLiteral()));
  }

  @Override
  public TableOptionPackKeys visitTableOptionPackKeys(MySqlParser.TableOptionPackKeysContext ctx) {
    if (ctx == null) {
      return null;
    }

    DdlStatement.ExtBoolValueEnum extBoolValue = null;
    if (ctx.DEFAULT() != null) {
      extBoolValue = DdlStatement.ExtBoolValueEnum.DEFAULT;
    } else if ("0".equals(ctx.extBoolValue.getText())) {
      extBoolValue = DdlStatement.ExtBoolValueEnum.ZERO;
    } else if ("1".equals(ctx.extBoolValue.getText())) {
      extBoolValue = DdlStatement.ExtBoolValueEnum.ONE;
    } else {
      throw ParserError.make(ctx);
    }
    return RelationalAlgebraExpressionFactory.makeTableOptionPackKeys(extBoolValue);
  }

  @Override
  public TableOptionPassword visitTableOptionPassword(MySqlParser.TableOptionPasswordContext ctx) {
    if (ctx == null) {
      return null;
    }
    return RelationalAlgebraExpressionFactory
        .makeTableOptionPassword(ctx.STRING_LITERAL().getText());
  }

  @Override
  public TableOptionRowFormat
      visitTableOptionRowFormat(MySqlParser.TableOptionRowFormatContext ctx) {
    if (ctx == null) {
      return null;
    }

    TableOptionRowFormat.RowFormatEnum rowFormat =
        TableOptionRowFormat.RowFormatEnum.valueOf(ctx.rowFormat.getText().toUpperCase());
    return RelationalAlgebraExpressionFactory.makeTableOptionRowFormat(rowFormat);
  }

  @Override
  public TableOptionRecalculation
      visitTableOptionRecalculation(MySqlParser.TableOptionRecalculationContext ctx) {
    if (ctx == null) {
      return null;
    }

    DdlStatement.ExtBoolValueEnum extBoolValue = null;
    if (ctx.DEFAULT() != null) {
      extBoolValue = DdlStatement.ExtBoolValueEnum.DEFAULT;
    } else if ("0".equals(ctx.extBoolValue.getText())) {
      extBoolValue = DdlStatement.ExtBoolValueEnum.ZERO;
    } else if ("1".equals(ctx.extBoolValue.getText())) {
      extBoolValue = DdlStatement.ExtBoolValueEnum.ONE;
    } else {
      throw ParserError.make(ctx);
    }
    return RelationalAlgebraExpressionFactory.makeTableOptionRecalculation(extBoolValue);
  }

  @Override
  public TableOptionPersistent
      visitTableOptionPersistent(MySqlParser.TableOptionPersistentContext ctx) {
    if (ctx == null) {
      return null;
    }

    DdlStatement.ExtBoolValueEnum extBoolValue = null;
    if (ctx.DEFAULT() != null) {
      extBoolValue = DdlStatement.ExtBoolValueEnum.DEFAULT;
    } else if ("0".equals(ctx.extBoolValue.getText())) {
      extBoolValue = DdlStatement.ExtBoolValueEnum.ZERO;
    } else if ("1".equals(ctx.extBoolValue.getText())) {
      extBoolValue = DdlStatement.ExtBoolValueEnum.ONE;
    } else {
      throw ParserError.make(ctx);
    }

    return RelationalAlgebraExpressionFactory.makeTableOptionPersistent(extBoolValue);
  }

  @Override
  public TableOptionSamplePage
      visitTableOptionSamplePage(MySqlParser.TableOptionSamplePageContext ctx) {
    if (ctx == null) {
      return null;
    }

    DecimalLiteral decimalLiteral = this.visitDecimalLiteral(ctx.decimalLiteral());
    return RelationalAlgebraExpressionFactory.makeTableOptionSamplePage(decimalLiteral);
  }

  @Override
  public TableOptionTablespace
      visitTableOptionTablespace(MySqlParser.TableOptionTablespaceContext ctx) {
    if (ctx == null) {
      return null;
    }

    Uid uid = this.visitUid(ctx.uid());
    DdlStatement.TablespaceStorageEnum tablespaceStorage =
        this.visitTablespaceStorage(ctx.tablespaceStorage());
    return RelationalAlgebraExpressionFactory.makeTableOptionTablespace(uid, tablespaceStorage);
  }

  @Override
  public TableOptionUnion visitTableOptionUnion(MySqlParser.TableOptionUnionContext ctx) {
    if (ctx == null) {
      return null;
    }

    Tables tables = this.visitTables(ctx.tables());
    return RelationalAlgebraExpressionFactory.makeTableOptionUnion(tables);
  }

  @Override
  public DdlStatement.TablespaceStorageEnum
      visitTablespaceStorage(MySqlParser.TablespaceStorageContext ctx) {
    if (ctx == null) {
      return null;
    }

    if (ctx.DISK() != null) {
      return DdlStatement.TablespaceStorageEnum.DISK;
    } else if (ctx.MEMORY() != null) {
      return DdlStatement.TablespaceStorageEnum.MEMORY;
    } else if (ctx.DEFAULT() != null) {
      return DdlStatement.TablespaceStorageEnum.DEFAULT;
    } else {
      throw ParserError.make(ctx);
    }
  }

  @Override
  public PartitionDefinitions
      visitPartitionDefinitions(MySqlParser.PartitionDefinitionsContext ctx) {
    if (ctx == null) {
      return null;
    }

    PartitionFunctionDefinition partitionFunctionDefinition =
        this.visitPartitionFunctionDefinition(ctx.partitionFunctionDefinition());
    DecimalLiteral count = this.visitDecimalLiteral(ctx.count);
    SubpartitionFunctionDefinition subpartitionFunctionDefinition =
        this.visitSubpartitionFunctionDefinition(ctx.subpartitionFunctionDefinition());
    DecimalLiteral subCount = this.visitDecimalLiteral(ctx.subCount);
    List<PartitionDefinition> partitionDefinitions = Lists.newArrayList();
    if (CollectionUtils.isNotEmpty(ctx.partitionDefinition())) {
      for (MySqlParser.PartitionDefinitionContext partitionDefinitionCtx : ctx
          .partitionDefinition()) {
        partitionDefinitions.add(this.visitPartitionDefinition(partitionDefinitionCtx));
      }
    }

    return RelationalAlgebraExpressionFactory.makePartitionDefinitions(partitionFunctionDefinition,
      count, subpartitionFunctionDefinition, subCount, partitionDefinitions);
  }

  public PartitionFunctionDefinition
      visitPartitionFunctionDefinition(MySqlParser.PartitionFunctionDefinitionContext ctx) {
    if (ctx == null) {
      return null;
    }

    if (ctx instanceof MySqlParser.PartitionFunctionHashContext) {
      return this.visitPartitionFunctionHash((MySqlParser.PartitionFunctionHashContext) ctx);
    } else if (ctx instanceof MySqlParser.PartitionFunctionKeyContext) {
      return this.visitPartitionFunctionKey((MySqlParser.PartitionFunctionKeyContext) ctx);
    } else if (ctx instanceof MySqlParser.PartitionFunctionRangeContext) {
      return this.visitPartitionFunctionRange((MySqlParser.PartitionFunctionRangeContext) ctx);
    } else if (ctx instanceof MySqlParser.PartitionFunctionListContext) {
      return this.visitPartitionFunctionList((MySqlParser.PartitionFunctionListContext) ctx);
    } else {
      throw ParserError.make(ctx);
    }
  }

  @Override
  public PartitionFunctionHash
      visitPartitionFunctionHash(MySqlParser.PartitionFunctionHashContext ctx) {
    if (ctx == null) {
      return null;
    }

    Boolean linear = null;
    if (ctx.LINEAR() != null) {
      linear = Boolean.TRUE;
    }
    Expression expression = this.visitExpression(ctx.expression());

    return RelationalAlgebraExpressionFactory.makePartitionFunctionHash(linear, expression);
  }

  @Override
  public PartitionFunctionKey
      visitPartitionFunctionKey(MySqlParser.PartitionFunctionKeyContext ctx) {
    if (ctx == null) {
      return null;
    }

    Boolean linear = null;
    if (ctx.LINEAR() != null) {
      linear = Boolean.TRUE;
    }
    UidList uidList = this.visitUidList(ctx.uidList());
    DdlStatement.PartitionAlgTypeEnum algType = null;
    if (ctx.algType != null) {
      if ("1".equals(ctx.algType.getText())) {
        algType = DdlStatement.PartitionAlgTypeEnum.ONE;
      } else if ("2".equals(ctx.algType.getText())) {
        algType = DdlStatement.PartitionAlgTypeEnum.TWO;
      }
    }
    return RelationalAlgebraExpressionFactory.makePartitionFunctionKey(linear, algType, uidList);
  }

  @Override
  public PartitionFunctionRange
      visitPartitionFunctionRange(MySqlParser.PartitionFunctionRangeContext ctx) {
    if (ctx == null) {
      return null;
    }

    Expression expression = this.visitExpression(ctx.expression());
    UidList uidList = this.visitUidList(ctx.uidList());
    return RelationalAlgebraExpressionFactory.makePartitionFunctionRange(expression, uidList);
  }

  @Override
  public PartitionFunctionList
      visitPartitionFunctionList(MySqlParser.PartitionFunctionListContext ctx) {
    if (ctx == null) {
      return null;
    }

    Expression expression = this.visitExpression(ctx.expression());
    UidList uidList = this.visitUidList(ctx.uidList());
    return RelationalAlgebraExpressionFactory.makePartitionFunctionList(expression, uidList);
  }

  public SubpartitionFunctionDefinition
      visitSubpartitionFunctionDefinition(MySqlParser.SubpartitionFunctionDefinitionContext ctx) {
    if (ctx == null) {
      return null;
    }
    if (ctx instanceof MySqlParser.SubPartitionFunctionHashContext) {
      return this.visitSubPartitionFunctionHash((MySqlParser.SubPartitionFunctionHashContext) ctx);
    } else if (ctx instanceof MySqlParser.SubPartitionFunctionKeyContext) {
      return this.visitSubPartitionFunctionKey((MySqlParser.SubPartitionFunctionKeyContext) ctx);
    } else {
      throw ParserError.make(ctx);
    }
  }

  @Override
  public SubPartitionFunctionHash
      visitSubPartitionFunctionHash(MySqlParser.SubPartitionFunctionHashContext ctx) {
    if (ctx == null) {
      return null;
    }

    Boolean linear = null;
    if (ctx.LINEAR() != null) {
      linear = Boolean.TRUE;
    }
    Expression expression = this.visitExpression(ctx.expression());

    return RelationalAlgebraExpressionFactory.makeSubPartitionFunctionHash(linear, expression);
  }

  @Override
  public SubPartitionFunctionKey
      visitSubPartitionFunctionKey(MySqlParser.SubPartitionFunctionKeyContext ctx) {
    if (ctx == null) {
      return null;
    }

    Boolean linear = null;
    if (ctx.LINEAR() != null) {
      linear = Boolean.TRUE;
    }
    DdlStatement.PartitionAlgTypeEnum algType = null;
    if (ctx.algType != null) {
      if ("1".equals(ctx.algType.getText())) {
        algType = DdlStatement.PartitionAlgTypeEnum.ONE;
      } else if ("2".equals(ctx.algType.getText())) {
        algType = DdlStatement.PartitionAlgTypeEnum.TWO;
      }
    }
    UidList uidList = this.visitUidList(ctx.uidList());

    return RelationalAlgebraExpressionFactory.makeSubPartitionFunctionKey(linear, algType, uidList);
  }

  public PartitionDefinition visitPartitionDefinition(MySqlParser.PartitionDefinitionContext ctx) {
    if (ctx == null) {
      return null;
    }

    if (ctx instanceof MySqlParser.PartitionComparisionContext) {
      return this.visitPartitionComparision((MySqlParser.PartitionComparisionContext) ctx);
    } else if (ctx instanceof MySqlParser.PartitionListAtomContext) {
      return this.visitPartitionListAtom((MySqlParser.PartitionListAtomContext) ctx);
    } else if (ctx instanceof MySqlParser.PartitionListVectorContext) {
      return this.visitPartitionListVector((MySqlParser.PartitionListVectorContext) ctx);
    } else if (ctx instanceof MySqlParser.PartitionSimpleContext) {
      return this.visitPartitionSimple((MySqlParser.PartitionSimpleContext) ctx);
    } else {
      throw ParserError.make(ctx);
    }

  }

  @Override
  public PartitionComparision
      visitPartitionComparision(MySqlParser.PartitionComparisionContext ctx) {
    if (ctx == null) {
      return null;
    }

    Uid uid = this.visitUid(ctx.uid());
    List<PartitionDefinerAtom> partitionDefinerAtoms = Lists.newArrayList();
    for (MySqlParser.PartitionDefinerAtomContext partitionDefinerAtomCtx : ctx
        .partitionDefinerAtom()) {
      partitionDefinerAtoms.add(this.visitPartitionDefinerAtom(partitionDefinerAtomCtx));
    }
    List<PartitionOption> partitionOptions = Lists.newArrayList();
    if (CollectionUtils.isNotEmpty(ctx.partitionOption())) {
      for (MySqlParser.PartitionOptionContext partitionOptionCtx : ctx.partitionOption()) {
        partitionOptions.add(this.visitPartitionOption(partitionOptionCtx));
      }
    }
    List<SubpartitionDefinition> subpartitionDefinitions = Lists.newArrayList();
    if (CollectionUtils.isNotEmpty(ctx.subpartitionDefinition())) {
      for (MySqlParser.SubpartitionDefinitionContext subpartitionDefinitionCtx : ctx
          .subpartitionDefinition()) {
        subpartitionDefinitions.add(this.visitSubpartitionDefinition(subpartitionDefinitionCtx));
      }
    }

    return RelationalAlgebraExpressionFactory.makePartitionComparision(uid, partitionDefinerAtoms,
      partitionOptions, subpartitionDefinitions);
  }

  @Override
  public PartitionListAtom visitPartitionListAtom(MySqlParser.PartitionListAtomContext ctx) {
    if (ctx == null) {
      return null;
    }

    Uid uid = this.visitUid(ctx.uid());
    List<PartitionDefinerAtom> partitionDefinerAtoms = Lists.newArrayList();
    for (MySqlParser.PartitionDefinerAtomContext partitionDefinerAtomCtx : ctx
        .partitionDefinerAtom()) {
      partitionDefinerAtoms.add(this.visitPartitionDefinerAtom(partitionDefinerAtomCtx));
    }
    List<PartitionOption> partitionOptions = Lists.newArrayList();
    if (CollectionUtils.isNotEmpty(ctx.partitionOption())) {
      for (MySqlParser.PartitionOptionContext partitionOptionCtx : ctx.partitionOption()) {
        partitionOptions.add(this.visitPartitionOption(partitionOptionCtx));
      }
    }
    List<SubpartitionDefinition> subpartitionDefinitions = Lists.newArrayList();
    if (CollectionUtils.isNotEmpty(ctx.subpartitionDefinition())) {
      for (MySqlParser.SubpartitionDefinitionContext subpartitionDefinitionCtx : ctx
          .subpartitionDefinition()) {
        subpartitionDefinitions.add(this.visitSubpartitionDefinition(subpartitionDefinitionCtx));
      }
    }

    return RelationalAlgebraExpressionFactory.makePartitionListAtom(uid, partitionDefinerAtoms,
      partitionOptions, subpartitionDefinitions);
  }

  @Override
  public PartitionListVector visitPartitionListVector(MySqlParser.PartitionListVectorContext ctx) {
    if (ctx == null) {
      return null;
    }

    Uid uid = this.visitUid(ctx.uid());
    List<PartitionDefinerVector> partitionDefinerVectors = Lists.newArrayList();
    for (MySqlParser.PartitionDefinerVectorContext partitionDefinerVectorCtx : ctx
        .partitionDefinerVector()) {
      partitionDefinerVectors.add(this.visitPartitionDefinerVector(partitionDefinerVectorCtx));
    }
    List<PartitionOption> partitionOptions = Lists.newArrayList();
    if (CollectionUtils.isNotEmpty(ctx.partitionOption())) {
      for (MySqlParser.PartitionOptionContext partitionOptionCtx : ctx.partitionOption()) {
        partitionOptions.add(this.visitPartitionOption(partitionOptionCtx));
      }
    }
    List<SubpartitionDefinition> subpartitionDefinitions = Lists.newArrayList();
    if (CollectionUtils.isNotEmpty(ctx.subpartitionDefinition())) {
      for (MySqlParser.SubpartitionDefinitionContext subpartitionDefinitionCtx : ctx
          .subpartitionDefinition()) {
        subpartitionDefinitions.add(this.visitSubpartitionDefinition(subpartitionDefinitionCtx));
      }
    }

    return RelationalAlgebraExpressionFactory.makePartitionListVector(uid, partitionDefinerVectors,
      partitionOptions, subpartitionDefinitions);
  }

  @Override
  public PartitionSimple visitPartitionSimple(MySqlParser.PartitionSimpleContext ctx) {
    if (ctx == null) {
      return null;
    }

    Uid uid = this.visitUid(ctx.uid());
    List<PartitionOption> partitionOptions = Lists.newArrayList();
    if (CollectionUtils.isNotEmpty(ctx.partitionOption())) {
      for (MySqlParser.PartitionOptionContext partitionOptionCtx : ctx.partitionOption()) {
        partitionOptions.add(this.visitPartitionOption(partitionOptionCtx));
      }
    }
    List<SubpartitionDefinition> subpartitionDefinitions = Lists.newArrayList();
    if (CollectionUtils.isNotEmpty(ctx.subpartitionDefinition())) {
      for (MySqlParser.SubpartitionDefinitionContext subpartitionDefinitionCtx : ctx
          .subpartitionDefinition()) {
        subpartitionDefinitions.add(this.visitSubpartitionDefinition(subpartitionDefinitionCtx));
      }
    }

    return RelationalAlgebraExpressionFactory.makePartitionSimple(uid, partitionOptions,
      subpartitionDefinitions);
  }

  @Override
  public PartitionDefinerAtom
      visitPartitionDefinerAtom(MySqlParser.PartitionDefinerAtomContext ctx) {
    if (ctx == null) {
      return null;
    }

    PartitionDefinerAtom.Type type;

    Constant constant = null;
    Expression expression = null;
    if (ctx.constant() != null) {
      type = PartitionDefinerAtom.Type.CONSTANT;
      constant = this.visitConstant(ctx.constant());
    } else if (ctx.expression() != null) {
      type = PartitionDefinerAtom.Type.EXPRESSION;
      expression = this.visitExpression(ctx.expression());
    } else if (ctx.MAXVALUE() != null) {
      type = PartitionDefinerAtom.Type.MAXVALUE;
    } else {
      throw ParserError.make(ctx);
    }

    return RelationalAlgebraExpressionFactory.makePartitionDefinerAtom(type, constant, expression);
  }

  @Override
  public PartitionDefinerVector
      visitPartitionDefinerVector(MySqlParser.PartitionDefinerVectorContext ctx) {
    if (ctx == null) {
      return null;
    }

    List<PartitionDefinerAtom> partitionDefinerAtoms = Lists.newArrayList();
    for (MySqlParser.PartitionDefinerAtomContext partitionDefinerAtomCtx : ctx
        .partitionDefinerAtom()) {
      partitionDefinerAtoms.add(this.visitPartitionDefinerAtom(partitionDefinerAtomCtx));
    }
    return RelationalAlgebraExpressionFactory.makePartitionDefinerVector(partitionDefinerAtoms);
  }

  @Override
  public SubpartitionDefinition
      visitSubpartitionDefinition(MySqlParser.SubpartitionDefinitionContext ctx) {
    if (ctx == null) {
      return null;
    }

    Uid uid = this.visitUid(ctx.uid());
    List<PartitionOption> partitionOptions = Lists.newArrayList();
    if (CollectionUtils.isNotEmpty(ctx.partitionOption())) {
      for (MySqlParser.PartitionOptionContext partitionOptionCtx : ctx.partitionOption()) {
        partitionOptions.add(this.visitPartitionOption(partitionOptionCtx));
      }
    }

    return RelationalAlgebraExpressionFactory.makeSubpartitionDefinition(uid, partitionOptions);
  }

  public PartitionOption visitPartitionOption(MySqlParser.PartitionOptionContext ctx) {
    if (ctx == null) {
      return null;
    }

    if (ctx instanceof MySqlParser.PartitionOptionEngineContext) {
      return this.visitPartitionOptionEngine((MySqlParser.PartitionOptionEngineContext) ctx);
    } else if (ctx instanceof MySqlParser.PartitionOptionCommentContext) {
      return this.visitPartitionOptionComment((MySqlParser.PartitionOptionCommentContext) ctx);
    } else if (ctx instanceof MySqlParser.PartitionOptionDataDirectoryContext) {
      return this
          .visitPartitionOptionDataDirectory((MySqlParser.PartitionOptionDataDirectoryContext) ctx);
    } else if (ctx instanceof MySqlParser.PartitionOptionIndexDirectoryContext) {
      return this.visitPartitionOptionIndexDirectory(
        (MySqlParser.PartitionOptionIndexDirectoryContext) ctx);
    } else if (ctx instanceof MySqlParser.PartitionOptionMaxRowsContext) {
      return this.visitPartitionOptionMaxRows((MySqlParser.PartitionOptionMaxRowsContext) ctx);
    } else if (ctx instanceof MySqlParser.PartitionOptionMinRowsContext) {
      return this.visitPartitionOptionMinRows((MySqlParser.PartitionOptionMinRowsContext) ctx);
    } else if (ctx instanceof MySqlParser.PartitionOptionTablespaceContext) {
      return this
          .visitPartitionOptionTablespace((MySqlParser.PartitionOptionTablespaceContext) ctx);
    } else if (ctx instanceof MySqlParser.PartitionOptionNodeGroupContext) {
      return this.visitPartitionOptionNodeGroup((MySqlParser.PartitionOptionNodeGroupContext) ctx);
    } else {
      throw ParserError.make(ctx);
    }
  }

  @Override
  public PartitionOptionEngine
      visitPartitionOptionEngine(MySqlParser.PartitionOptionEngineContext ctx) {
    if (ctx == null) {
      return null;
    }

    EngineName engineName = this.visitEngineName(ctx.engineName());
    return RelationalAlgebraExpressionFactory.makePartitionOptionEngine(engineName);
  }

  @Override
  public PartitionOptionComment
      visitPartitionOptionComment(MySqlParser.PartitionOptionCommentContext ctx) {
    if (ctx == null) {
      return null;
    }

    String comment = ctx.STRING_LITERAL().getText();
    return RelationalAlgebraExpressionFactory.makePartitionOptionComment(comment);
  }

  @Override
  public PartitionOptionDataDirectory
      visitPartitionOptionDataDirectory(MySqlParser.PartitionOptionDataDirectoryContext ctx) {
    if (ctx == null) {
      return null;
    }

    String dataDirectory = ctx.STRING_LITERAL().getText();
    return RelationalAlgebraExpressionFactory.makePartitionOptionDataDirectory(dataDirectory);
  }

  @Override
  public PartitionOptionIndexDirectory
      visitPartitionOptionIndexDirectory(MySqlParser.PartitionOptionIndexDirectoryContext ctx) {
    if (ctx == null) {
      return null;
    }

    String indexDirectory = ctx.STRING_LITERAL().getText();
    return RelationalAlgebraExpressionFactory.makePartitionOptionIndexDirectory(indexDirectory);
  }

  @Override
  public PartitionOptionMaxRows
      visitPartitionOptionMaxRows(MySqlParser.PartitionOptionMaxRowsContext ctx) {
    if (ctx == null) {
      return null;
    }

    DecimalLiteral maxRows = this.visitDecimalLiteral(ctx.maxRows);
    return RelationalAlgebraExpressionFactory.makePartitionOptionMaxRows(maxRows);
  }

  @Override
  public PartitionOptionMinRows
      visitPartitionOptionMinRows(MySqlParser.PartitionOptionMinRowsContext ctx) {
    if (ctx == null) {
      return null;
    }

    DecimalLiteral minRows = this.visitDecimalLiteral(ctx.minRows);
    return RelationalAlgebraExpressionFactory.makePartitionOptionMinRows(minRows);
  }

  @Override
  public PartitionOptionTablespace
      visitPartitionOptionTablespace(MySqlParser.PartitionOptionTablespaceContext ctx) {
    if (ctx == null) {
      return null;
    }

    Uid tablespace = this.visitUid(ctx.tablespace);
    return RelationalAlgebraExpressionFactory.makePartitionOptionTablespace(tablespace);
  }

  @Override
  public PartitionOptionNodeGroup
      visitPartitionOptionNodeGroup(MySqlParser.PartitionOptionNodeGroupContext ctx) {
    if (ctx == null) {
      return null;
    }

    Uid nodegroup = this.visitUid(ctx.nodegroup);
    return RelationalAlgebraExpressionFactory.makePartitionOptionNodeGroup(nodegroup);
  }

  public AlterDatabase visitAlterDatabase(MySqlParser.AlterDatabaseContext ctx) {
    if (ctx == null) {
      return null;
    }

    if (ctx instanceof MySqlParser.AlterSimpleDatabaseContext) {
      return this.visitAlterSimpleDatabase((MySqlParser.AlterSimpleDatabaseContext) ctx);
    } else if (ctx instanceof MySqlParser.AlterUpgradeNameContext) {
      return this.visitAlterUpgradeName((MySqlParser.AlterUpgradeNameContext) ctx);
    } else {
      throw ParserError.make(ctx);
    }
  }

  @Override
  public AlterSimpleDatabase visitAlterSimpleDatabase(MySqlParser.AlterSimpleDatabaseContext ctx) {
    if (ctx == null) {
      return null;
    }

    DdlStatement.DbFormatEnum dbFormat =
        DdlStatement.DbFormatEnum.valueOf(ctx.dbFormat.getText().toUpperCase());
    Uid uid = this.visitUid(ctx.uid());
    List<CreateDatabaseOption> createDatabaseOptions = Lists.newArrayList();
    for (MySqlParser.CreateDatabaseOptionContext createDatabaseOptionCtx : ctx
        .createDatabaseOption()) {
      createDatabaseOptions.add(this.visitCreateDatabaseOption(createDatabaseOptionCtx));
    }
    return RelationalAlgebraExpressionFactory.makeAlterSimpleDatabase(dbFormat, uid,
      createDatabaseOptions);
  }

  @Override
  public AlterUpgradeName visitAlterUpgradeName(MySqlParser.AlterUpgradeNameContext ctx) {
    if (ctx == null) {
      return null;
    }

    DdlStatement.DbFormatEnum dbFormat =
        DdlStatement.DbFormatEnum.valueOf(ctx.dbFormat.getText().toUpperCase());
    Uid uid = this.visitUid(ctx.uid());
    return RelationalAlgebraExpressionFactory.makeAlterUpgradeName(dbFormat, uid);
  }

  @Override
  public AlterEvent visitAlterEvent(MySqlParser.AlterEventContext ctx) {
    if (ctx == null) {
      return null;
    }

    OwnerStatement ownerStatement = this.visitOwnerStatement(ctx.ownerStatement());
    FullId fullId = this.visitFullId(ctx.fullId(0));
    ScheduleExpression scheduleExpression = this.visitScheduleExpression(ctx.scheduleExpression());
    Boolean notPreserve = null;
    if (ctx.PRESERVE() != null) {
      if (ctx.NOT() != null) {
        notPreserve = Boolean.TRUE;
      } else {
        notPreserve = Boolean.FALSE;
      }
    }
    FullId renameToFullId = null;
    if (ctx.fullId().size() > 1) {
      renameToFullId = this.visitFullId(ctx.fullId(1));
    }
    DdlStatement.EnableTypeEnum enableType = this.visitEnableType(ctx.enableType());
    String comment = null;
    if (ctx.STRING_LITERAL() != null) {
      comment = ctx.STRING_LITERAL().getText();
    }
    RoutineBody routineBody = this.visitRoutineBody(ctx.routineBody());
    return RelationalAlgebraExpressionFactory.makeAlterEvent(ownerStatement, fullId,
      scheduleExpression, notPreserve, renameToFullId, enableType, comment, routineBody);
  }

  @Override
  public AlterFunction visitAlterFunction(MySqlParser.AlterFunctionContext ctx) {
    if (ctx == null) {
      return null;
    }

    FullId fullId = this.visitFullId(ctx.fullId());
    List<RoutineOption> routineOptions = Lists.newArrayList();
    if (CollectionUtils.isNotEmpty(ctx.routineOption())) {
      for (MySqlParser.RoutineOptionContext routineOptionCtx : ctx.routineOption()) {
        routineOptions.add(this.visitRoutineOption(routineOptionCtx));
      }
    }
    return RelationalAlgebraExpressionFactory.makeAlterFunction(fullId, routineOptions);
  }

  @Override
  public AlterInstance visitAlterInstance(MySqlParser.AlterInstanceContext ctx) {
    if (ctx == null) {
      return null;
    }
    return RelationalAlgebraExpressionFactory.makeAlterInstance();
  }

  @Override
  public AlterLogfileGroup visitAlterLogfileGroup(MySqlParser.AlterLogfileGroupContext ctx) {
    if (ctx == null) {
      return null;
    }

    Uid uid = this.visitUid(ctx.uid());
    String undoFile = ctx.STRING_LITERAL().getText();
    FileSizeLiteral fileSizeLiteral = this.visitFileSizeLiteral(ctx.fileSizeLiteral());
    Boolean wait = null;
    if (ctx.WAIT() != null) {
      wait = Boolean.TRUE;
    }
    EngineName engineName = this.visitEngineName(ctx.engineName());

    return RelationalAlgebraExpressionFactory.makeAlterLogfileGroup(uid, undoFile, fileSizeLiteral,
      wait, engineName);
  }

  @Override
  public AlterProcedure visitAlterProcedure(MySqlParser.AlterProcedureContext ctx) {
    if (ctx == null) {
      return null;
    }

    FullId fullId = this.visitFullId(ctx.fullId());
    List<RoutineOption> routineOptions = Lists.newArrayList();
    if (CollectionUtils.isNotEmpty(ctx.routineOption())) {
      for (MySqlParser.RoutineOptionContext routineOptionCtx : ctx.routineOption()) {
        routineOptions.add(this.visitRoutineOption(routineOptionCtx));
      }
    }

    return RelationalAlgebraExpressionFactory.makeAlterProcedure(fullId, routineOptions);
  }

  @Override
  public AlterServer visitAlterServer(MySqlParser.AlterServerContext ctx) {
    if (ctx == null) {
      return null;
    }

    Uid uid = this.visitUid(ctx.uid());
    List<ServerOption> serverOptions = Lists.newArrayList();
    for (MySqlParser.ServerOptionContext serverOptionCtx : ctx.serverOption()) {
      serverOptions.add(this.visitServerOption(serverOptionCtx));
    }
    return RelationalAlgebraExpressionFactory.makeAlterServer(uid, serverOptions);
  }

  @Override
  public AlterTable visitAlterTable(MySqlParser.AlterTableContext ctx) {
    if (ctx == null) {
      return null;
    }

    DdlStatement.IntimeActionEnum intimeAction = null;
    if (ctx.intimeAction != null) {
      intimeAction =
          DdlStatement.IntimeActionEnum.valueOf(ctx.intimeAction.getText().toUpperCase());
    }
    Boolean ignore = null;
    if (ctx.IGNORE() != null) {
      ignore = Boolean.TRUE;
    }
    TableName tableName = this.visitTableName(ctx.tableName());
    List<AlterSpecification> alterSpecifications = Lists.newArrayList();
    if (CollectionUtils.isNotEmpty(ctx.alterSpecification())) {
      for (MySqlParser.AlterSpecificationContext alterSpecificationCtx : ctx.alterSpecification()) {
        alterSpecifications.add(this.visitAlterSpecification(alterSpecificationCtx));
      }
    }
    PartitionDefinitions partitionDefinitions =
        this.visitPartitionDefinitions(ctx.partitionDefinitions());

    return RelationalAlgebraExpressionFactory.makeAlterTable(intimeAction, ignore, tableName,
      alterSpecifications, partitionDefinitions);
  }

  @Override
  public AlterTablespace visitAlterTablespace(MySqlParser.AlterTablespaceContext ctx) {
    if (ctx == null) {
      return null;
    }

    Uid uid = this.visitUid(ctx.uid());
    AlterTablespace.ObjectActionEnum objectAction =
        AlterTablespace.ObjectActionEnum.valueOf(ctx.objectAction.getText().toUpperCase());
    String dataFile = ctx.STRING_LITERAL().getText();
    FileSizeLiteral fileSizeLiteral = this.visitFileSizeLiteral(ctx.fileSizeLiteral());
    Boolean wait = null;
    if (ctx.WAIT() != null) {
      wait = Boolean.TRUE;
    }
    EngineName engineName = this.visitEngineName(ctx.engineName());

    return RelationalAlgebraExpressionFactory.makeAlterTablespace(uid, objectAction, dataFile,
      fileSizeLiteral, wait, engineName);
  }

  @Override
  public AlterView visitAlterView(MySqlParser.AlterViewContext ctx) {
    if (ctx == null) {
      return null;
    }

    AlterView.AlgTypeEnum algType = null;
    if (ctx.algType != null) {
      algType = AlterView.AlgTypeEnum.valueOf(ctx.algType.getText().toUpperCase());
    }
    OwnerStatement ownerStatement = this.visitOwnerStatement(ctx.ownerStatement());
    Boolean sqlSecurity = null;
    if (ctx.SECURITY() != null) {
      sqlSecurity = Boolean.TRUE;
    }
    AlterView.SecContextEnum secContext = null;
    if (ctx.secContext != null) {
      secContext = AlterView.SecContextEnum.valueOf(ctx.secContext.getText().toUpperCase());
    }
    FullId fullId = this.visitFullId(ctx.fullId());
    UidList uidList = this.visitUidList(ctx.uidList());
    SelectStatement selectStatement = this.visitSelectStatement(ctx.selectStatement());
    Boolean withCheckOption = null;
    if (ctx.WITH() != null) {
      withCheckOption = Boolean.TRUE;
    }
    AlterView.CheckOptEnum checkOpt = null;
    if (ctx.checkOpt != null) {
      checkOpt = AlterView.CheckOptEnum.valueOf(ctx.checkOpt.getText().toUpperCase());
    }

    return RelationalAlgebraExpressionFactory.makeAlterView(algType, ownerStatement, sqlSecurity,
      secContext, fullId, uidList, selectStatement, withCheckOption, checkOpt);
  }

  public AlterSpecification visitAlterSpecification(MySqlParser.AlterSpecificationContext ctx) {
    if (ctx == null) {
      return null;
    }

    if (ctx instanceof MySqlParser.AlterByTableOptionContext) {
      return this.visitAlterByTableOption((MySqlParser.AlterByTableOptionContext) ctx);
    } else if (ctx instanceof MySqlParser.AlterByAddColumnContext) {
      return this.visitAlterByAddColumn((MySqlParser.AlterByAddColumnContext) ctx);
    } else if (ctx instanceof MySqlParser.AlterByAddColumnsContext) {
      return this.visitAlterByAddColumns((MySqlParser.AlterByAddColumnsContext) ctx);
    } else if (ctx instanceof MySqlParser.AlterByAddIndexContext) {
      return this.visitAlterByAddIndex((MySqlParser.AlterByAddIndexContext) ctx);
    } else if (ctx instanceof MySqlParser.AlterByAddPrimaryKeyContext) {
      return this.visitAlterByAddPrimaryKey((MySqlParser.AlterByAddPrimaryKeyContext) ctx);
    } else if (ctx instanceof MySqlParser.AlterByAddUniqueKeyContext) {
      return this.visitAlterByAddUniqueKey((MySqlParser.AlterByAddUniqueKeyContext) ctx);
    } else if (ctx instanceof MySqlParser.AlterByAddSpecialIndexContext) {
      return this.visitAlterByAddSpecialIndex((MySqlParser.AlterByAddSpecialIndexContext) ctx);
    } else if (ctx instanceof MySqlParser.AlterByAddForeignKeyContext) {
      return this.visitAlterByAddForeignKey((MySqlParser.AlterByAddForeignKeyContext) ctx);
    } else if (ctx instanceof MySqlParser.AlterByAddCheckTableConstraintContext) {
      return this.visitAlterByAddCheckTableConstraint(
        (MySqlParser.AlterByAddCheckTableConstraintContext) ctx);
    } else if (ctx instanceof MySqlParser.AlterBySetAlgorithmContext) {
      return this.visitAlterBySetAlgorithm((MySqlParser.AlterBySetAlgorithmContext) ctx);
    } else if (ctx instanceof MySqlParser.AlterByChangeDefaultContext) {
      return this.visitAlterByChangeDefault((MySqlParser.AlterByChangeDefaultContext) ctx);
    } else if (ctx instanceof MySqlParser.AlterByChangeColumnContext) {
      return this.visitAlterByChangeColumn((MySqlParser.AlterByChangeColumnContext) ctx);
    } else if (ctx instanceof MySqlParser.AlterByRenameColumnContext) {
      return this.visitAlterByRenameColumn((MySqlParser.AlterByRenameColumnContext) ctx);
    } else if (ctx instanceof MySqlParser.AlterByLockContext) {
      return this.visitAlterByLock((MySqlParser.AlterByLockContext) ctx);
    } else if (ctx instanceof MySqlParser.AlterByModifyColumnContext) {
      return this.visitAlterByModifyColumn((MySqlParser.AlterByModifyColumnContext) ctx);
    } else if (ctx instanceof MySqlParser.AlterByDropColumnContext) {
      return this.visitAlterByDropColumn((MySqlParser.AlterByDropColumnContext) ctx);
    } else if (ctx instanceof MySqlParser.AlterByDropPrimaryKeyContext) {
      return this.visitAlterByDropPrimaryKey((MySqlParser.AlterByDropPrimaryKeyContext) ctx);
    } else if (ctx instanceof MySqlParser.AlterByRenameIndexContext) {
      return this.visitAlterByRenameIndex((MySqlParser.AlterByRenameIndexContext) ctx);
    } else if (ctx instanceof MySqlParser.AlterByDropIndexContext) {
      return this.visitAlterByDropIndex((MySqlParser.AlterByDropIndexContext) ctx);
    } else if (ctx instanceof MySqlParser.AlterByDropForeignKeyContext) {
      return this.visitAlterByDropForeignKey((MySqlParser.AlterByDropForeignKeyContext) ctx);
    } else if (ctx instanceof MySqlParser.AlterByDisableKeysContext) {
      return this.visitAlterByDisableKeys((MySqlParser.AlterByDisableKeysContext) ctx);
    } else if (ctx instanceof MySqlParser.AlterByEnableKeysContext) {
      return this.visitAlterByEnableKeys((MySqlParser.AlterByEnableKeysContext) ctx);
    } else if (ctx instanceof MySqlParser.AlterByRenameContext) {
      return this.visitAlterByRename((MySqlParser.AlterByRenameContext) ctx);
    } else if (ctx instanceof MySqlParser.AlterByOrderContext) {
      return this.visitAlterByOrder((MySqlParser.AlterByOrderContext) ctx);
    } else if (ctx instanceof MySqlParser.AlterByConvertCharsetContext) {
      return this.visitAlterByConvertCharset((MySqlParser.AlterByConvertCharsetContext) ctx);
    } else if (ctx instanceof MySqlParser.AlterByDefaultCharsetContext) {
      return this.visitAlterByDefaultCharset((MySqlParser.AlterByDefaultCharsetContext) ctx);
    } else if (ctx instanceof MySqlParser.AlterByDiscardTablespaceContext) {
      return this.visitAlterByDiscardTablespace((MySqlParser.AlterByDiscardTablespaceContext) ctx);
    } else if (ctx instanceof MySqlParser.AlterByImportTablespaceContext) {
      return this.visitAlterByImportTablespace((MySqlParser.AlterByImportTablespaceContext) ctx);
    } else if (ctx instanceof MySqlParser.AlterByForceContext) {
      return this.visitAlterByForce((MySqlParser.AlterByForceContext) ctx);
    } else if (ctx instanceof MySqlParser.AlterByValidateContext) {
      return this.visitAlterByValidate((MySqlParser.AlterByValidateContext) ctx);
    } else if (ctx instanceof MySqlParser.AlterByAddPartitionContext) {
      return this.visitAlterByAddPartition((MySqlParser.AlterByAddPartitionContext) ctx);
    } else if (ctx instanceof MySqlParser.AlterByDropPartitionContext) {
      return this.visitAlterByDropPartition((MySqlParser.AlterByDropPartitionContext) ctx);
    } else if (ctx instanceof MySqlParser.AlterByDiscardPartitionContext) {
      return this.visitAlterByDiscardPartition((MySqlParser.AlterByDiscardPartitionContext) ctx);
    } else if (ctx instanceof MySqlParser.AlterByImportPartitionContext) {
      return this.visitAlterByImportPartition((MySqlParser.AlterByImportPartitionContext) ctx);
    } else if (ctx instanceof MySqlParser.AlterByTruncatePartitionContext) {
      return this.visitAlterByTruncatePartition((MySqlParser.AlterByTruncatePartitionContext) ctx);
    } else if (ctx instanceof MySqlParser.AlterByCoalescePartitionContext) {
      return this.visitAlterByCoalescePartition((MySqlParser.AlterByCoalescePartitionContext) ctx);
    } else if (ctx instanceof MySqlParser.AlterByReorganizePartitionContext) {
      return this
          .visitAlterByReorganizePartition((MySqlParser.AlterByReorganizePartitionContext) ctx);
    } else if (ctx instanceof MySqlParser.AlterByExchangePartitionContext) {
      return this.visitAlterByExchangePartition((MySqlParser.AlterByExchangePartitionContext) ctx);
    } else if (ctx instanceof MySqlParser.AlterByAnalyzePartitionContext) {
      return this.visitAlterByAnalyzePartition((MySqlParser.AlterByAnalyzePartitionContext) ctx);
    } else if (ctx instanceof MySqlParser.AlterByCheckPartitionContext) {
      return this.visitAlterByCheckPartition((MySqlParser.AlterByCheckPartitionContext) ctx);
    } else if (ctx instanceof MySqlParser.AlterByOptimizePartitionContext) {
      return this.visitAlterByOptimizePartition((MySqlParser.AlterByOptimizePartitionContext) ctx);
    } else if (ctx instanceof MySqlParser.AlterByRebuildPartitionContext) {
      return this.visitAlterByRebuildPartition((MySqlParser.AlterByRebuildPartitionContext) ctx);
    } else if (ctx instanceof MySqlParser.AlterByRepairPartitionContext) {
      return this.visitAlterByRepairPartition((MySqlParser.AlterByRepairPartitionContext) ctx);
    } else if (ctx instanceof MySqlParser.AlterByRemovePartitioningContext) {
      return this
          .visitAlterByRemovePartitioning((MySqlParser.AlterByRemovePartitioningContext) ctx);
    } else if (ctx instanceof MySqlParser.AlterByUpgradePartitioningContext) {
      return this
          .visitAlterByUpgradePartitioning((MySqlParser.AlterByUpgradePartitioningContext) ctx);
    } else {
      throw ParserError.make(ctx);
    }
  }

  @Override
  public AlterByTableOption visitAlterByTableOption(MySqlParser.AlterByTableOptionContext ctx) {
    if (ctx == null) {
      return null;
    }

    List<TableOption> tableOptions = Lists.newArrayList();
    for (MySqlParser.TableOptionContext tableOptionCtx : ctx.tableOption()) {
      tableOptions.add(this.visitTableOption(tableOptionCtx));
    }
    return RelationalAlgebraExpressionFactory.makeAlterByTableOption(tableOptions);
  }

  @Override
  public AlterByAddColumn visitAlterByAddColumn(MySqlParser.AlterByAddColumnContext ctx) {
    if (ctx == null) {
      return null;
    }

    Uid uid = this.visitUid(ctx.uid(0));
    ColumnDefinition columnDefinition = this.visitColumnDefinition(ctx.columnDefinition());
    Boolean first = null;
    if (ctx.FIRST() != null) {
      first = Boolean.TRUE;
    }
    Uid afterUid = null;
    if (ctx.uid().size() == 2) {
      afterUid = this.visitUid(ctx.uid(1));
    }

    return RelationalAlgebraExpressionFactory.makeAlterByAddColumn(uid, columnDefinition, first,
      afterUid);
  }

  @Override
  public AlterByAddColumns visitAlterByAddColumns(MySqlParser.AlterByAddColumnsContext ctx) {
    if (ctx == null) {
      return null;
    }

    List<Uid> uids = Lists.newArrayList();
    for (MySqlParser.UidContext uidCtx : ctx.uid()) {
      uids.add(this.visitUid(uidCtx));
    }
    List<ColumnDefinition> columnDefinitions = Lists.newArrayList();
    for (MySqlParser.ColumnDefinitionContext columnDefinitionCtx : ctx.columnDefinition()) {
      columnDefinitions.add(this.visitColumnDefinition(columnDefinitionCtx));
    }

    return RelationalAlgebraExpressionFactory.makeAlterByAddColumns(uids, columnDefinitions);
  }

  @Override
  public AlterByAddIndex visitAlterByAddIndex(MySqlParser.AlterByAddIndexContext ctx) {
    if (ctx == null) {
      return null;
    }

    DdlStatement.IndexFormatEnum indexFormat =
        DdlStatement.IndexFormatEnum.valueOf(ctx.indexFormat.getText().toUpperCase());
    Uid uid = this.visitUid(ctx.uid());
    DdlStatement.IndexTypeEnum indexType = this.visitIndexType(ctx.indexType());
    IndexColumnNames indexColumnNames = this.visitIndexColumnNames(ctx.indexColumnNames());
    List<IndexOption> indexOptions = Lists.newArrayList();
    if (CollectionUtils.isNotEmpty(ctx.indexOption())) {
      for (MySqlParser.IndexOptionContext indexOptionCtx : ctx.indexOption()) {
        indexOptions.add(this.visitIndexOption(indexOptionCtx));
      }
    }

    return RelationalAlgebraExpressionFactory.makeAlterByAddIndex(indexFormat, uid, indexType,
      indexColumnNames, indexOptions);
  }

  @Override
  public AlterByAddPrimaryKey
      visitAlterByAddPrimaryKey(MySqlParser.AlterByAddPrimaryKeyContext ctx) {
    if (ctx == null) {
      return null;
    }

    Boolean constraint = null;
    if (ctx.CONSTRAINT() != null) {
      constraint = Boolean.TRUE;
    }
    Uid name = this.visitUid(ctx.name);
    DdlStatement.IndexTypeEnum indexType = this.visitIndexType(ctx.indexType());
    IndexColumnNames indexColumnNames = this.visitIndexColumnNames(ctx.indexColumnNames());
    List<IndexOption> indexOptions = Lists.newArrayList();
    if (CollectionUtils.isNotEmpty(ctx.indexOption())) {
      for (MySqlParser.IndexOptionContext indexOptionCtx : ctx.indexOption()) {
        indexOptions.add(this.visitIndexOption(indexOptionCtx));
      }
    }

    return RelationalAlgebraExpressionFactory.makeAlterByAddPrimaryKey(constraint, name, indexType,
      indexColumnNames, indexOptions);
  }

  @Override
  public AlterByAddUniqueKey visitAlterByAddUniqueKey(MySqlParser.AlterByAddUniqueKeyContext ctx) {
    if (ctx == null) {
      return null;
    }

    Boolean constraint = null;
    if (ctx.CONSTRAINT() != null) {
      constraint = Boolean.TRUE;
    }
    Uid name = this.visitUid(ctx.name);
    DdlStatement.IndexFormatEnum indexFormat = null;
    if (ctx.indexFormat != null) {
      indexFormat = DdlStatement.IndexFormatEnum.valueOf(ctx.indexFormat.getText().toUpperCase());
    }
    Uid indexName = this.visitUid(ctx.indexName);
    DdlStatement.IndexTypeEnum indexType = this.visitIndexType(ctx.indexType());
    IndexColumnNames indexColumnNames = this.visitIndexColumnNames(ctx.indexColumnNames());
    List<IndexOption> indexOptions = Lists.newArrayList();
    if (CollectionUtils.isNotEmpty(ctx.indexOption())) {
      for (MySqlParser.IndexOptionContext indexOptionCtx : ctx.indexOption()) {
        indexOptions.add(this.visitIndexOption(indexOptionCtx));
      }
    }

    return RelationalAlgebraExpressionFactory.makeAlterByAddUniqueKey(constraint, name, indexFormat,
      indexName, indexType, indexColumnNames, indexOptions);
  }

  @Override
  public AlterByAddSpecialIndex
      visitAlterByAddSpecialIndex(MySqlParser.AlterByAddSpecialIndexContext ctx) {
    if (ctx == null) {
      return null;
    }

    AlterByAddSpecialIndex.KeyTypeEnum keyType =
        AlterByAddSpecialIndex.KeyTypeEnum.valueOf(ctx.keyType.getText().toUpperCase());
    DdlStatement.IndexFormatEnum indexFormat = null;
    if (ctx.indexFormat != null) {
      indexFormat = DdlStatement.IndexFormatEnum.valueOf(ctx.indexFormat.getText().toUpperCase());
    }
    Uid uid = this.visitUid(ctx.uid());
    IndexColumnNames indexColumnNames = this.visitIndexColumnNames(ctx.indexColumnNames());
    List<IndexOption> indexOptions = Lists.newArrayList();
    if (CollectionUtils.isNotEmpty(ctx.indexOption())) {
      for (MySqlParser.IndexOptionContext indexOptionCtx : ctx.indexOption()) {
        indexOptions.add(this.visitIndexOption(indexOptionCtx));
      }
    }

    return RelationalAlgebraExpressionFactory.makeAlterByAddSpecialIndex(keyType, indexFormat, uid,
      indexColumnNames, indexOptions);
  }

  @Override
  public AlterByAddForeignKey
      visitAlterByAddForeignKey(MySqlParser.AlterByAddForeignKeyContext ctx) {
    if (ctx == null) {
      return null;
    }

    Boolean constraint = null;
    if (ctx.CONSTRAINT() != null) {
      constraint = Boolean.TRUE;
    }
    Uid name = this.visitUid(ctx.name);
    Uid indexName = this.visitUid(ctx.indexName);
    IndexColumnNames indexColumnNames = this.visitIndexColumnNames(ctx.indexColumnNames());
    ReferenceDefinition referenceDefinition =
        this.visitReferenceDefinition(ctx.referenceDefinition());

    return RelationalAlgebraExpressionFactory.makeAlterByAddForeignKey(constraint, name, indexName,
      indexColumnNames, referenceDefinition);
  }

  @Override
  public AlterByAddCheckTableConstraint
      visitAlterByAddCheckTableConstraint(MySqlParser.AlterByAddCheckTableConstraintContext ctx) {
    if (ctx == null) {
      return null;
    }

    Boolean constraint = null;
    if (ctx.CONSTRAINT() != null) {
      constraint = Boolean.TRUE;
    }
    Uid name = this.visitUid(ctx.name);
    Expression expression = this.visitExpression(ctx.expression());
    return RelationalAlgebraExpressionFactory.makeAlterByAddCheckTableConstraint(constraint, name,
      expression);
  }

  @Override
  public AlterBySetAlgorithm visitAlterBySetAlgorithm(MySqlParser.AlterBySetAlgorithmContext ctx) {
    if (ctx == null) {
      return null;
    }

    AlterBySetAlgorithm.AlgTypeEnum algType =
        AlterBySetAlgorithm.AlgTypeEnum.valueOf(ctx.algType.getText().toUpperCase());
    return RelationalAlgebraExpressionFactory.makeAlterBySetAlgorithm(algType);
  }

  @Override
  public AlterByChangeDefault
      visitAlterByChangeDefault(MySqlParser.AlterByChangeDefaultContext ctx) {
    if (ctx == null) {
      return null;
    }

    Boolean column = null;
    if (ctx.COLUMN() != null) {
      column = Boolean.TRUE;
    }
    Uid uid = this.visitUid(ctx.uid());
    AlterByChangeDefault.Type type = null;
    DefaultValue defaultValue = null;
    if (ctx.SET() != null) {
      type = AlterByChangeDefault.Type.SET_DEFAULT;
      defaultValue = this.visitDefaultValue(ctx.defaultValue());
    } else if (ctx.DROP() != null) {
      type = AlterByChangeDefault.Type.DROP_DEFAULT;
    }

    return RelationalAlgebraExpressionFactory.makeAlterByChangeDefault(column, uid, type,
      defaultValue);
  }

  @Override
  public AlterByChangeColumn visitAlterByChangeColumn(MySqlParser.AlterByChangeColumnContext ctx) {
    if (ctx == null) {
      return null;
    }

    Uid oldColumn = this.visitUid(ctx.oldColumn);
    Uid newColumn = this.visitUid(ctx.newColumn);
    ColumnDefinition columnDefinition = this.visitColumnDefinition(ctx.columnDefinition());
    Boolean first = null;
    if (ctx.FIRST() != null) {
      first = Boolean.TRUE;
    }
    Uid afterColumn = this.visitUid(ctx.afterColumn);

    return RelationalAlgebraExpressionFactory.makeAlterByChangeColumn(oldColumn, newColumn,
      columnDefinition, first, afterColumn);
  }

  @Override
  public AlterByRenameColumn visitAlterByRenameColumn(MySqlParser.AlterByRenameColumnContext ctx) {
    if (ctx == null) {
      return null;
    }

    Uid oldColumn = this.visitUid(ctx.oldColumn);
    Uid newColumn = this.visitUid(ctx.newColumn);
    return RelationalAlgebraExpressionFactory.makeAlterByRenameColumn(oldColumn, newColumn);
  }

  @Override
  public AlterByLock visitAlterByLock(MySqlParser.AlterByLockContext ctx) {
    if (ctx == null) {
      return null;
    }

    AlterByLock.LockTypeEnum lockType =
        AlterByLock.LockTypeEnum.valueOf(ctx.lockType.getText().toUpperCase());
    return RelationalAlgebraExpressionFactory.makeAlterByLock(lockType);
  }

  @Override
  public AlterByModifyColumn visitAlterByModifyColumn(MySqlParser.AlterByModifyColumnContext ctx) {
    if (ctx == null) {
      return null;
    }

    Uid uid = this.visitUid(ctx.uid(0));

    ColumnDefinition columnDefinition = this.visitColumnDefinition(ctx.columnDefinition());
    Boolean first = null;
    if (ctx.FIRST() != null) {
      first = Boolean.TRUE;
    }
    Uid afterUid = null;
    if (ctx.uid().size() == 2) {
      afterUid = this.visitUid(ctx.uid(1));
    }

    return RelationalAlgebraExpressionFactory.makeAlterByModifyColumn(uid, columnDefinition, first,
      afterUid);
  }

  @Override
  public AlterByDropColumn visitAlterByDropColumn(MySqlParser.AlterByDropColumnContext ctx) {
    if (ctx == null) {
      return null;
    }

    Boolean column = null;
    if (ctx.COLUMN() != null) {
      column = Boolean.TRUE;
    }
    Uid uid = this.visitUid(ctx.uid());
    Boolean restrict = null;
    if (ctx.RESTRICT() != null) {
      restrict = Boolean.TRUE;
    }

    return RelationalAlgebraExpressionFactory.makeAlterByDropColumn(column, uid, restrict);
  }

  @Override
  public AlterByDropPrimaryKey
      visitAlterByDropPrimaryKey(MySqlParser.AlterByDropPrimaryKeyContext ctx) {
    if (ctx == null) {
      return null;
    }

    return RelationalAlgebraExpressionFactory.makeAlterByDropPrimaryKey();
  }

  @Override
  public AlterByRenameIndex visitAlterByRenameIndex(MySqlParser.AlterByRenameIndexContext ctx) {
    if (ctx == null) {
      return null;
    }

    DdlStatement.IndexFormatEnum indexFormat =
        DdlStatement.IndexFormatEnum.valueOf(ctx.indexFormat.getText().toUpperCase());

    List<Uid> uids = Lists.newArrayList();
    for (MySqlParser.UidContext uidCtx : ctx.uid()) {
      uids.add(this.visitUid(uidCtx));
    }
    if (uids.size() != 2) {
      throw ParserError.make(ctx);
    }
    Uid oldUid = uids.get(0);
    Uid newUid = uids.get(1);

    return RelationalAlgebraExpressionFactory.makeAlterByRenameIndex(indexFormat, oldUid, newUid);
  }

  @Override
  public AlterByDropIndex visitAlterByDropIndex(MySqlParser.AlterByDropIndexContext ctx) {
    if (ctx == null) {
      return null;
    }

    DdlStatement.IndexFormatEnum indexFormat =
        DdlStatement.IndexFormatEnum.valueOf(ctx.indexFormat.getText().toUpperCase());
    Uid uid = this.visitUid(ctx.uid());
    return RelationalAlgebraExpressionFactory.makeAlterByDropIndex(indexFormat, uid);
  }

  @Override
  public AlterByDropForeignKey
      visitAlterByDropForeignKey(MySqlParser.AlterByDropForeignKeyContext ctx) {
    if (ctx == null) {
      return null;
    }

    Uid uid = this.visitUid(ctx.uid());
    return RelationalAlgebraExpressionFactory.makeAlterByDropForeignKey(uid);
  }

  @Override
  public AlterByDisableKeys visitAlterByDisableKeys(MySqlParser.AlterByDisableKeysContext ctx) {
    if (ctx == null) {
      return null;
    }
    return RelationalAlgebraExpressionFactory.makeAlterByDisableKeys();
  }

  @Override
  public AlterByEnableKeys visitAlterByEnableKeys(MySqlParser.AlterByEnableKeysContext ctx) {
    if (ctx == null) {
      return null;
    }
    return RelationalAlgebraExpressionFactory.makeAlterByEnableKeys();
  }

  @Override
  public AlterByRename visitAlterByRename(MySqlParser.AlterByRenameContext ctx) {
    if (ctx == null) {
      return null;
    }

    AlterByRename.RenameFormatEnum renameFormat = null;
    if (ctx.renameFormat != null) {
      renameFormat =
          AlterByRename.RenameFormatEnum.valueOf(ctx.renameFormat.getText().toUpperCase());
    }
    Uid uid = this.visitUid(ctx.uid());
    FullId fullId = this.visitFullId(ctx.fullId());

    return RelationalAlgebraExpressionFactory.makeAlterByRename(renameFormat, uid, fullId);
  }

  @Override
  public AlterByOrder visitAlterByOrder(MySqlParser.AlterByOrderContext ctx) {
    if (ctx == null) {
      return null;
    }

    UidList uidList = this.visitUidList(ctx.uidList());
    return RelationalAlgebraExpressionFactory.makeAlterByOrder(uidList);
  }

  @Override
  public AlterByConvertCharset
      visitAlterByConvertCharset(MySqlParser.AlterByConvertCharsetContext ctx) {
    if (ctx == null) {
      return null;
    }

    CharsetName charsetName = this.visitCharsetName(ctx.charsetName());
    CollationName collationName = this.visitCollationName(ctx.collationName());
    return RelationalAlgebraExpressionFactory.makeAlterByConvertCharset(charsetName, collationName);
  }

  @Override
  public AlterByDefaultCharset
      visitAlterByDefaultCharset(MySqlParser.AlterByDefaultCharsetContext ctx) {
    if (ctx == null) {
      return null;
    }

    Boolean isDefault = null;
    if (ctx.DEFAULT() != null) {
      isDefault = Boolean.TRUE;
    }
    CharsetName charsetName = this.visitCharsetName(ctx.charsetName());
    CollationName collationName = this.visitCollationName(ctx.collationName());

    return RelationalAlgebraExpressionFactory.makeAlterByDefaultCharset(isDefault, charsetName,
      collationName);
  }

  @Override
  public AlterByDiscardTablespace
      visitAlterByDiscardTablespace(MySqlParser.AlterByDiscardTablespaceContext ctx) {
    if (ctx == null) {
      return null;
    }
    return RelationalAlgebraExpressionFactory.makeAlterByDiscardTablespace();
  }

  @Override
  public AlterByImportTablespace
      visitAlterByImportTablespace(MySqlParser.AlterByImportTablespaceContext ctx) {
    if (ctx == null) {
      return null;
    }
    return RelationalAlgebraExpressionFactory.makeAlterByImportTablespace();
  }

  @Override
  public AlterByForce visitAlterByForce(MySqlParser.AlterByForceContext ctx) {
    if (ctx == null) {
      return null;
    }
    return RelationalAlgebraExpressionFactory.makeAlterByForce();
  }

  @Override
  public AlterByValidate visitAlterByValidate(MySqlParser.AlterByValidateContext ctx) {
    if (ctx == null) {
      return null;
    }

    AlterByValidate.ValidationFormatEnum validationFormat =
        AlterByValidate.ValidationFormatEnum.valueOf(ctx.validationFormat.getText().toUpperCase());
    return RelationalAlgebraExpressionFactory.makeAlterByValidate(validationFormat);
  }

  @Override
  public AlterByAddPartition visitAlterByAddPartition(MySqlParser.AlterByAddPartitionContext ctx) {
    if (ctx == null) {
      return null;
    }

    List<PartitionDefinition> partitionDefinitions = Lists.newArrayList();
    for (MySqlParser.PartitionDefinitionContext partitionDefinitionCtx : ctx
        .partitionDefinition()) {
      partitionDefinitions.add(this.visitPartitionDefinition(partitionDefinitionCtx));
    }

    return RelationalAlgebraExpressionFactory.makeAlterByAddPartition(partitionDefinitions);
  }

  @Override
  public AlterByDropPartition
      visitAlterByDropPartition(MySqlParser.AlterByDropPartitionContext ctx) {
    if (ctx == null) {
      return null;
    }

    UidList uidList = this.visitUidList(ctx.uidList());
    return RelationalAlgebraExpressionFactory.makeAlterByDropPartition(uidList);
  }

  @Override
  public AlterByDiscardPartition
      visitAlterByDiscardPartition(MySqlParser.AlterByDiscardPartitionContext ctx) {
    if (ctx == null) {
      return null;
    }

    UidList uidList = this.visitUidList(ctx.uidList());
    return RelationalAlgebraExpressionFactory.makeAlterByDiscardPartition(uidList);
  }

  @Override
  public AlterByImportPartition
      visitAlterByImportPartition(MySqlParser.AlterByImportPartitionContext ctx) {
    if (ctx == null) {
      return null;
    }

    UidList uidList = this.visitUidList(ctx.uidList());
    return RelationalAlgebraExpressionFactory.makeAlterByImportPartition(uidList);
  }

  @Override
  public AlterByTruncatePartition
      visitAlterByTruncatePartition(MySqlParser.AlterByTruncatePartitionContext ctx) {
    if (ctx == null) {
      return null;
    }

    UidList uidList = this.visitUidList(ctx.uidList());
    return RelationalAlgebraExpressionFactory.makeAlterByTruncatePartition(uidList);
  }

  @Override
  public AlterByCoalescePartition
      visitAlterByCoalescePartition(MySqlParser.AlterByCoalescePartitionContext ctx) {
    if (ctx == null) {
      return null;
    }

    DecimalLiteral decimalLiteral = this.visitDecimalLiteral(ctx.decimalLiteral());
    return RelationalAlgebraExpressionFactory.makeAlterByCoalescePartition(decimalLiteral);
  }

  @Override
  public AlterByReorganizePartition
      visitAlterByReorganizePartition(MySqlParser.AlterByReorganizePartitionContext ctx) {
    if (ctx == null) {
      return null;
    }

    UidList uidList = this.visitUidList(ctx.uidList());
    List<PartitionDefinition> partitionDefinitions = Lists.newArrayList();
    for (MySqlParser.PartitionDefinitionContext partitionDefinitionCtx : ctx
        .partitionDefinition()) {
      partitionDefinitions.add(this.visitPartitionDefinition(partitionDefinitionCtx));
    }

    return RelationalAlgebraExpressionFactory.makeAlterByReorganizePartition(uidList,
      partitionDefinitions);
  }

  @Override
  public AlterByExchangePartition
      visitAlterByExchangePartition(MySqlParser.AlterByExchangePartitionContext ctx) {
    if (ctx == null) {
      return null;
    }

    Uid uid = this.visitUid(ctx.uid());
    TableName tableName = this.visitTableName(ctx.tableName());
    AlterByExchangePartition.ValidationFormatEnum validationFormat = null;
    if (ctx.validationFormat != null) {
      validationFormat = AlterByExchangePartition.ValidationFormatEnum
          .valueOf(ctx.validationFormat.getText().toUpperCase());
    }
    return RelationalAlgebraExpressionFactory.makeAlterByExchangePartition(uid, tableName,
      validationFormat);
  }

  @Override
  public AlterByAnalyzePartition
      visitAlterByAnalyzePartition(MySqlParser.AlterByAnalyzePartitionContext ctx) {
    if (ctx == null) {
      return null;
    }

    UidList uidList = this.visitUidList(ctx.uidList());
    return RelationalAlgebraExpressionFactory.makeAlterByAnalyzePartition(uidList);
  }

  @Override
  public AlterByCheckPartition
      visitAlterByCheckPartition(MySqlParser.AlterByCheckPartitionContext ctx) {
    if (ctx == null) {
      return null;
    }

    UidList uidList = this.visitUidList(ctx.uidList());
    return RelationalAlgebraExpressionFactory.makeAlterByCheckPartition(uidList);
  }

  @Override
  public AlterByOptimizePartition
      visitAlterByOptimizePartition(MySqlParser.AlterByOptimizePartitionContext ctx) {
    if (ctx == null) {
      return null;
    }

    UidList uidList = this.visitUidList(ctx.uidList());
    return RelationalAlgebraExpressionFactory.makeAlterByOptimizePartition(uidList);
  }

  @Override
  public AlterByRebuildPartition
      visitAlterByRebuildPartition(MySqlParser.AlterByRebuildPartitionContext ctx) {
    if (ctx == null) {
      return null;
    }

    UidList uidList = this.visitUidList(ctx.uidList());
    return RelationalAlgebraExpressionFactory.makeAlterByRebuildPartition(uidList);
  }

  @Override
  public AlterByRepairPartition
      visitAlterByRepairPartition(MySqlParser.AlterByRepairPartitionContext ctx) {
    if (ctx == null) {
      return null;
    }

    UidList uidList = this.visitUidList(ctx.uidList());
    return RelationalAlgebraExpressionFactory.makeAlterByRepairPartition(uidList);
  }

  @Override
  public AlterByRemovePartitioning
      visitAlterByRemovePartitioning(MySqlParser.AlterByRemovePartitioningContext ctx) {
    if (ctx == null) {
      return null;
    }
    return RelationalAlgebraExpressionFactory.makeAlterByRemovePartitioning();
  }

  @Override
  public AlterByUpgradePartitioning
      visitAlterByUpgradePartitioning(MySqlParser.AlterByUpgradePartitioningContext ctx) {
    if (ctx == null) {
      return null;
    }
    return RelationalAlgebraExpressionFactory.makeAlterByUpgradePartitioning();
  }

  @Override
  public DropDatabase visitDropDatabase(MySqlParser.DropDatabaseContext ctx) {
    if (ctx == null) {
      return null;
    }

    DdlStatement.DbFormatEnum dbFormat =
        DdlStatement.DbFormatEnum.valueOf(ctx.dbFormat.getText().toUpperCase());
    IfExists ifExists = this.visitIfExists(ctx.ifExists());
    Uid uid = this.visitUid(ctx.uid());
    return RelationalAlgebraExpressionFactory.makeDropDatabase(dbFormat, ifExists, uid);
  }

  @Override
  public DropEvent visitDropEvent(MySqlParser.DropEventContext ctx) {
    if (ctx == null) {
      return null;
    }

    IfExists ifExists = this.visitIfExists(ctx.ifExists());
    FullId fullId = this.visitFullId(ctx.fullId());
    return RelationalAlgebraExpressionFactory.makeDropEvent(ifExists, fullId);
  }

  @Override
  public DropIndex visitDropIndex(MySqlParser.DropIndexContext ctx) {
    if (ctx == null) {
      return null;
    }

    DdlStatement.IntimeActionEnum intimeAction = null;
    if (ctx.intimeAction != null) {
      intimeAction =
          DdlStatement.IntimeActionEnum.valueOf(ctx.intimeAction.getText().toUpperCase());
    }
    Uid uid = this.visitUid(ctx.uid());
    TableName tableName = this.visitTableName(ctx.tableName());
    List<IndexAlgorithmOrLock> algorithmOrLocks = Lists.newArrayList();
    if (CollectionUtils.isNotEmpty(ctx.DEFAULT())) {
      algorithmOrLocks.add(RelationalAlgebraExpressionFactory
          .makeIndexAlgorithmOrLock(DdlStatement.IndexAlgTypeEnum.DEFAULT, null));
    }
    if (CollectionUtils.isNotEmpty(ctx.INPLACE())) {
      algorithmOrLocks.add(RelationalAlgebraExpressionFactory
          .makeIndexAlgorithmOrLock(DdlStatement.IndexAlgTypeEnum.INPLACE, null));
    }
    if (CollectionUtils.isNotEmpty(ctx.COPY())) {
      algorithmOrLocks.add(RelationalAlgebraExpressionFactory
          .makeIndexAlgorithmOrLock(DdlStatement.IndexAlgTypeEnum.COPY, null));
    }

    if (CollectionUtils.isNotEmpty(ctx.DEFAULT())) {
      algorithmOrLocks.add(RelationalAlgebraExpressionFactory.makeIndexAlgorithmOrLock(null,
        DdlStatement.LockTypeEnum.DEFAULT));
    }
    if (CollectionUtils.isNotEmpty(ctx.NONE())) {
      algorithmOrLocks.add(RelationalAlgebraExpressionFactory.makeIndexAlgorithmOrLock(null,
        DdlStatement.LockTypeEnum.NONE));
    }
    if (CollectionUtils.isNotEmpty(ctx.SHARED())) {
      algorithmOrLocks.add(RelationalAlgebraExpressionFactory.makeIndexAlgorithmOrLock(null,
        DdlStatement.LockTypeEnum.SHARED));
    }
    if (CollectionUtils.isNotEmpty(ctx.EXCLUSIVE())) {
      algorithmOrLocks.add(RelationalAlgebraExpressionFactory.makeIndexAlgorithmOrLock(null,
        DdlStatement.LockTypeEnum.EXCLUSIVE));
    }

    return RelationalAlgebraExpressionFactory.makeDropIndex(intimeAction, uid, tableName,
      algorithmOrLocks);
  }

  @Override
  public DropLogfileGroup visitDropLogfileGroup(MySqlParser.DropLogfileGroupContext ctx) {
    if (ctx == null) {
      return null;
    }

    Uid uid = this.visitUid(ctx.uid());
    EngineName engineName = this.visitEngineName(ctx.engineName());
    return RelationalAlgebraExpressionFactory.makeDropLogfileGroup(uid, engineName);
  }

  @Override
  public DropProcedure visitDropProcedure(MySqlParser.DropProcedureContext ctx) {
    if (ctx == null) {
      return null;
    }

    IfExists ifExists = this.visitIfExists(ctx.ifExists());
    FullId fullId = this.visitFullId(ctx.fullId());
    return RelationalAlgebraExpressionFactory.makeDropProcedure(ifExists, fullId);
  }

  @Override
  public DropFunction visitDropFunction(MySqlParser.DropFunctionContext ctx) {
    if (ctx == null) {
      return null;
    }

    IfExists ifExists = this.visitIfExists(ctx.ifExists());
    FullId fullId = this.visitFullId(ctx.fullId());
    return RelationalAlgebraExpressionFactory.makeDropFunction(ifExists, fullId);
  }

  @Override
  public DropServer visitDropServer(MySqlParser.DropServerContext ctx) {
    if (ctx == null) {
      return null;
    }

    IfExists ifExists = this.visitIfExists(ctx.ifExists());
    Uid uid = this.visitUid(ctx.uid());
    return RelationalAlgebraExpressionFactory.makeDropServer(ifExists, uid);
  }

  @Override
  public DropTable visitDropTable(MySqlParser.DropTableContext ctx) {
    if (ctx == null) {
      return null;
    }

    Boolean temporary = null;
    if (ctx.TEMPORARY() != null) {
      temporary = Boolean.TRUE;
    }
    IfExists ifExists = this.visitIfExists(ctx.ifExists());
    Tables tables = this.visitTables(ctx.tables());
    DdlStatement.DropTypeEnum dropType = null;
    if (ctx.dropType != null) {
      dropType = DdlStatement.DropTypeEnum.valueOf(ctx.dropType.getText().toUpperCase());
    }
    return RelationalAlgebraExpressionFactory.makeDropTable(temporary, ifExists, tables, dropType);
  }

  @Override
  public DropTablespace visitDropTablespace(MySqlParser.DropTablespaceContext ctx) {
    if (ctx == null) {
      return null;
    }

    Uid uid = this.visitUid(ctx.uid());
    EngineName engineName = this.visitEngineName(ctx.engineName());
    return RelationalAlgebraExpressionFactory.makeDropTablespace(uid, engineName);
  }

  @Override
  public DropTrigger visitDropTrigger(MySqlParser.DropTriggerContext ctx) {
    if (ctx == null) {
      return null;
    }

    IfExists ifExists = this.visitIfExists(ctx.ifExists());
    FullId fullId = this.visitFullId(ctx.fullId());
    return RelationalAlgebraExpressionFactory.makeDropTrigger(ifExists, fullId);
  }

  @Override
  public DropView visitDropView(MySqlParser.DropViewContext ctx) {
    if (ctx == null) {
      return null;
    }

    IfExists ifExists = this.visitIfExists(ctx.ifExists());
    List<FullId> fullIds = Lists.newArrayList();
    for (MySqlParser.FullIdContext fullIdCtx : ctx.fullId()) {
      fullIds.add(this.visitFullId(fullIdCtx));
    }
    DdlStatement.DropTypeEnum dropType = null;
    if (ctx.dropType != null) {
      dropType = DdlStatement.DropTypeEnum.valueOf(ctx.dropType.getText().toUpperCase());
    }

    return RelationalAlgebraExpressionFactory.makeDropView(ifExists, fullIds, dropType);
  }

  @Override
  public RenameTable visitRenameTable(MySqlParser.RenameTableContext ctx) {
    if (ctx == null) {
      return null;
    }

    List<RenameTableClause> renameTableClauses = Lists.newArrayList();
    for (MySqlParser.RenameTableClauseContext renameTableClauseCtx : ctx.renameTableClause()) {
      renameTableClauses.add(this.visitRenameTableClause(renameTableClauseCtx));
    }

    return RelationalAlgebraExpressionFactory.makeRenameTable(renameTableClauses);
  }

  @Override
  public RenameTableClause visitRenameTableClause(MySqlParser.RenameTableClauseContext ctx) {
    if (ctx == null) {
      return null;
    }

    List<TableName> tableNames = Lists.newArrayList();
    for (MySqlParser.TableNameContext tableNameCtx : ctx.tableName()) {
      tableNames.add(this.visitTableName(tableNameCtx));
    }
    if (tableNames.size() != 2) {
      throw ParserError.make(ctx);
    }
    TableName before = tableNames.get(0);
    TableName after = tableNames.get(1);
    return RelationalAlgebraExpressionFactory.makeRenameTableClause(before, after);
  }

  @Override
  public TruncateTable visitTruncateTable(MySqlParser.TruncateTableContext ctx) {
    if (ctx == null) {
      return null;
    }

    TableName tableName = this.visitTableName(ctx.tableName());
    return RelationalAlgebraExpressionFactory.makeTruncateTable(tableName);
  }

  @Override
  public CallStatement visitCallStatement(MySqlParser.CallStatementContext ctx) {
    if (ctx == null) {
      return null;
    }

    FullId fullId = this.visitFullId(ctx.fullId());
    Constants constants = this.visitConstants(ctx.constants());
    Expressions expressions = this.visitExpressions(ctx.expressions());

    return RelationalAlgebraExpressionFactory.makeCallStatement(fullId, constants, expressions);
  }

  @Override
  public DeleteStatement visitDeleteStatement(MySqlParser.DeleteStatementContext ctx) {
    if (ctx == null) {
      return null;
    }

    if (ctx.getChild(0) instanceof MySqlParser.SingleDeleteStatementContext) {
      return this
          .visitSingleDeleteStatement((MySqlParser.SingleDeleteStatementContext) ctx.getChild(0));
    } else if (ctx.getChild(0) instanceof MySqlParser.MultipleDeleteStatementContext) {
      return this.visitMultipleDeleteStatement(
        (MySqlParser.MultipleDeleteStatementContext) ctx.getChild(0));
    } else {
      throw ParserError.make(ctx);
    }
  }

  @Override
  public DoStatement visitDoStatement(MySqlParser.DoStatementContext ctx) {
    if (ctx == null) {
      return null;
    }

    Expressions expressions = this.visitExpressions(ctx.expressions());
    return RelationalAlgebraExpressionFactory.makeDoStatement(expressions);
  }

  @Override
  public HandlerStatement visitHandlerStatement(MySqlParser.HandlerStatementContext ctx) {
    if (ctx == null) {
      return null;
    }

    if (ctx.getChild(0) instanceof MySqlParser.HandlerOpenStatementContext) {
      return this
          .visitHandlerOpenStatement((MySqlParser.HandlerOpenStatementContext) ctx.getChild(0));
    } else if (ctx.getChild(0) instanceof MySqlParser.HandlerReadIndexStatementContext) {
      return this.visitHandlerReadIndexStatement(
        (MySqlParser.HandlerReadIndexStatementContext) ctx.getChild(0));
    } else if (ctx.getChild(0) instanceof MySqlParser.HandlerReadStatementContext) {
      return this
          .visitHandlerReadStatement((MySqlParser.HandlerReadStatementContext) ctx.getChild(0));
    } else if (ctx.getChild(0) instanceof MySqlParser.HandlerCloseStatementContext) {
      return this
          .visitHandlerCloseStatement((MySqlParser.HandlerCloseStatementContext) ctx.getChild(0));
    } else {
      throw ParserError.make(ctx);
    }

  }

  @Override
  public InsertStatement visitInsertStatement(MySqlParser.InsertStatementContext ctx) {
    if (ctx == null) {
      return null;
    }

    InsertStatement.PriorityType priority = null;
    if (ctx.priority != null) {
      priority = InsertStatement.PriorityType.valueOf(ctx.priority.getText().toUpperCase());
    }
    Boolean ignore = null;
    if (ctx.IGNORE() != null) {
      ignore = Boolean.TRUE;
    }
    Boolean into = null;
    if (ctx.INTO() != null) {
      into = Boolean.TRUE;
    }
    TableName tableName = this.visitTableName(ctx.tableName());
    UidList partitions = this.visitUidList(ctx.partitions);
    UidList columns = this.visitUidList(ctx.columns);
    InsertStatementValue insertStatementValue =
        this.visitInsertStatementValue(ctx.insertStatementValue());
    List<UpdatedElement> setList = Lists.newArrayList();
    if (ctx.setFirst != null) {
      setList.add(this.visitUpdatedElement(ctx.setFirst));
      if (CollectionUtils.isNotEmpty(ctx.setElements)) {
        for (MySqlParser.UpdatedElementContext updatedElementCtx : ctx.setElements) {
          setList.add(this.visitUpdatedElement(updatedElementCtx));
        }
      }
    }
    List<UpdatedElement> duplicatedList = Lists.newArrayList();
    if (ctx.duplicatedFirst != null) {
      duplicatedList.add(this.visitUpdatedElement(ctx.duplicatedFirst));
      if (CollectionUtils.isNotEmpty(ctx.duplicatedElements)) {
        for (MySqlParser.UpdatedElementContext updatedElementCtx : ctx.duplicatedElements) {
          duplicatedList.add(this.visitUpdatedElement(updatedElementCtx));
        }
      }
    }

    return RelationalAlgebraExpressionFactory.makeInsertStatement(priority, ignore, into, tableName,
      partitions, columns, insertStatementValue, setList, duplicatedList);
  }

  @Override
  public LoadDataStatement visitLoadDataStatement(MySqlParser.LoadDataStatementContext ctx) {
    if (ctx == null) {
      return null;
    }

    DmlStatement.PriorityEnum priority = null;
    if (ctx.priority != null) {
      priority = DmlStatement.PriorityEnum.valueOf(ctx.priority.getText().toUpperCase());
    }
    Boolean local = null;
    if (ctx.LOCAL() != null) {
      local = Boolean.TRUE;
    }
    String filename = ctx.filename.getText();
    DmlStatement.ViolationEnum violation = null;
    if (ctx.violation != null) {
      violation = DmlStatement.ViolationEnum.valueOf(ctx.violation.getText().toUpperCase());
    }
    TableName tableName = this.visitTableName(ctx.tableName());
    UidList uidList = this.visitUidList(ctx.uidList());
    CharsetName charsetName = this.visitCharsetName(ctx.charset);
    DmlStatement.FieldsFormatEnum fieldsFormat = null;
    if (ctx.fieldsFormat != null) {
      fieldsFormat =
          DmlStatement.FieldsFormatEnum.valueOf(ctx.fieldsFormat.getText().toUpperCase());
    }
    List<SelectFieldsInto> selectFieldsIntos = Lists.newArrayList();
    if (CollectionUtils.isNotEmpty(ctx.selectFieldsInto())) {
      for (MySqlParser.SelectFieldsIntoContext selectFieldsIntoCtx : ctx.selectFieldsInto()) {
        selectFieldsIntos.add(this.visitSelectFieldsInto(selectFieldsIntoCtx));
      }
    }
    List<SelectLinesInto> selectLinesIntos = Lists.newArrayList();
    if (CollectionUtils.isNotEmpty(ctx.selectLinesInto())) {
      for (MySqlParser.SelectLinesIntoContext selectLinesIntoCtx : ctx.selectLinesInto()) {
        selectLinesIntos.add(this.visitSelectLinesInto(selectLinesIntoCtx));
      }
    }
    DecimalLiteral decimalLiteral = this.visitDecimalLiteral(ctx.decimalLiteral());
    DmlStatement.LinesFormatEnum linesFormat = null;
    if (ctx.linesFormat != null) {
      linesFormat = DmlStatement.LinesFormatEnum.valueOf(ctx.linesFormat.getText().toUpperCase());
    }
    List<AssignmentField> assignmentFields = Lists.newArrayList();
    if (CollectionUtils.isNotEmpty(ctx.assignmentField())) {
      for (MySqlParser.AssignmentFieldContext assignmentFieldCtx : ctx.assignmentField()) {
        assignmentFields.add(this.visitAssignmentField(assignmentFieldCtx));
      }
    }
    List<UpdatedElement> updatedElements = Lists.newArrayList();
    if (CollectionUtils.isNotEmpty(ctx.updatedElement())) {
      for (MySqlParser.UpdatedElementContext updatedElementCtx : ctx.updatedElement()) {
        updatedElements.add(this.visitUpdatedElement(updatedElementCtx));
      }
    }

    return RelationalAlgebraExpressionFactory.makeLoadDataStatement(priority, local, filename,
      violation, tableName, uidList, charsetName, fieldsFormat, selectFieldsIntos, selectLinesIntos,
      decimalLiteral, linesFormat, assignmentFields, updatedElements);
  }

  @Override
  public LoadXmlStatement visitLoadXmlStatement(MySqlParser.LoadXmlStatementContext ctx) {
    if (ctx == null) {
      return null;
    }

    DmlStatement.PriorityEnum priority = null;
    if (ctx.priority != null) {
      priority = DmlStatement.PriorityEnum.valueOf(ctx.priority.getText().toUpperCase());
    }
    Boolean local = null;
    if (ctx.LOCAL() != null) {
      local = Boolean.TRUE;
    }
    String filename = ctx.filename.getText();
    DmlStatement.ViolationEnum violation = null;
    if (ctx.violation != null) {
      violation = DmlStatement.ViolationEnum.valueOf(ctx.violation.getText().toUpperCase());
    }
    TableName tableName = this.visitTableName(ctx.tableName());
    CharsetName charsetName = this.visitCharsetName(ctx.charset);
    String tag = null;
    if (ctx.tag != null) {
      tag = ctx.tag.getText();
    }
    DecimalLiteral decimalLiteral = this.visitDecimalLiteral(ctx.decimalLiteral());
    DmlStatement.LinesFormatEnum linesFormat = null;
    if (ctx.linesFormat != null) {
      linesFormat = DmlStatement.LinesFormatEnum.valueOf(ctx.linesFormat.getText().toUpperCase());
    }
    List<AssignmentField> assignmentFields = Lists.newArrayList();
    if (CollectionUtils.isNotEmpty(ctx.assignmentField())) {
      for (MySqlParser.AssignmentFieldContext assignmentFieldCtx : ctx.assignmentField()) {
        assignmentFields.add(this.visitAssignmentField(assignmentFieldCtx));
      }
    }
    List<UpdatedElement> updatedElements = Lists.newArrayList();
    if (CollectionUtils.isNotEmpty(ctx.updatedElement())) {
      for (MySqlParser.UpdatedElementContext updatedElementCtx : ctx.updatedElement()) {
        updatedElements.add(this.visitUpdatedElement(updatedElementCtx));
      }
    }

    return RelationalAlgebraExpressionFactory.makeLoadXmlStatement(priority, local, filename,
      violation, tableName, charsetName, tag, decimalLiteral, linesFormat, assignmentFields,
      updatedElements);
  }

  @Override
  public ReplaceStatement visitReplaceStatement(MySqlParser.ReplaceStatementContext ctx) {
    if (ctx == null) {
      return null;
    }

    ReplaceStatement.PriorityEnum priority = null;
    if (ctx.priority != null) {
      priority = ReplaceStatement.PriorityEnum.valueOf(ctx.priority.getText().toUpperCase());
    }
    TableName tableName = this.visitTableName(ctx.tableName());
    UidList partitions = this.visitUidList(ctx.partitions);
    UidList columns = this.visitUidList(ctx.columns);
    InsertStatementValue insertStatementValue =
        this.visitInsertStatementValue(ctx.insertStatementValue());
    List<UpdatedElement> setList = Lists.newArrayList();
    if (CollectionUtils.isNotEmpty(ctx.updatedElement())) {
      for (MySqlParser.UpdatedElementContext updatedElementCtx : ctx.updatedElement()) {
        setList.add(this.visitUpdatedElement(updatedElementCtx));
      }
    }

    return RelationalAlgebraExpressionFactory.makeReplaceStatement(priority, tableName, partitions,
      columns, insertStatementValue, setList);
  }

  public SelectStatement visitSelectStatement(MySqlParser.SelectStatementContext ctx) {
    if (ctx == null) {
      return null;
    }

    if (ctx instanceof MySqlParser.SimpleSelectContext) {
      return this.visitSimpleSelect((MySqlParser.SimpleSelectContext) ctx);
    } else if (ctx instanceof MySqlParser.ParenthesisSelectContext) {
      return this.visitParenthesisSelect((MySqlParser.ParenthesisSelectContext) ctx);
    } else if (ctx instanceof MySqlParser.UnionSelectContext) {
      return this.visitUnionSelect((MySqlParser.UnionSelectContext) ctx);
    } else if (ctx instanceof MySqlParser.UnionParenthesisSelectContext) {
      return this.visitUnionParenthesisSelect((MySqlParser.UnionParenthesisSelectContext) ctx);
    } else {
      throw ParserError.make(ctx);
    }
  }

  @Override
  public SimpleSelect visitSimpleSelect(MySqlParser.SimpleSelectContext ctx) {
    if (ctx == null) {
      return null;
    }

    MySqlParser.QuerySpecificationContext querySpecificationCtx = ctx.querySpecification();
    QuerySpecification querySpecification = this.visitQuerySpecification(querySpecificationCtx);
    MySqlParser.LockClauseContext lockClauseCtx = ctx.lockClause();
    DmlStatement.LockClauseEnum lockClause = this.visitLockClause(lockClauseCtx);

    return RelationalAlgebraExpressionFactory.makeSimpleSelect(querySpecification, lockClause);
  }

  @Override
  public ParenthesisSelect visitParenthesisSelect(MySqlParser.ParenthesisSelectContext ctx) {
    if (ctx == null) {
      return null;
    }

    QueryExpression queryExpression = this.visitQueryExpression(ctx.queryExpression());
    DmlStatement.LockClauseEnum lockClause = this.visitLockClause(ctx.lockClause());

    return RelationalAlgebraExpressionFactory.makeParenthesisSelect(queryExpression, lockClause);
  }

  @Override
  public UnionSelect visitUnionSelect(MySqlParser.UnionSelectContext ctx) {
    if (ctx == null) {
      return null;
    }

    QuerySpecificationNointo querySpecificationNointo =
        this.visitQuerySpecificationNointo(ctx.querySpecificationNointo());
    List<UnionStatement> unionStatements = Lists.newArrayList();
    for (MySqlParser.UnionStatementContext unionStatementCtx : ctx.unionStatement()) {
      unionStatements.add(this.visitUnionStatement(unionStatementCtx));
    }

    SelectStatement.UnionTypeEnum unionType = null;
    if (ctx.unionType != null) {
      unionType = SelectStatement.UnionTypeEnum.valueOf(ctx.unionType.getText().toUpperCase());
    }
    QuerySpecification querySpecification = this.visitQuerySpecification(ctx.querySpecification());
    QueryExpression queryExpression = this.visitQueryExpression(ctx.queryExpression());
    OrderByClause orderByClause = this.visitOrderByClause(ctx.orderByClause());
    LimitClause limitClause = this.visitLimitClause(ctx.limitClause());
    DmlStatement.LockClauseEnum lockClause = this.visitLockClause(ctx.lockClause());

    return RelationalAlgebraExpressionFactory.makeUnionSelect(querySpecificationNointo,
      unionStatements, unionType, querySpecification, queryExpression, orderByClause, limitClause,
      lockClause);
  }

  @Override
  public UnionParenthesisSelect
      visitUnionParenthesisSelect(MySqlParser.UnionParenthesisSelectContext ctx) {
    if (ctx == null) {
      return null;
    }

    QueryExpressionNointo queryExpressionNointo =
        this.visitQueryExpressionNointo(ctx.queryExpressionNointo());
    List<UnionParenthesis> unionParenthesisList = Lists.newArrayList();
    for (MySqlParser.UnionParenthesisContext unionParenthesisCtx : ctx.unionParenthesis()) {
      unionParenthesisList.add(this.visitUnionParenthesis(unionParenthesisCtx));
    }

    SelectStatement.UnionTypeEnum unionType = null;
    if (ctx.unionType != null) {
      unionType = SelectStatement.UnionTypeEnum.valueOf(ctx.unionType.getText().toUpperCase());
    }
    QueryExpression queryExpression = this.visitQueryExpression(ctx.queryExpression());
    OrderByClause orderByClause = this.visitOrderByClause(ctx.orderByClause());
    LimitClause limitClause = this.visitLimitClause(ctx.limitClause());
    DmlStatement.LockClauseEnum lockClause = this.visitLockClause(ctx.lockClause());

    return RelationalAlgebraExpressionFactory.makeUnionParenthesisSelect(queryExpressionNointo,
      unionParenthesisList, unionType, queryExpression, orderByClause, limitClause, lockClause);
  }

  @Override
  public UpdateStatement visitUpdateStatement(MySqlParser.UpdateStatementContext ctx) {
    if (ctx == null) {
      return null;
    }

    ParseTree child0 = ctx.getChild(0);
    if (child0 instanceof MySqlParser.SingleUpdateStatementContext) {
      return this.visitSingleUpdateStatement((MySqlParser.SingleUpdateStatementContext) child0);
    } else if (child0 instanceof MySqlParser.MultipleUpdateStatementContext) {
      return this.visitMultipleUpdateStatement((MySqlParser.MultipleUpdateStatementContext) child0);
    } else {
      throw ParserError.make(ctx);
    }
  }

  @Override
  public InsertStatementValue
      visitInsertStatementValue(MySqlParser.InsertStatementValueContext ctx) {
    if (ctx == null) {
      return null;
    }

    SelectStatement selectStatement = this.visitSelectStatement(ctx.selectStatement());
    List<ExpressionsWithDefaults> expressionsWithDefaults = Lists.newArrayList();
    if (CollectionUtils.isNotEmpty(ctx.expressionsWithDefaults())) {
      for (MySqlParser.ExpressionsWithDefaultsContext expressionsWithDefaultsCtx : ctx
          .expressionsWithDefaults()) {
        expressionsWithDefaults.add(this.visitExpressionsWithDefaults(expressionsWithDefaultsCtx));
      }
    }
    return RelationalAlgebraExpressionFactory.makeInsertStatementValue(selectStatement,
      expressionsWithDefaults);
  }

  @Override
  public UpdatedElement visitUpdatedElement(MySqlParser.UpdatedElementContext ctx) {
    if (ctx == null) {
      return null;
    }

    FullColumnName fullColumnName = this.visitFullColumnName(ctx.fullColumnName());
    Expression expression = this.visitExpression(ctx.expression());
    return RelationalAlgebraExpressionFactory.makeUpdatedElement(fullColumnName, expression);
  }

  @Override
  public AssignmentField visitAssignmentField(MySqlParser.AssignmentFieldContext ctx) {
    if (ctx == null) {
      return null;
    }

    Uid uid = this.visitUid(ctx.uid());
    String localId = null;
    if (ctx.LOCAL_ID() != null) {
      localId = ctx.LOCAL_ID().getText();
    }

    return RelationalAlgebraExpressionFactory.makeAssignmentField(uid, localId);
  }

  @Override
  public DmlStatement.LockClauseEnum visitLockClause(MySqlParser.LockClauseContext ctx) {
    if (ctx == null) {
      return null;
    }

    if (ctx.FOR() != null) {
      return DmlStatement.LockClauseEnum.FOR_UPDATE;
    } else if (ctx.LOCK() != null) {
      return DmlStatement.LockClauseEnum.LOCK_IN_SHARE_MODE;
    } else {
      throw ParserError.make(ctx);
    }

  }

  @Override
  public SingleDeleteStatement
      visitSingleDeleteStatement(MySqlParser.SingleDeleteStatementContext ctx) {
    if (ctx == null) {
      return null;
    }

    Boolean lowPriority = null;
    if (ctx.priority != null) {
      lowPriority = Boolean.TRUE;
    }
    Boolean quick = null;
    if (ctx.QUICK() != null) {
      quick = Boolean.TRUE;
    }
    Boolean ignore = null;
    if (ctx.IGNORE() != null) {
      ignore = Boolean.TRUE;
    }
    TableName tableName = this.visitTableName(ctx.tableName());
    UidList uidList = this.visitUidList(ctx.uidList());
    Expression where = this.visitExpression(ctx.expression());
    OrderByClause orderByClause = this.visitOrderByClause(ctx.orderByClause());
    DecimalLiteral limit = this.visitDecimalLiteral(ctx.decimalLiteral());

    return RelationalAlgebraExpressionFactory.makeSingleDeleteStatement(lowPriority, quick, ignore,
      tableName, uidList, where, orderByClause, limit);
  }

  @Override
  public MultipleDeleteStatement
      visitMultipleDeleteStatement(MySqlParser.MultipleDeleteStatementContext ctx) {
    if (ctx == null) {
      return null;
    }

    Boolean lowPriority = null;
    if (ctx.priority != null) {
      lowPriority = Boolean.TRUE;
    }
    Boolean quick = null;
    if (ctx.QUICK() != null) {
      quick = Boolean.TRUE;
    }
    Boolean ignore = null;
    if (ctx.IGNORE() != null) {
      ignore = Boolean.TRUE;
    }
    boolean using;
    if (ctx.USING() != null) {
      using = true;
    } else {
      using = false;
    }
    List<TableName> tableNames = Lists.newArrayList();
    for (MySqlParser.TableNameContext tableNameCtx : ctx.tableName()) {
      tableNames.add(this.visitTableName(tableNameCtx));
    }
    TableSources tableSources = this.visitTableSources(ctx.tableSources());
    Expression where = this.visitExpression(ctx.expression());

    return RelationalAlgebraExpressionFactory.makeMultipleDeleteStatement(lowPriority, quick,
      ignore, using, tableNames, tableSources, where);
  }

  @Override
  public HandlerOpenStatement
      visitHandlerOpenStatement(MySqlParser.HandlerOpenStatementContext ctx) {
    if (ctx == null) {
      return null;
    }

    TableName tableName = this.visitTableName(ctx.tableName());
    Uid uid = this.visitUid(ctx.uid());
    return RelationalAlgebraExpressionFactory.makeHandlerOpenStatement(tableName, uid);
  }

  @Override
  public HandlerReadIndexStatement
      visitHandlerReadIndexStatement(MySqlParser.HandlerReadIndexStatementContext ctx) {
    if (ctx == null) {
      return null;
    }

    TableName tableName = this.visitTableName(ctx.tableName());
    Uid index = this.visitUid(ctx.index);
    RelationalComparisonOperatorEnum comparisonOperator = null;
    Constants constants = null;
    HandlerReadIndexStatement.MoveOrderEnum moveOrder = null;
    if (ctx.comparisonOperator() != null) {
      comparisonOperator = this.visitComparisonOperator(ctx.comparisonOperator());
      constants = this.visitConstants(ctx.constants());
    } else if (ctx.moveOrder != null) {
      moveOrder =
          HandlerReadIndexStatement.MoveOrderEnum.valueOf(ctx.moveOrder.getText().toUpperCase());
    } else {
      throw ParserError.make(ctx);
    }
    Expression where = this.visitExpression(ctx.expression());
    DecimalLiteral limit = this.visitDecimalLiteral(ctx.decimalLiteral());

    return RelationalAlgebraExpressionFactory.makeHandlerReadIndexStatement(tableName, index,
      comparisonOperator, constants, moveOrder, where, limit);
  }

  @Override
  public HandlerReadStatement
      visitHandlerReadStatement(MySqlParser.HandlerReadStatementContext ctx) {
    if (ctx == null) {
      return null;
    }

    TableName tableName = this.visitTableName(ctx.tableName());
    HandlerReadStatement.MoveOrderEnum moveOrder =
        HandlerReadStatement.MoveOrderEnum.valueOf(ctx.moveOrder.getText().toUpperCase());
    Expression where = this.visitExpression(ctx.expression());
    DecimalLiteral limit = this.visitDecimalLiteral(ctx.decimalLiteral());
    return RelationalAlgebraExpressionFactory.makeHandlerReadStatement(tableName, moveOrder, where,
      limit);
  }

  @Override
  public HandlerCloseStatement
      visitHandlerCloseStatement(MySqlParser.HandlerCloseStatementContext ctx) {
    if (ctx == null) {
      return null;
    }

    TableName tableName = this.visitTableName(ctx.tableName());
    return RelationalAlgebraExpressionFactory.makeHandlerCloseStatement(tableName);
  }

  @Override
  public SingleUpdateStatement
      visitSingleUpdateStatement(MySqlParser.SingleUpdateStatementContext ctx) {
    if (ctx == null) {
      return null;
    }

    Boolean lowPriority = null;
    if (ctx.priority != null) {
      lowPriority = Boolean.TRUE;
    }
    Boolean ignore = null;
    if (ctx.IGNORE() != null) {
      ignore = Boolean.TRUE;
    }
    TableName tableName = this.visitTableName(ctx.tableName());
    Uid uid = this.visitUid(ctx.uid());
    List<UpdatedElement> updatedElements = Lists.newArrayList();
    for (MySqlParser.UpdatedElementContext updatedElementCtx : ctx.updatedElement()) {
      updatedElements.add(this.visitUpdatedElement(updatedElementCtx));
    }
    Expression where = this.visitExpression(ctx.expression());
    OrderByClause orderByClause = this.visitOrderByClause(ctx.orderByClause());
    LimitClause limitClause = this.visitLimitClause(ctx.limitClause());

    return RelationalAlgebraExpressionFactory.makeSingleUpdateStatement(lowPriority, ignore,
      tableName, uid, updatedElements, where, orderByClause, limitClause);
  }

  @Override
  public MultipleUpdateStatement
      visitMultipleUpdateStatement(MySqlParser.MultipleUpdateStatementContext ctx) {
    if (ctx == null) {
      return null;
    }

    Boolean lowPriority = null;
    if (ctx.priority != null) {
      lowPriority = Boolean.TRUE;
    }
    Boolean ignore = null;
    if (ctx.IGNORE() != null) {
      ignore = Boolean.TRUE;
    }
    TableSources tableSources = this.visitTableSources(ctx.tableSources());
    List<UpdatedElement> updatedElements = Lists.newArrayList();
    for (MySqlParser.UpdatedElementContext updatedElementCtx : ctx.updatedElement()) {
      updatedElements.add(this.visitUpdatedElement(updatedElementCtx));
    }
    Expression where = this.visitExpression(ctx.expression());
    return RelationalAlgebraExpressionFactory.makeMultipleUpdateStatement(lowPriority, ignore,
      tableSources, updatedElements, where);
  }

  @Override
  public OrderByClause visitOrderByClause(MySqlParser.OrderByClauseContext ctx) {
    if (ctx == null) {
      return null;
    }

    List<OrderByExpression> orderByExpressions = Lists.newArrayList();
    for (MySqlParser.OrderByExpressionContext orderByExpressionCtx : ctx.orderByExpression()) {
      orderByExpressions.add(this.visitOrderByExpression(orderByExpressionCtx));
    }
    return RelationalAlgebraExpressionFactory.makeOrderByClause(orderByExpressions);
  }

  @Override
  public OrderByExpression visitOrderByExpression(MySqlParser.OrderByExpressionContext ctx) {
    if (ctx == null) {
      return null;
    }
    Expression expression = this.visitExpression(ctx.expression());
    OrderByExpression.OrderType order = null;
    if (ctx.order != null) {
      order = OrderByExpression.OrderType.valueOf(ctx.order.getText().toUpperCase());
    }

    return RelationalAlgebraExpressionFactory.makeOrderByExpression(expression, order);
  }

  @Override
  public TableSources visitTableSources(MySqlParser.TableSourcesContext ctx) {
    if (ctx == null) {
      return null;
    }

    List<MySqlParser.TableSourceContext> tableSourceCtxs = ctx.tableSource();
    List<TableSource> tableSources = Lists.newArrayList();
    if (CollectionUtils.isNotEmpty(tableSourceCtxs)) {
      for (MySqlParser.TableSourceContext tableSourceCtx : tableSourceCtxs) {
        tableSources.add(this.visitTableSource(tableSourceCtx));
      }
    }

    return RelationalAlgebraExpressionFactory.makeTableSources(tableSources);
  }

  public TableSource visitTableSource(MySqlParser.TableSourceContext ctx) {
    if (ctx == null) {
      return null;
    }

    if (ctx instanceof MySqlParser.TableSourceBaseContext) {
      return this.visitTableSourceBase((MySqlParser.TableSourceBaseContext) ctx);
    } else if (ctx instanceof MySqlParser.TableSourceNestedContext) {
      return this.visitTableSourceNested((MySqlParser.TableSourceNestedContext) ctx);
    } else {
      throw ParserError.make(ctx);
    }
  }

  @Override
  public TableSourceBase visitTableSourceBase(MySqlParser.TableSourceBaseContext ctx) {
    if (ctx == null) {
      return null;
    }

    MySqlParser.TableSourceItemContext tableSourceItemCtx = ctx.tableSourceItem();
    TableSourceItem tableSourceItem = this.visitTableSourceItem(tableSourceItemCtx);

    List<MySqlParser.JoinPartContext> joinPartCtxs = ctx.joinPart();
    List<JoinPart> joinParts = Lists.newArrayList();
    if (CollectionUtils.isNotEmpty(joinPartCtxs)) {
      for (MySqlParser.JoinPartContext joinPartCtx : joinPartCtxs) {
        joinParts.add(this.visitJoinPart(joinPartCtx));
      }
    }

    return RelationalAlgebraExpressionFactory.makeTableSourceBase(tableSourceItem, joinParts);
  }

  @Override
  public TableSourceNested visitTableSourceNested(MySqlParser.TableSourceNestedContext ctx) {
    if (ctx == null) {
      return null;
    }

    TableSourceItem tableSourceItem = this.visitTableSourceItem(ctx.tableSourceItem());
    List<JoinPart> joinParts = Lists.newArrayList();
    if (CollectionUtils.isNotEmpty(ctx.joinPart())) {
      for (MySqlParser.JoinPartContext joinPartCtx : ctx.joinPart()) {
        joinParts.add(this.visitJoinPart(joinPartCtx));
      }
    }

    return RelationalAlgebraExpressionFactory.makeTableSourceNested(tableSourceItem, joinParts);
  }

  public TableSourceItem visitTableSourceItem(MySqlParser.TableSourceItemContext ctx) {
    if (ctx == null) {
      return null;
    }

    if (ctx instanceof MySqlParser.AtomTableItemContext) {
      return this.visitAtomTableItem((MySqlParser.AtomTableItemContext) ctx);
    } else if (ctx instanceof MySqlParser.SubqueryTableItemContext) {
      return this.visitSubqueryTableItem((MySqlParser.SubqueryTableItemContext) ctx);
    } else if (ctx instanceof MySqlParser.TableSourcesItemContext) {
      return this.visitTableSourcesItem((MySqlParser.TableSourcesItemContext) ctx);
    } else {
      throw ParserError.make(ctx);
    }

  }

  @Override
  public AtomTableItem visitAtomTableItem(MySqlParser.AtomTableItemContext ctx) {
    if (ctx == null) {
      return null;
    }

    MySqlParser.TableNameContext tableNameCtx = ctx.tableName();
    TableName tableName = this.visitTableName(tableNameCtx);

    MySqlParser.UidListContext uidListCtx = ctx.uidList();
    UidList uidList = this.visitUidList(uidListCtx);

    MySqlParser.UidContext uidCtx = ctx.uid();
    Uid uid = this.visitUid(uidCtx);

    List<MySqlParser.IndexHintContext> indexHintCtxs = ctx.indexHint();
    List<IndexHint> indexHints = Lists.newArrayList();
    if (CollectionUtils.isNotEmpty(indexHintCtxs)) {
      for (MySqlParser.IndexHintContext indexHintCtx : indexHintCtxs) {
        indexHints.add(this.visitIndexHint(indexHintCtx));
      }
    }

    return RelationalAlgebraExpressionFactory.makeAtomTableItem(tableName, uidList, uid,
      indexHints);
  }

  @Override
  public SubqueryTableItem visitSubqueryTableItem(MySqlParser.SubqueryTableItemContext ctx) {
    if (ctx == null) {
      return null;
    }

    SelectStatement selectStatement = this.visitSelectStatement(ctx.selectStatement());
    Uid alias = this.visitUid(ctx.alias);

    return RelationalAlgebraExpressionFactory.makeSubqueryTableItem(selectStatement, alias);
  }

  @Override
  public TableSourcesItem visitTableSourcesItem(MySqlParser.TableSourcesItemContext ctx) {
    if (ctx == null) {
      return null;
    }

    TableSources tableSources = this.visitTableSources(ctx.tableSources());
    return RelationalAlgebraExpressionFactory.makeTableSourcesItem(tableSources);
  }

  @Override
  public IndexHint visitIndexHint(MySqlParser.IndexHintContext ctx) {
    if (ctx == null) {
      return null;
    }

    IndexHint.IndexHintAction indexHintAction =
        IndexHint.IndexHintAction.valueOf(ctx.indexHintAction.getText().toUpperCase());
    IndexHint.KeyFormat keyFormat =
        IndexHint.KeyFormat.valueOf(ctx.keyFormat.getText().toUpperCase());
    DmlStatement.IndexHintTypeEnum indexHintType = this.visitIndexHintType(ctx.indexHintType());
    UidList uidList = this.visitUidList(ctx.uidList());
    return RelationalAlgebraExpressionFactory.makeIndexHint(indexHintAction, keyFormat,
      indexHintType, uidList);
  }

  @Override
  public DmlStatement.IndexHintTypeEnum visitIndexHintType(MySqlParser.IndexHintTypeContext ctx) {
    if (ctx == null) {
      return null;
    }

    if (ctx.JOIN() != null) {
      return DmlStatement.IndexHintTypeEnum.JOIN;
    } else if (ctx.ORDER() != null) {
      return DmlStatement.IndexHintTypeEnum.ORDER_BY;
    } else if (ctx.GROUP() != null) {
      return DmlStatement.IndexHintTypeEnum.GROUP_BY;
    } else {
      throw ParserError.make(ctx);
    }

  }

  public JoinPart visitJoinPart(MySqlParser.JoinPartContext ctx) {
    if (ctx == null) {
      return null;
    }

    if (ctx instanceof MySqlParser.InnerJoinContext) {
      return this.visitInnerJoin((MySqlParser.InnerJoinContext) ctx);
    } else if (ctx instanceof MySqlParser.StraightJoinContext) {
      return this.visitStraightJoin((MySqlParser.StraightJoinContext) ctx);
    } else if (ctx instanceof MySqlParser.OuterJoinContext) {
      return this.visitOuterJoin((MySqlParser.OuterJoinContext) ctx);
    } else if (ctx instanceof MySqlParser.NaturalJoinContext) {
      return this.visitNaturalJoin((MySqlParser.NaturalJoinContext) ctx);
    } else {
      throw ParserError.make(ctx);
    }
  }

  @Override
  public InnerJoin visitInnerJoin(MySqlParser.InnerJoinContext ctx) {
    if (ctx == null) {
      return null;
    }

    TableSourceItem tableSourceItem = this.visitTableSourceItem(ctx.tableSourceItem());
    Expression expression = this.visitExpression(ctx.expression());
    UidList uidList = this.visitUidList(ctx.uidList());
    return RelationalAlgebraExpressionFactory.makeInnerJoin(tableSourceItem, expression, uidList);
  }

  @Override
  public StraightJoin visitStraightJoin(MySqlParser.StraightJoinContext ctx) {
    if (ctx == null) {
      return null;
    }

    TableSourceItem tableSourceItem = this.visitTableSourceItem(ctx.tableSourceItem());
    Expression expression = this.visitExpression(ctx.expression());
    return RelationalAlgebraExpressionFactory.makeStraightJoin(tableSourceItem, expression);
  }

  @Override
  public OuterJoin visitOuterJoin(MySqlParser.OuterJoinContext ctx) {
    if (ctx == null) {
      return null;
    }

    DmlStatement.OuterJoinType type = null;
    if (ctx.LEFT() != null) {
      type = DmlStatement.OuterJoinType.LEFT;
    } else if (ctx.RIGHT() != null) {
      type = DmlStatement.OuterJoinType.RIGHT;
    } else {
      throw ParserError.make(ctx);
    }

    TableSourceItem tableSourceItem = this.visitTableSourceItem(ctx.tableSourceItem());
    Expression expression = this.visitExpression(ctx.expression());
    UidList uidList = this.visitUidList(ctx.uidList());
    return RelationalAlgebraExpressionFactory.makeOuterJoin(type, tableSourceItem, expression,
      uidList);
  }

  @Override
  public NaturalJoin visitNaturalJoin(MySqlParser.NaturalJoinContext ctx) {
    if (ctx == null) {
      return null;
    }

    DmlStatement.OuterJoinType outerJoinType = null;
    if (ctx.LEFT() != null) {
      outerJoinType = DmlStatement.OuterJoinType.LEFT;
    } else if (ctx.RIGHT() != null) {
      outerJoinType = DmlStatement.OuterJoinType.RIGHT;
    }
    TableSourceItem tableSourceItem = this.visitTableSourceItem(ctx.tableSourceItem());

    return RelationalAlgebraExpressionFactory.makeNaturalJoin(outerJoinType, tableSourceItem);
  }

  @Override
  public QueryExpression visitQueryExpression(MySqlParser.QueryExpressionContext ctx) {
    if (ctx == null) {
      return null;
    }

    QuerySpecification querySpecification = this.visitQuerySpecification(ctx.querySpecification());
    if (querySpecification != null) {
      return RelationalAlgebraExpressionFactory.makeQueryExpression(querySpecification);
    } else {
      return this.visitQueryExpression(ctx.queryExpression()); // tail
    }
  }

  @Override
  public QueryExpressionNointo
      visitQueryExpressionNointo(MySqlParser.QueryExpressionNointoContext ctx) {
    if (ctx == null) {
      return null;
    }

    QuerySpecificationNointo querySpecificationNointo =
        this.visitQuerySpecificationNointo(ctx.querySpecificationNointo());
    if (querySpecificationNointo != null) {
      return RelationalAlgebraExpressionFactory.makeQueryExpressionNointo(querySpecificationNointo);
    } else {
      return this.visitQueryExpressionNointo(ctx.queryExpressionNointo()); // tail
    }
  }

  @Override
  public QuerySpecification visitQuerySpecification(MySqlParser.QuerySpecificationContext ctx) {
    if (ctx == null) {
      return null;
    }
    List<SelectStatement.SelectSpecEnum> selectSpecs = Lists.newArrayList();
    if (CollectionUtils.isNotEmpty(ctx.selectSpec())) {
      for (MySqlParser.SelectSpecContext selectSpecCtx : ctx.selectSpec()) {
        selectSpecs.add(this.visitSelectSpec(selectSpecCtx));
      }
    }

    SelectElements selectElements = this.visitSelectElements(ctx.selectElements());
    SelectIntoExpression selectIntoExpression =
        this.visitSelectIntoExpression(ctx.selectIntoExpression());
    FromClause fromClause = this.visitFromClause(ctx.fromClause());
    OrderByClause orderByClause = this.visitOrderByClause(ctx.orderByClause());
    LimitClause limitClause = this.visitLimitClause(ctx.limitClause());

    return RelationalAlgebraExpressionFactory.makeQuerySpecification(//
      selectSpecs, selectElements, selectIntoExpression, //
      fromClause, orderByClause, limitClause);
  }

  @Override
  public QuerySpecificationNointo
      visitQuerySpecificationNointo(MySqlParser.QuerySpecificationNointoContext ctx) {
    if (ctx == null) {
      return null;
    }

    List<SelectStatement.SelectSpecEnum> selectSpecs = Lists.newArrayList();
    if (CollectionUtils.isNotEmpty(ctx.selectSpec())) {
      for (MySqlParser.SelectSpecContext selectSpecCtx : ctx.selectSpec()) {
        selectSpecs.add(this.visitSelectSpec(selectSpecCtx));
      }
    }
    SelectElements selectElements = this.visitSelectElements(ctx.selectElements());
    FromClause fromClause = this.visitFromClause(ctx.fromClause());
    OrderByClause orderByClause = this.visitOrderByClause(ctx.orderByClause());
    LimitClause limitClause = this.visitLimitClause(ctx.limitClause());

    return RelationalAlgebraExpressionFactory.makeQuerySpecificationNointo(selectSpecs,
      selectElements, fromClause, orderByClause, limitClause);
  }

  @Override
  public UnionParenthesis visitUnionParenthesis(MySqlParser.UnionParenthesisContext ctx) {
    if (ctx == null) {
      return null;
    }

    SelectStatement.UnionTypeEnum unionType = null;
    if (ctx.ALL() != null) {
      unionType = SelectStatement.UnionTypeEnum.ALL;
    } else if (ctx.DISTINCT() != null) {
      unionType = SelectStatement.UnionTypeEnum.DISTINCT;
    }
    QueryExpressionNointo queryExpressionNointo =
        this.visitQueryExpressionNointo(ctx.queryExpressionNointo());

    return RelationalAlgebraExpressionFactory.makeUnionParenthesis(unionType,
      queryExpressionNointo);
  }

  @Override
  public UnionStatement visitUnionStatement(MySqlParser.UnionStatementContext ctx) {
    if (ctx == null) {
      return null;
    }

    SelectStatement.UnionTypeEnum unionType = null;
    if (ctx.ALL() != null) {
      unionType = SelectStatement.UnionTypeEnum.ALL;
    } else if (ctx.DISTINCT() != null) {
      unionType = SelectStatement.UnionTypeEnum.DISTINCT;
    }
    QuerySpecificationNointo querySpecificationNointo =
        this.visitQuerySpecificationNointo(ctx.querySpecificationNointo());
    QueryExpressionNointo queryExpressionNointo =
        this.visitQueryExpressionNointo(ctx.queryExpressionNointo());
    return RelationalAlgebraExpressionFactory.makeUnionStatement(unionType,
      querySpecificationNointo, queryExpressionNointo);
  }

  @Override
  public SelectStatement.SelectSpecEnum visitSelectSpec(MySqlParser.SelectSpecContext ctx) {
    if (ctx == null) {
      return null;
    }

    return SelectStatement.SelectSpecEnum.valueOf(ctx.getText().toUpperCase());
  }

  @Override
  public SelectElements visitSelectElements(MySqlParser.SelectElementsContext ctx) {
    if (ctx == null) {
      return null;
    }

    Token star = ctx.star;
    List<MySqlParser.SelectElementContext> selectElementCtxs = ctx.selectElement();
    List<SelectElement> selectElements = Lists.newArrayList();
    if (CollectionUtils.isNotEmpty(selectElementCtxs)) {
      for (MySqlParser.SelectElementContext selectElementCtx : selectElementCtxs) {
        selectElements.add(this.visitSelectElement(selectElementCtx));
      }
    }

    return RelationalAlgebraExpressionFactory.makeSelectElements(//
      star != null, selectElements);
  }

  public SelectElement visitSelectElement(MySqlParser.SelectElementContext ctx) {
    if (ctx == null) {
      return null;
    }

    if (ctx instanceof MySqlParser.SelectStarElementContext) {
      return this.visitSelectStarElement((MySqlParser.SelectStarElementContext) ctx);
    } else if (ctx instanceof MySqlParser.SelectColumnElementContext) {
      return this.visitSelectColumnElement((MySqlParser.SelectColumnElementContext) ctx);
    } else if (ctx instanceof MySqlParser.SelectFunctionElementContext) {
      return this.visitSelectFunctionElement((MySqlParser.SelectFunctionElementContext) ctx);
    } else if (ctx instanceof MySqlParser.SelectExpressionElementContext) {
      return this.visitSelectExpressionElement((MySqlParser.SelectExpressionElementContext) ctx);
    } else {
      throw ParserError.make(ctx);
    }
  }

  @Override
  public SelectStarElement visitSelectStarElement(MySqlParser.SelectStarElementContext ctx) {
    if (ctx == null) {
      return null;
    }

    FullId fullId = this.visitFullId(ctx.fullId());
    return RelationalAlgebraExpressionFactory.makeSelectStarElement(fullId);
  }

  @Override
  public SelectColumnElement visitSelectColumnElement(MySqlParser.SelectColumnElementContext ctx) {
    if (ctx == null) {
      return null;
    }

    MySqlParser.FullColumnNameContext fullColumnNameCtx = ctx.fullColumnName();
    FullColumnName fullColumnName = this.visitFullColumnName(fullColumnNameCtx);

    MySqlParser.UidContext uidCtx = ctx.uid();
    Uid uid = this.visitUid(uidCtx);

    return RelationalAlgebraExpressionFactory.makeSelectColumnElement(fullColumnName, uid);
  }

  @Override
  public SelectFunctionElement
      visitSelectFunctionElement(MySqlParser.SelectFunctionElementContext ctx) {
    if (ctx == null) {
      return null;
    }

    FunctionCall functionCall = this.visitFunctionCall(ctx.functionCall());
    Uid uid = this.visitUid(ctx.uid());
    return RelationalAlgebraExpressionFactory.makeSelectFunctionElement(functionCall, uid);
  }

  @Override
  public SelectExpressionElement
      visitSelectExpressionElement(MySqlParser.SelectExpressionElementContext ctx) {
    if (ctx == null) {
      return null;
    }

    String localId = null;
    if (ctx.LOCAL_ID() != null) {
      localId = ctx.LOCAL_ID().getText();
    }
    Expression expression = this.visitExpression(ctx.expression());
    Uid uid = this.visitUid(ctx.uid());

    return RelationalAlgebraExpressionFactory.makeSelectExpressionElement(localId, expression, uid);
  }

  public SelectIntoExpression
      visitSelectIntoExpression(MySqlParser.SelectIntoExpressionContext ctx) {
    if (ctx == null) {
      return null;
    }

    if (ctx instanceof MySqlParser.SelectIntoVariablesContext) {
      return this.visitSelectIntoVariables((MySqlParser.SelectIntoVariablesContext) ctx);
    } else if (ctx instanceof MySqlParser.SelectIntoDumpFileContext) {
      return this.visitSelectIntoDumpFile((MySqlParser.SelectIntoDumpFileContext) ctx);
    } else if (ctx instanceof MySqlParser.SelectIntoTextFileContext) {
      return this.visitSelectIntoTextFile((MySqlParser.SelectIntoTextFileContext) ctx);
    } else {
      throw ParserError.make(ctx);
    }
  }

  @Override
  public SelectIntoVariables visitSelectIntoVariables(MySqlParser.SelectIntoVariablesContext ctx) {
    if (ctx == null) {
      return null;
    }

    List<AssignmentField> assignmentFields = Lists.newArrayList();
    for (MySqlParser.AssignmentFieldContext assignmentFieldCtx : ctx.assignmentField()) {
      assignmentFields.add(this.visitAssignmentField(assignmentFieldCtx));
    }

    return RelationalAlgebraExpressionFactory.makeSelectIntoVariables(assignmentFields);
  }

  @Override
  public SelectIntoDumpFile visitSelectIntoDumpFile(MySqlParser.SelectIntoDumpFileContext ctx) {
    if (ctx == null) {
      return null;
    }

    String stringLiteral = ctx.STRING_LITERAL().getText();
    return RelationalAlgebraExpressionFactory.makeSelectIntoDumpFile(stringLiteral);
  }

  @Override
  public SelectIntoTextFile visitSelectIntoTextFile(MySqlParser.SelectIntoTextFileContext ctx) {
    if (ctx == null) {
      return null;
    }

    String filename = ctx.filename.getText();
    CharsetName charsetName = this.visitCharsetName(ctx.charset);
    SelectIntoTextFile.TieldsFormatType fieldsFormat = null;
    if (ctx.fieldsFormat != null) {
      fieldsFormat =
          SelectIntoTextFile.TieldsFormatType.valueOf(ctx.fieldsFormat.getText().toUpperCase());
    }
    List<SelectFieldsInto> selectFieldsIntos = Lists.newArrayList();
    if (CollectionUtils.isNotEmpty(ctx.selectFieldsInto())) {
      for (MySqlParser.SelectFieldsIntoContext selectFieldsIntoCtx : ctx.selectFieldsInto()) {
        selectFieldsIntos.add(this.visitSelectFieldsInto(selectFieldsIntoCtx));
      }
    }
    List<SelectLinesInto> selectLinesIntos = Lists.newArrayList();
    if (CollectionUtils.isNotEmpty(ctx.selectLinesInto())) {
      for (MySqlParser.SelectLinesIntoContext selectLinesIntoCtx : ctx.selectLinesInto()) {
        selectLinesIntos.add(this.visitSelectLinesInto(selectLinesIntoCtx));
      }
    }

    return RelationalAlgebraExpressionFactory.makeSelectIntoTextFile(filename, charsetName,
      fieldsFormat, selectFieldsIntos, selectLinesIntos);
  }

  @Override
  public SelectFieldsInto visitSelectFieldsInto(MySqlParser.SelectFieldsIntoContext ctx) {
    if (ctx == null) {
      return null;
    }
    SelectFieldsInto.Type type = null;
    Boolean optionally = null;
    String stringLiteral = null;

    if (ctx.TERMINATED() != null) {
      type = SelectFieldsInto.Type.TERMINATED_BY;
      stringLiteral = ctx.terminationField.getText();
    } else if (ctx.ENCLOSED() != null) {
      type = SelectFieldsInto.Type.ENCLOSED_BY;
      if (ctx.OPTIONALLY() != null) {
        optionally = Boolean.TRUE;
      }
      stringLiteral = ctx.enclosion.getText();
    } else if (ctx.ESCAPED() != null) {
      type = SelectFieldsInto.Type.ESCAPED_BY;
      stringLiteral = ctx.escaping.getText();
    } else {
      throw ParserError.make(ctx);
    }

    return RelationalAlgebraExpressionFactory.makeSelectFieldsInto(type, optionally, stringLiteral);
  }

  @Override
  public SelectLinesInto visitSelectLinesInto(MySqlParser.SelectLinesIntoContext ctx) {
    if (ctx == null) {
      return null;
    }

    SelectLinesInto.Type type = null;
    String stringLiteral = null;
    if (ctx.STARTING() != null) {
      type = SelectLinesInto.Type.STARTING_BY;
      stringLiteral = ctx.starting.getText();
    } else if (ctx.TERMINATED() != null) {
      type = SelectLinesInto.Type.TERMINATED_BY;
      stringLiteral = ctx.terminationLine.getText();
    } else {
      throw ParserError.make(ctx);
    }

    return RelationalAlgebraExpressionFactory.makeSelectLinesInto(type, stringLiteral);
  }

  @Override
  public FromClause visitFromClause(MySqlParser.FromClauseContext ctx) {
    if (ctx == null) {
      return null;
    }

    MySqlParser.TableSourcesContext tableSourcesCtx = ctx.tableSources();
    TableSources tableSources = this.visitTableSources(tableSourcesCtx);

    MySqlParser.ExpressionContext whereExprCtx = ctx.whereExpr;
    Expression whereExpr = this.visitExpression(whereExprCtx);

    List<MySqlParser.GroupByItemContext> groupByItemCtxs = ctx.groupByItem();
    List<GroupByItem> groupByItems = Lists.newArrayList();
    if (CollectionUtils.isNotEmpty(groupByItemCtxs)) {
      for (MySqlParser.GroupByItemContext groupByItemCtx : groupByItemCtxs) {
        groupByItems.add(this.visitGroupByItem(groupByItemCtx));
      }
    }

    MySqlParser.ExpressionContext havingExprCtx = ctx.havingExpr;
    Expression havingExpr = this.visitExpression(havingExprCtx);

    Boolean withRollup = ctx.ROLLUP() != null;

    return RelationalAlgebraExpressionFactory.makeFromClause(//
      tableSources, whereExpr, groupByItems, withRollup, havingExpr);
  }

  @Override
  public GroupByItem visitGroupByItem(MySqlParser.GroupByItemContext ctx) {
    if (ctx == null) {
      return null;
    }

    Expression expression = this.visitExpression(ctx.expression());
    GroupByItem.OrderType order = null;
    if (ctx.order != null) {
      order = GroupByItem.OrderType.valueOf(ctx.order.getText().toUpperCase());
    }

    return RelationalAlgebraExpressionFactory.makeGroupByItem(expression, order);
  }

  @Override
  public LimitClause visitLimitClause(MySqlParser.LimitClauseContext ctx) {
    if (ctx == null) {
      return null;
    }

    LimitClauseAtom limit = this.visitLimitClauseAtom(ctx.limit);
    LimitClauseAtom offset = this.visitLimitClauseAtom(ctx.offset);

    return RelationalAlgebraExpressionFactory.makeLimitClause(limit, offset);
  }

  @Override
  public LimitClauseAtom visitLimitClauseAtom(MySqlParser.LimitClauseAtomContext ctx) {
    if (ctx == null) {
      return null;
    }

    DecimalLiteral decimalLiteral = this.visitDecimalLiteral(ctx.decimalLiteral());
    MysqlVariable mysqlVariable = this.visitMysqlVariable(ctx.mysqlVariable());
    return RelationalAlgebraExpressionFactory.makeLimitClauseAtom(decimalLiteral, mysqlVariable);
  }

  @Override
  public StartTransaction visitStartTransaction(MySqlParser.StartTransactionContext ctx) {
    if (ctx == null) {
      return null;
    }

    List<TransactionStatement.TransactionModeEnum> transactionModes = Lists.newArrayList();
    if (CollectionUtils.isNotEmpty(ctx.transactionMode())) {
      for (MySqlParser.TransactionModeContext transactionModeCtx : ctx.transactionMode()) {
        transactionModes.add(this.visitTransactionMode(transactionModeCtx));
      }
    }
    return RelationalAlgebraExpressionFactory.makeStartTransaction(transactionModes);
  }

  @Override
  public BeginWork visitBeginWork(MySqlParser.BeginWorkContext ctx) {
    if (ctx == null) {
      return null;
    }
    return RelationalAlgebraExpressionFactory.makeBeginWork();
  }

  @Override
  public CommitWork visitCommitWork(MySqlParser.CommitWorkContext ctx) {
    if (ctx == null) {
      return null;
    }

    Boolean chain = null;
    if (ctx.CHAIN() != null) {
      if (ctx.nochain != null) {
        chain = Boolean.FALSE;
      } else {
        chain = Boolean.TRUE;
      }
    }
    Boolean release = null;
    if (ctx.RELEASE() != null) {
      if (ctx.norelease != null) {
        release = Boolean.FALSE;
      } else {
        release = Boolean.TRUE;
      }
    }
    return RelationalAlgebraExpressionFactory.makeCommitWork(chain, release);
  }

  @Override
  public RollbackWork visitRollbackWork(MySqlParser.RollbackWorkContext ctx) {
    if (ctx == null) {
      return null;
    }

    Boolean chain = null;
    if (ctx.CHAIN() != null) {
      if (ctx.nochain != null) {
        chain = Boolean.FALSE;
      } else {
        chain = Boolean.TRUE;
      }
    }
    Boolean release = null;
    if (ctx.RELEASE() != null) {
      if (ctx.norelease != null) {
        release = Boolean.FALSE;
      } else {
        release = Boolean.TRUE;
      }
    }
    return RelationalAlgebraExpressionFactory.makeRollbackWork(chain, release);
  }

  @Override
  public SavepointStatement visitSavepointStatement(MySqlParser.SavepointStatementContext ctx) {
    if (ctx == null) {
      return null;
    }

    Uid uid = this.visitUid(ctx.uid());
    return RelationalAlgebraExpressionFactory.makeSavepointStatement(uid);
  }

  @Override
  public RollbackStatement visitRollbackStatement(MySqlParser.RollbackStatementContext ctx) {
    if (ctx == null) {
      return null;
    }

    Uid uid = this.visitUid(ctx.uid());
    return RelationalAlgebraExpressionFactory.makeRollbackStatement(uid);
  }

  @Override
  public ReleaseStatement visitReleaseStatement(MySqlParser.ReleaseStatementContext ctx) {
    if (ctx == null) {
      return null;
    }

    Uid uid = this.visitUid(ctx.uid());
    return RelationalAlgebraExpressionFactory.makeReleaseStatement(uid);
  }

  @Override
  public LockTables visitLockTables(MySqlParser.LockTablesContext ctx) {
    if (ctx == null) {
      return null;
    }

    List<LockTableElement> lockTableElements = Lists.newArrayList();
    for (MySqlParser.LockTableElementContext lockTableElementCtx : ctx.lockTableElement()) {
      lockTableElements.add(this.visitLockTableElement(lockTableElementCtx));
    }
    return RelationalAlgebraExpressionFactory.makeLockTables(lockTableElements);
  }

  @Override
  public UnlockTables visitUnlockTables(MySqlParser.UnlockTablesContext ctx) {
    if (ctx == null) {
      return null;
    }
    return RelationalAlgebraExpressionFactory.makeUnlockTables();
  }

  @Override
  public SetAutocommitStatement
      visitSetAutocommitStatement(MySqlParser.SetAutocommitStatementContext ctx) {
    if (ctx == null) {
      return null;
    }

    boolean autocommitValue;
    if ("0".equals(ctx.autocommitValue.getText())) {
      autocommitValue = false;
    } else {
      autocommitValue = true;
    }

    return RelationalAlgebraExpressionFactory.makeSetAutocommitStatement(autocommitValue);
  }

  @Override
  public SetTransactionStatement
      visitSetTransactionStatement(MySqlParser.SetTransactionStatementContext ctx) {
    if (ctx == null) {
      return null;
    }

    SetTransactionStatement.TransactionContextEnum transactionContext = null;
    if (ctx.transactionContext != null) {
      transactionContext = SetTransactionStatement.TransactionContextEnum
          .valueOf(ctx.transactionContext.getText().toUpperCase());
    }
    List<TransactionOption> transactionOptions = Lists.newArrayList();
    for (MySqlParser.TransactionOptionContext transactionOptionCtx : ctx.transactionOption()) {
      transactionOptions.add(this.visitTransactionOption(transactionOptionCtx));
    }
    return RelationalAlgebraExpressionFactory.makeSetTransactionStatement(transactionContext,
      transactionOptions);
  }

  @Override
  public TransactionStatement.TransactionModeEnum
      visitTransactionMode(MySqlParser.TransactionModeContext ctx) {
    if (ctx == null) {
      return null;
    }

    if (ctx.WITH() != null) {
      return TransactionStatement.TransactionModeEnum.WITH_CONSISTENT_SNAPSHOT;
    } else if (ctx.WRITE() != null) {
      return TransactionStatement.TransactionModeEnum.READ_WRITE;
    } else if (ctx.ONLY() != null) {
      return TransactionStatement.TransactionModeEnum.READ_ONLY;
    } else {
      throw ParserError.make(ctx);
    }
  }

  @Override
  public LockTableElement visitLockTableElement(MySqlParser.LockTableElementContext ctx) {
    if (ctx == null) {
      return null;
    }

    TableName tableName = this.visitTableName(ctx.tableName());
    Uid uid = this.visitUid(ctx.uid());
    LockAction lockAction = this.visitLockAction(ctx.lockAction());
    return RelationalAlgebraExpressionFactory.makeLockTableElement(tableName, uid, lockAction);
  }

  @Override
  public LockAction visitLockAction(MySqlParser.LockActionContext ctx) {
    if (ctx == null) {
      return null;
    }

    LockAction.Type type = null;
    Boolean local = null;
    Boolean lowPriority = null;
    if (ctx.READ() != null) {
      type = LockAction.Type.READ;
      if (ctx.LOCAL() != null) {
        local = Boolean.TRUE;
      }
    } else if (ctx.WRITE() != null) {
      type = LockAction.Type.WRITE;
      if (ctx.LOW_PRIORITY() != null) {
        lowPriority = Boolean.TRUE;
      }
    } else {
      throw ParserError.make(ctx);
    }

    return RelationalAlgebraExpressionFactory.makeLockAction(type, local, lowPriority);
  }

  @Override
  public TransactionOption visitTransactionOption(MySqlParser.TransactionOptionContext ctx) {
    if (ctx == null) {
      return null;
    }

    TransactionOption.Type type = null;
    TransactionStatement.TransactionLevelEnum transactionLevel = null;
    if (ctx.ISOLATION() != null) {
      type = TransactionOption.Type.ISOLATION_LEVEL;
      transactionLevel = this.visitTransactionLevel(ctx.transactionLevel());
    } else if (ctx.WRITE() != null) {
      type = TransactionOption.Type.READ_WRITE;
    } else if (ctx.ONLY() != null) {
      type = TransactionOption.Type.READ_ONLY;
    } else {
      throw ParserError.make(ctx);
    }

    return RelationalAlgebraExpressionFactory.makeTransactionOption(type, transactionLevel);

  }

  @Override
  public TransactionStatement.TransactionLevelEnum
      visitTransactionLevel(MySqlParser.TransactionLevelContext ctx) {
    if (ctx == null) {
      return null;
    }

    if (ctx.REPEATABLE() != null) {
      return TransactionStatement.TransactionLevelEnum.REPEATABLE_READ;
    } else if (ctx.COMMITTED() != null) {
      return TransactionStatement.TransactionLevelEnum.READ_COMMITTED;
    } else if (ctx.UNCOMMITTED() != null) {
      return TransactionStatement.TransactionLevelEnum.READ_UNCOMMITTED;
    } else if (ctx.SERIALIZABLE() != null) {
      return TransactionStatement.TransactionLevelEnum.SERIALIZABLE;
    } else {
      throw ParserError.make(ctx);
    }

  }

  @Override
  public ChangeMaster visitChangeMaster(MySqlParser.ChangeMasterContext ctx) {
    if (ctx == null) {
      return null;
    }

    List<MasterOption> masterOptions = Lists.newArrayList();
    for (MySqlParser.MasterOptionContext masterOptionCtx : ctx.masterOption()) {
      masterOptions.add(this.visitMasterOption(masterOptionCtx));
    }
    ChannelOption channelOption = this.visitChannelOption(ctx.channelOption());
    return RelationalAlgebraExpressionFactory.makeChangeMaster(masterOptions, channelOption);
  }

  @Override
  public ChangeReplicationFilter
      visitChangeReplicationFilter(MySqlParser.ChangeReplicationFilterContext ctx) {
    if (ctx == null) {
      return null;
    }

    List<ReplicationFilter> replicationFilters = Lists.newArrayList();
    for (MySqlParser.ReplicationFilterContext replicationFilterCtx : ctx.replicationFilter()) {
      replicationFilters.add(this.visitReplicationFilter(replicationFilterCtx));
    }
    return RelationalAlgebraExpressionFactory.makeChangeReplicationFilter(replicationFilters);
  }

  @Override
  public PurgeBinaryLogs visitPurgeBinaryLogs(MySqlParser.PurgeBinaryLogsContext ctx) {
    if (ctx == null) {
      return null;
    }

    PurgeBinaryLogs.PurgeFormatEnum purgeFormat =
        PurgeBinaryLogs.PurgeFormatEnum.valueOf(ctx.purgeFormat.getText().toUpperCase());
    PurgeBinaryLogs.Type type = null;
    String typeValue = null;
    if (ctx.TO() != null) {
      type = PurgeBinaryLogs.Type.TO_FILE;
      typeValue = ctx.fileName.getText();
    } else if (ctx.BEFORE() != null) {
      type = PurgeBinaryLogs.Type.BEFORE_TIME;
      typeValue = ctx.timeValue.getText();
    } else {
      throw ParserError.make(ctx);
    }
    return RelationalAlgebraExpressionFactory.makePurgeBinaryLogs(purgeFormat, type, typeValue);
  }

  @Override
  public ResetMaster visitResetMaster(MySqlParser.ResetMasterContext ctx) {
    if (ctx == null) {
      return null;
    }
    return RelationalAlgebraExpressionFactory.makeResetMaster();
  }

  @Override
  public ResetSlave visitResetSlave(MySqlParser.ResetSlaveContext ctx) {
    if (ctx == null) {
      return null;
    }

    Boolean all = null;
    if (ctx.ALL() != null) {
      all = Boolean.TRUE;
    }
    ChannelOption channelOption = this.visitChannelOption(ctx.channelOption());
    return RelationalAlgebraExpressionFactory.makeResetSlave(all, channelOption);
  }

  @Override
  public StartSlave visitStartSlave(MySqlParser.StartSlaveContext ctx) {
    if (ctx == null) {
      return null;
    }
    List<ReplicationStatement.ThreadTypeEnum> threadTypes = Lists.newArrayList();
    if (CollectionUtils.isNotEmpty(ctx.threadType())) {
      for (MySqlParser.ThreadTypeContext threadTypeCtx : ctx.threadType()) {
        threadTypes.add(this.visitThreadType(threadTypeCtx));
      }
    }
    UntilOption untilOption = this.visitUntilOption(ctx.untilOption());
    List<ConnectionOption> connectionOptions = Lists.newArrayList();
    if (CollectionUtils.isNotEmpty(ctx.connectionOption())) {
      for (MySqlParser.ConnectionOptionContext connectionOptionCtx : ctx.connectionOption()) {
        connectionOptions.add(this.visitConnectionOption(connectionOptionCtx));
      }
    }
    ChannelOption channelOption = this.visitChannelOption(ctx.channelOption());
    return RelationalAlgebraExpressionFactory.makeStartSlave(threadTypes, untilOption,
      connectionOptions, channelOption);
  }

  @Override
  public StopSlave visitStopSlave(MySqlParser.StopSlaveContext ctx) {
    if (ctx == null) {
      return null;
    }

    List<ReplicationStatement.ThreadTypeEnum> threadTypes = Lists.newArrayList();
    if (CollectionUtils.isNotEmpty(ctx.threadType())) {
      for (MySqlParser.ThreadTypeContext threadTypeCtx : ctx.threadType()) {
        threadTypes.add(this.visitThreadType(threadTypeCtx));
      }
    }
    return RelationalAlgebraExpressionFactory.makeStopSlave(threadTypes);
  }

  @Override
  public StartGroupReplication
      visitStartGroupReplication(MySqlParser.StartGroupReplicationContext ctx) {
    if (ctx == null) {
      return null;
    }
    return RelationalAlgebraExpressionFactory.makeStartGroupReplication();
  }

  @Override
  public StopGroupReplication
      visitStopGroupReplication(MySqlParser.StopGroupReplicationContext ctx) {
    if (ctx == null) {
      return null;
    }
    return RelationalAlgebraExpressionFactory.makeStopGroupReplication();
  }

  public MasterOption visitMasterOption(MySqlParser.MasterOptionContext ctx) {
    if (ctx == null) {
      return null;
    }

    if (ctx instanceof MySqlParser.MasterStringOptionContext) {
      return this.visitMasterStringOption((MySqlParser.MasterStringOptionContext) ctx);
    } else if (ctx instanceof MySqlParser.MasterDecimalOptionContext) {
      return this.visitMasterDecimalOption((MySqlParser.MasterDecimalOptionContext) ctx);
    } else if (ctx instanceof MySqlParser.MasterBoolOptionContext) {
      return this.visitMasterBoolOption((MySqlParser.MasterBoolOptionContext) ctx);
    } else if (ctx instanceof MySqlParser.MasterRealOptionContext) {
      return this.visitMasterRealOption((MySqlParser.MasterRealOptionContext) ctx);
    } else if (ctx instanceof MySqlParser.MasterUidListOptionContext) {
      return this.visitMasterUidListOption((MySqlParser.MasterUidListOptionContext) ctx);
    } else {
      throw ParserError.make(ctx);
    }
  }

  @Override
  public MasterStringOption visitMasterStringOption(MySqlParser.MasterStringOptionContext ctx) {
    if (ctx == null) {
      return null;
    }

    ReplicationStatement.StringMasterOptionEnum stringMasterOption =
        this.visitStringMasterOption(ctx.stringMasterOption());
    String value = ctx.STRING_LITERAL().getText();
    return RelationalAlgebraExpressionFactory.makeMasterStringOption(stringMasterOption, value);
  }

  @Override
  public MasterDecimalOption visitMasterDecimalOption(MySqlParser.MasterDecimalOptionContext ctx) {
    if (ctx == null) {
      return null;
    }

    ReplicationStatement.DecimalMasterOptionEnum decimalMasterOption =
        this.visitDecimalMasterOption(ctx.decimalMasterOption());
    DecimalLiteral value = this.visitDecimalLiteral(ctx.decimalLiteral());

    return RelationalAlgebraExpressionFactory.makeMasterDecimalOption(decimalMasterOption, value);
  }

  @Override
  public MasterBoolOption visitMasterBoolOption(MySqlParser.MasterBoolOptionContext ctx) {
    if (ctx == null) {
      return null;
    }

    ReplicationStatement.BoolMasterOptionEnum boolMasterOption =
        this.visitBoolMasterOption(ctx.boolMasterOption());
    boolean value;
    if ("0".equals(ctx.boolVal.getText())) {
      value = false;
    } else if ("1".equals(ctx.boolVal.getText())) {
      value = true;
    } else {
      throw ParserError.make(ctx);
    }

    return RelationalAlgebraExpressionFactory.makeMasterBoolOption(boolMasterOption, value);
  }

  @Override
  public MasterRealOption visitMasterRealOption(MySqlParser.MasterRealOptionContext ctx) {
    if (ctx == null) {
      return null;
    }

    String realLiteral = ctx.REAL_LITERAL().getText();
    return RelationalAlgebraExpressionFactory.makeMasterRealOption(realLiteral);
  }

  @Override
  public MasterUidListOption visitMasterUidListOption(MySqlParser.MasterUidListOptionContext ctx) {
    if (ctx == null) {
      return null;
    }

    List<Uid> uids = Lists.newArrayList();
    for (MySqlParser.UidContext uidCtx : ctx.uid()) {
      uids.add(this.visitUid(uidCtx));
    }
    return RelationalAlgebraExpressionFactory.makeMasterUidListOption(uids);
  }

  @Override
  public ReplicationStatement.StringMasterOptionEnum
      visitStringMasterOption(MySqlParser.StringMasterOptionContext ctx) {
    if (ctx == null) {
      return null;
    }
    return ReplicationStatement.StringMasterOptionEnum.valueOf(ctx.getText().toUpperCase());
  }

  @Override
  public ReplicationStatement.DecimalMasterOptionEnum
      visitDecimalMasterOption(MySqlParser.DecimalMasterOptionContext ctx) {
    if (ctx == null) {
      return null;
    }
    return ReplicationStatement.DecimalMasterOptionEnum.valueOf(ctx.getText().toUpperCase());
  }

  @Override
  public ReplicationStatement.BoolMasterOptionEnum
      visitBoolMasterOption(MySqlParser.BoolMasterOptionContext ctx) {
    if (ctx == null) {
      return null;
    }
    return ReplicationStatement.BoolMasterOptionEnum.valueOf(ctx.getText().toUpperCase());
  }

  @Override
  public ChannelOption visitChannelOption(MySqlParser.ChannelOptionContext ctx) {
    if (ctx == null) {
      return null;
    }

    String channel = ctx.STRING_LITERAL().getText();
    return RelationalAlgebraExpressionFactory.makeChannelOption(channel);
  }

  public ReplicationFilter visitReplicationFilter(MySqlParser.ReplicationFilterContext ctx) {
    if (ctx == null) {
      return null;
    }

    if (ctx instanceof MySqlParser.DoDbReplicationContext) {
      return this.visitDoDbReplication((MySqlParser.DoDbReplicationContext) ctx);
    } else if (ctx instanceof MySqlParser.IgnoreDbReplicationContext) {
      return this.visitIgnoreDbReplication((MySqlParser.IgnoreDbReplicationContext) ctx);
    } else if (ctx instanceof MySqlParser.DoTableReplicationContext) {
      return this.visitDoTableReplication((MySqlParser.DoTableReplicationContext) ctx);
    } else if (ctx instanceof MySqlParser.IgnoreTableReplicationContext) {
      return this.visitIgnoreTableReplication((MySqlParser.IgnoreTableReplicationContext) ctx);
    } else if (ctx instanceof MySqlParser.WildDoTableReplicationContext) {
      return this.visitWildDoTableReplication((MySqlParser.WildDoTableReplicationContext) ctx);
    } else if (ctx instanceof MySqlParser.WildIgnoreTableReplicationContext) {
      return this
          .visitWildIgnoreTableReplication((MySqlParser.WildIgnoreTableReplicationContext) ctx);
    } else if (ctx instanceof MySqlParser.RewriteDbReplicationContext) {
      return this.visitRewriteDbReplication((MySqlParser.RewriteDbReplicationContext) ctx);
    } else {
      throw ParserError.make(ctx);
    }

  }

  @Override
  public DoDbReplication visitDoDbReplication(MySqlParser.DoDbReplicationContext ctx) {
    if (ctx == null) {
      return null;
    }

    UidList uidList = this.visitUidList(ctx.uidList());
    return RelationalAlgebraExpressionFactory.makeDoDbReplication(uidList);
  }

  @Override
  public IgnoreDbReplication visitIgnoreDbReplication(MySqlParser.IgnoreDbReplicationContext ctx) {
    if (ctx == null) {
      return null;
    }

    UidList uidList = this.visitUidList(ctx.uidList());
    return RelationalAlgebraExpressionFactory.makeIgnoreDbReplication(uidList);
  }

  @Override
  public DoTableReplication visitDoTableReplication(MySqlParser.DoTableReplicationContext ctx) {
    if (ctx == null) {
      return null;
    }

    Tables tables = this.visitTables(ctx.tables());
    return RelationalAlgebraExpressionFactory.makeDoTableReplication(tables);
  }

  @Override
  public IgnoreTableReplication
      visitIgnoreTableReplication(MySqlParser.IgnoreTableReplicationContext ctx) {
    if (ctx == null) {
      return null;
    }

    Tables tables = this.visitTables(ctx.tables());
    return RelationalAlgebraExpressionFactory.makeIgnoreTableReplication(tables);
  }

  @Override
  public WildDoTableReplication
      visitWildDoTableReplication(MySqlParser.WildDoTableReplicationContext ctx) {
    if (ctx == null) {
      return null;
    }

    SimpleStrings simpleStrings = this.visitSimpleStrings(ctx.simpleStrings());
    return RelationalAlgebraExpressionFactory.makeWildDoTableReplication(simpleStrings);
  }

  @Override
  public WildIgnoreTableReplication
      visitWildIgnoreTableReplication(MySqlParser.WildIgnoreTableReplicationContext ctx) {
    if (ctx == null) {
      return null;
    }

    SimpleStrings simpleStrings = this.visitSimpleStrings(ctx.simpleStrings());
    return RelationalAlgebraExpressionFactory.makeWildIgnoreTableReplication(simpleStrings);
  }

  @Override
  public RewriteDbReplication
      visitRewriteDbReplication(MySqlParser.RewriteDbReplicationContext ctx) {
    if (ctx == null) {
      return null;
    }

    List<TablePair> tablePairs = Lists.newArrayList();
    for (MySqlParser.TablePairContext tablePairCtx : ctx.tablePair()) {
      tablePairs.add(this.visitTablePair(tablePairCtx));
    }
    return RelationalAlgebraExpressionFactory.makeRewriteDbReplication(tablePairs);
  }

  @Override
  public TablePair visitTablePair(MySqlParser.TablePairContext ctx) {
    if (ctx == null) {
      return null;
    }

    TableName firstTable = this.visitTableName(ctx.firstTable);
    TableName secondTable = this.visitTableName(ctx.secondTable);
    return RelationalAlgebraExpressionFactory.makeTablePair(firstTable, secondTable);
  }

  @Override
  public ReplicationStatement.ThreadTypeEnum visitThreadType(MySqlParser.ThreadTypeContext ctx) {
    if (ctx == null) {
      return null;
    }

    if (ctx.IO_THREAD() != null) {
      return ReplicationStatement.ThreadTypeEnum.IO_THREAD;
    } else if (ctx.SQL_THREAD() != null) {
      return ReplicationStatement.ThreadTypeEnum.SQL_THREAD;
    } else {
      throw ParserError.make(ctx);
    }

  }

  public UntilOption visitUntilOption(MySqlParser.UntilOptionContext ctx) {
    if (ctx == null) {
      return null;
    }
    if (ctx instanceof MySqlParser.GtidsUntilOptionContext) {
      return this.visitGtidsUntilOption((MySqlParser.GtidsUntilOptionContext) ctx);
    } else if (ctx instanceof MySqlParser.MasterLogUntilOptionContext) {
      return this.visitMasterLogUntilOption((MySqlParser.MasterLogUntilOptionContext) ctx);
    } else if (ctx instanceof MySqlParser.RelayLogUntilOptionContext) {
      return this.visitRelayLogUntilOption((MySqlParser.RelayLogUntilOptionContext) ctx);
    } else if (ctx instanceof MySqlParser.SqlGapsUntilOptionContext) {
      return this.visitSqlGapsUntilOption((MySqlParser.SqlGapsUntilOptionContext) ctx);
    } else {
      throw ParserError.make(ctx);
    }
  }

  @Override
  public GtidsUntilOption visitGtidsUntilOption(MySqlParser.GtidsUntilOptionContext ctx) {
    if (ctx == null) {
      return null;
    }

    GtidsUntilOption.Type type = GtidsUntilOption.Type.valueOf(ctx.gtids.getText().toUpperCase());
    GtuidSet gtuidSet = this.visitGtuidSet(ctx.gtuidSet());

    return RelationalAlgebraExpressionFactory.makeGtidsUntilOption(type, gtuidSet);
  }

  @Override
  public MasterLogUntilOption
      visitMasterLogUntilOption(MySqlParser.MasterLogUntilOptionContext ctx) {
    if (ctx == null) {
      return null;
    }

    String logFile = ctx.STRING_LITERAL().getText();
    DecimalLiteral pos = this.visitDecimalLiteral(ctx.decimalLiteral());
    return RelationalAlgebraExpressionFactory.makeMasterLogUntilOption(logFile, pos);
  }

  @Override
  public RelayLogUntilOption visitRelayLogUntilOption(MySqlParser.RelayLogUntilOptionContext ctx) {
    if (ctx == null) {
      return null;
    }

    String logFile = ctx.STRING_LITERAL().getText();
    DecimalLiteral pos = this.visitDecimalLiteral(ctx.decimalLiteral());
    return RelationalAlgebraExpressionFactory.makeRelayLogUntilOption(logFile, pos);
  }

  @Override
  public SqlGapsUntilOption visitSqlGapsUntilOption(MySqlParser.SqlGapsUntilOptionContext ctx) {
    if (ctx == null) {
      return null;
    }
    return RelationalAlgebraExpressionFactory.makeSqlGapsUntilOption();
  }

  public ConnectionOption visitConnectionOption(MySqlParser.ConnectionOptionContext ctx) {
    if (ctx == null) {
      return null;
    }
    if (ctx instanceof MySqlParser.UserConnectionOptionContext) {
      return this.visitUserConnectionOption((MySqlParser.UserConnectionOptionContext) ctx);
    } else if (ctx instanceof MySqlParser.PasswordConnectionOptionContext) {
      return this.visitPasswordConnectionOption((MySqlParser.PasswordConnectionOptionContext) ctx);
    } else if (ctx instanceof MySqlParser.DefaultAuthConnectionOptionContext) {
      return this
          .visitDefaultAuthConnectionOption((MySqlParser.DefaultAuthConnectionOptionContext) ctx);
    } else if (ctx instanceof MySqlParser.PluginDirConnectionOptionContext) {
      return this
          .visitPluginDirConnectionOption((MySqlParser.PluginDirConnectionOptionContext) ctx);
    } else {
      throw ParserError.make(ctx);
    }

  }

  @Override
  public ConnectionOption visitUserConnectionOption(MySqlParser.UserConnectionOptionContext ctx) {
    if (ctx == null) {
      return null;
    }
    return RelationalAlgebraExpressionFactory.makeConnectionOption(ConnectionOption.Type.USER,
      ctx.conOptUser.getText());
  }

  @Override
  public ConnectionOption
      visitPasswordConnectionOption(MySqlParser.PasswordConnectionOptionContext ctx) {
    if (ctx == null) {
      return null;
    }
    return RelationalAlgebraExpressionFactory.makeConnectionOption(ConnectionOption.Type.PASSWORD,
      ctx.conOptPassword.getText());
  }

  @Override
  public ConnectionOption
      visitDefaultAuthConnectionOption(MySqlParser.DefaultAuthConnectionOptionContext ctx) {
    if (ctx == null) {
      return null;
    }
    return RelationalAlgebraExpressionFactory
        .makeConnectionOption(ConnectionOption.Type.DEFAULT_AUTH, ctx.conOptDefAuth.getText());
  }

  @Override
  public ConnectionOption
      visitPluginDirConnectionOption(MySqlParser.PluginDirConnectionOptionContext ctx) {
    if (ctx == null) {
      return null;
    }
    return RelationalAlgebraExpressionFactory.makeConnectionOption(ConnectionOption.Type.PLUGIN_DIR,
      ctx.conOptPluginDir.getText());
  }

  @Override
  public GtuidSet visitGtuidSet(MySqlParser.GtuidSetContext ctx) {
    if (ctx == null) {
      return null;
    }

    List<UuidSet> uuidSets = Lists.newArrayList();
    if (CollectionUtils.isNotEmpty(ctx.uuidSet())) {
      for (MySqlParser.UuidSetContext uuidSetCtx : ctx.uuidSet()) {
        uuidSets.add(this.visitUuidSet(uuidSetCtx));
      }
    }
    String stringLiteral = null;
    if (ctx.STRING_LITERAL() != null) {
      stringLiteral = ctx.STRING_LITERAL().getText();
    }

    return RelationalAlgebraExpressionFactory.makeGtuidSet(uuidSets, stringLiteral);
  }

  @Override
  public XaStartTransaction visitXaStartTransaction(MySqlParser.XaStartTransactionContext ctx) {
    if (ctx == null) {
      return null;
    }
    XaStartTransaction.XaStartEnum xaStart =
        XaStartTransaction.XaStartEnum.valueOf(ctx.xaStart.getText().toUpperCase());
    Xid xid = this.visitXid(ctx.xid());
    XaStartTransaction.XaActionEnum xaAction = null;
    if (ctx.xaAction != null) {
      xaAction = XaStartTransaction.XaActionEnum.valueOf(ctx.xaAction.getText().toUpperCase());
    }

    return RelationalAlgebraExpressionFactory.makeXaStartTransaction(xaStart, xid, xaAction);
  }

  @Override
  public XaEndTransaction visitXaEndTransaction(MySqlParser.XaEndTransactionContext ctx) {
    if (ctx == null) {
      return null;
    }

    Xid xid = this.visitXid(ctx.xid());
    Boolean suspend = null;
    if (ctx.SUSPEND() != null) {
      suspend = Boolean.TRUE;
    }
    Boolean forMigrate = null;
    if (ctx.MIGRATE() != null) {
      forMigrate = Boolean.TRUE;
    }

    return RelationalAlgebraExpressionFactory.makeXaEndTransaction(xid, suspend, forMigrate);
  }

  @Override
  public XaPrepareStatement visitXaPrepareStatement(MySqlParser.XaPrepareStatementContext ctx) {
    if (ctx == null) {
      return null;
    }
    return RelationalAlgebraExpressionFactory.makeXaPrepareStatement(this.visitXid(ctx.xid()));
  }

  @Override
  public XaCommitWork visitXaCommitWork(MySqlParser.XaCommitWorkContext ctx) {
    if (ctx == null) {
      return null;
    }

    Xid xid = this.visitXid(ctx.xid());
    Boolean onePhase = null;
    if (ctx.PHASE() != null) {
      onePhase = Boolean.TRUE;
    }
    return RelationalAlgebraExpressionFactory.makeXaCommitWork(xid, onePhase);
  }

  @Override
  public XaRollbackWork visitXaRollbackWork(MySqlParser.XaRollbackWorkContext ctx) {
    if (ctx == null) {
      return null;
    }
    return RelationalAlgebraExpressionFactory.makeXaRollbackWork(this.visitXid(ctx.xid()));
  }

  @Override
  public XaRecoverWork visitXaRecoverWork(MySqlParser.XaRecoverWorkContext ctx) {
    if (ctx == null) {
      return null;
    }
    return RelationalAlgebraExpressionFactory.makeXaRecoverWork(this.visitXid(ctx.xid()));
  }

  @Override
  public PrepareStatement visitPrepareStatement(MySqlParser.PrepareStatementContext ctx) {
    if (ctx == null) {
      return null;
    }

    Uid uid = this.visitUid(ctx.uid());
    PrepareStatement.Type type = null;
    String typeValue = null;
    if (ctx.query != null) {
      type = PrepareStatement.Type.STRING_LITERAL;
      typeValue = ctx.STRING_LITERAL().getText();
    } else if (ctx.variable != null) {
      type = PrepareStatement.Type.LOCAL_ID;
      typeValue = ctx.LOCAL_ID().getText();
    } else {
      throw ParserError.make(ctx);
    }

    return RelationalAlgebraExpressionFactory.makePrepareStatement(uid, type, typeValue);
  }

  @Override
  public ExecuteStatement visitExecuteStatement(MySqlParser.ExecuteStatementContext ctx) {
    if (ctx == null) {
      return null;
    }
    Uid uid = this.visitUid(ctx.uid());
    UserVariables userVariables = this.visitUserVariables(ctx.userVariables());
    return RelationalAlgebraExpressionFactory.makeExecuteStatement(uid, userVariables);
  }

  @Override
  public DeallocatePrepare visitDeallocatePrepare(MySqlParser.DeallocatePrepareContext ctx) {
    if (ctx == null) {
      return null;
    }

    DeallocatePrepare.DropFormatEnum dropFormat =
        DeallocatePrepare.DropFormatEnum.valueOf(ctx.dropFormat.getText().toUpperCase());
    Uid uid = this.visitUid(ctx.uid());
    return RelationalAlgebraExpressionFactory.makeDeallocatePrepare(dropFormat, uid);
  }

  @Override
  public RoutineBody visitRoutineBody(MySqlParser.RoutineBodyContext ctx) {
    if (ctx == null) {
      return null;
    }

    BlockStatement blockStatement = this.visitBlockStatement(ctx.blockStatement());
    SqlStatement sqlStatement = this.visitSqlStatement(ctx.sqlStatement());
    return RelationalAlgebraExpressionFactory.makeRoutineBody(blockStatement, sqlStatement);
  }

  @Override
  public BlockStatement visitBlockStatement(MySqlParser.BlockStatementContext ctx) {
    if (ctx == null) {
      return null;
    }
    Uid beginUid = null;
    Uid endUid = null;
    if (CollectionUtils.isNotEmpty(ctx.uid())) {
      List<Uid> uids = Lists.newArrayList();
      for (MySqlParser.UidContext uidCtx : ctx.uid()) {
        uids.add(this.visitUid(uidCtx));
      }
      if (uids.size() >= 1) {
        beginUid = uids.get(0);
      }
      if (uids.size() >= 2) {
        endUid = uids.get(1);
      }
    }
    List<DeclareVariable> declareVariables = Lists.newArrayList();
    if (CollectionUtils.isNotEmpty(ctx.declareVariable())) {
      for (MySqlParser.DeclareVariableContext declareVariableCtx : ctx.declareVariable()) {
        declareVariables.add(this.visitDeclareVariable(declareVariableCtx));
      }
    }
    List<DeclareCondition> declareConditions = Lists.newArrayList();
    if (CollectionUtils.isNotEmpty(ctx.declareCondition())) {
      for (MySqlParser.DeclareConditionContext declareConditionCtx : ctx.declareCondition()) {
        declareConditions.add(this.visitDeclareCondition(declareConditionCtx));
      }
    }
    List<DeclareCursor> declareCursors = Lists.newArrayList();
    if (CollectionUtils.isNotEmpty(ctx.declareCursor())) {
      for (MySqlParser.DeclareCursorContext declareCursorCtx : ctx.declareCursor()) {
        declareCursors.add(this.visitDeclareCursor(declareCursorCtx));
      }
    }
    List<DeclareHandler> declareHandlers = Lists.newArrayList();
    if (CollectionUtils.isNotEmpty(ctx.declareHandler())) {
      for (MySqlParser.DeclareHandlerContext declareHandlerCtx : ctx.declareHandler()) {
        declareHandlers.add(this.visitDeclareHandler(declareHandlerCtx));
      }
    }
    List<ProcedureSqlStatement> procedureSqlStatements = Lists.newArrayList();
    if (CollectionUtils.isNotEmpty(ctx.procedureSqlStatement())) {
      for (MySqlParser.ProcedureSqlStatementContext procedureSqlStatementCtx : ctx
          .procedureSqlStatement()) {
        procedureSqlStatements.add(this.visitProcedureSqlStatement(procedureSqlStatementCtx));
      }
    }
    return RelationalAlgebraExpressionFactory.makeBlockStatement(beginUid, declareVariables,
      declareConditions, declareCursors, declareHandlers, procedureSqlStatements, endUid);
  }

  @Override
  public CaseStatement visitCaseStatement(MySqlParser.CaseStatementContext ctx) {
    if (ctx == null) {
      return null;
    }

    Uid uid = this.visitUid(ctx.uid());
    Expression expression = this.visitExpression(ctx.expression());
    List<CaseAlternative> caseAlternatives = Lists.newArrayList();
    for (MySqlParser.CaseAlternativeContext caseAlternativeCtx : ctx.caseAlternative()) {
      caseAlternatives.add(this.visitCaseAlternative(caseAlternativeCtx));
    }
    List<ProcedureSqlStatement> procedureSqlStatements = Lists.newArrayList();
    if (CollectionUtils.isNotEmpty(ctx.procedureSqlStatement())) {
      for (MySqlParser.ProcedureSqlStatementContext procedureSqlStatementCtx : ctx
          .procedureSqlStatement()) {
        procedureSqlStatements.add(this.visitProcedureSqlStatement(procedureSqlStatementCtx));
      }
    }
    return RelationalAlgebraExpressionFactory.makeCaseStatement(uid, expression, caseAlternatives,
      procedureSqlStatements);
  }

  @Override
  public IfStatement visitIfStatement(MySqlParser.IfStatementContext ctx) {
    if (ctx == null) {
      return null;
    }

    Expression ifExpression = this.visitExpression(ctx.expression());
    List<ProcedureSqlStatement> thenStatements = Lists.newArrayList();
    for (MySqlParser.ProcedureSqlStatementContext procedureSqlStatementCtx : ctx.thenStatements) {
      thenStatements.add(this.visitProcedureSqlStatement(procedureSqlStatementCtx));
    }
    List<ElifAlternative> elifAlternatives = Lists.newArrayList();
    if (CollectionUtils.isNotEmpty(ctx.elifAlternative())) {
      for (MySqlParser.ElifAlternativeContext elifAlternativeCtx : ctx.elifAlternative()) {
        elifAlternatives.add(this.visitElifAlternative(elifAlternativeCtx));
      }
    }
    List<ProcedureSqlStatement> elseStatements = Lists.newArrayList();
    if (CollectionUtils.isNotEmpty(ctx.elseStatements)) {
      for (MySqlParser.ProcedureSqlStatementContext procedureSqlStatementCtx : ctx.elseStatements) {
        elseStatements.add(this.visitProcedureSqlStatement(procedureSqlStatementCtx));
      }
    }
    return RelationalAlgebraExpressionFactory.makeIfStatement(ifExpression, thenStatements,
      elifAlternatives, elseStatements);
  }

  @Override
  public IterateStatement visitIterateStatement(MySqlParser.IterateStatementContext ctx) {
    if (ctx == null) {
      return null;
    }
    return RelationalAlgebraExpressionFactory.makeIterateStatement(this.visitUid(ctx.uid()));
  }

  @Override
  public LeaveStatement visitLeaveStatement(MySqlParser.LeaveStatementContext ctx) {
    if (ctx == null) {
      return null;
    }
    return RelationalAlgebraExpressionFactory.makeLeaveStatement(this.visitUid(ctx.uid()));
  }

  @Override
  public LoopStatement visitLoopStatement(MySqlParser.LoopStatementContext ctx) {
    if (ctx == null) {
      return null;
    }

    Uid uid = null;
    Uid endLoopUid = null;
    if (CollectionUtils.isNotEmpty(ctx.uid())) {
      List<Uid> uids = Lists.newArrayList();
      for (MySqlParser.UidContext uidCtx : ctx.uid()) {
        uids.add(this.visitUid(uidCtx));
      }
      if (uids.size() >= 1) {
        uid = uids.get(0);
      }
      if (uids.size() >= 2) {
        endLoopUid = uids.get(1);
      }
    }
    List<ProcedureSqlStatement> procedureSqlStatements = Lists.newArrayList();
    for (MySqlParser.ProcedureSqlStatementContext procedureSqlStatementCtx : ctx
        .procedureSqlStatement()) {
      procedureSqlStatements.add(this.visitProcedureSqlStatement(procedureSqlStatementCtx));
    }
    return RelationalAlgebraExpressionFactory.makeLoopStatement(uid, procedureSqlStatements,
      endLoopUid);
  }

  @Override
  public RepeatStatement visitRepeatStatement(MySqlParser.RepeatStatementContext ctx) {
    if (ctx == null) {
      return null;
    }

    Uid uid = null;
    Uid endRepeatUid = null;
    if (CollectionUtils.isNotEmpty(ctx.uid())) {
      List<Uid> uids = Lists.newArrayList();
      for (MySqlParser.UidContext uidCtx : ctx.uid()) {
        uids.add(this.visitUid(uidCtx));
      }
      if (uids.size() >= 1) {
        uid = uids.get(0);
      }
      if (uids.size() >= 2) {
        endRepeatUid = uids.get(1);
      }
    }
    Expression untilExpression = this.visitExpression(ctx.expression());
    List<ProcedureSqlStatement> procedureSqlStatements = Lists.newArrayList();
    for (MySqlParser.ProcedureSqlStatementContext procedureSqlStatementCtx : ctx
        .procedureSqlStatement()) {
      procedureSqlStatements.add(this.visitProcedureSqlStatement(procedureSqlStatementCtx));
    }

    return RelationalAlgebraExpressionFactory.makeRepeatStatement(uid, procedureSqlStatements,
      untilExpression, endRepeatUid);
  }

  @Override
  public ReturnStatement visitReturnStatement(MySqlParser.ReturnStatementContext ctx) {
    if (ctx == null) {
      return null;
    }

    Expression expression = this.visitExpression(ctx.expression());
    return RelationalAlgebraExpressionFactory.makeReturnStatement(expression);
  }

  @Override
  public WhileStatement visitWhileStatement(MySqlParser.WhileStatementContext ctx) {
    if (ctx == null) {
      return null;
    }

    Uid uid = null;
    Uid endWhileUid = null;
    if (CollectionUtils.isNotEmpty(ctx.uid())) {
      List<Uid> uids = Lists.newArrayList();
      for (MySqlParser.UidContext uidCtx : ctx.uid()) {
        uids.add(this.visitUid(uidCtx));
      }
      if (uids.size() >= 1) {
        uid = uids.get(0);
      }
      if (uids.size() >= 2) {
        endWhileUid = uids.get(1);
      }
    }
    Expression whileExpression = this.visitExpression(ctx.expression());
    List<ProcedureSqlStatement> procedureSqlStatements = Lists.newArrayList();
    for (MySqlParser.ProcedureSqlStatementContext procedureSqlStatementCtx : ctx
        .procedureSqlStatement()) {
      procedureSqlStatements.add(this.visitProcedureSqlStatement(procedureSqlStatementCtx));
    }

    return RelationalAlgebraExpressionFactory.makeWhileStatement(uid, whileExpression,
      procedureSqlStatements, endWhileUid);
  }

  public CursorStatement visitCursorStatement(MySqlParser.CursorStatementContext ctx) {
    if (ctx == null) {
      return null;
    }
    if (ctx instanceof MySqlParser.CloseCursorContext) {
      return this.visitCloseCursor((MySqlParser.CloseCursorContext) ctx);
    } else if (ctx instanceof MySqlParser.FetchCursorContext) {
      return this.visitFetchCursor((MySqlParser.FetchCursorContext) ctx);
    } else if (ctx instanceof MySqlParser.OpenCursorContext) {
      return this.visitOpenCursor((MySqlParser.OpenCursorContext) ctx);
    } else {
      throw ParserError.make(ctx);
    }
  }

  @Override
  public CloseCursor visitCloseCursor(MySqlParser.CloseCursorContext ctx) {
    if (ctx == null) {
      return null;
    }
    return RelationalAlgebraExpressionFactory.makeCloseCursor(this.visitUid(ctx.uid()));
  }

  @Override
  public FetchCursor visitFetchCursor(MySqlParser.FetchCursorContext ctx) {
    if (ctx == null) {
      return null;
    }

    Boolean isNext = null;
    if (ctx.NEXT() != null) {
      isNext = Boolean.TRUE;
    }
    Uid uid = this.visitUid(ctx.uid());
    UidList uidList = this.visitUidList(ctx.uidList());

    return RelationalAlgebraExpressionFactory.makeFetchCursor(isNext, uid, uidList);
  }

  @Override
  public OpenCursor visitOpenCursor(MySqlParser.OpenCursorContext ctx) {
    if (ctx == null) {
      return null;
    }
    return RelationalAlgebraExpressionFactory.makeOpenCursor(this.visitUid(ctx.uid()));
  }

  @Override
  public DeclareVariable visitDeclareVariable(MySqlParser.DeclareVariableContext ctx) {
    if (ctx == null) {
      return null;
    }

    UidList uidList = this.visitUidList(ctx.uidList());
    DataType dataType = this.visitDataType(ctx.dataType());
    DefaultValue defaultValue = this.visitDefaultValue(ctx.defaultValue());
    return RelationalAlgebraExpressionFactory.makeDeclareVariable(uidList, dataType, defaultValue);
  }

  @Override
  public DeclareCondition visitDeclareCondition(MySqlParser.DeclareConditionContext ctx) {
    if (ctx == null) {
      return null;
    }

    Uid uid = this.visitUid(ctx.uid());
    DecimalLiteral decimalLiteral = this.visitDecimalLiteral(ctx.decimalLiteral());
    String sqlState = null;
    if (ctx.STRING_LITERAL() != null) {
      sqlState = ctx.STRING_LITERAL().getText();
    }

    return RelationalAlgebraExpressionFactory.makeDeclareCondition(uid, decimalLiteral, sqlState);
  }

  @Override
  public DeclareCursor visitDeclareCursor(MySqlParser.DeclareCursorContext ctx) {
    if (ctx == null) {
      return null;
    }

    Uid uid = this.visitUid(ctx.uid());
    SelectStatement selectStatement = this.visitSelectStatement(ctx.selectStatement());
    return RelationalAlgebraExpressionFactory.makeDeclareCursor(uid, selectStatement);
  }

  @Override
  public DeclareHandler visitDeclareHandler(MySqlParser.DeclareHandlerContext ctx) {
    if (ctx == null) {
      return null;
    }

    DeclareHandler.HandlerActionEnum handlerAction =
        DeclareHandler.HandlerActionEnum.valueOf(ctx.handlerAction.getText().toUpperCase());
    List<HandlerConditionValue> handlerConditionValues = Lists.newArrayList();
    for (MySqlParser.HandlerConditionValueContext handlerConditionValueCtx : ctx
        .handlerConditionValue()) {
      handlerConditionValues.add(this.visitHandlerConditionValue(handlerConditionValueCtx));
    }
    RoutineBody routineBody = this.visitRoutineBody(ctx.routineBody());

    return RelationalAlgebraExpressionFactory.makeDeclareHandler(handlerAction,
      handlerConditionValues, routineBody);
  }

  public HandlerConditionValue
      visitHandlerConditionValue(MySqlParser.HandlerConditionValueContext ctx) {
    if (ctx == null) {
      return null;
    }

    if (ctx instanceof MySqlParser.HandlerConditionCodeContext) {
      return this.visitHandlerConditionCode((MySqlParser.HandlerConditionCodeContext) ctx);
    } else if (ctx instanceof MySqlParser.HandlerConditionStateContext) {
      return this.visitHandlerConditionState((MySqlParser.HandlerConditionStateContext) ctx);
    } else if (ctx instanceof MySqlParser.HandlerConditionNameContext) {
      return this.visitHandlerConditionName((MySqlParser.HandlerConditionNameContext) ctx);
    } else if (ctx instanceof MySqlParser.HandlerConditionWarningContext) {
      return this.visitHandlerConditionWarning((MySqlParser.HandlerConditionWarningContext) ctx);
    } else if (ctx instanceof MySqlParser.HandlerConditionNotfoundContext) {
      return this.visitHandlerConditionNotfound((MySqlParser.HandlerConditionNotfoundContext) ctx);
    } else if (ctx instanceof MySqlParser.HandlerConditionExceptionContext) {
      return this
          .visitHandlerConditionException((MySqlParser.HandlerConditionExceptionContext) ctx);
    } else {
      throw ParserError.make(ctx);
    }
  }

  @Override
  public HandlerConditionCode
      visitHandlerConditionCode(MySqlParser.HandlerConditionCodeContext ctx) {
    if (ctx == null) {
      return null;
    }
    return RelationalAlgebraExpressionFactory
        .makeHandlerConditionCode(this.visitDecimalLiteral(ctx.decimalLiteral()));
  }

  @Override
  public HandlerConditionState
      visitHandlerConditionState(MySqlParser.HandlerConditionStateContext ctx) {
    if (ctx == null) {
      return null;
    }
    return RelationalAlgebraExpressionFactory
        .makeHandlerConditionState(ctx.STRING_LITERAL().getText());
  }

  @Override
  public HandlerConditionName
      visitHandlerConditionName(MySqlParser.HandlerConditionNameContext ctx) {
    if (ctx == null) {
      return null;
    }
    return RelationalAlgebraExpressionFactory.makeHandlerConditionName(this.visitUid(ctx.uid()));
  }

  @Override
  public HandlerConditionWarning
      visitHandlerConditionWarning(MySqlParser.HandlerConditionWarningContext ctx) {
    if (ctx == null) {
      return null;
    }
    return RelationalAlgebraExpressionFactory.makeHandlerConditionWarning();
  }

  @Override
  public HandlerConditionNotfound
      visitHandlerConditionNotfound(MySqlParser.HandlerConditionNotfoundContext ctx) {
    if (ctx == null) {
      return null;
    }
    return RelationalAlgebraExpressionFactory.makeHandlerConditionNotfound();
  }

  @Override
  public HandlerConditionException
      visitHandlerConditionException(MySqlParser.HandlerConditionExceptionContext ctx) {
    if (ctx == null) {
      return null;
    }
    return RelationalAlgebraExpressionFactory.makeHandlerConditionException();
  }

  @Override
  public ProcedureSqlStatement
      visitProcedureSqlStatement(MySqlParser.ProcedureSqlStatementContext ctx) {
    if (ctx == null) {
      return null;
    }
    CompoundStatement compoundStatement = this.visitCompoundStatement(ctx.compoundStatement());
    SqlStatement sqlStatement = this.visitSqlStatement(ctx.sqlStatement());

    return RelationalAlgebraExpressionFactory.makeProcedureSqlStatement(compoundStatement,
      sqlStatement);
  }

  @Override
  public CaseAlternative visitCaseAlternative(MySqlParser.CaseAlternativeContext ctx) {
    if (ctx == null) {
      return null;
    }

    Constant whenConstant = this.visitConstant(ctx.constant());
    Expression whenExpression = this.visitExpression(ctx.expression());
    List<ProcedureSqlStatement> procedureSqlStatements = Lists.newArrayList();
    for (MySqlParser.ProcedureSqlStatementContext procedureSqlStatementCtx : ctx
        .procedureSqlStatement()) {
      procedureSqlStatements.add(this.visitProcedureSqlStatement(procedureSqlStatementCtx));
    }
    return RelationalAlgebraExpressionFactory.makeCaseAlternative(whenConstant, whenExpression,
      procedureSqlStatements);
  }

  @Override
  public ElifAlternative visitElifAlternative(MySqlParser.ElifAlternativeContext ctx) {
    if (ctx == null) {
      return null;
    }

    Expression elseIfExpression = this.visitExpression(ctx.expression());
    List<ProcedureSqlStatement> procedureSqlStatements = Lists.newArrayList();
    for (MySqlParser.ProcedureSqlStatementContext procedureSqlStatementCtx : ctx
        .procedureSqlStatement()) {
      procedureSqlStatements.add(this.visitProcedureSqlStatement(procedureSqlStatementCtx));
    }
    return RelationalAlgebraExpressionFactory.makeElifAlternative(elseIfExpression,
      procedureSqlStatements);
  }

  public AlterUser visitAlterUser(MySqlParser.AlterUserContext ctx) {
    if (ctx == null) {
      return null;
    }
    if (ctx instanceof MySqlParser.AlterUserMysqlV56Context) {
      return this.visitAlterUserMysqlV56((MySqlParser.AlterUserMysqlV56Context) ctx);
    } else if (ctx instanceof MySqlParser.AlterUserMysqlV57Context) {
      return this.visitAlterUserMysqlV57((MySqlParser.AlterUserMysqlV57Context) ctx);
    } else {
      throw ParserError.make(ctx);
    }
  }

  @Override
  public AlterUserMysqlV56 visitAlterUserMysqlV56(MySqlParser.AlterUserMysqlV56Context ctx) {
    if (ctx == null) {
      return null;
    }

    List<UserSpecification> userSpecifications = Lists.newArrayList();
    for (MySqlParser.UserSpecificationContext userSpecificationCtx : ctx.userSpecification()) {
      userSpecifications.add(this.visitUserSpecification(userSpecificationCtx));
    }

    return RelationalAlgebraExpressionFactory.makeAlterUserMysqlV56(userSpecifications);
  }

  @Override
  public AlterUserMysqlV57 visitAlterUserMysqlV57(MySqlParser.AlterUserMysqlV57Context ctx) {
    if (ctx == null) {
      return null;
    }

    IfExists ifExists = this.visitIfExists(ctx.ifExists());
    List<UserAuthOption> userAuthOptions = Lists.newArrayList();
    for (MySqlParser.UserAuthOptionContext userAuthOptionCtx : ctx.userAuthOption()) {
      userAuthOptions.add(this.visitUserAuthOption(userAuthOptionCtx));
    }
    Boolean tlsNone = null;
    if (ctx.tlsNone != null) {
      tlsNone = Boolean.TRUE;
    }
    List<TlsOption> tlsOptions = Lists.newArrayList();
    if (CollectionUtils.isNotEmpty(ctx.tlsOption())) {
      for (MySqlParser.TlsOptionContext tlsOptionCtx : ctx.tlsOption()) {
        tlsOptions.add(this.visitTlsOption(tlsOptionCtx));
      }
    }
    List<UserResourceOption> userResourceOptions = Lists.newArrayList();
    if (CollectionUtils.isNotEmpty(ctx.userResourceOption())) {
      for (MySqlParser.UserResourceOptionContext userResourceOptionCtx : ctx.userResourceOption()) {
        userResourceOptions.add(this.visitUserResourceOption(userResourceOptionCtx));
      }
    }
    List<UserPasswordOption> userPasswordOptions = Lists.newArrayList();
    if (CollectionUtils.isNotEmpty(ctx.userPasswordOption())) {
      for (MySqlParser.UserPasswordOptionContext userPasswordOptionCtx : ctx.userPasswordOption()) {
        userPasswordOptions.add(this.visitUserPasswordOption(userPasswordOptionCtx));
      }
    }
    List<AdministrationStatement.UserLockOptionEnum> userLockOptions = Lists.newArrayList();
    if (CollectionUtils.isNotEmpty(ctx.userLockOption())) {
      for (MySqlParser.UserLockOptionContext userLockOptionCtx : ctx.userLockOption()) {
        userLockOptions.add(this.visitUserLockOption(userLockOptionCtx));
      }
    }

    return RelationalAlgebraExpressionFactory.makeAlterUserMysqlV57(ifExists, userAuthOptions,
      tlsNone, tlsOptions, userResourceOptions, userPasswordOptions, userLockOptions);
  }

  public CreateUser visitCreateUser(MySqlParser.CreateUserContext ctx) {
    if (ctx == null) {
      return null;
    }

    if (ctx instanceof MySqlParser.CreateUserMysqlV56Context) {
      return this.visitCreateUserMysqlV56((MySqlParser.CreateUserMysqlV56Context) ctx);
    } else if (ctx instanceof MySqlParser.CreateUserMysqlV57Context) {
      return this.visitCreateUserMysqlV57((MySqlParser.CreateUserMysqlV57Context) ctx);
    } else {
      throw ParserError.make(ctx);
    }
  }

  @Override
  public CreateUserMysqlV56 visitCreateUserMysqlV56(MySqlParser.CreateUserMysqlV56Context ctx) {
    if (ctx == null) {
      return null;
    }

    List<UserAuthOption> userAuthOptions = Lists.newArrayList();
    for (MySqlParser.UserAuthOptionContext userAuthOptionCtx : ctx.userAuthOption()) {
      userAuthOptions.add(this.visitUserAuthOption(userAuthOptionCtx));
    }

    return RelationalAlgebraExpressionFactory.makeCreateUserMysqlV56(userAuthOptions);
  }

  @Override
  public CreateUserMysqlV57 visitCreateUserMysqlV57(MySqlParser.CreateUserMysqlV57Context ctx) {
    if (ctx == null) {
      return null;
    }

    IfNotExists ifNotExists = this.visitIfNotExists(ctx.ifNotExists());
    List<UserAuthOption> userAuthOptions = Lists.newArrayList();
    for (MySqlParser.UserAuthOptionContext userAuthOptionCtx : ctx.userAuthOption()) {
      userAuthOptions.add(this.visitUserAuthOption(userAuthOptionCtx));
    }
    Boolean tlsNone = null;
    if (ctx.tlsNone != null) {
      tlsNone = Boolean.TRUE;
    }
    List<TlsOption> tlsOptions = Lists.newArrayList();
    if (CollectionUtils.isNotEmpty(ctx.tlsOption())) {
      for (MySqlParser.TlsOptionContext tlsOptionCtx : ctx.tlsOption()) {
        tlsOptions.add(this.visitTlsOption(tlsOptionCtx));
      }
    }
    List<UserResourceOption> userResourceOptions = Lists.newArrayList();
    if (CollectionUtils.isNotEmpty(ctx.userResourceOption())) {
      for (MySqlParser.UserResourceOptionContext userResourceOptionCtx : ctx.userResourceOption()) {
        userResourceOptions.add(this.visitUserResourceOption(userResourceOptionCtx));
      }
    }
    List<UserPasswordOption> userPasswordOptions = Lists.newArrayList();
    if (CollectionUtils.isNotEmpty(ctx.userPasswordOption())) {
      for (MySqlParser.UserPasswordOptionContext userPasswordOptionCtx : ctx.userPasswordOption()) {
        userPasswordOptions.add(this.visitUserPasswordOption(userPasswordOptionCtx));
      }
    }
    List<AdministrationStatement.UserLockOptionEnum> userLockOptions = Lists.newArrayList();
    if (CollectionUtils.isNotEmpty(ctx.userLockOption())) {
      for (MySqlParser.UserLockOptionContext userLockOptionCtx : ctx.userLockOption()) {
        userLockOptions.add(this.visitUserLockOption(userLockOptionCtx));
      }
    }

    return RelationalAlgebraExpressionFactory.makeCreateUserMysqlV57(ifNotExists, userAuthOptions,
      tlsNone, tlsOptions, userResourceOptions, userPasswordOptions, userLockOptions);
  }

  @Override
  public DropUser visitDropUser(MySqlParser.DropUserContext ctx) {
    if (ctx == null) {
      return null;
    }

    IfExists ifExists = this.visitIfExists(ctx.ifExists());
    List<UserName> userNames = Lists.newArrayList();
    for (MySqlParser.UserNameContext userNameCtx : ctx.userName()) {
      userNames.add(this.visitUserName(userNameCtx));
    }

    return RelationalAlgebraExpressionFactory.makeDropUser(ifExists, userNames);
  }

  @Override
  public GrantStatement visitGrantStatement(MySqlParser.GrantStatementContext ctx) {
    if (ctx == null) {
      return null;
    }

    List<PrivelegeClause> privelegeClauses = Lists.newArrayList();
    for (MySqlParser.PrivelegeClauseContext privelegeClauseCtx : ctx.privelegeClause()) {
      privelegeClauses.add(this.visitPrivelegeClause(privelegeClauseCtx));
    }
    AdministrationStatement.PrivilegeObjectEnum privilegeObject = null;
    if (ctx.privilegeObject != null) {
      privilegeObject = AdministrationStatement.PrivilegeObjectEnum
          .valueOf(ctx.privilegeObject.getText().toUpperCase());
    }
    PrivilegeLevel privilegeLevel = this.visitPrivilegeLevel(ctx.privilegeLevel());
    List<UserAuthOption> userAuthOptions = Lists.newArrayList();
    for (MySqlParser.UserAuthOptionContext userAuthOptionCtx : ctx.userAuthOption()) {
      userAuthOptions.add(this.visitUserAuthOption(userAuthOptionCtx));
    }
    Boolean tlsNone = null;
    if (ctx.tlsNone != null) {
      tlsNone = Boolean.TRUE;
    }
    List<TlsOption> tlsOptions = Lists.newArrayList();
    if (CollectionUtils.isNotEmpty(ctx.tlsOption())) {
      for (MySqlParser.TlsOptionContext tlsOptionCtx : ctx.tlsOption()) {
        tlsOptions.add(this.visitTlsOption(tlsOptionCtx));
      }
    }
    List<UserResourceOption> userResourceOptions = Lists.newArrayList();
    if (CollectionUtils.isNotEmpty(ctx.userResourceOption())) {
      for (MySqlParser.UserResourceOptionContext userResourceOptionCtx : ctx.userResourceOption()) {
        userResourceOptions.add(this.visitUserResourceOption(userResourceOptionCtx));
      }
    }
    return RelationalAlgebraExpressionFactory.makeGrantStatement(privelegeClauses, privilegeObject,
      privilegeLevel, userAuthOptions, tlsNone, tlsOptions, userResourceOptions);
  }

  @Override
  public GrantProxy visitGrantProxy(MySqlParser.GrantProxyContext ctx) {
    if (ctx == null) {
      return null;
    }

    UserName fromFirst = this.visitUserName(ctx.fromFirst);
    List<UserName> tos = Lists.newArrayList();
    tos.add(this.visitUserName(ctx.toFirst));
    if (CollectionUtils.isNotEmpty(ctx.toOther)) {
      for (MySqlParser.UserNameContext userNameCtx : ctx.toOther) {
        tos.add(this.visitUserName(userNameCtx));
      }
    }
    Boolean withGrantOption = null;
    if (ctx.OPTION() != null) {
      withGrantOption = Boolean.TRUE;
    }
    return RelationalAlgebraExpressionFactory.makeGrantProxy(fromFirst, tos, withGrantOption);
  }

  @Override
  public RenameUser visitRenameUser(MySqlParser.RenameUserContext ctx) {
    if (ctx == null) {
      return null;
    }

    List<RenameUserClause> renameUserClauses = Lists.newArrayList();
    for (MySqlParser.RenameUserClauseContext renameUserClauseCtx : ctx.renameUserClause()) {
      renameUserClauses.add(this.visitRenameUserClause(renameUserClauseCtx));
    }
    return RelationalAlgebraExpressionFactory.makeRenameUser(renameUserClauses);
  }

  public RevokeStatement visitRevokeStatement(MySqlParser.RevokeStatementContext ctx) {
    if (ctx == null) {
      return null;
    }

    if (ctx instanceof MySqlParser.DetailRevokeContext) {
      return this.visitDetailRevoke((MySqlParser.DetailRevokeContext) ctx);
    } else if (ctx instanceof MySqlParser.ShortRevokeContext) {
      return this.visitShortRevoke((MySqlParser.ShortRevokeContext) ctx);
    } else {
      throw ParserError.make(ctx);
    }
  }

  @Override
  public DetailRevoke visitDetailRevoke(MySqlParser.DetailRevokeContext ctx) {
    if (ctx == null) {
      return null;
    }

    List<PrivelegeClause> privelegeClauses = Lists.newArrayList();
    for (MySqlParser.PrivelegeClauseContext privelegeClauseCtx : ctx.privelegeClause()) {
      privelegeClauses.add(this.visitPrivelegeClause(privelegeClauseCtx));
    }
    AdministrationStatement.PrivilegeObjectEnum privilegeObject = null;
    if (ctx.privilegeObject != null) {
      privilegeObject = AdministrationStatement.PrivilegeObjectEnum
          .valueOf(ctx.privilegeObject.getText().toUpperCase());
    }
    PrivilegeLevel privilegeLevel = this.visitPrivilegeLevel(ctx.privilegeLevel());
    List<UserName> userNames = Lists.newArrayList();
    for (MySqlParser.UserNameContext userNameCtx : ctx.userName()) {
      userNames.add(this.visitUserName(userNameCtx));
    }

    return RelationalAlgebraExpressionFactory.makeDetailRevoke(privelegeClauses, privilegeObject,
      privilegeLevel, userNames);
  }

  @Override
  public ShortRevoke visitShortRevoke(MySqlParser.ShortRevokeContext ctx) {
    if (ctx == null) {
      return null;
    }

    List<UserName> userNames = Lists.newArrayList();
    for (MySqlParser.UserNameContext userNameCtx : ctx.userName()) {
      userNames.add(this.visitUserName(userNameCtx));
    }

    return RelationalAlgebraExpressionFactory.makeShortRevoke(userNames);
  }

  @Override
  public RevokeProxy visitRevokeProxy(MySqlParser.RevokeProxyContext ctx) {
    if (ctx == null) {
      return null;
    }

    UserName onUser = this.visitUserName(ctx.onUser);
    List<UserName> froms = Lists.newArrayList();
    froms.add(this.visitUserName(ctx.fromFirst));
    if (CollectionUtils.isNotEmpty(ctx.fromOther)) {
      for (MySqlParser.UserNameContext userNameCtx : ctx.fromOther) {
        froms.add(this.visitUserName(userNameCtx));
      }
    }

    return RelationalAlgebraExpressionFactory.makeRevokeProxy(onUser, froms);
  }

  @Override
  public SetPasswordStatement
      visitSetPasswordStatement(MySqlParser.SetPasswordStatementContext ctx) {
    if (ctx == null) {
      return null;
    }

    UserName userName = this.visitUserName(ctx.userName());
    PasswordFunctionClause passwordFunctionClause =
        this.visitPasswordFunctionClause(ctx.passwordFunctionClause());
    String password = null;
    if (ctx.STRING_LITERAL() != null) {
      password = ctx.STRING_LITERAL().getText();
    }
    return RelationalAlgebraExpressionFactory.makeSetPasswordStatement(userName,
      passwordFunctionClause, password);
  }

  @Override
  public UserSpecification visitUserSpecification(MySqlParser.UserSpecificationContext ctx) {
    if (ctx == null) {
      return null;
    }

    UserName userName = this.visitUserName(ctx.userName());
    UserPasswordOption userPasswordOption = this.visitUserPasswordOption(ctx.userPasswordOption());
    return RelationalAlgebraExpressionFactory.makeUserSpecification(userName, userPasswordOption);
  }

  public UserAuthOption visitUserAuthOption(MySqlParser.UserAuthOptionContext ctx) {
    if (ctx == null) {
      return null;
    }

    if (ctx instanceof MySqlParser.PasswordAuthOptionContext) {
      return this.visitPasswordAuthOption((MySqlParser.PasswordAuthOptionContext) ctx);
    } else if (ctx instanceof MySqlParser.StringAuthOptionContext) {
      return this.visitStringAuthOption((MySqlParser.StringAuthOptionContext) ctx);
    } else if (ctx instanceof MySqlParser.HashAuthOptionContext) {
      return this.visitHashAuthOption((MySqlParser.HashAuthOptionContext) ctx);
    } else if (ctx instanceof MySqlParser.SimpleAuthOptionContext) {
      return this.visitSimpleAuthOption((MySqlParser.SimpleAuthOptionContext) ctx);
    } else {
      throw ParserError.make(ctx);
    }
  }

  @Override
  public PasswordAuthOption visitPasswordAuthOption(MySqlParser.PasswordAuthOptionContext ctx) {
    if (ctx == null) {
      return null;
    }

    UserName userName = this.visitUserName(ctx.userName());
    String hashed = ctx.STRING_LITERAL().getText();
    return RelationalAlgebraExpressionFactory.makePasswordAuthOption(userName, hashed);
  }

  @Override
  public StringAuthOption visitStringAuthOption(MySqlParser.StringAuthOptionContext ctx) {
    if (ctx == null) {
      return null;
    }

    UserName userName = this.visitUserName(ctx.userName());
    AuthPlugin authPlugin = this.visitAuthPlugin(ctx.authPlugin());
    String by = ctx.STRING_LITERAL().getText();

    return RelationalAlgebraExpressionFactory.makeStringAuthOption(userName, authPlugin, by);
  }

  @Override
  public HashAuthOption visitHashAuthOption(MySqlParser.HashAuthOptionContext ctx) {
    if (ctx == null) {
      return null;
    }

    UserName userName = this.visitUserName(ctx.userName());
    AuthPlugin authPlugin = this.visitAuthPlugin(ctx.authPlugin());
    String as = null;
    if (ctx.STRING_LITERAL() != null) {
      as = ctx.STRING_LITERAL().getText();
    }

    return RelationalAlgebraExpressionFactory.makeHashAuthOption(userName, authPlugin, as);
  }

  @Override
  public SimpleAuthOption visitSimpleAuthOption(MySqlParser.SimpleAuthOptionContext ctx) {
    if (ctx == null) {
      return null;
    }

    UserName userName = this.visitUserName(ctx.userName());
    return RelationalAlgebraExpressionFactory.makeSimpleAuthOption(userName);
  }

  @Override
  public TlsOption visitTlsOption(MySqlParser.TlsOptionContext ctx) {
    if (ctx == null) {
      return null;
    }
    TlsOption.Type type = TlsOption.Type.valueOf(ctx.getChild(0).getText().toUpperCase());
    String value = null;
    if (ctx.STRING_LITERAL() != null) {
      value = ctx.STRING_LITERAL().getText();
    }
    return RelationalAlgebraExpressionFactory.makeTlsOption(type, value);
  }

  @Override
  public UserResourceOption visitUserResourceOption(MySqlParser.UserResourceOptionContext ctx) {
    if (ctx == null) {
      return null;
    }

    UserResourceOption.Type type =
        UserResourceOption.Type.valueOf(ctx.getChild(0).getText().toUpperCase());
    DecimalLiteral decimalLiteral = this.visitDecimalLiteral(ctx.decimalLiteral());

    return RelationalAlgebraExpressionFactory.makeUserResourceOption(type, decimalLiteral);
  }

  @Override
  public UserPasswordOption visitUserPasswordOption(MySqlParser.UserPasswordOptionContext ctx) {
    if (ctx == null) {
      return null;
    }

    UserPasswordOption.ExpireType expireType = null;
    DecimalLiteral day = null;
    if (ctx.DEFAULT() != null) {
      expireType = UserPasswordOption.ExpireType.DEFAULT;
    } else if (ctx.NEVER() != null) {
      expireType = UserPasswordOption.ExpireType.NEVER;
    } else if (ctx.INTERVAL() != null) {
      expireType = UserPasswordOption.ExpireType.INTERVAL;
      day = this.visitDecimalLiteral(ctx.decimalLiteral());
    }

    return RelationalAlgebraExpressionFactory.makeUserPasswordOption(expireType, day);
  }

  @Override
  public AdministrationStatement.UserLockOptionEnum
      visitUserLockOption(MySqlParser.UserLockOptionContext ctx) {
    if (ctx == null) {
      return null;
    }
    return RelationalAlgebraExpressionFactory.makeUserLockOption(ctx.lockType.getText());
  }

  @Override
  public PrivelegeClause visitPrivelegeClause(MySqlParser.PrivelegeClauseContext ctx) {
    if (ctx == null) {
      return null;
    }

    AdministrationStatement.PrivilegeEnum privilege = this.visitPrivilege(ctx.privilege());
    UidList uidList = this.visitUidList(ctx.uidList());

    return RelationalAlgebraExpressionFactory.makePrivelegeClause(privilege, uidList);
  }

  @Override
  public AdministrationStatement.PrivilegeEnum visitPrivilege(MySqlParser.PrivilegeContext ctx) {
    if (ctx == null) {
      return null;
    }

    if (ctx.ALL() != null) {
      if (ctx.PRIVILEGES() != null) {
        return AdministrationStatement.PrivilegeEnum.ALL_PRIVILEGES;
      } else {
        return AdministrationStatement.PrivilegeEnum.ALL;
      }
    } else if (ctx.ALTER() != null) {
      if (ctx.ROUTINE() != null) {
        return AdministrationStatement.PrivilegeEnum.ALTER_ROUTINE;
      } else {
        return AdministrationStatement.PrivilegeEnum.ALTER;
      }
    } else if (ctx.CREATE() != null) {
      if (ctx.TABLES() != null) {
        return AdministrationStatement.PrivilegeEnum.CREATE_TEMPORARY_TABLES;
      } else if (ctx.ROUTINE() != null) {
        return AdministrationStatement.PrivilegeEnum.CREATE_ROUTINE;
      } else if (ctx.VIEW() != null) {
        return AdministrationStatement.PrivilegeEnum.CREATE_VIEW;
      } else if (ctx.USER() != null) {
        return AdministrationStatement.PrivilegeEnum.CREATE_USER;
      } else if (ctx.TABLESPACE() != null) {
        return AdministrationStatement.PrivilegeEnum.CREATE_TABLESPACE;
      } else {
        return AdministrationStatement.PrivilegeEnum.CREATE;
      }

    } else if (ctx.GRANT() != null) {
      return AdministrationStatement.PrivilegeEnum.GRANT_OPTION;
    } else if (ctx.LOCK() != null) {
      return AdministrationStatement.PrivilegeEnum.LOCK_TABLES;
    } else if (ctx.REPLICATION() != null) {
      if (ctx.CLIENT() != null) {
        return AdministrationStatement.PrivilegeEnum.REPLICATION_CLIENT;
      } else if (ctx.SLAVE() != null) {
        return AdministrationStatement.PrivilegeEnum.REPLICATION_SLAVE;
      } else {
        throw ParserError.make(ctx);
      }
    }

    else if (ctx.SHOW() != null) {
      if (ctx.VIEW() != null) {
        return AdministrationStatement.PrivilegeEnum.SHOW_VIEW;
      } else if (ctx.DATABASES() != null) {
        return AdministrationStatement.PrivilegeEnum.SHOW_DATABASES;
      } else {
        throw ParserError.make(ctx);
      }
    } else {
      AdministrationStatement.PrivilegeEnum privilege =
          AdministrationStatement.PrivilegeEnum.valueOf(ctx.getChild(0).getText().toUpperCase());
      if (privilege == null) {
        throw ParserError.make(ctx);
      }
      return privilege;
    }
  }

  public PrivilegeLevel visitPrivilegeLevel(MySqlParser.PrivilegeLevelContext ctx) {
    if (ctx == null) {
      return null;
    }

    if (ctx instanceof MySqlParser.CurrentSchemaPriviLevelContext) {
      return this.visitCurrentSchemaPriviLevel((MySqlParser.CurrentSchemaPriviLevelContext) ctx);
    } else if (ctx instanceof MySqlParser.GlobalPrivLevelContext) {
      return this.visitGlobalPrivLevel((MySqlParser.GlobalPrivLevelContext) ctx);
    } else if (ctx instanceof MySqlParser.DefiniteSchemaPrivLevelContext) {
      return this.visitDefiniteSchemaPrivLevel((MySqlParser.DefiniteSchemaPrivLevelContext) ctx);
    } else if (ctx instanceof MySqlParser.DefiniteFullTablePrivLevelContext) {
      return this
          .visitDefiniteFullTablePrivLevel((MySqlParser.DefiniteFullTablePrivLevelContext) ctx);
    } else if (ctx instanceof MySqlParser.DefiniteTablePrivLevelContext) {
      return this.visitDefiniteTablePrivLevel((MySqlParser.DefiniteTablePrivLevelContext) ctx);
    } else {
      throw ParserError.make(ctx);
    }
  }

  @Override
  public CurrentSchemaPriviLevel
      visitCurrentSchemaPriviLevel(MySqlParser.CurrentSchemaPriviLevelContext ctx) {
    if (ctx == null) {
      return null;
    }

    return RelationalAlgebraExpressionFactory.makeCurrentSchemaPriviLevel();
  }

  @Override
  public GlobalPrivLevel visitGlobalPrivLevel(MySqlParser.GlobalPrivLevelContext ctx) {
    if (ctx == null) {
      return null;
    }
    return RelationalAlgebraExpressionFactory.makeGlobalPrivLevel();
  }

  @Override
  public DefiniteSchemaPrivLevel
      visitDefiniteSchemaPrivLevel(MySqlParser.DefiniteSchemaPrivLevelContext ctx) {
    if (ctx == null) {
      return null;
    }
    return RelationalAlgebraExpressionFactory.makeDefiniteSchemaPrivLevel(this.visitUid(ctx.uid()));
  }

  @Override
  public DefiniteFullTablePrivLevel
      visitDefiniteFullTablePrivLevel(MySqlParser.DefiniteFullTablePrivLevelContext ctx) {
    if (ctx == null) {
      return null;
    }
    List<MySqlParser.UidContext> uidCtxs = ctx.uid();
    if (uidCtxs == null || uidCtxs.size() != 2) {
      throw ParserError.make(ctx);
    }

    Uid uid1 = this.visitUid(uidCtxs.get(0));
    Uid uid2 = this.visitUid(uidCtxs.get(1));
    return RelationalAlgebraExpressionFactory.makeDefiniteFullTablePrivLevel(uid1, uid2);
  }

  @Override
  public DefiniteTablePrivLevel
      visitDefiniteTablePrivLevel(MySqlParser.DefiniteTablePrivLevelContext ctx) {
    if (ctx == null) {
      return null;
    }
    return RelationalAlgebraExpressionFactory.makeDefiniteTablePrivLevel(this.visitUid(ctx.uid()));
  }

  @Override
  public RenameUserClause visitRenameUserClause(MySqlParser.RenameUserClauseContext ctx) {
    if (ctx == null) {
      return null;
    }

    UserName fromFirst = this.visitUserName(ctx.fromFirst);
    UserName toFirst = this.visitUserName(ctx.toFirst);
    return RelationalAlgebraExpressionFactory.makeRenameUserClause(fromFirst, toFirst);
  }

  @Override
  public AnalyzeTable visitAnalyzeTable(MySqlParser.AnalyzeTableContext ctx) {
    if (ctx == null) {
      return null;
    }

    AdministrationStatement.AdminTableActionOptionEnum actionOption = null;
    if (ctx.actionOption != null) {
      actionOption = AdministrationStatement.AdminTableActionOptionEnum
          .valueOf(ctx.actionOption.getText().toUpperCase());
    }
    Tables tables = this.visitTables(ctx.tables());
    return RelationalAlgebraExpressionFactory.makeAnalyzeTable(actionOption, tables);
  }

  @Override
  public CheckTable visitCheckTable(MySqlParser.CheckTableContext ctx) {
    if (ctx == null) {
      return null;
    }

    Tables tables = this.visitTables(ctx.tables());
    List<AdministrationStatement.CheckTableOptionEnum> checkTableOptions = Lists.newArrayList();
    for (MySqlParser.CheckTableOptionContext checkTableOptionCtx : ctx.checkTableOption()) {
      checkTableOptions.add(this.visitCheckTableOption(checkTableOptionCtx));
    }
    return RelationalAlgebraExpressionFactory.makeCheckTable(tables, checkTableOptions);
  }

  @Override
  public ChecksumTable visitChecksumTable(MySqlParser.ChecksumTableContext ctx) {
    if (ctx == null) {
      return null;
    }

    Tables tables = this.visitTables(ctx.tables());
    ChecksumTable.ActionOptionEnum actionOption = null;
    if (ctx.actionOption != null) {
      actionOption =
          ChecksumTable.ActionOptionEnum.valueOf(ctx.actionOption.getText().toUpperCase());
    }
    return RelationalAlgebraExpressionFactory.makeChecksumTable(tables, actionOption);
  }

  @Override
  public OptimizeTable visitOptimizeTable(MySqlParser.OptimizeTableContext ctx) {
    if (ctx == null) {
      return null;
    }

    AdministrationStatement.AdminTableActionOptionEnum actionOption = null;
    if (ctx.actionOption != null) {
      actionOption = AdministrationStatement.AdminTableActionOptionEnum
          .valueOf(ctx.actionOption.getText().toUpperCase());
    }
    Tables tables = this.visitTables(ctx.tables());

    return RelationalAlgebraExpressionFactory.makeOptimizeTable(actionOption, tables);
  }

  @Override
  public RepairTable visitRepairTable(MySqlParser.RepairTableContext ctx) {
    if (ctx == null) {
      return null;
    }

    AdministrationStatement.AdminTableActionOptionEnum actionOption = null;
    if (ctx.actionOption != null) {
      actionOption = AdministrationStatement.AdminTableActionOptionEnum
          .valueOf(ctx.actionOption.getText().toUpperCase());
    }
    Tables tables = this.visitTables(ctx.tables());
    Boolean quick = null;
    if (ctx.QUICK() != null) {
      quick = Boolean.TRUE;
    }
    Boolean extended = null;
    if (ctx.EXTENDED() != null) {
      extended = Boolean.TRUE;
    }
    Boolean useFrm = null;
    if (ctx.USE_FRM() != null) {
      useFrm = Boolean.TRUE;
    }
    return RelationalAlgebraExpressionFactory.makeRepairTable(actionOption, tables, quick, extended,
      useFrm);
  }

  @Override
  public AdministrationStatement.CheckTableOptionEnum
      visitCheckTableOption(MySqlParser.CheckTableOptionContext ctx) {
    if (ctx == null) {
      return null;
    }

    if (ctx.UPGRADE() != null) {
      return AdministrationStatement.CheckTableOptionEnum.FOR_UPGRADE;
    } else if (ctx.QUICK() != null) {
      return AdministrationStatement.CheckTableOptionEnum.QUICK;
    } else if (ctx.FAST() != null) {
      return AdministrationStatement.CheckTableOptionEnum.FAST;
    } else if (ctx.MEDIUM() != null) {
      return AdministrationStatement.CheckTableOptionEnum.MEDIUM;
    } else if (ctx.EXTENDED() != null) {
      return AdministrationStatement.CheckTableOptionEnum.EXTENDED;
    } else if (ctx.CHANGED() != null) {
      return AdministrationStatement.CheckTableOptionEnum.CHANGED;
    } else {
      throw ParserError.make(ctx);
    }

  }

  @Override
  public CreateUdfunction visitCreateUdfunction(MySqlParser.CreateUdfunctionContext ctx) {
    if (ctx == null) {
      return null;
    }

    Boolean aggregate = null;
    if (ctx.AGGREGATE() != null) {
      aggregate = Boolean.TRUE;
    }
    Uid uid = this.visitUid(ctx.uid());
    CreateUdfunction.ReturnTypeEnum returnType =
        CreateUdfunction.ReturnTypeEnum.valueOf(ctx.returnType.getText().toUpperCase());
    String soName = ctx.STRING_LITERAL().getText();
    return RelationalAlgebraExpressionFactory.makeCreateUdfunction(aggregate, uid, returnType,
      soName);
  }

  @Override
  public InstallPlugin visitInstallPlugin(MySqlParser.InstallPluginContext ctx) {
    if (ctx == null) {
      return null;
    }

    Uid uid = this.visitUid(ctx.uid());
    String soName = ctx.STRING_LITERAL().getText();
    return RelationalAlgebraExpressionFactory.makeInstallPlugin(uid, soName);
  }

  @Override
  public UninstallPlugin visitUninstallPlugin(MySqlParser.UninstallPluginContext ctx) {
    if (ctx == null) {
      return null;
    }
    return RelationalAlgebraExpressionFactory.makeUninstallPlugin(this.visitUid(ctx.uid()));
  }

  public SetStatement visitSetStatement(MySqlParser.SetStatementContext ctx) {
    if (ctx == null) {
      return null;
    }
    if (ctx instanceof MySqlParser.SetVariableContext) {
      return this.visitSetVariable((MySqlParser.SetVariableContext) ctx);
    } else if (ctx instanceof MySqlParser.SetCharsetContext) {
      return this.visitSetCharset((MySqlParser.SetCharsetContext) ctx);
    } else if (ctx instanceof MySqlParser.SetNamesContext) {
      return this.visitSetNames((MySqlParser.SetNamesContext) ctx);
    } else if (ctx instanceof MySqlParser.SetPasswordContext) {
      return this.visitSetPassword((MySqlParser.SetPasswordContext) ctx);
    } else if (ctx instanceof MySqlParser.SetTransactionContext) {
      return this.visitSetTransaction((MySqlParser.SetTransactionContext) ctx);
    } else if (ctx instanceof MySqlParser.SetAutocommitContext) {
      return this.visitSetAutocommit((MySqlParser.SetAutocommitContext) ctx);
    } else if (ctx instanceof MySqlParser.SetNewValueInsideTriggerContext) {
      return this.visitSetNewValueInsideTrigger((MySqlParser.SetNewValueInsideTriggerContext) ctx);
    } else {
      throw ParserError.make(ctx);
    }
  }

  @Override
  public SetVariable visitSetVariable(MySqlParser.SetVariableContext ctx) {
    if (ctx == null) {
      return null;
    }

    List<VariableClause> variableClauses = Lists.newArrayList();
    for (MySqlParser.VariableClauseContext variableClauseCtx : ctx.variableClause()) {
      variableClauses.add(this.visitVariableClause(variableClauseCtx));
    }

    List<Expression> expressions = Lists.newArrayList();
    for (MySqlParser.ExpressionContext expressionCtx : ctx.expression()) {
      expressions.add(this.visitExpression(expressionCtx));
    }

    return RelationalAlgebraExpressionFactory.makeSetVariable(variableClauses, expressions);
  }

  @Override
  public SetCharset visitSetCharset(MySqlParser.SetCharsetContext ctx) {
    if (ctx == null) {
      return null;
    }

    CharsetName charsetName = this.visitCharsetName(ctx.charsetName());
    return RelationalAlgebraExpressionFactory.makeSetCharset(charsetName);
  }

  @Override
  public SetNames visitSetNames(MySqlParser.SetNamesContext ctx) {
    if (ctx == null) {
      return null;
    }

    CharsetName charsetName = this.visitCharsetName(ctx.charsetName());
    CollationName collationName = this.visitCollationName(ctx.collationName());
    return RelationalAlgebraExpressionFactory.makeSetNames(charsetName, collationName);
  }

  @Override
  public SetPasswordStatement visitSetPassword(MySqlParser.SetPasswordContext ctx) {
    if (ctx == null) {
      return null;
    }
    return this.visitSetPasswordStatement(ctx.setPasswordStatement());
  }

  @Override
  public SetTransactionStatement visitSetTransaction(MySqlParser.SetTransactionContext ctx) {
    if (ctx == null) {
      return null;
    }
    return this.visitSetTransactionStatement(ctx.setTransactionStatement());
  }

  @Override
  public SetAutocommitStatement visitSetAutocommit(MySqlParser.SetAutocommitContext ctx) {
    if (ctx == null) {
      return null;
    }
    return this.visitSetAutocommitStatement(ctx.setAutocommitStatement());
  }

  @Override
  public SetNewValueInsideTrigger
      visitSetNewValueInsideTrigger(MySqlParser.SetNewValueInsideTriggerContext ctx) {
    if (ctx == null) {
      return null;
    }

    FullId fullId = this.visitFullId(ctx.fullId());
    Expression expression = this.visitExpression(ctx.expression());
    return RelationalAlgebraExpressionFactory.makeSetNewValueInsideTrigger(fullId, expression);
  }

  public ShowStatement visitShowStatement(MySqlParser.ShowStatementContext ctx) {
    if (ctx == null) {
      return null;
    }

    if (ctx instanceof MySqlParser.ShowMasterLogsContext) {
      return this.visitShowMasterLogs((MySqlParser.ShowMasterLogsContext) ctx);
    } else if (ctx instanceof MySqlParser.ShowLogEventsContext) {
      return this.visitShowLogEvents((MySqlParser.ShowLogEventsContext) ctx);
    } else if (ctx instanceof MySqlParser.ShowObjectFilterContext) {
      return this.visitShowObjectFilter((MySqlParser.ShowObjectFilterContext) ctx);
    } else if (ctx instanceof MySqlParser.ShowColumnsContext) {
      return this.visitShowColumns((MySqlParser.ShowColumnsContext) ctx);
    } else if (ctx instanceof MySqlParser.ShowCreateDbContext) {
      return this.visitShowCreateDb((MySqlParser.ShowCreateDbContext) ctx);
    } else if (ctx instanceof MySqlParser.ShowCreateFullIdObjectContext) {
      return this.visitShowCreateFullIdObject((MySqlParser.ShowCreateFullIdObjectContext) ctx);
    } else if (ctx instanceof MySqlParser.ShowCreateUserContext) {
      return this.visitShowCreateUser((MySqlParser.ShowCreateUserContext) ctx);
    } else if (ctx instanceof MySqlParser.ShowEngineContext) {
      return this.visitShowEngine((MySqlParser.ShowEngineContext) ctx);
    } else if (ctx instanceof MySqlParser.ShowGlobalInfoContext) {
      return this.visitShowGlobalInfo((MySqlParser.ShowGlobalInfoContext) ctx);
    } else if (ctx instanceof MySqlParser.ShowErrorsContext) {
      return this.visitShowErrors((MySqlParser.ShowErrorsContext) ctx);
    } else if (ctx instanceof MySqlParser.ShowCountErrorsContext) {
      return this.visitShowCountErrors((MySqlParser.ShowCountErrorsContext) ctx);
    } else if (ctx instanceof MySqlParser.ShowSchemaFilterContext) {
      return this.visitShowSchemaFilter((MySqlParser.ShowSchemaFilterContext) ctx);
    } else if (ctx instanceof MySqlParser.ShowRoutineContext) {
      return this.visitShowRoutine((MySqlParser.ShowRoutineContext) ctx);
    } else if (ctx instanceof MySqlParser.ShowGrantsContext) {
      return this.visitShowGrants((MySqlParser.ShowGrantsContext) ctx);
    } else if (ctx instanceof MySqlParser.ShowIndexesContext) {
      return this.visitShowIndexes((MySqlParser.ShowIndexesContext) ctx);
    } else if (ctx instanceof MySqlParser.ShowOpenTablesContext) {
      return this.visitShowOpenTables((MySqlParser.ShowOpenTablesContext) ctx);
    } else if (ctx instanceof MySqlParser.ShowProfileContext) {
      return this.visitShowProfile((MySqlParser.ShowProfileContext) ctx);
    } else if (ctx instanceof MySqlParser.ShowSlaveStatusContext) {
      return this.visitShowSlaveStatus((MySqlParser.ShowSlaveStatusContext) ctx);
    } else {
      throw ParserError.make(ctx);
    }
  }

  @Override
  public ShowMasterLogs visitShowMasterLogs(MySqlParser.ShowMasterLogsContext ctx) {
    if (ctx == null) {
      return null;
    }

    ShowMasterLogs.LogFormatEnum logFormat =
        ShowMasterLogs.LogFormatEnum.valueOf(ctx.logFormat.getText().toUpperCase());
    return RelationalAlgebraExpressionFactory.makeShowMasterLogs(logFormat);
  }

  @Override
  public ShowLogEvents visitShowLogEvents(MySqlParser.ShowLogEventsContext ctx) {
    if (ctx == null) {
      return null;
    }

    ShowLogEvents.LogFormatEnum logFormat =
        ShowLogEvents.LogFormatEnum.valueOf(ctx.logFormat.getText().toUpperCase());
    String filename = null;
    if (ctx.filename != null) {
      filename = ctx.filename.getText();
    }
    DecimalLiteral fromPosition = this.visitDecimalLiteral(ctx.fromPosition);
    DecimalLiteral offset = this.visitDecimalLiteral(ctx.offset);
    DecimalLiteral rowCount = this.visitDecimalLiteral(ctx.rowCount);

    return RelationalAlgebraExpressionFactory.makeShowLogEvents(logFormat, filename, fromPosition,
      offset, rowCount);
  }

  @Override
  public ShowObjectFilter visitShowObjectFilter(MySqlParser.ShowObjectFilterContext ctx) {
    if (ctx == null) {
      return null;
    }

    AdministrationStatement.ShowCommonEntityEnum showCommonEntity =
        this.visitShowCommonEntity(ctx.showCommonEntity());
    ShowFilter showFilter = this.visitShowFilter(ctx.showFilter());

    return RelationalAlgebraExpressionFactory.makeShowObjectFilter(showCommonEntity, showFilter);
  }

  @Override
  public ShowColumns visitShowColumns(MySqlParser.ShowColumnsContext ctx) {
    if (ctx == null) {
      return null;
    }

    Boolean full = null;
    if (ctx.FULL() != null) {
      full = Boolean.TRUE;
    }
    ShowColumns.ColumnsFormatEnum columnsFormat =
        ShowColumns.ColumnsFormatEnum.valueOf(ctx.columnsFormat.getText().toUpperCase());
    ShowColumns.TableFormatEnum tableFormat =
        ShowColumns.TableFormatEnum.valueOf(ctx.tableFormat.getText().toUpperCase());
    TableName tableName = this.visitTableName(ctx.tableName());
    ShowColumns.SchemaFormatEnum schemaFormat = null;
    if (ctx.schemaFormat != null) {
      schemaFormat = ShowColumns.SchemaFormatEnum.valueOf(ctx.schemaFormat.getText().toUpperCase());
    }
    Uid uid = this.visitUid(ctx.uid());
    ShowFilter showFilter = this.visitShowFilter(ctx.showFilter());

    return RelationalAlgebraExpressionFactory.makeShowColumns(full, columnsFormat, tableFormat,
      tableName, schemaFormat, uid, showFilter);
  }

  @Override
  public ShowCreateDb visitShowCreateDb(MySqlParser.ShowCreateDbContext ctx) {
    if (ctx == null) {
      return null;
    }

    ShowCreateDb.SchemaFormatEnum schemaFormat =
        ShowCreateDb.SchemaFormatEnum.valueOf(ctx.schemaFormat.getText().toUpperCase());
    IfNotExists ifNotExists = this.visitIfNotExists(ctx.ifNotExists());
    Uid uid = this.visitUid(ctx.uid());

    return RelationalAlgebraExpressionFactory.makeShowCreateDb(schemaFormat, ifNotExists, uid);
  }

  @Override
  public ShowCreateFullIdObject
      visitShowCreateFullIdObject(MySqlParser.ShowCreateFullIdObjectContext ctx) {
    if (ctx == null) {
      return null;
    }

    ShowCreateFullIdObject.NamedEntityEnum namedEntity =
        ShowCreateFullIdObject.NamedEntityEnum.valueOf(ctx.namedEntity.getText().toUpperCase());
    FullId fullId = this.visitFullId(ctx.fullId());

    return RelationalAlgebraExpressionFactory.makeShowCreateFullIdObject(namedEntity, fullId);
  }

  @Override
  public ShowCreateUser visitShowCreateUser(MySqlParser.ShowCreateUserContext ctx) {
    if (ctx == null) {
      return null;
    }

    UserName userName = this.visitUserName(ctx.userName());
    return RelationalAlgebraExpressionFactory.makeShowCreateUser(userName);
  }

  @Override
  public ShowEngine visitShowEngine(MySqlParser.ShowEngineContext ctx) {
    if (ctx == null) {
      return null;
    }

    EngineName engineName = this.visitEngineName(ctx.engineName());
    ShowEngine.EngineOptionEnum engineOption =
        ShowEngine.EngineOptionEnum.valueOf(ctx.engineOption.getText().toUpperCase());

    return RelationalAlgebraExpressionFactory.makeShowEngine(engineName, engineOption);

  }

  @Override
  public ShowGlobalInfo visitShowGlobalInfo(MySqlParser.ShowGlobalInfoContext ctx) {
    if (ctx == null) {
      return null;
    }

    AdministrationStatement.ShowGlobalInfoClauseEnum showGlobalInfoClause =
        this.visitShowGlobalInfoClause(ctx.showGlobalInfoClause());
    return RelationalAlgebraExpressionFactory.makeShowGlobalInfo(showGlobalInfoClause);
  }

  @Override
  public ShowErrors visitShowErrors(MySqlParser.ShowErrorsContext ctx) {
    if (ctx == null) {
      return null;
    }

    ShowErrors.ErrorFormatEnum errorFormat =
        ShowErrors.ErrorFormatEnum.valueOf(ctx.errorFormat.getText().toUpperCase());
    DecimalLiteral offset = this.visitDecimalLiteral(ctx.offset);
    DecimalLiteral rowCount = this.visitDecimalLiteral(ctx.rowCount);
    return RelationalAlgebraExpressionFactory.makeShowErrors(errorFormat, offset, rowCount);
  }

  @Override
  public ShowCountErrors visitShowCountErrors(MySqlParser.ShowCountErrorsContext ctx) {
    if (ctx == null) {
      return null;
    }

    ShowCountErrors.ErrorFormatEnum errorFormat =
        ShowCountErrors.ErrorFormatEnum.valueOf(ctx.errorFormat.getText().toUpperCase());
    return RelationalAlgebraExpressionFactory.makeShowCountErrors(errorFormat);

  }

  @Override
  public ShowSchemaFilter visitShowSchemaFilter(MySqlParser.ShowSchemaFilterContext ctx) {
    if (ctx == null) {
      return null;
    }

    ShowSchemaFilter.ShowSchemaEntityEnum showSchemaEntity =
        this.visitShowSchemaEntity(ctx.showSchemaEntity());
    ShowSchemaFilter.SchemaFormatEnum schemaFormat = null;
    if (ctx.schemaFormat != null) {
      schemaFormat =
          ShowSchemaFilter.SchemaFormatEnum.valueOf(ctx.schemaFormat.getText().toUpperCase());
    }
    Uid uid = this.visitUid(ctx.uid());
    ShowFilter showFilter = this.visitShowFilter(ctx.showFilter());
    return RelationalAlgebraExpressionFactory.makeShowSchemaFilter(showSchemaEntity, schemaFormat,
      uid, showFilter);
  }

  @Override
  public ShowRoutine visitShowRoutine(MySqlParser.ShowRoutineContext ctx) {
    if (ctx == null) {
      return null;
    }

    ShowRoutine.RoutineEnum routine =
        ShowRoutine.RoutineEnum.valueOf(ctx.routine.getText().toUpperCase());
    FullId fullId = this.visitFullId(ctx.fullId());

    return RelationalAlgebraExpressionFactory.makeShowRoutine(routine, fullId);
  }

  @Override
  public ShowGrants visitShowGrants(MySqlParser.ShowGrantsContext ctx) {
    if (ctx == null) {
      return null;
    }

    UserName userName = this.visitUserName(ctx.userName());
    return RelationalAlgebraExpressionFactory.makeShowGrants(userName);
  }

  @Override
  public ShowIndexes visitShowIndexes(MySqlParser.ShowIndexesContext ctx) {
    if (ctx == null) {
      return null;
    }

    ShowIndexes.IndexFormatEnum indexFormat =
        ShowIndexes.IndexFormatEnum.valueOf(ctx.indexFormat.getText().toUpperCase());
    ShowIndexes.TableFormatEnum tableFormat =
        ShowIndexes.TableFormatEnum.valueOf(ctx.tableFormat.getText().toUpperCase());
    TableName tableName = this.visitTableName(ctx.tableName());
    ShowIndexes.SchemaFormatEnum schemaFormat = null;
    if (ctx.schemaFormat != null) {
      schemaFormat = ShowIndexes.SchemaFormatEnum.valueOf(ctx.schemaFormat.getText().toUpperCase());
    }
    Uid uid = this.visitUid(ctx.uid());
    Expression where = this.visitExpression(ctx.expression());
    return RelationalAlgebraExpressionFactory.makeShowIndexes(indexFormat, tableFormat, tableName,
      schemaFormat, uid, where);
  }

  @Override
  public ShowOpenTables visitShowOpenTables(MySqlParser.ShowOpenTablesContext ctx) {
    if (ctx == null) {
      return null;
    }

    ShowOpenTables.SchemaFormatEnum schemaFormat = null;
    if (ctx.schemaFormat != null) {
      schemaFormat =
          ShowOpenTables.SchemaFormatEnum.valueOf(ctx.schemaFormat.getText().toUpperCase());
    }
    Uid uid = this.visitUid(ctx.uid());
    ShowFilter showFilter = this.visitShowFilter(ctx.showFilter());

    return RelationalAlgebraExpressionFactory.makeShowOpenTables(schemaFormat, uid, showFilter);
  }

  @Override
  public ShowProfile visitShowProfile(MySqlParser.ShowProfileContext ctx) {
    if (ctx == null) {
      return null;
    }

    List<AdministrationStatement.ShowProfileTypeEnum> showProfileTypes = Lists.newArrayList();
    for (MySqlParser.ShowProfileTypeContext showProfileTypeCtx : ctx.showProfileType()) {
      showProfileTypes.add(this.visitShowProfileType(showProfileTypeCtx));
    }
    DecimalLiteral queryCount = this.visitDecimalLiteral(ctx.queryCount);
    DecimalLiteral offset = this.visitDecimalLiteral(ctx.offset);
    DecimalLiteral rowCount = this.visitDecimalLiteral(ctx.rowCount);
    return RelationalAlgebraExpressionFactory.makeShowProfile(showProfileTypes, queryCount, offset,
      rowCount);
  }

  @Override
  public ShowSlaveStatus visitShowSlaveStatus(MySqlParser.ShowSlaveStatusContext ctx) {
    if (ctx == null) {
      return null;
    }

    String channel = null;
    if (ctx.STRING_LITERAL() != null) {
      channel = ctx.STRING_LITERAL().getText();
    }
    return RelationalAlgebraExpressionFactory.makeShowSlaveStatus(channel);
  }

  @Override
  public VariableClause visitVariableClause(MySqlParser.VariableClauseContext ctx) {
    if (ctx == null) {
      return null;
    }

    VariableClause.Type type = null;
    String id = null;
    Boolean has = null;
    VariableClause.ScopeType scopeType = null;
    Uid uid = null;

    if (ctx.LOCAL_ID() != null) {
      type = VariableClause.Type.LOCAL_ID;
      id = ctx.LOCAL_ID().getText();
      return RelationalAlgebraExpressionFactory.makeVariableClause(type, id);
    } else if (ctx.GLOBAL_ID() != null) {
      type = VariableClause.Type.GLOBAL_ID;
      id = ctx.GLOBAL_ID().getText();
      return RelationalAlgebraExpressionFactory.makeVariableClause(type, id);
    } else if (ctx.uid() != null) {
      type = VariableClause.Type.UID;
      if ("@".equals(ctx.getChild(0).getText())) {
        has = Boolean.TRUE;
      }
      if (ctx.GLOBAL() != null) {
        scopeType = VariableClause.ScopeType.GLOBAL;
      }
      if (ctx.SESSION() != null) {
        scopeType = VariableClause.ScopeType.SESSION;
      }
      if (ctx.LOCAL() != null) {
        scopeType = VariableClause.ScopeType.LOCAL;
      }
      uid = this.visitUid(ctx.uid());
      return RelationalAlgebraExpressionFactory.makeVariableClause(type, id, has, scopeType, uid);
    } else {
      throw ParserError.make(ctx);
    }

  }

  @Override
  public AdministrationStatement.ShowCommonEntityEnum
      visitShowCommonEntity(MySqlParser.ShowCommonEntityContext ctx) {
    if (ctx == null) {
      return null;
    }

    if (ctx.CHARACTER() != null) {
      return AdministrationStatement.ShowCommonEntityEnum.CHARACTER_SET;
    } else if (ctx.COLLATION() != null) {
      return AdministrationStatement.ShowCommonEntityEnum.COLLATION;
    } else if (ctx.DATABASES() != null) {
      return AdministrationStatement.ShowCommonEntityEnum.DATABASES;
    } else if (ctx.SCHEMAS() != null) {
      return AdministrationStatement.ShowCommonEntityEnum.SCHEMAS;
    } else if (ctx.FUNCTION() != null) {
      return AdministrationStatement.ShowCommonEntityEnum.FUNCTION_STATUS;
    } else if (ctx.PROCEDURE() != null) {
      return AdministrationStatement.ShowCommonEntityEnum.PROCEDURE_STATUS;
    } else if (ctx.STATUS() != null) {
      if (ctx.GLOBAL() != null) {
        return AdministrationStatement.ShowCommonEntityEnum.GLOBAL_STATUS;
      } else if (ctx.SESSION() != null) {
        return AdministrationStatement.ShowCommonEntityEnum.SESSION_STATUS;
      } else {
        return AdministrationStatement.ShowCommonEntityEnum.STATUS;
      }
    } else if (ctx.VARIABLES() != null) {
      if (ctx.GLOBAL() != null) {
        return AdministrationStatement.ShowCommonEntityEnum.GLOBAL_VARIABLES;
      } else if (ctx.SESSION() != null) {
        return AdministrationStatement.ShowCommonEntityEnum.SESSION_VARIABLES;
      } else {
        return AdministrationStatement.ShowCommonEntityEnum.VARIABLES;
      }
    } else {
      throw ParserError.make(ctx);
    }
  }

  @Override
  public ShowFilter visitShowFilter(MySqlParser.ShowFilterContext ctx) {
    if (ctx == null) {
      return null;
    }

    String like = ctx.STRING_LITERAL().getText();
    Expression where = this.visitExpression(ctx.expression());
    return RelationalAlgebraExpressionFactory.makeShowFilter(like, where);
  }

  @Override
  public AdministrationStatement.ShowGlobalInfoClauseEnum
      visitShowGlobalInfoClause(MySqlParser.ShowGlobalInfoClauseContext ctx) {
    if (ctx == null) {
      return null;
    }

    if (ctx.ENGINES() != null) {
      if (ctx.STORAGE() != null) {
        return AdministrationStatement.ShowGlobalInfoClauseEnum.STORAGE_ENGINES;
      } else {
        return AdministrationStatement.ShowGlobalInfoClauseEnum.ENGINES;
      }
    } else if (ctx.MASTER() != null) {
      return AdministrationStatement.ShowGlobalInfoClauseEnum.MASTER_STATUS;
    } else if (ctx.PLUGINS() != null) {
      return AdministrationStatement.ShowGlobalInfoClauseEnum.PLUGINS;
    } else if (ctx.PRIVILEGES() != null) {
      return AdministrationStatement.ShowGlobalInfoClauseEnum.PRIVILEGES;
    } else if (ctx.PROCESSLIST() != null) {
      if (ctx.FULL() != null) {
        return AdministrationStatement.ShowGlobalInfoClauseEnum.FULL_PROCESSLIST;
      } else {
        return AdministrationStatement.ShowGlobalInfoClauseEnum.PROCESSLIST;
      }
    } else if (ctx.PROFILES() != null) {
      return AdministrationStatement.ShowGlobalInfoClauseEnum.PROFILES;
    } else if (ctx.HOSTS() != null) {
      return AdministrationStatement.ShowGlobalInfoClauseEnum.SLAVE_HOSTS;
    } else if (ctx.AUTHORS() != null) {
      return AdministrationStatement.ShowGlobalInfoClauseEnum.AUTHORS;
    } else if (ctx.CONTRIBUTORS() != null) {
      return AdministrationStatement.ShowGlobalInfoClauseEnum.CONTRIBUTORS;
    } else {
      throw ParserError.make(ctx);
    }
  }

  @Override
  public ShowSchemaFilter.ShowSchemaEntityEnum
      visitShowSchemaEntity(MySqlParser.ShowSchemaEntityContext ctx) {
    if (ctx == null) {
      return null;
    }

    if (ctx.EVENTS() != null) {
      return ShowSchemaFilter.ShowSchemaEntityEnum.EVENTS;
    } else if (ctx.TABLE() != null) {
      return ShowSchemaFilter.ShowSchemaEntityEnum.TABLE_STATUS;
    } else if (ctx.TABLES() != null) {
      if (ctx.FULL() != null) {
        return ShowSchemaFilter.ShowSchemaEntityEnum.FULL_TABLES;
      } else {
        return ShowSchemaFilter.ShowSchemaEntityEnum.TABLES;
      }
    } else if (ctx.TRIGGERS() != null) {
      return ShowSchemaFilter.ShowSchemaEntityEnum.TRIGGERS;
    } else {
      throw ParserError.make(ctx);
    }

  }

  @Override
  public AdministrationStatement.ShowProfileTypeEnum
      visitShowProfileType(MySqlParser.ShowProfileTypeContext ctx) {
    if (ctx == null) {
      return null;
    }

    if (ctx.ALL() != null) {
      return AdministrationStatement.ShowProfileTypeEnum.ALL;
    } else if (ctx.BLOCK() != null) {
      return AdministrationStatement.ShowProfileTypeEnum.BLOCK_IO;
    } else if (ctx.CONTEXT() != null) {
      return AdministrationStatement.ShowProfileTypeEnum.CONTEXT_SWITCHES;
    } else if (ctx.CPU() != null) {
      return AdministrationStatement.ShowProfileTypeEnum.CPU;
    } else if (ctx.IPC() != null) {
      return AdministrationStatement.ShowProfileTypeEnum.IPC;
    } else if (ctx.MEMORY() != null) {
      return AdministrationStatement.ShowProfileTypeEnum.MEMORY;
    } else if (ctx.PAGE() != null) {
      return AdministrationStatement.ShowProfileTypeEnum.PAGE_FAULTS;
    } else if (ctx.SOURCE() != null) {
      return AdministrationStatement.ShowProfileTypeEnum.SOURCE;
    } else if (ctx.SWAPS() != null) {
      return AdministrationStatement.ShowProfileTypeEnum.SWAPS;
    } else {
      throw ParserError.make(ctx);
    }

  }

  @Override
  public BinlogStatement visitBinlogStatement(MySqlParser.BinlogStatementContext ctx) {
    if (ctx == null) {
      return null;
    }
    return RelationalAlgebraExpressionFactory.makeBinlogStatement(ctx.STRING_LITERAL().getText());
  }

  @Override
  public CacheIndexStatement visitCacheIndexStatement(MySqlParser.CacheIndexStatementContext ctx) {
    if (ctx == null) {
      return null;
    }

    List<TableIndexes> tableIndexes = Lists.newArrayList();
    for (MySqlParser.TableIndexesContext tableIndexesCtx : ctx.tableIndexes()) {
      tableIndexes.add(this.visitTableIndexes(tableIndexesCtx));
    }
    UidList partitionUidList = this.visitUidList(ctx.uidList());
    Boolean partitionAll = null;
    if (ctx.ALL() != null) {
      partitionAll = Boolean.TRUE;
    }
    Uid schema = this.visitUid(ctx.schema);

    return RelationalAlgebraExpressionFactory.makeCacheIndexStatement(tableIndexes,
      partitionUidList, partitionAll, schema);
  }

  @Override
  public FlushStatement visitFlushStatement(MySqlParser.FlushStatementContext ctx) {
    if (ctx == null) {
      return null;
    }

    AdministrationStatement.FlushFormatEnum flushFormat = null;
    if (ctx.flushFormat != null) {
      flushFormat =
          AdministrationStatement.FlushFormatEnum.valueOf(ctx.flushFormat.getText().toUpperCase());
    }
    List<FlushOption> flushOptions = Lists.newArrayList();
    for (MySqlParser.FlushOptionContext flushOptionCtx : ctx.flushOption()) {
      flushOptions.add(this.visitFlushOption(flushOptionCtx));
    }

    return RelationalAlgebraExpressionFactory.makeFlushStatement(flushFormat, flushOptions);
  }

  @Override
  public KillStatement visitKillStatement(MySqlParser.KillStatementContext ctx) {
    if (ctx == null) {
      return null;
    }

    AdministrationStatement.ConnectionFormatEnum connectionFormat = null;
    if (ctx.connectionFormat != null) {
      connectionFormat = AdministrationStatement.ConnectionFormatEnum
          .valueOf(ctx.connectionFormat.getText().toUpperCase());
    }
    List<DecimalLiteral> decimalLiterals = Lists.newArrayList();
    for (MySqlParser.DecimalLiteralContext decimalLiteralCtx : ctx.decimalLiteral()) {
      decimalLiterals.add(this.visitDecimalLiteral(decimalLiteralCtx));
    }
    return RelationalAlgebraExpressionFactory.makeKillStatement(connectionFormat, decimalLiterals);
  }

  @Override
  public LoadIndexIntoCache visitLoadIndexIntoCache(MySqlParser.LoadIndexIntoCacheContext ctx) {
    if (ctx == null) {
      return null;
    }

    List<LoadedTableIndexes> loadedTableIndexes = Lists.newArrayList();
    for (MySqlParser.LoadedTableIndexesContext loadedTableIndexesCtx : ctx.loadedTableIndexes()) {
      loadedTableIndexes.add(this.visitLoadedTableIndexes(loadedTableIndexesCtx));
    }
    return RelationalAlgebraExpressionFactory.makeLoadIndexIntoCache(loadedTableIndexes);
  }

  @Override
  public ResetStatement visitResetStatement(MySqlParser.ResetStatementContext ctx) {
    if (ctx == null) {
      return null;
    }
    return RelationalAlgebraExpressionFactory.makeResetStatement();
  }

  @Override
  public ShutdownStatement visitShutdownStatement(MySqlParser.ShutdownStatementContext ctx) {
    if (ctx == null) {
      return null;
    }
    return RelationalAlgebraExpressionFactory.makeShutdownStatement();
  }

  @Override
  public TableIndexes visitTableIndexes(MySqlParser.TableIndexesContext ctx) {
    if (ctx == null) {
      return null;
    }

    TableName tableName = this.visitTableName(ctx.tableName());
    DdlStatement.IndexFormatEnum indexFormat = null;
    if (ctx.indexFormat != null) {
      indexFormat = DdlStatement.IndexFormatEnum.valueOf(ctx.indexFormat.getText().toUpperCase());
    }
    UidList uidList = this.visitUidList(ctx.uidList());
    return RelationalAlgebraExpressionFactory.makeTableIndexes(tableName, indexFormat, uidList);
  }

  public FlushOption visitFlushOption(MySqlParser.FlushOptionContext ctx) {
    if (ctx == null) {
      return null;
    }
    if (ctx instanceof MySqlParser.SimpleFlushOptionContext) {
      return this.visitSimpleFlushOption((MySqlParser.SimpleFlushOptionContext) ctx);
    } else if (ctx instanceof MySqlParser.ChannelFlushOptionContext) {
      return this.visitChannelFlushOption((MySqlParser.ChannelFlushOptionContext) ctx);
    } else if (ctx instanceof MySqlParser.TableFlushOptionContext) {
      return this.visitTableFlushOption((MySqlParser.TableFlushOptionContext) ctx);
    } else {
      throw ParserError.make(ctx);
    }
  }

  @Override
  public SimpleFlushOption visitSimpleFlushOption(MySqlParser.SimpleFlushOptionContext ctx) {
    if (ctx == null) {
      return null;
    }

    SimpleFlushOption.Type type;
    if (ctx.LOGS() != null) {
      type = SimpleFlushOption.Type.LOGS;
    } else if (ctx.getChild(0) != null) {
      type = SimpleFlushOption.Type.valueOf(ctx.getChild(0).getText().toUpperCase());
    } else {
      throw ParserError.make(ctx);
    }
    SimpleFlushOption.LogType logType = null;
    if (SimpleFlushOption.Type.LOGS.equals(type)) {
      if (ctx.getChild(0) != null) {
        logType = SimpleFlushOption.LogType.valueOf(ctx.getChild(0).getText().toUpperCase());
      }
    }
    Boolean tablesWithReadLock = null;
    if (ctx.LOCK() != null) {
      tablesWithReadLock = Boolean.TRUE;
    }

    return RelationalAlgebraExpressionFactory.makeSimpleFlushOption(type, logType,
      tablesWithReadLock);
  }

  @Override
  public ChannelFlushOption visitChannelFlushOption(MySqlParser.ChannelFlushOptionContext ctx) {
    if (ctx == null) {
      return null;
    }

    ChannelOption channelOption = this.visitChannelOption(ctx.channelOption());
    return RelationalAlgebraExpressionFactory.makeChannelFlushOption(channelOption);
  }

  @Override
  public TableFlushOption visitTableFlushOption(MySqlParser.TableFlushOptionContext ctx) {
    if (ctx == null) {
      return null;
    }

    Tables tables = this.visitTables(ctx.tables());
    AdministrationStatement.FlushTableOptionEnum flushTableOption =
        this.visitFlushTableOption(ctx.flushTableOption());
    return RelationalAlgebraExpressionFactory.makeTableFlushOption(tables, flushTableOption);
  }

  @Override
  public AdministrationStatement.FlushTableOptionEnum
      visitFlushTableOption(MySqlParser.FlushTableOptionContext ctx) {
    if (ctx == null) {
      return null;
    }

    if (ctx.WITH() != null) {
      return AdministrationStatement.FlushTableOptionEnum.WITH_READ_LOCK;
    } else if (ctx.FOR() != null) {
      return AdministrationStatement.FlushTableOptionEnum.FOR_EXPORT;
    } else {
      throw ParserError.make(ctx);
    }

  }

  @Override
  public LoadedTableIndexes visitLoadedTableIndexes(MySqlParser.LoadedTableIndexesContext ctx) {
    if (ctx == null) {
      return null;
    }

    TableName tableName = this.visitTableName(ctx.tableName());
    UidList partitionList = this.visitUidList(ctx.partitionList);
    Boolean partitionAll = null;
    if (ctx.ALL() != null) {
      partitionAll = Boolean.TRUE;
    }
    DdlStatement.IndexFormatEnum indexFormat = null;
    if (ctx.indexFormat != null) {
      indexFormat = DdlStatement.IndexFormatEnum.valueOf(ctx.indexFormat.getText().toUpperCase());
    }
    UidList indexList = this.visitUidList(ctx.indexList);
    Boolean ignoreLeaves = null;
    if (ctx.IGNORE() != null) {
      ignoreLeaves = Boolean.TRUE;
    }
    return RelationalAlgebraExpressionFactory.makeLoadedTableIndexes(tableName, partitionList,
      partitionAll, indexFormat, indexList, ignoreLeaves);
  }

  @Override
  public SimpleDescribeStatement
      visitSimpleDescribeStatement(MySqlParser.SimpleDescribeStatementContext ctx) {
    if (ctx == null) {
      return null;
    }

    SimpleDescribeStatement.CommandEnum command =
        SimpleDescribeStatement.CommandEnum.valueOf(ctx.command.getText().toUpperCase());
    TableName tableName = this.visitTableName(ctx.tableName());
    Uid column = this.visitUid(ctx.column);
    String pattern = null;
    if (ctx.STRING_LITERAL() != null) {
      pattern = ctx.STRING_LITERAL().getText();
    }
    return RelationalAlgebraExpressionFactory.makeSimpleDescribeStatement(command, tableName,
      column, pattern);
  }

  @Override
  public FullDescribeStatement
      visitFullDescribeStatement(MySqlParser.FullDescribeStatementContext ctx) {
    if (ctx == null) {
      return null;
    }

    FullDescribeStatement.CommandEnum command =
        FullDescribeStatement.CommandEnum.valueOf(ctx.command.getText().toUpperCase());
    FullDescribeStatement.FormatTypeEnum formatType = null;
    if (ctx.formatType != null) {
      formatType =
          FullDescribeStatement.FormatTypeEnum.valueOf(ctx.formatType.getText().toUpperCase());
    }
    FullDescribeStatement.FormatValueEnum formatValue = null;
    if (ctx.formatValue != null) {
      formatValue =
          FullDescribeStatement.FormatValueEnum.valueOf(ctx.formatValue.getText().toUpperCase());
    }
    DescribeObjectClause describeObjectClause =
        this.visitDescribeObjectClause(ctx.describeObjectClause());
    return RelationalAlgebraExpressionFactory.makeFullDescribeStatement(command, formatType,
      formatValue, describeObjectClause);
  }

  @Override
  public HelpStatement visitHelpStatement(MySqlParser.HelpStatementContext ctx) {
    if (ctx == null) {
      return null;
    }

    return RelationalAlgebraExpressionFactory.makeHelpStatement(ctx.STRING_LITERAL().getText());
  }

  @Override
  public UseStatement visitUseStatement(MySqlParser.UseStatementContext ctx) {
    if (ctx == null) {
      return null;
    }
    return RelationalAlgebraExpressionFactory.makeUseStatement(this.visitUid(ctx.uid()));
  }

  public DescribeObjectClause
      visitDescribeObjectClause(MySqlParser.DescribeObjectClauseContext ctx) {
    if (ctx == null) {
      return null;
    }

    if (ctx instanceof MySqlParser.DescribeStatementsContext) {
      return this.visitDescribeStatements((MySqlParser.DescribeStatementsContext) ctx);
    } else if (ctx instanceof MySqlParser.DescribeConnectionContext) {
      return this.visitDescribeConnection((MySqlParser.DescribeConnectionContext) ctx);
    } else {
      throw ParserError.make(ctx);
    }
  }

  @Override
  public DescribeStatements visitDescribeStatements(MySqlParser.DescribeStatementsContext ctx) {
    if (ctx == null) {
      return null;
    }

    SelectStatement selectStatement = this.visitSelectStatement(ctx.selectStatement());
    DeleteStatement deleteStatement = this.visitDeleteStatement(ctx.deleteStatement());
    InsertStatement insertStatement = this.visitInsertStatement(ctx.insertStatement());
    ReplaceStatement replaceStatement = this.visitReplaceStatement(ctx.replaceStatement());
    UpdateStatement updateStatement = this.visitUpdateStatement(ctx.updateStatement());
    return RelationalAlgebraExpressionFactory.makeDescribeStatements(selectStatement,
      deleteStatement, insertStatement, replaceStatement, updateStatement);
  }

  @Override
  public DescribeConnection visitDescribeConnection(MySqlParser.DescribeConnectionContext ctx) {
    if (ctx == null) {
      return null;
    }

    Uid uid = this.visitUid(ctx.uid());
    return RelationalAlgebraExpressionFactory.makeDescribeConnection(uid);
  }

  @Override
  public FullId visitFullId(MySqlParser.FullIdContext ctx) {
    if (ctx == null) {
      return null;
    }

    List<MySqlParser.UidContext> uidCtxs = ctx.uid();
    List<Uid> uids = Lists.newArrayList();
    if (CollectionUtils.isNotEmpty(uidCtxs)) {
      for (MySqlParser.UidContext uidCtx : uidCtxs) {
        uids.add(this.visitUid(uidCtx));
      }
    }
    TerminalNode DOT_ID = ctx.DOT_ID();
    String dotId = null;
    if (DOT_ID != null) {
      dotId = DOT_ID.getText();
    }

    return RelationalAlgebraExpressionFactory.makeFullId(uids, dotId);
  }

  @Override
  public TableName visitTableName(MySqlParser.TableNameContext ctx) {
    if (ctx == null) {
      return null;
    }

    MySqlParser.FullIdContext fullIdCtx = ctx.fullId();
    FullId fullId = this.visitFullId(fullIdCtx);

    return RelationalAlgebraExpressionFactory.makeTableName(fullId);
  }

  @Override
  public FullColumnName visitFullColumnName(MySqlParser.FullColumnNameContext ctx) {
    if (ctx == null) {
      return null;
    }

    MySqlParser.UidContext uidCtx = ctx.uid();
    Uid uid = this.visitUid(uidCtx);

    List<MySqlParser.DottedIdContext> dottedIdCtxs = ctx.dottedId();
    List<DottedId> dottedIds = Lists.newArrayList();
    if (CollectionUtils.isNotEmpty(dottedIdCtxs)) {
      for (MySqlParser.DottedIdContext dottedIdCtx : dottedIdCtxs) {
        dottedIds.add(this.visitDottedId(dottedIdCtx));
      }
    }

    return RelationalAlgebraExpressionFactory.makeFullColumnName(uid, dottedIds);
  }

  @Override
  public IndexColumnName visitIndexColumnName(MySqlParser.IndexColumnNameContext ctx) {
    if (ctx == null) {
      return null;
    }

    Uid uid = this.visitUid(ctx.uid());
    String stringLiteral = null;
    if (ctx.STRING_LITERAL() != null) {
      stringLiteral = ctx.STRING_LITERAL().getText();
    }
    DecimalLiteral decimalLiteral = this.visitDecimalLiteral(ctx.decimalLiteral());
    IndexColumnName.SortType sortType = null;
    if (ctx.sortType != null) {
      sortType = IndexColumnName.SortType.valueOf(ctx.sortType.getText().toUpperCase());
    }
    return RelationalAlgebraExpressionFactory.makeIndexColumnName(uid, stringLiteral,
      decimalLiteral, sortType);
  }

  @Override
  public UserName visitUserName(MySqlParser.UserNameContext ctx) {
    if (ctx == null) {
      return null;
    }

    UserName.Type type = null;
    String literal = null;
    if (ctx.STRING_USER_NAME() != null) {
      type = UserName.Type.STRING_USER_NAME;
      literal = ctx.STRING_USER_NAME().getText();
    } else if (ctx.ID() != null) {
      type = UserName.Type.ID;
      literal = ctx.ID().getText();
    } else if (ctx.STRING_LITERAL() != null) {
      type = UserName.Type.STRING_LITERAL;
      literal = ctx.STRING_LITERAL().getText();
    }

    return RelationalAlgebraExpressionFactory.makeUserName(type, literal);
  }

  @Override
  public MysqlVariable visitMysqlVariable(MySqlParser.MysqlVariableContext ctx) {
    if (ctx == null) {
      return null;
    }

    String localId = null;
    if (ctx.LOCAL_ID() != null) {
      localId = ctx.LOCAL_ID().getText();
    }
    String globalId = null;
    if (ctx.GLOBAL_ID() != null) {
      globalId = ctx.GLOBAL_ID().getText();
    }

    return RelationalAlgebraExpressionFactory.makeMysqlVariable(localId, globalId);
  }

  @Override
  public CharsetName visitCharsetName(MySqlParser.CharsetNameContext ctx) {
    if (ctx == null) {
      return null;
    }

    CharsetName.Type type = null;
    String literal = null;
    if (ctx.BINARY() != null) {
      type = CharsetName.Type.BINARY;
    } else if (ctx.charsetNameBase() != null) {
      type = CharsetName.Type.CHARSET_NAME_BASE;
      literal = this.visitCharsetNameBase(ctx.charsetNameBase()).literal();
    } else if (ctx.STRING_LITERAL() != null) {
      type = CharsetName.Type.STRING_LITERAL;
      literal = ctx.STRING_LITERAL().getText();
    } else if (ctx.CHARSET_REVERSE_QOUTE_STRING() != null) {
      type = CharsetName.Type.CHARSET_REVERSE_QOUTE_STRING;
      literal = ctx.CHARSET_REVERSE_QOUTE_STRING().getText();
    } else {
      throw ParserError.make(ctx);
    }

    return RelationalAlgebraExpressionFactory.makeCharsetName(type, literal);
  }

  @Override
  public CollationName visitCollationName(MySqlParser.CollationNameContext ctx) {
    if (ctx == null) {
      return null;
    }

    Uid uid = this.visitUid(ctx.uid());
    TerminalNode STRING_LITERAL = ctx.STRING_LITERAL();
    String stringLiteral = null;
    if (STRING_LITERAL != null) {
      stringLiteral = STRING_LITERAL.getText();
    }
    return RelationalAlgebraExpressionFactory.makeCollationName(uid, stringLiteral);
  }

  @Override
  public EngineName visitEngineName(MySqlParser.EngineNameContext ctx) {
    if (ctx == null) {
      return null;
    }

    EngineName.Type type = null;
    String literal = null;
    // REMOVED BY zhoujiagen: 20190822
    // if (ctx.STRING_LITERAL() != null) {
    // type = EngineName.Type.STRING_LITERAL;
    // literal = ctx.STRING_LITERAL().getText();
    // } else if (ctx.REVERSE_QUOTE_ID() != null) {
    // type = EngineName.Type.REVERSE_QUOTE_ID;
    // literal = ctx.REVERSE_QUOTE_ID().getText();
    // } else {
    type = EngineName.Type.valueOf(ctx.getChild(0).getText().toUpperCase());
    literal = ctx.getChild(0).getText();
    // }

    return RelationalAlgebraExpressionFactory.makeEngineName(type, literal);
  }

  @Override
  public UuidSet visitUuidSet(MySqlParser.UuidSetContext ctx) {
    if (ctx == null) {
      return null;
    }

    List<DecimalLiteral> decimalLiterals = Lists.newArrayList();
    int i = 0;
    for (; i < 5; i++) {
      decimalLiterals.add(this.visitDecimalLiteral(ctx.decimalLiteral(i)));
    }
    int totalSize = ctx.decimalLiteral().size();
    List<DecimalLiteral> colonDecimalLiterals = Lists.newArrayList();
    for (; i < totalSize; i++) {
      colonDecimalLiterals.add(this.visitDecimalLiteral(ctx.decimalLiteral(i)));
    }

    return RelationalAlgebraExpressionFactory.makeUuidSet(decimalLiterals, colonDecimalLiterals);
  }

  @Override
  public Xid visitXid(MySqlParser.XidContext ctx) {
    if (ctx == null) {
      return null;
    }

    XuidStringId globalTableUid = this.visitXuidStringId(ctx.globalTableUid);
    XuidStringId qualifier = this.visitXuidStringId(ctx.qualifier);
    DecimalLiteral idFormat = this.visitDecimalLiteral(ctx.idFormat);

    return RelationalAlgebraExpressionFactory.makeXid(globalTableUid, qualifier, idFormat);
  }

  @Override
  public XuidStringId visitXuidStringId(MySqlParser.XuidStringIdContext ctx) {
    if (ctx == null) {
      return null;
    }

    XuidStringId.Type type;
    List<String> literals = Lists.newArrayList();
    if (ctx.STRING_LITERAL() != null) {
      type = XuidStringId.Type.STRING_LITERAL;
      literals.add(ctx.STRING_LITERAL().getText());
    } else if (ctx.BIT_STRING() != null) {
      type = XuidStringId.Type.BIT_STRING;
      literals.add(ctx.BIT_STRING().getText());
    } else if (ctx.HEXADECIMAL_LITERAL() != null) {
      type = XuidStringId.Type.HEXADECIMAL_LITERAL;
      for (TerminalNode hexadecimalLiteral : ctx.HEXADECIMAL_LITERAL()) {
        literals.add(hexadecimalLiteral.getText());
      }
    } else {
      throw ParserError.make(ctx);
    }

    return RelationalAlgebraExpressionFactory.makeXuidStringId(type, literals);
  }

  @Override
  public AuthPlugin visitAuthPlugin(MySqlParser.AuthPluginContext ctx) {
    if (ctx == null) {
      return null;
    }

    Uid uid = this.visitUid(ctx.uid());
    String stringLiteral = null;
    if (ctx.STRING_LITERAL() != null) {
      stringLiteral = ctx.STRING_LITERAL().getText();
    }
    return RelationalAlgebraExpressionFactory.makeAuthPlugin(uid, stringLiteral);
  }

  @Override
  public Uid visitUid(MySqlParser.UidContext ctx) {
    if (ctx == null) {
      return null;
    }

    MySqlParser.SimpleIdContext simpleIdCtx = ctx.simpleId();
    SimpleId simpleId = this.visitSimpleId(simpleIdCtx);

    Uid.Type type = null;
    if (simpleIdCtx != null) {
      type = Uid.Type.SIMPLE_ID;
    } else if (ctx.REVERSE_QUOTE_ID() != null) {
      type = Uid.Type.REVERSE_QUOTE_ID;
    } else if (ctx.CHARSET_REVERSE_QOUTE_STRING() != null) {
      type = Uid.Type.CHARSET_REVERSE_QOUTE_STRING;
    }

    return RelationalAlgebraExpressionFactory.makeUid(type, simpleId.literal);
  }

  @Override
  public SimpleId visitSimpleId(MySqlParser.SimpleIdContext ctx) {
    if (ctx == null) {
      return null;
    }

    TerminalNode ID = ctx.ID();
    MySqlParser.CharsetNameBaseContext charsetNameBaseCtx = ctx.charsetNameBase();
    MySqlParser.TransactionLevelBaseContext transactionLevelBaseCtx = ctx.transactionLevelBase();
    MySqlParser.EngineNameContext engineNameCtx = ctx.engineName();
    MySqlParser.PrivilegesBaseContext privilegesBaseCtx = ctx.privilegesBase();
    MySqlParser.IntervalTypeBaseContext intervalTypeBaseCtx = ctx.intervalTypeBase();
    MySqlParser.DataTypeBaseContext dataTypeBaseCtx = ctx.dataTypeBase();
    MySqlParser.KeywordsCanBeIdContext keywordsCanBeIdCtx = ctx.keywordsCanBeId();
    MySqlParser.FunctionNameBaseContext functionNameBaseContext = ctx.functionNameBase();

    SimpleId.Type type = null;
    String literal = null;
    if (ID != null) {
      type = SimpleId.Type.ID;
      literal = ID.getText();
    } else if (charsetNameBaseCtx != null) {
      type = SimpleId.Type.CHARSET_NAME_BASE;
      literal = charsetNameBaseCtx.getText();
    } else if (transactionLevelBaseCtx != null) {
      type = SimpleId.Type.TRANSACTION_LEVEL_BASE;
      literal = transactionLevelBaseCtx.getText();
    } else if (engineNameCtx != null) {
      type = SimpleId.Type.ENGINE_NAME;
      literal = engineNameCtx.getText();
    } else if (privilegesBaseCtx != null) {
      type = SimpleId.Type.PRIVILEGES_BASE;
      literal = privilegesBaseCtx.getText();
    } else if (intervalTypeBaseCtx != null) {
      type = SimpleId.Type.INTERVAL_TYPE_BASE;
      literal = intervalTypeBaseCtx.getText();
    } else if (dataTypeBaseCtx != null) {
      type = SimpleId.Type.DATA_TYPE_BASE;
      literal = dataTypeBaseCtx.getText();
    } else if (keywordsCanBeIdCtx != null) {
      type = SimpleId.Type.KEYWORDS_CAN_BE_ID;
      literal = keywordsCanBeIdCtx.getText();
    } else if (functionNameBaseContext != null) {
      type = SimpleId.Type.FUNCTION_NAME_BASE;
      literal = functionNameBaseContext.getText();
    } else {
      throw ParserError.make(ctx);
    }

    return RelationalAlgebraExpressionFactory.makeSimpleId(type, literal);
  }

  @Override
  public DottedId visitDottedId(MySqlParser.DottedIdContext ctx) {
    if (ctx == null) {
      return null;
    }

    String dotId = null;
    if (ctx.DOT_ID() != null) {
      dotId = ctx.DOT_ID().getText();
    }
    Uid uid = this.visitUid(ctx.uid());

    return RelationalAlgebraExpressionFactory.makeDottedId(dotId, uid);
  }

  @Override
  public DecimalLiteral visitDecimalLiteral(MySqlParser.DecimalLiteralContext ctx) {
    if (ctx == null) {
      return null;
    }

    DecimalLiteral.Type type = null;
    String literal = null;

    if (ctx.DECIMAL_LITERAL() != null) {
      type = DecimalLiteral.Type.DECIMAL_LITERAL;
      literal = ctx.DECIMAL_LITERAL().getText();
    } else if (ctx.ZERO_DECIMAL() != null) {
      type = DecimalLiteral.Type.ZERO_DECIMAL;
      literal = ctx.ZERO_DECIMAL().getText();
    } else if (ctx.ONE_DECIMAL() != null) {
      type = DecimalLiteral.Type.ONE_DECIMAL;
      literal = ctx.ONE_DECIMAL().getText();
    } else if (ctx.TWO_DECIMAL() != null) {
      type = DecimalLiteral.Type.TWO_DECIMAL;
      literal = ctx.TWO_DECIMAL().getText();
    } else {
      throw ParserError.make(ctx);
    }

    return RelationalAlgebraExpressionFactory.makeDecimalLiteral(type, literal);
  }

  @Override
  public FileSizeLiteral visitFileSizeLiteral(MySqlParser.FileSizeLiteralContext ctx) {
    if (ctx == null) {
      return null;
    }

    String filesizeLiteral = null;
    if (ctx.FILESIZE_LITERAL() != null) {
      filesizeLiteral = ctx.FILESIZE_LITERAL().getText();
    }
    DecimalLiteral decimalLiteral = this.visitDecimalLiteral(ctx.decimalLiteral());

    return RelationalAlgebraExpressionFactory.makeFileSizeLiteral(filesizeLiteral, decimalLiteral);
  }

  @Override
  public StringLiteral visitStringLiteral(MySqlParser.StringLiteralContext ctx) {
    if (ctx == null) {
      return null;
    }

    TerminalNode STRING_CHARSET_NAME = ctx.STRING_CHARSET_NAME();
    SimpleIdSets.CharsetNameBaseEnum charsetNameBaseEnum = null;
    if (STRING_CHARSET_NAME != null) {
      charsetNameBaseEnum =
          SimpleIdSets.CharsetNameBaseEnum.valueOf(STRING_CHARSET_NAME.getText().toUpperCase());
    }

    // TODO(zhoujiagen) need trim pre and post '/""/'N'
    // STRING_LITERAL: DQUOTA_STRING | SQUOTA_STRING | BQUOTA_STRING;
    List<TerminalNode> STRING_LITERALs = ctx.STRING_LITERAL();
    List<String> stringLiterals = Lists.newArrayList();
    if (CollectionUtils.isNotEmpty(STRING_LITERALs)) {
      for (TerminalNode STRING_LITERAL : STRING_LITERALs) {
        stringLiterals.add(STRING_LITERAL.getText());
      }
    }

    // START_NATIONAL_STRING_LITERAL: 'N' SQUOTA_STRING;
    TerminalNode START_NATIONAL_STRING_LITERAL = ctx.START_NATIONAL_STRING_LITERAL();
    String startNationalStringLiteral = null;
    if (START_NATIONAL_STRING_LITERAL != null) {
      startNationalStringLiteral = START_NATIONAL_STRING_LITERAL.getText();
    }

    CollationName collationName = this.visitCollationName(ctx.collationName());

    return RelationalAlgebraExpressionFactory.makeStringLiteral(charsetNameBaseEnum, stringLiterals,
      startNationalStringLiteral, collationName);
  }

  @Override
  public BooleanLiteral visitBooleanLiteral(MySqlParser.BooleanLiteralContext ctx) {
    if (ctx == null) {
      return null;
    }

    Boolean literal = null;
    if (ctx.TRUE() != null) {
      literal = Boolean.TRUE;
    } else if (ctx.FALSE() != null) {
      literal = Boolean.FALSE;
    } else {
      throw ParserError.make(ctx);
    }

    return RelationalAlgebraExpressionFactory.makeBooleanLiteral(literal);
  }

  @Override
  public HexadecimalLiteral visitHexadecimalLiteral(MySqlParser.HexadecimalLiteralContext ctx) {
    if (ctx == null) {
      return null;
    }

    TerminalNode STRING_CHARSET_NAME = ctx.STRING_CHARSET_NAME();
    SimpleIdSets.CharsetNameBaseEnum charsetNameBaseEnum = null;
    if (STRING_CHARSET_NAME != null) {
      charsetNameBaseEnum =
          SimpleIdSets.CharsetNameBaseEnum.valueOf(STRING_CHARSET_NAME.getText().toUpperCase());
    }

    return RelationalAlgebraExpressionFactory.makeHexadecimalLiteral(charsetNameBaseEnum,
      ctx.HEXADECIMAL_LITERAL().getText());
  }

  @Override
  public NullNotnull visitNullNotnull(MySqlParser.NullNotnullContext ctx) {
    if (ctx == null) {
      return null;
    }

    Boolean not = null;
    if (ctx.NOT() != null) {
      not = Boolean.TRUE;
    }
    String nullLiteral = null;
    if (ctx.NULL_LITERAL() != null) {
      nullLiteral = ctx.NULL_LITERAL().getText();
    }
    String nullSpecLiteral = null;
    if (ctx.NULL_SPEC_LITERAL() != null) {
      nullSpecLiteral = ctx.NULL_SPEC_LITERAL().getText();
    }

    return RelationalAlgebraExpressionFactory.makeNullNotnull(not, nullLiteral, nullSpecLiteral);
  }

  @Override
  public Constant visitConstant(MySqlParser.ConstantContext ctx) {
    if (ctx == null) {
      return null;
    }

    Constant.Type type = null;
    String literal = null;
    Boolean not = null;

    if (ctx.stringLiteral() != null) {
      type = Constant.Type.STRING_LITERAL;
      StringLiteral stringLiteral = this.visitStringLiteral(ctx.stringLiteral());
      literal = stringLiteral.literal();
    } else if (ctx.decimalLiteral() != null) {
      type = Constant.Type.DECIMAL_LITERAL;
      DecimalLiteral decimalLiteral = this.visitDecimalLiteral(ctx.decimalLiteral());
      if ("-".equals(ctx.start.getText())) {
        literal = "-" + decimalLiteral.literal();
      } else {
        literal = decimalLiteral.literal();
      }
    } else if (ctx.hexadecimalLiteral() != null) {
      type = Constant.Type.HEXADECIMAL_LITERAL;
      HexadecimalLiteral hexadecimalLiteral =
          this.visitHexadecimalLiteral(ctx.hexadecimalLiteral());
      literal = hexadecimalLiteral.literal();
    } else if (ctx.booleanLiteral() != null) {
      type = Constant.Type.BOOLEAN_LITERAL;
      BooleanLiteral booleanLiteral = this.visitBooleanLiteral(ctx.booleanLiteral());
      literal = booleanLiteral.literal();
    } else if (ctx.REAL_LITERAL() != null) {
      type = Constant.Type.REAL_LITERAL;
      literal = ctx.REAL_LITERAL().getText();
    } else if (ctx.BIT_STRING() != null) {
      type = Constant.Type.BIT_STRING;
      literal = ctx.BIT_STRING().getText();
    } else if (ctx.nullLiteral != null) {
      type = Constant.Type.NULL_LITERAL;
      if (ctx.NOT() != null) {
        not = Boolean.TRUE;
      }
      if (ctx.NULL_LITERAL() != null) {
        literal = ctx.NULL_LITERAL().getText();
      } else if (ctx.NULL_SPEC_LITERAL() != null) {
        literal = ctx.NULL_SPEC_LITERAL().getText();
      } else {
        throw ParserError.make(ctx);
      }
    } else {
      throw ParserError.make(ctx);
    }

    return RelationalAlgebraExpressionFactory.makeConstant(type, literal, not);
  }

  public DataType visitDataType(MySqlParser.DataTypeContext ctx) {
    if (ctx == null) {
      return null;
    }

    if (ctx instanceof MySqlParser.StringDataTypeContext) {
      return this.visitStringDataType((MySqlParser.StringDataTypeContext) ctx);
    } else if (ctx instanceof MySqlParser.NationalStringDataTypeContext) {
      return this.visitNationalStringDataType((MySqlParser.NationalStringDataTypeContext) ctx);
    } else if (ctx instanceof MySqlParser.NationalStringDataTypeContext) {
      return this.visitNationalStringDataType((MySqlParser.NationalStringDataTypeContext) ctx);
    } else if (ctx instanceof MySqlParser.NationalVaryingStringDataTypeContext) {
      return this.visitNationalVaryingStringDataType(
        (MySqlParser.NationalVaryingStringDataTypeContext) ctx);
    } else if (ctx instanceof MySqlParser.DimensionDataTypeContext) {
      return this.visitDimensionDataType((MySqlParser.DimensionDataTypeContext) ctx);
    } else if (ctx instanceof MySqlParser.DimensionDataTypeContext) {
      return this.visitDimensionDataType((MySqlParser.DimensionDataTypeContext) ctx);
    } else if (ctx instanceof MySqlParser.DimensionDataTypeContext) {
      return this.visitDimensionDataType((MySqlParser.DimensionDataTypeContext) ctx);
    } else if (ctx instanceof MySqlParser.DimensionDataTypeContext) {
      return this.visitDimensionDataType((MySqlParser.DimensionDataTypeContext) ctx);
    } else if (ctx instanceof MySqlParser.SimpleDataTypeContext) {
      return this.visitSimpleDataType((MySqlParser.SimpleDataTypeContext) ctx);
    } else if (ctx instanceof MySqlParser.DimensionDataTypeContext) {
      return this.visitDimensionDataType((MySqlParser.DimensionDataTypeContext) ctx);
    } else if (ctx instanceof MySqlParser.CollectionDataTypeContext) {
      return this.visitCollectionDataType((MySqlParser.CollectionDataTypeContext) ctx);
    } else if (ctx instanceof MySqlParser.SpatialDataTypeContext) {
      return this.visitSpatialDataType((MySqlParser.SpatialDataTypeContext) ctx);
    } else {
      throw ParserError.make(ctx);
    }
  }

  @Override
  public StringDataType visitStringDataType(MySqlParser.StringDataTypeContext ctx) {
    if (ctx == null) {
      return null;
    }

    StringDataType.Type dataType =
        StringDataType.Type.valueOf(ctx.getChild(0).getText().toUpperCase());
    LengthOneDimension lengthOneDimension = this.visitLengthOneDimension(ctx.lengthOneDimension());
    Boolean binary = null;
    if (ctx.BINARY() != null) {
      binary = Boolean.TRUE;
    }
    CharsetName charsetName = this.visitCharsetName(ctx.charsetName());
    CollationName collationName = this.visitCollationName(ctx.collationName());
    return RelationalAlgebraExpressionFactory.makeStringDataType(dataType, lengthOneDimension,
      binary, charsetName, collationName);
  }

  @Override
  public NationalStringDataType
      visitNationalStringDataType(MySqlParser.NationalStringDataTypeContext ctx) {
    if (ctx == null) {
      return null;
    }

    NationalStringDataType.NType type =
        NationalStringDataType.NType.valueOf(ctx.getChild(0).getText().toUpperCase());
    NationalStringDataType.Type dataType =
        NationalStringDataType.Type.valueOf(ctx.typeName.getText().toUpperCase());
    LengthOneDimension lengthOneDimension = this.visitLengthOneDimension(ctx.lengthOneDimension());
    Boolean binary = null;
    if (ctx.BINARY() != null) {
      binary = Boolean.TRUE;
    }
    return RelationalAlgebraExpressionFactory.makeNationalStringDataType(type, dataType,
      lengthOneDimension, binary);
  }

  @Override
  public NationalVaryingStringDataType
      visitNationalVaryingStringDataType(MySqlParser.NationalVaryingStringDataTypeContext ctx) {
    if (ctx == null) {
      return null;
    }

    NationalVaryingStringDataType.Type dataType =
        NationalVaryingStringDataType.Type.valueOf(ctx.typeName.getText().toUpperCase());
    LengthOneDimension lengthOneDimension = this.visitLengthOneDimension(ctx.lengthOneDimension());
    Boolean binary = null;
    if (ctx.BINARY() != null) {
      binary = Boolean.TRUE;
    }

    return RelationalAlgebraExpressionFactory.makeNationalVaryingStringDataType(dataType,
      lengthOneDimension, binary);
  }

  @Override
  public DimensionDataType visitDimensionDataType(MySqlParser.DimensionDataTypeContext ctx) {
    if (ctx == null) {
      return null;
    }

    DimensionDataType.Type dataType =
        DimensionDataType.Type.valueOf(ctx.typeName.getText().toUpperCase());
    LengthOneDimension lengthOneDimension = this.visitLengthOneDimension(ctx.lengthOneDimension());
    LengthTwoDimension lengthTwoDimension = this.visitLengthTwoDimension(ctx.lengthTwoDimension());
    LengthTwoOptionalDimension lengthTwoOptionalDimension =
        this.visitLengthTwoOptionalDimension(ctx.lengthTwoOptionalDimension());
    Boolean signed = null;
    if (ctx.SIGNED() != null) {
      signed = Boolean.TRUE;
    } else if (ctx.UNSIGNED() != null) {
      signed = Boolean.FALSE;
    }
    Boolean zeroFill = null;
    if (ctx.ZEROFILL() != null) {
      zeroFill = Boolean.TRUE;
    }
    Boolean precision = null;
    if (ctx.PRECISION() != null) {
      precision = Boolean.TRUE;
    }
    return RelationalAlgebraExpressionFactory.makeDimensionDataType(dataType, lengthOneDimension,
      lengthTwoDimension, lengthTwoOptionalDimension, signed, zeroFill, precision);
  }

  @Override
  public SimpleDataType visitSimpleDataType(MySqlParser.SimpleDataTypeContext ctx) {
    if (ctx == null) {
      return null;
    }

    SimpleDataType.Type dataType =
        SimpleDataType.Type.valueOf(ctx.typeName.getText().toUpperCase());
    return RelationalAlgebraExpressionFactory.makeSimpleDataType(dataType);
  }

  @Override
  public CollectionDataType visitCollectionDataType(MySqlParser.CollectionDataTypeContext ctx) {
    if (ctx == null) {
      return null;
    }

    CollectionDataType.Type dataType =
        CollectionDataType.Type.valueOf(ctx.typeName.getText().toUpperCase());
    CollectionOptions collectionOptions = this.visitCollectionOptions(ctx.collectionOptions());
    Boolean binary = null;
    if (ctx.BINARY() != null) {
      binary = Boolean.TRUE;
    }
    CharsetName charsetName = this.visitCharsetName(ctx.charsetName());
    return RelationalAlgebraExpressionFactory.makeCollectionDataType(dataType, collectionOptions,
      binary, charsetName);
  }

  @Override
  public SpatialDataType visitSpatialDataType(MySqlParser.SpatialDataTypeContext ctx) {
    if (ctx == null) {
      return null;
    }

    SpatialDataType.Type dataType =
        SpatialDataType.Type.valueOf(ctx.typeName.getText().toUpperCase());
    return RelationalAlgebraExpressionFactory.makeSpatialDataType(dataType);
  }

  @Override
  public CollectionOptions visitCollectionOptions(MySqlParser.CollectionOptionsContext ctx) {
    if (ctx == null) {
      return null;
    }

    List<String> stringLiterals = Lists.newArrayList();
    String text = null;
    for (ParseTree child : ctx.children) {
      text = child.getText();
      if ("(".equals(text) || ",".equals(text) || ")".equals(text)) {
        // ignore
      } else {
        stringLiterals.add(text);
      }
    }
    return RelationalAlgebraExpressionFactory.makeCollectionOptions(stringLiterals);
  }

  @Override
  public ConvertedDataType visitConvertedDataType(MySqlParser.ConvertedDataTypeContext ctx) {
    if (ctx == null) {
      return null;
    }

    ConvertedDataType.Type type = null;
    LengthOneDimension lengthOneDimension = null;
    CharsetName charsetName = null;
    LengthTwoDimension lengthTwoDimension;
    Boolean signed = null;
    if (ctx.typeName != null) {
      type = ConvertedDataType.Type.valueOf(ctx.typeName.getText().toUpperCase());
    } else {
      type = ConvertedDataType.Type.INTEGER;
    }
    switch (type) {
    case BINARY:
    case NCHAR:
      lengthOneDimension = this.visitLengthOneDimension(ctx.lengthOneDimension());
      return RelationalAlgebraExpressionFactory.makeConvertedDataType(type, lengthOneDimension);
    case CHAR:
      charsetName = this.visitCharsetName(ctx.charsetName());
      return RelationalAlgebraExpressionFactory.makeConvertedDataType(type, lengthOneDimension,
        charsetName);
    case DATE:
    case DATETIME:
    case TIME:
      return RelationalAlgebraExpressionFactory.makeConvertedDataType(type);
    case DECIMAL:
      lengthTwoDimension = this.visitLengthTwoDimension(ctx.lengthTwoDimension());
      return RelationalAlgebraExpressionFactory.makeConvertedDataType(type, lengthTwoDimension);
    case INTEGER:
      if (ctx.SIGNED() != null) {
        signed = Boolean.TRUE;
      } else if (ctx.UNSIGNED() != null) {
        signed = Boolean.FALSE;
      } else {
        throw ParserError.make(ctx);
      }
      return RelationalAlgebraExpressionFactory.makeConvertedDataType(signed, type);
    default:
      throw ParserError.make(ctx);
    }
  }

  @Override
  public LengthOneDimension visitLengthOneDimension(MySqlParser.LengthOneDimensionContext ctx) {
    if (ctx == null) {
      return null;
    }

    DecimalLiteral decimalLiteral = this.visitDecimalLiteral(ctx.decimalLiteral());
    return RelationalAlgebraExpressionFactory.makeLengthOneDimension(decimalLiteral);
  }

  @Override
  public LengthTwoDimension visitLengthTwoDimension(MySqlParser.LengthTwoDimensionContext ctx) {
    if (ctx == null) {
      return null;
    }

    List<MySqlParser.DecimalLiteralContext> decimalLiteralCtxs = ctx.decimalLiteral();
    List<DecimalLiteral> decimalLiterals = Lists.newArrayList();
    if (CollectionUtils.isNotEmpty(decimalLiteralCtxs)) {
      for (MySqlParser.DecimalLiteralContext decimalLiteralCtx : decimalLiteralCtxs) {
        decimalLiterals.add(this.visitDecimalLiteral(decimalLiteralCtx));
      }
    }

    if (decimalLiterals.size() >= 2) {
      return RelationalAlgebraExpressionFactory.makeLengthTwoDimension(decimalLiterals.get(0),
        decimalLiterals.get(1));
    } else {
      throw ParserError.make(ctx);
    }
  }

  @Override
  public LengthTwoOptionalDimension
      visitLengthTwoOptionalDimension(MySqlParser.LengthTwoOptionalDimensionContext ctx) {
    if (ctx == null) {
      return null;
    }

    List<MySqlParser.DecimalLiteralContext> decimalLiteralCtxs = ctx.decimalLiteral();
    List<DecimalLiteral> decimalLiterals = Lists.newArrayList();
    if (CollectionUtils.isNotEmpty(decimalLiteralCtxs)) {
      for (MySqlParser.DecimalLiteralContext decimalLiteralCtx : decimalLiteralCtxs) {
        decimalLiterals.add(this.visitDecimalLiteral(decimalLiteralCtx));
      }
    }

    if (decimalLiterals.size() > 0) {
      if (decimalLiterals.size() == 1) {
        return RelationalAlgebraExpressionFactory.makeLengthTwoOptionalDimension(//
          decimalLiterals.get(0), null);
      } else if (decimalLiterals.size() == 2) {
        return RelationalAlgebraExpressionFactory.makeLengthTwoOptionalDimension(//
          decimalLiterals.get(0), decimalLiterals.get(1));
      } else {
        throw ParserError.make(ctx);
      }
    } else {
      throw ParserError.make(ctx);
    }
  }

  @Override
  public UidList visitUidList(MySqlParser.UidListContext ctx) {
    if (ctx == null) {
      return null;
    }

    List<Uid> uids = Lists.newArrayList();
    for (MySqlParser.UidContext uidCtx : ctx.uid()) {
      uids.add(this.visitUid(uidCtx));
    }
    return RelationalAlgebraExpressionFactory.makeUidList(uids);
  }

  @Override
  public Tables visitTables(MySqlParser.TablesContext ctx) {
    if (ctx == null) {
      return null;
    }

    List<TableName> tableNames = Lists.newArrayList();
    for (MySqlParser.TableNameContext tableNameCtx : ctx.tableName()) {
      tableNames.add(this.visitTableName(tableNameCtx));
    }
    return RelationalAlgebraExpressionFactory.makeTables(tableNames);
  }

  @Override
  public IndexColumnNames visitIndexColumnNames(MySqlParser.IndexColumnNamesContext ctx) {
    if (ctx == null) {
      return null;
    }

    List<IndexColumnName> indexColumnNames = Lists.newArrayList();
    for (MySqlParser.IndexColumnNameContext indexColumnNameCtx : ctx.indexColumnName()) {
      indexColumnNames.add(this.visitIndexColumnName(indexColumnNameCtx));
    }
    return RelationalAlgebraExpressionFactory.makeIndexColumnNames(indexColumnNames);
  }

  @Override
  public Expressions visitExpressions(MySqlParser.ExpressionsContext ctx) {
    if (ctx == null) {
      return null;
    }

    List<Expression> expressions = Lists.newArrayList();
    for (MySqlParser.ExpressionContext expressionCtx : ctx.expression()) {
      expressions.add(this.visitExpression(expressionCtx));
    }
    return RelationalAlgebraExpressionFactory.makeExpressions(expressions);
  }

  @Override
  public ExpressionsWithDefaults
      visitExpressionsWithDefaults(MySqlParser.ExpressionsWithDefaultsContext ctx) {
    if (ctx == null) {
      return null;
    }

    List<ExpressionOrDefault> expressionOrDefaults = Lists.newArrayList();
    for (MySqlParser.ExpressionOrDefaultContext expressionOrDefaultCtx : ctx
        .expressionOrDefault()) {
      expressionOrDefaults.add(this.visitExpressionOrDefault(expressionOrDefaultCtx));
    }
    return RelationalAlgebraExpressionFactory.makeExpressionsWithDefaults(expressionOrDefaults);
  }

  @Override
  public Constants visitConstants(MySqlParser.ConstantsContext ctx) {
    if (ctx == null) {
      return null;
    }

    List<Constant> constants = Lists.newArrayList();
    for (MySqlParser.ConstantContext constantCtx : ctx.constant()) {
      constants.add(this.visitConstant(constantCtx));
    }
    return RelationalAlgebraExpressionFactory.makeConstants(constants);
  }

  @Override
  public SimpleStrings visitSimpleStrings(MySqlParser.SimpleStringsContext ctx) {
    if (ctx == null) {
      return null;
    }

    List<String> stringLiterals = Lists.newArrayList();
    String text = null;
    for (ParseTree child : ctx.children) {
      text = child.getText();
      if (",".equals(text)) {
        // ignore
      } else {
        stringLiterals.add(text);
      }
    }
    return RelationalAlgebraExpressionFactory.makeSimpleStrings(stringLiterals);
  }

  @Override
  public UserVariables visitUserVariables(MySqlParser.UserVariablesContext ctx) {
    if (ctx == null) {
      return null;
    }

    List<String> localIds = Lists.newArrayList();
    String text = null;
    for (ParseTree child : ctx.children) {
      text = child.getText();
      if (",".equals(text)) {
        // ignore
      } else {
        localIds.add(text);
      }
    }
    return RelationalAlgebraExpressionFactory.makeUserVariables(localIds);
  }

  @Override
  public DefaultValue visitDefaultValue(MySqlParser.DefaultValueContext ctx) {
    if (ctx == null) {
      return null;
    }

    DefaultValue.Type type = null;
    if (ctx.NULL_LITERAL() != null) {
      type = DefaultValue.Type.NULL_LITERAL;
    } else if (ctx.constant() != null) {
      type = DefaultValue.Type.CONSTANT;
    } else if (ctx.currentTimestamp() != null) {
      type = DefaultValue.Type.CURRENTTIMESTAMP;
    } else {
      throw ParserError.make(ctx);
    }
    RelationalUnaryOperatorEnum unaryOperator = this.visitUnaryOperator(ctx.unaryOperator());
    Constant constant = this.visitConstant(ctx.constant());
    List<CurrentTimestamp> currentTimestamps = Lists.newArrayList();
    for (MySqlParser.CurrentTimestampContext currentTimestampCtx : ctx.currentTimestamp()) {
      currentTimestamps.add(this.visitCurrentTimestamp(currentTimestampCtx));
    }

    return RelationalAlgebraExpressionFactory.makeDefaultValue(type, unaryOperator, constant,
      currentTimestamps);
  }

  @Override
  public CurrentTimestamp visitCurrentTimestamp(MySqlParser.CurrentTimestampContext ctx) {
    if (ctx == null) {
      return null;
    }

    CurrentTimestamp.Type type =
        CurrentTimestamp.Type.valueOf(ctx.getChild(0).getText().toUpperCase());
    DecimalLiteral decimalLiteral = this.visitDecimalLiteral(ctx.decimalLiteral());

    return RelationalAlgebraExpressionFactory.makeCurrentTimestamp(type, decimalLiteral);
  }

  @Override
  public ExpressionOrDefault visitExpressionOrDefault(MySqlParser.ExpressionOrDefaultContext ctx) {
    if (ctx == null) {
      return null;
    }

    Expression expression = this.visitExpression(ctx.expression());
    Boolean isDefault = null;
    if (ctx.DEFAULT() != null) {
      isDefault = Boolean.TRUE;
    }
    return RelationalAlgebraExpressionFactory.makeExpressionOrDefault(expression, isDefault);
  }

  @Override
  public IfExists visitIfExists(MySqlParser.IfExistsContext ctx) {
    if (ctx == null) {
      return null;
    }
    return RelationalAlgebraExpressionFactory.makeIfExists();
  }

  @Override
  public IfNotExists visitIfNotExists(MySqlParser.IfNotExistsContext ctx) {
    if (ctx == null) {
      return null;
    }

    return RelationalAlgebraExpressionFactory.makeIfNotExists();
  }

  public FunctionCall visitFunctionCall(MySqlParser.FunctionCallContext ctx) {
    if (ctx == null) {
      return null;
    }

    if (ctx instanceof MySqlParser.SpecificFunctionCallContext) {
      return this.visitSpecificFunctionCall((MySqlParser.SpecificFunctionCallContext) ctx);
    } else if (ctx instanceof MySqlParser.AggregateFunctionCallContext) {
      return this.visitAggregateFunctionCall((MySqlParser.AggregateFunctionCallContext) ctx);
    } else if (ctx instanceof MySqlParser.ScalarFunctionCallContext) {
      return this.visitScalarFunctionCall((MySqlParser.ScalarFunctionCallContext) ctx);
    } else if (ctx instanceof MySqlParser.UdfFunctionCallContext) {
      return this.visitUdfFunctionCall((MySqlParser.UdfFunctionCallContext) ctx);
    } else if (ctx instanceof MySqlParser.PasswordFunctionCallContext) {
      return this.visitPasswordFunctionCall((MySqlParser.PasswordFunctionCallContext) ctx);
    } else {
      throw ParserError.make(ctx);
    }
  }

  @Override
  public SpecificFunction visitSpecificFunctionCall(MySqlParser.SpecificFunctionCallContext ctx) {
    if (ctx == null) {
      return null;
    }
    return this.visitSpecificFunction(ctx.specificFunction());
  }

  @Override
  public AggregateWindowedFunction
      visitAggregateFunctionCall(MySqlParser.AggregateFunctionCallContext ctx) {
    if (ctx == null) {
      return null;
    }
    return this.visitAggregateWindowedFunction(ctx.aggregateWindowedFunction());
  }

  @Override
  public ScalarFunctionCall visitScalarFunctionCall(MySqlParser.ScalarFunctionCallContext ctx) {
    if (ctx == null) {
      return null;
    }

    Functions.ScalarFunctionNameEnum scalarFunctionName =
        this.visitScalarFunctionName(ctx.scalarFunctionName());
    FunctionArgs functionArgs = this.visitFunctionArgs(ctx.functionArgs());

    return RelationalAlgebraExpressionFactory.makeScalarFunctionCall(scalarFunctionName,
      functionArgs);
  }

  @Override
  public UdfFunctionCall visitUdfFunctionCall(MySqlParser.UdfFunctionCallContext ctx) {
    if (ctx == null) {
      return null;
    }

    FullId fullId = this.visitFullId(ctx.fullId());
    FunctionArgs functionArgs = this.visitFunctionArgs(ctx.functionArgs());

    return RelationalAlgebraExpressionFactory.makeUdfFunctionCall(fullId, functionArgs);
  }

  @Override
  public PasswordFunctionClause
      visitPasswordFunctionCall(MySqlParser.PasswordFunctionCallContext ctx) {
    if (ctx == null) {
      return null;
    }
    return this.visitPasswordFunctionClause(ctx.passwordFunctionClause());
  }

  public SpecificFunction visitSpecificFunction(MySqlParser.SpecificFunctionContext ctx) {
    if (ctx == null) {
      return null;
    }
    throw ParserError.make(ctx);
  }

  @Override
  public SimpleFunctionCall visitSimpleFunctionCall(MySqlParser.SimpleFunctionCallContext ctx) {
    if (ctx == null) {
      return null;
    }
    SimpleFunctionCall.Type type = SimpleFunctionCall.Type.valueOf(ctx.getText().toUpperCase());
    return RelationalAlgebraExpressionFactory.makeSimpleFunctionCall(type);
  }

  @Override
  public DataTypeFunctionCall
      visitDataTypeFunctionCall(MySqlParser.DataTypeFunctionCallContext ctx) {
    if (ctx == null) {
      return null;
    }

    DataTypeFunctionCall.Type type = null;
    Expression expression = null;
    CharsetName charsetName = null;
    ConvertedDataType convertedDataType = null;
    if (ctx.CONVERT() != null) {
      if (ctx.convertedDataType() != null) {
        type = DataTypeFunctionCall.Type.CONVERT_DATATYPE;
        expression = this.visitExpression(ctx.expression());
        convertedDataType = this.visitConvertedDataType(ctx.convertedDataType());
      }
      if (ctx.charsetName() != null) {
        type = DataTypeFunctionCall.Type.CONVERT_CHARSET;
        expression = this.visitExpression(ctx.expression());
        charsetName = this.visitCharsetName(ctx.charsetName());
      }
    }
    if (ctx.CAST() != null) {
      type = DataTypeFunctionCall.Type.CAST;
      expression = this.visitExpression(ctx.expression());
      convertedDataType = this.visitConvertedDataType(ctx.convertedDataType());
    }
    switch (type) {
    case CONVERT_DATATYPE:
      return RelationalAlgebraExpressionFactory.makeDataTypeFunctionCall(type, expression,
        convertedDataType);
    case CONVERT_CHARSET:
      return RelationalAlgebraExpressionFactory.makeDataTypeFunctionCall(type, expression,
        charsetName);
    case CAST:
      return RelationalAlgebraExpressionFactory.makeDataTypeFunctionCall(type, expression,
        convertedDataType);
    default:
      throw ParserError.make(ctx);
    }

  }

  @Override
  public ValuesFunctionCall visitValuesFunctionCall(MySqlParser.ValuesFunctionCallContext ctx) {
    if (ctx == null) {
      return null;
    }

    FullColumnName fullColumnName = this.visitFullColumnName(ctx.fullColumnName());
    return RelationalAlgebraExpressionFactory.makeValuesFunctionCall(fullColumnName);
  }

  @Override
  public CaseFunctionCall visitCaseFunctionCall(MySqlParser.CaseFunctionCallContext ctx) {
    if (ctx == null) {
      return null;
    }

    Expression expression = this.visitExpression(ctx.expression());
    List<MySqlParser.CaseFuncAlternativeContext> caseFuncAlternativeCtxs =
        ctx.caseFuncAlternative();
    List<CaseFuncAlternative> caseFuncAlternatives = Lists.newArrayList();
    for (MySqlParser.CaseFuncAlternativeContext caseFuncAlternativeCtx : caseFuncAlternativeCtxs) {
      caseFuncAlternatives.add(this.visitCaseFuncAlternative(caseFuncAlternativeCtx));
    }
    FunctionArg functionArg = this.visitFunctionArg(ctx.functionArg());

    return RelationalAlgebraExpressionFactory.makeCaseFunctionCall(expression, caseFuncAlternatives,
      functionArg);
  }

  @Override
  public CharFunctionCall visitCharFunctionCall(MySqlParser.CharFunctionCallContext ctx) {
    if (ctx == null) {
      return null;
    }

    FunctionArgs functionArgs = this.visitFunctionArgs(ctx.functionArgs());
    CharsetName charsetName = this.visitCharsetName(ctx.charsetName());
    return RelationalAlgebraExpressionFactory.makeCharFunctionCall(functionArgs, charsetName);
  }

  @Override
  public PositionFunctionCall
      visitPositionFunctionCall(MySqlParser.PositionFunctionCallContext ctx) {
    if (ctx == null) {
      return null;
    }

    StringLiteral positionString = this.visitStringLiteral(ctx.positionString);
    Expression positionExpression = this.visitExpression(ctx.positionExpression);
    StringLiteral inString = this.visitStringLiteral(ctx.inString);
    Expression inExpression = this.visitExpression(ctx.inExpression);

    return RelationalAlgebraExpressionFactory.makePositionFunctionCall(positionString,
      positionExpression, inString, inExpression);
  }

  @Override
  public SubstrFunctionCall visitSubstrFunctionCall(MySqlParser.SubstrFunctionCallContext ctx) {
    if (ctx == null) {
      return null;
    }

    StringLiteral sourceString = this.visitStringLiteral(ctx.sourceString);
    Expression sourceExpression = this.visitExpression(ctx.sourceExpression);
    DecimalLiteral fromDecimal = this.visitDecimalLiteral(ctx.fromDecimal);
    Expression fromExpression = this.visitExpression(ctx.fromExpression);
    DecimalLiteral forDecimal = this.visitDecimalLiteral(ctx.forDecimal);
    Expression forExpression = this.visitExpression(ctx.forExpression);

    return RelationalAlgebraExpressionFactory.makeSubstrFunctionCall(sourceString, sourceExpression,
      fromDecimal, fromExpression, forDecimal, forExpression);
  }

  @Override
  public TrimFunctionCall visitTrimFunctionCall(MySqlParser.TrimFunctionCallContext ctx) {
    if (ctx == null) {
      return null;
    }

    TrimFunctionCall.PositioinFormType type = null;
    if (ctx.positioinForm != null) {
      type = TrimFunctionCall.PositioinFormType.valueOf(ctx.positioinForm.getText().toUpperCase());
    }
    StringLiteral sourceString = this.visitStringLiteral(ctx.sourceString);
    Expression sourceExpression = this.visitExpression(ctx.sourceExpression);
    StringLiteral fromString = this.visitStringLiteral(ctx.fromString);
    Expression fromExpression = this.visitExpression(ctx.fromExpression);

    return RelationalAlgebraExpressionFactory.makeTrimFunctionCall(type, sourceString,
      sourceExpression, fromString, fromExpression);
  }

  @Override
  public WeightFunctionCall visitWeightFunctionCall(MySqlParser.WeightFunctionCallContext ctx) {
    if (ctx == null) {
      return null;
    }

    StringLiteral stringLiteral = this.visitStringLiteral(ctx.stringLiteral());
    Expression expression = this.visitExpression(ctx.expression());
    WeightFunctionCall.StringFormatType type =
        WeightFunctionCall.StringFormatType.valueOf(ctx.stringFormat.getText().toUpperCase());
    DecimalLiteral decimalLiteral = this.visitDecimalLiteral(ctx.decimalLiteral());
    LevelsInWeightString levelsInWeightString =
        this.visitLevelsInWeightString(ctx.levelsInWeightString());
    return RelationalAlgebraExpressionFactory.makeWeightFunctionCall(stringLiteral, expression,
      type, decimalLiteral, levelsInWeightString);
  }

  @Override
  public ExtractFunctionCall visitExtractFunctionCall(MySqlParser.ExtractFunctionCallContext ctx) {
    if (ctx == null) {
      return null;
    }
    IntervalType intervalType = this.visitIntervalType(ctx.intervalType());
    StringLiteral sourceString = this.visitStringLiteral(ctx.sourceString);
    Expression sourceExpression = this.visitExpression(ctx.sourceExpression);

    return RelationalAlgebraExpressionFactory.makeExtractFunctionCall(intervalType, sourceString,
      sourceExpression);
  }

  @Override
  public GetFormatFunctionCall
      visitGetFormatFunctionCall(MySqlParser.GetFormatFunctionCallContext ctx) {
    if (ctx == null) {
      return null;
    }

    GetFormatFunctionCall.DatetimeFormatType type = GetFormatFunctionCall.DatetimeFormatType
        .valueOf(ctx.datetimeFormat.getText().toUpperCase());
    StringLiteral stringLiteral = this.visitStringLiteral(ctx.stringLiteral());
    return RelationalAlgebraExpressionFactory.makeGetFormatFunctionCall(type, stringLiteral);
  }

  @Override
  public CaseFuncAlternative visitCaseFuncAlternative(MySqlParser.CaseFuncAlternativeContext ctx) {
    if (ctx == null) {
      return null;
    }

    FunctionArg condition = this.visitFunctionArg(ctx.condition);
    FunctionArg consequent = this.visitFunctionArg(ctx.consequent);
    return RelationalAlgebraExpressionFactory.makeCaseFuncAlternative(condition, consequent);
  }

  public LevelsInWeightString
      visitLevelsInWeightString(MySqlParser.LevelsInWeightStringContext ctx) {
    if (ctx == null) {
      return null;
    }

    if (ctx instanceof MySqlParser.LevelWeightListContext) {
      return this.visitLevelWeightList((MySqlParser.LevelWeightListContext) ctx);
    } else if (ctx instanceof MySqlParser.LevelWeightRangeContext) {
      return this.visitLevelWeightRange((MySqlParser.LevelWeightRangeContext) ctx);
    } else {
      throw ParserError.make(ctx);
    }
  }

  @Override
  public LevelWeightList visitLevelWeightList(MySqlParser.LevelWeightListContext ctx) {
    if (ctx == null) {
      return null;
    }

    List<LevelInWeightListElement> levelInWeightListElements = Lists.newArrayList();
    for (MySqlParser.LevelInWeightListElementContext levelInWeightListElementCtx : ctx
        .levelInWeightListElement()) {
      levelInWeightListElements
          .add(this.visitLevelInWeightListElement(levelInWeightListElementCtx));
    }

    return RelationalAlgebraExpressionFactory.makeLevelWeightList(levelInWeightListElements);
  }

  @Override
  public LevelWeightRange visitLevelWeightRange(MySqlParser.LevelWeightRangeContext ctx) {
    if (ctx == null) {
      return null;
    }
    DecimalLiteral firstLevel = this.visitDecimalLiteral(ctx.firstLevel);
    DecimalLiteral lastLevel = this.visitDecimalLiteral(ctx.lastLevel);
    return RelationalAlgebraExpressionFactory.makeLevelWeightRange(firstLevel, lastLevel);
  }

  @Override
  public LevelInWeightListElement
      visitLevelInWeightListElement(MySqlParser.LevelInWeightListElementContext ctx) {
    if (ctx == null) {
      return null;
    }

    DecimalLiteral decimalLiteral = this.visitDecimalLiteral(ctx.decimalLiteral());
    LevelInWeightListElement.OrderType orderType = null;
    if (ctx.orderType != null) {
      orderType = LevelInWeightListElement.OrderType.valueOf(ctx.orderType.getText().toUpperCase());
    }

    return RelationalAlgebraExpressionFactory.makeLevelInWeightListElement(decimalLiteral,
      orderType);
  }

  @Override
  public AggregateWindowedFunction
      visitAggregateWindowedFunction(MySqlParser.AggregateWindowedFunctionContext ctx) {
    if (ctx == null) {
      return null;
    }

    AggregateWindowedFunction.Type type =
        AggregateWindowedFunction.Type.valueOf(ctx.getChild(0).getText().toUpperCase());
    AggregateWindowedFunction.AggregatorEnum aggregator = null;
    if (ctx.aggregator != null) {
      aggregator =
          AggregateWindowedFunction.AggregatorEnum.valueOf(ctx.aggregator.getText().toUpperCase());
    }
    FunctionArg functionArg = this.visitFunctionArg(ctx.functionArg());
    FunctionArgs functionArgs = this.visitFunctionArgs(ctx.functionArgs());
    List<OrderByExpression> orderByExpressions = Lists.newArrayList();
    for (MySqlParser.OrderByExpressionContext orderByExpressionCtx : ctx.orderByExpression()) {
      orderByExpressions.add(this.visitOrderByExpression(orderByExpressionCtx));
    }
    String separator = null;
    if (ctx.separator != null) {
      separator = ctx.separator.getText();
    }

    return RelationalAlgebraExpressionFactory.makeAggregateWindowedFunction(type, aggregator,
      functionArg, functionArgs, orderByExpressions, separator);
  }

  @Override
  public Functions.ScalarFunctionNameEnum
      visitScalarFunctionName(MySqlParser.ScalarFunctionNameContext ctx) {
    if (ctx == null) {
      return null;
    }
    return Functions.ScalarFunctionNameEnum.valueOf(ctx.getText().toUpperCase());
  }

  @Override
  public PasswordFunctionClause
      visitPasswordFunctionClause(MySqlParser.PasswordFunctionClauseContext ctx) {
    if (ctx == null) {
      return null;
    }

    PasswordFunctionClause.Type functionName =
        PasswordFunctionClause.Type.valueOf(ctx.functionName.getText().toUpperCase());
    FunctionArg functionArg = this.visitFunctionArg(ctx.functionArg());

    return RelationalAlgebraExpressionFactory.makePasswordFunctionClause(functionName, functionArg);
  }

  @Override
  public FunctionArgs visitFunctionArgs(MySqlParser.FunctionArgsContext ctx) {
    if (ctx == null) {
      return null;
    }

    List<FunctionArg> functionArgs = Lists.newArrayList();
    for (ParseTree child : ctx.children) {
      if (child instanceof MySqlParser.ConstantContext) {
        functionArgs.add(RelationalAlgebraExpressionFactory.makeFunctionArg(
          FunctionArg.Type.CONSTANT, this.visitConstant((MySqlParser.ConstantContext) child)));
      } else if (child instanceof MySqlParser.FullColumnNameContext) {
        functionArgs.add(
          RelationalAlgebraExpressionFactory.makeFunctionArg(FunctionArg.Type.FULL_COLUMN_NAME,
            this.visitFullColumnName((MySqlParser.FullColumnNameContext) child)));
      } else if (child instanceof MySqlParser.FunctionCallContext) {
        functionArgs
            .add(RelationalAlgebraExpressionFactory.makeFunctionArg(FunctionArg.Type.FUNCTION_CALL,
              this.visitFunctionCall((MySqlParser.FunctionCallContext) child)));
      } else if (child instanceof MySqlParser.ExpressionContext) {
        functionArgs
            .add(RelationalAlgebraExpressionFactory.makeFunctionArg(FunctionArg.Type.EXPRESSION,
              this.visitExpression((MySqlParser.ExpressionContext) child)));
      } else {
        // ignore
      }
    }

    return RelationalAlgebraExpressionFactory.makeFunctionArgs(functionArgs);
  }

  @Override
  public FunctionArg visitFunctionArg(MySqlParser.FunctionArgContext ctx) {
    if (ctx == null) {
      return null;
    }
    FunctionArg.Type type = null;
    Object value = null;
    if (ctx.constant() != null) {
      type = FunctionArg.Type.CONSTANT;
      value = this.visitConstant(ctx.constant());
    } else if (ctx.fullColumnName() != null) {
      type = FunctionArg.Type.FULL_COLUMN_NAME;
      value = this.visitFullColumnName(ctx.fullColumnName());
    } else if (ctx.functionCall() != null) {
      type = FunctionArg.Type.FUNCTION_CALL;
      value = this.visitFunctionCall(ctx.functionCall());
    } else if (ctx.expression() != null) {
      type = FunctionArg.Type.EXPRESSION;
      value = this.visitExpression(ctx.expression());
    }

    return RelationalAlgebraExpressionFactory.makeFunctionArg(type, value);
  }

  public Expression visitExpression(MySqlParser.ExpressionContext ctx) {
    if (ctx == null) {
      return null;
    }

    if (ctx instanceof MySqlParser.NotExpressionContext) {
      return this.visitNotExpression((MySqlParser.NotExpressionContext) ctx);
    } else if (ctx instanceof MySqlParser.LogicalExpressionContext) {
      return this.visitLogicalExpression((MySqlParser.LogicalExpressionContext) ctx);
    } else if (ctx instanceof MySqlParser.IsExpressionContext) {
      return this.visitIsExpression((MySqlParser.IsExpressionContext) ctx);
    } else if (ctx instanceof MySqlParser.PredicateExpressionContext) {
      return this.visitPredicateExpression((MySqlParser.PredicateExpressionContext) ctx);
    } else {
      throw ParserError.make(ctx);
    }
  }

  @Override
  public NotExpression visitNotExpression(MySqlParser.NotExpressionContext ctx) {
    if (ctx == null) {
      return null;
    }

    Expression expression = this.visitExpression(ctx.expression());
    return RelationalAlgebraExpressionFactory.makeNotExpression(expression);
  }

  @Override
  public LogicalExpression visitLogicalExpression(MySqlParser.LogicalExpressionContext ctx) {
    if (ctx == null) {
      return null;
    }
    Expression first = this.visitExpression(ctx.expression(0));
    RelationalLogicalOperatorEnum operator = this.visitLogicalOperator(ctx.logicalOperator());
    Expression second = this.visitExpression(ctx.expression(1));

    return RelationalAlgebraExpressionFactory.makeLogicalExpression(first, operator, second);
  }

  @Override
  public IsExpression visitIsExpression(MySqlParser.IsExpressionContext ctx) {
    if (ctx == null) {
      return null;
    }

    PredicateExpression predicate = this.visitPredicate(ctx.predicate());
    Boolean not = null;
    if (ctx.NOT() != null) {
      not = Boolean.TRUE;
    }
    IsExpression.TestValue testValue =
        IsExpression.TestValue.valueOf(ctx.testValue.getText().toUpperCase());

    return RelationalAlgebraExpressionFactory.makeIsExpression(predicate, not, testValue);
  }

  @Override
  public PredicateExpression visitPredicateExpression(MySqlParser.PredicateExpressionContext ctx) {
    if (ctx == null) {
      return null;
    }

    return this.visitPredicate(ctx.predicate());
  }

  @Override
  public SoundsLikePredicate visitSoundsLikePredicate(MySqlParser.SoundsLikePredicateContext ctx) {
    if (ctx == null) {
      return null;
    }

    PredicateExpression first = this.visitPredicate(ctx.predicate(0));
    PredicateExpression second = this.visitPredicate(ctx.predicate(1));

    return RelationalAlgebraExpressionFactory.makeSoundsLikePredicate(first, second);
  }

  @Override
  public ExpressionAtomPredicate
      visitExpressionAtomPredicate(MySqlParser.ExpressionAtomPredicateContext ctx) {
    if (ctx == null) {
      return null;
    }

    String localId = null;
    if (ctx.LOCAL_ID() != null) {
      localId = ctx.LOCAL_ID().getText();
    }
    ExpressionAtom expressionAtom = this.visitExpressionAtom(ctx.expressionAtom());

    return RelationalAlgebraExpressionFactory.makeExpressionAtomPredicate(localId, expressionAtom);
  }

  public ExpressionAtom visitExpressionAtom(MySqlParser.ExpressionAtomContext ctx) {
    if (ctx == null) {
      return null;
    }

    if (ctx instanceof MySqlParser.ConstantExpressionAtomContext) {
      return this.visitConstantExpressionAtom((MySqlParser.ConstantExpressionAtomContext) ctx);
    } else if (ctx instanceof MySqlParser.FullColumnNameExpressionAtomContext) {
      return this.visitFullColumnNameExpressionAtom(//
        (MySqlParser.FullColumnNameExpressionAtomContext) ctx);
    } else if (ctx instanceof MySqlParser.FunctionCallExpressionAtomContext) {
      return this.visitFunctionCallExpressionAtom(//
        (MySqlParser.FunctionCallExpressionAtomContext) ctx);
    } else if (ctx instanceof MySqlParser.CollateExpressionAtomContext) {
      return this.visitCollateExpressionAtom((MySqlParser.CollateExpressionAtomContext) ctx);
    } else if (ctx instanceof MySqlParser.MysqlVariableExpressionAtomContext) {
      return this.visitMysqlVariableExpressionAtom(//
        (MySqlParser.MysqlVariableExpressionAtomContext) ctx);
    } else if (ctx instanceof MySqlParser.UnaryExpressionAtomContext) {
      return this.visitUnaryExpressionAtom((MySqlParser.UnaryExpressionAtomContext) ctx);
    } else if (ctx instanceof MySqlParser.BinaryExpressionAtomContext) {
      return this.visitBinaryExpressionAtom((MySqlParser.BinaryExpressionAtomContext) ctx);
    } else if (ctx instanceof MySqlParser.NestedExpressionAtomContext) {
      return this.visitNestedExpressionAtom((MySqlParser.NestedExpressionAtomContext) ctx);
    } else if (ctx instanceof MySqlParser.NestedRowExpressionAtomContext) {
      return this.visitNestedRowExpressionAtom((MySqlParser.NestedRowExpressionAtomContext) ctx);
    } else if (ctx instanceof MySqlParser.ExistsExpessionAtomContext) {
      return this.visitExistsExpessionAtom((MySqlParser.ExistsExpessionAtomContext) ctx);
    } else if (ctx instanceof MySqlParser.SubqueryExpessionAtomContext) {
      return this.visitSubqueryExpessionAtom((MySqlParser.SubqueryExpessionAtomContext) ctx);
    } else if (ctx instanceof MySqlParser.IntervalExpressionAtomContext) {
      return this.visitIntervalExpressionAtom((MySqlParser.IntervalExpressionAtomContext) ctx);
    } else if (ctx instanceof MySqlParser.BitExpressionAtomContext) {
      return this.visitBitExpressionAtom((MySqlParser.BitExpressionAtomContext) ctx);
    } else if (ctx instanceof MySqlParser.MathExpressionAtomContext) {
      return this.visitMathExpressionAtom((MySqlParser.MathExpressionAtomContext) ctx);
    } else {
      throw ParserError.make(ctx);
    }
  }

  public PredicateExpression visitPredicate(MySqlParser.PredicateContext ctx) {
    if (ctx == null) {
      return null;
    }

    if (ctx instanceof MySqlParser.InPredicateContext) {
      return this.visitInPredicate((MySqlParser.InPredicateContext) ctx);
    } else if (ctx instanceof MySqlParser.IsNullPredicateContext) {
      return this.visitIsNullPredicate((MySqlParser.IsNullPredicateContext) ctx);
    } else if (ctx instanceof MySqlParser.BinaryComparasionPredicateContext) {
      return this.visitBinaryComparasionPredicate(//
        (MySqlParser.BinaryComparasionPredicateContext) ctx);
    } else if (ctx instanceof MySqlParser.SubqueryComparasionPredicateContext) {
      return this.visitSubqueryComparasionPredicate(//
        (MySqlParser.SubqueryComparasionPredicateContext) ctx);
    } else if (ctx instanceof MySqlParser.BetweenPredicateContext) {
      return this.visitBetweenPredicate((MySqlParser.BetweenPredicateContext) ctx);
    } else if (ctx instanceof MySqlParser.SoundsLikePredicateContext) {
      return this.visitSoundsLikePredicate((MySqlParser.SoundsLikePredicateContext) ctx);
    } else if (ctx instanceof MySqlParser.LikePredicateContext) {
      return this.visitLikePredicate((MySqlParser.LikePredicateContext) ctx);
    } else if (ctx instanceof MySqlParser.RegexpPredicateContext) {
      return this.visitRegexpPredicate((MySqlParser.RegexpPredicateContext) ctx);
    } else if (ctx instanceof MySqlParser.ExpressionAtomPredicateContext) {
      return this.visitExpressionAtomPredicate((MySqlParser.ExpressionAtomPredicateContext) ctx);
    } else {
      throw ParserError.make(ctx);
    }
  }

  @Override
  public InPredicate visitInPredicate(MySqlParser.InPredicateContext ctx) {
    if (ctx == null) {
      return null;
    }
    PredicateExpression predicate = this.visitPredicate(ctx.predicate());
    Boolean not = null;
    if (ctx.NOT() != null) {
      not = Boolean.TRUE;
    }
    SelectStatement selectStatement = this.visitSelectStatement(ctx.selectStatement());
    Expressions expressions = this.visitExpressions(ctx.expressions());

    return RelationalAlgebraExpressionFactory.makeInPredicate(predicate, not, selectStatement,
      expressions);
  }

  @Override
  public SubqueryComparasionPredicate
      visitSubqueryComparasionPredicate(MySqlParser.SubqueryComparasionPredicateContext ctx) {
    if (ctx == null) {
      return null;
    }

    PredicateExpression predicate = this.visitPredicate(ctx.predicate());
    RelationalComparisonOperatorEnum comparisonOperator =
        this.visitComparisonOperator(ctx.comparisonOperator());
    SubqueryComparasionPredicate.QuantifierEnum quantifier =
        SubqueryComparasionPredicate.QuantifierEnum.valueOf(ctx.quantifier.getText().toUpperCase());
    SelectStatement selectStatement = this.visitSelectStatement(ctx.selectStatement());

    return RelationalAlgebraExpressionFactory.makeSubqueryComparasionPredicate(predicate,
      comparisonOperator, quantifier, selectStatement);
  }

  @Override
  public BetweenPredicate visitBetweenPredicate(MySqlParser.BetweenPredicateContext ctx) {
    if (ctx == null) {
      return null;
    }
    PredicateExpression first = this.visitPredicate(ctx.predicate(0));
    Boolean not = null;
    if (ctx.NOT() != null) {
      not = Boolean.TRUE;
    }
    PredicateExpression second = this.visitPredicate(ctx.predicate(1));
    PredicateExpression third = this.visitPredicate(ctx.predicate(2));

    return RelationalAlgebraExpressionFactory.makeBetweenPredicate(first, not, second, third);
  }

  @Override
  public BinaryComparasionPredicate
      visitBinaryComparasionPredicate(MySqlParser.BinaryComparasionPredicateContext ctx) {
    if (ctx == null) {
      return null;
    }

    PredicateExpression left = this.visitPredicate(ctx.left);
    MySqlParser.ComparisonOperatorContext comparisonOperatorCtx = ctx.comparisonOperator();
    RelationalComparisonOperatorEnum comparisonOperator =
        this.visitComparisonOperator(comparisonOperatorCtx);
    PredicateExpression right = this.visitPredicate(ctx.right);

    return RelationalAlgebraExpressionFactory.makeBinaryComparasionPredicate(left,
      comparisonOperator, right);
  }

  @Override
  public IsNullPredicate visitIsNullPredicate(MySqlParser.IsNullPredicateContext ctx) {
    if (ctx == null) {
      return null;
    }

    PredicateExpression predicate = this.visitPredicate(ctx.predicate());
    NullNotnull nullNotnull = this.visitNullNotnull(ctx.nullNotnull());

    return RelationalAlgebraExpressionFactory.makeIsNullPredicate(predicate, nullNotnull);
  }

  @Override
  public LikePredicate visitLikePredicate(MySqlParser.LikePredicateContext ctx) {
    if (ctx == null) {
      return null;
    }

    PredicateExpression first = null;
    Boolean not = null;
    PredicateExpression second = null;
    String stringLiteral = null;

    List<MySqlParser.PredicateContext> predicateCtxs = ctx.predicate();
    List<PredicateExpression> predicates = Lists.newArrayList();
    if (CollectionUtils.isNotEmpty(predicateCtxs)) {
      for (MySqlParser.PredicateContext predicateCtx : predicateCtxs) {
        predicates.add(this.visitPredicate(predicateCtx));
      }

      if (predicates.size() >= 1) {
        first = predicates.get(0);
      }
      if (predicates.size() >= 2) {
        second = predicates.get(1);
      }
    }

    if (ctx.NOT() != null) {
      not = Boolean.TRUE;
    }
    TerminalNode STRING_LITERAL = ctx.STRING_LITERAL();
    if (STRING_LITERAL != null) {
      stringLiteral = STRING_LITERAL.getText();
    }

    return RelationalAlgebraExpressionFactory.makeLikePredicate(first, not, second, stringLiteral);
  }

  @Override
  public RegexpPredicate visitRegexpPredicate(MySqlParser.RegexpPredicateContext ctx) {
    if (ctx == null) {
      return null;
    }

    PredicateExpression first = this.visitPredicate(ctx.predicate(0));
    Boolean not = null;
    if (ctx.NOT() != null) {
      not = Boolean.TRUE;
    }
    RegexpPredicate.RegexType regex =
        RegexpPredicate.RegexType.valueOf(ctx.regex.getText().toUpperCase());
    PredicateExpression second = this.visitPredicate(ctx.predicate(1));

    return RelationalAlgebraExpressionFactory.makeRegexpPredicate(first, not, regex, second);
  }

  @Override
  public UnaryExpressionAtom visitUnaryExpressionAtom(MySqlParser.UnaryExpressionAtomContext ctx) {
    if (ctx == null) {
      return null;
    }

    RelationalUnaryOperatorEnum unaryOperator = this.visitUnaryOperator(ctx.unaryOperator());
    ExpressionAtom expressionAtom = this.visitExpressionAtom(ctx.expressionAtom());
    return RelationalAlgebraExpressionFactory.makeUnaryExpressionAtom(unaryOperator,
      expressionAtom);
  }

  @Override
  public Collate visitCollateExpressionAtom(MySqlParser.CollateExpressionAtomContext ctx) {
    if (ctx == null) {
      return null;
    }

    ExpressionAtom expressionAtom = this.visitExpressionAtom(ctx.expressionAtom());
    CollationName collationName = this.visitCollationName(ctx.collationName());
    return RelationalAlgebraExpressionFactory.makeCollate(expressionAtom, collationName);
  }

  @Override
  public SubqueryExpessionAtom
      visitSubqueryExpessionAtom(MySqlParser.SubqueryExpessionAtomContext ctx) {
    if (ctx == null) {
      return null;
    }

    SelectStatement selectStatement = this.visitSelectStatement(ctx.selectStatement());
    return RelationalAlgebraExpressionFactory.makeSubqueryExpessionAtom(selectStatement);
  }

  @Override
  public MysqlVariable
      visitMysqlVariableExpressionAtom(MySqlParser.MysqlVariableExpressionAtomContext ctx) {
    if (ctx == null) {
      return null;
    }

    if (ctx.getChild(0) instanceof MySqlParser.MysqlVariableContext) {
      return this.visitMysqlVariable((MySqlParser.MysqlVariableContext) ctx.getChild(0));
    } else {
      throw ParserError.make(ctx);
    }
  }

  @Override
  public NestedExpressionAtom
      visitNestedExpressionAtom(MySqlParser.NestedExpressionAtomContext ctx) {
    if (ctx == null) {
      return null;
    }

    List<Expression> expressions = Lists.newArrayList();
    for (MySqlParser.ExpressionContext expressionCtx : ctx.expression()) {
      expressions.add(this.visitExpression(expressionCtx));
    }
    return RelationalAlgebraExpressionFactory.makeNestedExpressionAtom(expressions);
  }

  @Override
  public NestedRowExpressionAtom
      visitNestedRowExpressionAtom(MySqlParser.NestedRowExpressionAtomContext ctx) {
    if (ctx == null) {
      return null;
    }

    List<Expression> expressions = Lists.newArrayList();
    for (MySqlParser.ExpressionContext expressionCtx : ctx.expression()) {
      expressions.add(this.visitExpression(expressionCtx));
    }
    return RelationalAlgebraExpressionFactory.makeNestedRowExpressionAtom(expressions);
  }

  @Override
  public MathExpressionAtom visitMathExpressionAtom(MySqlParser.MathExpressionAtomContext ctx) {
    if (ctx == null) {
      return null;
    }

    ExpressionAtom left = this.visitExpressionAtom(ctx.left);
    RelationalMathOperatorEnum mathOperator = this.visitMathOperator(ctx.mathOperator());
    ExpressionAtom right = this.visitExpressionAtom(ctx.right);
    return RelationalAlgebraExpressionFactory.makeMathExpressionAtom(left, mathOperator, right);
  }

  @Override
  public IntervalExpressionAtom
      visitIntervalExpressionAtom(MySqlParser.IntervalExpressionAtomContext ctx) {
    if (ctx == null) {
      return null;
    }

    Expression expression = this.visitExpression(ctx.expression());
    IntervalType intervalType = this.visitIntervalType(ctx.intervalType());
    return RelationalAlgebraExpressionFactory.makeIntervalExpressionAtom(expression, intervalType);
  }

  @Override
  public ExistsExpessionAtom visitExistsExpessionAtom(MySqlParser.ExistsExpessionAtomContext ctx) {
    if (ctx == null) {
      return null;
    }

    SelectStatement selectStatement = this.visitSelectStatement(ctx.selectStatement());
    return RelationalAlgebraExpressionFactory.makeExistsExpessionAtom(selectStatement);
  }

  @Override
  public Constant visitConstantExpressionAtom(MySqlParser.ConstantExpressionAtomContext ctx) {
    if (ctx == null) {
      return null;
    }

    return this.visitConstant(ctx.constant());
  }

  @Override
  public FunctionCall
      visitFunctionCallExpressionAtom(MySqlParser.FunctionCallExpressionAtomContext ctx) {
    if (ctx == null) {
      return null;
    }

    return this.visitFunctionCall(ctx.functionCall());
  }

  @Override
  public BinaryExpressionAtom
      visitBinaryExpressionAtom(MySqlParser.BinaryExpressionAtomContext ctx) {
    if (ctx == null) {
      return null;
    }

    ExpressionAtom expressionAtom = this.visitExpressionAtom(ctx.expressionAtom());
    return RelationalAlgebraExpressionFactory.makeBinaryExpressionAtom(expressionAtom);
  }

  @Override
  public FullColumnName
      visitFullColumnNameExpressionAtom(MySqlParser.FullColumnNameExpressionAtomContext ctx) {
    if (ctx == null) {
      return null;
    }

    return this.visitFullColumnName(ctx.fullColumnName());
  }

  @Override
  public BitExpressionAtom visitBitExpressionAtom(MySqlParser.BitExpressionAtomContext ctx) {
    if (ctx == null) {
      return null;
    }

    ExpressionAtom left = this.visitExpressionAtom(ctx.left);
    RelationalBitOperatorEnum bitOperator = this.visitBitOperator(ctx.bitOperator());
    ExpressionAtom right = this.visitExpressionAtom(ctx.right);
    return RelationalAlgebraExpressionFactory.makeBitExpressionAtom(left, bitOperator, right);
  }

  @Override
  public RelationalUnaryOperatorEnum visitUnaryOperator(MySqlParser.UnaryOperatorContext ctx) {
    if (ctx == null) {
      return null;
    }

    return RelationalUnaryOperatorEnum.of(ctx.getText().toUpperCase());
  }

  @Override
  public RelationalComparisonOperatorEnum
      visitComparisonOperator(MySqlParser.ComparisonOperatorContext ctx) {
    if (ctx == null) {
      return null;
    }

    return RelationalComparisonOperatorEnum.of(ctx.getText().toUpperCase());
  }

  @Override
  public RelationalLogicalOperatorEnum
      visitLogicalOperator(MySqlParser.LogicalOperatorContext ctx) {
    if (ctx == null) {
      return null;
    }

    return RelationalLogicalOperatorEnum.of(ctx.getText().toUpperCase());
  }

  @Override
  public RelationalBitOperatorEnum visitBitOperator(MySqlParser.BitOperatorContext ctx) {
    if (ctx == null) {
      return null;
    }

    return RelationalBitOperatorEnum.of(ctx.getText().toUpperCase());
  }

  @Override
  public RelationalMathOperatorEnum visitMathOperator(MySqlParser.MathOperatorContext ctx) {
    if (ctx == null) {
      return null;
    }

    return RelationalMathOperatorEnum.of(ctx.getText().toUpperCase());
  }

  @Override
  public SimpleIdSets.CharsetNameBaseEnum
      visitCharsetNameBase(MySqlParser.CharsetNameBaseContext ctx) {
    if (ctx == null) {
      return null;
    }

    return SimpleIdSets.CharsetNameBaseEnum.valueOf(ctx.getText().toUpperCase());
  }

  @Override
  public SimpleIdSets.TransactionLevelBaseEnum
      visitTransactionLevelBase(MySqlParser.TransactionLevelBaseContext ctx) {
    if (ctx == null) {
      return null;
    }

    return SimpleIdSets.TransactionLevelBaseEnum.valueOf(ctx.getText().toUpperCase());
  }

  @Override
  public SimpleIdSets.PrivilegesBaseEnum
      visitPrivilegesBase(MySqlParser.PrivilegesBaseContext ctx) {
    if (ctx == null) {
      return null;
    }

    return SimpleIdSets.PrivilegesBaseEnum.valueOf(ctx.getText().toUpperCase());
  }

  @Override
  public SimpleIdSets.IntervalTypeBaseEnum
      visitIntervalTypeBase(MySqlParser.IntervalTypeBaseContext ctx) {
    if (ctx == null) {
      return null;
    }

    return SimpleIdSets.IntervalTypeBaseEnum.valueOf(ctx.getText().toUpperCase());
  }

  @Override
  public SimpleIdSets.DataTypeBaseEnum visitDataTypeBase(MySqlParser.DataTypeBaseContext ctx) {
    if (ctx == null) {
      return null;
    }

    return SimpleIdSets.DataTypeBaseEnum.valueOf(ctx.getText().toUpperCase());
  }

  @Override
  public SimpleIdSets.KeywordsCanBeIdEnum
      visitKeywordsCanBeId(MySqlParser.KeywordsCanBeIdContext ctx) {
    if (ctx == null) {
      return null;
    }

    return SimpleIdSets.KeywordsCanBeIdEnum.valueOf(ctx.getText().toUpperCase());
  }

  @Override
  public SimpleIdSets.FunctionNameBaseEnum
      visitFunctionNameBase(MySqlParser.FunctionNameBaseContext ctx) {
    if (ctx == null) {
      return null;
    }

    return SimpleIdSets.FunctionNameBaseEnum.valueOf(ctx.getText().toUpperCase());
  }
}
