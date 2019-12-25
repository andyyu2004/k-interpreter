package interpreter.typechecking.types

import interpreter.error.*
import interpreter.lexing.Token
import interpreter.typechecking.*
import interpreter.util.NameGenerator

sealed class LType: Substitutable<LType> {
    infix fun arrow(other: LType) = TArrow(this, other)
    infix fun eq(other: LType): Constraint = CEq(this, other)

    fun normalizeTypeVariables(): LType {
        val g = NameGenerator()
        val map = mutableMapOf<String, String>()
        fun norm(type: LType) : LType = when (type) {
            is TArrow -> TArrow(norm(type.l), norm(type.r))
            is TVar   -> {
                val (name, col, line, tokentype) = type.token
                if (map.containsKey(name)) TVar(Token(map[name]!!, col, line, tokentype))
                else {
                    val newname = g.gen()
                    map[name] = newname
                    TVar(Token(newname, col, line, tokentype))
                }
            }
            is TTuple -> TTuple(type.types.map(::norm))
            else -> type
        }
        return norm(this)
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























