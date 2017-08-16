package projectbase.data.hibernatemapbycode.convention;

import java.util.Arrays;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.cfg.naming.JpaNamingStrategyDelegate;
import org.hibernate.cfg.naming.NamingStrategyDelegate;
import org.hibernate.cfg.naming.NamingStrategyDelegator;
import org.hibernate.internal.util.StringHelper;

import projectbase.utils.JavaArchException;

public class NamingConvention extends JpaNamingStrategyDelegate implements
		NamingStrategyDelegator {
	private static Logger log=Logger.getLogger(NamingConvention.class);
	private static final long serialVersionUID = 1L;

	private static Map<String, String> _namespaceMapToTablePrefix;
	public static final NamingStrategyDelegator INSTANCE = new NamingConvention();

	public static Map<String, String> getNamespaceMapToTablePrefix() {
		return _namespaceMapToTablePrefix;
	}

	public static void setNamespaceMapToTablePrefix(
			Map<String, String> namespaceMapToTablePrefix) {
		_namespaceMapToTablePrefix = namespaceMapToTablePrefix;
	}

	@Override
	public NamingStrategyDelegate getNamingStrategyDelegate(boolean isHbm) {
		return (NamingStrategyDelegate) this;
	}

	/**
	 * Domain Object的类名与数据库表名映射
	 * 
	 * @param className
	 * @return 表名
	 */
	public String TableNameConvention(String className) {
		String ns = StringHelper.qualifier(className);
		String prefix = getNamespaceMapToTablePrefix().get(
				ns.substring(ns.lastIndexOf(".") + 1));
		String name = StringHelper.unqualify(className);
		if (prefix == null)
			throw new JavaArchException(
					"DomainObject's package has not been registered for table mapping:"
							+ ns + "." + name);
		return (prefix + name).toLowerCase();
	}

	/**
	 * 一个DO类中的属性引用另一个DO类与外键字段的映射
	 * @param propertyClassName
	 * @return 外键字段名
	 */
	public static String ReferenceConvention(String referencedColumnName) {
		return referencedColumnName + "ID";
	}

	/**
	 * 主表名
	 */
	@Override
	public String determineImplicitPrimaryTableName(String entityName,
			String jpaEntityName) {
		log.debug(Arrays.toString(new String[]{"determineImplicitPrimaryTableName",entityName,jpaEntityName,"--",TableNameConvention(entityName)}));
		return TableNameConvention(entityName);
	}
	/**
	 * 	外键字段名
	 */
	@Override
	public String determineImplicitEntityAssociationJoinColumnName(
			String propertyEntityName, String propertyJpaEntityName,
			String propertyTableName, String referencedColumnName,
			String referencingPropertyName) {
		log.debug(Arrays.toString(new String[]{"determineImplicitEntityAssociationJoinColumnName",propertyEntityName, propertyJpaEntityName,
				propertyTableName, referencedColumnName,
				referencingPropertyName,
				"--",super.determineImplicitEntityAssociationJoinColumnName(propertyEntityName, propertyJpaEntityName,
						propertyTableName, referencedColumnName,
						referencingPropertyName)}));
			return referencingPropertyName+referencedColumnName;
	}
	
	/**
	 * onetomany集合对应的外键表名
	 */
//	@Override
//	public String determineImplicitEntityAssociationJoinTableName(
//			String ownerEntityName, String ownerJpaEntityName,
//			String ownerEntityTable, String associatedEntityName,
//			String associatedJpaEntityName, String associatedEntityTable,
//			String propertyPath) {
//		log.debug(Arrays.toString(new String[]{"determineImplicitEntityAssociationJoinTableName",ownerEntityName,
//				ownerJpaEntityName, ownerEntityTable, associatedEntityName,
//				associatedJpaEntityName, associatedEntityTable, propertyPath,
//				"--",super.determineImplicitEntityAssociationJoinTableName(ownerEntityName,
//						ownerJpaEntityName, ownerEntityTable, associatedEntityName,
//						associatedJpaEntityName, associatedEntityTable, propertyPath),TableNameConvention(associatedEntityName)}));
//		return TableNameConvention(associatedEntityName);
//	}
	
/*	@Override
	public String determineImplicitElementCollectionTableName(
			String ownerEntityName, String ownerJpaEntityName,
			String ownerEntityTable, String propertyPath) {
		log.debug(Arrays.toString(new String[]{"determineImplicitElementCollectionTableName",ownerEntityName,
				ownerJpaEntityName, ownerEntityTable, propertyPath,
				"--",super.determineImplicitElementCollectionTableName(ownerEntityName,
						ownerJpaEntityName, ownerEntityTable, propertyPath)}));
		return super.determineImplicitElementCollectionTableName(ownerEntityName,
				ownerJpaEntityName, ownerEntityTable, propertyPath);
	}

	@Override
	public String determineImplicitElementCollectionJoinColumnName(
			String ownerEntityName, String ownerJpaEntityName,
			String ownerEntityTable, String referencedColumnName,
			String propertyPath) {
		log.debug(Arrays.toString(new String[]{"determineImplicitElementCollectionJoinColumnName",ownerEntityName,
				ownerJpaEntityName, ownerEntityTable, referencedColumnName,
				propertyPath,
				"--",super.determineImplicitElementCollectionJoinColumnName(ownerEntityName,
						ownerJpaEntityName, ownerEntityTable, referencedColumnName,
						propertyPath)}));
		return super.determineImplicitElementCollectionJoinColumnName(ownerEntityName,
				ownerJpaEntityName, ownerEntityTable, referencedColumnName,
				propertyPath);
	}

*/



//	@Override
//	public String determineImplicitPropertyColumnName(String propertyPath) {
//		log.debug(Arrays.toString(new String[]{"determineImplicitPropertyColumnName",propertyPath,
//				"--","Implicit"+super.determineImplicitPropertyColumnName(propertyPath)}));
//		return "Implicit"+super.determineImplicitPropertyColumnName(propertyPath);
//	}

//	@Override
//	public String toPhysicalTableName(String tableName) {
//		log.debug(Arrays.toString(new String[]{"toPhysicalTableName",tableName,
//				"--",super.toPhysicalTableName(tableName)}));
//		return "physical tablename";
//	}
//
//	@Override
//	public String toPhysicalColumnName(String columnName) {
//		log.debug(Arrays.toString(new String[]{"toPhysicalColumnName",columnName,
//				"--",super.toPhysicalColumnName(columnName)}));
//		return "physical colname";
//	}
//
//	@Override
//	public String toPhysicalJoinKeyColumnName(String joinedColumn,
//			String joinedTable) {
//		log.debug(Arrays.toString(new String[]{"toPhysicalJoinKeyColumnName",joinedColumn, joinedTable,
//				"--",super.toPhysicalJoinKeyColumnName(joinedColumn, joinedTable)}));
//		return super.toPhysicalJoinKeyColumnName(joinedColumn, joinedTable);
//	}


//	@Override
//	public String determineLogicalColumnName(String columnName,
//			String propertyName) {
//		log.debug(Arrays.toString(new String[]{"determineLogicalColumnName",columnName, propertyName,
//				"--",super.determineLogicalColumnName(columnName, propertyName)}));
//		return super.determineLogicalColumnName(columnName, propertyName);
//	}
//
//	@Override
//	public String determineLogicalElementCollectionTableName(String tableName,
//			String ownerEntityName, String ownerJpaEntityName,
//			String ownerEntityTable, String propertyName) {
//		log.debug(Arrays.toString(new String[]{"determineLogicalElementCollectionTableName",tableName,
//				ownerEntityName, ownerJpaEntityName, ownerEntityTable, propertyName,
//				"--",super.determineLogicalElementCollectionTableName(tableName,
//						ownerEntityName, ownerJpaEntityName, ownerEntityTable, propertyName)}));
//		return super.determineLogicalElementCollectionTableName(tableName,
//				ownerEntityName, ownerJpaEntityName, ownerEntityTable, propertyName);
//	}
//
//	@Override
//	public String determineLogicalEntityAssociationJoinTableName(
//			String tableName, String ownerEntityName,
//			String ownerJpaEntityName, String ownerEntityTable,
//			String associatedEntityName, String associatedJpaEntityName,
//			String associatedEntityTable, String propertyName) {
//		log.debug(Arrays.toString(new String[]{"determineLogicalEntityAssociationJoinTableName",tableName,
//				ownerEntityName, ownerJpaEntityName, ownerEntityTable,
//				associatedEntityName, associatedJpaEntityName, associatedEntityTable,
//				propertyName,
//				"--",super.determineLogicalEntityAssociationJoinTableName(tableName,
//						ownerEntityName, ownerJpaEntityName, ownerEntityTable,
//						associatedEntityName, associatedJpaEntityName, associatedEntityTable,
//						propertyName),associatedJpaEntityName}));
//		return associatedJpaEntityName;
//	}
//	@Override
//	public String determineLogicalCollectionColumnName(String columnName,
//			String propertyName, String referencedColumn) {
//		log.debug(Arrays.toString(new String[]{"determineLogicalCollectionColumnName",columnName, propertyName,
//				referencedColumn,
//				"--",super.determineLogicalCollectionColumnName(columnName, propertyName,
//						referencedColumn)}));
//		return super.determineLogicalCollectionColumnName(columnName, propertyName,
//				referencedColumn);
//	}
	



}
