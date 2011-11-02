using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Nase.Syntax
{
    class SyntaxTreeWriteNode : SyntaxTreeNode
    {
        public SyntaxTreeWriteNode(FilePosition position, SyntaxTreeNode identNode)
            : base(position)
        {
            this._children.Add(identNode);
        }
    }
}
