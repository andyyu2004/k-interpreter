package parser

import com.andreapivetta.kolor.Color
import com.andreapivetta.kolor.Kolor
import org.jline.reader.LineReader
import org.jline.reader.LineReaderBuilder
import org.jline.reader.UserInterruptException
import org.jline.reader.impl.LineReaderImpl
import org.jline.reader.impl.history.DefaultHistory
import org.jline.terminal.TerminalBuilder
import parser.lexing.Lexer
import parser.lexing.generateSyntax
import parser.parsing.Parser
import parser.parsing.generateGrammar
import java.nio.file.Paths


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

    val reader = LineReaderImpl(terminal)
    reader.setVariable(LineReader.HISTORY_FILE, Paths.get("historyfile.txt"))
    reader.setVariable(LineReader.HISTORY_SIZE, 100)
    val history = DefaultHistory(reader)
    history.attach(reader)

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


















