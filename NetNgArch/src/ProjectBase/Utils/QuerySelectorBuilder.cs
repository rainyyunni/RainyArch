using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ProjectBase.Utils
{
    public class QuerySelectorBuilder
    {
        private static char[] _caption = new char[]{
                                                           'A','B','C','D','E','F','G','H','I','J','K','L',
                                                           'M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'
        };
    

    public static String BuildSelector<TEntity,TDto>(IDictionary<String,String> propNameMap=null){
		String selector = "";
      	var props = typeof(TDto).GetProperties().Where(o=>o.CanWrite).ToArray();
      	String parameterExpr = "this";
      	foreach(var prop in props){
          	String propName=prop.Name;

          	String entityPropExpr=null; 
          	if (propNameMap != null && propNameMap.ContainsKey(propName)){
          		String propMappedName=propNameMap[propName];
          		entityPropExpr=ChainedPropExprByMap(parameterExpr, propMappedName,typeof(TEntity),prop.PropertyType);
          	}else
                entityPropExpr = ChainedPropExpr(parameterExpr, propName, typeof(TEntity) ,prop.PropertyType);
              if (entityPropExpr == null) throw new InvalidOperationException("can't figure out " + propName);
              selector=selector+","+entityPropExpr+" as "+propName;
          }
          return selector.Substring(1);
		}

    private static String ChainedPropExpr(String expr, String propName, Type containerType, Type dtoPropType)
      {
          if (propName == "") return null;
          var entityProp = containerType.GetProperty(propName);
          if (entityProp != null) return expr + "." + propName;
          var firstWordEnd = propName.IndexOfAny(_caption, 1);
          if (firstWordEnd <= 0) return null;
          var firstWord = propName.Substring(0, firstWordEnd);
          entityProp = containerType.GetProperty(firstWord);
          if (entityProp == null) return null;
          var firstPart = expr + "." + firstWord;
          var sencondPart = propName.Substring(firstWordEnd);
          return ChainedPropExpr(firstPart, sencondPart, entityProp.PropertyType, dtoPropType);
     }

    private static String ChainedPropExprByMap(String expr, String propMappedName, Type containerType, Type dtoPropType)
      {
          if (propMappedName == "") return null;
          var entityProp = containerType.GetProperty(propMappedName);
          if (entityProp != null) return expr + "." + propMappedName;
          var firstWordEnd = propMappedName.IndexOf("|");
          if (firstWordEnd <= 0) return null;
          var firstWord = propMappedName.Substring(0, firstWordEnd);
          entityProp = containerType.GetProperty(firstWord);
          if (entityProp == null) return null;
          var firstPart = expr + "." + firstWord;
          var sencondPart = propMappedName.Substring(firstWordEnd + 1);
          return ChainedPropExprByMap(firstPart, sencondPart, entityProp.PropertyType, dtoPropType);
      }
    }
}
