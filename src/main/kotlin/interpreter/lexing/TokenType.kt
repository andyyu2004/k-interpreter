package interpreter.lexing

enum class TokenType {
    // Symbols
    Plus, DPlus, TPlus, Minus, Star, Slash,
    DMinus,
    Bind,
    Backslash,
    Bang, BangEqual,
    Tilde,
    Colon,
    Caret,
    LParen, RParen,
    Equal, DEqual, QMark,
    Dot, Comma,
    LBrace, RBrace, DColon, Semicolon,
    RFArrow, RArrow,

    // Keywords
    Let, Var, Fn, In,
    // Primitive Types
    Bool, Int, String,

    // Token Classes
    Integral, EOF, Float, Identifier, Unknown,
    Typename, Typevar,
    False, True,
}
