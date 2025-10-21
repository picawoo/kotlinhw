package com.example.kotlinhw

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.kotlinhw.ui.theme.KotlinhwTheme

@Composable
fun UserSurveyScreen() {
    var name by rememberSaveable { mutableStateOf("") }
    var ageFloat by rememberSaveable { mutableStateOf(25f) }
    val age = ageFloat.toInt()
    var gender by rememberSaveable { mutableStateOf("male") }
    var subscribed by rememberSaveable { mutableStateOf(false) }
    var submitted by rememberSaveable { mutableStateOf(false) }

    val nameValid = name.isNotBlank()
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val avatarModifier = Modifier.size(96.dp).clip(CircleShape)
        val avatarPainter = runCatching {
            painterResource(id = R.drawable.avatar_default)
        }.getOrElse {
            null
        }

        if (avatarPainter != null) {
            Image(
                painter = avatarPainter,
                contentDescription = stringResource(R.string.avatar_description),
                modifier = avatarModifier,
                contentScale = ContentScale.Crop
            )
        } else {
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = stringResource(R.string.avatar_description),
                modifier = avatarModifier
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text(stringResource(R.string.label_name)) },
            modifier = Modifier.fillMaxWidth(),
            isError = !nameValid,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )
        if (!nameValid) {
            Text(
                text = stringResource(R.string.error_name_required),
                color = MaterialTheme.colors.error,
                style = MaterialTheme.typography.caption,
                modifier = Modifier.align(Alignment.Start)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(text = stringResource(R.string.label_age) + ": $age")
        Slider(
            value = ageFloat,
            onValueChange = { ageFloat = it },
            valueRange = 1f..100f,
            steps = 99,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(text = stringResource(R.string.label_gender))
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            val options = listOf("male", "female")
            options.forEach { opt ->
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .selectable(selected = gender == opt, onClick = { gender = opt }),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(selected = gender == opt, onClick = { gender = opt })
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (opt == "male")
                            stringResource(R.string.gender_male)
                        else stringResource(R.string.gender_female)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            Checkbox(checked = subscribed, onCheckedChange = { subscribed = it })
            Spacer(modifier = Modifier.width(8.dp))
            Text(stringResource(R.string.subscribe_text))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { submitted = true },
            enabled = nameValid,
            modifier = Modifier.fillMaxWidth().height(48.dp)
        ) {
            Text(stringResource(R.string.button_submit))
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (submitted) {
            Card(modifier = Modifier.fillMaxWidth(), elevation = 4.dp) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(stringResource(R.string.summary_title), style = MaterialTheme.typography.subtitle1)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(stringResource(R.string.summary_name, name))
                    Text(stringResource(R.string.summary_age, age))
                    Text(
                        stringResource(
                            R.string.summary_gender,
                            if (gender == "male") stringResource(R.string.gender_male)
                            else stringResource(R.string.gender_female)
                        )
                    )
                    Text(
                        stringResource(
                            R.string.summary_subscribed,
                            if (subscribed) stringResource(R.string.yes)
                            else stringResource(R.string.no)
                        )
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun PreviewLight() = KotlinhwTheme { UserSurveyScreen() }

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewDark() = KotlinhwTheme { UserSurveyScreen() }
