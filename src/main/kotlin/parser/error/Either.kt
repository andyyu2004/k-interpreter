package parser.error

sealed class Either<L, R> {
    abstract infix fun <T> map(f: (R) -> T) : Either<L, T>
    abstract infix fun <T> bind(f: (R) -> Either<L, T>) : Either<L, T>
    abstract infix fun <T> bindLeft(f: (L) -> Either<T, R>) : Either<T, R>
    abstract infix fun <T> catch(f: (L) -> T) : Either<T, R>
    abstract fun isLeft() : Boolean

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
    }
}

class Right<L, R>(val r: R) : Either<L, R>() {
    override fun <T> map(f: (R) -> T): Either<L, T> = Right(f(r))
    override fun <T> bind(f: (R) -> Either<L, T>): Either<L, T> = f(r)
    override fun <T> bindLeft(f: (L) -> Either<T, R>): Either<T, R> = Right(r)
    override fun <T> catch(f: (L) -> T): Either<T, R> = Right(r)
    override fun isLeft() = false

    override fun toString() = "Right {${r}}"
}

class Left<L, R>(val l: L) : Either<L, R>() {
    override fun <T> map(f: (R) -> T): Either<L, T> = Left(l)
    override fun <T> bind(f: (R) -> Either<L, T>): Either<L, T> = Left(l)
    override fun <T> bindLeft(f: (L) -> Either<T, R>): Either<T, R> = f(l)
    override fun <T> catch(f: (L) -> T): Either<T, R> = Left(f(l))
    override fun isLeft() = true

    override fun toString() = "Left {${l}}"
}



