using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Nase.Syntax
{
    class SyntaxTreeErrorNode : SyntaxTreeNode
    {
        public SyntaxTreeErrorNode(FilePosition position)
            : base(position)
        {}
    }
}
