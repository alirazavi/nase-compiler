using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Nase.Syntax;

namespace Nase
{
    class Parser
    {
        static readonly Logger Logger = LogManager.CreateLogger();

        FileManager _fileManager;
        Scanner _scanner;

        public Parser(FileManager fileManager, Scanner scanner)
        {
            this._fileManager = fileManager;
            this._scanner = scanner;
        }

        public bool ParseProgramm(out SyntaxTree syntaxTree)
        {
            syntaxTree = null;

            SyntaxTreeNode rootNode = IsProgramm();
            syntaxTree = new SyntaxTree(rootNode);

            Logger.Debug(this._scanner.SymbolTable.DumpSymbolTable());
            Logger.Debug(syntaxTree.DumpTreeTable());

            return rootNode != null;
        }

        FilePosition GetInputFilePosition()
        {
            return this._fileManager.Input.Position;
        }

        /*
         * program = statementSequence EOF_SYMBOL.
         */
        SyntaxTreeNode IsProgramm()
        {
            SyntaxTreeNode statementSequenceNode;

            FilePosition position = GetInputFilePosition();
            if (null != (statementSequenceNode = IsStatementSequence()))
            {
                if (IsEofSymbol())
                {
                    return new SyntaxTreeProgramNode(position, statementSequenceNode);
                }
            }
            return null;
        }

        /*
         * statementSequence = statement { statement }.
         */
        SyntaxTreeNode IsStatementSequence()
        {
            SyntaxTreeNode statementSequenceNode = null;
            SyntaxTreeNode statementNode;

            FilePosition position = GetInputFilePosition();
            if (null != (statementNode = IsStatement()))
            {
                statementSequenceNode = new SyntaxTreeSequenceNode(position, statementNode, statementSequenceNode);
            }

            SyntaxTreeNode firstStatementNode = statementSequenceNode;

            while (null != (statementNode = IsStatement()))
            {
                position = GetInputFilePosition();
                statementSequenceNode = new SyntaxTreeSequenceNode(position, statementNode, statementSequenceNode);
            }
            return firstStatementNode;
        }

        /*
         * statement = [ declaration | assignment | read | write ] DELIMITER_SYMBOL.
         */
        SyntaxTreeNode IsStatement()
        {
            SyntaxTreeNode statementNode = null;

            if (null != (statementNode = IsDeclaration()) ||
                null != (statementNode = IsAssignment()) ||
                null != (statementNode = IsRead()) ||
                null != (statementNode = IsWrite()))
            {
                if (this._scanner.PeekSymbol() != Symbol.DELIMITER_SYMBOL)
                {
                    return CreateErrorNode("\';\' expected");
                }
                this._scanner.NextSymbol();
                return statementNode;
            }
            else if (IsEofSymbol())
            {
                return null;
            }

            return CreateErrorNode("Statement expected");
            throw new Exception("The method or operation is not implemented.");
        }

        /*
         * declaration = typeName identifier { COMMA_SYMBOL identifier }.
         */
        SyntaxTreeNode IsDeclaration()
        {
            bool commaOccurred = false;
            SyntaxTreeNode firstDeclSequeceNode = null;
            SyntaxTreeNode declSequenceNode = null;
            SyntaxTreeNode declNode = null;
            SyntaxTreeNode typeNode;

            FilePosition position = GetInputFilePosition();
            if (null != (typeNode = IsTypeName()))
            {
                do
                {
                    Symbol currentSymbol = this._scanner.PeekSymbol();
                    if (this._scanner.SymbolTable.IsIdentifierSymbol(currentSymbol))
                    {
                        declNode = new SyntaxTreeDeclarationNode(position, currentSymbol);
                        this._scanner.NextSymbol();
                        if (this._scanner.PeekSymbol() == Symbol.EOF_SYMBOL)
                        {
                            return CreateErrorNode("Unexpected EOF", false);
                        }
                    }
                    else
                    {
                        return CreateErrorNode("Identifier expected after type name");
                    }

                    if (this._scanner.PeekSymbol() == Symbol.COMMA_SYMBOL)
                    {
                        position = GetInputFilePosition();
                        declSequenceNode = new SyntaxTreeSequenceNode(position, declNode, declSequenceNode);
                        if (!commaOccurred)
                        {
                            commaOccurred = true;
                            firstDeclSequeceNode = declSequenceNode;
                        }
                        this._scanner.NextSymbol();
                        if (this._scanner.PeekSymbol() == Symbol.EOF_SYMBOL)
                        {
                            return CreateErrorNode("Unexpected EOF", false);
                        }
                    }
                    else if (this._scanner.PeekSymbol() == Symbol.DELIMITER_SYMBOL)
                    {
                        position = GetInputFilePosition();
                        declSequenceNode = new SyntaxTreeSequenceNode(position, declNode, declSequenceNode);
                        if (!commaOccurred)
                        {
                            firstDeclSequeceNode = declSequenceNode;
                        }
                    }
                    else
                    {
                        return CreateErrorNode("\',\' or \';\' expected after an identifier in a declaration");
                    }
                } while (Symbol.DELIMITER_SYMBOL != this._scanner.PeekSymbol());
            }

            return firstDeclSequeceNode;
        }

        /*
         * assignment = identifier ASSIGN_SYMBOL intExpr.
         */
        SyntaxTreeNode IsAssignment()
        {
            SyntaxTreeNode identNode = null;
            SyntaxTreeNode intExprNode = null;

            if(null != (identNode = IsIdentifier()))
            {
                FilePosition position = GetInputFilePosition();
                if (this._scanner.PeekSymbol() == Symbol.ASSIGN_SYMBOL)
                {
                    this._scanner.NextSymbol();
                    if (null != (intExprNode = IsIntExpr()))
                    {
                        return new SyntaxTreeAssignmentNode(position, identNode, intExprNode);
                    }
                    else
                    {
                        return CreateErrorNode("IntExpr expected on right hand side of \':=\'");
                    }
                }
                else
                {
                    return CreateErrorNode("\':=\' expected");
                }
            }
            return null;
        }

        /*
         * identifier = ANY_LETTER { ANY_DIGIT | ANY_LETTER }.
         */
        SyntaxTreeNode IsIdentifier()
        {
            FilePosition position = GetInputFilePosition();
            Symbol currentSymbol = this._scanner.PeekSymbol();
            if (this._scanner.SymbolTable.IsIdentifierSymbol(currentSymbol))
            {
                this._scanner.NextSymbol();
                return new SyntaxTreeIdentNode(position, currentSymbol);
            }
            return null;
        }

        /*
         * read = READ_SYMBOL identifier.
         */
        SyntaxTreeNode IsRead()
        {
            SyntaxTreeNode identNode = null;

            FilePosition position = GetInputFilePosition();
            if (this._scanner.PeekSymbol() == Symbol.READ_SYMBOL)
            {
                this._scanner.NextSymbol();
                if (null != (identNode = IsIdentifier()))
                {
                    return new SyntaxTreeReadNode(position, identNode);
                }
                else
                {
                    return CreateErrorNode("Identifier expected after READ");
                }
            }
            return null;
        }

        /*
         * write = WRITE_SYMBOL identifier.
         */
        SyntaxTreeNode IsWrite()
        {
            SyntaxTreeNode identNode = null;
            FilePosition position = GetInputFilePosition();
            if (this._scanner.PeekSymbol() == Symbol.WRITE_SYMBOL)
            {
                this._scanner.NextSymbol();
                if (null != (identNode = IsIdentifier()))
                {
                    return new SyntaxTreeWriteNode(position, identNode);
                }
                else
                {
                    return CreateErrorNode("Identifier expected after WRITE");
                }
            }
            return null;
        }

        /*
         * intExpr = [ MINUS_SYMBOL ] term { addOp term }.
         */
        SyntaxTreeNode IsIntExpr()
        {
            bool minusFlag;
            SyntaxTreeNode leftTermNode = null;
            SyntaxTreeNode rightTreeNode = null;
            FilePosition position = GetInputFilePosition();

            Symbol currentSymbol = this._scanner.PeekSymbol();
            if (currentSymbol == Symbol.MINUS_SYMBOL)
            {
                this._scanner.NextSymbol();
                minusFlag = true;
            }
            else
            {
                minusFlag = false;
            }

            if (null == (leftTermNode = IsIntTerm()))
            {
                if (!minusFlag)
                    return null;
                else
                {
                    return CreateErrorNode("intTerm expected after \'-\'");
                }
            }

            if (minusFlag)
            {
                leftTermNode = new SyntaxTreeMonadicOpNode(position, Symbol.MINUS_SYMBOL, leftTermNode);
            }

            currentSymbol = this._scanner.PeekSymbol();
            while (currentSymbol == Symbol.MINUS_SYMBOL || currentSymbol == Symbol.PLUS_SYMBOL)
            {
                position = GetInputFilePosition();
                this._scanner.NextSymbol();
                if (null == (rightTreeNode = IsIntTerm()))
                {
                    return CreateErrorNode("intTerm expected after \'addOp\'");
                }
                else
                {
                    leftTermNode = new SyntaxTreeDyadicOpNode(position, currentSymbol, leftTermNode, rightTreeNode);
                    currentSymbol = this._scanner.PeekSymbol();
                }
            }

            return leftTermNode;
        }

        /*
         * intTerm = isIntTerm { multOp intFactor }.
         */
        SyntaxTreeNode IsIntTerm()
        {
            SyntaxTreeNode leftFactorNode = null;
            SyntaxTreeNode rightFactorNode = null;

            if (null == (leftFactorNode = IsIntFactor()))
                return null;

            Symbol currentSymbol = this._scanner.PeekSymbol();
            while (currentSymbol == Symbol.TIMES_SYMBOL ||
                currentSymbol == Symbol.DIVIDE_SYMBOL ||
                currentSymbol == Symbol.MODULO_SYMBOL)
            {
                FilePosition position = GetInputFilePosition();
                this._scanner.NextSymbol();
                if (null == (rightFactorNode = IsIntFactor()))
                {
                    return CreateErrorNode("intFactor expected after \'multOp\'");
                }
                else
                {
                    leftFactorNode = new SyntaxTreeDyadicOpNode(position, currentSymbol, leftFactorNode, rightFactorNode);
                    currentSymbol = this._scanner.PeekSymbol();
                }
            }
            return leftFactorNode;
        }

        /*
         * intFactor = integer | identifier | OPEN_PARAENTHESIS_SYMBOL intExpr CLOSE_PARENTHESIS_SYMBOL | inlineIfStatement.
         */
        SyntaxTreeNode IsIntFactor()
        {
            SyntaxTreeNode intFactorNode = null;
            SyntaxTreeNode intExprNode = null;
            FilePosition position = GetInputFilePosition();
            if (null != (intFactorNode = IsInteger()))
            {
                return intFactorNode;
            }
            else if(null != (intFactorNode = IsIdentifier()))
            {
                return intFactorNode;
            }
            else if (this._scanner.PeekSymbol() == Symbol.OPEN_PARAENTHESIS_SYMBOL)
            {
                this._scanner.NextSymbol();
                Symbol currentSymbol = this._scanner.PeekSymbol();
                if (null != (intExprNode = IsIntExpr()))
                {
                    currentSymbol = this._scanner.PeekSymbol();
                    if (currentSymbol == Symbol.CLOSE_PARENTHESIS_SYMBOL)
                    {
                        this._scanner.NextSymbol();
                        return new SyntaxTreeNodeParenthesisNode(position, intExprNode);
                    }
                    else
                    {
                        return CreateErrorNode("\')\' expected");
                    }
                }
                else
                {
                    return CreateErrorNode("intExpr expected after \'(\'");
                }
            }
            else if (null != (intFactorNode = IsInlineIf()))
            {
                return intFactorNode;
            }

            return CreateErrorNode("intFactor expected");
        }

        /*
         * integer = ANY_DIGIT { ANY_DIGIT }.
         */
        SyntaxTreeNode IsInteger()
        {
            SyntaxTreeNode constNode = null;
            FilePosition position = GetInputFilePosition();
            Symbol currentSymbol = this._scanner.PeekSymbol();
            if (this._scanner.SymbolTable.IsNumberSymbol(currentSymbol))
            {
                this._scanner.NextSymbol();
                constNode = this._scanner.SymbolTable.GetDeclarationNodeLinkToSymbol(currentSymbol);
                if (null == constNode)
                {
                    constNode = new SyntaxTreeConstNode(position, currentSymbol);
                    this._scanner.SymbolTable.SetDeclarationNodeLinkToSymbol(currentSymbol, constNode);
                }
                return constNode;
            }
            return null;
        }

        /*
         * inlineIfStatement = INLINE_IF_SYMBOL boolExpr INLINE_THEN_SYMBOL intExpr INLINE_ELSE_SYMBOL intExpr INLINE_FI_SYMBOL.
         */
        SyntaxTreeNode IsInlineIf()
        {
            SyntaxTreeNode boolExprNode = null;
            SyntaxTreeNode thenIntExprNode = null;
            SyntaxTreeNode elseIntExprNode = null;
            FilePosition position = GetInputFilePosition();

            if (this._scanner.PeekSymbol() != Symbol.INLINE_IF_SYMBOL)
            {
                return null;
            }

            this._scanner.NextSymbol();

            if (null == (boolExprNode = IsBoolExpr()))
            {
                return CreateErrorNode("boolExpr expected");
            }

            if (this._scanner.PeekSymbol() != Symbol.INLINE_THEN_SYMBOL)
            {
                return CreateErrorNode("\'?\' expected");
            }

            this._scanner.NextSymbol();

            if (null == (thenIntExprNode = IsIntExpr()))
            {
                return CreateErrorNode("intExpr expected");
            }

            if (this._scanner.PeekSymbol() != Symbol.INLINE_ELSE_SYMBOL)
            {
                return CreateErrorNode("\':\' expected");
            }

            this._scanner.NextSymbol();

            if (null == (elseIntExprNode = IsIntExpr()))
            {
                return CreateErrorNode("intExpr expected");
            }

            if (this._scanner.PeekSymbol() == Symbol.INLINE_FI_SYMBOL)
            {
                this._scanner.NextSymbol();
                return new SyntaxTreeInlineIfNode(position, boolExprNode, thenIntExprNode, elseIntExprNode);
            }

            return CreateErrorNode("\'FII\' expected");
        }

        /*
         * boolExpr = intExpr relationOp intExpr { boolOp intExpr relationOp intExpr }.
         */
        SyntaxTreeNode IsBoolExpr()
        {
            SyntaxTreeNode leftIntExprNode = null;
            SyntaxTreeNode rightIntExprNode = null;
            SyntaxTreeNode newBoolExprNode1 = null;

            if (null == (leftIntExprNode = IsIntExpr()))
            {
                return null;
            }

            Symbol currentSymbol = this._scanner.PeekSymbol();
            FilePosition position = GetInputFilePosition();
            if (!IsRelationOpSymbol(currentSymbol))
            {
                return CreateErrorNode("\'relationOp\' expected after \'intExpr\'");
            }

            this._scanner.NextSymbol();
            if (null == (rightIntExprNode = IsIntExpr()))
            {
                return CreateErrorNode("\'intExpr\' expected after \'relationOp\'");
            }

            newBoolExprNode1 = new SyntaxTreeDyadicOpNode(position, currentSymbol, leftIntExprNode, rightIntExprNode);

            Symbol currentSymbolForBoolOp = this._scanner.PeekSymbol();
            while (currentSymbolForBoolOp == Symbol.AND_SYMBOL || currentSymbolForBoolOp == Symbol.OR_SYMBOL)
            {
                position = GetInputFilePosition();
                this._scanner.NextSymbol();

                if (null == (leftIntExprNode = IsIntExpr()))
                {
                    return CreateErrorNode("\'intExpr\' expected after \'boolOp\'");
                }
                currentSymbol = this._scanner.PeekSymbol();
                if (!IsRelationOpSymbol(currentSymbol))
                {
                    return CreateErrorNode("\'relationOp\' expected after \'intExpr\'");
                }
                this._scanner.NextSymbol();
                if (null == (rightIntExprNode = IsIntExpr()))
                {
                    return CreateErrorNode("\'intExpr\' expected after \'relationOp\'");
                }
                SyntaxTreeNode newBoolExprNode2 = new SyntaxTreeDyadicOpNode(position, currentSymbol, leftIntExprNode, rightIntExprNode);
                newBoolExprNode1 = new SyntaxTreeDyadicOpNode(position, currentSymbolForBoolOp, newBoolExprNode1, newBoolExprNode2);
                currentSymbolForBoolOp = this._scanner.PeekSymbol();
            }
            return newBoolExprNode1;
        }

        /*
         * typeName = INT_TYPE_SYMBOL.
         */
        SyntaxTreeNode IsTypeName()
        {
            SyntaxTreeNode typeNode = null;
            FilePosition position = GetInputFilePosition();

            if (Symbol.INT_TYPE_SYMBOL == this._scanner.PeekSymbol())
            {
                typeNode =  new SyntaxTreeTypeNode(position, Symbol.INT_TYPE_SYMBOL);
                this._scanner.NextSymbol();
            }
            return typeNode;
        }

        bool IsEofSymbol()
        {
            return Symbol.EOF_SYMBOL == this._scanner.PeekSymbol();
        }

        bool IsRelationOpSymbol(Symbol symbol)
        {
            return
                symbol == Symbol.LT_SYMBOL ||
                symbol == Symbol.LE_SYMBOL ||
                symbol == Symbol.EQ_SYMBOL ||
                symbol == Symbol.GE_SYMBOL ||
                symbol == Symbol.GT_SYMBOL ||
                symbol == Symbol.NE_SYMBOL;
        }


        void SyntaxError(string message)
        {
            FilePosition position = GetInputFilePosition(); ;
            Logger.Error("Syntax Error near line " + position.Line + ", column " + position.Column + ": " + message);
        }

        SyntaxTreeNode CreateErrorNode(string message, bool skipToDelimiter = true)
        {
            SyntaxError(message);
            FilePosition position = GetInputFilePosition();
            if(skipToDelimiter)
                this._scanner.SkipToDelimiter();
            return new SyntaxTreeErrorNode(position);
        }
    }
}
