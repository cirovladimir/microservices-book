package se.magnus.microservices.util.exceptions;

public class NotFoundException extends RuntimeException{
    public NotFoundException(){}

    public NotFoundException(String msg){
        super(msg);
    }

    public NotFoundException(String msg, Throwable cause){
        super(msg, cause);
    }

    public NotFoundException(Throwable cause){
        super(cause);
    }
}
