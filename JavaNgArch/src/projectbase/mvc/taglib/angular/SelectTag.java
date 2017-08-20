package projectbase.mvc.taglib.angular;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.tags.form.TagWriter;

import projectbase.domain.DictEnum;
import projectbase.mvc.GlobalConstant;
import projectbase.mvc.ModelMetadata;
import projectbase.utils.ArgumentNullException;
import projectbase.utils.Util;


@SuppressWarnings("serial")
public class SelectTag extends org.springframework.web.servlet.tags.form.SelectTag implements BaseHtmlTag{
	private String name;
	private String optionLabel;
	private String selectList;
	public String getOptionLabel() {
		return optionLabel;
	}
	public void setOptionLabel(String value) {
		this.optionLabel = value;
	}
	public String getSelectList() {
		return selectList;
	}
	public void setSelectList(String value) {
		this.selectList = value;
	}
	protected String autogenerateId() throws JspException {
		return autogenerateId(super.autogenerateId());
	}
	@Override
	public String getName() throws JspException {
		//return StringUtils.isEmpty(name)?super.getName():name;
		return getName(name,getPropertyPath(),this.getDynamicAttributes());
	}

	public void setName(String name) {
		this.name = name;
	}
	@Override
	protected int writeTagContent(TagWriter tagWriter) throws JspException {
		tagWriter.startTag("select");

		AddClientValidation(pageContext,tagWriter,getPath());

		Class<?> modelclass=getBindStatus().getValueType();
		boolean isDictEnum=DictEnum.class.isAssignableFrom(modelclass);
		if (isDictEnum){
            String ngoptions = "value as label for (label, value) in " + GlobalConstant.DictJsName+"."+modelclass.getSimpleName();
            if(getDynamicAttributes()!=null && getDynamicAttributes().containsKey("pb-AddOptions")){
            	ngoptions = ngoptions + "|SelectAddOptions:" + (String)getDynamicAttributes().get("pb-AddOptions");;
            }
            tagWriter.writeAttribute("ng-options", ngoptions);
    		if(this.getDynamicAttributes()==null || this.getDynamicAttributes().get("ng-model")==null){
    			tagWriter.writeAttribute("ng-model", GetNgModel(pageContext,getPath()));
    		}
    		writeOptionalAttribute(tagWriter, "name", getName());
        }else{
        	String ngmodel="";
    		if(getDynamicAttributes()==null || getDynamicAttributes().get("ng-model")==null){
    			ngmodel=getPath();
    			tagWriter.writeAttribute("ng-model", GetNgModel(pageContext,getPath())+".id");
    			writeOptionalAttribute(tagWriter, "name", getName()+".id");
    		}else{
    			ngmodel=(String)getDynamicAttributes().get("ng-model");
    			writeOptionalAttribute(tagWriter, "name", getName());
    		}
        	if(getDynamicAttributes()==null ||(getDynamicAttributes()!=null && !getDynamicAttributes().containsKey("ng-options")))
	        {
	            if (selectList==null)
	            {
	            	throw new ArgumentNullException("ng-options or ngSelectList");
	            }
	            else
	            {
	                String ngoptions = "item.id as item.refText for item in " + selectList ;
	                if(getDynamicAttributes()!=null && getDynamicAttributes().containsKey("pb-EnforceMatch")){
	                	ngoptions = ngoptions + "|RefSelectEnforceMatch:" + ngmodel;
	                }
	                if(getDynamicAttributes()!=null && getDynamicAttributes().containsKey("pb-AddOptions")){
	                	ngoptions = ngoptions + "|SelectAddOptions:" + (String)getDynamicAttributes().get("pb-AddOptions");;
	                }
	                tagWriter.writeAttribute("ng-options", ngoptions);
	            }
	        }
        }
		writeOptionalAttribute(tagWriter, "id", resolveId()+(isDictEnum?"":"_id"));
		writeOptionalAttributes(tagWriter);
        if (optionLabel != null)
        {
        	tagWriter.startTag("option");
        	tagWriter.writeAttribute("value", "");
        	tagWriter.writeAttribute("translate", "");
        	tagWriter.appendValue(optionLabel);
			tagWriter.endTag(true);
        }
        tagWriter.endTag(true);;

		return SKIP_BODY;
	}


}
