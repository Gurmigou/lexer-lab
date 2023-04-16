package code.token;

public class InvalidToken extends AbstractToken {
    public static final String INVALID_TOKEN_MSG = "Invalid token: ";
    private String errorMessage;
    private String invalidSymbol;

    public InvalidToken(int row, int column, String errorMessage, String invalidSymbol) {
        super(row, column);
        this.errorMessage = errorMessage;
        this.invalidSymbol = invalidSymbol;
    }

    public String getInvalidSymbol() {
        return invalidSymbol;
    }

    public void setInvalidSymbol(String invalidSymbol) {
        this.invalidSymbol = invalidSymbol;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getFullErrorMessage() {
        return INVALID_TOKEN_MSG + "\"" + invalidSymbol + "\". " + errorMessage;
    }
}
