package projectbase.utils;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import projectbase.sharparch.domain.designbycontract.Check;

public class ProjectHierarchy {
	private static String _businessDelegateNS;
	private static String _dataNS;
	private static String _domainNS;
	private static String _mvcNS;
	private static Map<String,String> _namespaceMapToTablePrefix;

	public static String ProjectName = null;

	public static String getBusinessDelegateNS() {

		if (_businessDelegateNS == null || _businessDelegateNS.isEmpty())
			return ProjectName + ".businessdelegate";
		else {
			return _businessDelegateNS;
		}
	}

	public static void setBusinessDelegateNS(String value) {
		_businessDelegateNS = value;
	}

	public static String getDataNS() {
		if (_dataNS == null || _dataNS.isEmpty())
			return ProjectName + ".data";
		else {
			return _dataNS;
		}
	}

	public static void setDataNS(String value) {
		_dataNS = value;
	}

	public static String getDomainNS() {
		if (_domainNS == null || _domainNS.isEmpty())
			return ProjectName + ".domain";
		else {
			return _domainNS;
		}
	}

	public static void setDomainNS(String value) {
		_domainNS = value;
	}

	public static String getMvcNS() {

		if (_mvcNS == null || _mvcNS.isEmpty())
			return ProjectName + ".mvc";
		else {
			return _mvcNS;
		}
	}
	public static void setMvcNS(String value) {
		_mvcNS = value;
	}

	public static Map<String,String> getNamespaceMapToTablePrefix() {
		return _namespaceMapToTablePrefix;
	}
	public static void setNamespaceMapToTablePrefix(Map<String,String> value)
    {
		_namespaceMapToTablePrefix=value;
    }
	public static void Init(String projectname,Map<String,String> namespaceMapToTablePrefix){
		Check.Require(StringUtils.isNotBlank(projectname));
		Check.Require(namespaceMapToTablePrefix!=null);
		ProjectName=projectname;
		_namespaceMapToTablePrefix=namespaceMapToTablePrefix;
	}
	public static String MessagesResourceClassKey = "Messages";
	public static String ValidationMessagesResourceClassKey = "ValidationMessages";
	public static String DisplayNameResourceClassKey = "DisplayName";
}
