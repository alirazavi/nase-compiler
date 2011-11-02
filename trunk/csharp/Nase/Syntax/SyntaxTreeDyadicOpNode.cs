using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Nase.Syntax
{
    class SyntaxTreeDyadicOpNode :SyntaxTreeNode
    {
        Symbol _opCodeSymbol;

        public SyntaxTreeDyadicOpNode(FilePosition position, Symbol opCodeSymbol,
            SyntaxTreeNode leftHandOperand, SyntaxTreeNode rightHandOperand)
            : base(position)
        {
            this._children.Add(leftHandOperand);
            this._children.Add(rightHandOperand);
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
