package interpreter.util

import interpreter.error.Either

fun <L, R, T> Iterable<T>.mapM(f: (T) -> Either<L, R>): Either<L, List<R>> = Either.sequence(map(f))
fun <L, R, T> Iterable<T>.mapM_(f: (T) -> Either<L, R>): Either<L, Unit> = Either.sequence_(map(f))