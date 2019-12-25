//fun eval(expr: Expr) : Double =
//    when (expr) {
//        is Expr.Num -> expr.num
//        is Expr.Bin -> evalBin(expr.op.type, expr.left, expr.right)
//    }
//
//fun evalBin(op: AOp, left: Expr, right: Expr) =
//    when (op) {
//        AOp.Mult  -> eval(left) * eval(right)
//        AOp.Div   -> eval(left) / eval(right)
//        AOp.Plus  -> eval(left) + eval(right)
//        AOp.Minus -> eval(left) - eval(right)
//        AOp.Exp   -> eval(left).pow(eval(right))
//    }


class Interpreter {

}
