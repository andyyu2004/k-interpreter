package interpreter.typechecking.types

import interpreter.error.*
import interpreter.lexing.Token
import interpreter.typechecking.*

sealed class LType: Substitutable<LType> {
    infix fun arrow(other: LType) = TArrow(this, other)
    infix fun eq(other: LType): Constraint = CEq(this, other)

    infix fun unify(t: LType): Either<LError, Substitution> = when {
        this == t                     -> Right(Substitution.empty())
        this is TVar                  -> this bind t
        t is TVar                     -> t bind this
        // Unify respective components and compose the substitutions
        this is TArrow && t is TArrow -> Either.sequence2(Pair(
            l unify t.l,
            r unify t.r
        )).map { (s0, s1) -> s0 compose s1 }
        else -> Left(LError(Token.dummy(), "Failed to unify type $this with $t"))
    }
}

object TInt: LType() {
    override fun toString() = "TInt"
    override fun ftv(): Set<String> = setOf()
    override fun apply(s: Substitution): LType = this
}
object TBool: LType() {
    override fun toString() = "TBool"
    override fun apply(s: Substitution): LType = this
    override fun ftv(): Set<String> = setOf()
}

//object TUnit: LType() {
//    override fun toString() = "TUnit"
//    override fun ftv(): Set<String> = setOf()
//    override fun apply(s: Substitution): LType = this
//}

data class TArrow(val l: LType, val r: LType) : LType() {
    override fun ftv(): Set<String> = l.ftv().union(r.ftv())
    override fun apply(s: Substitution): LType = TArrow(l.apply(s), r.apply(s))
    override fun toString() = when (l) {
        is TArrow -> "($l) -> $r"
        else      -> "$l -> $r"
    }
}

/** Just holds a token representing a type which can be processed later */
data class TName(val token: Token): LType() {
    override fun ftv(): Set<String> {
        TODO()
    }

    override fun apply(s: Substitution): LType {
        TODO()
    }



    override fun toString() = token.lexeme
}

// Type Variable
data class TVar(val token: Token): LType() {
    override fun toString() = token.lexeme
    override fun ftv(): Set<String> = setOf(token.lexeme)

    override fun apply(s: Substitution): LType = when {
        s.map.containsKey(token.lexeme) -> s.map.getValue(token.lexeme)
        else -> this
    }

    // Generate substitution that binds type variable to some type for unification
    infix fun bind(type: LType) : Either<LError, Substitution> = when(type) {
        is TVar -> Right(if (this == type) Substitution.empty() else Substitution.single(token.lexeme, type))
        else -> when {
            occurs(type) -> Left(LError(token, "Occurs check failed: $this occurs in type $type"))
            else -> Right(Substitution.single(token.lexeme, type))
        }
    }

    // Occurs check against infinite types
    private infix fun occurs(type: LType) : Boolean = type.ftv().contains(token.lexeme)
}

data class TTuple(val types: List<LType>): LType() {
    override fun toString() = "(${types.joinToString(", ")})"
    override fun ftv() = types.map { it.ftv() }.fold(setOf<String>(), { acc, x -> acc.union(x) })
    override fun apply(s: Substitution) = TTuple(types.map { it.apply(s) })
}























