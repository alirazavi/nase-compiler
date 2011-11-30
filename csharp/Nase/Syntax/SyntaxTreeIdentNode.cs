using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Nase.GeneratedParser;
using Nase.Files;

namespace Nase.Syntax
{
    class SyntaxTreeIdentNode : SyntaxTreeNode, ITypedExpression
    {
        internal Symbol Identifier { get; private set; }
        internal SyntaxTreeNode DeclarationNode { get; set; }

        public SyntaxTreeIdentNode(FilePosition position, Symbol identifier)
            : base(position)
        {
            this.Identifier = identifier;
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
            b.Append("( Symbol = "); b.Append(this.Identifier); b.Append(" )");
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

        public override void GenerateCode(FileManager fileManager, SymbolTable symbolTable, CodeGeneratorHelper labelHelper)
        {
            AppendNodeComment(fileManager);

            SyntaxTreeDeclarationNode declNode = symbolTable.GetDeclarationNodeLinkToSymbol(this.Identifier) as SyntaxTreeDeclarationNode;

            fileManager.Output.Append(Macro.LoadAccu(declNode.GetMemoryAddress()));
        }

        public ExpressionType GetExpressionType()
        {
            var declNode = DeclarationNode as SyntaxTreeDeclarationNode;
            if (declNode != null)
            {
                switch (declNode.GetTypeSymbol())
                {
                    case Symbol.INT_TYPE_SYMBOL:
                        return ExpressionType.Integer;
                    case Symbol.BOOL_TYPE_SYMBOL:
                        return ExpressionType.Boolean;
                }
            }
            throw new Exception("DeclarationNode must be set to evaluate expression type.");
        }
    }
}
