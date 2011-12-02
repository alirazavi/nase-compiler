%output=NaseParser.cs
%namespace Nase.GeneratedParser
%parsertype NaseParser
%partial
%YYLTYPE FilePosition
%using Nase.Files;
%using Nase.Syntax;

%union { public Symbol symbol;
         public SyntaxTreeNode node;
         public List<Symbol> symbolList; }

%tokentype Symbol
%token NULL_SYMBOL
%token BEGIN_SYMBOL END_SYMBOL
%token DELIMITER_SYMBOL
%token COMMA_SYMBOL
%token INT_TYPE_SYMBOL
%token BOOL_TYPE_SYMBOL
%token READ_SYMBOL WRITE_SYMBOL
%token IF_SYMBOL THEN_SYMBOL ELSE_SYMBOL
%token WHILE_SYMBOL DO_SYMBOL
%token ASSIGN_SYMBOL
%token OPEN_PARENTHESIS_SYMBOL CLOSE_PARENTHESIS_SYMBOL
%token INLINE_IF_SYMBOL INLINE_THEN_SYMBOL INLINE_ELSE_SYMBOL INLINE_FI_SYMBOL
%token PLUS_SYMBOL MINUS_SYMBOL
%token TIMES_SYMBOL DIVIDE_SYMBOL MODULO_SYMBOL
%token LT_SYMBOL LE_SYMBOL EQ_SYMBOL GE_SYMBOL GT_SYMBOL NE_SYMBOL
%token AND_SYMBOL OR_SYMBOL NOT_SYMBOL
%token TRUE_SYMBOL FALSE_SYMBOL

%token <symbol> IDENTIFIER_SYMBOL
%token <symbol> INTEGER_LITERAL_SYMBOL

%type <node> program
%type <node> declarationSequence
%type <node> blockSequence
%type <node> block
%type <node> statementSequence
%type <node> statement
%type <node> declaration
%type <symbolList> declarationRec
%type <node> assignment
%type <node> read
%type <node> write
%type <node> typeName
%type <node> intExpr
%type <node> intTerm
%type <node> intTermSecondary
%type <node> intFactor
%type <node> boolExpr
%type <node> boolTerm
%type <node> boolTermSecondary
%type <node> boolFactor
%type <node> comparisonExpr
%type <node> inlineIfStatement
%type <node> ifStatement
%type <node> whileStatement

%type <node> identifier
%type <node> integer

%start program

%%

program :
        declarationSequence blockSequence EOF
            {
                $$ = new SyntaxTreeProgramNode(@$, new SyntaxTreeSequenceNode(@1, $1, new SyntaxTreeSequenceNode(@2, $2, null)));
                SyntaxTree = new SyntaxTree($$);
            }
    |    blockSequence EOF
            {
                $$ = new SyntaxTreeProgramNode(@$, $1);
                SyntaxTree = new SyntaxTree($$);
            }
    ;

declarationSequence :
        declaration DELIMITER_SYMBOL declarationSequence
            {
                $$ = new SyntaxTreeSequenceNode(@$, $1, $3);
            }
    |    declaration DELIMITER_SYMBOL
            {
                $$ = new SyntaxTreeSequenceNode(@$, $1, null);
            }
    ;

blockSequence :
        block blockSequence
            {
                $$ = new SyntaxTreeSequenceNode(@$, $1, $2);
            }
    |    block
            {
                $$ = new SyntaxTreeSequenceNode(@$, $1, null);
            }
    ;

block :
        BEGIN_SYMBOL statementSequence END_SYMBOL DELIMITER_SYMBOL
            {
                $$ = $2;
            }
    ;

statementSequence :
        statement statementSequence
            {
                $$ = new SyntaxTreeSequenceNode(@$, $1, $2);
            }
    |    statement
            {
                $$ = new SyntaxTreeSequenceNode(@$, $1, null);
            }
    ;

statement :
        declaration DELIMITER_SYMBOL
            {
                $$ = $1;
            }
    |    block
            {
                $$ = $1;
            }
    |    assignment DELIMITER_SYMBOL
            {
                $$ = $1;
            }
    |    read DELIMITER_SYMBOL
            {
                $$ = $1;
            }
    |    write DELIMITER_SYMBOL
            {
                $$ = $1;
            }
    |    ifStatement
            {
                $$ = $1;
            }
    |    whileStatement
            {
                $$ = $1;
            }
    ;

declaration :
        typeName IDENTIFIER_SYMBOL declarationRec
            {
                SyntaxTreeDeclarationNode declNode = null;
                SyntaxTreeSequenceNode seqNode = null;

                var firstDeclNode = new SyntaxTreeDeclarationNode(@$, $1, $2);
                this._symbolTable.SetDeclarationNodeLinkToSymbol($2, firstDeclNode);

                foreach(Symbol s in $3)
                {
                    declNode = new SyntaxTreeDeclarationNode(@$, $1, s);
                    this._symbolTable.SetDeclarationNodeLinkToSymbol(s, declNode);
                    seqNode = new SyntaxTreeSequenceNode(@$, declNode, seqNode);
                }

                $$ = new SyntaxTreeSequenceNode(@$, firstDeclNode, seqNode);
            }
    |    typeName IDENTIFIER_SYMBOL
            {
                $$ = new SyntaxTreeDeclarationNode(@$, $1, $2);
                this._symbolTable.SetDeclarationNodeLinkToSymbol($2, $$);
            }
    ;

declarationRec :
        COMMA_SYMBOL IDENTIFIER_SYMBOL declarationRec
            {
                $$ = $3;
                $$.Add($2);
            }
    |    COMMA_SYMBOL IDENTIFIER_SYMBOL
            {
                $$ = new List<Symbol>();
                $$.Add($2);
            }
    ;

typeName :
        INT_TYPE_SYMBOL
            {
                $$ = new SyntaxTreeTypeNode(@$, Symbol.INT_TYPE_SYMBOL);
            }
    |    BOOL_TYPE_SYMBOL
            {
                $$ = new SyntaxTreeTypeNode(@$, Symbol.BOOL_TYPE_SYMBOL);
            }
    ;

assignment :
        identifier ASSIGN_SYMBOL intExpr
            {
                $$ = new SyntaxTreeAssignmentNode(@$, $1, $3);
            }
    |    identifier ASSIGN_SYMBOL boolExpr
            {
                $$ = new SyntaxTreeAssignmentNode(@$, $1, $3);
            }
    ;

intExpr :
        intTerm PLUS_SYMBOL intExpr
            {
                $$ = new SyntaxTreeDyadicOpNode(@$, Symbol.PLUS_SYMBOL, $1, $3);
            }
    |    intTerm MINUS_SYMBOL intExpr
            {
                $$ = new SyntaxTreeDyadicOpNode(@$, Symbol.MINUS_SYMBOL, $1, $3);
            }
    |    intTerm
            {
                $$ = $1;
            }
    ;

intTerm :
        MINUS_SYMBOL intTermSecondary
            {
                $$ = new SyntaxTreeMonadicOpNode(@$, Symbol.MINUS_SYMBOL, $2);
            }
    |    intTermSecondary
            {
                $$ = $1;
            }
    ;

intTermSecondary :
        intFactor TIMES_SYMBOL intTerm
            {
                $$ = new SyntaxTreeDyadicOpNode(@$, Symbol.TIMES_SYMBOL, $1, $3);
            }
    |    intFactor DIVIDE_SYMBOL intTerm
            {
                $$ = new SyntaxTreeDyadicOpNode(@$, Symbol.DIVIDE_SYMBOL, $1, $3);
            }
    |    intFactor MODULO_SYMBOL intTerm
            {
                $$ = new SyntaxTreeDyadicOpNode(@$, Symbol.MODULO_SYMBOL, $1, $3);
            }
    |    intFactor
            {
                $$ = $1;
            }
    ;

intFactor :
        integer
            {
                $$ = $1;
            }
    |    identifier
            {
                $$ = $1;
            }
    |    OPEN_PARENTHESIS_SYMBOL intExpr CLOSE_PARENTHESIS_SYMBOL
            {
                $$ = $2;
            }
    |    inlineIfStatement
            {
                $$ = $1;
            }
    ;

inlineIfStatement :
        INLINE_IF_SYMBOL boolExpr INLINE_THEN_SYMBOL intExpr INLINE_ELSE_SYMBOL intExpr INLINE_FI_SYMBOL
            {
                $$ = new SyntaxTreeInlineIfNode(@$, $2, $4, $6);
            }
    ;

boolExpr :
        boolTerm OR_SYMBOL boolExpr
            {
                $$ = new SyntaxTreeDyadicOpNode(@$, Symbol.OR_SYMBOL, $1, $3);
            }
    |    boolTerm
            {
                $$ = $1;
            }
    ;

boolTerm :
        NOT_SYMBOL boolTermSecondary
            {
                $$ = new SyntaxTreeMonadicOpNode(@$, Symbol.NOT_SYMBOL, $2);
            }
    |    boolTermSecondary
            {
                $$ = $1;
            }
    ;

boolTermSecondary :
        boolFactor AND_SYMBOL boolTermSecondary
            {
                $$ = new SyntaxTreeDyadicOpNode(@$, Symbol.AND_SYMBOL, $1, $3);
            }
    |    boolFactor
            {
                $$ = $1;
            }
    ;

boolFactor :
        TRUE_SYMBOL
            {
                $$ = new SyntaxTreeConstNode(@$, Symbol.TRUE_SYMBOL);
            }
    |    FALSE_SYMBOL
            {
                $$ = new SyntaxTreeConstNode(@$, Symbol.FALSE_SYMBOL);
            }
    |    comparisonExpr
            {
                $$ = $1;
            }
    |    identifier
            {
                $$ = $1;
            }
    |    OPEN_PARENTHESIS_SYMBOL boolExpr CLOSE_PARENTHESIS_SYMBOL
            {
                $$ = $2;
            }
    ;

comparisonExpr :
        intExpr LT_SYMBOL intExpr
            {
                $$ = new SyntaxTreeDyadicOpNode(@$, Symbol.LT_SYMBOL, $1, $3);
            }
    |    intExpr LE_SYMBOL intExpr
            {
                $$ = new SyntaxTreeDyadicOpNode(@$, Symbol.LE_SYMBOL, $1, $3);
            }
    |    intExpr EQ_SYMBOL intExpr
            {
                $$ = new SyntaxTreeDyadicOpNode(@$, Symbol.EQ_SYMBOL, $1, $3);
            }
    |    intExpr GE_SYMBOL intExpr
            {
                $$ = new SyntaxTreeDyadicOpNode(@$, Symbol.GE_SYMBOL, $1, $3);
            }
    |    intExpr GT_SYMBOL intExpr
            {
                $$ = new SyntaxTreeDyadicOpNode(@$, Symbol.GT_SYMBOL, $1, $3);
            }
    |    intExpr NE_SYMBOL intExpr
            {
                $$ = new SyntaxTreeDyadicOpNode(@$, Symbol.NE_SYMBOL, $1, $3);
            }
    ;

identifier :
        IDENTIFIER_SYMBOL
            {
                $$ = new SyntaxTreeIdentNode(@$, $1);
            }
    ;

integer :
        INTEGER_LITERAL_SYMBOL
            {
                $$ = new SyntaxTreeConstNode(@$, $1);
                this._symbolTable.SetDeclarationNodeLinkToSymbol($1, $$);
            }
    ;

read :
        READ_SYMBOL identifier
            {
                $$ = new SyntaxTreeReadNode(@$, $2);
            }
    ;

write :
        WRITE_SYMBOL intExpr
            {
                $$ = new SyntaxTreeWriteNode(@$, $2);
            }
    |    WRITE_SYMBOL boolExpr
            {
                $$ = new SyntaxTreeWriteNode(@$, $2);
            }
    ;

ifStatement :
        IF_SYMBOL boolExpr THEN_SYMBOL statement ELSE_SYMBOL statement
            {
                $$ = new SyntaxTreeIfStatementNode(@$, $2, $4, $6);
            }
    |    IF_SYMBOL boolExpr THEN_SYMBOL statement
            {
                $$ = new SyntaxTreeIfStatementNode(@$, $2, $4, null);
            }
    ;

whileStatement :
        WHILE_SYMBOL boolExpr DO_SYMBOL statement
            {
                $$ = new SyntaxTreeWhileStatementNode(@$, $2, $4);
            }
    ;