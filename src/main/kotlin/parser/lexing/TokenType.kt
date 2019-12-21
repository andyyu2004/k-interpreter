package parser.lexing

enum class TokenType {
    // Symbols
    Plus, DPlus, TPlus, Minus, Star, Slash,
    DMinus,
    Bind,
    Bang, BangEqual,
    Tilde,
    Colon,
    Caret,
    LParen, RParen,
    Equal, DEqual, QMark,
    Dot, Comma,
    LBrace, RBrace, DColon, SemiColon,
    RFArrow, RArrow,
    // Keywords
    Let, Var, Fn,
    // Token Classes
    Integral, EOF, Float, String, Identifier,
}
