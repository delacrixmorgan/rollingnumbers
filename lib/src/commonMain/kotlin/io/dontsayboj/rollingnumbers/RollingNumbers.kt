package io.dontsayboj.rollingnumbers

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
import io.dontsayboj.rollingnumbers.levenshtein.LevenshteinAction
import io.dontsayboj.rollingnumbers.levenshtein.LevenshteinUtils
import io.dontsayboj.rollingnumbers.model.ScrollingDirection
import kotlinx.coroutines.delay
import kotlin.math.max

/**
 * Based on Robinhood TickerView that provides smooth scrolling
 * text animations between different strings.
 *
 * This composable handles animating from one text to another by scrolling individual
 * characters vertically based on predefined character lists that dictate the animation sequence.
 *
 * @param text The target text to display
 * @param modifier Modifier to be applied to the RollingNumbers
 * @param characterLists List of character sequences that define animation paths
 * @param animationDuration Duration of the animation in milliseconds
 * @param textStyle Style to apply to the text
 * @param scrollingDirection Preferred direction for scrolling animations
 * @param animateChanges Whether to animate text changes
 * @param useFullLevenshtein Whether to use the full Levenshtein algorithm (true) or simplified version (false)
 */
@Composable
fun RollingNumbers(
    text: String,
    modifier: Modifier = Modifier,
    characterLists: List<String> = listOf(Utils.provideNumberList()),
    animationDuration: Int = 350,
    textStyle: TextStyle = LocalTextStyle.current,
    scrollingDirection: ScrollingDirection = ScrollingDirection.Any,
    animateChanges: Boolean = true,
    useFullLevenshtein: Boolean = false
) {
    var currentText by remember { mutableStateOf("") }
    var isAnimating by remember { mutableStateOf(false) }

    val textMeasurer = rememberTextMeasurer()
    val density = LocalDensity.current

    // Initialize current text if empty
    LaunchedEffect(Unit) {
        if (currentText.isEmpty() && text.isNotEmpty()) {
            currentText = text
        }
    }

    // Handle text changes
    LaunchedEffect(text) {
        if (text != currentText && animateChanges && !isAnimating) {
            isAnimating = true
            // Animation will be handled by individual columns
            delay(animationDuration.toLong())
            currentText = text
            isAnimating = false
        } else if (!animateChanges) {
            currentText = text
        }
    }

    if (currentText.isEmpty()) return

    // Calculate column actions using Levenshtein algorithm
    val columnActions = remember(currentText, text, useFullLevenshtein) {
        if (currentText == text) {
            intArrayOf()
        } else {
            val supportedCharacters = characterLists.flatMap { it.toSet() }.toSet()
            if (useFullLevenshtein) {
                LevenshteinUtils.computeColumnActions(
                    currentText.toCharArray(),
                    text.toCharArray(),
                    supportedCharacters,
                )
            } else {
                computeColumnActionsSimple(
                    currentText.toCharArray(),
                    text.toCharArray(),
                    supportedCharacters,
                )
            }
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

/**
 * Computes the column actions using a simplified Levenshtein algorithm
 */
private fun computeColumnActionsSimple(
    source: CharArray,
    target: CharArray,
    supportedCharacters: Set<Char>
): IntArray {
    val actions = mutableListOf<Int>()
    var sourceIndex = 0
    var targetIndex = 0

    while (sourceIndex < source.size || targetIndex < target.size) {
        when {
            sourceIndex >= source.size -> {
                // Insert remaining target characters
                repeat(target.size - targetIndex) {
                    actions.add(LevenshteinAction.INSERT)
                }
                break
            }
            targetIndex >= target.size -> {
                // Delete remaining source characters
                repeat(source.size - sourceIndex) {
                    actions.add(LevenshteinAction.DELETE)
                }
                break
            }
            else -> {
                val sourceChar = source[sourceIndex]
                val targetChar = target[targetIndex]

                when {
                    sourceChar == targetChar -> {
                        actions.add(LevenshteinAction.SAME)
                        sourceIndex++
                        targetIndex++
                    }
                    supportedCharacters.contains(sourceChar) &&
                            supportedCharacters.contains(targetChar) -> {
                        actions.add(LevenshteinAction.SAME)
                        sourceIndex++
                        targetIndex++
                    }
                    supportedCharacters.contains(targetChar) -> {
                        actions.add(LevenshteinAction.INSERT)
                        targetIndex++
                    }
                    supportedCharacters.contains(sourceChar) -> {
                        actions.add(LevenshteinAction.DELETE)
                        sourceIndex++
                    }
                    else -> {
                        actions.add(LevenshteinAction.SAME)
                        sourceIndex++
                        targetIndex++
                    }
                }
            }
        }
    }

    return actions.toIntArray()
}