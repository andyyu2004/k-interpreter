package parser.parsing.parselets.nud

import parser.error.Either
import parser.lexing.Token
import parser.parsing.Expr
import parser.parsing.Parser

class PrefixOpParselet : NullParselet {
    override fun parse(parser: Parser, token: Token): Either<String, Expr> =
        parser.parseExpression(precedence).map { Expr.Prefix(token, it) }
}