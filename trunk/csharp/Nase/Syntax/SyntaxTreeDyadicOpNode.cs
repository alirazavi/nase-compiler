﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Nase.Syntax
{
    class SyntaxTreeDyadicOpNode :SyntaxTreeNode
    {
        static readonly Logger Logger = LogManager.CreateLogger();

        Symbol _opCodeSymbol;

        public SyntaxTreeDyadicOpNode(FilePosition position, Symbol opCodeSymbol,
            SyntaxTreeNode leftHandOperand, SyntaxTreeNode rightHandOperand)
            : base(position)
        {
            this._children.Add(leftHandOperand);
            this._children.Add(rightHandOperand);
            this._opCodeSymbol = opCodeSymbol;
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
            b.Append("( Symbol = "); b.Append(this._opCodeSymbol); b.Append(" )");
            b.Append("\n");

            foreach (var node in this._children)
            {
                node.AsString(b, level + 1);
            }
        }

        public override bool CheckForIntegrity()
        {
            if (this._children.Count != 2)
            {
                Logger.Debug("Left and/or right operand are missing.");
                return false;
            }
            if (this._children[0] == null)
            {
                Logger.Debug("Left operand must not be null.");
                return false;
            }
            if (this._children[1] == null)
            {
                Logger.Debug("Right operand must not be null.");
                return false;
            }
            return this._children[0].CheckForIntegrity()
                && this._children[1].CheckForIntegrity();
        }

        public override void GenerateCode(FileManager fileManager, SymbolTable symbolTable, CodeGeneratorHelper codeGeneratorHelper)
        {
            AppendNodeComment(fileManager);

            this._children[0].GenerateCode(fileManager, symbolTable, codeGeneratorHelper);

            ulong tempMemoryAddress = codeGeneratorHelper.GetAndPushVariableAddress();
            fileManager.Output.Append(Macro.StoreAccu(tempMemoryAddress));

            this._children[1].GenerateCode(fileManager, symbolTable, codeGeneratorHelper);

            fileManager.Output.Append(Macro.DyadicOperator(this._opCodeSymbol, tempMemoryAddress));
            codeGeneratorHelper.PopVariableAddress();
        }
    }
}
