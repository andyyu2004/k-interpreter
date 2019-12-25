package interpreter.parsing.parselets.nud

import interpreter.error.*
import interpreter.lexing.Token
import interpreter.lexing.TokenType
import interpreter.parsing.Binder
import interpreter.parsing.Parser
import interpreter.typechecking.types.*

object TypeParselets {
    // parses <id> : <T>
    fun parseBinder(parser: Parser) : Either<LError, Binder> = parser.expect(TokenType.Identifier).bind { token ->
        if (!parser.match(TokenType.Colon)) Right(Binder(token, null))
        else parseType(parser).map { type -> Binder(token, type) }
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
            parser.match(TokenType.Int) -> {
                return Right(TInt)
            }
            parser.match(TokenType.Bool) -> {
                return Right(TBool)
            }
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
                        is Right -> Right(type)
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



