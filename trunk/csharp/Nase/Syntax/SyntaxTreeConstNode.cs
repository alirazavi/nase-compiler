using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Nase.Syntax
{
    class SyntaxTreeConstNode : SyntaxTreeNode
    {
        internal Symbol ConstSymbol { get; private set; }
        bool _addressReserved;
        ulong _memoryAddress;

        public SyntaxTreeConstNode(FilePosition position, Symbol symbol)
            : base(position)
        {
            this.ConstSymbol = symbol;
        }

        public void SetMemoryAddress(CodeGeneratorHelper storage)
        {
            if (!this._addressReserved)
            {
                this._memoryAddress = storage.GetAndPushConstantAddress();
                this._addressReserved = true;
            }
        }

        public override void AsString(StringBuilder b, int level)
        {
            for (int i = 0; i < level; ++i)
            {
                b.Append(" . ");
            }
            b.Append(this._position.Line);
            b.Append(":");
            b.Append(this._position.Column);
            b.Append(" ");
            b.Append(this.GetType().Name);
            b.Append("( Symbol = "); b.Append(this.ConstSymbol); b.Append(" )");
            b.Append("\n");

            foreach (var node in this._children)
            {
                node.AsString(b, level + 1);
            }
        }

        public override bool CheckForIntegrity()
        {
            return true;
        }

        public override void GenerateCode(FileManager fileManager, SymbolTable symbolTable, CodeGeneratorHelper labelHelper)
        {
            AppendNodeComment(fileManager);

            fileManager.Output.Append(Macro.LoadAccuImmed(this._memoryAddress));
        }
    }
}
