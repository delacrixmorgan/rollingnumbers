package com.dontsaybojio.rollingnumbers

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import com.dontsaybojio.rollingnumbers.levenshtein.LevenshteinAction
import com.dontsaybojio.rollingnumbers.levenshtein.LevenshteinUtils
import com.dontsaybojio.rollingnumbers.model.DefaultAnimationDuration
import com.dontsaybojio.rollingnumbers.model.ScrollingDirection
import com.dontsaybojio.rollingnumbers.ui.AnimatedCharacterColumn
import com.dontsaybojio.rollingnumbers.ui.Utils
import kotlinx.coroutines.delay
import kotlin.math.max

/**
 * @param text The target text to display
 * @param modifier Modifier to be applied to the RollingNumbers
 * @param characterLists List of character sequences that define animation paths
 * @param animationDuration Duration of the animation in milliseconds
 * @param textStyle Style to apply to the text
 * @param scrollingDirection Preferred direction for scrolling animations
 * @param animateChanges Whether to animate text changes
 */
@Composable
fun RollingNumbers(
    text: String,
    modifier: Modifier = Modifier,
    characterLists: List<String> = listOf(Utils.provideNumberString()),
    animationDuration: Int = DefaultAnimationDuration.Default.duration,
    textStyle: TextStyle = LocalTextStyle.current,
    scrollingDirection: ScrollingDirection = ScrollingDirection.Any,
    animateChanges: Boolean = true,
) {
    var currentText by remember { mutableStateOf(text) }
    var isAnimating by remember { mutableStateOf(false) }

    val textMeasurer = rememberTextMeasurer()
    val density = LocalDensity.current

    // Handle text changes
    LaunchedEffect(text) {
        if (text != currentText && animateChanges && !isAnimating) {
            isAnimating = true
            // Animation will be handled by individual columns
            delay(animationDuration.toLong())
            currentText = text
            isAnimating = false
        } else if (!animateChanges || currentText.isEmpty()) {
            currentText = text
        }
    }

    if (currentText.isEmpty()) return

    // Calculate column actions using Levenshtein algorithm
    val columnActions = remember(currentText, text) {
        if (currentText == text) {
            intArrayOf()
        } else {
            val supportedCharacters = characterLists.flatMap { it.toSet() }.toSet()
            LevenshteinUtils.computeColumnActions(
                currentText.toCharArray(),
                text.toCharArray(),
                supportedCharacters,
            )
        }
    }

    Row(modifier = modifier.clipToBounds()) {
        val maxLength = max(currentText.length, text.length)

        for (i in 0 until maxLength) {
            val currentChar = currentText.getOrNull(i) ?: Utils.EMPTY_CHAR
            val targetChar = text.getOrNull(i) ?: Utils.EMPTY_CHAR
            val action = columnActions.getOrNull(i) ?: LevenshteinAction.SAME

            AnimatedCharacterColumn(
                currentChar = currentChar,
                targetChar = targetChar,
                characterLists = characterLists,
                animationDuration = animationDuration,
                textStyle = textStyle,
                scrollingDirection = scrollingDirection,
                textMeasurer = textMeasurer,
                density = density,
                animate = animateChanges && (currentChar != targetChar),
                action = action,
            )
        }
    }
}
