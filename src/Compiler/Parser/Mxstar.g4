grammar Mxstar;

@header {
package Compiler.Parser;
}

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
    : type variable(',' variable)* ';'
    ;

variable
    : Identifier ('=' expression)?
    ;

type
    : type '[' ']' # arrayType
    | nonArrayType # narrayType
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
    : type variable
    ;

constructorDef
    : Identifier '(' ')' block
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
              step = expression? ')' statement                          # forStmt
    | Return expression? ';'                                            # retStmt
    | Break ';'                                                         # breakStmt
    | Continue ';'                                                      # continueStmt
    | expression ';'                                                    # exprStmt
    | ';'                                                               # emptyStmt
    ;

expression
    // Basement
    : constant                                                       # constExpr
    | This                                                           # thisExpr
    | Identifier                                                     # idExpr
    | '(' expression ')'                                             # subExpr
    | <assoc = right> 'new' creator                                  # newExpr
    // Second Priority Level
    | expression op = ('++' | '--')                                  # suffixExpr
    | expression '.' Identifier                                      # memberExpr
    | expression '[' expression ']'                                  # subscriptExpr
    | expression '(' expressionList? ')'                             # funcCallExpr
    // Third Priority Level
    | <assoc = right> op = ('~' | '!' | '++' | '--') expression      # prefixExpr
    | <assoc = right> op = ('+' | '-')               expression      # prefixExpr
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
    | <assoc = right> lhs = expression op='=' rhs = expression       # binaryOpExpr
    ;

expressionList
    : expression (',' expression)*
    ;

constant
    : ConstInteger      # ConstInteger
    | ConstString       # ConstString
    | ConstBool         # ConstBool
    | ConstNull         # ConstNull
    ;

creator
    : (Void | nonArrayType) ('[' expression ']')+ ('[' ']')+ ('[' expression ']')+  # errorCreator
    | (Void | nonArrayType) ('[' expression ']')+ ('[' ']')*                        # arrayCreator
    | (Void | nonArrayType) '(' ')'                                                 # classCreator
    | (Void | nonArrayType)                                                         # naryCreator
    ;

// ------------------------------------------ Parser above, Lexer below

// -------------- keywords
Int         : 'int';
Bool        : 'bool';
String      : 'string';
Void        : 'void';
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
fragment Null   : 'null';
fragment True   : 'true';
fragment False  : 'false';

// -------------- const

ConstInteger
    : [1-9][0-9]*
    | '0'
    ;

ConstString
    : '"' ('\\"' | '\\n' | '\\\\' | .)*? '"'
    ;

ConstBool
    : True | False
    ;

ConstNull
    : Null
    ;

// -------------- other
Identifier
    : [A-Za-z] [_A-Za-z0-9]*
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
