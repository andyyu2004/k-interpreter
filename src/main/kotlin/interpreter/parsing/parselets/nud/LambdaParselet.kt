package interpreter.parsing.parselets.nud

import interpreter.error.*
import interpreter.lexing.Token
import interpreter.lexing.TokenType
import interpreter.parsing.*
import interpreter.typechecking.types.LType
import interpreter.util.Quadruple

object LambdaParselet : NullParselet {

    @Suppress("UNCHECKED_CAST")
    override fun parse(parser: Parser, token: Token): Either<LError, Expr> {
        val eargs = mutableListOf<Either<LError, Binder>>()
        if (parser.peek()?.type == TokenType.Identifier && parser.lookahead(1)?.type == TokenType.LParen)
            return Left(LError(token, "Lambdas can not have names like functions. Try use a let binding instead."))

        if (parser.match(TokenType.LParen)) {
            if (parser.peek()?.type != TokenType.RParen) {
                do eargs.add(TypeParselets.parseBinder(parser))
                    while (parser.match(TokenType.Comma))
            }
            when (val res = parser.expect(TokenType.RParen)) { is Left -> return Left(res.l) }
        } else eargs.add(TypeParselets.parseBinder(parser))

        val args = Either.sequence(eargs)
        val tret =
            if (parser.match(TokenType.RArrow)) TypeParselets.parseType(parser) as Either<LError, LType?>
            else Right<LError, LType?>(null)
        val expect = parser.expect(TokenType.RFArrow)
        val body = parser.parseExpression(0)

        return Either.sequence4(Quadruple(args, tret, expect, body)).map { (xs, ret, _, expr) ->
            Expr.Lambda(token, xs, expr, ret)
        }
    }

}



