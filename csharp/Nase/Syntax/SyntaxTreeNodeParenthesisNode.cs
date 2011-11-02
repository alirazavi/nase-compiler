using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Nase.Syntax
{
    class SyntaxTreeNodeParenthesisNode : SyntaxTreeNode
    {
        public SyntaxTreeNodeParenthesisNode(FilePosition position, SyntaxTreeNode intExprNode)
            : base(position)
        {
            this._children.Add(intExprNode);
        }
    }
}
