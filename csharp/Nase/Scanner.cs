using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Nase
{
    class Scanner
    {
        enum CharClass
        {
            ILLEGAL = 0,
            BLANK,
            UPPER,
            DIGIT,
            LOWER,
            SPECIAL
        }

        static readonly Logger Logger = LogManager.CreateLogger();

        public SymbolTable SymbolTable { get; private set; }

        FileManager _fileManager;
        string _specialChars;
        Symbol _peekSymbol;

        public Scanner(FileManager fileManager)
        {
            this._fileManager = fileManager;
            this.SymbolTable = new SymbolTable();
            this._specialChars = this.SymbolTable.GetSpecialCharList();
            this._peekSymbol = AdvanceSymbol();
        }

        public Symbol NextSymbol()
        {
            Symbol retSymbol = this._peekSymbol;
            this._peekSymbol = AdvanceSymbol();
            return retSymbol;
        }

        public Symbol PeekSymbol()
        {
            return this._peekSymbol;
        }

        public void ScannerTest()
        {
            Symbol curSymbol = NextSymbol();
            while (curSymbol != Symbol.EOF_SYMBOL)
            {
                curSymbol = NextSymbol();
            }
        }

        public void SkipToDelimiter()
        {
            Symbol symbol = NextSymbol();
            if (symbol == Symbol.NULL_SYMBOL ||
                symbol == Symbol.EOF_SYMBOL ||
                symbol == Symbol.DELIMITER_SYMBOL)
            {
                return;
            }

            bool delimiterFound = false;
            while (!delimiterFound)
            {
                string tokenString = GetNextToken();
                symbol = this.SymbolTable.ClassifySymbol(tokenString);
                if (symbol == Symbol.EOF_SYMBOL ||
                    symbol == Symbol.DELIMITER_SYMBOL)
                {
                    delimiterFound = true;
                }
                else
                {
                    DebugOutText("Skipping...");
                    DebugOutTokenSymbol(tokenString, symbol);
                }
            }
        }

        Symbol AdvanceSymbol()
        {
            Symbol symbol = Symbol.NULL_SYMBOL;
            string tokenString = GetNextToken();
            if (tokenString.Length != 0)
            {
                symbol = this.SymbolTable.ClassifySymbol(tokenString);
                if (symbol == Symbol.NULL_SYMBOL)
                {
                    symbol = this.SymbolTable.AddUserSymbol(tokenString);
                }
            }
            DebugOutTokenSymbol(tokenString, symbol);
            return symbol;
        }

        void DebugOutText(string message)
        {
            this._fileManager.Listing.AppendLine(message);
        }

        void DebugOutTokenSymbol(string tokenString, Symbol symbol)
        {
            Logger.Debug("Scanned Symbol: {0} has ID: {1}", tokenString, symbol.ToString());
            this._fileManager.Listing.AppendLine("Scanned Symbol: {0} has ID: {1}", tokenString, symbol.ToString());
        }

        CharClass GetCharClass(char ch)
        {
            if (ch == InputFile.BLANK_CHAR || ch == InputFile.TAB_CHAR)
                return CharClass.BLANK;
            else if (Char.IsUpper(ch))
                return CharClass.UPPER;
            else if (Char.IsDigit(ch))
                return CharClass.DIGIT;
            else if (Char.IsLower(ch))
                return CharClass.LOWER;
            else if (IsSpecialChar(ch))
                return CharClass.SPECIAL;
            else
                return CharClass.ILLEGAL;
        }

        bool IsSpecialChar(char ch)
        {
            return this._specialChars.Contains(ch);
        }

        bool IsSpecialDoublet(char c1, char c2)
        {
            string s = String.Format("{0}{1} ", c1, c2);
            return this._specialChars.Contains(s);
        }

        string GetNextToken()
        {
            StringBuilder tokenString = new StringBuilder();

            bool tokenFound = false;
            while (!tokenFound)
            {
                char curChar = this._fileManager.Input.NextChar();
                if (curChar == InputFile.EOF_CHAR)
                {
                    tokenString.Append(InputFile.EOF_CHAR);
                    tokenFound = true;
                }
                else if (curChar != InputFile.BLANK_CHAR)
                {
                    tokenString.Append(curChar);
                    char nextChar = this._fileManager.Input.PeekChar();
                    if (GetCharClass(curChar) != GetCharClass(nextChar) &&
                        !(GetCharClass(curChar) == CharClass.LOWER && GetCharClass(nextChar) == CharClass.DIGIT))
                    {
                        tokenFound = true;
                    }
                    else
                    {
                        if (GetCharClass(curChar) == CharClass.SPECIAL)
                        {
                            tokenFound = true;
                            if (IsSpecialDoublet(curChar, nextChar))
                            {
                                tokenString.Append(nextChar);
                                this._fileManager.Input.NextChar();
                            }
                        }
                    }
                }
            }
            return tokenString.ToString();
        }

    }
}
