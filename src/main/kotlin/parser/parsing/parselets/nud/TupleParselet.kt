package parser.parsing.parselets.nud

import parser.error.*
import parser.lexing.*
import parser.parsing.*

object TupleParselet {
    // Also pass a parsing function so it can be reused for value tuples / type tuples etc
    // Expects the opening LParen to be parsed already
    fun <T> parse(parser: Parser, parsef: () -> Either<LError, T>): Either<LError, List<T>> {
        if (parser.match(TokenType.RParen)) return Right(listOf())
        val elements: MutableList<Either<LError, T>> = mutableListOf()
        if (parser.peek()?.type != TokenType.RParen) {
            do (elements.add(parsef()))
            while (parser.match(TokenType.Comma))
        }
        return Either.sequence(elements).map { it } assert parser.expect(TokenType.RParen)
    }

}