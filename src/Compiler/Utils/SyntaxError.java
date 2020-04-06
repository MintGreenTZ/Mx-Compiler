package Compiler.Utils;

public class SyntaxError extends RuntimeException {
    String msg;
    Location location;

    public SyntaxError() {
        super();
    }

    public SyntaxError(String message, Location location) {
        super(message);
        this.msg = message;
        this.location = location;
    }

    @Override
    public String getMessage() {
        return "Syntax Error:" + msg + " at " + location.toString();
    }
}