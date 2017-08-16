

package projectbase.mvc;

import projectbase.mvc.result.ActionResult;

    public class PreHandleResultException extends Exception
    {
        public PreHandleResultException(){super();}
        public PreHandleResultException(String msg){ super(msg) ; }
        public PreHandleResultException(ActionResult result){
        	this.result=result;
        }
        
        public ActionResult getResult() {
			return result;
		}
		public void setResult(ActionResult value) {
			this.result = value;
		}

		private ActionResult result;
    }

