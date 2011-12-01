using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Nase.Files;

namespace Nase.Syntax
{
    class SyntaxTreeInlineIfNode : SyntaxTreeNode, ITypedExpression
    {
        static readonly Logger Logger = LogManager.CreateLogger();

        public SyntaxTreeInlineIfNode(FilePosition position, SyntaxTreeNode boolExprNode,
            SyntaxTreeNode thenIntExprNode, SyntaxTreeNode elseIntExprNode)
            : base(position)
        {
            this._children.Add(boolExprNode);
            this._children.Add(thenIntExprNode);
            this._children.Add(elseIntExprNode);
        }

        public override bool CheckForIntegrity()
        {
            if (this._children.Count != 3)
            {
                Logger.Debug("Boolean expression, \"then\"-expression and/or \"else\"-expression are missing.");
                return false;
            }
            if (this._children[0] == null)
            {
                Logger.Debug("Boolean expression must not be null.");
                return false;
            }
            if (this._children[1] == null)
            {
                Logger.Debug("\"Then\"-expression must not be null.");
                return false;
            }
            if (this._children[2] == null)
            {
                Logger.Debug("\"Else\"-Expression must not be null.");
                return false;
            }
            return this._children[0].CheckForIntegrity()
                && this._children[1].CheckForIntegrity()
                && this._children[2].CheckForIntegrity();
        }

        public override bool CheckForTypeMismatch()
        {
            if (!base.CheckForTypeMismatch())
            {
                return false;
            }
            var boolExpr = this._children[0] as ITypedExpression;
            var thenExpr = this._children[1] as ITypedExpression;
            var elseExpr = this._children[2] as ITypedExpression;

            if (boolExpr == null ||
                boolExpr.GetExpressionType() != ExpressionType.Boolean)
            {
                Logger.Error(ContextErrorString("Inline-If expects a boolean expression as condition."));
                return false;
            }
            if (thenExpr == null ||
                elseExpr == null ||
                thenExpr.GetExpressionType() != elseExpr.GetExpressionType())
            {
                Logger.Error(ContextErrorString("Inline-If expects same expression types in then and else."));
                return false;
            }
            return true;
        }

        public override void GenerateCode(FileManager fileManager, SymbolTable symbolTable, CodeGeneratorHelper codeGeneratorHelper)
        {
            AppendNodeComment(fileManager);

            this._children[0].GenerateCode(fileManager, symbolTable, codeGeneratorHelper);

            string elseLabel = codeGeneratorHelper.GenerateNextLabel();
            fileManager.Output.Append(Macro.JumpOnFalse(elseLabel));

            this._children[1].GenerateCode(fileManager, symbolTable, codeGeneratorHelper);

            string fiiLabel = codeGeneratorHelper.GenerateNextLabel();
            fileManager.Output.Append(Macro.Jump(fiiLabel));
            fileManager.Output.Append(Macro.Label(elseLabel));

            this._children[2].GenerateCode(fileManager, symbolTable, codeGeneratorHelper);

            fileManager.Output.Append(Macro.Label(fiiLabel));
        }

        public ExpressionType GetExpressionType()
        {
            var thenExpr = this._children[1] as ITypedExpression;
            return thenExpr.GetExpressionType();
        }
    }
}
