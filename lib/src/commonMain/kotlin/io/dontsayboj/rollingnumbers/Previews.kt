package io.dontsayboj.rollingnumbers

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import io.dontsayboj.rollingnumbers.model.DefaultAnimationDuration
import io.dontsayboj.rollingnumbers.ui.Utils.provideAlphanumericList
import io.dontsayboj.rollingnumbers.ui.Utils.provideNumberString
import kotlinx.coroutines.delay
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
private fun NumberPreview() {
    var text by remember { mutableStateOf("1234") }

    LaunchedEffect(Unit) {
        while (true) {
            delay(2_000)
            text = "5678"
            delay(2_000)
            text = "9999"
            delay(2_000)
            text = "0000"
        }
    }

    RollingNumbers(
        text = text,
        characterLists = listOf(provideNumberString()),
        animationDuration = DefaultAnimationDuration.Slow.duration,
    )
}

@Preview
@Composable
private fun AlphanumericPreview() {
    var text by remember { mutableStateOf("ABC123") }
    LaunchedEffect(Unit) {
        while (true) {
            delay(2_000)
            text = "XYZ789"
            delay(2_000)
            text = "DEF456"
            delay(2_000)
            text = "GHI000"
        }
    }
    RollingNumbers(
        text = text,
        characterLists = provideAlphanumericList(),
        animationDuration = DefaultAnimationDuration.Slow.duration,
    )
}

@Preview
@Composable
private fun CurrencyPreview() {
    var amount by remember { mutableStateOf(0.0) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(2_000)
            amount = 100.0
            delay(2_000)
            amount = 200.0
            delay(2_000)
            amount = 300.0

            delay(2_000)
            amount = 200.0
            delay(2_000)
            amount = 100.0
            delay(2_000)
            amount = 100.0

            delay(2_000)
            amount = 0.0
            delay(2_000)
            amount = -100.0
            delay(2_000)
            amount = -200.0
        }
    }
    CurrencyRollingNumbers(
        amount = amount,
        textStyle = MaterialTheme.typography.displayLarge,
        characterLists = listOf(provideNumberString()),
        animationDuration = DefaultAnimationDuration.Slow.duration,
    )
}