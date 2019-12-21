package parser.lexing

enum class TokenType {
    Plus, DPlus, TPlus, Minus, Star, Slash,
    DMinus,
    Integral, EOF, Float,
    Identifier,
    Bind,
    Bang,
    Tilde,
    Colon,
    Caret
}
