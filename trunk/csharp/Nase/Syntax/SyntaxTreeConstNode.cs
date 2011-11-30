using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Nase.GeneratedParser;
using Nase.Files;

namespace Nase.Syntax
{
    class SyntaxTreeConstNode : SyntaxTreeNode, ITypedExpression
    {
        internal Symbol ConstSymbol { get; private set; }
        bool _addressReserved;
        ulong _memoryAddress;

        public SyntaxTreeConstNode(FilePosition position, Symbol symbol)
            : base(position)
        {
            this.ConstSymbol = symbol;
        }

        public ExpressionType GetExpressionType()
        {
            if (this.ConstSymbol == Symbol.TRUE_SYMBOL ||
                this.ConstSymbol == Symbol.FALSE_SYMBOL)
            {
                return ExpressionType.Boolean;
            }
            return ExpressionType.Integer;
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
            b.Append(this._position.StartLine);
            b.Append(":");
            b.Append(this._position.StartColumn);
            b.Append(" ");
            b.Append(this.GetType().Name);
            b.Append("( Symbol = "); b.Append(this.ConstSymbol); b.Append(" )");
            b.Append("\n");

            foreach (var node in this._children)
            {
                if (node != null)
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
