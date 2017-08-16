package projectbase.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import projectbase.sharparch.domain.designbycontract.Check;


    public class HqlCriterion
    {
    	private int innerNamedParameterCount;
        private StringBuffer hql=new StringBuffer();
        private Map<String,Object> parameters=new HashMap<String,Object>();
        

        public HqlCriterion()
        {
        }

        public boolean IsEmpty()
        {
            return StringUtils.isEmpty(hql);
        }  
        /**
         * hql string with parameters
         * @return
         */
        public String ToHqlString()
        {
            return hql==null?"":hql.toString();
        }
        /**
         * hql string without parameters which have been evaluated and replaced
         * @return
         */
        public String ToEvalualtedHqlString()
        {
        	if(StringUtils.isEmpty(hql)) return "";
        	String rtn=hql.toString();
        	 for (Map.Entry<String, Object> entry : parameters.entrySet()){
        		 if(entry.getValue() instanceof String || entry.getValue() instanceof Date)
        			 rtn=rtn.replace(":"+entry.getKey(), "'"+ToSqlString(entry.getValue().toString())+"'");
        		 else
        			 rtn=rtn.replace(":"+entry.getKey(), entry.getValue().toString());
        	 }
            return rtn;
        }
        public HqlCriterion And(String hqlinWhere)
        {
        	if (!StringUtils.isEmpty(hql)) hql.append(" And ");
            hql.append(hqlinWhere);
            return this;
        }
        /**
         * take all ? in hqlinWhere as the same one parameter and to be given the parameterValue
         * @param hqlinWhere
         * @param parameterValue
         * @return
         */
        public HqlCriterion And(String hqlinWhere, Object parameterValue)
        {
        	Check.Require(parameterValue!=null,"parameterValue can't be null!");
            if (!StringUtils.isEmpty(hql)) hql.append(" And ");
            if(parameterValue!=null){
            	String namedParameterName="p"+innerNamedParameterCount+"_";
            	hqlinWhere=hqlinWhere.replace("?",":"+namedParameterName );
            	parameters.put(namedParameterName, parameterValue);
            	innerNamedParameterCount++;
            }
            hql.append(hqlinWhere);
            return this;
        }
        /**
         * take each ? in hqlinWhere as a different parameter and give each a value in order . 
         * @param hqlinWhere
         * @param parameterValues
         * @return
         */
        public HqlCriterion And(String hqlinWhere, Object parameterValue,Object...moreParameters )
        {
        	Check.Require(parameterValue!=null,"parameterValue can't be null!");
        	Check.Require(moreParameters!=null,"moreParameters can't be null!");
            if (!StringUtils.isEmpty(hql)) hql.append(" And ");
            if(parameterValue!=null){
            	String namedParameterName="p"+innerNamedParameterCount+"_";
            	hqlinWhere=StringUtils.replaceOnce(hqlinWhere,"?",":"+namedParameterName );
            	parameters.put(namedParameterName, parameterValue);
            	innerNamedParameterCount++;
            }
            for(Object moreParameterValue : moreParameters){
            	Check.Require(moreParameterValue!=null,"moreParameterValue can't be null!");
            	String namedParameterName="p"+innerNamedParameterCount+"_";
            	hqlinWhere=StringUtils.replaceOnce(hqlinWhere,"?",":"+namedParameterName );
            	parameters.put(namedParameterName, moreParameterValue);
            	innerNamedParameterCount++;
            }
            hql.append(hqlinWhere);
            return this;
        } 
        /**
         * take each ? in hqlinWhere as a different parameter and give each a value in order . 
         * @param hqlinWhere
         * @param parameterValues
         * @return
         */
        public HqlCriterion And(String hqlinWhere, Object[] parameterValues)
        {
        	Check.Require(parameterValues!=null,"parameterValues can't be null!");
            if (!StringUtils.isEmpty(hql)) hql.append(" And ");
            for(Object parameterValue : parameterValues){
            	Check.Require(parameterValue!=null,"parameterValue can't be null!");
            	String namedParameterName="p"+innerNamedParameterCount+"_";
            	hqlinWhere=StringUtils.replaceOnce(hqlinWhere,"?",":"+namedParameterName );
            	parameters.put(namedParameterName, parameterValue);
            	innerNamedParameterCount++;
            }
            hql.append(hqlinWhere);
            return this;
        }  
        /**
         * you can use namedparameter in hqlinWhere,and map name to value by parameterMap
         * note that prefix "p_" has been use by this framework so don't use it to name your parameter.
         * @param hqlinWhere
         * @param parameterMap
         * @return
         */
        public HqlCriterion And(String hqlinWhere, Map<String,Object> parameterMap)
        {
        	Check.Require(parameterMap!=null,"parameterMap can't be null!");
            if (!StringUtils.isEmpty(hql)) hql.append(" And ");
            if(parameterMap!=null){
            	parameters.putAll(parameterMap);
            }
            hql.append(hqlinWhere);
            return this;
        } 
        public HqlCriterion And(HqlCriterion criterion)
        {
        	if (!StringUtils.isEmpty(hql)) hql=new StringBuffer("("+hql+") And ");
        	String includedHql=criterion.ToHqlString();
        	int includedCnt=criterion.getParameters().size();
        	for (int i=includedCnt-1;i>=0;i--){
        		int newIndex=innerNamedParameterCount+i;
        		parameters.put("p"+newIndex+"_",criterion.getParameters().get("p"+i+"_"));
        		includedHql=includedHql.replace(":p"+i+"_",":p"+newIndex+"_");
        	}
    		innerNamedParameterCount=innerNamedParameterCount+includedCnt;
        	hql.append( "(" + includedHql + ")");
            return this;
        }


        public HqlCriterion Or(String hqlinWhere)
        {
        	if (!StringUtils.isEmpty(hql)) hql.append(" Or ");
            hql.append(hqlinWhere);
            return this;
        }
        /**
         * take all ? in hqlinWhere as the same one parameter and to be given the parameterValue
         * @param hqlinWhere
         * @param parameterValue
         * @return
         */
        public HqlCriterion Or(String hqlinWhere, Object parameterValue)
        {
        	Check.Require(parameterValue!=null,"parameterValue can't be null!");
            if (!StringUtils.isEmpty(hql)) hql.append(" Or ");
            if(parameterValue!=null){
            	String namedParameterName="p"+innerNamedParameterCount+"_";
            	hqlinWhere=hqlinWhere.replace("?",":"+namedParameterName );
            	parameters.put(namedParameterName, parameterValue);
            	innerNamedParameterCount++;
            }
            hql.append(hqlinWhere);
            return this;
        }
        /**
         * take each ? in hqlinWhere as a different parameter and give each a value in order . 
         * @param hqlinWhere
         * @param parameterValues
         * @return
         */
        public HqlCriterion Or(String hqlinWhere, Object[] parameterValues)
        {
        	Check.Require(parameterValues!=null,"parameterValues can't be null!");
            if (!StringUtils.isEmpty(hql)) hql.append(" Or ");
            for(Object parameterValue : parameterValues){
            	Check.Require(parameterValue!=null,"parameterValue can't be null!");
            	String namedParameterName="p"+innerNamedParameterCount+"_";
            	hqlinWhere=StringUtils.replaceOnce(hqlinWhere,"?",":"+namedParameterName );
            	parameters.put(namedParameterName, parameterValue);
            	innerNamedParameterCount++;
            }
            hql.append(hqlinWhere);
            return this;
        }  
        /**
         * you can use namedparameter in hqlinWhere,and map name to value by parameterMap
         * note that prefix "p_" has been use by this framework so don't use it to name your parameter.
         * @param hqlinWhere
         * @param parameterMap
         * @return
         */
        public HqlCriterion Or(String hqlinWhere, Map<String,Object> parameterMap)
        {
        	Check.Require(parameterMap!=null,"parameterMap can't be null!");
            if (!StringUtils.isEmpty(hql)) hql.append(" Or ");
            if(parameterMap!=null){
            	parameters.putAll(parameterMap);
            }
            hql.append(hqlinWhere);
            return this;
        } 
        public HqlCriterion Or(HqlCriterion criterion)
        {
        	if (!StringUtils.isEmpty(hql)) hql=new StringBuffer("("+hql+") Or ");
        	String includedHql=criterion.ToHqlString();
        	int includedCnt=criterion.getParameters().size();
        	for (int i=includedCnt-1;i>=0;i--){
        		int newIndex=innerNamedParameterCount+i;
        		parameters.put("p"+newIndex+"_",criterion.getParameters().get("p"+i+"_"));
        		includedHql=includedHql.replace(":p"+i+"_",":p"+newIndex+"_");
        	}
    		innerNamedParameterCount=innerNamedParameterCount+includedCnt;
        	hql.append( "(" + includedHql + ")");
            return this;
        }

		public Map<String,Object> getParameters() {
			return parameters;
		}
        public static String ToSqlString(String inputstring)
        {
            return inputstring == null ? null  :  inputstring.replace("'", "''");
        }

    }


