package parser.parsing.parselets

import parser.error.*
import parser.lexing.Token
import parser.parsing.Expr
import parser.parsing.Parser
import parser.parsing.Precedence


interface PrefixParselet {
    val precedence
        get() = Precedence.PREFIX

    fun parse(parser: Parser, token: Token) : Either<String, Expr>
}

class NameParselet : PrefixParselet {
    override fun parse(parser: Parser, token: Token): Either<String, Expr> = Right(Expr.Id(token))
}

class PrefixOpParselet : PrefixParselet {
    override fun parse(parser: Parser, token: Token): Either<String, Expr> =
        parser.parseExpression(precedence).map { Expr.Prefix(token, it) }
}