using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Nase.GeneratedParser;
using Nase.Files;

namespace Nase.Syntax
{
    class SyntaxTreeDeclarationNode : SyntaxTreeNode
    {
        static readonly Logger Logger = LogManager.CreateLogger();

        internal Symbol Identifier { get; private set; }
        ulong _memoryAddress;

        public SyntaxTreeDeclarationNode(FilePosition position, SyntaxTreeNode typeNode, Symbol identifier)
            : base(position)
        {
            this._children.Add(typeNode);
            this.Identifier = identifier;
        }

        public void SetMemoryAddress(CodeGeneratorHelper storage)
        {
            switch (GetTypeSymbol())
            {
                case Symbol.INT_TYPE_SYMBOL:
                    this._memoryAddress = storage.GetAndPushVariableAddress();
                    break;
                case Symbol.BOOL_TYPE_SYMBOL:
                    this._memoryAddress = storage.GetAndPushVariableAddress();
                    break;
            }
        }

        public ulong GetMemoryAddress()
        {
            return this._memoryAddress;
        }

        public Symbol GetTypeSymbol()
        {
            if (this._children[0] != null)
            {
                SyntaxTreeTypeNode typeNode = this._children[0] as SyntaxTreeTypeNode;
                return typeNode.TypeSymbol;
            }
            else
            {
                throw new Exception("Type node must not be null. Should not happen after integrity check.");
            }
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
                if(node != null)
                    node.AsString(b, level + 1);
            }
        }

        public override bool CheckForIntegrity()
        {
            if (this._children.Count != 1)
            {
                Logger.Error("Type node is missing.");
                return false;
            }
            if (this._children[0] == null)
            {
                Logger.Error("Type node must not be null.");
                return false;
            }
            return this._children[0].CheckForIntegrity();
        }
    }
}
