
package projectbase.mvc;

import java.util.ArrayList;
import java.util.List;

import projectbase.utils.Pager;


    public class ListInput
    {
        public ListInput()
        {
            selectedValues=new ArrayList<Integer>();
        }
        public ListInput(int pageSize)
        {
            selectedValues = new ArrayList<Integer>();
            pager = new Pager(pageSize);
        }
        private List<Integer> selectedValues;
        private String orderExpression;
        private Pager pager;
		public List<Integer> getSelectedValues() {
			return selectedValues;
		}
		public void setSelectedValues(List<Integer> value) {
			this.selectedValues = value;
		}
		public String getOrderExpression() {
			return orderExpression;
		}
		public void setOrderExpression(String value) {
			this.orderExpression = value;
		}
		public Pager getPager() {
			return pager;
		}
		public void setPager(Pager value) {
			this.pager = value;
		}
    }


