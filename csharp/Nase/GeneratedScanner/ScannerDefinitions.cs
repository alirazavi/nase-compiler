using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using QUT.Gppg;
using Nase;

namespace Nase.GeneratedScanner
{
    public enum Tokens
    {
        EOF = 0,
        Number,

        maxParseToken = int.MaxValue
    }

    public struct ValueType
    {
        public double dVal;
        public char cVal;
        public int iVal;
    }

    public abstract class ScanBase : AbstractScanner<ValueType, LexLocation>
    {
        //public abstract int yylex();

        public SymbolTable SymbolTable { get; private set; }

        protected virtual bool yywrap() { return true; }

        public ScanBase()
        {
            this.SymbolTable = new SymbolTable();
        }
    }
}
