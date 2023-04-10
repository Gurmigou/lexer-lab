package code.lexer.state;

import code.token.TokenType;

import java.util.HashMap;
import java.util.Map;

public class AutomataState {
    private TokenType tokenType = TokenType.INVALID_TOKEN;
    private char cur = '\0';
    public Map<Character, AutomataState> nextStateMap;

    public AutomataState() {
        this.nextStateMap = new HashMap<>();
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public void setTokenType(TokenType tokenType) {
        this.tokenType = tokenType;
    }

    public char getCur() {
        return cur;
    }

    public void setCur(char cur) {
        this.cur = cur;
    }
}
