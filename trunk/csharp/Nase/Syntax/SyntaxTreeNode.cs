using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Nase.Syntax
{
    abstract class SyntaxTreeNode
    {
        protected FilePosition _position;
        protected List<SyntaxTreeNode> _children;

        public SyntaxTreeNode(FilePosition position)
        {
            this._children = new List<SyntaxTreeNode>();
            this._position = position;
        }

        public virtual void AsString(StringBuilder b, int level)
        {
            for(int i = 0; i < level; ++i)
            {
                b.Append(" . ");
            }
            b.Append(this._position.Line);
            b.Append(":");
            b.Append(this._position.Column);
            b.Append(" ");
            b.Append(this.GetType().Name);
            b.Append("\n");

            foreach (var node in this._children)
            {
                node.AsString(b, level + 1);
            }
        }
    }
}
