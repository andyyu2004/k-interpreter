package parser

import com.andreapivetta.kolor.Color
import com.andreapivetta.kolor.Kolor
import com.andreapivetta.kolor.red
import com.andreapivetta.kolor.yellow
import parser.lexing.*
import org.jline.reader.*
import org.jline.terminal.*
import parser.parsing.Parser
import parser.parsing.generateGrammar



fun main() {
//    val expr = Expr.Bin(
//        AOp.Mult,
//        Expr.Num(32.0),
//        Expr.Num(50.0)
//    )
//    println(eval(expr))
    System.setProperty("org.jline.terminal.dumb", "true");
    interactive()
}



fun interactive() {
    val lexer = Lexer(generateSyntax())
    val parser = Parser(generateGrammar())

    val terminal = TerminalBuilder.builder()
        .system(true)
        .build()

    val reader = LineReaderBuilder.builder()
        .terminal(terminal)
//        .history(history)
        .build()

//    val history = DefaultHistory(reader)

    try {
        while (true) {
            val line = reader.readLine("λ ")
            if (line.isBlank()) continue
            lexer.lex(line).bind {
                // println(it)
                // Only parse expressions in interactive mode
                parser.parse(it)
            }.map {
                println(it.fmtp())
                println(it)
                it
            }.catch { println(Kolor.foreground(it.joinToString("\n"), Color.LIGHT_RED)) }


        }

    } catch (e: UserInterruptException) { }

}


















