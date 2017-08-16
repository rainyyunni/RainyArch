package projectbase.domain;

public class DBDuplicateException extends Exception implements IDBErrorForUser {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String _table;
	private String[] _fieldNames;
	private String[] _fieldValues;

	public DBDuplicateException() {
	}

	public DBDuplicateException(String msg)

	{
		super(msg);
	}
}
