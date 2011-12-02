using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Nase.Syntax;
using Nase.GeneratedParser;
using Nase.Files;

namespace Nase
{
    public enum SymbolType
    {
        ReservedWordSymbolType,
        IdentifierSymbolType,
        NumberSymbolType,
        BooleanSymbolType
    }

    /*
    public enum Symbol
    {
        NULL_SYMBOL = 0,
        EOF,                         // 1
        DELIMITER_SYMBOL,            // 2
        BEGIN_SYMBOL,                // 3
        END_SYMBOL,                  // 4
        INT_TYPE_SYMBOL,             // 5
        COMMA_SYMBOL,                // 6
        ASSIGN_SYMBOL,               // 7
        MINUS_SYMBOL,                // 8
        PLUS_SYMBOL,                 // 9
        TIMES_SYMBOL,                // 10
        DIVIDE_SYMBOL,               // 11
        MODULO_SYMBOL,               // 12
        OR_SYMBOL,                   // 13
        AND_SYMBOL,                  // 14
        OPEN_PARAENTHESIS_SYMBOL,    // 15
        CLOSE_PARENTHESIS_SYMBOL,    // 16
        INLINE_IF_SYMBOL,            // 17
        INLINE_FI_SYMBOL,            // 18
        INLINE_THEN_SYMBOL,          // 19
        INLINE_ELSE_SYMBOL,          // 20
        LT_SYMBOL,                   // 21
        LE_SYMBOL,                   // 22
        EQ_SYMBOL,                   // 23
        GE_SYMBOL,                   // 24
        GT_SYMBOL,                   // 25
        NE_SYMBOL,                   // 26
        READ_SYMBOL,                 // 27
        WRITE_SYMBOL,                // 28
    }
     */

    public class SymbolTable
    {
        class SymbolTableEntry
        {
            public Symbol symbol;
            public string sRepresentation;
            public bool bIsBreakingCharSeq;
            public int iYielding;
            public bool bIsFixEntry;
            public SymbolType type;
            public SyntaxTreeNode nodeLink;
        }

        static readonly Logger Logger = LogManager.CreateLogger();

        Stack<List<SymbolTableEntry>> _symbolTableStack;
        List<SymbolTableEntry> _processedSymbols;
        int _nextSymbol;

        public SymbolTable()
        {
            this._nextSymbol = 0;

            this._processedSymbols = new List<SymbolTableEntry>();
            this._symbolTableStack = new Stack<List<SymbolTableEntry>>();
            this._symbolTableStack.Push(new List<SymbolTableEntry>(200));

            AddFixSymbol("", false);
            this._nextSymbol++;
            AddFixSymbol(InputFile.EOF_CHAR.ToString(), true);
            this._nextSymbol++;
            AddFixSymbol("BEGIN", false);
            AddFixSymbol("END", false);
            AddFixSymbol(";", true);
            AddFixSymbol(",", true);
            AddFixSymbol("INTEGER", false);
            AddFixSymbol("BOOLEAN", false);
            AddFixSymbol("READ", false);
            AddFixSymbol("WRITE", false);
            AddFixSymbol("IF", false);
            AddFixSymbol("THEN", false);
            AddFixSymbol("ELSE", false);
            AddFixSymbol("WHILE", false);
            AddFixSymbol("FOR", false);
            AddFixSymbol("DO", false);
            AddFixSymbol(":=", true);
            AddFixSymbol("(", true);
            AddFixSymbol(")", true);
            AddFixSymbol("IIF", false);
            AddFixSymbol("FII", false);
            AddFixSymbol("?", true);
            AddFixSymbol(":", true);
            AddFixSymbol("+", true);
            AddFixSymbol("-", true);
            AddFixSymbol("*", true);
            AddFixSymbol("/", true);
            AddFixSymbol("%", true);
            AddFixSymbol("<", true);
            AddFixSymbol("<=", true);
            AddFixSymbol("=", true);
            AddFixSymbol(">=", true);
            AddFixSymbol(">", true);
            AddFixSymbol("<>", true);
            AddFixSymbol("AND", false);
            AddFixSymbol("OR", false);
            AddFixSymbol("NOT", false);

            var baseSymbolTable = this._symbolTableStack.Peek();
            AddFixSymbol("TRUE", false);
            baseSymbolTable.Last().type = SymbolType.BooleanSymbolType;
            baseSymbolTable.Last().iYielding = -1;

            AddFixSymbol("FALSE", false);
            baseSymbolTable.Last().type = SymbolType.BooleanSymbolType;
            baseSymbolTable.Last().iYielding = 0;

            this._nextSymbol += 2;
        }

        public string GetSpecialCharList()
        {
            var list =
                from entry in this._symbolTableStack.Last()
                where entry.bIsFixEntry && entry.bIsBreakingCharSeq
                select entry.sRepresentation;

            StringBuilder result = new StringBuilder();

            foreach (string s in list)
            {
                result.Append(s);
                result.Append(" ");
            }
            return result.ToString();
        }

        public Symbol ClassifySymbol(string token)
        {
            foreach (var table in this._symbolTableStack)
            {
                foreach (var entry in table)
                {
                    if (token == entry.sRepresentation)
                    {
                        return entry.symbol;
                    }
                }
            }
            return AddUserSymbol(token);
        }

        public Symbol AddUserSymbol(string token)
        {
            foreach (var e in this._symbolTableStack.Peek())
            {
                if (token == e.sRepresentation)
                {
                    return e.symbol;
                }
            }

            SymbolTableEntry entry = new SymbolTableEntry();
            entry.symbol = (Symbol)this._nextSymbol++;
            entry.bIsFixEntry = false;
            entry.bIsBreakingCharSeq = false;
            entry.nodeLink = null;
            entry.sRepresentation = token;

            int number;
            if (int.TryParse(token, out number))
            {
                entry.type = SymbolType.NumberSymbolType;
                entry.iYielding = number;
            }
            else
            {
                entry.type = SymbolType.IdentifierSymbolType;
            }

            return AddSymbol(entry);
        }

        public void AddNestingLevel()
        {
            this._symbolTableStack.Push(new List<SymbolTableEntry>());
        }

        public void RemoveNestingLevel()
        {
            this._processedSymbols.AddRange(this._symbolTableStack.Pop());
        }

        public int GetValueOfNumericSymbol(Symbol symbol)
        {
            if (!IsNumberSymbol(symbol) && !IsBooleanSymbol(symbol))
            {
                Logger.Warn("Trying to get a numeric value from not a numeric or boolean symbol");
            }
            foreach (var entry in this._processedSymbols.Union(this._symbolTableStack.Peek()))
                if (entry.symbol == symbol)
                    return entry.iYielding;
            throw new Exception("Symbol not found!");
        }

        public bool IsIdentifierSymbol(Symbol symbol)
        {
            foreach (var entry in this._processedSymbols.Union(this._symbolTableStack.Peek()))
                if (entry.symbol == symbol)
                    return entry.type == SymbolType.IdentifierSymbolType;
            throw new Exception("Symbol not found!");
        }

        public bool IsNumberSymbol(Symbol symbol)
        {
            foreach (var entry in this._processedSymbols.Union(this._symbolTableStack.Peek()))
                if (entry.symbol == symbol)
                    return entry.type == SymbolType.NumberSymbolType;
            throw new Exception("Symbol not found!");
        }

        public bool IsBooleanSymbol(Symbol symbol)
        {
            foreach (var entry in this._processedSymbols.Union(this._symbolTableStack.Peek()))
                if (entry.symbol == symbol)
                    return entry.type == SymbolType.BooleanSymbolType;
            throw new Exception("Symbol not found!");
        }

        public string GetRepresentationOfSymbol(Symbol symbol)
        {
            foreach (var entry in this._processedSymbols.Union(this._symbolTableStack.Peek()))
                if (entry.symbol == symbol)
                    return entry.sRepresentation;
            throw new Exception("Symbol not found!");
        }

        public SyntaxTreeNode GetDeclarationNodeLinkToSymbol(Symbol symbol)
        {
            foreach (var entry in this._processedSymbols.Union(this._symbolTableStack.Peek()))
                if (entry.symbol == symbol)
                    return entry.nodeLink;
            throw new Exception("Symbol not found: " + symbol);
        }

        public void SetDeclarationNodeLinkToSymbol(Symbol symbol, SyntaxTreeNode node)
        {
            foreach (var table in this._symbolTableStack)
                foreach (var entry in table)
                    if (entry.symbol == symbol)
                    {
                        if (entry.nodeLink == null)
                        {
                            entry.nodeLink = node;
                        }
                        return;
                    }
            throw new Exception("Symbol not found: " + symbol);
        }

        public string DumpSymbolTable()
        {
            StringBuilder buffer = new StringBuilder();
            buffer.AppendLine();
            buffer.AppendLine("SybolTable Dump");
            buffer.AppendLine("---------------");

            foreach(var table in this._symbolTableStack)
            {
                foreach (var entry in table)
                {
                    DumpSymbolEntry(buffer, entry);
                }
            }

            foreach (var entry in this._processedSymbols.OrderBy(a => (int)a.symbol))
            {
                DumpSymbolEntry(buffer, entry);
            }

            return buffer.ToString();
        }

        void DumpSymbolEntry(StringBuilder buffer, SymbolTableEntry entry)
        {
            buffer.AppendFormat("{0,-30}", entry.symbol);
            buffer.AppendFormat("{0,-8}", (entry.bIsFixEntry ? "Fixed" : "User"));
            buffer.AppendFormat("{0,-12}", (entry.bIsBreakingCharSeq ? "IsBreakSeq" : ""));

            switch (entry.type)
            {
                case SymbolType.ReservedWordSymbolType:
                    buffer.AppendFormat("{0,-12}", "ResWord");
                    break;
                case SymbolType.IdentifierSymbolType:
                    buffer.AppendFormat("{0,-12}", "Identifier");
                    break;
                case SymbolType.NumberSymbolType:
                    buffer.AppendFormat("{0,-12}", "Number");
                    break;
                case SymbolType.BooleanSymbolType:
                    buffer.AppendFormat("{0,-12}", "Boolean");
                    break;
            }

            buffer.Append("NodeLink: ");
            if (entry.nodeLink != null)
            {
                buffer.AppendFormat("{0, -25}", entry.nodeLink.GetType().Name);
            }
            else
            {
                buffer.AppendFormat("{0, -25}", "null");
            }
            buffer.AppendFormat(" -- {0}", entry.sRepresentation);
            buffer.AppendLine();
        }

        void AddFixSymbol(string representation, bool isBreakingCharSeq)
        {
            AddSymbol(new SymbolTableEntry()
            {
                symbol = (Symbol)this._nextSymbol,
                sRepresentation = representation,
                bIsBreakingCharSeq = isBreakingCharSeq,
                iYielding = 0,
                bIsFixEntry = true,
                type = SymbolType.ReservedWordSymbolType,
                nodeLink = null,
            });

            this._nextSymbol++;
        }

        Symbol AddSymbol(SymbolTableEntry entry)
        {
            var currentSymbolTable = this._symbolTableStack.Peek();

            currentSymbolTable.Add(entry);
            return entry.symbol;
        }
    }
}
