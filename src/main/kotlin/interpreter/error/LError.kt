package interpreter.error

import interpreter.lexing.Token

data class LError(val token: Token, val msg: String) {
    override fun toString() : String {
        val (_, col, line) = token
        return "$line:$col:$msg"
    }
}
