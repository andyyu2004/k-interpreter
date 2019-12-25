package interpreter.parsing.parselets.left

import interpreter.error.Either
import interpreter.error.LError
import interpreter.lexing.Token
import interpreter.parsing.Expr
import interpreter.parsing.Parser

class BinaryOpParselet(override val precedence: Int, private val rightAssociative: Boolean = false) : LeftParselet {

   private val recursivePrecedence
        get() = precedence - if (rightAssociative) 1 else 0

    override fun parse(parser: Parser, token: Token, left: Expr): Either<LError, Expr> =
        parser.parseExpression(recursivePrecedence).map { Expr.Bin(left, token, it) }

}