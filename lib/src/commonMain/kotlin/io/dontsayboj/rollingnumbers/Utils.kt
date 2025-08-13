package io.dontsayboj.rollingnumbers

/**
 * Utility constants and methods
 */
object Utils {
    const val EMPTY_CHAR = '\u0000'

    fun provideNumberList(): String = "0123456789"
    fun provideAlphabeticalList(): String = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
    fun provideAlphanumericList() = listOf(provideNumberList(), provideAlphabeticalList())
}