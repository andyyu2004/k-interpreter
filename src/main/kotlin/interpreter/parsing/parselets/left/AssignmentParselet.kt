package interpreter.parsing.parselets.left

import interpreter.error.*
import interpreter.lexing.Token
import interpreter.parsing.*

object AssignmentParselet : LeftParselet {
    override val precedence: Int
        get() = Precedence.ASSIGNMENT

    override fun parse(parser: Parser, token: Token, left: Expr): Either<LError, Expr> {
        if (left !is Expr.Var) return Left(LError(token, "Cannot assign to r-value $left"))
        return parser.parseExpression(precedence - 1).map {
            Expr.Assign(token, left, it)
        }
    }
}