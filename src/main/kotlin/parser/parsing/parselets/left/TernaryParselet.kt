package parser.parsing.parselets.left

import parser.error.Either
import parser.error.LError
import parser.error.Left
import parser.lexing.Token
import parser.lexing.TokenType
import parser.parsing.Expr
import parser.parsing.Parser
import parser.parsing.Precedence
import parser.parsing.parselets.left.LeftParselet

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