
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

import projectbase.mvc.validation.angular.ModelClientValidationMaxRule;
import projectbase.mvc.validation.angular.ModelClientValidationMinRule;
import projectbase.mvc.validation.angular.ModelClientValidationRegexRule;
import projectbase.mvc.validation.angular.ModelClientValidationRequiredRule;
import projectbase.mvc.validation.angular.ModelClientValidationRule;
import projectbase.mvc.validation.angular.ModelClientValidationMaxLengthRule;
import projectbase.mvc.validation.angular.ModelClientValidationMinLengthRule;

    public class AngularClientValidationProvider 
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

        private static AngularClientValidationProvider current;
        public static int DefaultDecimalPrecision = 18;
        public static int DefaultDecimalScale = 2;
       
    	public static AngularClientValidationProvider getCurrent() {
    		return current;
    	}
    	public static AngularClientValidationProvider setCurrent(AngularClientValidationProvider value) {
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

            //annotated bean validation
            if(metadata.getIsRequired()){
                ModelClientValidationRequiredRule rule=new ModelClientValidationRequiredRule();
            	rules.add(rule);
            }
            Size sizeAttr=metadata.getField()==null?null:metadata.getField().getAnnotation(Size.class);
            if(sizeAttr==null)metadata.getPropertyGetter().getAnnotation(Size.class);
            Length lenAttr=metadata.getField()==null?null:metadata.getField().getAnnotation(Length.class);
            if(lenAttr==null)metadata.getPropertyGetter().getAnnotation(Length.class);
            if(sizeAttr!=null){
            	if(sizeAttr.min()>0)
            		rules.add(new ModelClientValidationMinLengthRule(sizeAttr.min()));
            	if(sizeAttr.max()>0)
            		rules.add(new ModelClientValidationMaxLengthRule(sizeAttr.max()));
            }else if(lenAttr!=null){
            	if(lenAttr.min()>0)
            		rules.add(new ModelClientValidationMinLengthRule(lenAttr.min()));
            	if(lenAttr.max()>0)
            		rules.add(new ModelClientValidationMaxLengthRule(lenAttr.max()));
            }

            Range rangeAttr=metadata.getField()==null?null:metadata.getField().getAnnotation(Range.class);
            if(rangeAttr==null)metadata.getPropertyGetter().getAnnotation(Range.class);
            Max maxAttr=metadata.getField()==null?null:metadata.getField().getAnnotation(Max.class);
            if(maxAttr==null)metadata.getPropertyGetter().getAnnotation(Max.class);
            Min minAttr=metadata.getField()==null?null:metadata.getField().getAnnotation(Min.class);
            if(minAttr==null)metadata.getPropertyGetter().getAnnotation(Min.class);
            if(rangeAttr!=null){
            	rules.add(new ModelClientValidationMinRule(rangeAttr.min()));
            	rules.add(new ModelClientValidationMaxRule(rangeAttr.max()));
            }else if(maxAttr!=null||minAttr!=null){
            	Long min=Long.MIN_VALUE;
            	Long max=Long.MAX_VALUE;
            	if(maxAttr!=null){
            		max=maxAttr.value();
            	}
            	if(minAttr!=null){
            		min=minAttr.value();
            	}
            	rules.add(new ModelClientValidationMinRule(min));
            	rules.add(new ModelClientValidationMaxRule(max));
            }
            
            Pattern regexAttr=metadata.getField()==null?null:metadata.getField().getAnnotation(Pattern.class);
            if(regexAttr==null)metadata.getPropertyGetter().getAnnotation(Pattern.class);
            if(regexAttr!=null){
            	rules.add(new ModelClientValidationRegexRule(regexAttr.regexp()));
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
                ArrayList<ModelClientValidationRule> rules=new ArrayList<ModelClientValidationRule>();
                long min=Long.MIN_VALUE ,max=Long.MAX_VALUE ;


                if (metadata.getContainerType() != null)
                {
                    //add default range for integral number
                    if (IsIntegralType(metadata.getModelType()) 
                    		&& (metadata.getField()==null|| metadata.getField().getAnnotation(Range.class)==null)
                    		&& (metadata.getField()==null|| metadata.getField().getAnnotation(Max.class)==null)
                    		&& (metadata.getField()==null|| metadata.getField().getAnnotation(Min.class)==null)
                    		&& metadata.getPropertyGetter().getAnnotation(Range.class)==null
                        	&& metadata.getPropertyGetter().getAnnotation(Max.class)==null
                        	&& metadata.getPropertyGetter().getAnnotation(Min.class)==null)
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

                    	rules.add(new ModelClientValidationMinRule(min));
                    	rules.add(new ModelClientValidationMaxRule(max));
                    }


                    //add default edit format for decimal
                    if (metadata.getModelType() == BigDecimal.class)
                    {
                        int precision = DefaultDecimalPrecision;
                        int scale = DefaultDecimalScale;

                        Digits deciamlformatAttr=metadata.getField()==null?null:metadata.getField().getAnnotation(Digits.class);
                        if(deciamlformatAttr==null)metadata.getPropertyGetter().getAnnotation(Digits.class);
                        if (deciamlformatAttr!=null){
                            precision = deciamlformatAttr.integer()+deciamlformatAttr.fraction();
                            scale=deciamlformatAttr.fraction();
                        }

                        String rex = "^(\\d{1," + (precision - scale) + "})(\\.\\d{1," + scale + "})?$";
                        rules.add(new ModelClientValidationRegexRule(rex));
                    }
                }
                ModelClientValidationRule rule0 = new ModelClientValidationRule();
                rule0.setValidationType("type");
                rule0.setValidationParameter("number");
                rules.add(rule0);
                return rules;
            }



            public Iterable<ModelClientValidationRule> GetDateClientValidationRules(ModelMetadata metadata)
            {
                ModelClientValidationRule rule = new ModelClientValidationRule();
                rule.setValidationType("date");
                rule.setValidationParameter("");
                ArrayList<ModelClientValidationRule> rules=new ArrayList<ModelClientValidationRule>();
                rules.add(rule);
                return rules;
            }


    }



