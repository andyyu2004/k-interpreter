package interpreter.util

class NameGenerator {
    private var i = 0
    private var n = 0
    private var src = ('a'..'z').toList()

    fun reset() {
        i = 0
        n = 0
    }

    fun gen(): String {
        val name = if (n != 0) "${src[i]}$n" else "${src[i]}"
        i = (i + 1) % src.count()
        if (i == 0) n++
        return name
    }
}
