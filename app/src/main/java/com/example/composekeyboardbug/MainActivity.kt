package com.example.composekeyboardbug

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composekeyboardbug.ui.theme.ComposeKeyboardBugTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeKeyboardBugTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                    ) {
                        NumberTextField()
                    }
                }
            }
        }
    }
}

@Composable
private fun NumberTextField() {
    var input by remember { mutableStateOf("") }

    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = input,
        onValueChange = { input = it.sanitizeInput() },
        label = { Text("Enter 9 digits") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true,
        visualTransformation = NumberVisualTransformation()
    )
}

internal data class NumberVisualTransformation(
    private val separator: Char = '—'
) : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = text.text.filter { it.isDigit() }.take(NumberTextFieldMaxLength)
        var out = ""
        for (i in trimmed.indices) {
            out += trimmed[i]
            if (i != trimmed.lastIndex) {
                out += separator
            }
        }

        return TransformedText(
            text = AnnotatedString(out),
            offsetMapping = object : OffsetMapping {
                override fun originalToTransformed(offset: Int): Int {
                    if (offset == 0) return 0
                    return offset * 2 - 1
                }

                override fun transformedToOriginal(offset: Int): Int {
                    return (offset + 1) / 2
                }
            }
        )
    }
}

private const val NumberTextFieldMaxLength = 9
private fun String.sanitizeInput() = filter { it.isDigit() }.take(NumberTextFieldMaxLength)

@Preview(showBackground = true)
@Composable
private fun GreetingPreview() {
    ComposeKeyboardBugTheme {
        NumberTextField()
    }
}