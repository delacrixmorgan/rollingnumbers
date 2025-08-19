package io.dontsayboj.rollingnumbers.ui

import io.dontsayboj.rollingnumbers.getDecimalSeparator
import io.dontsayboj.rollingnumbers.getGroupingSeparator
import kotlin.math.pow
import kotlin.math.round

fun Int.format(
    groupingSeparator: Char = getGroupingSeparator(),
    signed: Boolean = true,
): String {
    // Check if number is negative
    val isNegative = this < 0
    val absoluteValue = if (isNegative) -this else this

    // Convert to string and build with grouping separator
    val integerString = absoluteValue.toString()
    val groupedInt = buildString {
        var count = 0
        for (i in integerString.length - 1 downTo 0) {
            append(integerString[i])
            count++
            if (count % 3 == 0 && i != 0) append(groupingSeparator)
        }
    }.reversed()

    return if (signed) {
        if (isNegative) "-$groupedInt" else groupedInt
    } else {
        groupedInt
    }
}

fun Double.format(
    decimals: Int = 2,
    decimalSeparator: Char = getDecimalSeparator(),
    groupingSeparator: Char = getGroupingSeparator(),
    signed: Boolean = true,
): String {
    val factor = 10.0.pow(decimals)
    val rounded = round(this * factor) / factor

    // Check if number is negative
    val isNegative = rounded < 0
    val absoluteValue = if (isNegative) -rounded else rounded

    // Custom conversion to avoid scientific notation
    val (integerPart, fractionPart) = convertDoubleToStringParts(absoluteValue, decimals)

    // Build integer with grouping separator
    val groupedInt = buildString {
        var count = 0
        for (i in integerPart.length - 1 downTo 0) {
            append(integerPart[i])
            count++
            if (count % 3 == 0 && i != 0) append(groupingSeparator)
        }
    }.reversed()

    // Build fractional part
    val fraction = buildString {
        append(fractionPart)
        val missingZeros = decimals - fractionPart.length
        repeat(missingZeros) { append('0') }
    }

    val result = "$groupedInt$decimalSeparator$fraction"
    return if (signed) {
        if (isNegative) "-$result" else result
    } else {
        result
    }
}

/**
 * Converts a Double to string parts without scientific notation
 * Returns Pair<integerPart, fractionalPart>
 */
private fun convertDoubleToStringParts(value: Double, decimals: Int): Pair<String, String> {
    if (value == 0.0) return Pair("0", "")
    
    // For small numbers that won't have scientific notation, use standard conversion
    val standardString = value.toString()
    if (!standardString.contains('E') && !standardString.contains('e')) {
        val parts = standardString.split('.')
        return Pair(parts[0], if (parts.size > 1) parts[1] else "")
    }
    
    // Handle large numbers that would use scientific notation
    var workingValue = value
    val integerDigits = mutableListOf<Char>()
    
    // Extract integer part digit by digit
    if (workingValue >= 1.0) {
        // For very large numbers, we need to extract digits carefully
        var tempValue = workingValue
        val digits = mutableListOf<Int>()
        
        // Find the magnitude (number of digits)
        var magnitude = 0
        var testValue = tempValue
        while (testValue >= 10.0) {
            testValue /= 10.0
            magnitude++
        }
        
        // Extract each digit from most significant to least significant
        for (i in magnitude downTo 0) {
            val divisor = 10.0.pow(i)
            val digit = (tempValue / divisor).toInt()
            digits.add(digit)
            tempValue -= digit * divisor
        }
        
        // Convert digits to string
        digits.forEach { integerDigits.add('0' + it) }
        workingValue = tempValue
    } else {
        integerDigits.add('0')
    }
    
    // Extract fractional part
    val fractionalDigits = mutableListOf<Char>()
    var fractionalValue = workingValue - workingValue.toInt()
    
    repeat(decimals) {
        fractionalValue *= 10
        val digit = fractionalValue.toInt()
        fractionalDigits.add('0' + digit)
        fractionalValue -= digit
    }
    
    return Pair(
        integerDigits.joinToString(""),
        fractionalDigits.joinToString("")
    )
}
