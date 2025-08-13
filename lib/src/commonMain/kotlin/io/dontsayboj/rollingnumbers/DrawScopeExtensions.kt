package io.dontsayboj.rollingnumbers

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import io.dontsayboj.rollingnumbers.model.AnimationState
import kotlin.math.abs

/**
 * Draws the column with animated character transitions
 */
internal fun DrawScope.drawColumn(
    animationState: AnimationState,
    animationProgress: Float,
    textMeasurer: TextMeasurer,
    textStyle: TextStyle,
    charHeight: Float
) {
    val characterList = animationState.characterList
    val startIndex = animationState.startIndex
    val endIndex = animationState.endIndex

    if (characterList.isEmpty()) return

    // Calculate animation parameters
    val totalHeight = charHeight * abs(endIndex - startIndex)
    val currentBase = animationProgress * totalHeight
    val bottomCharPosition = currentBase / charHeight
    val bottomCharIndex = startIndex + (bottomCharPosition.toInt() *
            if (endIndex >= startIndex) 1 else -1)
    val bottomDelta = (bottomCharPosition - bottomCharPosition.toInt()) * charHeight *
            if (endIndex >= startIndex) 1 else -1

    // Draw the main character
    drawCharacterAtOffset(
        character = characterList.getOrNull(bottomCharIndex),
        offset = Offset(0f, bottomDelta),
        textMeasurer = textMeasurer,
        textStyle = textStyle,
    )

    // Draw adjacent characters for smooth transitions
    drawCharacterAtOffset(
        character = characterList.getOrNull(bottomCharIndex + 1),
        offset = Offset(0f, bottomDelta - charHeight),
        textMeasurer = textMeasurer,
        textStyle = textStyle,
    )

    drawCharacterAtOffset(
        character = characterList.getOrNull(bottomCharIndex - 1),
        offset = Offset(0f, bottomDelta + charHeight),
        textMeasurer = textMeasurer,
        textStyle = textStyle,
    )
}

/**
 * Draws a single character at the specified offset
 */
internal fun DrawScope.drawCharacterAtOffset(
    character: Char?,
    offset: Offset,
    textMeasurer: TextMeasurer,
    textStyle: TextStyle
) {
    if (character != null && character != Utils.EMPTY_CHAR) {
        translate(offset.x, offset.y) {
            drawText(
                textMeasurer = textMeasurer,
                text = character.toString(),
                style = textStyle,
                topLeft = Offset.Zero,
            )
        }
    }
}