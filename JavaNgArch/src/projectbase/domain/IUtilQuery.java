package projectbase.domain;

import java.util.List;
import java.util.Map;

import org.hibernate.procedure.ProcedureCall;

import projectbase.utils.Pager;

public interface IUtilQuery {
	
	Object GetScalarBySql(String sql);

	List<Object[]> GetBySql(String sql);

    List<Object[]> GetBySql(String sql, Map<String, Object> namedParameters);

	<TDto> List<TDto> GetDtoBySql(Class<TDto> dtoClass,String sql);
	
	<TDto> List<TDto> GetDtoBySql(Class<TDto> dtoClass,String sql, Map<String, Object> namedParameters);
    
	<T> List<T> GetEntityBySql(Class<T> entityClass,String sql);

	int ExcuteSql(String sql);

	<TDto> List<TDto> GetFromDBObj(Class<TDto> dtoClass,String dbobjectname);

	<TDto> List<TDto> GetFromDBObj(Class<TDto> dtoClass,String dbobjectname, String appendsql);
	
	ProcedureCall CreateSPCall(String spName);
	void ExcuteSPCall(ProcedureCall sp);

	<TDto> List<TDto> GetDtoBySql(Pager pager,String countSql,Class<TDto> dtoClass,String sql,Map<String,Object> namedParameters);
}
