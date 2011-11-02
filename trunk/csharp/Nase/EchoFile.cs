using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;

namespace Nase
{
    class EchoFile : IDisposable
    {
        static readonly Logger Logger = LogManager.CreateLogger();

        FileStream _fileStream;
        StreamWriter _echoStream;

        public EchoFile(string baseFilename)
        {
            try
            {
                this._fileStream = File.Open(baseFilename + FileManager.ECHO_FILE_EXTENSION, FileMode.Create);
                this._echoStream = new StreamWriter(this._fileStream);
            }
            catch (Exception ex)
            {
                Logger.FatalException(ex, "Error while creating echo file");
            }
        }

        public void AppendChar(char c)
        {
            this._echoStream.Write(c);
        }

        public void AppendLine(string line)
        {
            this._echoStream.WriteLine(line);
        }

        public void AppendLine(string line, params object[] args)
        {
            this._echoStream.WriteLine(line, args);
        }

        public void AppendLineNumber(uint lineNr)
        {
            this._echoStream.Write("{0:00000}: ", lineNr);
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
                this._echoStream.Close();
                this._echoStream.Dispose();
                this._fileStream.Close();
                this._fileStream.Dispose();
            }

            this._echoStream = null;
            this._fileStream = null;

            this._disposed = true;
        }

        ~EchoFile() { Dispose(false); } // finalizer called by the runtime. we should only dispose unmanaged objects and should NOT reference managed ones.

        #endregion

    }
}
