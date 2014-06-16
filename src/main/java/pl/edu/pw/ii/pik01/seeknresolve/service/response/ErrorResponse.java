package pl.edu.pw.ii.pik01.seeknresolve.service.response;

public class ErrorResponse {
    private String error;

    public ErrorResponse(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}
