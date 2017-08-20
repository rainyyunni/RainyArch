package projectbase.mvc.result;

import lombok.Getter;
import lombok.Setter;

public class RichClientJsonResult extends JsonResult {
    public static final String Command_Noop = "Noop";
	public static final String Command_Message="Message";
	public static final String Command_Redirect="Redirect";
    public static final String Command_AppPage = "AppPage";
	public static final String Command_ServerVM="ServerVM";
	public static final String Command_ServerData="ServerData";
	public RichClientJsonResult(boolean isRcResult,String command,Object data){
		Result r=new Result();
		r.isRcResult=isRcResult;
		r.command=command==null?Command_Noop:command;
		r.data=data;
		this.setData(r);
		setJsonRequestBehavior(JsonRequestBehavior.AllowGet);
	}
    public boolean IsErrorResult(){
    	if(this.getData()!=null && this.getData() instanceof RichClientJsonResult.Result )
        return !((RichClientJsonResult.Result)this.getData()).getIsRcResult();
    	return false;
    }
	@Getter@Setter
    public class Result{
    	private boolean isRcResult;
    	private String command;
    	private Object data;
    	public boolean getIsRcResult() {
    		return isRcResult;
    	}
    	public void setIsRcResult(boolean value) {
    		this.isRcResult = value;
    	}
    }

}
