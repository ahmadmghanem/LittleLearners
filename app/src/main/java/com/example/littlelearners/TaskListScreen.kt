package com.example.littlelearners

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import java.util.Locale

@Composable
fun TaskListScreen() {
    val context = LocalContext.current
    var taskText by remember { mutableStateOf("") }
    val tasks = remember { mutableStateListOf<String>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.title),
                style = MaterialTheme.typography.headlineMedium
            )
            Button(onClick = { toggleLanguage(context) }) {
                Text(text = stringResource(id = R.string.lang_button))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = taskText,
                onValueChange = { taskText = it },
                placeholder = { Text(stringResource(id = R.string.add_hint)) },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    if (taskText.isNotBlank()) {
                        tasks.add(taskText)
                        taskText = ""
                    }
                },
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Text(stringResource(id = R.string.add))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(tasks) { task ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Text(
                        text = task,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}

fun toggleLanguage(context: Context) {
    val prefs = context.getSharedPreferences("Settings", Context.MODE_PRIVATE)
    val currentLang = prefs.getString("My_Lang", "en") ?: "en"
    val newLang = if (currentLang == "ar") "en" else "ar"

    prefs.edit().putString("My_Lang", newLang).apply()

    val locale = Locale(newLang)
    Locale.setDefault(locale)
    val config = Configuration(context.resources.configuration)
    config.setLocale(locale)
    context.resources.updateConfiguration(config, context.resources.displayMetrics)

    (context as? Activity)?.recreate()
}
