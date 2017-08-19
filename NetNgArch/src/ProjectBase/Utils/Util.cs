using System;
using System.Collections;
using System.Collections.Generic;
using System.Collections.Specialized;
using System.Configuration;
using System.IO;
using System.Linq;
using System.Security.Cryptography;
using System.Text;
using System.Web;
using System.Web.Configuration;
using NHibernate;
using ProjectBase.BusinessDelegate;
using ProjectBase.Data;
using ProjectBase.Domain;
using ProjectBase.Web.Mvc;
using SharpArch.NHibernate;

namespace ProjectBase.Utils
{
    public class Util:NHibernateQuery
    {
        private static IApplicationStorage _appState;
        public static string logRoot;
        private static string _key_For_DictMap = "Key_For_DictMap";
        private static string _key_For_FuncMap = "Key_For_FuncMap";
        private static string _key_For_FuncTree = "Key_For_FuncTree";

        public static String LogRoot
        {
            get
            {
                if (logRoot != null) return logRoot;

                if (logRoot == null)
                {
                    String logroot = _appState.GetAppSetting("LogRoot");
                    if (!string.IsNullOrEmpty(logroot))
                    {
                        logRoot = _appState.GetRealPath(logroot);
                    }
                }
                return logRoot;
            }
            set
            {
                logRoot = value;
            }
        }

        //db dictionary items map
        public static Dictionary<string, string> DictMap 
        {
            get
            {
                if (_appState == null) throw new Exception("Util has not been initiated yet!");
                if (_appState.Get(_key_For_DictMap) == null)
                {
                    //load dict items from db and keep the map in memory
                    var list = UtilQuery.StatelessGetBySql("select isnull(a.ConstName+'.'+b.ConstName,''),b.ItemName from GN_DictItem b inner join GN_Dict a on b.DictID=a.ID");
                    _appState.Set(_key_For_DictMap, list.ToDictionary(o => o[0] as string, o => o[1] as string));


                    if (_appState.Get(_key_For_DictMap) == null)
                        _appState.Set(_key_For_DictMap, new Dictionary<string, string>());

                    //add any extra mapping 
                    var map = _appState.Get(_key_For_DictMap) as Dictionary<string, string>;
                    map["ROV"] = "R.O.V";
                    map["Undefined"] = "";
                }

                return _appState.Get(_key_For_DictMap) as Dictionary<string, string>;
            }
        }
        //db func map
        public static Dictionary<string, int> FuncMap
        {
            get
            {
                if (_appState == null) throw new Exception("Util has not been initiated yet!");
                if (_appState.Get(_key_For_FuncMap) == null)
                {
                    //load from db and keep the map in memory
                    var list = UtilQuery.StatelessGetBySql("select Code,Id from GN_Func");
                    _appState.Set(_key_For_FuncMap,list.ToDictionary(o => o[0] as string, o => (int)o[1]));


                    if (_appState.Get(_key_For_FuncMap) == null)
                        _appState.Set(_key_For_FuncMap, new Dictionary<string, int>());
                }

                return _appState.Get(_key_For_FuncMap) as Dictionary<string, int>;
            }
        }
        //generate html for func checkboxtree once 
        public static string FuncTree
        {
            get
            {
                if (_appState == null) throw new Exception("Util has not been initiated yet!");
                if (_appState.Get(_key_For_FuncTree) == null)
                {
                    //load from db and keep the tree in memory
                    var list = UtilQuery.StatelessGetBySql("select Id,Level,Code,Name from GN_Func Order by Level");
                    String html0 = "<div class='row' pb-init-var='d' pb-init-data='{";
                    String html = "";
                    int activelevellength = 2;
                    String ids = "";
                    String levels = "";
                    for (var i = 0; i < list.Count;i++)
                    {
                        ids = ids + "," + list[i][0];
                        var currentlevel =(string)list[i][1];
                        levels = levels + ",\"" + currentlevel+"\"";
                        var nextlevel="00";
                        if (i + 1 < list.Count)
                        {
                            nextlevel = (string) list[i + 1][1];
                        }
                        if (currentlevel.Length == 2)
                        {
                            if (i > 0 && ((String)list[i - 1][1]).Length != 2)
                            {
                                html = html + "</ul></div>";
                            }
                            html = html + "<div class='panel panel-default col-md-3'><ul style='list-style:none'>";
                        }
                        String omg = "d.isCollapsed[\"" + currentlevel + "\"]";
                        String lihtml = "";
                        if (nextlevel.Length > currentlevel.Length)
                        {
                            lihtml = "<li  style='list-style:none'><span class='glyphicon' ng-class='" + omg + "?\"glyphicon-plus\":\"glyphicon-minus\"' ng-click='" + omg + "=!" + omg + "'></span>";
                        }
                        else
                        {
                            lihtml = "<li style='list-style:none'>";
                        }
                        if (currentlevel.Length>2 && currentlevel.Length > activelevellength)
                        {
                            String parentlevel = currentlevel.Substring(0, currentlevel.Length - 2);
                            html = html + "<ul style='list-style:none' uib-collapse='d.isCollapsed[\"" + parentlevel + "\"]'>";
                            html = html + lihtml +"<input type='checkbox' ng-change='d.chk_change(\"" + currentlevel + "\")' ng-disabled='disabled||d.disableList[" + i + "]' ng-model='d.selectedList[" + i + "]' ><label>" +
                                   list[i][3] + "</label>";
                            activelevellength = activelevellength + 2;
                        }
                        else if (currentlevel.Length == activelevellength || currentlevel.Length==2)
                        {
                            html = html + lihtml + "<input type='checkbox' ng-change='d.chk_change(\"" + currentlevel + "\")' ng-disabled='disabled||d.disableList[" + i + "]' ng-model='d.selectedList[" + i + "]' ><label>" +
                                   list[i][3] + "</label>";
                        }
                        if (nextlevel.Length < activelevellength)
                        {
                            var times = (activelevellength - nextlevel.Length) / 2;//两位一级
                            if (nextlevel.Length == 2) times = times - 1;
                            for (var j = 0; j <= times;j++ )
                                html = html + "</ul>";
                            activelevellength = activelevellength-(times+1) * 2;
                        }
                    }
                    html = html + "</div>";
                    html = html0 +"\"funcList\":["+ids.Substring(1) + "],"+"\"levelList\":["+levels.Substring(1) + "]}'>\n" + html;
                    _appState.Set(_key_For_FuncTree,html);
                }

                return _appState.Get(_key_For_FuncTree) as string;
            }
        }
        public static void InitStorage(IApplicationStorage appState)
        {
            _appState = appState;
        }

        public static void AddLog(string psSource, Exception ex)
        {
            var sMessage = GetExceptionMsg(ex);

            var sFileName = LogRoot + "\\" + DateTime.Now.ToString("yyyy-MM-dd") + ".log";

            System.IO.TextWriter oWriter;
            if (System.IO.File.Exists(sFileName))
            {
                oWriter = System.IO.File.AppendText(sFileName);
            }
            else
            {
                var oFileInfo = new System.IO.FileInfo(sFileName);
                oWriter = oFileInfo.CreateText();
            }
            oWriter.WriteLine("Source:" + psSource);
            oWriter.WriteLine("Time:" + DateTime.Now);
            if (ex != null)
            {
                oWriter.WriteLine("StackTrace:" + ex.StackTrace);
                oWriter.WriteLine("Message:" + sMessage);
            }
            oWriter.WriteLine("----------------------------------");
            oWriter.Close();
        }
        public static void AddLog(string psSource)
        {
            AddLog(psSource, null);
        }

        public static ISession CurrentSession()
        {
            return (new Util()).Session;
        }

        public static string GetExceptionMsg(Exception ex)
        {
            if (ex == null) return null;
            var sMsg = ex.InnerException != null ? GetExceptionMsg(ex.InnerException) : ex.ToString();
            
            sMsg = sMsg + "\r\n" + ex.Message;
            return sMsg;
        }
        public static bool IsNullableType(Type theType)
        {
            return theType.IsGenericType && theType.GetGenericTypeDefinition() == typeof (Nullable<>);
        }
        public static string DesEncrypt(string pToEncrypt, string desKey)
        {
            var des = new DESCryptoServiceProvider();
            var inputByteArray = Encoding.UTF8.GetBytes(pToEncrypt);
            des.Key = ASCIIEncoding.ASCII.GetBytes(desKey);
            des.IV = ASCIIEncoding.ASCII.GetBytes(desKey);
            var ms = new MemoryStream();
            var cs = new CryptoStream(ms, des.CreateEncryptor(), CryptoStreamMode.Write);
            cs.Write(inputByteArray, 0, inputByteArray.Length);
            cs.FlushFinalBlock();
            cs.Close();

            string str = Convert.ToBase64String(ms.ToArray());
            ms.Close();
            return str;

        }
        public static string DesDecrypt(string pToDecrypt, string desKey)
        {
            byte[] inputByteArray = Convert.FromBase64String(pToDecrypt);

            var des = new DESCryptoServiceProvider();
            des.Key = ASCIIEncoding.ASCII.GetBytes(desKey);
            des.IV = ASCIIEncoding.ASCII.GetBytes(desKey);
            var ms = new MemoryStream();
            var cs = new CryptoStream(ms, des.CreateDecryptor(), CryptoStreamMode.Write);
            cs.Write(inputByteArray, 0, inputByteArray.Length);
            cs.FlushFinalBlock();
            cs.Close();
            var s = Encoding.UTF8.GetString(ms.ToArray());
            ms.Close();
            return s;
        }
        public static bool IsNull(BaseDomainObject entity)
        {
            return entity == null || entity.Id == null;
        }
        public static bool IsNull(DORef doRef)
        {
            return doRef == null || doRef.Id == null;
        }
        public static bool IsNotNull(BaseDomainObject entity)
        {
            return entity != null && entity.Id != null;
        }
        public static bool IsNotNull(DORef doRef)
        {
            return doRef != null && doRef.Id != null;
        }
        public static string NgControllerJs()
        {
            if (!HttpContext.Current.IsDebuggingEnabled) return "";
            var webroot = HttpContext.Current.Server.MapPath("~/");
            var fileNames=Directory.EnumerateFiles(webroot, "*Ctrl.js", SearchOption.AllDirectories);
            var scripts = fileNames.Aggregate("", (aggregate, fileName) =>
                                  {
                                      var content = File.ReadAllText(fileName);
                                      return aggregate + content + "\r\n";
                                  });
            fileNames = Directory.EnumerateFiles(webroot + "/Shared/Directive/", "*.js", SearchOption.AllDirectories);
            scripts = fileNames.Aggregate(scripts, (aggregate, fileName) =>
            {
                var content = File.ReadAllText(fileName);
                return aggregate + content + "\r\n";
            });
            return scripts;
        }
        public static IDictionary<string, object> Obj2QsDict(object obj, IDictionary<string, object> map = null, String enclosingPropName = "", Type objType = null)
        {
            if (map == null) map = new Dictionary<string, object>();

            var type = objType != null ? objType : obj.GetType();
            var typedef = type.IsGenericType ? type.GetGenericTypeDefinition():null;
            if (obj == null)
            {
                map.Add(enclosingPropName, "");
            }
            else if (type.IsValueType || type == typeof(System.String))
            {
                map.Add(enclosingPropName, obj.ToString());
            }
            else if (type.IsArray)
            {
                var array = (Array)obj;
                if (array.GetLength(0) == 0)
                {
                    map.Add(enclosingPropName, "");
                }
                else
                {
                    for (var i = 0; i < array.GetLength(0); i++)
                    {
                        Obj2QsDict(array.GetValue(i), map, enclosingPropName + "[" + i + "]");
                    }
                }
            }
            else if (type == typeof(IList) || type.GetInterfaces().Contains(typeof(IList)) || (typedef!=null && (typedef==typeof(IList<>) || typedef.GetInterfaces().Contains(typeof(IList)))))
            {
                var list = (IList)obj;
                if (list.Count == 0)
                {
                    map.Add(enclosingPropName, "");
                }
                else
                {
                    for (var i = 0; i < list.Count; i++)
                    {
                        Obj2QsDict(list[i], map, enclosingPropName + "[" + i + "]");
                    }
                }
            }
            else if (type == typeof(IDictionary) || type.GetInterfaces().Contains(typeof(IDictionary)) || (typedef != null && (typedef == typeof(IDictionary<,>) || typedef.GetInterfaces().Contains(typeof(IDictionary)))))
            {
                var dict = (IDictionary)obj;
                foreach (var key in dict.Keys)
                {
                    Obj2QsDict(dict[key], map, enclosingPropName + "[" + key + "]");
                }
            }
            else
            {
                var props = obj.GetType().GetProperties();
                Array.ForEach(props, p =>
                {
                    var pname = (enclosingPropName == "" ? "" : (enclosingPropName + ".")) +
                                p.Name;
                    Obj2QsDict(p.GetValue(obj, null), map, pname, p.PropertyType);
                });
            }


            return map;

        }
        public static String Obj2Qs(object obj)
        {
            var map = Obj2QsDict(obj);
            var qs = "";
            foreach (var name in map.Keys)
            {
                qs = qs + "&" + name + "=" + map[name];
            }
            return qs.Substring(1);
        }
        public static String test()
        {
            IList<int> ee = new System.Collections.Generic.List<int>() {100, 200};
            var t = ProjectBase.Utils.Util.Obj2Qs(new
            {
                a1=new System.Collections.Generic.List<int>() {1, 2},
                a = new int[] { 1, 2, 3 },
                b = 10,
                c = new ArrayList() { 10, 20 },
                e =ee, 
                d = new Hashtable() { { "key1", "value1" }, { "key2", "value2" }, { "key3", new int[] { 4, 5, 6 } } },
                f = (IDictionary<String, Object>)new System.Collections.Generic.Dictionary<String, Object>() { { "key9", "value1" }, { "key29", "value2" }, { "key39", new int[] { 4, 5, 6 } } },
                Input = new { User = new Util() },
                Depts = new Util[] { new Util(), new Util(), new Util() }
            });
            return t;
        }
    }

 
}