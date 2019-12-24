package parser.parsing.parselets.nud

import parser.error.*
import parser.lexing.Token
import parser.lexing.TokenType
import parser.parsing.*
import parser.types.LType
import parser.util.Quadruple

object LambdaParselet : NullParselet {

    @Suppress("UNCHECKED_CAST")
    override fun parse(parser: Parser, token: Token): Either<LError, Expr> {
        val eargs = mutableListOf<Either<LError, Pair<Token, LType?>>>()
        if (parser.peek()?.type == TokenType.Identifier && parser.lookahead(1)?.type == TokenType.LParen)
            return Left(LError(token, "Lambdas can not have names like functions. Try use a let binding instead."))

        if (parser.match(TokenType.LParen)) {
            if (parser.peek()?.type != TokenType.RParen) {
                do eargs.add(TypeParselets.parseNameTypePair(parser))
                    while (parser.match(TokenType.Comma))
            }
            when (val res = parser.expect(TokenType.RParen)) { is Left -> return Left(res.l) }
        } else eargs.add(TypeParselets.parseNameTypePair(parser))

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



