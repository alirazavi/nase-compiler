using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Nase.Files;

namespace Nase.Syntax
{
    public abstract class SyntaxTreeNode
    {
        protected FilePosition _position;
        protected List<SyntaxTreeNode> _children;

        public abstract bool CheckForIntegrity();

        public SyntaxTreeNode(FilePosition position)
        {
            this._children = new List<SyntaxTreeNode>();
            this._position = position;
        }

        public virtual void GenerateCode(FileManager fileManager, SymbolTable symbolTable, CodeGeneratorHelper labelHelper)
        {
            foreach (var node in this._children)
            {
                if (node != null)
                    node.GenerateCode(fileManager, symbolTable, labelHelper);
            }
        }

        public virtual bool CheckForTypeMismatch()
        {
            foreach (var node in this._children)
            {
                if (node != null && !node.CheckForTypeMismatch())
                    return false;
            }
            return true;
        }

        public virtual void AsString(StringBuilder b, int level)
        {
            for(int i = 0; i < level; ++i)
            {
                b.Append(" . ");
            }
            b.Append(this._position.StartLine);
            b.Append(":");
            b.Append(this._position.StartColumn);
            b.Append(" ");
            b.Append(this.GetType().Name);
            b.Append("\n");

            foreach (var node in this._children)
            {
                if(node != null)
                    node.AsString(b,level + 1);
            }
        }

        public string ContextErrorString(string message, params object[] args)
        {
            string fullMessage = string.Format(message, args);
            return string.Format("CONTEXT ERROR near {0}: {1}", PositionAsString(), fullMessage);
        }

        internal bool RunDelegateForType(System.Type type, SyntaxTree.TreeNodeDelegate delegateToRun)
        {
            bool result = true;
            if (type.IsInstanceOfType(this))
            {
                result = delegateToRun(this);
            }
            foreach (var node in this._children)
            {
                if (node != null)
                    result &= node.RunDelegateForType(type, delegateToRun);
            }
            return result;
        }

        protected void AppendNodeComment(FileManager fileManager)
        {
            fileManager.Output.AppendLine("% POSITION {0}/{1}: begin coding of {2}", this._position.StartLine, this._position.StartColumn, this.GetType().Name);
        }

        protected string PositionAsString()
        {
            return String.Format("line {0}, column {1}", this._position.StartLine, this._position.StartColumn);
        }
    }
}
