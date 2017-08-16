package projectbase.domain;

    public class DORef<T extends  BaseDomainObjectWithTypedId<Integer> > 
    {
    	private DORefValue refValue;
        public T ToReferencedDO(ICommonBD<T> bd)
        {
            return bd.Get(refValue.getId());
        }

        public DORef()
        {
        	refValue=new DORefValue();
        }
        public DORef(int id)
        {
        	refValue=new DORefValue();
        	refValue.setId (id);
        }
        public DORef(int id,String refText)
        {
        	refValue=new DORefValue();
        	refValue.setId (id);
        	refValue.setRefText(refText);
        }
        public DORef(DORefValue value)
        {
        	refValue=value;
        }
        public DORef(T entity)
        {
        	refValue=new DORefValue();
        	refValue.setId (entity.getId());
        	refValue.setRefText(entity.getRefText());
        }
        public Integer getId() {
			return refValue.getId();
		}
		public void setId(Integer value) {
			refValue.setId(value);;
		}
		public String getRefText() {
			return refValue.getRefText();
		}
		public void setRefText(String value) {
			refValue.setRefText(value); 
		}
        
    }


