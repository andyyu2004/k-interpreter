package interpreter.parsing.parselets.nud

import interpreter.error.*
import interpreter.lexing.*
import interpreter.parsing.*

object StringParselet : NullParselet {
    override fun parse(parser: Parser, token: Token): Either<LError, Expr> = Right(Expr.EString(token))
}
