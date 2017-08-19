using Castle.MicroKernel.Registration;
using Castle.Windsor;
using ProjectBase.Data;
using ProjectBase.Domain;
using ProjectBase.Utils;
using SharpArch.Domain.Commands;
using SharpArch.Domain.PersistenceSupport;
using SharpArch.NHibernate;
using SharpArch.NHibernate.Contracts.Repositories;
using SharpArch.Web.Mvc.Castle;

namespace ProjectBase.BusinessDelegate
{
    public class CastleWindorComponentRegistrar
    {
        public static void AddComponentsTo(IWindsorContainer container)
        {
            AddGenericDaosTo(container);
            AddCustomDaosTo(container);
            AddQueryObjectsTo(container);
            AddBusinessDelegateTo(container);
            AddOtherTo(container);
        }

        private static void AddBusinessDelegateTo(IWindsorContainer container)
        {
            container.Register(
                AllTypes
                    .FromAssemblyNamed(ProjectHierarchy.BusinessDelegateNS)
                    .BasedOn<IBusinessDelegate>()
                    .WithService.AllInterfaces());//.FirstNonGenericCoreInterface(ProjectHierarchy.DomainNS));

            //keep this as the last one BD registeration,so derived class can be used first
            container.Register(
                Component.For(typeof(ICommonBD<>))
                    .ImplementedBy(typeof(CommonBD<>)));

            container.Register(
                Component.For(typeof(ICommandProcessor))
                    .ImplementedBy(typeof(CommandProcessor))
                    .Named("commandProcessor"));

            container.Register(
                AllTypes.FromAssemblyNamed(ProjectHierarchy.BusinessDelegateNS)
                    .BasedOn(typeof(ICommandHandler<>))
                    .WithService.FirstInterface());


        }

        private static void AddCustomDaosTo(IWindsorContainer container)
        {
            container.Register(
                AllTypes
                    .FromAssemblyNamed(ProjectHierarchy.DataNS)
                    .BasedOn(typeof(IGenericDaoWithTypedId<,>))
                    .WithService.FirstNonGenericCoreInterface(ProjectHierarchy.DomainNS));
        }

        private static void AddGenericDaosTo(IWindsorContainer container)
        {
            container.Register(
                Component.For(typeof(IEntityDuplicateChecker))
                    .ImplementedBy(typeof(EntityDuplicateChecker))
                    .Named("entityDuplicateChecker"));

            container.Register(
                Component.For(typeof(IGenericDao<>))
                    .ImplementedBy(typeof(BaseNHibernateLinqDao<>))
                    .Forward(typeof(INHibernateRepository<>))
                    .Forward(typeof(ILinqRepository<>))
                    .Forward(typeof(IRepository<>))
                    .Named("nhibernateRepositoryType"));

            container.Register(
                Component.For(typeof(IGenericDaoWithTypedId<,>))
                    .ImplementedBy(typeof(BaseNHibernateLinqDaoWithTypedId<,>))
                    .Forward(typeof(INHibernateRepositoryWithTypedId<,>))
                    .Forward(typeof(ILinqRepositoryWithTypedId<,>))
                    .Forward(typeof(IRepositoryWithTypedId<,>))
                    .Named("nhibernateRepositoryWithTypedId"));

            container.Register(
                    Component.For(typeof(ISessionFactoryKeyProvider))
                        .ImplementedBy(typeof(DefaultSessionFactoryKeyProvider))
                        .Named("sessionFactoryKeyProvider"));

        }

        private static void AddQueryObjectsTo(IWindsorContainer container)
        {
            container.Register(
                AllTypes.FromAssemblyNamed(ProjectHierarchy.MvcNS)
                    .BasedOn<NHibernateQuery>()
                    .WithService.FirstInterface());
            container.Register(
                Component.For(typeof(IUtilQuery))
                    .ImplementedBy(typeof(UtilQuery)));
        }

        private static void AddOtherTo(IWindsorContainer container)
        {
            container.Register(
                Component.For(typeof (IExceptionTranslator))
                    .ImplementedBy(typeof (NHibernateExceptionTranslator)));
        }
    }
}
