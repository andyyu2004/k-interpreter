package interpreter.parsing

import interpreter.error.Either
import interpreter.error.*
import interpreter.lexing.Token
import interpreter.lexing.TokenType

class Parser(private val grammar: Grammar) {

    private var tokens = listOf<Token>()
    private var i = 0

    private var backtrackIndex = 0

    private val precedence
        get() = grammar.leftParselets[peek()?.type]?.precedence ?: 0

    // The entry point of the parser intended to be called from the repl
    public fun parse(tokens: List<Token>): Either<List<LError>, Expr>{
        this.tokens = tokens
        i = 0

        // val errors = mutableListOf<String>();

        // Assertation only runs lazily
        return parseExpression(0)
            .assert({ tokens[i].type == TokenType.EOF }, Left(LError(tokens[i], "Did not consume all input during parsing")))
            .catch(::listOf)
    }

    public fun parseProgram() {

    }

    // Don't make precedence (right binding power, rbp) default to 0 to avoid missing parameter mistakes
    fun parseExpression(precedence: Int): Either<LError, Expr> {
        var token = next() ?: return Left(LError(tokens[i - 1], "Ran out of tokens"))
        val prefix = grammar.nullParselets[token.type]
            ?: return error(token, "Failed to parse null denotation operator $token")

        var expr = prefix.parse(this, token)

        // Loop will also stop if there is no binary operator as this.precedence = 0 in that case
        // Continue loop until the `next` operator's precedence is lower than the current precedence.
        // Quit once a lower precedence operator is found
        while (precedence < this.precedence) {
            // Can safely unwrap these two optionals due to the while condition
            token = next()!!
            val infix = grammar.leftParselets[token.type]!!
            expr = expr bind { infix.parse(this, token, it) }
        }

        return expr
    }

    fun backtrack() { i = backtrackIndex }
    fun markBacktrackPoint() { backtrackIndex = i }

    private fun next() : Token? {
        return if (i >= tokens.count()) null
        else tokens[i++]
    }

    fun peek() = tokens.getOrNull(i)

    public fun lookahead(n: Int) : Token? {
        return if (i + n >= tokens.count()) null
        else tokens[i + n]
    }

    /** Returns error if the current token is not the expected */
    fun expect(type: TokenType) : Either<LError, Token> {
        val curr = peek()
        return if (curr?.type != type) error(curr!!, "Expected $type found ${curr.type}")
        else { i++; Right(curr) }
    }

    /** Return whether current token matches the parameter
     * Skips the token on match otherwise remains the same */
    fun match(type: TokenType) : Boolean {
        if (peek()?.type == type) {
            i++;
            return true
        }
        return false
    }

    // Generate error from a token and error message
    // val error: (Token, String) -> Left<String, Any> = { (line, col), err ->  }
    fun <T> error(token: Token, err: String) : Left<LError, T> = Left(LError(token, err))

}















