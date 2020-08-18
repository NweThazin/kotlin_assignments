package nicestring

fun String.isNice(): Boolean {

    val vowelCount =
            this.count { ch -> ch == 'a' || ch == 'e' || ch == 'i' || ch == 'o' || ch == 'u' }
    if (vowelCount >= 3) {
        //condition 2 is correct check 1 and 3 again
        if (isNotSubstringContained(this) || isRepeatedCharacters(this)) {
            //condition 1 and 3 is correct
            return true
        }
    } else {
        //check condition 1 and 3
        //condition 1 and 3 must be correct since condition 2 is wrong
        if (isNotSubstringContained(this) && isRepeatedCharacters(this)) {
            //condition 1 and 3 is correct
            return true
        }
    }
    return false
}

private fun isRepeatedCharacters(str: String): Boolean {
    //condition 3
    str.zipWithNext { first, second ->
        if (first == second) {
            return true
        }
    }
    return false
}

private fun isNotSubstringContained(str: String): Boolean {
    //condition 1
    if (str.contains("bu") || str.contains("ba") || str.contains("be")) {
        return false
    }
    return true
}