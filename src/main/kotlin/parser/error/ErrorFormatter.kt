package parser.error

class ErrorFormatter() {

    private lateinit var src: List<String>

    constructor(src: String) : this() {
        this.src = src.split('\n')
    }
}