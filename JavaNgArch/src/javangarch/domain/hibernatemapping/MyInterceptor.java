package javangarch.domain.hibernatemapping;
import org.hibernate.EmptyInterceptor;


    public class MyInterceptor  extends  EmptyInterceptor
    {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public String onPrepareStatement(String sql) {
			return super.onPrepareStatement(sql);
		}


    } 

