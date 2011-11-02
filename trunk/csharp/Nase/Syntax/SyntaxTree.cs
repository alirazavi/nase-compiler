using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Nase.Syntax
{
    class SyntaxTree
    {
        SyntaxTreeNode _rootNode = null;

        public SyntaxTree(SyntaxTreeNode rootNode)
        {
            this._rootNode = rootNode;
        }

        public string DumpTreeTable()
        {
            StringBuilder b = new StringBuilder();
            b.AppendLine();
            b.AppendLine("SyntaxTree Dump");
            b.AppendLine("---------------");
            this._rootNode.AsString(b, 0);
            return b.ToString();
        }
    }
}
