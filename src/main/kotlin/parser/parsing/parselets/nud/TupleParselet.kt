package parser.parsing.parselets.nud

import parser.error.*
import parser.lexing.*
import parser.parsing.*

object TupleParselet : NullParselet {
    override fun parse(parser: Parser, token: Token): Either<LError, Expr> {
        if (parser.match(TokenType.RParen)) return Right(Expr.Tuple(token, listOf()))
        val elements: MutableList<Either<LError, Expr>> = mutableListOf()
        if (parser.peek()?.type != TokenType.RParen) {
            do (elements.add(parser.parseExpression(0)))
            while (parser.match(TokenType.Comma))
        }
        return Either.sequence(elements).map { Expr.Tuple(token, it) as Expr } assert parser.expect(TokenType.RParen)
    }

}