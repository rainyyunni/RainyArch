using System;
//using System.Data.Entity.Design.PluralizationServices;
using System.Collections;
using System.Globalization;
using System.Linq;
using NHibernate.Mapping.ByCode;
using System.Reflection;
using ProjectBase.Domain;
using ProjectBase.Utils;
using SharpArch.Domain.DomainModel;

namespace ProjectBase.Data.NHibernateMapByCode.Convention
{
    public static class NamingConventions
    {
        #region Fields 

        //private static readonly PluralizationService Service = PluralizationService.CreateService(CultureInfo.GetCultureInfo("en"));
        public static Hashtable NamespaceMapToTablePrefix { get; set; }

        #endregion Fields

        #region Methods 

        public static void ApplyNamingConventions(this ModelMapper mapper)
        {
            mapper.BeforeMapClass += (modelInspector, type, map) =>
                                         {
                                             TableNameConvention(type, map);
                                             //PluralizeEntityName(type, map);
                                             PrimaryKeyConvention(type, map);
            };
            mapper.BeforeMapManyToOne += ReferenceConvention;
            mapper.BeforeMapBag += MapBag;
            mapper.BeforeMapManyToMany += ManyToManyConvention;
            mapper.BeforeMapJoinedSubclass += MapJoinedSubclass;
            mapper.BeforeMapAny += MapAny;
            mapper.BeforeMapComponent += MapComponent;
            mapper.BeforeMapElement += MapElement;
            mapper.BeforeMapIdBag += MapIdBag;
            mapper.BeforeMapList += MapList;
            mapper.BeforeMapMap += MapMap;
            mapper.BeforeMapMapKey += MapMapKey;
            mapper.BeforeMapMapKeyManyToMany += MapMapKeyManyToMany;
            mapper.BeforeMapOneToMany += MapOneToMany;
            mapper.BeforeMapOneToOne += MapOneToOne;
            mapper.BeforeMapProperty += MapProperty;
            mapper.BeforeMapSet += MapSet;
            mapper.BeforeMapSubclass += MapSubclass;
            mapper.BeforeMapUnionSubclass += MapUnionSubclass;
        }

        public static void ManyToManyConvention(IModelInspector modelInspector, PropertyPath member, IManyToManyMapper map)
        {
        }

        public static void MapAny(IModelInspector modelInspector, PropertyPath member, IAnyMapper map)
        {
        }

        public static void MapComponent(IModelInspector modelInspector, PropertyPath member, IComponentAttributesMapper map)
        {
        }

        public static void MapElement(IModelInspector modelInspector, PropertyPath member, IElementMapper map)
        {
        }

        public static void MapIdBag(IModelInspector modelInspector, PropertyPath member, IIdBagPropertiesMapper map)
        {
        }

        public static void MapJoinedSubclass(IModelInspector modelInspector, Type type, IJoinedSubclassAttributesMapper map)
        {

        }

        public static void MapList(IModelInspector modelInspector, PropertyPath member, IListPropertiesMapper map)
        {
        }

        public static void MapMap(IModelInspector modelInspector, PropertyPath member, IMapPropertiesMapper map)
        {
        }

        public static void MapMapKey(IModelInspector modelInspector, PropertyPath member, IMapKeyMapper map)
        {
        }

        public static void MapMapKeyManyToMany(IModelInspector modelInspector, PropertyPath member, IMapKeyManyToManyMapper map)
        {
            var property = member.LocalMember as PropertyInfo;
            var keytypename=property.PropertyType.DetermineDictionaryKeyType().Name;
            map.Column(keytypename + "Id");
        }

        public static void MapOneToMany(IModelInspector modelInspector, PropertyPath member, IOneToManyMapper map)
        {
        }

        public static void MapOneToOne(IModelInspector modelInspector, PropertyPath member, IOneToOneMapper map)
        {
        }

        public static void MapProperty(IModelInspector modelInspector, PropertyPath member, IPropertyMapper map)
        {
            ComponentNamingConvention(modelInspector, member, map);
            RefTextNamingConvention(modelInspector, member, map);
        }

        public static void ComponentNamingConvention(IModelInspector modelInspector, PropertyPath member, IPropertyMapper map)
        {
            //var property = member.LocalMember as PropertyInfo;
            //if (modelInspector.IsComponent(property.DeclaringType))
            //{
            //    map.Column(member.PreviousPath.LocalMember.Name + "_" + member.LocalMember.Name);
            //}
        }
        public static void RefTextNamingConvention(IModelInspector modelInspector, PropertyPath member, IPropertyMapper map)
        {
            var property = member.LocalMember as PropertyInfo;
            if (property.Name == "RefText")
            {
                var props =member.GetContainerEntity(modelInspector).GetProperties();
                var refTextProp = props.Where(p => Attribute.IsDefined(p, typeof (RefTextAttribute), true));
                if (refTextProp.Count()==1)
                {
                    map.Formula(refTextProp.First().Name);
                }
                else
                {
                    string s = "";
                    var signatures = props.Where(
                        p => Attribute.IsDefined(p, typeof(DomainSignatureAttribute), true) && p.PropertyType == typeof(String)); 
                    foreach (var prop in signatures)
                    {
                        s += prop.Name + "+'-'+";
                    }
                    //if (s == "") throw new NetArchException("Must define RefText, or DomainSignature on string props for a DomainObject class:" + member.GetContainerEntity(modelInspector).Name);
                    if (s == "")
                        map.Formula("'not defined'");
                    else
                    {
                        map.Formula(s.Remove(s.Length - 5));
                    }
                    
                }

            }
        }
        public static void MapSet(IModelInspector modelInspector, PropertyPath member, ISetPropertiesMapper map)
        {
        }

        public static void MapSubclass(IModelInspector modelInspector, Type type, ISubclassAttributesMapper map)
        {
        }

        public static void MapUnionSubclass(IModelInspector modelInspector, Type type, IUnionSubclassAttributesMapper map)
        {
        }

        public static void MapBag(IModelInspector modelInspector, PropertyPath member, IBagPropertiesMapper map)
        {
            var inv = member.LocalMember.GetInverseProperty();
            if (inv == null)
            {
                map.Key(x => x.Column(member.GetContainerEntity(modelInspector).Name + "Id"));
                //map.Cascade(Cascade.All | Cascade.DeleteOrphans);
                map.BatchSize(20);
                map.Inverse(true);
                var elementType = member.LocalMember.GetPropertyOrFieldType().DetermineCollectionElementType();
                var genericCollectionType = typeof(DomainListType<>);
                var collectionType = genericCollectionType.MakeGenericType(new[] { elementType});
                map.Type(collectionType);
            }
        }

        //public static void PluralizeEntityName(Type type, IClassAttributesMapper map)
        //{
        //    map.Table(Service.Pluralize(type.Name));
        //}
        public static void TableNameConvention(Type type, IClassAttributesMapper map)
        {
            var ns = type.Namespace;
            var prefix = NamespaceMapToTablePrefix[ns.Substring(ns.LastIndexOf(".") + 1)];
            if (prefix == null)
                throw new NetArchException("DomainObject's namespace has not been registered for table mapping:" + ns + "." +
                                    type.Name);
            map.Table(prefix + type.Name);
        }

        public static void PrimaryKeyConvention(Type type, IClassAttributesMapper map)
        {
            map.Id(k =>
            {
                k.Generator(Generators.Identity);
                k.UnsavedValue(0);
            });
        }
        public static void ReferenceConvention(IModelInspector modelInspector, PropertyPath member, IManyToOneMapper map)
        {
            map.Column(member.LocalMember.Name  + "Id");
        }

        #endregion Methods
    }
}
