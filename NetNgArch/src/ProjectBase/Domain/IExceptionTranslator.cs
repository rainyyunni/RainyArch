using System;
using System.Collections.Generic;
using System.Text;
using ProjectBase.Utils;

namespace ProjectBase.Domain
{
    public interface IExceptionTranslator
    {
        Exception Translate(Exception e);
    }
    public interface IDBErrorForUser : IErrorForUser
    {
    }

    public class DBDuplicateException : Exception, IDBErrorForUser
    {
        private string _table;
        private string[] _fieldNames;
        private string[] _fieldValues;
        public DBDuplicateException()
        {
        }
        public DBDuplicateException(string msg)
            : base(msg)
        {
        }
    }

    public class DBReferencedException : Exception, IDBErrorForUser
    {
        private string _foriegnTable;
        private string _foriegnFieldName;
        private string _foriegnFieldVale;
        private string _primaryTable;
        private string _primaryFieldName;
        private string _primaryFieldValue;
        public DBReferencedException()
        {
        }
        public DBReferencedException(string msg)
            : base(msg)
        {
        }
    }
    public class DBUserDefinedException : Exception, IDBErrorForUser
    {
        private string _foriegnTable;
        private string _foriegnFieldName;
        private string _foriegnFieldVale;
        private string _primaryTable;
        private string _primaryFieldName;
        private string _primaryFieldValue;
        public DBUserDefinedException()
        {
        }
        public DBUserDefinedException(string msg)
            : base(msg)
        {
        }
    }
}
