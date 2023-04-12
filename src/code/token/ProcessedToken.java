package code.token;

public class ProcessedToken extends AbstractToken {
    private TokenType tokenType;
    private int symbolTableIndex;

    public ProcessedToken(int row, int column, TokenType tokenType, int symbolTableIndex) {
        super(row, column);
        this.tokenType = tokenType;
        this.symbolTableIndex = symbolTableIndex;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public void setTokenType(TokenType tokenType) {
        this.tokenType = tokenType;
    }

    public int getSymbolTableIndex() {
        return symbolTableIndex;
    }

    public void setSymbolTableIndex(int symbolTableIndex) {
        this.symbolTableIndex = symbolTableIndex;
    }

    @Override
    public String toString() {
        return tokenType.toString() + " symbol table index: " + symbolTableIndex + " " + super.toString();
    }
}
