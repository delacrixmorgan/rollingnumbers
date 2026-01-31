@file:OptIn(ExperimentalWasmJsInterop::class)

package com.dontsaybojio.rollingnumbers

// Top-level JS interop functions (required for Kotlin/Wasm - js() must be single expression)
private fun getFormattedDecimal(): String = js("(1.1).toLocaleString()")
private fun getFormattedThousand(): String = js("(1000).toLocaleString()")
private fun getFormattedCurrency(): String = js("(1).toLocaleString(undefined, {style: 'currency', currency: 'USD'})")

internal actual fun getDecimalSeparator(): Char {
    return getFormattedDecimal().firstOrNull { !it.isDigit() } ?: '.'
}

internal actual fun getGroupingSeparator(): Char {
    return getFormattedThousand().firstOrNull { !it.isDigit() } ?: ','
}

internal actual fun isCurrencySymbolInFront(): Boolean {
    val formatted = getFormattedCurrency()
    val firstDigitIndex = formatted.indexOfFirst { it.isDigit() }
    return firstDigitIndex > 0
}