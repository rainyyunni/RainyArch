package projectbase.sharparch.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import projectbase.sharparch.domain.designbycontract.Check;
import projectbase.sharparch.domain.designbycontract.PreconditionException;
import projectbase.sharparch.domain.persistencesupport.IDbContext;

public class DbContext implements IDbContext {
	public DbContext(String factoryKey) {
		Check.Require(factoryKey != null && !factoryKey.isEmpty(),
				"factoryKey may not be null or empty");

		setFactoryKey(factoryKey);
	}

	private String _factoryKey;

	public String getFactoryKey() {
		return _factoryKey;
	}

	public void setFactoryKey(String value) {
		_factoryKey = value;
	}

	private Session getSession() {

		return HibernateSession.CurrentFor(_factoryKey);

	}

	public Transaction BeginTransaction()  {
		return getSession().beginTransaction();
	}

	// / <summary>
	// / This isn't specific to any one DAO and flushes everything that has been
	// / changed since the last commit.
	// / </summary>
	public void CommitChanges() throws HibernateException,
			PreconditionException {
		getSession().flush();
	}

	public void CommitTransaction()  {
		getSession().getTransaction().commit();
	}

	public void RollbackTransaction() {
		getSession().getTransaction().rollback();
	}
}
