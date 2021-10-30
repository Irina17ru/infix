import kotlin.Exception as Exception1

fun isOperator(x: String): Boolean {
    when (x) {
        "+", "-", "*", "/" -> return true
    }
    return false
}

fun prefixToInfix(op: MutableList<String>, final:Boolean = false):String {
    var result = ""
    var flag = false
    var temp: MutableList<String> = ArrayList()

    if(op.size == 3) {
        var mresult = "${op[1]} ${op[0]} ${op[2]}"
        return mresult
    }
    for (i in op.size-1 downTo 0) {
        if(i == 0) flag = true
        if(result == "" && !isOperator(op[i]) && !isOperator(op[i - 1])  && !isOperator(op[i - 2]) ) {
            temp.add(op.removeAt(i))
            continue
        }
        if(isOperator(op[i])) {
            if(result == "" && !isOperator(op[i + 1]) && !isOperator(op[i + 2])) {
                result = op[i+2]
                op.removeAt(i+2)
            }
            if(i != op.lastIndex) {
                result = prefixToInfix(arrayOf(op[i], op[i + 1], result).toMutableList(), flag && final)
                op.removeAt(i+1)
            }
            else {
                result = prefixToInfix(arrayOf(op[i], result, temp.removeAt(temp.lastIndex)).toMutableList(), flag && final)
            }
            op.removeAt(i)
        }
    }
    return result
}

fun carriageFind(exp: String): String {
    //спасибо Владу за эту строчку
    var arr_parts = Regex("""(.+?(( [0-9]+){2,}| [0-9]+${'$'}))""").findAll(exp.toString()).map { it.value.trim() }.toList()
    var res: MutableList<String> = ArrayList()
    var count = 0
    var temp = ""
    println(arr_parts)
    /*
    for (i in arr_exp) {
        if (!isOperator(i)) {
            count += 1
        }
        temp += "$i "
        if (count == 2) {
            arr_parts.add(temp.dropLast(1))
            temp = ""
            count = 0
        }
    }
    if (temp.length > 1) {
        arr_parts.add(temp.dropLast(1))
    }*/
    for (part in arr_parts) {
        var part_lst: MutableList<String> = part.split(" ").map { it -> it.trim() }.toMutableList()
        var operators: MutableList<String> = ArrayList()
        var numbers: MutableList<String> = ArrayList()
        for (i in part_lst) {
            if (isOperator(i)){
                operators.add(i)
            }
            else {
                try {
                    i.toInt()
                    numbers.add(i)
                }
                catch (ex: NumberFormatException) { continue }
            }
        }
        if (numbers.size == operators.size) {
            res.add(0,part_lst.removeAt(0))
        }
        if (numbers.size == 1) {
            res.add(part_lst.removeAt(0))
            continue
        }
        res.add(prefixToInfix(part_lst, arr_parts.size == 1))
    }
    println(res)
    return if(res.size == 1) {
        res[0]
    }
    else {
        prefixToInfix(res, true)
    }
}

fun checker(exp: String): Boolean {
    val lst_exp: List<String> = exp.split(" ").map { it -> it.trim() }
    var arr_exp: MutableList<String> = lst_exp.toMutableList()
    var operators: MutableList<String> = ArrayList()
    var numbers: MutableList<String> = ArrayList()

    for (i in arr_exp) {
        if (isOperator(i)){
            operators.add(i)
        }
        else {
            try {
                i.toInt()
                numbers.add(i)
            }
            catch (ex: NumberFormatException) { continue }
        }
    }
    if ((operators.size >= numbers.size) or (numbers.size - operators.size > 1) or (operators.size == 0) or (numbers.size == 0)) throw IllegalArgumentException("Wrong operators number!")
    return true
}

fun main(args: Array<String>) {
    val exp = "/ + 3 10 * + 2 3 - 3 5"
    /// + 3 10 * + 2 3 - 3 5
    //3 + 10 / 2 + 3 * 3 - 5
    //+ 2 * 2 - 2 1
    //2 + 2 * 2 - 1

    var total: MutableList<String> = ArrayList()
    if (checker(exp)) {
        var st = carriageFind(exp)
        print(st)
    }
}

