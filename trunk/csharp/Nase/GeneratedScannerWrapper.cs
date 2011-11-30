using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Nase.GeneratedScanner;
using Nase.GeneratedParser;
using Nase.Files;

namespace Nase
{
    class GeneratedScannerWrapper : IScanner
    {
        NaseScanner _scanner;
        SymbolTable _symbolTable;
        Symbol _peekSymbol;

        public GeneratedScannerWrapper(FileManager fileManager, SymbolTable symbolTable)
        {
            this._scanner = new NaseScanner(fileManager.Input);
            this._scanner.SymbolTable = symbolTable;
            this._symbolTable = symbolTable;
            this._peekSymbol = (Symbol)this._scanner.yylex();
            if (this._peekSymbol == Symbol.INTEGER_LITERAL_SYMBOL || this._peekSymbol == Symbol.IDENTIFIER_SYMBOL)
            {
                UpdateSymbol();
            }
        }

        public FilePosition InputFilePosition
        {
            get
            {
                return new FilePosition()
                {
                    StartLine = this._scanner.yylloc.StartLine,
                    StartColumn = this._scanner.yylloc.StartColumn + 1
                };
            }
        }

        public Symbol NextSymbol()
        {
            this._peekSymbol = (Symbol)this._scanner.yylex();
            if (this._peekSymbol == Symbol.INTEGER_LITERAL_SYMBOL || this._peekSymbol == Symbol.IDENTIFIER_SYMBOL)
            {
                UpdateSymbol();
            }
            Console.WriteLine(this._peekSymbol);
            return this._peekSymbol;
        }

        public void SkipToDelimiter()
        {
            while(this._peekSymbol != Symbol.DELIMITER_SYMBOL &&
                this._peekSymbol != Symbol.EOF)
            {
                NextSymbol();
            }
        }

        public Symbol PeekSymbol()
        {
            return this._peekSymbol;
        }

        void UpdateSymbol()
        {
            this._peekSymbol = this._symbolTable.ClassifySymbol(this._scanner.yytext);
        }
    }
}
