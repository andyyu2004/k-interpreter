package interpreter.parsing.parselets.nud

import interpreter.error.Either
import interpreter.error.LError
import interpreter.error.Right
import interpreter.lexing.Token
import interpreter.parsing.Expr
import interpreter.parsing.Parser

object NameParselet : NullParselet {
    override fun parse(parser: Parser, token: Token): Either<LError, Expr> = Right(Expr.Var(token))
}