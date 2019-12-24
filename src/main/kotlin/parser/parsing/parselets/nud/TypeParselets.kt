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
        else parseType(parser).map { type -> Pair<Token, LType?>(it, type) }
    }

    fun parseType(parser: Parser) : Either<LError, LType> {
        var type = primitive(parser)
        while (parser.match(TokenType.RArrow)) {
            type.bind { t -> parseType(parser).map { type = Right(TArrow(t, it)) }}
        }
        return type
    }

    private fun primitive(parser: Parser) : Either<LError, LType> {
        when {
            parser.match(TokenType.Typename) -> {
                val token = parser.lookahead(-1)!!
                return Right(TName(token))
            }
            parser.match(TokenType.Typevar) -> {
                val token = parser.lookahead(-1)!!
                return Right(TVar(token))
            }
            parser.match(TokenType.LParen) -> {
                parser.markBacktrackPoint()
                return parseType(parser).bind { type ->
                    // If after parsing a type the next token is RParen then it was a grouping, else a Tuple
                    when (parser.expect(TokenType.RParen)) {
                        is Right -> Right<LError, LType>(type)
                        is Left -> {
                            parser.backtrack()
                            TupleParselet.parse(parser) { parseType(parser) }.map { TTuple(it) as LType }
                        }
                    }
                }
            }
            else -> {
                TODO()
            }
        }
    }

}