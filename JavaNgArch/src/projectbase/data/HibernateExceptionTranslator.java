package projectbase.data;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.hibernate.JDBCException;
import projectbase.sharparch.hibernate.HibernateQuery;
import projectbase.utils.Util;
import projectbase.bd.IApplicationStorage;
import projectbase.domain.DBDuplicateException;
import projectbase.domain.DBReferencedException;
import projectbase.domain.DBUserDefinedException;
import projectbase.domain.IDBErrorForUser;
import projectbase.domain.IExceptionTranslator;

public class HibernateExceptionTranslator extends HibernateQuery implements
		IExceptionTranslator {
	private static IApplicationStorage _appState;
	private static String _key_For_DBErrorMap = "Key_For_DBErrorMap";
	private static Map<String, String> emptyServerMap=new HashMap<String, String>();

	public Map<String, String> getDBConstraintMap() {
		return emptyServerMap;//LoadDBErrorMap();
	}

	public static void InitStorage(IApplicationStorage appState) {
		_appState = appState;
	}

	@SuppressWarnings("unchecked")
	protected Map<String, String> LoadDBErrorMap() {
		if (_appState.Get(_key_For_DBErrorMap) == null) {
			List<Object[]> list = (List<Object[]>) UtilQuery.StatelessGetBySql(
					"select Code,Message from GN_ErrorMsgMap");
			_appState.Set(_key_For_DBErrorMap, Util.<String,String>ListToMap(list));
		}
		return (Map<String, String>) _appState
				.Get(_key_For_DBErrorMap);
	}

	public Exception Translate(Exception exception) {
		SQLException e=null;
		if (exception instanceof IDBErrorForUser)
			return exception;
		if (exception instanceof JDBCException)
			e=((JDBCException)exception).getSQLException();
		if (exception instanceof SQLException) 
			e=(SQLException)exception;
		else if(e==null)
			return exception;
		
		String msg = e.getMessage();
		if (msg.indexOf("Duplicate") >= 0) {
			if (getDBConstraintMap() == null)
				return new DBDuplicateException(msg);
			else {
				String constraintName = "";
				try {
					int startIndex = msg.indexOf("UQ_");
					int endIndex = msg.indexOf("'", startIndex);
					constraintName = msg.substring(startIndex, endIndex);
					msg=getDBConstraintMap().get(constraintName);
					if(msg==null) msg=constraintName;
					return new DBDuplicateException(msg);
				} catch (Exception ex) {
					return new DBDuplicateException(msg);
				}
			}
		} else if (msg.indexOf("foreign key constraint fails") >= 0) {
			if (getDBConstraintMap() == null)
				return new DBReferencedException(msg);
			else {
				String constraintName = "";
				try {
					int startIndex = msg.indexOf("FK_");
					int endIndex = msg.indexOf("`", startIndex);
					constraintName = msg.substring(startIndex, endIndex);
					msg=getDBConstraintMap().get(constraintName);
					if(msg==null) msg=constraintName;
					return new DBReferencedException(msg);
				} catch (Exception ex) {
					return new DBReferencedException(msg);
				}
			}
		} else if (msg.indexOf("UserDefined:") >= 0) {
            int posb = msg.indexOf("UserDefined:");
            int pose = msg.indexOf("]]", posb);
            msg = msg.substring(posb, pose);
			return new DBUserDefinedException(msg.replace("UserDefined:", ""));
		} else {
			return e;
		}

	}

}
