package classes;

public class LongLengthException extends Exception{

    public LongLengthException(String mensaje) {
        super(mensaje);
    }

    public LongLengthException(String mensaje, Throwable throwable) {
        super(mensaje, throwable);
    }

}
