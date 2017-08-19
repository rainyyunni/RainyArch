using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using AutoMapper;
using ProjectBase.Domain;

namespace ProjectBase.BusinessDelegate
{

    public class AutoMapperProfile : Profile 
    {
        public static string[] DomainModelMappingAssemblies { get; set; }
        protected override void Configure()
        {
            ForSourceType<decimal>().AddFormatExpression(context => 
                ((decimal) context.SourceValue).ToString("c"));
            //CreateMap(typeof(BaseDomainObject), typeof(DORef));
            CreateMapForDORef();

            //.ForMember(x => x.ShippingAddress, opt =>
            //{
            //    opt.AddFormatter<AddressFormatter>();
            //});
        }

        protected void CreateMapForDORef()
        {
            var assemblies =new List<Assembly>();
            Array.ForEach(DomainModelMappingAssemblies, i => assemblies.Add(Assembly.LoadFrom(i)));
            assemblies.Each(assembly=>assembly.GetTypes()
                .Where(t => t.IsSubclassOf(typeof (BaseDomainObject)))
                .Each(t => CreateMap(t, typeof(DORef<>).MakeGenericType(new[]{t})))
                );
        }
    }

}
