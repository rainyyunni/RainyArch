package projectbase.data;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.ParameterMode;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import org.hibernate.JDBCException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.procedure.ProcedureCall;
import org.hibernate.transform.Transformers;

import projectbase.domain.IUtilQuery;
import projectbase.sharparch.domain.designbycontract.Check;
import projectbase.sharparch.hibernate.HibernateQuery;
import projectbase.sharparch.hibernate.HibernateSession;
import projectbase.utils.Pager;

    /// <summary>
    /// Utility class for native sql. Mind that use it only in special cases and with cautions for String concatenation.
    /// Watch for SQL injection holes!!!
    /// </summary>
    public class UtilQuery  extends  HibernateQuery implements  IUtilQuery
    {
        private static Logger _log = LogManager.getLogger(UtilQuery.class);

        public Object GetScalarBySql(String sql)
        {
            CheckSqlInjection(sql);
            return getSession().createSQLQuery(sql).uniqueResult();
        }
        public List<Object[]> GetBySql(String sql)
        {
            CheckSqlInjection(sql);
            return (List<Object[]>)getSession().createSQLQuery(sql).list();
        }
        public List<Object[]> GetBySql(String sql, Map<String, Object> namedParameters)
        {
            CheckSqlInjection(sql);
            SQLQuery q = getSession().createSQLQuery(sql);
            for (Entry<String, Object> namedParameter : namedParameters.entrySet())
            {
                q.setParameter(namedParameter.getKey(), namedParameter.getValue());
            }
            return (List<Object[]>)q.list();
        }
        /// <summary>
        /// Dto must have corresponding property to each column selected by the sql 
        /// </summary>
        /// <typeparam name="TDto"></typeparam>
        /// <param name="sql"></param>
        /// <returns></returns>
        public <TDto> List<TDto> GetDtoBySql(Class<TDto> dtoClass,String sql)
        {
	        return GetDtoBySql(dtoClass,sql,null);
        }
    public <TDto> List<TDto> GetDtoBySql(Class<TDto> dtoClass,String sql,Map<String,Object> namedParameters)
    {
        CheckSqlInjection(sql);
        SQLQuery q = getSession().createSQLQuery(sql);
        if(namedParameters!=null){
	        for (Entry<String, Object> namedParameter : namedParameters.entrySet())
	        {
	            q.setParameter(namedParameter.getKey(), namedParameter.getValue());
	        }
        }
        if (dtoClass.isPrimitive() || dtoClass==String.class)
        {
            return q.list();
        }
        return q.setResultTransformer(
                Transformers.aliasToBean(dtoClass)).list();
    }
    public <TDto> List<TDto> GetDtoBySql(Pager pager,String countSql,Class<TDto> dtoClass,String sql,Map<String,Object> namedParameters)
    {
    	Check.Require(pager != null, "pager may not be null!");
        CheckSqlInjection(sql);
        SQLQuery q = getSession().createSQLQuery(sql);
        SQLQuery cq = getSession().createSQLQuery(countSql);
        if(namedParameters!=null){
	        for (Entry<String, Object> namedParameter : namedParameters.entrySet())
	        {
	            q.setParameter(namedParameter.getKey(), namedParameter.getValue());
	            cq.setParameter(namedParameter.getKey(), namedParameter.getValue());
	        }
        }
        BigInteger c = (BigInteger)cq.uniqueResult();
        pager.setItemCount(c.intValue());
        if(pager.getPageSize()>0){
        	q.setFirstResult(pager.getFromRowIndex()).setMaxResults(pager.getPageSize());
        }
        if (dtoClass.isPrimitive() || dtoClass==String.class)
        {
            return q.list();
        }
        return q.setResultTransformer(
                Transformers.aliasToBean(dtoClass)).list();
    } 
        public <T> List<T> GetEntityBySql(Class<T> entityClass,String sql)
        {
            CheckSqlInjection(sql);
            return getSession().createSQLQuery(sql).addEntity(entityClass.getName()).list();
        }
        public int ExcuteSql(String sql)
        {
            CheckSqlInjection(sql,true);
            return getSession().createSQLQuery(sql).executeUpdate();
        }
        public int ExcuteSql(String sql,Map<String,Object> namedParameters)
        {
            CheckSqlInjection(sql,true);
            SQLQuery q = getSession().createSQLQuery(sql);
            for (Entry<String, Object> namedParameter : namedParameters.entrySet())
            {
                q.setParameter(namedParameter.getKey(), namedParameter.getValue());
            }
            return q.executeUpdate();
        }
        public ProcedureCall CreateSPCall(String spName)
        {
            ProcedureCall sp = getSession().createStoredProcedureCall(spName);
            return sp;
        }
        public void ExcuteSPCall(ProcedureCall sp)
        {
        	String outputError=(String)sp.getOutputs().getOutputParameterValue(sp.getRegisteredParameters().size()-1);
        	if(StringUtils.isNotEmpty(outputError))
        		throw new JDBCException("generated by Procedure call with output parameter indicating sql errors",new SQLException(outputError));
        }
        /// <summary>
        /// Dto defines which columns will be returned by the dbobject
        /// </summary>
        /// <typeparam name="TDto"></typeparam>
        /// <param name="dbobjectname">db view or function or stored procedures which return recordsets</param>
        /// <returns></returns>
        public <TDto> List<TDto> GetFromDBObj(Class<TDto> dtoClass,String dbobjectname)
        {
            return GetFromDBObj(dtoClass,dbobjectname, null);
        }
        /// <summary>
        /// Dto defines which columns will be returned by the dbobject,and you can append a sql String to do filtering 
        /// </summary>
        /// <typeparam name="TDto"></typeparam>
        /// <param name="dbobjectname"></param>
        /// <param name="appendsql"></param>
        /// <returns></returns>
        public <TDto> List<TDto> GetFromDBObj(Class<TDto> dtoClass,String dbobjectname,String appendsql)
        {
            CheckSqlInjection(appendsql);
            StringBuffer selectlist = new StringBuffer();
        	PropertyDescriptor[] pds=PropertyUtils.getPropertyDescriptors(dtoClass);
        	for(PropertyDescriptor pd : pds){
        		Method getter=pd.getReadMethod();
        		if(getter==null||getter.getDeclaringClass()==Object.class)continue;
            	String prop=pd.getName();
            	selectlist.append("`"+prop.substring(3)+"`,");
            }
            selectlist.deleteCharAt(selectlist.length()-1) ;
            StringBuffer sql = new StringBuffer("select " + selectlist + " from " + dbobjectname+" ");
            if(appendsql!=null&&!appendsql.isEmpty())
            {
                sql.append(appendsql);
            }
            List<TDto> t = getSession().createSQLQuery(sql.toString()).setResultTransformer(
                    Transformers.aliasToBean(dtoClass)).list();
            return t;
        }
        public static void StatelessExecuteSql(String sql)
        {
            CheckSqlInjection(sql,true);
            StatelessSession session = GetStatelessSession();
            try {
            	session.createSQLQuery(sql).executeUpdate();
            }catch(Exception e){
            	throw e;
            }
            finally{
            	CloseStatelessSession(session);
            }
        }
        public static void StatelessExecuteSql(String sql,Map<String, Object> namedParameters)
        {
            CheckSqlInjection(sql,true);
            StatelessSession session = GetStatelessSession();
            try {
            	SQLQuery q=session.createSQLQuery(sql);
	            for (Entry<String, Object> namedParameter : namedParameters.entrySet())
	            {
	                q.setParameter(namedParameter.getKey(), namedParameter.getValue());
	            }
            	q.executeUpdate();
            }catch(Exception e){
            	throw e;
            }
            finally{
            	CloseStatelessSession(session);
            }
        }
        public static Object StatelessGetScalarBySql(String sql)
        {
            CheckSqlInjection(sql);
            StatelessSession session = GetStatelessSession();
            try {
            	Object rtn= session.createSQLQuery(sql).uniqueResult();
            	return rtn;
            }catch(Exception e){
            	throw e;
            }
            finally{
            	CloseStatelessSession(session);
            }
        }
        public static List<Object[]> StatelessGetBySql(String sql)
        {
            CheckSqlInjection(sql);
            StatelessSession session = GetStatelessSession();
            try{
	            List<Object[]> rtn = session.createSQLQuery(sql).list();
	            return rtn;
            }catch(Exception e){
            	throw e;
            }
            finally{
            	CloseStatelessSession(session);
            }
        }
        public static List<Object[]> StatelessGetBySql(String sql, Map<String, Object> namedParameters)
        {
            CheckSqlInjection(sql);
            StatelessSession session = GetStatelessSession();
            try{
	            SQLQuery q = session.createSQLQuery(sql);
	            for (Entry<String, Object> namedParameter : namedParameters.entrySet())
	            {
	                q.setParameter(namedParameter.getKey(), namedParameter.getValue());
	            }
	            List<Object[]> l = q.list();
	
	            return l;
            }catch(Exception e){
            	throw e;
            }
            finally{
            	CloseStatelessSession(session);
            }
        }
        private static StatelessSession GetStatelessSession()
        {
            return HibernateSessionModified.SessionFactories.get(HibernateSession.DefaultFactoryKey).openStatelessSession();
        }
        private static void CloseStatelessSession(StatelessSession session)
        {
            session.close() ;
        }       
		private static void CheckSqlInjection(String sql)
	
		{
			CheckSqlInjection(sql, false);
		}
        
        private static void CheckSqlInjection(String sql,boolean checkChange)
        {
            if(!_log.isEnabledFor(Priority.WARN)) return;

            if (checkChange){
                sql = sql.toLowerCase();
                String[] words =new String[]{"delete","update","insert","create","drop","alter"};
                for(String word :words)
                                        {
                                            if(sql.contains(word))
                                            {
                                                _log.warn("changing word found in native sql:" + sql);
                                            }
                                        }
                
            }
            if(sql.contains("\'"))
                _log.warn("single quote found in native sql:"+sql);
        }

    }


