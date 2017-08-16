package projectbase.domain;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
    /// <summary>
    /// 
    /// </summary>
	@MappedSuperclass
    public abstract class BaseDomainObject  extends  BaseDomainObjectWithTypedId<Integer>
    {
    	/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private Integer _id;
    	@Id
    	@Column(name="ID")
        @GeneratedValue(strategy=GenerationType.IDENTITY)
        public Integer getId(){
        	return _id;
        }
        public void setId(Integer id){
        	_id=id;
        }
    }


