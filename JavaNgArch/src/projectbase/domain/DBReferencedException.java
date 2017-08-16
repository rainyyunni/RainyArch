package projectbase.domain;

public class DBReferencedException  extends  Exception implements IDBErrorForUser
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String _foriegnTable;
    private String _foriegnFieldName;
    private String _foriegnFieldVale;
    private String _primaryTable;
    private String _primaryFieldName;
    private String _primaryFieldValue;
    public DBReferencedException()
    {
    }
    public DBReferencedException(String msg)

    {super(msg);
    }
}
