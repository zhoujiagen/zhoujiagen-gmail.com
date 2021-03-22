package com.spike.giantdataanalysis.rdfstore.sql;

import org.antlr.v4.runtime.ParserRuleContext;

public class ParserError extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public static ParserError make(String message) {
    return new ParserError(message);
  }

  public static ParserError make(ParserRuleContext ctx) {
    return new ParserError(
        ctx.getClass().getName() + ": \n" + ctx.getText() + "\n" + ctx.toStringTree());
  }

  ParserError() {
    super();
  }

  ParserError(String message) {
    super(message);
  }

  ParserError(String message, Throwable cause) {
    super(message, cause);
  }

  ParserError(Throwable cause) {
    super(cause);
  }

  protected ParserError(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

}