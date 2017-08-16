package projectbase.mvc;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

import projectbase.data.hibernatemapbycode.convention.DOClassAnnotationAppender;
import projectbase.utils.JavaArchException;
import projectbase.utils.ProjectHierarchy;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.LoaderClassPath;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.ArrayMemberValue;
import javassist.bytecode.annotation.EnumMemberValue;
import javassist.bytecode.annotation.MemberValue;
import javassist.bytecode.annotation.StringMemberValue;

public class AnnotatingConvention {
	private static Logger logger=org.apache.log4j.Logger.getLogger(AnnotatingConvention.class);
	private ClassPool pool;
	private CtClass baseControllerClass = null;
	private CtClass actionResultClass = null;

	public AnnotatingConvention() {
		pool = ClassPool.getDefault();
		pool.insertClassPath(new LoaderClassPath(AnnotatingConvention.class
				.getClassLoader()));

		try {
			baseControllerClass = pool.get("projectbase.mvc.BaseController");
			actionResultClass= pool.get("projectbase.mvc.result.IActionResult");
		} catch (NotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	public Class<? extends BaseController> GetAnnotatedClass(String className)
			throws NotFoundException {
		CtClass cClass = null;
		try {
			cClass = pool.get(className);
		} catch (NotFoundException e) {
			throw new RuntimeException(e);
		}

		ClassFile cf = cClass.getClassFile();
		ConstPool cp = cf.getConstPool();

		// add @Controller/@RequestMapping:
		AnnotationsAttribute attr = (AnnotationsAttribute) cf
				.getAttribute(AnnotationsAttribute.visibleTag);
		Annotation an = null;
		if (attr == null) {
			attr = new AnnotationsAttribute(cp, AnnotationsAttribute.visibleTag);
		}
		an=attr.getAnnotation("org.springframework.stereotype.Controller");
		if(an==null){
			an = new Annotation("org.springframework.stereotype.Controller", cp);
			attr.addAnnotation(an);
			an = new Annotation("org.springframework.web.bind.annotation.RequestMapping", cp);
			MemberValue mv=an.getMemberValue("value");
			if(mv==null){
				ArrayMemberValue amv=new ArrayMemberValue(cp);
				String s=StringUtils.removeStart(cClass.getPackageName(), ProjectHierarchy.getMvcNS()+".")+"."+cClass.getSimpleName();
				s=StringUtils.removeEnd(s,"Controller").replace(".","/");
				StringMemberValue sm=new StringMemberValue(s,cp);
				StringMemberValue[] ss={sm};
				amv.setValue(ss);
				an.addMemberValue("value", amv);
			}
			attr.addAnnotation(an);
			cf.addAttribute(attr);
		}

		MethodInfo methodInfo = null;
		CtMethod[] methods = cClass.getDeclaredMethods();
		for (CtMethod cm : methods) {
			if(!Modifier.isPublic(cm.getModifiers())) continue;
			CtClass rtype=cm.getReturnType();
			if(!(rtype.subtypeOf(actionResultClass) || rtype.isPrimitive())) continue;
			
			try {
				Auth auth=(Auth)cm.getAnnotation(Auth.class);
				if(auth==null)
					logger.debug("select '"+StringUtils.removeEnd(cClass.getSimpleName(),"Controller")+"."+cm.getName()+"',null union");
				else
					logger.debug("select '"+StringUtils.removeEnd(cClass.getSimpleName(),"Controller")+"."+cm.getName()+"','"+auth.value()+"' union");
			} catch (ClassNotFoundException e) {

			}
			methodInfo = cm.getMethodInfo();
			attr = (AnnotationsAttribute) methodInfo
					.getAttribute(AnnotationsAttribute.visibleTag);
			if (attr == null) {
				attr = new AnnotationsAttribute(cp,
						AnnotationsAttribute.visibleTag);
			}
			//add RequestMapping(value="methodname",method=)
			an=attr.getAnnotation("org.springframework.web.bind.annotation.RequestMapping");
			if (an==null){
				an = new Annotation("org.springframework.web.bind.annotation.RequestMapping", cp);
			}
			MemberValue mv=an.getMemberValue("value");
			if(mv==null){
				ArrayMemberValue amv=new ArrayMemberValue(cp);
				StringMemberValue s=new StringMemberValue(cm.getName(),cp);
				StringMemberValue[] ss={s};
				amv.setValue(ss);
				an.addMemberValue("value", amv);
			}
			mv=an.getMemberValue("method");
			if(mv==null){
				ArrayMemberValue amv=new ArrayMemberValue(cp);
				EnumMemberValue emv1=new EnumMemberValue(cp);
				emv1.setType("org.springframework.web.bind.annotation.RequestMethod");
				emv1.setValue("GET");
				EnumMemberValue emv2=new EnumMemberValue(cp);
				emv2.setType("org.springframework.web.bind.annotation.RequestMethod");
				emv2.setValue("POST");
				EnumMemberValue[] emvs={emv1,emv2};
				amv.setValue(emvs);
				an.addMemberValue("method", amv);
			}
			attr.addAnnotation(an);
			methodInfo.addAttribute(attr);

			try {
				methodInfo.rebuildStackMap(pool);
			} catch (BadBytecode e1) {
				e1.printStackTrace();
			}
		}
		Class<? extends BaseController> result = null;
		try {
			result = cClass.toClass();
		} catch (Exception e) {
			throw new JavaArchException(e);
		}
		return result;
	}

}
