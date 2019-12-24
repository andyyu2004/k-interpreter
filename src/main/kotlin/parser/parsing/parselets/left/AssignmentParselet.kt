package parser.parsing.parselets.left

import parser.error.*
import parser.lexing.Token
import parser.parsing.*

object AssignmentParselet : LeftParselet {
    override val precedence: Int
        get() = Precedence.ASSIGNMENT

    override fun parse(parser: Parser, token: Token, left: Expr): Either<LError, Expr> {
        if (left !is Expr.Id) return Left(LError(token, "Cannot assign to r-value $left"))
        return parser.parseExpression(precedence - 1).map {
            Expr.Assign(token, left, it)
        }
    }
}