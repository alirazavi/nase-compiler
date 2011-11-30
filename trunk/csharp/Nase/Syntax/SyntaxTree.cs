using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Nase.GeneratedParser;
using Nase.Files;

namespace Nase.Syntax
{
    public class SyntaxTree
    {
        static readonly Logger Logger = LogManager.CreateLogger();

        internal delegate bool TreeNodeDelegate(SyntaxTreeNode node);

        SyntaxTreeNode _rootNode = null;

        public SyntaxTree(SyntaxTreeNode rootNode)
        {
            this._rootNode = rootNode;
        }

        public string DumpTreeTable()
        {
            StringBuilder b = new StringBuilder();
            b.AppendLine();
            b.AppendLine("SyntaxTree Dump");
            b.AppendLine("---------------");
            this._rootNode.AsString(b, 0);
            return b.ToString();
        }

        public bool CheckForSemanticErrors(SymbolTable symbolTable)
        {
            Logger.Debug("Semantic check...");

            if (!this._rootNode.CheckForIntegrity() ||
                !CheckForErrorNodes() ||
                !CheckForUndeclaredIdentifiers(symbolTable) ||
                !CheckForDuplicateDeclarations(symbolTable) ||
                !this._rootNode.CheckForTypeMismatch())
            {
                Logger.Error("...failed!");
                return false;
            }
            return true;
        }

        public bool AllocateMemoryForConstants(CodeGeneratorHelper storage)
        {
            Logger.Debug("Constant definition...");

            return this._rootNode.RunDelegateForType(typeof(SyntaxTreeConstNode), delegate(SyntaxTreeNode node)
            {
                SyntaxTreeConstNode constNode = node as SyntaxTreeConstNode;
                constNode.SetMemoryAddress(storage);
                return true;
            });
        }

        public bool AllocateMemoryForVariables(CodeGeneratorHelper storage)
        {
            Logger.Debug("Variable storage allocation...");

            return this._rootNode.RunDelegateForType(typeof(SyntaxTreeDeclarationNode), delegate(SyntaxTreeNode node)
            {
                SyntaxTreeDeclarationNode declNode = node as SyntaxTreeDeclarationNode;
                declNode.SetMemoryAddress(storage);
                return true;
            });
        }

        public void GenerateCode(FileManager fileManager, SymbolTable symbolTable, CodeGeneratorHelper cdh)
        {
            Logger.Debug("Code generation...");
            this._rootNode.GenerateCode(fileManager, symbolTable, cdh);
        }

        bool CheckForUndeclaredIdentifiers(SymbolTable symbolTable)
        {
            return this._rootNode.RunDelegateForType(typeof(SyntaxTreeIdentNode), delegate(SyntaxTreeNode node)
            {
                SyntaxTreeIdentNode identNode = node as SyntaxTreeIdentNode;
                Symbol symbol = identNode.Identifier;
                if (symbolTable.GetDeclarationNodeLinkToSymbol(symbol) == null)
                {
                    string message = identNode.ContextErrorString("Identifier {0} was not declared.", symbolTable.GetRepresentationOfSymbol(symbol));
                    Logger.Error(message);
                    return false;
                }
                identNode.DeclarationNode = symbolTable.GetDeclarationNodeLinkToSymbol(symbol);
                return true;
            });
        }

        bool CheckForDuplicateDeclarations(SymbolTable symbolTable)
        {
            return this._rootNode.RunDelegateForType(typeof(SyntaxTreeDeclarationNode), delegate(SyntaxTreeNode node)
            {
                SyntaxTreeDeclarationNode declNode = node as SyntaxTreeDeclarationNode;
                Symbol symbol = declNode.Identifier;
                if (!symbolTable.IsIdentifierSymbol(symbol))
                {
                    string message = declNode.ContextErrorString("Declaration node contains not an identifier symbol.");
                    Logger.Error(message);
                    return false;
                }
                SyntaxTreeNode nodeFromSymbol = symbolTable.GetDeclarationNodeLinkToSymbol(symbol);
                if (nodeFromSymbol != declNode)
                {
                    string message = declNode.ContextErrorString("Duplicate declaration of an identifier {0}.", symbolTable.GetRepresentationOfSymbol(symbol));
                    Logger.Error(message);
                    return false;
                }
                return true;
            });
        }

        bool CheckForErrorNodes()
        {
            return this._rootNode.RunDelegateForType(typeof(SyntaxTreeErrorNode), delegate(SyntaxTreeNode node)
            {
                Logger.Error(node.ContextErrorString("Program contains at least one syntactic error."));
                return false;
            });
        }
    }
}
