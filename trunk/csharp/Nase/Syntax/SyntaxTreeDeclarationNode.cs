using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Nase.Syntax
{
    class SyntaxTreeDeclarationNode : SyntaxTreeNode
    {
        Symbol _identifier;

        public SyntaxTreeDeclarationNode(FilePosition position, Symbol identifier)
            : base(position)
        {
            this._identifier = identifier;
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
            b.Append("( Symbol = "); b.Append(this._identifier); b.Append(" )");
            b.Append("\n");

            foreach (var node in this._children)
            {
                node.AsString(b, level + 1);
            }
        }
    }
}
