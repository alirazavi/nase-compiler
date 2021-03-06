﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Nase.GeneratedParser;
using Nase.Files;

namespace Nase.Syntax
{
    class SyntaxTreeAssignmentNode : SyntaxTreeNode
    {
        static readonly Logger Logger = LogManager.CreateLogger();

        public SyntaxTreeAssignmentNode(FilePosition position, SyntaxTreeNode identNode, SyntaxTreeNode intExprNode)
            : base(position)
        {
            this._children.Add(identNode);
            this._children.Add(intExprNode);
        }

        public override bool CheckForIntegrity()
        {
            if (this._children.Count != 2)
            {
                Logger.Error("Identifier and/or expression nodes are missing.");
                return false;
            }

            if (this._children[0] == null)
            {
                Logger.Error("Identifier node must not be null.");
                return false;
            }

            if (this._children[1] == null)
            {
                Logger.Error("Expression node must not be null.");
                return false;
            }

            return this._children[0].CheckForIntegrity()
                && this._children[1].CheckForIntegrity();
        }

        public override bool CheckForTypeMismatch()
        {
            if (!base.CheckForTypeMismatch())
            {
                return false;
            }

            var leftNode = this._children[0] as ITypedExpression;
            var rightNode = this._children[1] as ITypedExpression;
            if (leftNode == null ||
                rightNode == null ||
                leftNode.GetExpressionType() != rightNode.GetExpressionType())
            {
                Logger.Error(ContextErrorString("Identifier type doesn't match the expression type at"));
                return false;
            }
            return true;
        }

        public override void GenerateCode(FileManager fileManager, SymbolTable symbolTable, CodeGeneratorHelper codeGeneratorHelper)
        {
            AppendNodeComment(fileManager);
            this._children[1].GenerateCode(fileManager, symbolTable, codeGeneratorHelper);

            SyntaxTreeIdentNode identNode = this._children[0] as SyntaxTreeIdentNode;
            Symbol identSymbol = identNode.Identifier;
            SyntaxTreeDeclarationNode declNode = symbolTable.GetDeclarationNodeLinkToSymbol(identSymbol) as SyntaxTreeDeclarationNode;

            fileManager.Output.Append(Macro.StoreAccu(declNode.GetMemoryAddress()));
        }
    }
}
