%namespace Nase.GeneratedScanner
%scannertype NaseScanner
%option out:NaseScanner.cs
%using Nase;
%tokentype Symbol

digit [0-9]
alpha [a-z]
//floatDenotation [\+\-]?{digit}+(\.{digit}*)?(E[\+\-]?{digit}+)?
comment \$.*$


%%


[ \t]+ /* skip whitespace */
{comment} /* skip comments */
;            { return (int)Symbol.DELIMITER_SYMBOL; }
BEGIN        { return (int)Symbol.BEGIN_SYMBOL; }
END          { return (int)Symbol.END_SYMBOL; }
INTEGER      { return (int)Symbol.INT_TYPE_SYMBOL; }
READ         { return (int)Symbol.READ_SYMBOL; }
WRITE        { return (int)Symbol.WRITE_SYMBOL; }
AND          { return (int)Symbol.AND_SYMBOL; }
OR           { return (int)Symbol.OR_SYMBOL; }
IIF          { return (int)Symbol.INLINE_IF_SYMBOL; }
FII          { return (int)Symbol.INLINE_FI_SYMBOL; }
,            { return (int)Symbol.COMMA_SYMBOL; }
:=           { return (int)Symbol.ASSIGN_SYMBOL; }
\-           { return (int)Symbol.MINUS_SYMBOL; }
\+           { return (int)Symbol.PLUS_SYMBOL; }
\*           { return (int)Symbol.TIMES_SYMBOL; }
/            { return (int)Symbol.DIVIDE_SYMBOL; }
%            { return (int)Symbol.MODULO_SYMBOL; }
\?           { return (int)Symbol.INLINE_THEN_SYMBOL; }
:            { return (int)Symbol.INLINE_ELSE_SYMBOL; }
\<>          { return (int)Symbol.NE_SYMBOL; }
\<=           { return (int)Symbol.LE_SYMBOL; }
\<            { return (int)Symbol.LT_SYMBOL; }
>=           { return (int)Symbol.GE_SYMBOL; }
>            { return (int)Symbol.GT_SYMBOL; }
=            { return (int)Symbol.EQ_SYMBOL; }

{digit}+     { return (int)Symbol.NULL_SYMBOL; }
{alpha}+     { return (int)Symbol.NULL_SYMBOL; }


/*{floatDenotation}   {
                        Console.WriteLine("float found: {0}", yytext);
                        yylval.dVal = Double.Parse(yytext);
                        Symbol symbol = this.SymbolTable.ClassifySymbol(yytext);
                        if (symbol == Symbol.NULL_SYMBOL)
                        {
                            symbol = this.SymbolTable.AddUserSymbol(yytext);
                        }
                        return (int)symbol;
                    }
*/