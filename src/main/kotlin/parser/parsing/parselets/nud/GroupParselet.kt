package parser.parsing.parselets.nud

import parser.error.Either
import parser.error.LError
import parser.lexing.Token
import parser.lexing.TokenType
import parser.parsing.Expr
import parser.parsing.Parser

object GroupParselet : NullParselet {
    override fun parse(parser: Parser, token: Token): Either<LError, Expr> {
        parser.markBacktrackPoint()
        return parser.parseExpression(0)
            .assert(parser.expect(TokenType.RParen))
            .map { Expr.Group(token, it) as Expr }
            // Try parse tuple if the assertion fails or if there is no expression inside (i.e. parserExpression fails)
            .bindLeft {
                parser.backtrack()
                TupleParselet.parse(parser, token)
            }
    }
}

