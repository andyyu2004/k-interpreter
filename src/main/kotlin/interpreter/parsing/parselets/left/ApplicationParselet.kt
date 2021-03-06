package interpreter.parsing.parselets.left

import interpreter.error.Either
import interpreter.error.LError
import interpreter.lexing.*
import interpreter.parsing.*

object ApplicationParselet : LeftParselet {
    override val precedence: Int
        get() = Precedence.INVOKE

    override fun parse(parser: Parser, token: Token, left: Expr): Either<LError, Expr> {
        val args: MutableList<Either<LError, Expr>> = mutableListOf()
        if (parser.peek()?.type != TokenType.RParen) {
            do args.add(parser.parseExpression(0))
            while (parser.match(TokenType.Comma))
        }

        return Either.sequence(args)
            .assert(parser.expect(TokenType.RParen))
            .map { Expr.Apply(token, left, it) }
    }

}
