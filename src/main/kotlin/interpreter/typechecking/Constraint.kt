package interpreter.typechecking

import interpreter.error.*
import interpreter.typechecking.types.LType
import interpreter.typechecking.types.TVar

sealed class Constraint : Substitutable<Constraint> {
    override fun ftv(): Set<String> = TODO()
    override fun apply(s: Substitution): Constraint = when (this) {
        is CAnd   -> CAnd(l.apply(s), r.apply(s))
        is CEq    -> CEq(t.apply(s), u.apply(s))
        is CEmpty -> CEmpty
    }

    companion object {
        fun conjunction(constraints: Iterable<Constraint>): Constraint = constraints.fold(CEmpty, ::CAnd)
    }

    infix fun and(other: Constraint): Constraint = CAnd(this, other)

    /** Convert tree constraint to a set of primitive constraints */
    fun toSet() : Set<Constraint> = when (this) {
        is CEmpty -> setOf()
        is CEq    -> setOf(this)
        is CAnd   -> l.toSet() union r.toSet()
    }
}

object CEmpty: Constraint() { override fun toString() = "ε"}
data class CEq(val t: LType, val u: LType): Constraint() { override fun toString() = "$t ~ $u" }
data class CAnd(val l: Constraint, val r: Constraint): Constraint() {
    // Avoids showing epsilons
    override fun toString() = when (l) {
        is CEmpty -> when (r) {
            is CEmpty -> ""
            else      -> "$r"
        }
        else -> when (r) {
            is CEmpty -> "$l"
            else      -> "$l & $r"
        }
    }
}


