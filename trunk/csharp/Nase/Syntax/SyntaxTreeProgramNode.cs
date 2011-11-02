using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Nase.Syntax
{
    class SyntaxTreeProgramNode : SyntaxTreeNode
    {
        public SyntaxTreeProgramNode(FilePosition position, SyntaxTreeNode statementSequenceNode)
            : base(position)
        {
            this._children.Add(statementSequenceNode);
        }
    }
}
