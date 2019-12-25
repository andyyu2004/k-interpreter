package interpreter.parsing.parselets.nud

import interpreter.error.Either
import interpreter.error.LError
import interpreter.lexing.Token
import interpreter.parsing.Expr
import interpreter.parsing.Parser
import interpreter.parsing.Precedence

/** Null Denotation */
interface NullParselet {
    val precedence
        get() = Precedence.PREFIX

    fun parse(parser: Parser, token: Token) : Either<LError, Expr>
}

