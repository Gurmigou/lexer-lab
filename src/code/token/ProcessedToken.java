package code.token;

public class ProcessedToken extends AbstractToken {
    private final TokenType tokenType;
    private final int symbolTableIndex;

    public ProcessedToken(int row, int column, TokenType tokenType, int symbolTableIndex) {
        super(row, column);
        this.tokenType = tokenType;
        this.symbolTableIndex = symbolTableIndex;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public int getSymbolTableIndex() {
        return symbolTableIndex;
    }


    @Override
    public String toString() {
        return tokenType.toString() + " symbol table index: " + symbolTableIndex + " " + super.toString();
    }
}
