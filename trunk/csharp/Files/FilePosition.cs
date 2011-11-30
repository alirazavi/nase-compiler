using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using QUT.Gppg;

namespace Nase.Files
{

    public class FilePosition : IMerge<FilePosition>
    {
        public int StartLine;
        public int StartColumn;
        public int EndLine;
        public int EndColumn;

        public FilePosition() { }
        public FilePosition(int sl, int sc, int el, int ec)
        {
            this.StartLine = sl;
            this.StartColumn = sc;
            this.EndLine = el;
            this.EndColumn = ec;
        }

        public FilePosition Merge(FilePosition last)
        {
            return new FilePosition(this.StartLine, this.StartColumn, last.EndLine, last.EndColumn);
        }
    }
}
