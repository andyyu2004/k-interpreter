package interpreter.parsing.parselets.left

import interpreter.error.Either
import interpreter.error.LError
import interpreter.lexing.Token
import interpreter.parsing.Expr
import interpreter.parsing.Parser

/**
 * Left denotation
 * Works for anything but prefix i.e. postfix infix mixfix
 */
interface LeftParselet {
    val precedence: Int
    fun parse(parser: Parser, token: Token, left: Expr) : Either<LError, Expr>
}


