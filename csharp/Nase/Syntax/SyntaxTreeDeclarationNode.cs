﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

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
            if (this._children[0] != null)
            {
                SyntaxTreeTypeNode typeNode = this._children[0] as SyntaxTreeTypeNode;

                switch (typeNode.TypeSymbol)
                {
                    case Symbol.INT_TYPE_SYMBOL:
                        this._memoryAddress = storage.GetAndPushVariableAddress();
                        break;
                }
            }
            else
            {
                throw new Exception("Type node must not be null.");
            }
        }

        public ulong GetMemoryAddress()
        {
            return this._memoryAddress;
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
