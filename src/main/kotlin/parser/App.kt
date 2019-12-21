package parser

import parser.lexing.*
import org.jline.reader.*
import org.jline.terminal.*
import org.jline.reader.impl.history.DefaultHistory
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
            print("λ ")
            val line = reader.readLine()
            lexer.lex(line).bind {
//                println(it)
                parser.parse(it)
            }.map{
                println(it.fmtp())
                println(it)
                it
            }.catch(::println)

        }

    } catch (e: UserInterruptException) { }

}


















