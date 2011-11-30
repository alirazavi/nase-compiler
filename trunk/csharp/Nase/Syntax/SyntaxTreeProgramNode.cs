using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Nase.GeneratedParser;
using Nase.Files;

namespace Nase.Syntax
{
    class SyntaxTreeProgramNode : SyntaxTreeNode
    {
        static readonly Logger Logger = LogManager.CreateLogger();

        public SyntaxTreeProgramNode(FilePosition position, SyntaxTreeNode statementSequenceNode)
            : base(position)
        {
            this._children.Add(statementSequenceNode);
        }

        public override bool CheckForIntegrity()
        {
            if (this._children.Count != 1)
            {
                Logger.Debug("Statement sequence node is missing.");
                return false;
            }
            if (this._children[0] == null)
            {
                Logger.Debug("Statement sequence node must not be null.");
                return false;
            }
            return this._children[0].CheckForIntegrity();
        }

        public override void GenerateCode(FileManager fileManager, SymbolTable symbolTable, CodeGeneratorHelper codeGeneratorHelper)
        {
            AppendNodeComment(fileManager);

            fileManager.Output.Append(Macro.Environment());
            fileManager.Output.AppendLine(Macro.OpenConstDefinitionTable());

            this.RunDelegateForType(typeof(SyntaxTreeConstNode), delegate(SyntaxTreeNode node)
            {
                SyntaxTreeConstNode constNode = node as SyntaxTreeConstNode;
                Symbol constSymbol = constNode.ConstSymbol;
                int value = symbolTable.GetValueOfNumericSymbol(constSymbol);
                fileManager.Output.Append(Macro.DefineConstValue(value));
                return true;
            });

            fileManager.Output.Append(Macro.AllocateStaticMemory(codeGeneratorHelper.GetVariableAddressCount()));
            fileManager.Output.Append(Macro.ProgramStart());

            this._children[0].GenerateCode(fileManager, symbolTable, codeGeneratorHelper);

            fileManager.Output.Append(Macro.ProgramEnd());
        }
    }
}
