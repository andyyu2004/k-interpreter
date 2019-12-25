package interpreter.parsing.parselets.nud

import interpreter.error.*
import interpreter.lexing.*
import interpreter.parsing.*
import java.lang.RuntimeException

object BoolParselet : NullParselet {
    override fun parse(parser: Parser, token: Token): Either<LError, Expr> = when (token.type) {
        TokenType.True  -> Right(Expr.EBool(token, true))
        TokenType.False -> Right(Expr.EBool(token, false))
        else -> throw RuntimeException()
    }

}
