package se.magnus.microservices.util.http;

import java.time.ZonedDateTime;

import org.springframework.http.HttpStatus;

public class HttpErrorInfo {

    private final ZonedDateTime timestamp;
    private final String path;
    private final HttpStatus status;
    private final String message;


    protected HttpErrorInfo(){
        timestamp = null;
        path = null;
        status = null;
        message = null;
    }

    public HttpErrorInfo(HttpStatus status, String path, String message) {
        this.timestamp = ZonedDateTime.now();
        this.status = status;
        this.path = path;
        this.message = message;
    }

    public HttpErrorInfo(ZonedDateTime timestamp, String path, HttpStatus status, String message) {
        this.timestamp = timestamp;
        this.path = path;
        this.status = status;
        this.message = message;
    }

    public ZonedDateTime getTimestamp() {
        return this.timestamp;
    }

    public String getPath() {
        return this.path;
    }

    public HttpStatus getStatus() {
        return this.status;
    }

    public String getMessage() {
        return this.message;
    }
}
