package interpreter.parsing.parselets.nud

import interpreter.error.Either
import interpreter.error.LError
import interpreter.lexing.Token
import interpreter.lexing.TokenType
import interpreter.parsing.Expr
import interpreter.parsing.Parser

object GroupParselet : NullParselet {
    override fun parse(parser: Parser, token: Token): Either<LError, Expr> {
        parser.markBacktrackPoint()
        return parser.parseExpression(0)
            .assert(parser.expect(TokenType.RParen))
            .map { Expr.Group(token, it) as Expr }
            // Try parse tuple if the assertion fails or if there is no expression inside (i.e. parserExpression fails)
            .bindLeft {
                parser.backtrack()
                TupleParselet.parse(parser) { parser.parseExpression(0) }.map { Expr.Tuple(token, it) as Expr }
            }
    }
}

