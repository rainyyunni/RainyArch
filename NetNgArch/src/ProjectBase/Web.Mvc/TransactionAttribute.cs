using System;
using System.Web.Mvc;
using ProjectBase.Domain;
using ProjectBase.Web.Mvc.Angular;
using SharpArch.NHibernate;

namespace ProjectBase.Web.Mvc
{


    //I think we need to modify the sharparch transaction to commit when actionexecuted instead of resultexecuted
    public class TransactionAttribute:ActionFilterAttribute 
    {

        /// <summary>
        ///     Optionally holds the factory key to be used when beginning/committing a transaction
        /// </summary>
        private readonly string factoryKey;

        /// <summary>
        ///     When used, assumes the <see cref = "factoryKey" /> to be NHibernateSession.DefaultFactoryKey
        /// </summary>
        public TransactionAttribute()
        {
        }

        /// <summary>
        ///     Overrides the default <see cref = "factoryKey" /> with a specific factory key
        /// </summary>
        public TransactionAttribute(string factoryKey)
        {
            this.factoryKey = factoryKey;
        }

        public bool RollbackOnModelStateError { get; set; }

        public override void OnActionExecuted(ActionExecutedContext filterContext)
        {
            var effectiveFactoryKey = this.GetEffectiveFactoryKey();
            var currentTransaction = NHibernateSession.CurrentFor(effectiveFactoryKey).Transaction;
            
            try
            {
                if (currentTransaction.IsActive)
                {
                    if (filterContext.Exception != null || this.ShouldRollback(filterContext) 
                        || (filterContext.Result is RichClientJsonResult && ((RichClientJsonResult)filterContext.Result).IsErrorResult))
                    {
                        currentTransaction.Rollback();
                        if (filterContext.Exception != null)
                        {
                            ShowUserError(filterContext,filterContext.Exception);
                        }
                    }
                    else
                    {
                        currentTransaction.Commit();
                    }
                }
            }
            catch(Exception e)
            {
                if (!ShowUserError(filterContext,e))
                    throw e;
            }
            finally
            {
                currentTransaction.Dispose();
            }
        }

        public override void OnActionExecuting(ActionExecutingContext filterContext)
        {
            NHibernateSession.CurrentFor(this.GetEffectiveFactoryKey()).BeginTransaction();
        }

        private string GetEffectiveFactoryKey()
        {
            return String.IsNullOrEmpty(factoryKey) ? SessionFactoryKeyHelper.GetKey() : factoryKey;
        }

        private bool ShouldRollback(ControllerContext filterContext)
        {
            return this.RollbackOnModelStateError && !filterContext.Controller.ViewData.ModelState.IsValid;
        }

        private bool ShowUserError(ActionExecutedContext filterContext, Exception e)
      {
            var controller = (BaseController)filterContext.Controller;
            var error = controller.ExTranslator.Translate(e);
            if (error is IDBErrorForUser)
            {
                filterContext.ExceptionHandled = true;
                filterContext.Result = controller.RcJsonError(error.Message);
                return true;
            }
            return false;
      }
    }
}