package projectbase.mvc;

import projectbase.utils.IErrorForUser;

public class AdminException extends RuntimeException implements IErrorForUser
{
    public AdminException(){}
    public AdminException(String msg)  { super(msg);}

}
