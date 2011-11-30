using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Nase.GeneratedParser;
using Nase.Files;

namespace Nase.Syntax
{
    class SyntaxTreeTypeNode : SyntaxTreeNode
    {
        internal Symbol TypeSymbol { get; private set; }

        public SyntaxTreeTypeNode(FilePosition position, Symbol typeSymbol)
            : base(position)
        {
            this.TypeSymbol = typeSymbol;
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

            b.Append("( Symbol = ");
            b.Append(this.TypeSymbol);
            b.Append(")");
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
    }
}
