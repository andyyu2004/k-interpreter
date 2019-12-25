package interpreter

import com.andreapivetta.kolor.Color
import com.andreapivetta.kolor.Kolor
import interpreter.error.ErrorFormatter
import org.jline.reader.LineReader
import org.jline.reader.UserInterruptException
import org.jline.reader.impl.LineReaderImpl
import org.jline.reader.impl.history.DefaultHistory
import org.jline.terminal.TerminalBuilder
import interpreter.lexing.Lexer
import interpreter.lexing.generateSyntax
import interpreter.parsing.Parser
import interpreter.parsing.generateGrammar
import interpreter.typechecking.Substitution
import interpreter.typechecking.Typechecker
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
    val typechecker = Typechecker()
    val formatter = ErrorFormatter()

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
            formatter.src = line.split("\n")

            if (line.isBlank()) continue
            lexer.lex(line).bind {
                // println(it)
                // Only parse expressions in interactive mode
                parser.parse(it)
            }.map {
                println(it.fmtp())
                println(it)
                it
            }.bind {
                typechecker.typecheck(it)
            }.catch(formatter::print)


        }

    } catch (e: UserInterruptException) { }

}


















