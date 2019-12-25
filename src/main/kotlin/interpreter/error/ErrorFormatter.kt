package interpreter.error

import com.andreapivetta.kolor.Color
import com.andreapivetta.kolor.Kolor
import interpreter.lexing.TokenType

class ErrorFormatter {

    var src = listOf<String>()

    fun print(errors: List<LError>) {
        // line starts from 1, col from 0
        val errmsg = errors.joinToString("\n") {
            if (it.token.line <= 0 || it.token.type == TokenType.EOF) "$it"
            else "$it in `${src[it.token.line - 1]}`"
        }

        println(Kolor.foreground(errmsg, Color.LIGHT_RED))
    }
}