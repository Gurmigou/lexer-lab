package code.token;

public class Operators {

    private static final String[] OPERATORS = {
            // Math operators
            "plus", // +
            "minus", // -
            "multiply", // *
            "divide", // /
            "modulo", // %

            // Math + logical operators
            "equalTo", // ==
            "notEqualTo", // !=
            "greaterThan", //  >
            "lessThan", // <
            "greaterThanOrEqualTo", // >=
            "lessThanOrEqualTo", // <=

            // Boolean operators
            "and", // &&
            "or", // ||
            "not", // !

            // Assignment operators
            "assign", // =
            "plusAssign", // +=
            "minusAssign", // -=
            "multiplyAssign", // *=
            "divideAssign", // /=
            "moduloAssign", // %=

            // Bitwise operators
            "bitwiseAnd", // &
            "bitwiseOr", // |
            "bitwiseXor", // ^
            "bitwiseNot", // ~
            "leftShift", //  >>
            "rightShift", //  <<
            "unsignedRightShift", // >>>
            "leftShiftAssign", // <<=
            "rightShiftAssign", // >>=
            "bitwiseAndAssign", // &=
            "bitwiseXorAssign", // ^=
            "bitwiseOrAssign", // |=
            "unsignedRightShiftAssign", // >>>=

            // Type
            "typeColonOperator", // :

            // Other
            "yield_LeftArrow",  // <-
            "mapTo_RightArrow", // ->
            "implies_LeftEqualArrow", // =>
            "listCons_TwoColon", // ::
            "prepend_PlusColon", // +:
            "concatenate_PlusPlus", // ++
            "append_ColonPlusPlus", // :++

            "dot", // .
            "comma", // ,
            "semicolon", // ;
            "openParenthesis", // (
            "closeParenthesis", // )
            "openBracket", // [
            "closeBracket", // ]
            "openBrace", // {
            "closeBrace", // }
            "sInterpolator", // s
    };


}
