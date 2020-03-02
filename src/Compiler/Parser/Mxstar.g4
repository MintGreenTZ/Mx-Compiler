grammar Mxstar;

program
    : decl*
    ;

decl
    : (classDecl | variableDecl | functionDecl)
    ;

classDecl
    : Class Identifier '{' (variableDecl | functionDecl | constructorDef)* '}' ';'
    ;

variableDecl
    : type Identifier (',' Identifier)* ';'
    ;

type
    : nonArrayType ('[' ']')*
    ;

nonArrayType
    : Int
    | Bool
    | String
    | Identifier
    ;

functionDecl
    : (type | Void) Identifier '(' parameterList? ')' block
    ;

parameterList
    : parameter (',' parameter)*
    ;

parameter
    : type Identifier
    ;

constructorDef
    : '(' ')' Identifier block
    ;

block
    : '{' statement* '}'
    ;

// -------------- statement & expression

statement
    : block                                                             # blockStmt
    | variableDecl                                                      # variableDeclStmt
    | If '(' expression ')' statement (Else statement)?                 # ifStmt
    | While '(' expression ')' statement                                # whileStmt
    | For '(' init = expression? ';' cond = expression? ';'
              step = expression ')' statement                           # forStmt
    | Return expression ';'                                             # retStmt
    | Break ';'                                                         # breakStmt
    | Continue ';'                                                      # continueStmt
    | expression ';'                                                    # exprStmt
    | ';'                                                               # emptyStmt
    ;

expression
    // Basement
    : constant                                                       # constExpr
    | This                                                           # thisExpr
    | Identifier                                                     # idExpt
    | '(' expression ')'                                             # subExpr
    // Second Priority Level
    | expression op = ('++' | '--')                                  # suffixExpr
    | expression '.' Identifier                                      # memberExpr
    | expression '[' expression ']'                                  # subsriptExpr
    | expression '(' expressionList? ')'                             # funcCallExpr
    // Third Priority Level
    | <assoc = right> op = ('~' | '!' | '++' | '--') expression      # prefixExpr
    | <assoc = right> op = ('+' | '-')               expression      # prefixExpr
    | 'new' creator                                                  # newExpr
    // Binary Operator
    | lhs = expression op=('*' | '/' | '%') rhs = expression         # binaryOpExpr
    | lhs = expression op=('+' | '-') rhs = expression               # binaryOpExpr
    | lhs = expression op=('<<' | '>>') rhs = expression             # binaryOpExpr
    | lhs = expression op=('<' | '>' | '>=' | '<=') rhs = expression # binaryOpExpr
    | lhs = expression op=('==' | '!=' ) rhs = expression            # binaryOpExpr
    | lhs = expression op='&' rhs = expression                       # binaryOpExpr
    | lhs = expression op='^' rhs = expression                       # binaryOpExpr
    | lhs = expression op='|' rhs = expression                       # binaryOpExpr
    | lhs = expression op='&&' rhs = expression                      # binaryOpExpr
    | lhs = expression op='||' rhs = expression                      # binaryOpExpr
    | <assoc = right> lhs = expression op='=' rhs = expression       # assignOpExpr
    ;

expressionList
    : expression (',' expression)*
    ;

constant
    : ConstInteger
    | ConstString
    | ConstBool
    | ConstNull
    ;

creator
    : (Void | nonArrayType)                                     # naryCreator
    | (Void | nonArrayType) ('[' expression ']')+ ('[' ']') *   # arrayCreator
    | (Void | nonArrayType) '(' ')'                             # classCreator
    ;

// ------------------------------------------ Parser above, Lexer below

// -------------- keywords
Int         : 'int';
Bool        : 'bool';
String      : 'string';
Null        : 'null';
Void        : 'void';
True        : 'true';
False       : 'false';
If          : 'if';
Else        : 'else';
For         : 'for';
While       : 'while';
Break       : 'break';
Continue    : 'continue';
Return      : 'return';
New         : 'new';
Class       : 'class';
This        : 'this';

// -------------- const

ConstInteger
    : [1-9][0-9]*
    | '0'
    ;

ConstString
    : '"' (. | '\\"' | '\\n' | '\\\\')*? '"'
    ;

ConstBool
    : True | False
    ;

ConstNull
    : Null
    ;

// -------------- other
Identifier
    : [_A-Za-z] [_A-Za-z0-9]*
    ;

WhiteSpace
    : [ \r\t\n]+ -> skip
    ;

LineComment
    : '//' ~[\r\n]* -> skip
    ;

BlockComment // extra
    : '/*' .*? '*/' -> skip
    ;
