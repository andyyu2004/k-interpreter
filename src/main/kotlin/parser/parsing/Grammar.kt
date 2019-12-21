package parser.parsing

import parser.lexing.TokenType
import parser.parsing.parselets.*
import java.util.*

data class Grammar(
    val prefixParselets: EnumMap<TokenType, PrefixParselet> = EnumMap(TokenType::class.java),
    val infixParselets:  EnumMap<TokenType, BinaryOpParselet> = EnumMap(TokenType::class.java)
)

fun generateGrammar() = Grammar (
    prefixParselets = EnumMap(mapOf(
        TokenType.Identifier to NameParselet(),
        TokenType.Tilde      to PrefixOpParselet(),
        TokenType.Plus       to PrefixOpParselet(),
        TokenType.Minus      to PrefixOpParselet(),
        TokenType.Bang       to PrefixOpParselet()
    )),

    infixParselets = EnumMap(mapOf(
        TokenType.Plus to BinaryOpParselet(Precedence.SUM),
        TokenType.Minus to BinaryOpParselet(Precedence.SUM),
        TokenType.Caret to BinaryOpParselet(Precedence.EXPONENT, true),
        TokenType.Star to BinaryOpParselet(Precedence.PRODUCT),
        TokenType.Slash to BinaryOpParselet(Precedence.PRODUCT)
    ))

)