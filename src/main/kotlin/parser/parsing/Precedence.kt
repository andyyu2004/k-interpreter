package parser.parsing

object Precedence {
    const val ASSIGNMENT = 1
    const val CONDITIONAL = 2
    const val SUM = 3
    const val PRODUCT = 4
    const val EXPONENT = 5
    const val PREFIX = 6
    const val POSTFIX = 7
    const val INVOKE = 8
}