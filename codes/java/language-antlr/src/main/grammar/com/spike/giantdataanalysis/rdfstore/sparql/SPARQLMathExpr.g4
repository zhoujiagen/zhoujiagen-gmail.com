grammar SPARQLMathExpr;

//---------------------------------------------------------------------------
// counterexample: ?p*(1-?discount)
//---------------------------------------------------------------------------

//@header {
//package com.spike.giantdataanalysis.rdfstore.sparql.mathexpr;
//}

//[71] 参数列表
gArgList: NIL | '(' K_DISTINCT? gExpression ( ',' gExpression )* ')';

//[108] 变量: $var, ?var
gVar: VAR1 | VAR2;

//[110] 表达式
gExpression: gConditionalOrExpression;
//[111] 或条件表达式
gConditionalOrExpression: gConditionalAndExpression ( K_OR gConditionalAndExpression )*;
//[112] 与条件表达式
gConditionalAndExpression: gValueLogical ( K_AND gValueLogical )*;
//[113] 值逻辑
gValueLogical: gRelationalExpression;
//[114] 关系表达式
gRelationalExpression: gNumericExpression ( gRelationalExpression2 )?;
gRelationalExpression2: K_EQ gNumericExpression
                        | K_NEQ gNumericExpression
                        | K_LT gNumericExpression
                        | K_GT gNumericExpression
                        | K_LTE gNumericExpression
                        | K_GTE gNumericExpression;
//[115] 数值表达式
gNumericExpression: gAdditiveExpression;
//[116] 可加数值表达式
//AdditiveExpression	  ::=	MultiplicativeExpression ( '+' MultiplicativeExpression | '-' MultiplicativeExpression | ( NumericLiteralPositive | NumericLiteralNegative ) (( '*' UnaryExpression ) | ( '/' UnaryExpression ))* )*
gAdditiveExpression: gMultiplicativeExpression ( ('+' gMultiplicativeExpression) | ('-' gMultiplicativeExpression) | (gNumericLiteral (('*' | '/') gUnaryExpression)*) )*;
//[117] 可乘数值表达式
//MultiplicativeExpression	  ::=  	UnaryExpression ( '*' UnaryExpression | '/' UnaryExpression )*
gMultiplicativeExpression: gUnaryExpression (('*' | '/') gUnaryExpression)*;
//[118] 一元表达式
gUnaryExpression: '!' gPrimaryExpression
                  |	'+' gPrimaryExpression
                  |	'-' gPrimaryExpression
                  |	gPrimaryExpression;
//[119] 原始表达式
gPrimaryExpression: gBrackettedExpression
                    | gNumericLiteral
                    | gBooleanLiteral
                    | gRDFLiteral
                    | gBuiltInCall
                    | giriOrFunction
                    | gVar;
//[130] 数值字面量
gNumericLiteral: gNumericLiteralUnsigned | gNumericLiteralPositive | gNumericLiteralNegative;
//[131] 无符号数值字面量
gNumericLiteralUnsigned: INTEGER | DECIMAL | DOUBLE;
//[132] 正数值字面量
gNumericLiteralPositive: INTEGER_POSITIVE |	DECIMAL_POSITIVE |	DOUBLE_POSITIVE;
//[133] 负数值字面量
gNumericLiteralNegative: INTEGER_NEGATIVE |	DECIMAL_NEGATIVE |	DOUBLE_NEGATIVE;
//[134] 布尔字面量
gBooleanLiteral: K_true |	K_false;

//[120] 带括号的表达式
gBrackettedExpression: '(' gExpression ')';
//[121] 内建调用
gBuiltInCall: K_STR '(' gExpression ')';

//[128]
giriOrFunction: giri gArgList?;
//[129] RDF字面量
gRDFLiteral: gString ( LANGTAG | ( '^^' giri ) )?;


//[135] 字符串: 可能包含空格
gString: STRING_LITERAL1 | STRING_LITERAL2 | STRING_LITERAL_LONG1 | STRING_LITERAL_LONG2;
//[136] IRI: IRI引用, 带前缀的名字(例foaf:knows)
giri: IRIREF |	gPrefixedName;
//[137] 带前缀的名字
gPrefixedName: PNAME_LN | PNAME_NS;



fragment A : [aA]; // match either an 'a' or 'A'
fragment B : [bB];
fragment C : [cC];
fragment D : [dD];
fragment E : [eE];
fragment F : [fF];
fragment G : [gG];
fragment H : [hH];
fragment I : [iI];
fragment J : [jJ];
fragment K : [kK];
fragment L : [lL];
fragment M : [mM];
fragment N : [nN];
fragment O : [oO];
fragment P : [pP];
fragment Q : [qQ];
fragment R : [rR];
fragment S : [sS];
fragment T : [tT];
fragment U : [uU];
fragment V : [vV];
fragment W : [wW];
fragment X : [xX];
fragment Y : [yY];
fragment Z : [zZ];

K_OR: '||';
K_AND: '&&';
K_EQ: '=';
K_NEQ: '!=';
K_LT: '<';
K_GT: '>';
K_LTE: '<=';
K_GTE: '>=';

K_ABS: A B S  ;
K_ADD: A D D  ;
K_ALL: A L L  ;
K_AS: A S  ;
K_ASC: A S C  ;
K_ASK: A S K  ;
K_AVG: A V G  ;
K_BASE: B A S E  ;
K_BIND: B I N D  ;
K_BNODE: B N O D E  ;
K_BOUND: B O U N D  ;
K_BY: B Y  ;
K_CEIL: C E I L  ;
K_CLEAR: C L E A R  ;
K_COALESCE: C O A L E S C E  ;
K_CONCAT: C O N C A T  ;
K_CONSTRUCT: C O N S T R U C T  ;
K_CONTAINS: C O N T A I N S  ;
K_COPY: C O P Y  ;
K_COUNT: C O U N T  ;
K_CREATE: C R E A T E  ;
K_DATATYPE: D A T A T Y P E  ;
K_DAY: D A Y  ;
K_DEFAULT: D E F A U L T  ;
K_DELETE: D E L E T E  ;
K_DESC: D E S C  ;
K_DESCRIBE: D E S C R I B E  ;
K_DISTINCT: D I S T I N C T  ;
K_DROP: D R O P  ;
K_ENCODE_FOR_URI: E N C O D E '_' F O R '_' U R I  ;
K_EXISTS: E X I S T S  ;
K_FILTER: F I L T E R  ;
K_FLOOR: F L O O R  ;
K_FROM: F R O M  ;
K_GRAPH: G R A P H  ;
K_GROUP: G R O U P  ;
K_GROUP_CONCAT: G R O U P '_' C O N C A T  ;
K_HAVING: H A V I N G  ;
K_HOURS: H O U R S  ;
K_IF: I F  ;
K_IN: I N  ;
K_INSERT: I N S E R T  ;
K_INTO: I N T O  ;
K_IRI: I R I  ;
K_LANG: L A N G  ;
K_LANGMATCHES: L A N G M A T C H E S  ;
K_LCASE: L C A S E  ;
K_LIMIT: L I M I T  ;
K_LOAD: L O A D  ;
K_MAX: M A X  ;
K_MD5: M D '5'  ;
K_MIN: M I N  ;
K_MINUS: M I N U S  ;
K_MINUTES: M I N U T E S  ;
K_MONTH: M O N T H  ;
K_MOVE: M O V E  ;
K_NAMED: N A M E D  ;
K_NOT: N O T  ;
K_NOW: N O W  ;
K_OFFSET: O F F S E T  ;
K_OPTIONAL: O P T I O N A L  ;
K_ORDER: O R D E R  ;
K_PREFIX: P R E F I X  ;
K_RAND: R A N D  ;
K_REDUCED: R E D U C E D  ;
K_REGEX: R E G E X  ;
K_REPLACE: R E P L A C E  ;
K_ROUND: R O U N D  ;
K_SAMPLE: S A M P L E  ;
K_SECONDS: S E C O N D S  ;
K_SELECT: S E L E C T  ;
K_SEPARATOR: S E P A R A T O R  ;
K_SERVICE: S E R V I C E  ;
K_SHA1: S H A '1'  ;
K_SHA256: S H A '256'  ;
K_SHA384: S H A '384'  ;
K_SHA512: S H A '512'  ;
K_SILENT: S I L E N T  ;
K_STR: S T R  ;
K_STRAFTER: S T R A F T E R  ;
K_STRBEFORE: S T R B E F O R E  ;
K_STRDT: S T R D T  ;
K_STRENDS: S T R E N D S  ;
K_STRLANG: S T R L A N G  ;
K_STRLEN: S T R L E N  ;
K_STRSTARTS: S T R S T A R T S  ;
K_STRUUID: S T R U U I D  ;
K_SUBSTR: S U B S T R  ;
K_SUM: S U M  ;
K_TIMEZONE: T I M E Z O N E  ;
K_TO: T O  ;
K_TZ: T Z  ;
K_UCASE: U C A S E  ;
K_UNDEF: U N D E F  ;
K_UNION: U N I O N  ;
K_URI: U R I  ;
K_USING: U S I N G  ;
K_UUID: U U I D  ;
K_VALUES: V A L U E S  ;
K_WHERE: W H E R E  ;
K_WITH: W I T H  ;
K_YEAR: Y E A R  ;
K_A: A  ;
K_false: F A L S E  ;
K_isBLANK: I S B L A N K  ;
K_isIRI: I S I R I  ;
K_isLITERAL: I S L I T E R A L  ;
K_isNUMERIC: I S N U M E R I C  ;
K_isURI: I S U R I  ;
K_sameTerm: S A M E T E R M  ;
K_true: T R U E  ;
K_DATA: D A T A;


//[139]
//IRIREF::=  	'<' ([^<>"{}|^`\]-[#x00-#x20])* '>'
//IRIREF: '<' (~([[<>"{}|`\\\]] | '^') |'-'| [0-9a-zA-Z#/:] | '.')* '>';
IRIREF: '<' (~([[<>"{}|`\\\]] | '^' |'-') | [0-9a-zA-Z#/:] | '.')* '>';
//[140]
PNAME_NS: PN_PREFIX? ':';
//[141]
PNAME_LN: PNAME_NS PN_LOCAL;
//[142]
BLANK_NODE_LABEL: '_:' ( PN_CHARS_U | [0-9] ) ((PN_CHARS|'.')* PN_CHARS)?;
//[143]
VAR1: '?' VARNAME;
//[144]
VAR2: '$' VARNAME;
//[145]
LANGTAG: '@' [a-zA-Z]+ ('-' [a-zA-Z0-9]+)*;
//[146]
INTEGER: [0-9]+;
//[147]
DECIMAL: [0-9]* '.' [0-9]+;
//[148]
DOUBLE: [0-9]+ '.' [0-9]* EXPONENT | '.' ([0-9])+ EXPONENT | ([0-9])+ EXPONENT;
//[149]
INTEGER_POSITIVE: '+' INTEGER;
//[150]
DECIMAL_POSITIVE: '+' DECIMAL;
//[151]
DOUBLE_POSITIVE: '+' DOUBLE;
//[152]
INTEGER_NEGATIVE: '-' INTEGER;
//[153]
DECIMAL_NEGATIVE: '-' DECIMAL;
//[154]
DOUBLE_NEGATIVE: '-' DOUBLE;
//[155]
EXPONENT: [eE] [+-]? [0-9]+;
//[156]
//STRING_LITERAL1::=  	"'" ( ([^#x27#x5C#xA#xD]) | ECHAR )* "'"
STRING_LITERAL1: '\'' ( [a-zA-Z_^\\. ] | ECHAR )* '\'';
//[157]
//	STRING_LITERAL2::=  	'"' ( ([^#x22#x5C#xA#xD]) | ECHAR )* '"'
STRING_LITERAL2: '"' ( [a-zA-Z_^\\. ] | ECHAR )* '"';
//[158]
//STRING_LITERAL_LONG1::=  	"'''" ( ( "'" | "''" )? ( [^'\] | ECHAR ) )* "'''"
STRING_LITERAL_LONG1: '\'\'\'' ( ( '\'' | '\'\'' )? ( [^'\\] | ECHAR ) )* '\'\'\'';
//[159]
STRING_LITERAL_LONG2: '"""' ( ( '"' | '""' )? ( [^"\\] | ECHAR ) )* '"""';
//[160] escape character
//ECHAR::=  	'\' [tbnrf\"']
ECHAR: '\\' [tbnrf\\"'];
//[161]
NIL: '(' WS* ')';
//[162]
//WS::=  	#x20 | #x9 | #xD | #xA
WS: [ \r\t\n]+ -> skip;
NON_SKIP_WS: [ \r\t\n]+;
//[163]
ANON: '[' WS* ']';
//[164]
//PN_CHARS_BASE::=  	[A-Z] | [a-z] | [#x00C0-#x00D6] | [#x00D8-#x00F6] | [#x00F8-#x02FF] | [#x0370-#x037D] | [#x037F-#x1FFF] | [#x200C-#x200D] | [#x2070-#x218F] | [#x2C00-#x2FEF] | [#x3001-#xD7FF] | [#xF900-#xFDCF] | [#xFDF0-#xFFFD] | [#x10000-#xEFFFF]
PN_CHARS_BASE: [A-Z] | [a-z];
//[165]
PN_CHARS_U: PN_CHARS_BASE | '_';
//[166]
//VARNAME::=  	( PN_CHARS_U | [0-9] ) ( PN_CHARS_U | [0-9] | #x00B7 | [#x0300-#x036F] | [#x203F-#x2040] )*
VARNAME: ( PN_CHARS_U | [0-9] ) ( PN_CHARS_U | [0-9] )*;
//[167]
//PN_CHARS::=  	PN_CHARS_U | '-' | [0-9] | #x00B7 | [#x0300-#x036F] | [#x203F-#x2040]
PN_CHARS: PN_CHARS_U | '-' | [0-9]; /////////////////////////////////
//[168]
PN_PREFIX: PN_CHARS_BASE ((PN_CHARS|'.')* PN_CHARS)?;
//[169]
PN_LOCAL: (PN_CHARS_U | ':' | [0-9] | PLX ) ((PN_CHARS | '.' | ':' | PLX)* (PN_CHARS | ':' | PLX) )?;
//[170]
PLX: PERCENT | PN_LOCAL_ESC;
//[171]
PERCENT: '%' HEX HEX;
//[172]
HEX: [0-9] | [A-F] | [a-f];
//[173]
PN_LOCAL_ESC: '\\' ( '_' | '~' | '.' | '-' | '!' | '$' | '&' | '\'' | '(' | ')' | '*' | '+' | ',' | ';' | '=' | '/' | '?' | '#' | '@' | '%' );
