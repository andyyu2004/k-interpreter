package parser.parsing

import parser.error.Either
import parser.error.*
import parser.lexing.Token
import parser.lexing.TokenType

class Parser(private val grammar: Grammar) {

    private var tokens = listOf<Token>()
    private var i = 0

    private var backtrackIndex = 0

    private val precedence
        get() = grammar.leftParselets[peek()?.type]?.precedence ?: 0

    fun parse(tokens: List<Token>): Either<String, Expr>{
        this.tokens = tokens
        i = 0
        return parseExpression(0)
                .assert({ tokens[i].type == TokenType.EOF }, Left("Did not consume all input during parsing"))
    }

    // Don't make precedence (right binding power, rbp) default to 0 to avoid missing parameter mistakes
    fun parseExpression(precedence: Int): Either<String, Expr> {
        backtrackIndex = i
        var token = next() ?: return Left("Ran out of tokens")
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

    private fun next() : Token? {
        return if (i >= tokens.count()) null
        else tokens[i++]
    }

    fun peek() = tokens.getOrNull(i)

    /** Returns error if the current token is not the expected */
    fun expect(type: TokenType) : Either<String, Token> {
        val next = next()!!
        return if (next.type != type) error(next, "Expected $type found ${next.type}")
        else Right(next)
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
    fun <T> error(token: Token, err: String) : Left<String, T> =
        Left("${token.line}:${token.col}:$err")

}















