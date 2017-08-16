package projectbase.data.hibernatemapbycode.convention;

import java.util.Arrays;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import org.springframework.util.StringUtils;

import projectbase.domain.BaseDomainObjectWithTypedId;
import projectbase.domain.RefText;
import projectbase.sharparch.domain.domainmodel.DomainSignature;
import projectbase.utils.InvalidOperationException;
import projectbase.utils.JavaArchException;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.LoaderClassPath;
import javassist.NotFoundException;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.BooleanMemberValue;
import javassist.bytecode.annotation.StringMemberValue;

public class AnnotatingConvention {
	private static Logger logger=org.apache.log4j.Logger.getLogger(DOClassAnnotationAppender.class);
	private ClassPool pool;
	private CtClass baseDOClass = null;
	private CtClass baseCollectionClass = null;
	private CtClass stringClass = null;

	public AnnotatingConvention() {
		pool = ClassPool.getDefault();
		pool.insertClassPath(new LoaderClassPath(AnnotatingConvention.class
				.getClassLoader()));

		try {
			baseDOClass = pool.get("projectbase.domain.BaseDomainObjectWithTypedId");
			baseCollectionClass = pool.get("java.util.Collection");
			stringClass=pool.get("java.lang.String");
		} catch (NotFoundException e) {
			throw new JavaArchException(e);
		}
	}

	public Class<? extends BaseDomainObjectWithTypedId<Integer>> GetAnnotatedClass(String className)
			throws NotFoundException, ClassNotFoundException {
		logger.debug("------------"+className);
		CtClass cClass = null;
		cClass = pool.get(className);
		if(!cClass.subclassOf(baseDOClass))return null;

		ClassFile cf = cClass.getClassFile();
		ConstPool cp = cf.getConstPool();

		// add @Entity:
		AnnotationsAttribute attr = (AnnotationsAttribute) cf
				.getAttribute(AnnotationsAttribute.visibleTag);
		Annotation an = null;
		if (attr == null) {
			attr = new AnnotationsAttribute(cp, AnnotationsAttribute.visibleTag);
			an = new Annotation("javax.persistence.Entity", cp);
			attr.addAnnotation(an);
			cf.addAttribute(attr);
		}

		MethodInfo methodInfo = null;
		CtMethod[] methods = cClass.getDeclaredMethods();

		for (CtMethod cm : methods) {
			boolean isReference = cm.getReturnType().subclassOf(baseDOClass);
			boolean isCollection = cm.getReturnType().subtypeOf(
					baseCollectionClass);
			if (!isReference && !isCollection)
				continue;

			methodInfo = cm.getMethodInfo();
			attr = (AnnotationsAttribute) methodInfo
					.getAttribute(AnnotationsAttribute.visibleTag);
			if (attr == null) {
				attr = new AnnotationsAttribute(cp,
						AnnotationsAttribute.visibleTag);
			}
			boolean noMappingAnnotaion=NoMappingAnnotaion(cm.getAnnotations());
			// add ManyToOne(optional=false)
			if (isReference && noMappingAnnotaion){
				an = new Annotation("javax.persistence.ManyToOne", cp);
				CtClass referencedClass = cm.getReturnType();
				if(cm.getName().equals("get"+referencedClass.getSimpleName())){
					try {
						String name = cClass.getSimpleName();
						CtMethod m=referencedClass.getDeclaredMethod("get" + name+"s");
						if(m.getAnnotations().length==0){
							an.addMemberValue("optional", new BooleanMemberValue(false, cp));
						}
						logger.debug("ManyToOne(optional=false)--"+cm.getName());
					} catch (NotFoundException e) {
						// did not find the Many end of manytoone association,then optional=true as default is.
						logger.debug("ManyToOne--"+cm.getName());
					}
				}
				attr.addAnnotation(an);
				methodInfo.addAttribute(attr);
			}
			// add
			// OneToMany(mappedBy="xxx")和CollectionType(type="projectbase.domain.customcollection.DomainListType")
			if (isCollection && noMappingAnnotaion){
				an = new Annotation("javax.persistence.OneToMany", cp);
				String elementClassName = cm.getGenericSignature();
				elementClassName = elementClassName.substring(
						elementClassName.indexOf("<L") + 2,
						elementClassName.indexOf(";>")).replace("/", ".");
				CtClass eClass = pool.get(elementClassName);
				if (!eClass.subclassOf(baseDOClass))
					continue;
				try {
					String name = cClass.getSimpleName();
					eClass.getDeclaredMethod("get" + name);
					an.addMemberValue("mappedBy", new StringMemberValue(
							StringUtils.uncapitalize(name), cp));
					attr.addAnnotation(an);
					methodInfo.addAttribute(attr);
					logger.debug("OneToMany(mappedBy="+StringUtils.uncapitalize(name)+")--"+cm.getName());
				} catch (NotFoundException e) {
					// did not find the one end property of onetomany association
				}
			}
			if (isCollection && noMappingAnnotaion){
/*					&& attr.getAnnotation("javax.persistence.OneToMany") != null
					&& attr.getAnnotation("org.hibernate.annotations.CollectionType") == null) {*/
				an = new Annotation("org.hibernate.annotations.CollectionType",
						cp);
				an.addMemberValue("type", new StringMemberValue(
						"projectbase.domain.customcollection.DomainListType",
						cp));
				attr.addAnnotation(an);
				methodInfo.addAttribute(attr);
				logger.debug("CollectionType--"+cm.getName());
			}
			try {
				methodInfo.rebuildStackMap(pool);
			} catch (BadBytecode e1) {
				throw new JavaArchException(e1);
			}
		}
		//add RefText formula and transient getter GetRefTextFormula
		CtMethod m=null;
		try{
			m=cClass.getDeclaredMethod("GetRefTextFormula");
		}catch(NotFoundException e){
			m=null;
		}
		String[] src=null;
		if(m==null){
			src=BuildFormulaSrc(cClass);
			CtMethod overrideMethod;
			try {
				overrideMethod = CtNewMethod.make(
				         "public static String GetRefTextFormula() { return \""+src[0]+"\";}",
				         cClass);
				cClass.addMethod(overrideMethod);
			} catch (CannotCompileException e) {
				throw new InvalidOperationException(e.getMessage());
			}
			
		}
		try{
			m=cClass.getDeclaredMethod("getRefText");
		}catch(NotFoundException e){
			m=null;
		}
		if(m==null){
			CtMethod overrideMethod;
			try {
				overrideMethod = CtNewMethod.make(
				         "public String getRefText() { return "+src[1]+";}",
				         cClass);
				cClass.addMethod(overrideMethod);
			} catch (CannotCompileException e) {
				throw new InvalidOperationException(e.getMessage());
			}
			
			methodInfo = overrideMethod.getMethodInfo();
			attr = new AnnotationsAttribute(cp,
					AnnotationsAttribute.visibleTag);
			an = new Annotation("javax.persistence.Transient", cp);
			attr.addAnnotation(an);
			methodInfo.addAttribute(attr);
			try {
				methodInfo.rebuildStackMap(pool);
			} catch (BadBytecode e) {
				throw new JavaArchException(e);
			}
		}
		
		Class<? extends BaseDomainObjectWithTypedId<Integer>> result = null;
		try {
			result = cClass.toClass();
			org.apache.log4j.Logger.getLogger(DOClassAnnotationAppender.class)
					.log(Priority.DEBUG,
							"-------------------" + cClass.getName()
									+ cClass.isFrozen());
		} catch (Exception e) {
			throw new JavaArchException(e);
		}
		return result;
	}
	public String[]  BuildFormulaSrc(CtClass cClass) throws ClassNotFoundException, NotFoundException{
		String[] codes=new String[]{"",""};
	    for(CtField property:cClass.getDeclaredFields()){
	    	if(property.getType()!=stringClass)continue;
	    	if(property.getAnnotation(RefText.class)!=null){
	    		codes[0]= "this."+property.getName()+" as refText";
	    		codes[1]="get"+StringUtils.capitalize(property.getName())+"()";
	    		return codes;
	    	}
			else if (property.getAnnotation(DomainSignature.class)!=null){
				codes[0]=codes[0]+"this."+property.getName()+"+'-'+";
				codes[1]=codes[1]+"get"+StringUtils.capitalize(property.getName())+"()+\"-\"+";
			}
	    }
	    for(CtMethod property:cClass.getDeclaredMethods()){
	    	if(property.getReturnType()!=stringClass)continue;
	    	String propname=StringUtils.uncapitalize(property.getName().replaceFirst("get", ""));
	    	if(property.getAnnotation(RefText.class)!=null){
	    		codes[0]= "this."+propname+" as refText";
	    		codes[1]=property.getName()+"()";
	    		return codes;
	    	}
			else if (property.getAnnotation(DomainSignature.class)!=null){
				codes[0]=codes[0]+"this."+propname+"+'-'+";
				codes[1]=codes[1]+property.getName()+"()+\"-\"+";
			}
	    }
	    if(StringUtils.isEmpty(codes[0])) return new String[]{"this.id||'' as refText","getId().toString()"};
		//	throw new JavaArchException("Must define RefText, or DomainSignature on String props for a DomainObject class:"+cClass.getName());
	    codes[0]=codes[0].substring(0, codes[0].length() - 5)+" as refText";
	    codes[1]=codes[1].substring(0, codes[1].length() - 5);
	    return codes;
	}
	private boolean NoMappingAnnotaion(Object[] anotations){
		return Arrays.stream(anotations).noneMatch(o->o.toString().contains("persistence"));
	}
}
