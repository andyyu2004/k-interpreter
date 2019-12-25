package interpreter.typechecking.types

import interpreter.parsing.Expr

/** Typed Expression */
data class TExpr(val expr: Expr, val type: LType? = null) {
    override fun toString() = expr.toString()
}

