package parser.parsing.parselets.left

import parser.error.Either
import parser.error.Left
import parser.lexing.Token
import parser.parsing.Expr
import parser.parsing.Parser
import parser.parsing.Precedence

object AssignmentParselet : LeftParselet {
    override val precedence: Int
        get() = Precedence.ASSIGNMENT

    override fun parse(parser: Parser, token: Token, left: Expr): Either<String, Expr> {
        if (left !is Expr.Id) return Left("Cannot assign to r-value $left")
        return parser.parseExpression(precedence - 1).map {
            Expr.Assign(token, left, it)
        }
    }
}