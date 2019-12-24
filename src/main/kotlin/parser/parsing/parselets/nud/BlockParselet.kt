package parser.parsing.parselets.nud

import parser.error.*
import parser.lexing.*
import parser.parsing.*

object BlockParselet : NullParselet {
    override fun parse(parser: Parser, token: Token): Either<LError, Expr> {
        if (parser.match(TokenType.RBrace)) return Right(Expr.Block(token, listOf()))
        val expressions: MutableList<Either<LError, Expr>> = mutableListOf()
        do {
            if (parser.peek()?.type == TokenType.RBrace) {
                // Then the last expression also had a semicolon and return should be suppressed
                break
            }
            expressions.add(parser.parseExpression(0))
        } while (parser.match(TokenType.SemiColon))

        return Either.sequence(expressions)
            .assert(parser.expect(TokenType.RBrace))
            .map { Expr.Block(token, it) }
    }

}
