%namespace Nase.GeneratedScanner
%scannertype NaseScanner
%option out:NaseScanner.cs
%using Nase;
%using Nase.GeneratedParser;
%using Nase.Files;
%using QUT.Gppg;
%tokentype Symbol

digit          [0-9]
alpha          [a-z]
comment        \$.*$

beginKeyword   [Bb][Ee][Gg][Ii][Nn]
endKeyword     [Ee][Nn][Dd]
integerKeyword [Ii][Nn][Tt][Ee][Gg][Ee][Rr]
booleanKeyword [Bb][Oo][Oo][Ll][Ee][Aa][Nn]
readKeyword    [Rr][Ee][Aa][Dd]
writeKeyword   [Ww][Rr][Ii][Tt][Ee]
andKeyword     [Aa][Nn][Dd]
orKeyword      [Oo][Rr]
notKeyword     [Nn][Oo][Tt]
iifKeyword     [Ii][Ii][Ff]
fiiKeyword     [Ff][Ii][Ii]
trueKeyword    [Tt][Rr][Uu][Ee]
falseKeyword   [Ff][Aa][Ll][Ss][Ee]
ifKeyword      [Ii][Ff]
thenKeyword    [Tt][Hh][Ee][Nn]
elseKeyword    [Ee][Ll][Ss][Ee]
whileKeyword   [Ww][Hh][Ii][Ll][Ee]
doKeyword      [Dd][Oo]

%%

%{
    Symbol symbol;
%}

[ \t\n]+              /* skip whitespace */
{comment}             /* skip comments */
;                     { return (int)Symbol.DELIMITER_SYMBOL; }
,                     { return (int)Symbol.COMMA_SYMBOL; }
\(                    { return (int)Symbol.OPEN_PARENTHESIS_SYMBOL; }
\)                    { return (int)Symbol.CLOSE_PARENTHESIS_SYMBOL; }
{beginKeyword}        { return (int)Symbol.BEGIN_SYMBOL; }
{endKeyword}          { return (int)Symbol.END_SYMBOL; }
{integerKeyword}      { return (int)Symbol.INT_TYPE_SYMBOL; }
{booleanKeyword}      { return (int)Symbol.BOOL_TYPE_SYMBOL; }
{readKeyword}         { return (int)Symbol.READ_SYMBOL; }
{writeKeyword}        { return (int)Symbol.WRITE_SYMBOL; }
{ifKeyword}           { return (int)Symbol.IF_SYMBOL; }
{thenKeyword}         { return (int)Symbol.THEN_SYMBOL; }
{elseKeyword}         { return (int)Symbol.ELSE_SYMBOL; }
{whileKeyword}        { return (int)Symbol.WHILE_SYMBOL; }
{doKeyword}           { return (int)Symbol.DO_SYMBOL; }
{andKeyword}          { return (int)Symbol.AND_SYMBOL; }
{orKeyword}           { return (int)Symbol.OR_SYMBOL; }
{notKeyword}          { return (int)Symbol.NOT_SYMBOL; }
{iifKeyword}          { return (int)Symbol.INLINE_IF_SYMBOL; }
{fiiKeyword}          { return (int)Symbol.INLINE_FI_SYMBOL; }
:=                    { return (int)Symbol.ASSIGN_SYMBOL; }
\-                    { return (int)Symbol.MINUS_SYMBOL; }
\+                    { return (int)Symbol.PLUS_SYMBOL; }
\*                    { return (int)Symbol.TIMES_SYMBOL; }
/                     { return (int)Symbol.DIVIDE_SYMBOL; }
%                     { return (int)Symbol.MODULO_SYMBOL; }
\?                    { return (int)Symbol.INLINE_THEN_SYMBOL; }
:                     { return (int)Symbol.INLINE_ELSE_SYMBOL; }
\<>                   { return (int)Symbol.NE_SYMBOL; }
\<=                   { return (int)Symbol.LE_SYMBOL; }
\<                    { return (int)Symbol.LT_SYMBOL; }
>=                    { return (int)Symbol.GE_SYMBOL; }
>                     { return (int)Symbol.GT_SYMBOL; }
=                     { return (int)Symbol.EQ_SYMBOL; }
{trueKeyword}         { return (int)Symbol.TRUE_SYMBOL; }
{falseKeyword}        { return (int)Symbol.FALSE_SYMBOL; }

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
    yylloc = new FilePosition(tokLin,tokCol + 1,tokELin,tokECol + 1);
%}
