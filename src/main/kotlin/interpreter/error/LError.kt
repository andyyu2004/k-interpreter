package interpreter.error

import interpreter.lexing.Token

data class LError(val token: Token, val msg: String) {
    override fun toString() : String {
        val (_, col, line) = token
        return if (col < 0 || line <= 0) msg
        else "$line:$col:$msg"
    }
}
