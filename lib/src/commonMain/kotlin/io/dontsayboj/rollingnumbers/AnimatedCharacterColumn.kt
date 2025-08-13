package io.dontsayboj.rollingnumbers

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import io.dontsayboj.rollingnumbers.levenshtein.LevenshteinAction
import io.dontsayboj.rollingnumbers.model.CharacterIndices
import io.dontsayboj.rollingnumbers.model.CharacterListData
import io.dontsayboj.rollingnumbers.model.ScrollingDirection
import io.dontsayboj.rollingnumbers.model.AnimationState

/**
 * Individual character column that handles the vertical scrolling animation
 * between characters based on the character lists.
 */
@Composable
internal fun AnimatedCharacterColumn(
    currentChar: Char,
    targetChar: Char,
    characterLists: List<String>,
    animationDuration: Int,
    textStyle: TextStyle,
    scrollingDirection: ScrollingDirection,
    textMeasurer: TextMeasurer,
    density: Density,
    animate: Boolean,
    action: Int
) {
    val animationProgress = remember { Animatable(0f) }
    var animationState by remember { mutableStateOf(AnimationState()) }

    // Determine actual animation characters based on the Levenshtein action
    val (animationCurrentChar, animationTargetChar) = remember(currentChar, targetChar, action) {
        when (action) {
            LevenshteinAction.INSERT -> {
                // Insert: animate from empty to target character
                Utils.EMPTY_CHAR to targetChar
            }
            LevenshteinAction.DELETE -> {
                // Delete: animate from current to empty character
                currentChar to Utils.EMPTY_CHAR
            }
            LevenshteinAction.SAME -> {
                // Same: animate from current to target character
                currentChar to targetChar
            }
            else -> {
                // Fallback to SAME behavior
                currentChar to targetChar
            }
        }
    }

    // Update animation state when characters change
    LaunchedEffect(animationCurrentChar, animationTargetChar, action) {
        if (animate && animationCurrentChar != animationTargetChar) {
            // Find the character list and indices for animation
            val characterListData = findCharacterListAndIndices(
                animationCurrentChar, animationTargetChar, characterLists, scrollingDirection,
            )

            animationState = AnimationState(
                characterList = characterListData.characterList,
                startIndex = characterListData.startIndex,
                endIndex = characterListData.endIndex,
                currentChar = animationCurrentChar,
                targetChar = animationTargetChar,
            )

            animationProgress.snapTo(0f)
            animationProgress.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = animationDuration),
            )
        } else {
            animationState = animationState.copy(
                currentChar = animationTargetChar,
                targetChar = animationTargetChar,
            )
            animationProgress.snapTo(1f)
        }
    }

    // Calculate character dimensions
    val charWidth = remember(targetChar, textStyle) {
        if (targetChar == Utils.EMPTY_CHAR) {
            0.dp
        } else {
            with(density) {
                textMeasurer.measure(
                    text = targetChar.toString(),
                    style = textStyle,
                ).size.width.toDp()
            }
        }
    }

    val charHeight = remember(textStyle) {
        with(density) {
            textMeasurer.measure(
                text = "A", // Use a standard character for height
                style = textStyle,
            ).size.height.toDp()
        }
    }

    if (charWidth > 0.dp) {
        Box(
            modifier = Modifier
                .width(charWidth)
                .height(charHeight),
            contentAlignment = Alignment.Center,
        ) {
            Canvas(
                modifier = Modifier.fillMaxSize(),
            ) {
                drawColumn(
                    animationState = animationState,
                    animationProgress = animationProgress.value,
                    textMeasurer = textMeasurer,
                    textStyle = textStyle,
                    charHeight = charHeight.toPx(),
                )
            }
        }
    }
}

/**
 * Finds the appropriate character list and start/end indices for animation
 */
private fun findCharacterListAndIndices(
    startChar: Char,
    endChar: Char,
    characterLists: List<String>,
    scrollingDirection: ScrollingDirection
): CharacterListData {
    // Try to find a character list that contains both characters
    for (characterListString in characterLists) {
        val characterList = createCharacterList(characterListString)
        val indices = getCharacterIndices(startChar, endChar, characterList, scrollingDirection)
        if (indices != null) {
            return CharacterListData(
                characterList = characterList,
                startIndex = indices.startIndex,
                endIndex = indices.endIndex,
            )
        }
    }

    // Fallback: create a simple list with just the two characters
    val fallbackList = if (startChar == endChar) {
        charArrayOf(startChar)
    } else {
        charArrayOf(startChar, endChar)
    }

    return CharacterListData(
        characterList = fallbackList,
        startIndex = 0,
        endIndex = if (startChar == endChar) 0 else 1,
    )
}

/**
 * Creates a character list in the format: EMPTY, list, list (for wrap-around animation)
 */
private fun createCharacterList(characterListString: String): CharArray {
    val chars = characterListString.toCharArray()
    val result = CharArray(chars.size * 2 + 1)
    result[0] = Utils.EMPTY_CHAR

    for (i in chars.indices) {
        result[1 + i] = chars[i]
        result[1 + chars.size + i] = chars[i]
    }

    return result
}

/**
 * Gets the start and end indices for character animation based on scrolling direction
 */
private fun getCharacterIndices(
    startChar: Char,
    endChar: Char,
    characterList: CharArray,
    scrollingDirection: ScrollingDirection
): CharacterIndices? {
    val startIndex = getIndexOfChar(startChar, characterList)
    val endIndex = getIndexOfChar(endChar, characterList)

    if (startIndex < 0 || endIndex < 0) return null

    val numOriginalCharacters = (characterList.size - 1) / 2

    return when (scrollingDirection) {
        ScrollingDirection.Down -> {
            var adjustedEndIndex = endIndex
            if (endChar == Utils.EMPTY_CHAR) {
                adjustedEndIndex = characterList.size
            } else if (endIndex < startIndex) {
                adjustedEndIndex += numOriginalCharacters
            }
            CharacterIndices(startIndex, adjustedEndIndex)
        }
        ScrollingDirection.Up -> {
            var adjustedStartIndex = startIndex
            if (startIndex < endIndex) {
                adjustedStartIndex += numOriginalCharacters
            }
            CharacterIndices(adjustedStartIndex, endIndex)
        }
        ScrollingDirection.Any -> {
            // Choose the shorter animation path
            if (startChar != Utils.EMPTY_CHAR && endChar != Utils.EMPTY_CHAR) {
                val adjustedIndices = if (endIndex < startIndex) {
                    val nonWrapDistance = startIndex - endIndex
                    val wrapDistance = numOriginalCharacters - startIndex + endIndex
                    if (wrapDistance < nonWrapDistance) {
                        CharacterIndices(startIndex, endIndex + numOriginalCharacters)
                    } else {
                        CharacterIndices(startIndex, endIndex)
                    }
                } else if (startIndex < endIndex) {
                    val nonWrapDistance = endIndex - startIndex
                    val wrapDistance = numOriginalCharacters - endIndex + startIndex
                    if (wrapDistance < nonWrapDistance) {
                        CharacterIndices(startIndex + numOriginalCharacters, endIndex)
                    } else {
                        CharacterIndices(startIndex, endIndex)
                    }
                } else {
                    CharacterIndices(startIndex, endIndex)
                }
                adjustedIndices
            } else {
                CharacterIndices(startIndex, endIndex)
            }
        }
    }
}

/**
 * Gets the index of a character in the character list
 */
private fun getIndexOfChar(char: Char, characterList: CharArray): Int {
    if (char == Utils.EMPTY_CHAR) return 0

    // Look in the first half of the list (excluding the empty char at index 0)
    val halfSize = (characterList.size - 1) / 2
    for (i in 1..halfSize) {
        if (characterList[i] == char) {
            return i
        }
    }
    return -1
}