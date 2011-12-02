using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Nase.Files;

namespace Nase.Syntax
{
    class SyntaxTreeForStatementNode : SyntaxTreeNode
    {
        static readonly Logger Logger = LogManager.CreateLogger();

        public SyntaxTreeForStatementNode(FilePosition position, SyntaxTreeNode variableInitialization,
            SyntaxTreeNode condition, SyntaxTreeNode variableAltering, SyntaxTreeNode statement)
            : base(position)
        {
            this._children.Add(variableInitialization);
            this._children.Add(condition);
            this._children.Add(variableAltering);
            this._children.Add(statement);
        }

        public override bool CheckForIntegrity()
        {
            if (this._children.Count != 4)
            {
                Logger.Debug("Initialization assignment, condition, altering assignment and/or statement are missing.");
                return false;
            }
            if (this._children[0] == null)
            {
                Logger.Debug("Initialization assignment must not be null.");
                return false;
            }
            if (this._children[1] == null)
            {
                Logger.Debug("Condition expression must not be null.");
                return false;
            }
            if (this._children[2] == null)
            {
                Logger.Debug("Altering assignment must not be null.");
                return false;
            }
            if (this._children[3] == null)
            {
                Logger.Debug("Statement must not be null.");
                return false;
            }
            return this._children[0].CheckForIntegrity()
                && this._children[1].CheckForIntegrity()
                && this._children[2].CheckForIntegrity()
                && this._children[3].CheckForIntegrity();
        }

        public override bool CheckForTypeMismatch()
        {
            if (!base.CheckForTypeMismatch())
            {
                return false;
            }

            var boolExpr = this._children[1] as ITypedExpression;
            if (boolExpr == null ||
                boolExpr.GetExpressionType() != ExpressionType.Boolean)
            {
                Logger.Error(ContextErrorString("For statement expects a boolean expression as condition."));
                return false;
            }

            return true;
        }

        public override void GenerateCode(FileManager fileManager, SymbolTable symbolTable, CodeGeneratorHelper codeGeneratorHelper)
        {
            AppendNodeComment(fileManager);

            // Initialization assignment
            this._children[0].GenerateCode(fileManager, symbolTable, codeGeneratorHelper);

            // Generate loop start label
            string startLabel = codeGeneratorHelper.GenerateNextLabel();
            fileManager.Output.Append(Macro.Label(startLabel));

            // Check condition and jump out if false
            this._children[1].GenerateCode(fileManager, symbolTable, codeGeneratorHelper);
            string outLabel = codeGeneratorHelper.GenerateNextLabel();
            fileManager.Output.Append(Macro.JumpOnFalse(outLabel));

            // Loop body
            this._children[3].GenerateCode(fileManager, symbolTable, codeGeneratorHelper);

            // Altering assignment
            this._children[2].GenerateCode(fileManager, symbolTable, codeGeneratorHelper);

            // Jump back to condition check
            fileManager.Output.Append(Macro.Jump(startLabel));

            // Generate loop end label
            fileManager.Output.Append(Macro.Label(outLabel));
        }
    }
}
