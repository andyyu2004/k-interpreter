package parser.types

import parser.lexing.Token

sealed class LType

data class TArrow(val l: LType, val r: LType) : LType() {
    override fun toString() = when (l) {
        is TArrow -> "($l) -> $r"
        else      -> "$l -> $r"
    }
}

/** Just holds a token representing a type which can be processed later */
data class TName(val token: Token): LType() {
    override fun toString() = token.lexeme
}