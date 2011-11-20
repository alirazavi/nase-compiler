using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Nase.GeneratedScanner;

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
            this._symbolTable = symbolTable;
            this._peekSymbol = (Symbol)this._scanner.yylex();
            if (this._peekSymbol == Symbol.NULL_SYMBOL)
            {
                UpdateSymbol();
            }
        }

        public Symbol NextSymbol()
        {
            this._peekSymbol = (Symbol)this._scanner.yylex();
            if (this._peekSymbol == Symbol.NULL_SYMBOL)
            {
                UpdateSymbol();
            }
            return this._peekSymbol;
        }

        public void SkipToDelimiter()
        {
            throw new Exception("The method or operation is not implemented.");
        }

        public Symbol PeekSymbol()
        {
            return this._peekSymbol;
        }

        public void ScannerTest()
        {
            throw new Exception("The method or operation is not implemented.");
        }

        void UpdateSymbol()
        {
            this._peekSymbol = this._symbolTable.ClassifySymbol(this._scanner.yytext);
            if (this._peekSymbol == Symbol.NULL_SYMBOL)
            {
                this._peekSymbol = this._symbolTable.AddUserSymbol(this._scanner.yytext);
            }
        }
    }
}
