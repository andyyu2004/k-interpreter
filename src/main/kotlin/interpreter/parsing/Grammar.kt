package interpreter.parsing

import interpreter.lexing.TokenType
import interpreter.parsing.parselets.nud.LambdaParselet
import interpreter.parsing.parselets.nud.*
import interpreter.parsing.parselets.left.*
import java.util.*

/**
 * Null denotation (nud) and
 * Left denotation (led) symbols
 */
data class Grammar(
        val nullParselets: EnumMap<TokenType, NullParselet> = EnumMap(TokenType::class.java),
        val leftParselets:  EnumMap<TokenType, LeftParselet> = EnumMap(TokenType::class.java)
)

fun generateGrammar() = Grammar (
    nullParselets = EnumMap(mapOf(
        TokenType.Identifier to NameParselet,
        TokenType.Let        to LetParselet,
        TokenType.Float      to FloatingParselet,
        TokenType.Integral   to IntegralParselet,
        TokenType.Tilde      to PrefixOpParselet,
        TokenType.Plus       to PrefixOpParselet,
        TokenType.Minus      to PrefixOpParselet,
        TokenType.Bang       to PrefixOpParselet,
        TokenType.LParen     to GroupParselet,
        TokenType.LBrace     to BlockParselet,
        TokenType.String     to StringParselet,
        TokenType.Fn         to LambdaParselet,
        TokenType.False      to BoolParselet,
        TokenType.True       to BoolParselet
    )),

    leftParselets = EnumMap(mapOf(
        TokenType.Plus   to BinaryOpParselet(Precedence.SUM),
        TokenType.Minus  to BinaryOpParselet(Precedence.SUM),
        TokenType.Caret  to BinaryOpParselet(Precedence.EXPONENT, true),
        TokenType.Star   to BinaryOpParselet(Precedence.PRODUCT),
        TokenType.Slash  to BinaryOpParselet(Precedence.PRODUCT),
        TokenType.DPlus  to PostfixOpParselet,
        TokenType.QMark  to TernaryParselet,
        TokenType.Equal  to AssignmentParselet,
        TokenType.LParen to ApplicationParselet
    ))

)
