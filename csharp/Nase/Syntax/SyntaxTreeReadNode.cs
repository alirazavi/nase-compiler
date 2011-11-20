﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Nase.Syntax
{
    class SyntaxTreeReadNode : SyntaxTreeNode
    {
        static readonly Logger Logger = LogManager.CreateLogger();

        public SyntaxTreeReadNode(FilePosition position, SyntaxTreeNode identNode)
            : base(position)
        {
            this._children.Add(identNode);
        }

        public override bool CheckForIntegrity()
        {
            if (this._children.Count != 1)
            {
                Logger.Debug("Identifier node is missing.");
                return false;
            }
            if (this._children[0] == null)
            {
                Logger.Debug("Identifier node must not be null.");
                return false;
            }
            return this._children[0].CheckForIntegrity();
        }

        public override void GenerateCode(FileManager fileManager, SymbolTable symbolTable, CodeGeneratorHelper labelHelper)
        {
            AppendNodeComment(fileManager);

            SyntaxTreeIdentNode identNode = this._children[0] as SyntaxTreeIdentNode;
            Symbol identSymbol = identNode.Identifier;
            SyntaxTreeDeclarationNode declNode = symbolTable.GetDeclarationNodeLinkToSymbol(identSymbol) as SyntaxTreeDeclarationNode;

            fileManager.Output.Append(Macro.ReadValueIntoAccu());
            fileManager.Output.Append(Macro.StoreAccu(declNode.GetMemoryAddress()));
        }
    }
}
