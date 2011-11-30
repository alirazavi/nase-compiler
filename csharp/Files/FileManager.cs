using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;

namespace Nase.Files
{
    public class FileManager : IDisposable
    {
        public const string INPUT_FILE_EXTENSION = ".nase";
        public const string TOTAL_LIST_FILE_EXTENSION = ".lst";
        public const string ECHO_FILE_EXTENSION = ".lst1";
        public const string LIST_FILE_EXTENSION = ".lst2";

        public const string MMIX_FILE_EXTENSION = ".mms";
        public const string MMIX_OBJECT_FILE_EXTENSION = ".mmo";
        public const string MMIX_LISTING_FILE_EXTENSION = ".mml";
        public const string MMIX_PROLOG_FILE = "_prolog.mms";
        public const string PROG_STUB_FILE = "_nase_progstub.mms";

        static readonly Logger Logger = LogManager.CreateLogger();

        public InputFile Input { get; private set; }
        public OutputFile Listing { get; private set; }
        public OutputFile Output { get; private set; }

        string _baseFilename;
        OutputFile _echo;

        public FileManager(string filename)
        {
            this._baseFilename = Path.GetDirectoryName(Path.GetFullPath(filename))
                + Path.DirectorySeparatorChar
                + Path.GetFileNameWithoutExtension(filename);

            CleanUpFiles();
            this.Listing = new OutputFile(this._baseFilename + FileManager.LIST_FILE_EXTENSION);
            this._echo = new OutputFile(this._baseFilename + FileManager.ECHO_FILE_EXTENSION);
            this.Input = new InputFile(this._baseFilename + FileManager.INPUT_FILE_EXTENSION, this._echo);
        }

        void CleanUpFiles()
        {
            try
            {
                // Listing file
                File.Delete(this._baseFilename + FileManager.TOTAL_LIST_FILE_EXTENSION);
                // Listing intermediate files
                File.Delete(this._baseFilename + FileManager.ECHO_FILE_EXTENSION);
                File.Delete(this._baseFilename + FileManager.LIST_FILE_EXTENSION);
                File.Delete(this._baseFilename + FileManager.PROG_STUB_FILE);
                // MMIX files
                File.Delete(this._baseFilename + FileManager.MMIX_FILE_EXTENSION);
                File.Delete(this._baseFilename + FileManager.MMIX_OBJECT_FILE_EXTENSION);
                File.Delete(this._baseFilename + FileManager.MMIX_LISTING_FILE_EXTENSION);
            }
            catch (Exception ex)
            {
                Logger.FatalException(ex, "Error while cleaning up old files");
            }
        }

        public void FinalizeListing()
        {
            try
            {
                this._echo.AppendLine("\n");
                Dispose();
                File.Copy(this._baseFilename + FileManager.ECHO_FILE_EXTENSION, this._baseFilename + FileManager.TOTAL_LIST_FILE_EXTENSION);
                using (FileStream readStream = File.Open(this._baseFilename + FileManager.LIST_FILE_EXTENSION, FileMode.Open))
                {
                    using (FileStream writeStream = File.Open(this._baseFilename + FileManager.TOTAL_LIST_FILE_EXTENSION, FileMode.Append))
                    {
                        byte[] content = new byte[readStream.Length];
                        readStream.Read(content, 0, (int)readStream.Length);
                        writeStream.Write(content, 0, (int)readStream.Length);
                    }
                }
                File.Delete(this._baseFilename + FileManager.ECHO_FILE_EXTENSION);
                File.Delete(this._baseFilename + FileManager.LIST_FILE_EXTENSION);
            }
            catch (Exception ex)
            {
                Logger.FatalException(ex, "Error while creating final listing file");
            }
        }

        public void InitializeOutputFile()
        {
            bool prologFileExists = File.Exists(FileManager.MMIX_PROLOG_FILE);
            if (prologFileExists)
            {
                File.Copy(FileManager.MMIX_PROLOG_FILE, this._baseFilename + FileManager.MMIX_FILE_EXTENSION);
            }
            this.Output = new OutputFile(this._baseFilename + FileManager.MMIX_FILE_EXTENSION, true);
            if (!prologFileExists)
            {
                this.Output.AppendLine(Macro.DefaultProlog());
            }
        }

        public void FinalizeOutputFile()
        {
            this.Output.Dispose();
            this.Output = null;

            string cmdLine = string.Format("-b 120 -l {0} {1}",
                this._baseFilename + FileManager.MMIX_LISTING_FILE_EXTENSION,
                this._baseFilename + FileManager.MMIX_FILE_EXTENSION);
            System.Diagnostics.Process.Start("tools\\mmixal.exe", cmdLine);
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
                this.Input.Dispose();
                this._echo.Dispose();
                this.Listing.Dispose();

                if(this.Output != null)
                    this.Output.Dispose();
            }

            this.Listing = null;

            this._disposed = true;
        }

        ~FileManager() { Dispose(false); } // finalizer called by the runtime. we should only dispose unmanaged objects and should NOT reference managed ones.

        #endregion

    }
}
