using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Nase.Files;

namespace Nase.Syntax
{
    class SyntaxTreeErrorNode : SyntaxTreeNode
    {
        static readonly Logger Logger = LogManager.CreateLogger();

        public SyntaxTreeErrorNode(FilePosition position)
            : base(position)
        { }

        public override bool CheckForIntegrity()
        {
            return true;
        }
    }
}
