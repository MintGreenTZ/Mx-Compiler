package Compiler.Utils;

public class SemanticError extends RuntimeException {
    String msg;
    Location location;

    public SemanticError() {
        super();
    }

    public SemanticError(String message, Location location) {
        super(message);
        this.msg = message;
        this.location = location;
    }

    @Override
    public String getMessage() {
        return "Semantic Error:" + msg + " at " + location.toString();
    }
}
