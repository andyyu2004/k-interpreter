package parser.lexing


fun generateSyntax() : LexSyntax {
    val symbols = listOf(
        // Symbols
//        Regex("^\\+\\+\\+") to TokenType.TPlus,
        Regex("^\\+\\+") to TokenType.DPlus,
        Regex("^\\+")    to TokenType.Plus,
        Regex("^\\^")    to TokenType.Caret,
        Regex("^\\\\")   to TokenType.Backslash,
        Regex("^--")     to TokenType.DMinus,
        Regex("^!=")     to TokenType.BangEqual,
        Regex("^=>")     to TokenType.RFArrow,
        Regex("^->")     to TokenType.RArrow,
        Regex("^==")     to TokenType.DEqual,
        Regex("^~")      to TokenType.Tilde,
        Regex("^/")      to TokenType.Slash,
        Regex("^\\*")    to TokenType.Star,
        Regex("^>>=")    to TokenType.Bind,
        Regex("^\\(")    to TokenType.LParen,
        Regex("^\\)")    to TokenType.RParen,
        Regex("^\\?")    to TokenType.QMark,
        Regex("^::")     to TokenType.DColon,
        Regex("^\\{")    to TokenType.LBrace,
        Regex("^}")      to TokenType.RBrace,
        Regex("^:")      to TokenType.Colon,
        Regex("^,")      to TokenType.Comma,
        Regex("^\\.")    to TokenType.Dot,
        Regex("^=")      to TokenType.Equal,
        Regex("^;")      to TokenType.SemiColon,
        Regex("^!")      to TokenType.Bang,
        Regex("^-")      to TokenType.Minus,

        // Token Classes
        Regex("^'[a-z]+")                to TokenType.Typevar,
        Regex("^[A-Z][a-zA-Z]*")         to TokenType.Typename,
        Regex("^[_a-zA-Z][a-zA-Z0-9]*")  to TokenType.Identifier,
        Regex("^0|[1-9][0-9]*\\.[0-9]+") to TokenType.Float,
        Regex("^0|[1-9][0-9]*")          to TokenType.Integral,
        Regex("\"([^\"]|\\.)*\"")        to TokenType.String
    )

    val keywords = mapOf(
        "let"   to TokenType.Let,
        "var"   to TokenType.Var,
        "fn"    to TokenType.Fn
    )
    val singlelineComment = Regex("^//.*(\\n|\\z)")
    val multilineComment  = Regex("^/\\*.*\\*/")

    return LexSyntax(symbols, keywords, singlelineComment, multilineComment)
}

