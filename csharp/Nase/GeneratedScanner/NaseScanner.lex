%namespace Nase.GeneratedScanner
%scannertype NaseScanner
%option out:NaseScanner.cs
%using Nase;
%using Nase.GeneratedParser;
%using Nase.Files;
%using QUT.Gppg;
%tokentype Symbol

digit [0-9]
alpha [a-z]
comment \$.*$

%%

%{
    Symbol symbol;
%}

[ \t\n]+    /* skip whitespace */
{comment}   /* skip comments */
;           { return (int)Symbol.DELIMITER_SYMBOL; }
,           { return (int)Symbol.COMMA_SYMBOL; }
\(          { return (int)Symbol.OPEN_PARENTHESIS_SYMBOL; }
\)          { return (int)Symbol.CLOSE_PARENTHESIS_SYMBOL; }
BEGIN       { return (int)Symbol.BEGIN_SYMBOL; }
END         { return (int)Symbol.END_SYMBOL; }
INTEGER     { return (int)Symbol.INT_TYPE_SYMBOL; }
BOOLEAN     { return (int)Symbol.BOOL_TYPE_SYMBOL; }
READ        { return (int)Symbol.READ_SYMBOL; }
WRITE       { return (int)Symbol.WRITE_SYMBOL; }
AND         { return (int)Symbol.AND_SYMBOL; }
OR          { return (int)Symbol.OR_SYMBOL; }
NOT         { return (int)Symbol.NOT_SYMBOL; }
IIF         { return (int)Symbol.INLINE_IF_SYMBOL; }
FII         { return (int)Symbol.INLINE_FI_SYMBOL; }
:=          { return (int)Symbol.ASSIGN_SYMBOL; }
\-          { return (int)Symbol.MINUS_SYMBOL; }
\+          { return (int)Symbol.PLUS_SYMBOL; }
\*          { return (int)Symbol.TIMES_SYMBOL; }
/           { return (int)Symbol.DIVIDE_SYMBOL; }
%           { return (int)Symbol.MODULO_SYMBOL; }
\?          { return (int)Symbol.INLINE_THEN_SYMBOL; }
:           { return (int)Symbol.INLINE_ELSE_SYMBOL; }
\<>         { return (int)Symbol.NE_SYMBOL; }
\<=         { return (int)Symbol.LE_SYMBOL; }
\<          { return (int)Symbol.LT_SYMBOL; }
>=          { return (int)Symbol.GE_SYMBOL; }
>           { return (int)Symbol.GT_SYMBOL; }
=           { return (int)Symbol.EQ_SYMBOL; }
TRUE        { return (int)Symbol.TRUE_SYMBOL; }
FALSE       { return (int)Symbol.FALSE_SYMBOL; }

//{digit}+    { return (int)Symbol.NULL_SYMBOL; }
{digit}+    {
                symbol = SymbolTable.ClassifySymbol(yytext);
                if(symbol == Symbol.NULL_SYMBOL)
                {
                    symbol = SymbolTable.AddUserSymbol(yytext);
                }
                yylval.symbol = symbol;
                return (int)Symbol.INTEGER_LITERAL_SYMBOL;
            }

{alpha}+    {
                symbol = SymbolTable.ClassifySymbol(yytext);
                if(symbol == Symbol.NULL_SYMBOL)
                {
                    symbol = SymbolTable.AddUserSymbol(yytext);
                }
                yylval.symbol = symbol;
                return (int)Symbol.IDENTIFIER_SYMBOL;
            }

%{
    yylloc = new FilePosition(tokLin,tokCol,tokELin,tokECol);
%}