using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Nase
{
    public class CodeGeneratorHelper
    {
        ulong _curLabel = 0;
        ulong _nextFreeConstantAddress = 0;
        ulong _nextFreeVariableAddress = 0;

        public string GenerateNextLabel()
        {
            return string.Format("NCL{0,00000}", this._curLabel++);
        }

        public ulong GetAndPushConstantAddress()
        {
            return this._nextFreeConstantAddress++;
        }

        public ulong GetAndPushVariableAddress()
        {
            return this._nextFreeVariableAddress++;
        }

        public void PopVariableAddress()
        {
            this._nextFreeVariableAddress--;
        }

        public ulong GetVariableAddressCount()
        {
            return this._nextFreeVariableAddress;
        }
    }
}
