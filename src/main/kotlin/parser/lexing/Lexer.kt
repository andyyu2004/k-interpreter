package parser.lexing

import parser.error.*

class Lexer(private val syntax: LexSyntax) {

    private var line = 0
    private var col = 0
    private var index = 0
    private var src = ""
    private val tokens: MutableList<Token> = mutableListOf()
    private val comments = mutableListOf(syntax.lineComment, syntax.multiComment)


    private fun reset() {
        col = 0
        line = 1
        index = 0
        tokens.clear()
    }

    // Find a better way to do string substringing, copying can't be good
    fun lex(source: String): Either<List<LError>, List<Token>> {
        reset()
        src = source + "\n"
        val errors = mutableListOf<LError>()
        while (src.isNotEmpty()) {
            if (whitespaceComments()) break
            match().catch(errors::add)
        }

        tokens.add(Token("<eof>", col, line, TokenType.EOF))
        return if (errors.isEmpty()) Right(tokens) else Left(errors)
    }

    /** Trim whitespace and comments
     * Return whether the end has been reached */
    private fun whitespaceComments(): Boolean {
        outer@ while (src.isNotEmpty()) {
            for (regex in comments) {
                val res = regex?.find(src) ?: continue
                src = src.substring(res.range.last + 1)
            }

            if (src.isEmpty()) return true

            when (src.first()) {
                '\n' -> { line++; col = 0; src = src.substring(1) }
                '\t' -> { col += 4; src = src.substring(1) }
                '\r' -> { col = 0; src = src.substring(1) }
                ' '  -> { col++; src = src.substring(1) }
                else -> break@outer // If comments have finished trimming and there is no more whitespace then return
            }
        }

        return src.isEmpty()
    }

    private fun match() : Either<LError, Unit> {
        for ((regex, type) in syntax.symbols) {
            val res = regex.find(src) ?: continue
            val lexeme = src.slice(res.range)
            val keyword = syntax.keywords[lexeme]

            if (keyword != null) emitToken(lexeme, keyword)
            else emitToken(lexeme, type)

            return Right(Unit)
        }

        val symbol = src[0]
        // Push it forward to prevent freeze on failure
        src = src.substring(1)
        return Left(LError(Token(symbol.toString(), col, line, TokenType.Unknown), "$line:$col:Failed to lex symbol: `${symbol}`"))
    }

    private fun emitToken(lexeme: String, type: TokenType) {
        tokens.add(Token(lexeme, col, line, type))
        val inc = lexeme.length
        src = src.substring(inc)
        col += inc
    }

}
