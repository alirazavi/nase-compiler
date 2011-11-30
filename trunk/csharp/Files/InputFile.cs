using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;

namespace Nase.Files
{
    public class InputFile : Stream, IDisposable
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

        OutputFile _echo;
        int _lineNumber;
        int _columnNumber;

        public FilePosition InputFilePosition
        {
            get
            {
                return new FilePosition()
                {
                    StartLine = this._lineNumber,
                    StartColumn = this._columnNumber
                };
            }
        }

        public InputFile(string filename, OutputFile echo)
        {
            this._echo = echo;
            this._lineNumber = 1;
            this._columnNumber = 1;

            try
            {
                this._fileStream = File.Open(filename, FileMode.Open);
                this._inputStream = new StreamReader(this._fileStream);
            }
            catch (Exception ex)
            {
                Logger.FatalException(ex, "Error while opening input file");
            }

            echo.AppendLine("Compilation listing of <{0}>", filename);
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

        public override void Write(byte[] buffer, int offset, int count)
        {
            throw new NotImplementedException();
        }

        public override void SetLength(long value)
        {
            throw new NotImplementedException();
        }

        public override long Seek(long offset, SeekOrigin origin)
        {
            throw new NotImplementedException();
        }

        public override int Read(byte[] buffer, int offset, int count)
        {
            return this._fileStream.Read(buffer, offset, count);
        }

        public override long Position
        {
            get { return this._fileStream.Position; }
            set { this._fileStream.Position = value; }
        }

        public override long Length
        {
            get { return this._fileStream.Length; }
        }

        public override void Flush()
        {
            throw new NotImplementedException();
        }

        public override bool CanRead
        {
            get { throw new NotImplementedException(); }
        }

        public override bool CanWrite
        {
            get { throw new NotImplementedException(); }
        }

        public override bool CanSeek
        {
            get { throw new NotImplementedException(); }
        }

        #region de-ctor

        // IDisposable pattern: http://msdn.microsoft.com/en-us/library/fs2xkftw%28VS.80%29.aspx

        private bool _disposed = false;
        public new void Dispose()
        {
            Dispose(true);
            GC.SuppressFinalize(this); // Take object out the finalization queue to prevent finalization code for it from executing a second time.
        }

        protected override void Dispose(bool disposing)
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
