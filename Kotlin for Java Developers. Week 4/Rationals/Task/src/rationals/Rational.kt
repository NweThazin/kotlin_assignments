package rationals

import java.lang.IllegalArgumentException
import java.math.BigInteger

data class Rational(val numerator: BigInteger, val denominator: BigInteger) : Comparable<Rational> {
    init {
        if (denominator == BigInteger.ZERO) {
            throw IllegalArgumentException("Illegal Argument Exception")
        }
    }

    override fun equals(other: Any?): Boolean {
        return if (other is Rational) {
            val first = numerator.toDouble().div(denominator.toDouble())
            val second = other.numerator.toDouble().div(other.denominator.toDouble())
            return first == second
        } else {
            false
        }
    }

    override fun toString(): String {
        return if (denominator == BigInteger.ONE || numerator.rem(denominator) == BigInteger.ZERO) {
            numerator.divide(denominator).toString()
        } else {
            val gcd = numerator.gcd(denominator)
            val num = numerator.div(gcd)
            val deno = denominator.div(gcd)
            "$num/$deno"
        }
    }

    override operator fun compareTo(other: Rational): Int {
        val firstRationalRatio = this.numerator.toDouble() / this.denominator.toDouble()
        val secondRationalRatio = other.numerator.toDouble() / other.denominator.toDouble()
        return when {
            firstRationalRatio > secondRationalRatio -> 1
            firstRationalRatio == secondRationalRatio -> 0
            else -> -1
        }
    }

    operator fun unaryMinus(): Rational {
        val inverseNumerator = -BigInteger.ONE * this.numerator
        return Rational(inverseNumerator, this.denominator)
    }

    operator fun div(other: Rational): Rational {
        val numerator = this.numerator * other.denominator
        val denominator = this.denominator * other.numerator
        return Rational(numerator, denominator)
    }

    operator fun times(other: Rational): Rational {
        val numerator = (this.numerator * other.numerator)
        val denominator = (this.denominator * other.denominator)
        return Rational(numerator, denominator)
    }

    operator fun minus(other: Rational): Rational {
        val numerator = (this.numerator * other.denominator) - (this.denominator * other.numerator)
        val denominator = (this.denominator * other.denominator)
        return Rational(numerator, denominator)
    }

    operator fun plus(other: Rational): Rational {
        val numerator = (this.numerator * other.denominator) + (this.denominator * other.numerator)
        val denominator = (this.denominator * other.denominator)
        return Rational(numerator, denominator)
    }

    override fun hashCode(): Int {
        var result = numerator.hashCode()
        result = 31 * result + denominator.hashCode()
        return result
    }

}

fun String.toRational(): Rational {
    val splitString = this.split("/")

    return if (splitString.size == 1) {
        Rational(splitString.first().toBigInteger(), BigInteger.ONE)
    } else {
        val numerator = splitString.first().toBigInteger()
        val denominator = splitString.last().toBigInteger()
        //find gcd
        val gcd = numerator.gcd(denominator)
        var num = numerator.div(gcd)
        var deno = denominator.div(gcd)
        if (num < BigInteger.ZERO && deno < BigInteger.ZERO) {
            num = num.multiply((-1).toBigInteger())
            deno = deno.multiply((-1).toBigInteger())
        } else if(num > BigInteger.ZERO && deno < BigInteger.ZERO){
            num = num.multiply((-1).toBigInteger())
            deno = deno.multiply((-1).toBigInteger())
        }
        Rational(num, deno)
    }
}

infix fun <T> T.divBy(denominator: T) =
        Rational(this.toString().toBigInteger(), denominator.toString().toBigInteger())

fun main() {
    val half = 1 divBy 2
    val third = 1 divBy 3

    val sum: Rational = half + third
    println(5 divBy 6 == sum)

    val difference: Rational = half - third
    println(1 divBy 6 == difference)

    val product: Rational = half * third
    println(1 divBy 6 == product)

    val quotient: Rational = half / third
    println(3 divBy 2 == quotient)

    val negation: Rational = -half
    println(-1 divBy 2 == negation)

    println((2 divBy 1).toString() == "2")
    println((-2 divBy 4).toString() == "-1/2")
    println("117/1098".toRational().toString() == "13/122")

    val twoThirds = 2 divBy 3
    println(half < twoThirds)

    println(half in third..twoThirds)

    println(2000000000L divBy 4000000000L == 1 divBy 2)

    println("912016490186296920119201192141970416029".toBigInteger() divBy
            "1824032980372593840238402384283940832058".toBigInteger() == 1 divBy 2)
}