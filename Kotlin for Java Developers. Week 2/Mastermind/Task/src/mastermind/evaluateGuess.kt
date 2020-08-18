package mastermind

data class Evaluation(val rightPosition: Int, val wrongPosition: Int)

fun evaluateGuess(secret: String, guess: String): Evaluation {
    var rightPosition = 0
    var wrongPosition = 0

    var tempSecret = secret
    var tempGuess = guess

    //getting right position
    for ((index, item) in tempGuess.withIndex()) {
        val secretItem = tempSecret[index]
        if (item == secretItem) {
            rightPosition++
            tempSecret = replaceCharByIndex(index, tempSecret)
            tempGuess = replaceCharByIndex(index, tempGuess)
        }
    }

    //getting wrong positions
    for ((index, _) in tempGuess.withIndex()) {
        val item = tempGuess[index]
        if (item != '_' && item in tempSecret) {
            val secretIndex = tempSecret.indexOf(item)
            val secretItem = tempSecret[secretIndex]
            if (index != secretIndex && item == secretItem) {
                wrongPosition++
                tempSecret = tempSecret.removeFirstOccurrence(item)
            }
        }
    }

    return Evaluation(rightPosition = rightPosition, wrongPosition = wrongPosition)
}

private fun replaceCharByIndex(index: Int, str: String): String {
    return str.substring(0, index) + "_" + str.substring(index + 1, str.length)
}

private fun String.removeFirstOccurrence(replaceChar: Char): String {
    return this.replaceFirst(replaceChar, '_')
}
