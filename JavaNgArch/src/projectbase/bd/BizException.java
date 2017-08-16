package projectbase.bd;

import projectbase.utils.IErrorForUser;
    public class BizException extends Exception implements IErrorForUser
    {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public BizException(){}
        public BizException(String msg)  { super(msg);}

    }

