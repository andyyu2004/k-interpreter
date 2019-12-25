package interpreter.typechecking

import interpreter.typechecking.types.LType
import interpreter.typechecking.types.TVar

data class TScheme(val bound: Set<String>, val type: LType) {
    // Generate a fresh name for every bound variable and substitute them into the type
    fun instantiate(namegen: () -> TVar) : LType {
        val instantiated= bound.map { namegen() }
        val substitution = Substitution.fromList(bound.zip(instantiated))
        return type.apply(substitution)
    }
}
