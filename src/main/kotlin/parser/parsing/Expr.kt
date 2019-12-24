package parser.parsing

import parser.lexing.Token
import parser.types.LType
import parser.util.padLeft

/**
 * Arithmetic Operations
 */

/** Typed Expression */
data class TExpr(val expr: Expr, val type: LType? = null)

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
    class Tuple(val token: Token, val xs: List<Expr>): Expr()

    data class Lambda(val token: Token, val args: List<Pair<Token, LType?>>, val body: Expr, val ret: LType?): Expr() {
        override fun toString() = super.toString()
    }


    // Try to recreate the source code format
    override fun toString() = fmt(0)

    private fun fmt(depth: Int) : String = when(this) {
        is Bin     -> "$left $op $right"
        is Cond    -> "$cond ? $left : $right"
        is Id      -> token.lexeme
        is EInt    -> token.lexeme
        is EFloat  -> token.lexeme
        is Prefix  -> "${operator.lexeme}$operand"
        is Postfix -> "$operand${operator.lexeme}"
        is Group   -> "($expr)"
        is Assign  -> "$lvalue = $expr"
        is Apply   -> "$callee(${args.joinToString(", ")})"
        is Block   -> "{\n${exprs.joinToString("\n") { it.fmt(depth + 4) }} \n}"
        is Fn      -> TODO()
        is EString -> "\"${token.lexeme}\""
        is Tuple   -> "(${xs.joinToString(", ")})"
        is Lambda  -> "fn (${fmtargs(args)})${fmtret(ret)} => $body"
    }.prependIndent(" ".repeat(depth))

    /** Fully parenthesized prefix format */
    fun fmtp() = fmtpHelper(0)

    private fun fmtpHelper(depth: Int): String = when(this) {
        is Bin     -> "($op ${left.fmtp()} ${right.fmtp()})"
        is Cond    -> "${cond.fmtp()} ? ${left.fmtp()} : ${right.fmtp()}"
        is Id      -> token.lexeme
        is EInt    -> token.lexeme
        is EFloat  -> token.lexeme
        is Prefix  -> "${operator.lexeme}${operand.fmtp()}"
        is Postfix -> "${operand.fmtp()}${operator.lexeme}"
        is Group   -> "($expr)"
        is Assign  -> "(def $lvalue $expr)"
        is Apply   -> "($callee ${args.joinToString(" ") { it.fmtp() }})"
        is Block   -> "{\n${exprs.joinToString("\n") { it.fmtpHelper(depth + 4) } }\n}"
        is Fn      -> TODO()
        is EString -> "\"${token.lexeme}\""
        is Tuple   -> "(${xs.joinToString(", ")})"
        is Lambda  -> "(fn (${fmtargs(args)})${fmtret(ret)} ($body))"
    }.prependIndent(" ".repeat(depth))

    companion object {
        private fun fmtargs(args: List<Pair<Token, LType?>>): String =
            args.joinToString(", ") { (token, arg) ->
                if (arg == null) token.lexeme
                else "${token.lexeme}: $arg"
            }

        private fun fmtret(type: LType?) =
            if (type == null) ""
            else " -> $type"
    }

}





























