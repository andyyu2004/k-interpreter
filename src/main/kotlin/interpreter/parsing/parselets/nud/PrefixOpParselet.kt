package interpreter.parsing.parselets.nud

import interpreter.error.Either
import interpreter.error.LError
import interpreter.lexing.Token
import interpreter.parsing.Expr
import interpreter.parsing.Parser

object PrefixOpParselet : NullParselet {
    override fun parse(parser: Parser, token: Token): Either<LError, Expr> =
        parser.parseExpression(precedence).map { Expr.Prefix(token, it) }
}