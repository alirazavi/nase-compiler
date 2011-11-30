using System.Collections.Generic;
using System.Linq;
using System.Text;
using QUT.Gppg;
using Nase;
using Nase.GeneratedParser;
using Nase.Files;

namespace Nase.GeneratedScanner
{
    /*
    public struct ValueType
    {
        public double dVal;
        public char cVal;
        public int iVal;
    }
    */
    public abstract class ScanBase : AbstractScanner<ValueType, FilePosition>
    {
        static readonly Logger Logger = LogManager.CreateLogger();

        //public abstract int yylex();

        protected virtual bool yywrap() { return true; }

        public override FilePosition yylloc { get; set; }

        public SymbolTable SymbolTable { get; set; }

        public ScanBase()
        {
        }

        public override void yyerror(string format, params object[] args)
        {
            Logger.Error(format + string.Format(" near line {0}, column {1}", yylloc.StartLine, yylloc.StartColumn), args);
        }
    }
}
