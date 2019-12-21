package parser.parsing.parselets.left

import parser.error.Either
import parser.error.Right
import parser.lexing.Token
import parser.parsing.Expr
import parser.parsing.Parser
import parser.parsing.Precedence
import parser.parsing.parselets.left.LeftParselet

class PostfixOpParselet : LeftParselet {
    override val precedence
        get() = Precedence.POSTFIX

    override fun parse(parser: Parser, token: Token, left: Expr): Either<String, Expr> =
            Right(Expr.Postfix(left, token))
}