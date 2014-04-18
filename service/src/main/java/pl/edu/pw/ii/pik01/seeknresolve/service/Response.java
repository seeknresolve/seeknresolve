package pl.edu.pw.ii.pik01.seeknresolve.service;

public class Response<T> {
    private T object;
    private Status status;

    public Response(T object, Status status) {
        this.object = object;
        this.status = status;
    }

    public T getObject() {
        return object;
    }

    public Status getStatus() {
        return status;
    }

    public enum Status {
        CREATED,
        NOT_CREATED,
        RECEIVED,
        NOT_RECEIVED,
        UPDATED,
        NOT_UPDATED,
        DELETED,
        NOT_DELETED,
    }
}
