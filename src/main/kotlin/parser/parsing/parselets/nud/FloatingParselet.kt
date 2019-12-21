package parser.parsing.parselets.nud

import parser.error.Either
import parser.error.Right
import parser.lexing.Token
import parser.parsing.Expr
import parser.parsing.Parser

class FloatingParselet : NullParselet {
    override fun parse(parser: Parser, token: Token): Either<String, Expr> = Right(Expr.EFloat(token))
}