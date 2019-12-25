package interpreter.parsing.parselets.nud

import interpreter.error.*
import interpreter.lexing.Token
import interpreter.lexing.TokenType
import interpreter.parsing.*

object LetParselet : NullParselet {
    override fun parse(parser: Parser, token: Token): Either<LError, Expr> = TypeParselets.parseBinder(parser).assert { parser.expect(TokenType.Equal) }
        .bind { binder -> parser.parseExpression(0)
        .assert(parser.expect(listOf(TokenType.Semicolon, TokenType.In)))
        .bind { expr -> parser.parseExpression(0).map { body -> Expr.Let(token, binder, expr, body) as Expr } }
    }

}
