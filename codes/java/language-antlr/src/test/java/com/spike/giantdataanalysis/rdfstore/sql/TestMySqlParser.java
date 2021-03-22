package com.spike.giantdataanalysis.rdfstore.sql;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import org.antlr.v4.runtime.ANTLRErrorStrategy;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.DefaultErrorStrategy;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.tree.ParseTree;

import com.google.common.collect.Maps;
import com.spike.giantdataanalysis.rdfstore.sql.MySqlParser.RootContext;
import com.spike.giantdataanalysis.rdfstore.sql.MySqlParser.SqlStatementsContext;

/**
 * <p>
 * grammar: https://github.com/antlr/grammars-v4/tree/master/mysql
 * <p>
 * 注意文法中SQL关键字是大写的, 使用{@link CaseChangingCharStream}预处理.
 */
public class TestMySqlParser {

  static final Map<Path, String> errorMsgCollector = Maps.newHashMap();

  public static ANTLRErrorStrategy errorStrategy = new DefaultErrorStrategy() {
  };

  public static class MyErrorListener extends BaseErrorListener {
    private Path path;

    public MyErrorListener(Path path) {
      this.path = path;
    }

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line,
        int charPositionInLine, String msg, RecognitionException e) {
      errorMsgCollector.put(path, msg);
    }
  }

  public static void main(String[] args) throws IOException {

    Path path = Paths.get(TestConstants.all_path);
    CharStream rawCS = CharStreams.fromPath(path);
    CaseChangingCharStream cs = new CaseChangingCharStream(rawCS, true);
    MySqlLexer lexer = new MySqlLexer(cs);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    MySqlParser parser = new MySqlParser(tokens);

    parser.setErrorHandler(errorStrategy);
    parser.addErrorListener(new MyErrorListener(path));

    // ParseTree tree = parser.root();
    // System.out.println(tree.toStringTree(parser));
    RootContext rootContext = parser.root();
    SqlStatementsContext stmts = rootContext.sqlStatements();
    for (ParseTree stmt : stmts.children) {
      System.out.println(stmt.toStringTree(parser));
    }
  }
}
