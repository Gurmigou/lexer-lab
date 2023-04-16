package code.token;

public class InvalidToken extends AbstractToken {
    public static final String INVALID_TOKEN_MSG = "Invalid token: ";
    private final String errorMessage;
    private final String invalidSymbol;

    public InvalidToken(int row, int column, String errorMessage, String invalidSymbol) {
        super(row, column);
        this.errorMessage = errorMessage;
        this.invalidSymbol = invalidSymbol;
    }

    public String getFullErrorMessage() {
        return INVALID_TOKEN_MSG + "\"" + invalidSymbol + "\". " + errorMessage;
    }
}
