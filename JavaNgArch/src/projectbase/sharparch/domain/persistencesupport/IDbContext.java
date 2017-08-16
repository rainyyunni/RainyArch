package projectbase.sharparch.domain.persistencesupport;

import org.hibernate.HibernateException;
import org.hibernate.Transaction;

/// <summary>
///     Note that outside of CommitChanges(), you shouldn't have to invoke this Object very often.  
///     If you're import the <see cref = "SharpArch.Web.NHibernate.TransactionAttribute" /> on your 
///     controller actions, then the transaction opening/committing will be taken care of for you.
/// </summary>
public interface IDbContext {
	// should be disposable
	Transaction BeginTransaction();

	void CommitChanges() throws HibernateException;

	void CommitTransaction();

	void RollbackTransaction();
}
