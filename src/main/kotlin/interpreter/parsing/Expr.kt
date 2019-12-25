package interpreter.parsing

import interpreter.lexing.Token
import interpreter.typechecking.types.LType

/**
 * Arithmetic Operations
 */

sealed class Expr {
    // All expressions must have type property/field and token
    abstract var type: LType?
    abstract val token: Token

    class Bin(val left: Expr, override val token: Token, val right: Expr, override var type: LType? = null) : Expr()
    class Cond(override val token: Token, val cond: Expr, val left: Expr, val right: Expr, override var type: LType? = null) : Expr()
    class Var(override val token: Token, override var type: LType? = null) : Expr()
    class Prefix(override val token: Token, val operand: Expr, override var type: LType? = null) : Expr()
    class Postfix(val operand: Expr, override val token: Token, override var type: LType? = null) : Expr()
    class EInt(override val token: Token, override var type: LType? = null) : Expr()
    class EFloat(override val token: Token, override var type: LType? = null) : Expr()
    // Make an explicit grouping expr to make formatting easier
    class Group(override val token: Token, val inner: Expr, override var type: LType? = null) : Expr()
    class Assign(override val token: Token, val lvalue: Expr, val expr: Expr, override var type: LType? = null) : Expr()
    class Apply(override val token: Token, val f: Expr, val args: List<Expr>, override var type: LType? = null) : Expr()
    class Block(override val token: Token, val exprs: List<Expr>, override var type: LType? = null) : Expr()
    class EString(override val token: Token, override var type: LType? = null) : Expr()
    class Tuple(override val token: Token, val xs: List<Expr>, override var type: LType? = null): Expr()
    class EBool(override val token: Token, val bool: Boolean, override var type: LType? = null): Expr()

    data class Lambda(override val token: Token, var params: List<Pair<Token, LType?>>, val body: Expr, var ret: LType?, override var type: LType? = null): Expr() {
        override fun toString() = super.toString()
    }

    //    class Fn(override val token: Token, val params: List<String>, val body: Block, override var type: LType? = null): Expr()


    // Try to recreate the source code format
    override fun toString() = fmt(0)

    private fun fmt(depth: Int) : String = when(this) {
        is Bin     -> "$left $token $right"
        is Cond    -> "$cond ? $left : $right"
        is Var     -> token.lexeme
        is EInt    -> token.lexeme
        is EFloat  -> token.lexeme
        is Prefix  -> "${token.lexeme}$operand"
        is Postfix -> "$operand${token.lexeme}"
        is Group   -> "($inner)"
        is Assign  -> "$lvalue = $expr"
        is Apply   -> "$f(${args.joinToString(", ")})"
        is Block   -> "{\n${exprs.joinToString("\n") { it.fmt(depth + 4) }} \n}"
//        is Fn      -> TODO()
        is EString -> "\"${token.lexeme}\""
        is Tuple   -> "(${xs.joinToString(", ")})"
        is Lambda  -> "fn (${fmtargs(params)})${fmtret(ret)} => $body"
        is EBool   -> "$bool"
    }.prependIndent(" ".repeat(depth))

    /** Format showing types */
    fun fmtt() = "$this: $type"

    /** Fully parenthesized prefix format */
    fun fmtp() = fmtpHelper(0)

    private fun fmtpHelper(depth: Int): String = when(this) {
        is Bin     -> "($token ${left.fmtp()} ${right.fmtp()})"
        is Cond    -> "${cond.fmtp()} ? ${left.fmtp()} : ${right.fmtp()}"
        is Var     -> token.lexeme
        is EInt    -> token.lexeme
        is EFloat  -> token.lexeme
        is Prefix  -> "${token.lexeme}${operand.fmtp()}"
        is Postfix -> "${operand.fmtp()}${token.lexeme}"
        is Group   -> "($inner)"
        is Assign  -> "(def $lvalue $expr)"
        is Apply   -> "($f ${args.joinToString(" ") { it.fmtp() }})"
        is Block   -> "{\n${exprs.joinToString("\n") { it.fmtpHelper(depth + 4) } }\n}"
//        is Fn      -> TODO()
        is EString -> "\"${token.lexeme}\""
        is Tuple   -> "(${xs.joinToString(", ")})"
        is Lambda  -> "(fn (${fmtargs(params)})${fmtret(ret)} ($body))"
        is EBool   -> "$bool"
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





























