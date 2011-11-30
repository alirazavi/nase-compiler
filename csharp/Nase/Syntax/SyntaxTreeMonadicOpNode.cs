using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Nase.GeneratedParser;
using Nase.Files;

namespace Nase.Syntax
{
    class SyntaxTreeMonadicOpNode : SyntaxTreeNode, ITypedExpression
    {
        static readonly Logger Logger = LogManager.CreateLogger();

        Symbol _opCodeSymbol;

        public SyntaxTreeMonadicOpNode(FilePosition position, Symbol opCodeSymbol, SyntaxTreeNode operand)
            : base(position)
        {
            this._children.Add(operand);
            this._opCodeSymbol = opCodeSymbol;
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
            b.Append("( Symbol = "); b.Append(this._opCodeSymbol); b.Append(" )");
            b.Append("\n");

            foreach (var node in this._children)
            {
                if (node != null)
                    node.AsString(b, level + 1);
            }
        }

        public override bool CheckForIntegrity()
        {
            if (this._children.Count != 1)
            {
                Logger.Debug("Operand node is missing.");
                return false;
            }
            if (this._children[0] == null)
            {
                Logger.Debug("Operand node must not be null.");
                return false;
            }
            return this._children[0].CheckForIntegrity();
        }

        public override bool CheckForTypeMismatch()
        {
            if (base.CheckForTypeMismatch())
            {
                var expr = this._children[0] as ITypedExpression;
                if (expr.GetExpressionType() == this.GetExpressionType())
                {
                    return true;
                }
            }
            return false;
        }

        public override void GenerateCode(FileManager fileManager, SymbolTable symbolTable, CodeGeneratorHelper codeGeneratorHelper)
        {
            AppendNodeComment(fileManager);

            this._children[0].GenerateCode(fileManager, symbolTable, codeGeneratorHelper);

            fileManager.Output.Append(Macro.MonadicOperator(this._opCodeSymbol));
        }

        public ExpressionType GetExpressionType()
        {
            switch (this._opCodeSymbol)
            {
                case Symbol.MINUS_SYMBOL:
                    return ExpressionType.Integer;
                case Symbol.NOT_SYMBOL:
                    return ExpressionType.Boolean;
                default:
                    throw new Exception("Unknown opcode symbol.");
            }
        }
    }
}
