package parser.parsing.parselets.nud

import parser.error.Either
import parser.lexing.Token
import parser.parsing.Expr
import parser.parsing.Parser
import parser.parsing.Precedence

/** Null Denotation */
interface NullParselet {
    val precedence
        get() = Precedence.PREFIX

    fun parse(parser: Parser, token: Token) : Either<String, Expr>
}

