using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Nase.Syntax
{
    class SyntaxTreeReadNode : SyntaxTreeNode
    {
        public SyntaxTreeReadNode(FilePosition position, SyntaxTreeNode identNode)
            : base(position)
        {
            this._children.Add(identNode);
        }
    }
}
