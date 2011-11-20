using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Nase.Syntax
{
    class SyntaxTreeIdentNode : SyntaxTreeNode
    {
        internal Symbol Identifier { get; private set; }

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
            b.Append(this._position.Line);
            b.Append(":");
            b.Append(this._position.Column);
            b.Append(" ");
            b.Append(this.GetType().Name);
            b.Append("( Symbol = "); b.Append(this.Identifier); b.Append(" )");
            b.Append("\n");

            foreach (var node in this._children)
            {
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
    }
}
