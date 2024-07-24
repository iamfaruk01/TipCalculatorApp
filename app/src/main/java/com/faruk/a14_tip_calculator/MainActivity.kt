package com.faruk.a14_tip_calculator

import android.annotation.SuppressLint
import android.health.connect.datatypes.units.Percentage
import android.icu.text.DecimalFormat
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.faruk.a14_tip_calculator.ui.theme._14_tip_calculatorTheme
import kotlin.math.sign

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            _14_tip_calculatorTheme {
                TipTimeLayout()
            }
        }
    }
}

@Composable
fun TipTimeLayout(modifier: Modifier = Modifier) {
    var billAmountInput by rememberSaveable {
        mutableStateOf("")
    }

    var tipPercentage by rememberSaveable {
        mutableStateOf("")
    }
    var roundUp by rememberSaveable {
        mutableStateOf(false)
    }

    val billAmountInputDouble = billAmountInput.toDoubleOrNull()?:0.0
    val tipPercentageDouble = tipPercentage.toDoubleOrNull()?:0.0
    val tipDouble = calculateTip(billAmountInputDouble,tipPercentageDouble, roundUp)
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.LightGray)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

    ) {
        Text(
            text = "Calculate tip",
            fontSize = 24.sp
        )
        Spacer(modifier = Modifier.height(24.dp))

        EditTextField(
            billAmountInput,
            onValueChange = { billAmountInput = it}
        )
        Spacer(modifier = Modifier.height(16.dp))

        EditTipPercentageField(
            tipPercentage,
            onValueChange = {tipPercentage = it}
            )

        Spacer(modifier = Modifier.height(24.dp))

        RoundUpTip(
            roundUp,
            onRoundUpChecked = {roundUp = it}
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Tip amount: Rs. $tipDouble",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        )
    }
}

@Composable
fun EditTextField(
    billAmountInput: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    TextField(
        value = billAmountInput,
        onValueChange = onValueChange,
        label = { Text(text = "Enter bill amount")},
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 32.dp, end = 32.dp)
    )

}

@Composable
fun EditTipPercentageField(
    tipPercentage: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
)  {
    TextField(
        value = tipPercentage,
        onValueChange = onValueChange,
        label = { Text(text = "Tip Percentage")},
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        singleLine = true,
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 32.dp, end = 32.dp)
    )
}

@Composable
fun RoundUpTip(
    roundUp: Boolean,
    onRoundUpChecked: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 32.dp, end = 32.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "Round up tip?",
            fontSize = 20.sp
        )
        Switch(
            checked = roundUp,
            onCheckedChange = onRoundUpChecked,
            modifier = modifier
                .fillMaxWidth()
                .wrapContentSize(Alignment.TopEnd)

        )
    }
}

private fun calculateTip(amount: Double, tipPercentage: Double, roundUp: Boolean) : Double {
    var tip = amount * (tipPercentage/100)
    if(roundUp){
        tip = kotlin.math.ceil(tip)
    }
    return "%.2f".format(tip).toDouble()
}