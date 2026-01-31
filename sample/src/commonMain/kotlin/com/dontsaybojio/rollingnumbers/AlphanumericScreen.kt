package com.dontsaybojio.rollingnumbers

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.dontsaybojio.rollingnumbers.ui.Utils
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun AlphanumericScreen(innerPadding: PaddingValues) {
    Box(
        Modifier
            .fillMaxSize()
            .consumeWindowInsets(innerPadding),
        contentAlignment = Alignment.Center
    ) {
        var text by remember { mutableStateOf(randomFlightNumber()) }
        LaunchedEffect(Unit) {
            while (true) {
                delay(3_000)
                text = randomFlightNumber()
            }
        }
        Column {
            RollingNumbers(
                text = text,
                textStyle = MaterialTheme.typography.displayLarge,
                characterLists = Utils.provideAlphanumericList(),
            )
        }
    }
}

private fun randomFlightNumber(): String {
    val letters = (1..2)
        .map { Random.nextInt(65, 91).toChar() }
        .joinToString("")
    val numbers = Random.nextInt(0, 10000).toString().padStart(4, '0')
    return "$letters$numbers"
}