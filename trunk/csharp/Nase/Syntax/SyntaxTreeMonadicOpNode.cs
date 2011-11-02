using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Nase.Syntax
{
    class SyntaxTreeMonadicOpNode : SyntaxTreeNode
    {
        Symbol _opCodeSymbol;

        public SyntaxTreeMonadicOpNode(FilePosition position, Symbol opCodeSymbol, SyntaxTreeNode operand)
            : base(position)
        {
            this._children.Add(operand);
            this._opCodeSymbol = opCodeSymbol;
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
            b.Append("( Symbol = "); b.Append(this._opCodeSymbol); b.Append(" )");
            b.Append("\n");

            foreach (var node in this._children)
            {
                node.AsString(b, level + 1);
            }
        }
    }
}
