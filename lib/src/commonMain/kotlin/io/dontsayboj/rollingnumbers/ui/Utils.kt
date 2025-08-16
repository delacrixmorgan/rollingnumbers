package io.dontsayboj.rollingnumbers.ui

/**
 * Utility constants and methods
 */
object Utils {
    const val EMPTY_CHAR = '\u0000'

    fun provideNumberString(): String = "0123456789"
    fun provideAlphabeticalString(): String = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
    fun provideAlphanumericList() = listOf(provideNumberString(), provideAlphabeticalString())
}