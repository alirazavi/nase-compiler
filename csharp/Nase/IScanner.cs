using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Nase.GeneratedParser;
using Nase.Files;

namespace Nase
{
    public interface IScanner
    {
        Symbol NextSymbol();
        Symbol PeekSymbol();
        void SkipToDelimiter();
        FilePosition InputFilePosition { get; }
    }
}
