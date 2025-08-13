package io.dontsayboj.rollingnumbers

import androidx.compose.foundation.layout.Box
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
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun DecimalScreen(innerPadding: PaddingValues) {
    Box(
        Modifier
            .fillMaxSize()
            .consumeWindowInsets(innerPadding),
        contentAlignment = Alignment.Center
    ) {
        var amount by remember { mutableStateOf(0) }
        LaunchedEffect(Unit) {
            while (true) {
                delay(3_000)
                amount += Random.nextInt(10, 100)
            }
        }

        RollingNumbers(
            text = amount.toString(),
            textStyle = MaterialTheme.typography.displayLarge,
            characterLists = listOf(Utils.provideNumberList()),
            animationDuration = 800,
        )
    }
}