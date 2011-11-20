using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Nase
{
    public interface IScanner
    {
        Symbol NextSymbol();
        Symbol PeekSymbol();
        void SkipToDelimiter();
        void ScannerTest();
    }
}
