package interpreter.typechecking

import interpreter.lexing.toTok
import interpreter.typechecking.types.TBool
import interpreter.typechecking.types.TInt
import interpreter.typechecking.types.TVar
import kotlin.test.Test
import kotlin.test.assertEquals

class SubstitutionTest {
    @Test fun testComposeLeftBias() {
        val s0 = Substitution(mapOf(
            "a" to TInt,
            "b" to TVar("t".toTok())
        ))

        val s1 = Substitution(mapOf(
            "a" to TBool
        ))

        assertEquals(s0, s0 compose s1)
    }

    @Test fun testChaining() {
        val s0 = Substitution(mapOf(
            "a" to TInt,
            "b" to TVar("t".toTok())
        ))

        val s1 = Substitution(mapOf(
            "a" to TBool,
            "t" to TInt
        ))

        assertEquals(Substitution(mapOf(
            "a" to TInt,
            "b" to TInt,
            "t" to TInt
        )), s0 compose s1)
    }

    @Test fun testTripleChaining() {
        val s0 = Substitution(mapOf(
                "a" to TInt,
                "b" to TVar("t".toTok()),
                "c" to TVar("x".toTok())
        ))

        val s1 = Substitution(mapOf(
                "a" to TBool,
                "t" to TInt
        ))

        val s2 = Substitution(mapOf(
                "x" to TVar("z".toTok()),
                "t" to TBool
        ))

        assertEquals(Substitution(mapOf(
                "a" to TInt,
                "b" to TInt,
                "t" to TInt,
                "x" to TVar("z".toTok()),
                "c" to TVar("z".toTok())
        )), s0 compose s1 compose s2)
    }
}






