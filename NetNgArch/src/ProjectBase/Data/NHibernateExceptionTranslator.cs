using System;
using System.Collections.Generic;
using System.Text;
using System.Web;
using ProjectBase.BusinessDelegate;
using ProjectBase.Domain;
using NHibernate;
using System.Linq;
using SharpArch.NHibernate;

namespace ProjectBase.Data
{
    public class NHibernateExceptionTranslator : NHibernateQuery,IExceptionTranslator
    {
        private static IApplicationStorage _appState;
        private static string _key_For_DBErrorMap = "Key_For_DBErrorMap";
        private static IDictionary<String, String> emptyServerMap = new Dictionary<String, String>();

        public IDictionary<String, String> DBConstraintMap
        {
            get
            {
                return emptyServerMap;// LoadDBErrorMap();
            }
        }

        public static void InitStorage(IApplicationStorage appState)
        {
            _appState = appState;
        }

        protected IDictionary<string, string> LoadDBErrorMap()
        {
            if (_appState.Get(_key_For_DBErrorMap)== null)
            {
                var list = Session.CreateSQLQuery("select Code,Message from GN_ErrorMsgMap").List<Object[]>();
                _appState.Set(_key_For_DBErrorMap, list.ToDictionary(o=>o[0] as string,o=>o[1] as string));
            }
            return _appState.Get(_key_For_DBErrorMap) as Dictionary<string, string>;
        }

        #region IExceptionTranslator Members
        public Exception Translate(Exception e)
        {
            if (e is IDBErrorForUser) return e;

            if (!(e is ADOException)) return e;

            string msg = e.InnerException.Message;
            if (msg.IndexOf("UNIQUE KEY") >= 0)
            {
                if (DBConstraintMap == null)
                    return new DBDuplicateException();
                else
                {
                    var constraintName="";
                    try 
                    {
                         var startIndex = msg.IndexOf("UQ_");
                        var endIndex=msg.IndexOf("'",startIndex);
                        constraintName=msg.Substring(startIndex,endIndex-startIndex);
                        msg = DBConstraintMap.ContainsKey(constraintName) ? DBConstraintMap[constraintName] : constraintName;
                        return new DBDuplicateException(msg);
                    }
                    catch (Exception ex)
                    {
                        return new DBDuplicateException(msg);
                    }
                }
            }
            else if (msg.IndexOf("REFERENCE") >= 0)
            {
                if (DBConstraintMap == null)
                    return new DBReferencedException();
                else
                {
                    var constraintName = "";
                    try
                    {
                        var startIndex = msg.IndexOf("FK_");
                        var endIndex = msg.IndexOf("\"", startIndex);
                        constraintName = msg.Substring(startIndex, endIndex - startIndex);
                        msg = DBConstraintMap.ContainsKey(constraintName) ? DBConstraintMap[constraintName] : constraintName;
                        return new DBReferencedException(msg);
                    }
                    catch (Exception ex)
                    {
                        return new DBReferencedException(msg);
                    }
                }
            }
            else if (msg.IndexOf("UserDefined:") >= 0)
            {
                var posb = msg.IndexOf("UserDefined:");
                var pose = msg.IndexOf("]]", posb);
                msg = msg.Substring(posb, pose - posb);
                return new DBUserDefinedException(msg.Replace("UserDefined:", ""));
            }
            else
            {
                return e;
            }
           
        }

        #endregion
    }
}
