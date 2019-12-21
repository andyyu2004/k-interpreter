package parser.parsing

import parser.error.Either
import parser.error.*
import parser.lexing.Token
import parser.lexing.TokenType

class Parser(private val grammar: Grammar) {

    private var tokens = listOf<Token>()
    private var i = 0

    private val precedence
        get() = grammar.infixParselets[peek().type]?.precedence ?: 0

    fun parse(tokens: List<Token>): Either<String, Expr>{
        this.tokens = tokens
        i = 0
        return parseExpression(0)
    }

    fun parseExpression(precedence: Int): Either<String, Expr> {
        var token = next() ?: return Left("Ran out of tokens")
        val prefix = grammar.prefixParselets[token.type]
            ?: return Left("Failed to parse prefix operator $token")

        var expr = prefix.parse(this, token)

        // Loop will also stop if there is no binary operator as this.precedence = 0 in that case
        while (precedence < this.precedence) {
            // Can safely unwrap these two optionals due to the while condition
            token = next()!!
            val infix = grammar.infixParselets[token.type]!!
            expr = expr bind { infix.parse(this, token, it) }
        }

        return expr
    }

    private fun next() : Token? {
        return if (i >= tokens.count()) null
        else tokens[i++]
    }

    private fun peek() = tokens[i]

    fun expect(type: TokenType) : Either<String, Token> {
        val next = next()
        return if (next?.type != type) Left("Expected $type found ${next?.type}")
        else Right(next)
    }

}















