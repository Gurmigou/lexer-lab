package code.lexer;

import code.lexer.state.LexerState;

import java.util.Stack;

public class LexerStateManager {
    private final Stack<LexerState> lexerStateStack;

    public LexerStateManager() {
        this.lexerStateStack = new Stack<>();
        this.lexerStateStack.push(LexerState.DEFAULT);
    }

    public void transitToNextState(LexerState lexerState) {
        lexerStateStack.push(lexerState);
    }

    public void backToPrevState() {
        if (!lexerStateStack.isEmpty()) {
            lexerStateStack.pop();
        }
        if (lexerStateStack.isEmpty()) {
            transitToDefaultState();
        }
    }

    public void transitToDefaultState() {
        lexerStateStack.clear();
        lexerStateStack.push(LexerState.DEFAULT);
    }

    public LexerState getCurState() {
        return lexerStateStack.peek();
    }

    public boolean isInStateChain(LexerState lexerState) {
        for (var state : lexerStateStack) {
            if (state == lexerState)
                return true;
        }
        return false;
    }

    public void transitToNextStateIfNotIn(LexerState lexerState) {
        if (getCurState() != lexerState)
            transitToNextState(lexerState);
    }
}
