using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;

namespace Nase
{
    public struct FilePosition
    {
        public uint Line;
        public uint Column;
    }

    class InputFile : IDisposable
    {
        public const char EOF_CHAR = '\x3';
        public const char BLANK_CHAR = ' ';
        public const char TAB_CHAR = '\t';
        public const char NEWLINE_CHAR = '\n';
        public const char RETURN_CHAR = '\r';
        const int TAB_SIZE = 3;

        static readonly Logger Logger = LogManager.CreateLogger();

        FileStream _fileStream;
        StreamReader _inputStream;

        EchoFile _echo;
        uint _lineNumber;
        uint _columnNumber;

        public FilePosition Position
        {
            get
            {
                return new FilePosition()
                {
                    Line = this._lineNumber,
                    Column = this._columnNumber
                };
            }
        }

        public InputFile(string baseFilename, EchoFile echo)
        {
            this._echo = echo;
            this._lineNumber = 1;
            this._columnNumber = 1;

            try
            {
                this._fileStream = File.Open(baseFilename + FileManager.INPUT_FILE_EXTENSION, FileMode.Open);
                this._inputStream = new StreamReader(this._fileStream);
            }
            catch (Exception ex)
            {
                Logger.FatalException(ex, "Error while opening input file");
            }

            echo.AppendLine("Compilation listing of <{0}>", baseFilename + FileManager.INPUT_FILE_EXTENSION);
            echo.AppendLine("zzzzz:          11111111112222222222333333333344444444445555555555666666666677777777778");
            echo.AppendLine("eeeee: 12345678901234567890123456789012345678901234567890123456789012345678901234567890");
            echo.AppendLineNumber(this._lineNumber);
        }

        public char PeekChar()
        {
            int c = this._inputStream.Peek();
            return CheckChar(c, false);
        }

        public char NextChar()
        {
            this._columnNumber++;
            int c = this._inputStream.Read();
            return CheckChar(c, true);
        }

        char CheckChar(int c, bool echo)
        {
            if (c == -1)
            {
                return InputFile.EOF_CHAR;
            }

            char nextChar = (char)c;
            if (echo)
            {
                this._echo.AppendChar(nextChar);
            }

            switch (nextChar)
            {
                case InputFile.TAB_CHAR:
                    nextChar = InputFile.BLANK_CHAR;
                    this._columnNumber += InputFile.TAB_SIZE;
                    break;
                case InputFile.NEWLINE_CHAR:
                    nextChar = InputFile.BLANK_CHAR;
                    this._columnNumber = 1;
                    this._lineNumber++;
                    this._echo.AppendLineNumber(this._lineNumber);
                    break;
                case InputFile.RETURN_CHAR:
                    nextChar = InputFile.BLANK_CHAR;
                    this._columnNumber = 1;
                    break;
                case '$':
                    string line = this._inputStream.ReadLine();
                    if(line == null)
                    {
                        return InputFile.EOF_CHAR;
                    }
                    nextChar = InputFile.BLANK_CHAR;
                    this._echo.AppendLine(line);
                    this._columnNumber = 1;
                    this._lineNumber++;
                    this._echo.AppendLineNumber(this._lineNumber);
                    break;
            }

            return nextChar;
        }

        #region de-ctor

        // IDisposable pattern: http://msdn.microsoft.com/en-us/library/fs2xkftw%28VS.80%29.aspx

        private bool _disposed = false;
        public void Dispose()
        {
            Dispose(true);
            GC.SuppressFinalize(this); // Take object out the finalization queue to prevent finalization code for it from executing a second time.
        }

        private void Dispose(bool disposing)
        {
            if (this._disposed) return; // if already disposed, just return

            if (disposing) // only dispose managed resources if we're called from directly or in-directly from user code.
            {
                this._inputStream.Close();
                this._inputStream.Dispose();
                this._fileStream.Close();
                this._fileStream.Dispose();
            }

            this._inputStream = null;
            this._fileStream = null;

            this._disposed = true;
        }

        ~InputFile() { Dispose(false); } // finalizer called by the runtime. we should only dispose unmanaged objects and should NOT reference managed ones.

        #endregion

    }
}
