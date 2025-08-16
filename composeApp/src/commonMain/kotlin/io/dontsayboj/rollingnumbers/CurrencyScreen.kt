package io.dontsayboj.rollingnumbers

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.dontsayboj.rollingnumbers.model.DefaultAnimationDuration
import io.dontsayboj.rollingnumbers.ui.Utils
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun CurrencyScreen(innerPadding: PaddingValues) {
    Box(
        Modifier
            .fillMaxSize()
            .consumeWindowInsets(innerPadding),
    ) {
        val operations = Operation.entries
        var amount by remember { mutableStateOf(0.0) }
        var isCurrencySymbolInFrontChecked by remember { mutableStateOf(true) }
        var alternativeSeparatorChecked by remember { mutableStateOf(false) }
        var selectedOperationIndex by remember { mutableIntStateOf(0) }
        val selectedOperation by derivedStateOf {
            operations[selectedOperationIndex]
        }

        LaunchedEffect(Unit) {
            while (true) {
                delay(3_000)
                when (selectedOperation) {
                    Operation.Add -> amount += Random.nextDouble(10.0, 100.0).roundUp(2)
                    Operation.Minus -> amount -= Random.nextDouble(10.0, 100.0).roundUp(2)
                }
            }
        }

        CurrencyRollingNumbers(
            modifier = Modifier.align(Alignment.Center),
            amount = amount,
            textStyle = MaterialTheme.typography.displayLarge,
            characterLists = listOf(Utils.provideNumberString()),
            animationDuration = DefaultAnimationDuration.Slow.duration,
            isCurrencySymbolInFront = isCurrencySymbolInFrontChecked,
            decimalSeparator = if (!alternativeSeparatorChecked) '.' else ',',
            groupingSeparator = if (!alternativeSeparatorChecked) ',' else '.',
        )

        Column(
            Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = innerPadding.calculateBottomPadding() + 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("isCurrencySymbolInFront")
                Checkbox(
                    checked = isCurrencySymbolInFrontChecked,
                    onCheckedChange = { isCurrencySymbolInFrontChecked = it },
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("alternativeSeparators")
                Checkbox(
                    checked = alternativeSeparatorChecked,
                    onCheckedChange = { alternativeSeparatorChecked = it },
                )
            }
            Spacer(Modifier.height(16.dp))
            SingleChoiceSegmentedButtonRow {
                operations.forEachIndexed { index, label ->
                    SegmentedButton(
                        shape = SegmentedButtonDefaults.itemShape(
                            index = index,
                            count = operations.size
                        ),
                        onClick = { selectedOperationIndex = index },
                        selected = index == selectedOperationIndex,
                        label = {
                            BasicText(
                                text = label.name,
                                maxLines = 1,
                                autoSize = TextAutoSize.StepBased(),
                            )
                        }
                    )
                }
            }
        }
    }
}

enum class Operation {
    Add,
    Minus
}