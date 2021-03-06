package interpreter.lexing

data class Token (
    val lexeme: String,
    val col: Int,
    val line: Int,
    val type: TokenType
) {

    override fun toString() = lexeme
//    override fun toString() = "$lexeme $line:$col"

    companion object {
        fun dummy() = "<dummy>".toTok()
    }
}


fun String.toTok() = Token(this, -1, -1, TokenType.Unknown)
