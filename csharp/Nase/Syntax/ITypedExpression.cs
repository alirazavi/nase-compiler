using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Nase.Syntax
{
    public enum ExpressionType {
        Integer,
        Boolean
    }

    public interface ITypedExpression
    {
        ExpressionType GetExpressionType();
        bool CheckTypeMismatch();
    }
}
