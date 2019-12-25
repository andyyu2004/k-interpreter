package interpreter.typechecking

import interpreter.error.*
import interpreter.typechecking.types.LType
import interpreter.typechecking.types.TVar

sealed class Constraint {
    companion object {
        fun conjunction(constraints: Iterable<Constraint>): Constraint = constraints.fold(CEmpty, ::CAnd)
    }

    infix fun and(other: Constraint): Constraint = CAnd(this, other)

    // Checks for inconsistent constraints before attemption to unify
    // Consistency in this context means two eq constraints do not bind to different types
    // e.g. a ~ Bool & a ~ Int
    fun checkConsistency() : Either<LError, Unit> {
        val constraints = toSet()
        // Map containing the first encountered constraint for a particular type variable
        val map = hashMapOf<String, LType>()

        /** Check the tvar is consistent with the type in the constraint */
        fun check(tvar: TVar, type: LType) : Either<LError, Unit> {
            // If the right side is variable also, not inconsistent
            if (type is TVar) return Right(Unit)
            if (map.containsKey(tvar.token.lexeme)) {
                if (map[tvar.token.lexeme] != type) return Left(LError(tvar.token, "Inconsistent type annotations"))
            } else map[tvar.token.lexeme] = type
            return Right(Unit)
        }

        for (c in constraints) {
            if (c !is CEq) continue
            val res: Either<LError, Unit> = when {
                c.t is TVar -> check(c.t, c.u)
                c.u is TVar -> check(c.u, c.t)
                else -> Right(Unit)
            }
            if (res.isLeft()) return res
        }

        return Right(Unit)
    }

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
            else      -> "$l\n$r"
        }
    }
}


