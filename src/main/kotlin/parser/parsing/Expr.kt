package parser.parsing

import parser.lexing.Token

/**
 * Arithmetic Operations
 */

sealed class Expr {
    class Bin(val left: Expr, val op: Token, val right: Expr) : Expr()
    class Num(val num: Double) : Expr()
    class Cond(val token: Token, val cond: Expr, val left: Expr, val right: Expr) : Expr()
    class Id(val token: Token) : Expr()
    class Prefix(val operator: Token, val operand: Expr) : Expr()
    class Postfix(val operand: Expr, val operator: Token) : Expr()

    // Try to recreate the source code
    override fun toString() = when(this) {
        is Bin -> "$left $op $right"
        is Num -> "$num"
        is Cond -> "$cond ? $left : $right"
        is Id -> token.lexeme
        is Prefix -> "${operator.lexeme}$operand"
        is Postfix -> "$operand${operator.lexeme}"
    }

    /** Fully parenthesized format */
    fun fmtp(): String = when(this) {
        is Bin -> "(${left.fmtp()} $op ${right.fmtp()})"
        is Num -> "$num"
        is Cond -> "${cond.fmtp()} ? ${left.fmtp()} : ${right.fmtp()}"
        is Id -> token.lexeme
        is Prefix -> "${operator.lexeme}${operand.fmtp()}"
        is Postfix -> "${operand.fmtp()}${operator.lexeme}"
    }
}





























