package parser.parsing.parselets.left

import parser.error.Either
import parser.lexing.Token
import parser.parsing.Expr
import parser.parsing.Parser

class BinaryOpParselet(override val precedence: Int, private val rightAssociative: Boolean = false) : LeftParselet {

   private val recursivePrecedence
        get() = precedence - if (rightAssociative) 1 else 0

    override fun parse(parser: Parser, token: Token, left: Expr): Either<String, Expr> =
        parser.parseExpression(recursivePrecedence).map { Expr.Bin(left, token, it) }

}