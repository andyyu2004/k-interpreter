package interpreter.parsing.parselets.left

import interpreter.error.Either
import interpreter.error.LError
import interpreter.error.Right
import interpreter.lexing.Token
import interpreter.parsing.Expr
import interpreter.parsing.Parser
import interpreter.parsing.Precedence

object PostfixOpParselet : LeftParselet {
    override val precedence
        get() = Precedence.POSTFIX

    override fun parse(parser: Parser, token: Token, left: Expr): Either<LError, Expr> =
            Right(Expr.Postfix(left, token))
}