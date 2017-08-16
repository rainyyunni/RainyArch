package javangarch.domain.bdinterface.dictenum;
import projectbase.domain.DictEnum;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

public class Task_StatusEnum extends DictEnum implements Converter<Integer,Task_StatusEnum>{
	
	public static final int Default=0;
	public static final int Done=1;
	public static final int Closed=2;
	public static final int Canceled=3;

	
	@Override
	public Task_StatusEnum convert(
			MappingContext<Integer, Task_StatusEnum> context) {
		Task_StatusEnum d= new Task_StatusEnum();
		d.setIntValue((Integer)context.getSource());
		return d;
	}

}




