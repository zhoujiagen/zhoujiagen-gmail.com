grammar Expr;

expr:
    expr op=('*'|'/') expr                      #MulDiv
    |   expr op=('+'|'-') expr                  #AddSub
    |   VAR                                     #Var
    |   sign=('+'|'-')? DECIMAL_LITERAL         #DecimalLiteral
    |   sign=('+'|'-')? REAL_LITERAL            #RealLiteral
    |   '(' expr ')'                            #Parens
    ;


VAR: CHARACTER+;
DECIMAL_LITERAL: DEC_DIGIT+;
REAL_LITERAL: (DEC_DIGIT+)? '.' DEC_DIGIT+;


NEWLINE : [\r\n]+ -> skip;
WS  :   [ \t]+ -> skip ;

fragment DEC_DIGIT:                  [0-9];
fragment CHARACTER:                  [a-zA-Z];