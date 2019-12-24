package parser.parsing.parselets.nud

import parser.error.*
import parser.lexing.Token
import parser.lexing.TokenType
import parser.parsing.Parser
import parser.types.*

object TypeParselets {
    // parses <id> : <T>
    fun parseNameTypePair(parser: Parser) : Either<LError, Pair<Token, LType?>> = parser.expect(TokenType.Identifier).bind {
        if (!parser.match(TokenType.Colon)) Right<LError, Pair<Token, LType?>>(Pair(it, null))
        else parse(parser).map { type -> Pair<Token, LType?>(it, type) }
    }

    fun parse(parser: Parser) : Either<LError, LType> {
        var type = primitive(parser)
        while (parser.match(TokenType.RArrow)) {
            type.bind { t -> parse(parser).map { type = Right(TArrow(t, it)) }}
        }
        return type
    }

    private fun primitive(parser: Parser) : Either<LError, LType> {
        if (parser.match(TokenType.Typename)) {
            val token = parser.lookahead(-1)!!
            return Right(TName(token))
        } else if (parser.match(TokenType.LParen)) {
            println("backtrack")
            parser.markBacktrackPoint()
            when (val type = parse(parser)) {
                is Right -> {
                    return type assert parser.expect(TokenType.RParen)
                }
                is Left  -> {
                    parser.backtrack()
                    TupleParselet.parse(parser) { parse(parser) }.map { TTuple(it) }
                }
            }
        }
        TODO()
    }

}