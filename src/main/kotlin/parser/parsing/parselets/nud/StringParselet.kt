package parser.parsing.parselets.nud

import parser.error.*
import parser.lexing.*
import parser.parsing.*

class StringParselet : NullParselet {
    override fun parse(parser: Parser, token: Token): Either<String, Expr> = Right(Expr.EString(token))
}
