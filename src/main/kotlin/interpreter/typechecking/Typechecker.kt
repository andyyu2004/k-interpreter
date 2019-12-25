package interpreter.typechecking

import interpreter.error.*
import interpreter.lexing.Token
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
        ctx.clear()
        when (val res = annotate(expr)) { is Left -> return Left(listOf(res.l)) }
        return constrain(expr)
            .map { println(it); it }
            .bind(::unifyConstraint)
            .map { println("sub: $it"); expr.type!!.apply(it) }
            .map(LType::normalizeTypeVariables)
            .catch(::listOf)
        }

    private fun gen() : TVar = TVar(names.gen().toTok())

    private fun annotate(expr: Expr): Either<LError, Unit> {
        // Generates a type for the top level of each expression if not already annotated
        genUnificationVar(expr)
        return when (expr) {
            is Expr.EInt   -> assert(expr, TInt)
            is Expr.EBool  -> assert(expr, TBool)
            is Expr.Group  -> annotate(expr.inner).map { expr.type = expr.inner.type }
            // Bind each function parameter in the context, creating variable if one isn't already annotated
            is Expr.Lambda -> {
                expr.params = expr.params.map { (token, type) ->
                    val newtype = type ?: gen()
                    ctx[token.lexeme] = TScheme(setOf(), newtype)
                    Pair(token, newtype)
                }
                return annotate(expr.body).map { if (expr.ret == null) expr.ret = expr.body.type }
            }
            // Recursively annotate the function expression and each argument
            is Expr.Apply -> annotate(expr.f) and expr.args.mapM_ { annotate(it) }
            is Expr.Var   -> when (val scheme = ctx[expr.token.lexeme]) {
                null -> Left(LError(expr.token, "Unbound variable ${expr.token.lexeme}"))
                else -> { expr.type = scheme.instantiate(this::gen); Right(Unit) }
            }
            else -> Right(Unit)
        }
    }

    // Can safely unwrap all types due to the annotation
    private fun constrain(expr: Expr) : Either<LError, Constraint> = when (expr) {
        is Expr.EBool  -> Right(CEq(expr.type!!, TBool))
        is Expr.EInt   -> Right(CEq(expr.type!!, TInt))
        is Expr.Group  -> constrain(expr.inner)
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

    /**
     * WARNING!!!
     * Be careful not to write code like the following
     * is CAnd -> Either.sequence2(Pair(
     *      unifyConstraint(constraint.l),
     *      unifyConstraint(constraint.r))
     * ).map { (s0, s1) -> s0 compose s1 }
     * The information obtained from unifying prior constraints must be applied before unifying the other one
     * Otherwise you can get { a :-> b } compose { a :-> c } and you are screwed!
     */
    private fun unifyConstraint(constraint: Constraint): Either<LError, Substitution> = when (constraint) {
        is CEmpty -> Right(Substitution.empty())
        is CEq    -> unify(constraint.t, constraint.u)
        is CAnd   -> unifyConstraint(constraint.l).bind { s0 -> unifyConstraint(constraint.r.apply(s0)).map { s1 -> s0 compose s1 } }
    }

    private fun unify(l: LType, r: LType): Either<LError, Substitution> = when {
        l == r                     -> Right(Substitution.empty())
        l is TVar                  -> l bind r
        r is TVar                  -> r bind l
        l is TArrow && r is TArrow -> unifyConstraint((l.l eq r.l) and (l.r eq r.r))
        l is TTuple && r is TTuple && l.types.count() == r.types.count() ->
            unifyConstraint(Constraint.conjunction(l.types.zip(r.types).map { (x, y) -> x eq y }))
        else -> Left(LError(Token.dummy(), "Failed to unify type $l with $r"))
    }

    // If the type of an expr is known, assert that the annotation is correct, and if no annotation set its type
    private fun assert(expr: Expr, type: LType) : Either<LError, Unit> {
        if (expr.type != null && expr.type !is TVar && expr.type != type)
            return Left(LError(expr.token, "expression of type `$type` annotated with type `${expr.type}'"))
        expr.type = type
        return Right(Unit)
    }
}




























