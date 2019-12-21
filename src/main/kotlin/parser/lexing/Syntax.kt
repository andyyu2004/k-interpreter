package parser.lexing


fun generateSyntax() : LexSyntax {
    val symbols = listOf(
        Regex("^\\+\\+\\+") to TokenType.TPlus,
        Regex("^\\+\\+") to TokenType.DPlus,
        Regex("^\\+") to TokenType.Plus,
        Regex("^\\^") to TokenType.Caret,
        Regex("^--") to TokenType.DMinus,
        Regex("^-") to TokenType.Minus,
        Regex("^!") to TokenType.Bang,
        Regex("^~") to TokenType.Tilde,
        Regex("^/") to TokenType.Slash,
        Regex("^\\*") to TokenType.Star,
        Regex("^>>=") to TokenType.Bind,
        Regex("^[_a-zA-Z][a-zA-Z0-9]*") to TokenType.Identifier,
        Regex("^0|[1-9][0-9]*") to TokenType.Float,
        Regex("^0|[1-9][0-9]*") to TokenType.Integral
    )

    val singlelineComment = Regex("^//.*\\n")
    val multilineComment  = Regex("^/\\*.*\\*/")

    return LexSyntax(symbols, singlelineComment, multilineComment)
}

