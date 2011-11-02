using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Nase.Syntax;

namespace Nase
{
    public enum SymbolType
    {
        ReservedWordSymbolType,
        IdentifierSymbolType,
        NumberSymbolType
    }

    public enum Symbol
    {
        NULL_SYMBOL = 0,
        EOF_SYMBOL,                  // 1
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

    class SymbolTable
    {
        struct SymbolTableEntry
        {
            public string sRepresentation;
            public bool bIsBreakingCharSeq;
            public int iYielding;
            public bool bIsFixEntry;
            public SymbolType type;
            public SyntaxTreeNode nodeLink;
        }

        static readonly Logger Logger = LogManager.CreateLogger();

        SymbolTableEntry[] _symbolTable;
        int _usedEntries;

        public SymbolTable()
        {
            this._symbolTable = new SymbolTableEntry[200];
            this._usedEntries = 0;

            AddFixSymbol("", false);
            AddFixSymbol(InputFile.EOF_CHAR.ToString(), true);
            AddFixSymbol(";", true);
            AddFixSymbol("BEGIN", false);
            AddFixSymbol("END", false);
            AddFixSymbol("INTEGER", false);
            AddFixSymbol(",", true);
            AddFixSymbol(":=", true);
            AddFixSymbol("-", true);
            AddFixSymbol("+", true);
            AddFixSymbol("*", true);
            AddFixSymbol("/", true);
            AddFixSymbol("%", true);
            AddFixSymbol("OR", false);
            AddFixSymbol("AND", false);
            AddFixSymbol("(", true);
            AddFixSymbol(")", true);
            AddFixSymbol("IIF", false);
            AddFixSymbol("FII", false);
            AddFixSymbol("?", true);
            AddFixSymbol(":", true);
            AddFixSymbol("<", true);
            AddFixSymbol("<=", true);
            AddFixSymbol("=", true);
            AddFixSymbol(">=", true);
            AddFixSymbol(">", true);
            AddFixSymbol("<>", true);
            AddFixSymbol("READ", false);
            AddFixSymbol("WRITE", false);
        }

        public string GetSpecialCharList()
        {
            var list =
                from entry in this._symbolTable
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
            for (int i = 0; i < this._usedEntries; i++)
            {
                if (token == this._symbolTable[i].sRepresentation)
                {
                    return (Symbol)i;
                }
            }
            return Symbol.NULL_SYMBOL;
        }

        public Symbol AddUserSymbol(string token)
        {
            SymbolTableEntry entry = new SymbolTableEntry();
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

        public int GetValueOfNumericSymbol(Symbol symbol)
        {
            if ((int)symbol >= this._usedEntries)
            {
                Logger.Fatal("Invalid symbol table index");
                throw new IndexOutOfRangeException();
            }
            if (!IsNumberSymbol(symbol))
            {
                Logger.Warn("Trying to get a numeric value from not a numeric symbol");
            }
            return this._symbolTable[(int)symbol].iYielding;
        }

        public bool IsIdentifierSymbol(Symbol symbol)
        {
            if ((int)symbol >= this._usedEntries)
            {
                Logger.Fatal("Invalid symbol table index");
                throw new IndexOutOfRangeException();
            }
            return this._symbolTable[(int)symbol].type == SymbolType.IdentifierSymbolType;
        }

        public bool IsNumberSymbol(Symbol symbol)
        {
            if ((int)symbol >= this._usedEntries)
            {
                Logger.Fatal("Invalid symbol table index");
                throw new IndexOutOfRangeException();
            }
            return this._symbolTable[(int)symbol].type == SymbolType.NumberSymbolType;
        }

        public string GetRepresentationOfSymbol(Symbol symbol)
        {
            if ((int)symbol >= this._usedEntries)
            {
                Logger.Fatal("Invalid symbol table index");
                throw new IndexOutOfRangeException();
            }
            return this._symbolTable[(int)symbol].sRepresentation;
        }

        public SyntaxTreeNode GetDeclarationNodeLinkToSymbol(Symbol symbol)
        {
            if ((int)symbol >= this._usedEntries)
            {
                Logger.Fatal("Invalid symbol table index");
                throw new IndexOutOfRangeException();
            }
            return this._symbolTable[(int)symbol].nodeLink;
        }

        public void SetDeclarationNodeLinkToSymbol(Symbol symbol, SyntaxTreeNode node)
        {
            if ((int)symbol >= this._usedEntries)
            {
                Logger.Fatal("Invalid symbol table index");
                throw new IndexOutOfRangeException();
            }
            this._symbolTable[(int)symbol].nodeLink = node;
        }

        public string DumpSymbolTable()
        {
            StringBuilder buffer = new StringBuilder();
            buffer.AppendLine();
            buffer.AppendLine("SybolTable Dump");
            buffer.AppendLine("---------------");

            for (int i = 0; i < this._usedEntries; i++)
            {
                buffer.AppendFormat("{0,-4}", i);
                buffer.AppendFormat("{0,-8}", (this._symbolTable[i].bIsFixEntry ? "Fixed" : "User"));
                buffer.AppendFormat("{0,-12}", (this._symbolTable[i].bIsBreakingCharSeq ? "IsBreakSeq" : ""));

                switch(this._symbolTable[i].type) {
                    case SymbolType.ReservedWordSymbolType:
                        buffer.AppendFormat("{0,-12}", "ResWord");
                        break;
                    case SymbolType.IdentifierSymbolType:
                        buffer.AppendFormat("{0,-12}", "Identifier");
                        break;
                    case SymbolType.NumberSymbolType:
                        buffer.AppendFormat("{0,-12}", "Number");
                        break;
                }

                buffer.AppendFormat("NodeLink: {0,-5}", this._symbolTable[i].nodeLink);
                buffer.AppendFormat(" -- {0}", this._symbolTable[i].sRepresentation);
                buffer.AppendLine();
            }

            return buffer.ToString();
        }

        void AddFixSymbol(string representation, bool isBreakingCharSeq)
        {
            AddSymbol(new SymbolTableEntry()
            {
                sRepresentation = representation,
                bIsBreakingCharSeq = isBreakingCharSeq,
                iYielding = 0,
                bIsFixEntry = true,
                type = SymbolType.ReservedWordSymbolType,
                nodeLink = null,
            });
        }

        Symbol AddSymbol(SymbolTableEntry entry)
        {
            if (this._usedEntries == this._symbolTable.Length)
            {
                SymbolTableEntry[] newTable = new SymbolTableEntry[this._symbolTable.Length * 2];
                Array.Copy(this._symbolTable, newTable, this._symbolTable.Length);
                this._symbolTable = newTable;
            }
            this._symbolTable[this._usedEntries] = entry;
            return (Symbol)this._usedEntries++;
        }

    }
}
