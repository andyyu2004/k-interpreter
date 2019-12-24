package parser.parsing.parselets.nud

import parser.error.Either
import parser.error.LError
import parser.lexing.Token
import parser.parsing.Expr
import parser.parsing.Parser

object PrefixOpParselet : NullParselet {
    override fun parse(parser: Parser, token: Token): Either<LError, Expr> =
        parser.parseExpression(precedence).map { Expr.Prefix(token, it) }
}