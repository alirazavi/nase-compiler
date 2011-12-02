using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Nase.Files;

namespace Nase.Syntax
{
    class SyntaxTreeIfStatementNode : SyntaxTreeNode
    {
        static readonly Logger Logger = LogManager.CreateLogger();

        public SyntaxTreeIfStatementNode(FilePosition position, SyntaxTreeNode conditionNode,
            SyntaxTreeNode thenStatement, SyntaxTreeNode elseStatement)
            : base(position)
        {
            this._children.Add(conditionNode);
            this._children.Add(thenStatement);
            this._children.Add(elseStatement);
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
            bool result = this._children[0].CheckForIntegrity() && this._children[1].CheckForIntegrity();
            if (this._children[2] != null)
            {
                result &= this._children[2].CheckForIntegrity();
            }
            return result;
        }

        public override bool CheckForTypeMismatch()
        {
            if (!base.CheckForTypeMismatch())
            {
                return false;
            }

            var boolExpr = this._children[0] as ITypedExpression;
            if (boolExpr == null ||
                boolExpr.GetExpressionType() != ExpressionType.Boolean)
            {
                Logger.Error(ContextErrorString("If statement expects a boolean expression as condition."));
                return false;
            }

            return true;
        }

        public override void GenerateCode(FileManager fileManager, SymbolTable symbolTable, CodeGeneratorHelper codeGeneratorHelper)
        {
            AppendNodeComment(fileManager);

            // Check condition
            this._children[0].GenerateCode(fileManager, symbolTable, codeGeneratorHelper);

            // If the condition is false, jump to the else-statement (exit label if else-statement is null)
            string elseLabel = codeGeneratorHelper.GenerateNextLabel();
            fileManager.Output.Append(Macro.JumpOnFalse(elseLabel));

            // Then-statement
            this._children[1].GenerateCode(fileManager, symbolTable, codeGeneratorHelper);

            // On existing else-statement jump over it
            string outLabel = null;
            if (this._children[2] != null)
            {
                outLabel = codeGeneratorHelper.GenerateNextLabel();
                fileManager.Output.Append(Macro.Jump(outLabel));
            }

            // Generate else label
            fileManager.Output.Append(Macro.Label(elseLabel));

            // Else-statement and exit label
            if (this._children[2] != null)
            {
                this._children[2].GenerateCode(fileManager, symbolTable, codeGeneratorHelper);
                fileManager.Output.Append(Macro.Label(outLabel));
            }
        }
    }
}
