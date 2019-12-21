package parser.parsing.parselets.nud

import parser.error.Either
import parser.lexing.Token
import parser.lexing.TokenType
import parser.parsing.Expr
import parser.parsing.Parser

class GroupParselet : NullParselet {
    override fun parse(parser: Parser, token: Token): Either<String, Expr> =
        parser.parseExpression(0) assert parser.expect(TokenType.RParen) map {
            Expr.Group(token, it)
        }


}