package parser.lexing

data class Token(
    val lexeme: String,
    val col: Int,
    val line: Int,
    val type: TokenType
) {
    override fun toString() = lexeme
}
