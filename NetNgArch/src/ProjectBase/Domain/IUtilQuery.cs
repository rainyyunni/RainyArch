using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ProjectBase.Domain
{
    public interface IUtilQuery
    {
        Object GetScalarBySql(string sql);
        IList<Object[]> GetBySql(string sql);
        IList<Object[]> GetBySql(string sql, IDictionary<string, Object> namedParameters);
        IList<TDto> GetBySql<TDto>(string sql);
        IList<TDto> GetBySql<TDto>(string sql, IDictionary<string, Object> namedParameters);
        IList<T> GetEntityBySql<T>(string sql);
        int ExcuteSql(string sql);
        IList<TDto> GetFromDBObj<TDto>(string dbobjectname);
        IList<TDto> GetFromDBObj<TDto>(string dbobjectname, string appendsql);
    }
}
