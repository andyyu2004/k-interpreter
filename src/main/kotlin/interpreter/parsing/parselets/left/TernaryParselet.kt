package interpreter.parsing.parselets.left

import interpreter.error.Either
import interpreter.error.LError
import interpreter.error.Left
import interpreter.lexing.Token
import interpreter.lexing.TokenType
import interpreter.parsing.Expr
import interpreter.parsing.Parser
import interpreter.parsing.Precedence

object TernaryParselet : LeftParselet {

    override val precedence: Int
        get() = Precedence.CONDITIONAL

    override fun parse(parser: Parser, token: Token, left: Expr): Either<LError, Expr> {
        val then = parser.parseExpression(precedence)
        when (val x = parser.expect(TokenType.Colon)) {
            is Left -> return Left(x.l)
        }
        val elseExpr = parser.parseExpression(precedence)
        val xs = Either.sequence(listOf(then, elseExpr))
        return xs.map {
            val (fst, snd) = it
            Expr.Cond(token, left, fst, snd)
        }
    }

}