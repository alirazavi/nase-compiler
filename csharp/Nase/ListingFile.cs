using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;

namespace Nase
{
    class ListingFile : IDisposable
    {
        static readonly Logger Logger = LogManager.CreateLogger();

        FileStream _fileStream;
        StreamWriter _listingStream;

        public ListingFile(string baseFilename)
        {
            try
            {
                this._fileStream = new FileStream(baseFilename + FileManager.LIST_FILE_EXTENSION, FileMode.Create);
                this._listingStream = new StreamWriter(this._fileStream);
            }
            catch (Exception ex)
            {
                Logger.FatalException(ex, "Error while creating listing file");
            }
        }

        public void AppendChar(char c)
        {
            this._listingStream.Write(c);
        }

        public void AppendLine(string line)
        {
            this._listingStream.WriteLine(line);
        }

        public void AppendLine(string line, params object[] args)
        {
            this._listingStream.WriteLine(line, args);
        }

        public void AppendInternalErrorLine(string line)
        {
            AppendLine("INTERNAL ERROR: " + line);
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
                this._listingStream.Close();
                this._listingStream.Dispose();
                this._fileStream.Close();
                this._fileStream.Dispose();
            }

            this._listingStream = null;
            this._fileStream = null;

            this._disposed = true;
        }

        ~ListingFile() { Dispose(false); } // finalizer called by the runtime. we should only dispose unmanaged objects and should NOT reference managed ones.

        #endregion

    }
}
