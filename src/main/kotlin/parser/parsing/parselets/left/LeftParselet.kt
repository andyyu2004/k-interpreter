package parser.parsing.parselets.left

import parser.error.Either
import parser.lexing.Token
import parser.parsing.Expr
import parser.parsing.Parser

/**
 * Left denotation
 * Works for anything but prefix i.e. postfix infix mixfix
 */
interface LeftParselet {
    val precedence: Int
    fun parse(parser: Parser, token: Token, left: Expr) : Either<String, Expr>
}


