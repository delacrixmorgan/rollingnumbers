package com.dontsaybojio.rollingnumbers

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.dontsaybojio.rollingnumbers.model.DefaultAnimationDuration
import com.dontsaybojio.rollingnumbers.model.ScrollingDirection
import com.dontsaybojio.rollingnumbers.ui.Utils
import com.dontsaybojio.rollingnumbers.ui.format
import kotlin.math.abs

/**
 * @param amount The target amount to display
 * @param modifier Modifier to be applied to the RollingNumbers
 * @param characterLists List of character sequences that define animation paths
 * @param animationDuration Duration of the animation in milliseconds
 * @param textStyle Style to apply to the text
 * @param positiveSignedSymbolColor Positive signed symbol colour
 * @param negativeSignedSymbolColor Negative signed symbol colour
 * @param animateChanges Whether to animate text changes
 * @param decimals Decimal points when formatting amount
 * @param currencySymbol Currency symbol to display
 * @param spacingInBetweenCurrencySymbol Spacing in dp between currency symbol and RollingNumbers
 * @param spacingInBetweenSignedSymbol Spacing in dp between signed symbol and RollingNumbers
 * @param showPositiveSignedSymbol Show signed symbol for positive amount
 * @param isCurrencySymbolInFront Determine the position of the currencySymbol
 * @param decimalSeparator Separator for decimal points
 * @param groupingSeparator Separator for grouping points for thousands.
 */
@Composable
fun CurrencyRollingNumbers(
    amount: Double,
    modifier: Modifier = Modifier,
    characterLists: List<String> = listOf(Utils.provideNumberString()),
    animationDuration: Int = DefaultAnimationDuration.Default.duration,
    textStyle: TextStyle = LocalTextStyle.current,
    positiveSignedSymbolColor: Color = textStyle.color,
    negativeSignedSymbolColor: Color = textStyle.color,
    animateChanges: Boolean = true,
    decimals: Int = 2,
    currencySymbol: String = "$",
    spacingInBetweenCurrencySymbol: Dp = 8.dp,
    spacingInBetweenSignedSymbol: Dp = 8.dp,
    showPositiveSignedSymbol: Boolean = false,
    isCurrencySymbolInFront: Boolean = isCurrencySymbolInFront(),
    decimalSeparator: Char = getDecimalSeparator(),
    groupingSeparator: Char = getGroupingSeparator(),
) {
    // Remember the previous amount to determine scrolling direction
    var previousAmount by remember { mutableStateOf(amount) }

    // Determine the scrolling direction based on absolute values (magnitude)
    // This ensures proper direction for negative numbers
    val scrollingDirection = when {
        abs(previousAmount) < abs(amount) -> ScrollingDirection.Up
        abs(previousAmount) > abs(amount) -> ScrollingDirection.Down
        else -> ScrollingDirection.Any
    }

    // Update previous amount after determining direction
    LaunchedEffect(amount) {
        previousAmount = amount
    }

    Row(modifier = modifier) {
        if (amount < 0) {
            Text(text = "−", style = textStyle, color = negativeSignedSymbolColor)
            Spacer(Modifier.width(spacingInBetweenSignedSymbol))
        } else {
            if (showPositiveSignedSymbol) {
                Text(text = "+", style = textStyle, color = positiveSignedSymbolColor)
                Spacer(Modifier.width(spacingInBetweenSignedSymbol))
            }
        }
        if (isCurrencySymbolInFront) {
            Text(text = currencySymbol, style = textStyle)
            Spacer(Modifier.width(spacingInBetweenCurrencySymbol))
        }
        RollingNumbers(
            text = amount.format(
                decimals = decimals,
                decimalSeparator = decimalSeparator,
                groupingSeparator = groupingSeparator,
                signed = false
            ),
            characterLists = characterLists,
            animationDuration = animationDuration,
            textStyle = textStyle,
            scrollingDirection = scrollingDirection,
            animateChanges = animateChanges,
        )
        if (!isCurrencySymbolInFront) {
            Spacer(Modifier.width(spacingInBetweenCurrencySymbol))
            Text(text = currencySymbol, style = textStyle)
        }
    }
}
