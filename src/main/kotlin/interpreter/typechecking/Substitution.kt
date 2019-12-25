package interpreter.typechecking

import interpreter.typechecking.types.LType

data class Substitution(val map: Map<String, LType>) {

    fun <T> map(f: (LType) -> T) = map.mapValues { (_, v) -> f(v) }

    // set addition (union) is right-biased
    // left-right composition
    infix fun compose(s: Substitution) = Substitution(s.map + map { it.apply(s) } )

    override fun toString() = "{ ${map.entries.joinToString(", ") { (k, v) -> "$k :-> $v" }} }"

    companion object {
        fun fromList(xs: Iterable<Pair<String, LType>>) = Substitution(xs.toMap())
        fun empty() = Substitution(mapOf())
        fun single(tvar: String, type: LType) = Substitution(mapOf(tvar to type))
    }
}

interface Substitutable<T> {
    // Free Type Variables
    fun ftv(): Set<String>
    fun apply(s: Substitution): T
}


