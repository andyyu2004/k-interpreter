package parser.lexing

/**
 * Not using a HashMap as ordering will be important.
 * Ordering left-right in precedence
 */

data class LexSyntax(
    val symbols: Iterable<Pair<Regex, TokenType>>,
    val lineComment: Regex,
    val multiComment: Regex? = null
)
