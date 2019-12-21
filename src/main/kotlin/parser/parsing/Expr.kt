package parser.parsing

import parser.lexing.Token
import parser.util.padLeft

/**
 * Arithmetic Operations
 */

sealed class Expr {

    class Bin(val left: Expr, val op: Token, val right: Expr) : Expr()
    class Cond(val token: Token, val cond: Expr, val left: Expr, val right: Expr) : Expr()
    class Id(val token: Token) : Expr()
    class Prefix(val operator: Token, val operand: Expr) : Expr()
    class Postfix(val operand: Expr, val operator: Token) : Expr()
    class EInt(val token: Token) : Expr()
    class EFloat(val token: Token) : Expr()
    // Make an explicit grouping expr to make formatting easier
    class Group(val token: Token, val expr: Expr) : Expr()
    class Assign(val token: Token, val lvalue: Expr, val expr: Expr) : Expr()
    class Apply(val token: Token, val callee: Expr, val args: List<Expr>) : Expr()
    class Block(val token: Token, val exprs: List<Expr>) : Expr()
    class Fn(val token: Token, val params: List<String>, val body: Block): Expr()
    class EString(val token: Token) : Expr()

    // Try to recreate the source code format
    override fun toString() = fmt(0)

    private fun fmt(depth: Int) : String = when(this) {
        is Bin     -> "${left.fmt(depth)} ${left.fmt(depth)} ${right.fmt(depth)}"
        is Cond    -> "${cond.fmt(depth)} ? ${left.fmt(depth)} : ${right.fmt(depth)}"
        is Id      -> token.lexeme
        is EInt    -> token.lexeme
        is EFloat  -> token.lexeme
        is Prefix  -> "${operator.lexeme}${operand.fmt(depth)}"
        is Postfix -> "${operand.fmt(depth)}{operator.lexeme}"
        is Group   -> "($expr)"
        is Assign  -> "$lvalue = $expr"
        is Apply   -> "$callee(${args.joinToString(", ")})"
        is Block   -> "{\n${exprs.joinToString("\n") { it.fmt(depth + 4) }} \n${" ".repeat(depth)}}"
        is Fn      -> TODO()
        is EString -> "\"${token.lexeme}\""
    }.padLeft(depth)

    /** Fully parenthesized prefix format */
    fun fmtp() = fmtpHelper(0)

    private fun fmtpHelper(depth: Int): String = when(this) {
        is Bin     -> "($op ${left.fmtpHelper(depth)} ${right.fmtpHelper(depth)})"
        is Cond    -> "${cond.fmtpHelper(depth)} ? ${left.fmtpHelper(depth)} : ${right.fmtpHelper(depth)}"
        is Id      -> token.lexeme
        is EInt    -> token.lexeme
        is EFloat  -> token.lexeme
        is Prefix  -> "${operator.lexeme}${operand.fmtpHelper(depth)}"
        is Postfix -> "${operand.fmtpHelper(depth)}${operator.lexeme}"
        is Group   -> "($expr)"
        is Assign  -> "(def $lvalue $expr)"
        is Apply   -> "($callee ${args.joinToString(" ") { it.fmtpHelper(depth) }})"
        is Block   -> "{\n${exprs.joinToString("\n") { it.fmtpHelper(depth + 4) }}\n${" ".repeat(depth)}}"
        is Fn      -> TODO()
        is EString -> "\"${token.lexeme}\""
    }.padLeft(depth)

}





























