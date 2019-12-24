package parser.error

import parser.util.Quadruple

sealed class Either<L, R> {
    abstract infix fun <T> map(f: (R) -> T) : Either<L, T>
    abstract infix fun <T> bind(f: (R) -> Either<L, T>) : Either<L, T>
    abstract infix fun <T> bindLeft(f: (L) -> Either<T, R>) : Either<T, R>
    abstract infix fun <T> catch(f: (L) -> T) : Either<T, R>
    abstract infix fun <T> and(x: Either<L, T>): Either<L, T>
    // Can assert the an Either is Right, or assert a boolean (wrapped in thunk to only evaluate if necessary)
    abstract infix fun <T> assert(x: Either<L, T>): Either<L, R>
    abstract fun assert(p: () -> Boolean, error: Either<L, R>): Either<L, R>
    abstract fun isLeft() : Boolean
    abstract fun unwrap(): R

    companion object {
        fun <L, R> sequence(xs: Collection<Either<L, R>>) : Either<L, List<R>> {
            val acc = mutableListOf<R>()
            for (x in xs) {
                when (val res = x.map { acc.add(it) }) {
                    is Left -> return Left(res.l)
                }
            }
            return Right(acc)
        }

        fun <A, B, C, L> sequence3(triple: Triple<Either<L, A>, Either<L, B>, Either<L, C>>) : Either<L, Triple<A, B, C>> {
            val (a, b, c) = triple
            return a.bind { x -> b.bind { y -> c.map { z -> Triple(x, y, z) } } }
        }

        fun <A, B, C, D, L> sequence4(quad: Quadruple<Either<L, A>, Either<L, B>, Either<L, C>, Either<L, D>>) : Either<L, Quadruple<A, B, C, D>> {
            val (a, b, c, d) = quad
            return a.bind { x -> b.bind { y -> c.bind { z -> d.map { Quadruple(x, y, z, it) } } } }
        }

    }
}

class Right<L, R>(val r: R) : Either<L, R>() {
    override fun <T> map(f: (R) -> T): Either<L, T> = Right(f(r))
    override fun <T> bind(f: (R) -> Either<L, T>): Either<L, T> = f(r)
    override fun <T> bindLeft(f: (L) -> Either<T, R>): Either<T, R> = Right(r)
    override fun <T> catch(f: (L) -> T): Either<T, R> = Right(r)
    override fun <T> and(x: Either<L, T>): Either<L, T> = x
    override fun isLeft() = false
    override fun unwrap(): R = r
    override fun toString() = "Right {${r}}"
    // Expect fails if the parameter is left, but unlike and keeps the old value
    override fun <T> assert(x: Either<L, T>): Either<L, R> = x.and(this)
    override fun assert(p: () -> Boolean, error: Either<L, R>): Either<L, R> = if (p()) Right(r) else error
}

class Left<L, R>(val l: L) : Either<L, R>() {
    override fun <T> map(f: (R) -> T): Either<L, T> = Left(l)
    override fun <T> bind(f: (R) -> Either<L, T>): Either<L, T> = Left(l)
    override fun <T> bindLeft(f: (L) -> Either<T, R>): Either<T, R> = f(l)
    override fun <T> catch(f: (L) -> T) : Either<T, R> = Left(f(l))
    override fun <T> and(x: Either<L, T>): Either<L, T> = Left(l)
    override fun <T> assert(x: Either<L, T>): Either<L, R> = Left(l)
    override fun assert(p: () -> Boolean, error: Either<L, R>): Either<L, R> = Left(l)
    override fun isLeft() = true
    override fun unwrap(): R = error("Unwrapped a left")
    override fun toString() = "Left {${l}}"
}



