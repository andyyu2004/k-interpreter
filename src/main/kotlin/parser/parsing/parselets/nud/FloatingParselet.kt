package parser.parsing.parselets.nud

import parser.error.Either
import parser.error.LError
import parser.error.Right
import parser.lexing.Token
import parser.parsing.Expr
import parser.parsing.Parser

object FloatingParselet : NullParselet {
    override fun parse(parser: Parser, token: Token): Either<LError, Expr> = Right(Expr.EFloat(token))
}