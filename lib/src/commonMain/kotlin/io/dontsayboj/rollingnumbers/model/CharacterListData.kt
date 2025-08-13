package io.dontsayboj.rollingnumbers.model

/**
 * Character list data with indices
 */
internal data class CharacterListData(
    val characterList: CharArray,
    val startIndex: Int,
    val endIndex: Int
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        other as CharacterListData

        if (!characterList.contentEquals(other.characterList)) return false
        if (startIndex != other.startIndex) return false
        if (endIndex != other.endIndex) return false

        return true
    }

    override fun hashCode(): Int {
        var result = characterList.contentHashCode()
        result = 31 * result + startIndex
        result = 31 * result + endIndex
        return result
    }
}