package parser.parsing.parselets.nud

import parser.error.*
import parser.lexing.Token
import parser.lexing.TokenType
import parser.parsing.Parser
import parser.types.*

object TypeParselets {
    // parses <id> : <T>
    fun parseNameTypePair(parser: Parser) : Either<String, Pair<Token, LType?>> = parser.expect(TokenType.Identifier).bind {
        if (!parser.match(TokenType.Colon)) Right<String, Pair<Token, LType?>>(Pair(it, null))
        else parse(parser).map { type -> Pair<Token, LType?>(it, type) }
    }

    fun parse(parser: Parser) : Either<String, LType> {
        var type: Either<String, LType> = parser.expect(TokenType.Typename).map(::TName)
        while (parser.match(TokenType.RArrow)) {
            type.bind { t -> parse(parser).map { type = Right(TArrow(t, it)) }}
        }
        return type
    }

}