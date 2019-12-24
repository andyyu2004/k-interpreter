package parser.parsing.parselets.nud

import parser.error.*
import parser.lexing.*
import parser.parsing.*

object StringParselet : NullParselet {
    override fun parse(parser: Parser, token: Token): Either<String, Expr> = Right(Expr.EString(token))
}
