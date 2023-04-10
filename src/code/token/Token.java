package code.token;

public class Token extends AbstractToken {
    TokenType tokenType;
    int symbolTableIndex;

    public Token(int row, int column, TokenType tokenType, int symbolTableIndex) {
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
}
