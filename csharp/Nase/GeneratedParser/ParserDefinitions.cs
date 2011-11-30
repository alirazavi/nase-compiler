using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using QUT.Gppg;
using Nase.GeneratedScanner;
using Nase.Files;
using Nase.Syntax;

namespace Nase.GeneratedParser
{
    public partial class NaseParser : ShiftReduceParser<ValueType, FilePosition>
    {
        SymbolTable _symbolTable;

        public SyntaxTree SyntaxTree { get; private set; }

        public NaseParser(NaseScanner scanner, SymbolTable symbolTable) : base(scanner)
        {
            this._symbolTable = symbolTable;
        }
    }
}
