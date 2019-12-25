package interpreter.util

fun String.padLeft(depth: Int, padding: String = " ") = padding.repeat(depth) + this
