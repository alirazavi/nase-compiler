using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Nase.Files;

namespace Nase.Syntax
{
    class SyntaxTreeParenthesisNode : SyntaxTreeNode
    {
        static readonly Logger Logger = LogManager.CreateLogger();

        public SyntaxTreeParenthesisNode(FilePosition position, SyntaxTreeNode intExprNode)
            : base(position)
        {
            this._children.Add(intExprNode);
        }

        public override bool CheckForIntegrity()
        {
            if (this._children.Count != 1)
            {
                Logger.Debug("Expression node is missing.");
                return false;
            }
            if (this._children[0] == null)
            {
                Logger.Debug("Expression node must not be null.");
                return false;
            }
            return this._children[0].CheckForIntegrity();
        }

        public override void GenerateCode(FileManager fileManager, SymbolTable symbolTable, CodeGeneratorHelper codeGenerationHelper)
        {
            AppendNodeComment(fileManager);

            this._children[0].GenerateCode(fileManager, symbolTable, codeGenerationHelper);
        }
    }
}
