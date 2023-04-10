package code.token;

public class InvalidToken extends AbstractToken {
    private String invalidSymbol;
    private String errorMessage;

    public InvalidToken(int row, int column, String invalidSymbol, String errorMessage) {
        super(row, column);
        this.invalidSymbol = invalidSymbol;
        this.errorMessage = errorMessage;
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
}
