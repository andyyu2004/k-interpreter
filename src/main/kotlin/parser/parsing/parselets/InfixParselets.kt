package parser.parsing.parselets

import parser.error.*
import parser.lexing.Token
import parser.lexing.TokenType
import parser.parsing.Expr
import parser.parsing.Parser
import parser.parsing.Precedence

/**
 * Works for anything but prefix i.e. postfix infix mixfix
 */
interface InfixParselets {
    val precedence: Int
    fun parse(parser: Parser, token: Token, left: Expr) : Either<String, Expr>
}

class BinaryOpParselet(override val precedence: Int, private val rightAssociative: Boolean = false) : InfixParselets {

    private val recursivePrecedence
        get() = precedence - if (rightAssociative) 1 else 0

    override fun parse(parser: Parser, token: Token, left: Expr): Either<String, Expr> =
        parser.parseExpression(recursivePrecedence).map { Expr.Bin(left, token, it) }

}

class PostfixOpParselet() : InfixParselets {
    override val precedence
        get() = Precedence.POSTFIX

    override fun parse(parser: Parser, token: Token, left: Expr): Either<String, Expr> =
        Right(Expr.Postfix(left, token))
}

class Ternary(override val precedence: Int) : InfixParselets {
    override fun parse(parser: Parser, token: Token, left: Expr): Either<String, Expr> {
        val then = parser.parseExpression(Precedence.CONDITIONAL)
        parser.expect(TokenType.Colon)
        val elseExpr = parser.parseExpression(Precedence.CONDITIONAL)
        val xs = Either.sequence(listOf(then, elseExpr))
        return xs.map {
            val (fst, snd) = it
            Expr.Cond(token, left, fst, snd)
        }
    }

}
