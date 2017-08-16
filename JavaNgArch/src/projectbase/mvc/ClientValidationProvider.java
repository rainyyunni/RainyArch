
package projectbase.mvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import projectbase.mvc.validation.ModelClientValidationRangeRule;
import projectbase.mvc.validation.ModelClientValidationRegexRule;
import projectbase.mvc.validation.ModelClientValidationRequiredRule;
import projectbase.mvc.validation.ModelClientValidationRule;
import projectbase.mvc.validation.ModelClientValidationStringLengthRule;
import projectbase.utils.Res;

    public class ClientValidationProvider 
    {

        private static final List<Class<?>> _numericTypes = Arrays.asList(
        		byte.class,
        		short.class,
        		int.class,
        		long.class,
        		float.class,
        		double.class,
        		Byte.class,
        		Short.class,
        		Integer.class,
        		Long.class,
        		Float.class,
        		Double.class,
        		BigDecimal.class
        	);
        private static final List<Class<?>> _integralTypes = Arrays.asList(
        		byte.class,
        		short.class,
        		int.class,
        		long.class,
        		Byte.class,
        		Short.class,
        		Integer.class,
        		Long.class
        	);

        private static ClientValidationProvider current;
        public static int DefaultDecimalPrecision = 18;
        public static int DefaultDecimalScale = 2;
       
    	public static ClientValidationProvider getCurrent() {
    		return current;
    	}
    	public static ClientValidationProvider setCurrent(ClientValidationProvider value) {
    		return current=value;
    	}

        public Iterable<ModelClientValidationRule> GetValidationRules(ModelMetadata metadata)
        {
        	ArrayList<ModelClientValidationRule> rules=new ArrayList<ModelClientValidationRule>();
        	//client datatype validation
        	Class<?> type = metadata.getModelType();
            if (IsNumericType(type))
            {
            	rules=(ArrayList<ModelClientValidationRule>)GetNumericClientValidationRules(metadata);
            }
            if (metadata.getModelType() == Date.class)
            {
            	rules=(ArrayList<ModelClientValidationRule>)GetDateClientValidationRules(metadata);
            }

            String msg=null;
            //annotated bean validation
            if(metadata.getIsRequired()){
            	NotNull attr=metadata.getPropertyGetter().getAnnotation(NotNull.class);
                if(attr!=null){
                	msg=attr.message();
                }
                ModelClientValidationRequiredRule rule=new ModelClientValidationRequiredRule(msg, metadata.getDisplayName());
            	rules.add(rule);
            }
        	Size sizeAttr=metadata.getPropertyGetter().getAnnotation(Size.class);
        	Length lenAttr=metadata.getPropertyGetter().getAnnotation(Length.class);
            if(sizeAttr!=null){
            	msg=sizeAttr.message();
            	rules.add(new ModelClientValidationStringLengthRule(msg,sizeAttr.min(),sizeAttr.max(),metadata.getDisplayName()));
            }else if(lenAttr!=null){
            	msg=lenAttr.message();
            	rules.add(new ModelClientValidationStringLengthRule(msg,lenAttr.min(),lenAttr.max(),metadata.getDisplayName()));
            }

        	Range rangeAttr=metadata.getPropertyGetter().getAnnotation(Range.class);
        	Max maxAttr=metadata.getPropertyGetter().getAnnotation(Max.class);
        	Min minAttr=metadata.getPropertyGetter().getAnnotation(Min.class);
            if(rangeAttr!=null){
            	msg=rangeAttr.message();
            	rules.add(new ModelClientValidationRangeRule(msg,rangeAttr.min(),rangeAttr.max(),metadata.getDisplayName()));
            }else {
            	if(maxAttr!=null && minAttr!=null ){
            		msg=minAttr.message()+maxAttr.message();
            		rules.add(new ModelClientValidationRangeRule(msg,minAttr.value(),maxAttr.value(),metadata.getDisplayName()));
            	}else if(maxAttr!=null){
            		msg=maxAttr.message();
            		rules.add(new ModelClientValidationRangeRule(msg,Long.MIN_VALUE,maxAttr.value(),metadata.getDisplayName()));
            	}else if(minAttr!=null){
            		msg=minAttr.message();
            		rules.add(new ModelClientValidationRangeRule(msg,minAttr.value(),Long.MAX_VALUE,metadata.getDisplayName()));
            	}
            }
            
            Pattern regexAttr=metadata.getPropertyGetter().getAnnotation(Pattern.class);
            if(regexAttr!=null){
            	msg=regexAttr.message();
            	rules.add(new ModelClientValidationRegexRule(msg,regexAttr.regexp(),metadata.getDisplayName()));
        	}
            return rules;
        }

        private boolean IsNumericType(Class<?> type)
        {
        	//Class<?> underlyingType = type.; // strip off the Nullable<>
            return _numericTypes.contains(type);
        }
        private boolean IsIntegralType(Class<?> type)
        {
            //Type underlyingType = Nullable.GetUnderlyingType(type); // strip off the Nullable<>
            return _integralTypes.contains(type);
        }
        private boolean IsNotNullType(Class<?> type)
        {

            return _integralTypes.contains(type);
        }

            public Iterable<ModelClientValidationRule> GetNumericClientValidationRules(ModelMetadata metadata)
            {
                ModelClientValidationRule rule = null;
                String errorMessage;
                long min=Long.MIN_VALUE ,max=Long.MAX_VALUE ;


                if (metadata.getContainerType() != null)
                {
                    //add default range for integral number
                    if (IsIntegralType(metadata.getModelType()) 
                        &&  metadata.getPropertyGetter().getAnnotation(Range.class)==null
                        		&&  metadata.getPropertyGetter().getAnnotation(Max.class)==null
                        				&&  metadata.getPropertyGetter().getAnnotation(Min.class)==null)
                    {
                        if (metadata.getModelType() == Long.class)
                        {
                            min = Long.MIN_VALUE;
                            max = Long.MAX_VALUE;
                        }
                        else if (metadata.getModelType() == Integer.class)
                        {
                            min = Integer.MIN_VALUE;
                            max = Integer.MAX_VALUE;
                        }
                        else if (metadata.getModelType() == Short.class)
                        {
                            min = Short.MIN_VALUE;
                            max = Short.MAX_VALUE;
                        }

                        rule = new ModelClientValidationRangeRule(null, min, max,metadata.getDisplayName());
                    }


                    //add default edit format for decimal
                    if (metadata.getModelType() == BigDecimal.class)
                    {
                        int precision = DefaultDecimalPrecision;
                        int scale = DefaultDecimalScale;

                        Digits deciamlformatAttr=metadata.getPropertyGetter().getAnnotation(Digits.class);
                        if (deciamlformatAttr!=null){
                            precision = deciamlformatAttr.integer()+deciamlformatAttr.fraction();
                            scale=deciamlformatAttr.fraction();
                        }

                        String rex = "^(\\d{1," + (precision - scale) + "})(\\.\\d{1," + scale + "})?$";
                        errorMessage = Res.ValidationMessages("NumericModelValidator_DecimalFormatError",
                                                       metadata.getDisplayName(), precision - scale, scale);
                        rule = new ModelClientValidationRegexRule(errorMessage, rex);
                    }
                }
                ModelClientValidationRule rule0 = new ModelClientValidationRule();
                rule0.setValidationType("number");
                errorMessage = Res.ValidationMessages("NumericModelValidator_FormatError",
                        metadata.getDisplayName());
                rule0.setErrorMessage(errorMessage);
                ArrayList<ModelClientValidationRule> rules=new ArrayList<ModelClientValidationRule>();
                if (rule==null){
                	rules.add(rule0);
                }else{
                	rules.add(rule0);
                	rules.add(rule);
                }
                return rules;
            }



            public Iterable<ModelClientValidationRule> GetDateClientValidationRules(ModelMetadata metadata)
            {
                ModelClientValidationRule rule = new ModelClientValidationRule();
                rule.setValidationType("date");
                rule.setErrorMessage(Res.ValidationMessages("field.date",metadata.getDisplayName()));
                ArrayList<ModelClientValidationRule> rules=new ArrayList<ModelClientValidationRule>();
                rules.add(rule);
                return rules;
            }


    }



