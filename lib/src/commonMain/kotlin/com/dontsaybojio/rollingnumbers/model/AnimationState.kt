package com.dontsaybojio.rollingnumbers.model

import com.dontsaybojio.rollingnumbers.ui.Utils

/**
 * Animation state for a column
 */
internal data class AnimationState(
    val characterList: CharArray = charArrayOf(),
    val startIndex: Int = 0,
    val endIndex: Int = 0,
    val currentChar: Char = Utils.EMPTY_CHAR,
    val targetChar: Char = Utils.EMPTY_CHAR
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true

        other as AnimationState

        if (!characterList.contentEquals(other.characterList)) return false
        if (startIndex != other.startIndex) return false
        if (endIndex != other.endIndex) return false
        if (currentChar != other.currentChar) return false
        if (targetChar != other.targetChar) return false

        return true
    }

    override fun hashCode(): Int {
        var result = characterList.contentHashCode()
        result = 31 * result + startIndex
        result = 31 * result + endIndex
        result = 31 * result + currentChar.hashCode()
        result = 31 * result + targetChar.hashCode()
        return result
    }
}