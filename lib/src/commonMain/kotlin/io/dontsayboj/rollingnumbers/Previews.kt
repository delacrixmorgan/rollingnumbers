package io.dontsayboj.rollingnumbers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import io.dontsayboj.rollingnumbers.Utils.provideAlphanumericList
import io.dontsayboj.rollingnumbers.Utils.provideNumberList
import kotlinx.coroutines.delay
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
private fun Preview() {
    var text by remember { mutableStateOf("1234") }

    LaunchedEffect(Unit) {
        delay(1000)
        text = "5678"
        delay(2000)
        text = "9999"
        delay(2000)
        text = "0000"
    }

    RollingNumbers(
        text = text,
        characterLists = listOf(provideNumberList()),
        animationDuration = 500,
        useFullLevenshtein = false,
    )
}

@Preview
@Composable
private fun LevenshteinPreview() {
    var text by remember { mutableStateOf("ABC123") }

    LaunchedEffect(Unit) {
        delay(1000)
        text = "XYZ789"
        delay(2000)
        text = "DEF456"
        delay(2000)
        text = "GHI000"
    }

    RollingNumbers(
        text = text,
        characterLists = provideAlphanumericList(),
        animationDuration = 800,
        useFullLevenshtein = true,
    )
}