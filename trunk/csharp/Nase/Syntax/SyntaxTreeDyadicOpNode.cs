using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Nase.GeneratedParser;
using Nase.Files;

namespace Nase.Syntax
{
    class SyntaxTreeDyadicOpNode : SyntaxTreeNode, ITypedExpression
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

        public override bool CheckForTypeMismatch()
        {
            if (!base.CheckForTypeMismatch())
            {
                return false;
            }

            var leftOp = this._children[0] as ITypedExpression;
            var rightOp = this._children[1] as ITypedExpression;
            if (leftOp == null ||
                rightOp == null)
            {
                return false;
            }

            switch (this._opCodeSymbol)
            {
                case Symbol.AND_SYMBOL:
                case Symbol.OR_SYMBOL:
                    if (leftOp.GetExpressionType() != ExpressionType.Boolean ||
                        rightOp.GetExpressionType() != ExpressionType.Boolean)
                    {
                        Logger.Error(ContextErrorString("Boolean expressions expected on left and right side of {0}.", this._opCodeSymbol));
                        return false;
                    }
                    break;
                case Symbol.PLUS_SYMBOL:
                case Symbol.MINUS_SYMBOL:
                case Symbol.TIMES_SYMBOL:
                case Symbol.DIVIDE_SYMBOL:
                case Symbol.MODULO_SYMBOL:
                case Symbol.EQ_SYMBOL:
                case Symbol.NE_SYMBOL:
                case Symbol.LT_SYMBOL:
                case Symbol.LE_SYMBOL:
                case Symbol.GT_SYMBOL:
                case Symbol.GE_SYMBOL:
                    if (leftOp.GetExpressionType() != ExpressionType.Integer ||
                        rightOp.GetExpressionType() != ExpressionType.Integer)
                    {
                        Logger.Error(ContextErrorString("Integer expressions exprected on left and right side of {0}.", this._opCodeSymbol));
                        return false;
                    }
                    break;
                default:
                    throw new Exception("Unknown opcode symbol.");
            }
            return true;
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

        public ExpressionType GetExpressionType()
        {
            switch (this._opCodeSymbol)
            {
                case Symbol.PLUS_SYMBOL:
                case Symbol.MINUS_SYMBOL:
                case Symbol.TIMES_SYMBOL:
                case Symbol.DIVIDE_SYMBOL:
                case Symbol.MODULO_SYMBOL:
                    return ExpressionType.Integer;

                case Symbol.AND_SYMBOL:
                case Symbol.OR_SYMBOL:
                case Symbol.EQ_SYMBOL:
                case Symbol.NE_SYMBOL:
                case Symbol.LT_SYMBOL:
                case Symbol.LE_SYMBOL:
                case Symbol.GT_SYMBOL:
                case Symbol.GE_SYMBOL:
                    return ExpressionType.Boolean;

                default:
                    throw new Exception("Unknown opcode symbol.");
            }
        }
    }
}
