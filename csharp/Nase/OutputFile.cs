using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;

namespace Nase
{
    public class OutputFile : IDisposable
    {
        static readonly Logger Logger = LogManager.CreateLogger();

        FileStream _fileStream;
        StreamWriter _writeStream;

        public OutputFile(string filename, bool append = false)
        {
            try
            {
                FileMode openMode = FileMode.Create;
                if (append)
                {
                    openMode = FileMode.Append;
                }

                this._fileStream = File.Open(filename, openMode);
                this._writeStream = new StreamWriter(this._fileStream);
            }
            catch (Exception ex)
            {
                Logger.FatalException(ex, "Error while creating file \"{0}\"", filename);
            }
        }

        public void AppendChar(char c)
        {
            this._writeStream.Write(c);
        }

        public void Append(string line)
        {
            this._writeStream.Write(line);
        }

        public void AppendLine(string line)
        {
            this._writeStream.WriteLine(line);
        }

        public void AppendLine(string line, params object[] args)
        {
            this._writeStream.WriteLine(line, args);
        }

        public void AppendLineNumber(uint lineNr)
        {
            this._writeStream.Write("{0:00000}: ", lineNr);
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
                this._writeStream.Close();
                this._writeStream.Dispose();
                this._fileStream.Close();
                this._fileStream.Dispose();
            }

            this._writeStream = null;
            this._fileStream = null;

            this._disposed = true;
        }

        ~OutputFile() { Dispose(false); } // finalizer called by the runtime. we should only dispose unmanaged objects and should NOT reference managed ones.

        #endregion

    }
}
