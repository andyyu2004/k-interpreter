package interpreter.typechecking

import interpreter.error.*
import interpreter.lexing.toTok
import interpreter.parsing.Expr
import interpreter.typechecking.types.*
import interpreter.util.NameGenerator
import interpreter.util.mapM
import interpreter.util.mapM_

class Typechecker {
    private val names = NameGenerator()
    private val ctx = hashMapOf<String, TScheme>()

    fun typecheck(expr: Expr) : Either<List<LError>, LType> {
        names.reset()
        when (val res = annotate(expr)) { is Left -> return Left(listOf(res.l)) }
        return constrain(expr)
            .assert {
                println(it)
                it.checkConsistency()
            }
            .bind(::unifyConstraint)
            .map {
                println(it)
                val type = expr.type!!.apply(it)
                println("type: $type")
                type
            }.catch(::listOf)
        }

    private fun gen() : TVar = TVar(names.gen().toTok())

    private fun annotate(expr: Expr): Either<LError, Unit> {
        // Generates a type for the top level of each expression if not already annotated
        genUnificationVar(expr)
        when (expr) {
            is Expr.EInt   -> assert(expr, TInt)
            is Expr.EBool  -> assert(expr, TBool)
            is Expr.Group  -> annotate(expr.inner)
            // Bind each function parameter in the context, creating variable if one isn't already annotated
            is Expr.Lambda -> {
                expr.params = expr.params.map { (token, type) ->
                    val newtype = type ?: gen()
                    ctx[token.lexeme] = TScheme(setOf(), newtype)
                    Pair(token, newtype)
                }
                annotate(expr.body)
                if (expr.ret == null) expr.ret = expr.body.type
                Right(Unit)
            }
            // Recursively annotate the function expression and each argument
            is Expr.Apply  -> annotate(expr.f) andLeft expr.args.mapM_ { annotate(it) }
            is Expr.Var -> when (val scheme = ctx[expr.token.lexeme]) {
                null -> Left(LError(expr.token, "Unbound variable ${expr.token.lexeme}"))
                else -> { expr.type = scheme.instantiate(this::gen); Right(Unit) }
            }
            else -> Right(Unit)
        }
        return Right(Unit)
    }

    // Can safely unwrap all types due to the annotation
    private fun constrain(expr: Expr) : Either<LError, Constraint> = when (expr) {
        is Expr.EBool  -> Right(CEq(expr.type!!, TBool))
        is Expr.EInt   -> Right(CEq(expr.type!!, TInt))
        is Expr.Group  -> constrain(expr.inner) map { expr.type!! eq expr.inner.type!! and it }
        is Expr.Lambda -> {
            val paramType = TTuple(expr.params.map { (_, type) -> type!! })
            val clambda = paramType arrow expr.ret!! eq expr.type!!
            val cret = expr.ret!! eq expr.body.type!!
            constrain(expr.body).map { cbody -> Constraint.conjunction(setOf(clambda, cbody, cret)) }
        }
        is Expr.Apply -> {
            val cfn = constrain(expr.f)
            val cargs = expr.args.mapM(this::constrain)
            Either.sequence2(Pair(cfn, cargs)).map { (c, cs) ->
                // f :: (arg0, ..., argn) -> ret
                val targs = TTuple(expr.args.map { it.type!! })
                val capp = CEq(expr.f.type!!, targs arrow expr.type!!)
                c and capp and Constraint.conjunction(cs)
            }
        }
        is Expr.Var -> Right(CEmpty)
        else -> TODO("not impl for $expr")
    }

    // Same as below, but also returns the new type
    private fun genUnificationVar(expr: Expr): LType {
        if (expr.type == null) expr.type = gen()
        return expr.type!!
    }

    private fun unifyConstraint(constraint: Constraint): Either<LError, Substitution> = when (constraint) {
        is CEmpty -> Right(Substitution.empty())
        is CEq    -> constraint.t unify constraint.u
        is CAnd   -> Either.sequence2(Pair(
            unifyConstraint(constraint.l),
            unifyConstraint(constraint.r))
        ).map { (s0, s1) -> s0 compose s1 }
    }

    // If the type of an expr is known, assert that the annotation is correct, and if no annotation set its type
    private fun assert(expr: Expr, type: LType) : Either<LError, Unit> {
        if (expr.type != null && expr.type != type)
            return Left(LError(expr.token, "expression of type `$type` annotated with type `${expr.type}'"))
        expr.type = type
        return Right(Unit)
    }
}




























