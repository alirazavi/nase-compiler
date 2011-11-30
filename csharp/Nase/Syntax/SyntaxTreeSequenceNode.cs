using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Nase.Files;

namespace Nase.Syntax
{
    class SyntaxTreeSequenceNode : SyntaxTreeNode
    {
        static readonly Logger Logger = LogManager.CreateLogger();

        public SyntaxTreeSequenceNode(FilePosition position, SyntaxTreeNode statementNode, SyntaxTreeNode prevSequenceNode)
            : base(position)
        {
            this._children.Add(statementNode);
            this._children.Add(prevSequenceNode);
            /*
            if (null != prevSequenceNode && prevSequenceNode is SyntaxTreeSequenceNode)
            {
                SyntaxTreeSequenceNode curNode = (SyntaxTreeSequenceNode)prevSequenceNode;
                while (curNode._children[1] != null)
                {
                    curNode = (SyntaxTreeSequenceNode)curNode._children[1];
                }
                curNode._children[1] = this;
            }*/
        }

        public void AppendToLast()
        {
            if (null != this._children[1] && this._children[1] is SyntaxTreeSequenceNode)
            {
                SyntaxTreeSequenceNode curNode = (SyntaxTreeSequenceNode)this._children[1];
                while (curNode._children[1] != null)
                {
                    curNode = (SyntaxTreeSequenceNode)curNode._children[1];
                }
                curNode._children[1] = this;
            }
            this._children[1] = null;
        }

        public override bool CheckForIntegrity()
        {
            if (this._children.Count != 2)
            {
                Logger.Debug("Statement node and/or follower node are missing.");
                return false;
            }
            if (this._children[0] == null)
            {
                Logger.Debug("Statement node must not be null");
                return false;
            }

            bool result = this._children[0].CheckForIntegrity();
            if(this._children[1] != null)
                result &= this._children[1].CheckForIntegrity();
            return result;
        }
    }
}
