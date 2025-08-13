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

    // Split integer and fractional parts of absolute value
    val parts = absoluteValue.toString().split('.')
    val integerPart = parts[0]
    val fractionPart = if (parts.size > 1) parts[1] else ""

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
