package projectbase.sharparch.hibernate;

import projectbase.utils.function.Action;

    /// <summary>
    ///     Invoked by Global.asax.cx, or wherever you can to initialize NHibernate, to guarentee that 
    ///     NHibernate is only initialized once while working in IIS 7 integrated mode.  But note 
    ///     that this is not web specific, although that is the realm that prompted its creation.
    /// 
    ///     In a web context, it should be invoked from Application_BeginRequest with the NHibernateSession.Init()
    ///     function being passed as a parameter to InitializeNHiberate()
    /// </summary>
    public class HibernateInitializer
    {
        private static final Object _SyncLock = new Object();

        private static HibernateInitializer _instance;

        private boolean _hibernateSessionIsLoaded;

        protected HibernateInitializer()
        {
        }

        public static HibernateInitializer Instance()
        {
            if (_instance == null)
            {
            	synchronized (_SyncLock)
                {
                    if (_instance == null)
                    {
                        _instance = new HibernateInitializer();
                    }
                }
            }

            return _instance;
        }

        /// <summary>
        ///     This is the method which should be given the call to intialize the NHibernateSession; e.g.,
        ///     NHibernateInitializer.Instance().InitializeNHibernateOnce(() => InitializeNHibernateSession());
        ///     where InitializeNHibernateSession() is a method which calls NHibernateSession.Init()
        /// </summary>
        /// <param name = "initMethod"></param>
        public void InitializeHibernateOnce(Action initFunction)
        {
        	synchronized (_SyncLock)
            {
                if (!this._hibernateSessionIsLoaded)
                {
                	initFunction.Do();
                    this._hibernateSessionIsLoaded = true;
                }
            }
        }
    }
