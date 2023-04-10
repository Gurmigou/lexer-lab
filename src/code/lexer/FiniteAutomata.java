package code.lexer;

import code.lexer.state.AutomataState;
import code.token.TokenType;
import code.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class FiniteAutomata {
    private final AutomataState rootState;

    public FiniteAutomata(List<TokenType> tokenTypeList, Function<TokenType, String> nameMapper) {
        this.rootState = new AutomataState();
        List<Pair<String, TokenType>> nameTokenTypeList = tokenTypeList.stream()
                .map(token -> Pair.of(nameMapper.apply(token), token))
                .toList();
        initFiniteAutomataRecursively(rootState, nameTokenTypeList, 0);
    }

    public Pair<TokenType, Integer> checkValue(String codeLine, int startPos) {
        return checkValueHelper(rootState, codeLine, startPos);
    }

    public AutomataState getRootState() {
        return rootState;
    }

    private void initFiniteAutomataRecursively(AutomataState state, List<Pair<String, TokenType>> tokenTypeList, int pos) {
        Map<Character, List<Pair<String, TokenType>>> transitionsMap = new HashMap<>();
        for (Pair<String, TokenType> nameToken : tokenTypeList) {
            if (nameToken.getFirst().length() > pos) {
                char cur = nameToken.getFirst().charAt(pos);
                if (!transitionsMap.containsKey(cur)) {
                    transitionsMap.put(cur, new ArrayList<>());
                }
                transitionsMap.get(cur).add(nameToken);
            }
        }
        for (var entry : transitionsMap.entrySet()) {
            AutomataState newAutomataState = new AutomataState();
            state.nextStateMap.put(entry.getKey(), newAutomataState);
            for (Pair<String, TokenType> transition : entry.getValue()) {
                if (transition.getFirst().length() == pos + 1) {
                    newAutomataState.setTokenType(transition.getSecond());
                }
            }
            initFiniteAutomataRecursively(newAutomataState, transitionsMap.get(entry.getKey()), pos + 1);
        }
    }

    private Pair<TokenType, Integer> checkValueHelper(AutomataState state, String codeLine, int pos) {
        if (codeLine.length() <= pos) {
            return Pair.of(state.getTokenType(), pos);
        }
        if (state.nextStateMap.containsKey(codeLine.charAt(pos))) {
            AutomataState nextState = state.nextStateMap.get(codeLine.charAt(pos));
            return checkValueHelper(nextState, codeLine, pos + 1);
        }
        return Pair.of(state.getTokenType(), pos);
    }
}
