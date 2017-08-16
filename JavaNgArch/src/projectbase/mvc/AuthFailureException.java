

package projectbase.mvc;

    public class AuthFailureException extends RuntimeException
    {
        public AuthFailureException(){super();}
        public AuthFailureException(String msg){ super(msg) ; }

    }

