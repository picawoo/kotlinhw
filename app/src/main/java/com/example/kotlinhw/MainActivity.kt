package com.example.kotlinhw

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.kotlinhw.ui.theme.PictureGalleryTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PictureGalleryTheme {
                GalleryScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GalleryScreen() {
    var gallery by remember { mutableStateOf(generateInitialPictures()) }
    var searchText by remember { mutableStateOf(TextFieldValue("")) }
    var isGridLayout by remember { mutableStateOf(false) }

    var newAuthor by remember { mutableStateOf("") }
    var newUrl by remember { mutableStateOf("") }
    var showForm by remember { mutableStateOf(false) }

    val filteredGallery = gallery.filter {
        it.author.contains(searchText.text, ignoreCase = true)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Галерея изображений") },
                actions = {
                    IconButton(onClick = { gallery = mutableListOf() }) {
                        Icon(Icons.Default.Delete, contentDescription = "Очистить всё")
                    }
                    IconButton(onClick = { isGridLayout = !isGridLayout }) {
                        Icon(
                            if (isGridLayout) Icons.Default.List else Icons.Filled.GridView,
                            contentDescription = "Переключить вид"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showForm = !showForm }) {
                Icon(Icons.Default.Add, contentDescription = "Добавить")
            }
        }
    ) { innerPadding ->
        Column(Modifier.padding(innerPadding)) {
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                label = { Text("Поиск по автору") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )

            if (showForm) {
                Card(
                    Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        OutlinedTextField(
                            value = newAuthor,
                            onValueChange = { newAuthor = it },
                            label = { Text("Автор") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(Modifier.height(8.dp))
                        OutlinedTextField(
                            value = newUrl,
                            onValueChange = { newUrl = it },
                            label = { Text("Ссылка на изображение") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(Modifier.height(8.dp))
                        Button(
                            onClick = {
                                if (newAuthor.isNotBlank() && newUrl.isNotBlank()) {
                                    if (gallery.none { it.url == newUrl }) {
                                        val newPic = Picture(
                                            id = (gallery.maxOfOrNull { it.id } ?: 0) + 1,
                                            author = newAuthor,
                                            url = newUrl
                                        )
                                        gallery = (gallery + newPic).toMutableList()
                                        newAuthor = ""
                                        newUrl = ""
                                        showForm = false
                                    }
                                }
                            },
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text("Добавить")
                        }
                    }
                }
            }

            if (isGridLayout) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    items(filteredGallery.size) { index ->
                        PictureItem(
                            picture = filteredGallery[index],
                            onClick = {
                                gallery = gallery.filter { it.id != filteredGallery[index].id }.toMutableList()
                            }
                        )
                    }
                }
            } else {
                LazyColumn(contentPadding = PaddingValues(8.dp)) {
                    items(filteredGallery) { picture ->
                        PictureItem(
                            picture = picture,
                            onClick = {
                                gallery = gallery.filter { it.id != picture.id }.toMutableList()
                            }
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun PictureItem(picture: Picture, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = picture.url,
                    placeholder = painterResource(R.drawable.placeholder)
                ),
                contentDescription = picture.author,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(180.dp)
                    .fillMaxWidth()
            )
            Text(
                text = picture.author,
                modifier = Modifier.padding(8.dp),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}