package com.example.libraryapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import com.example.libraryapp.repository.Repository
import com.example.libraryapp.room.BookEntity
import com.example.libraryapp.room.BooksDB
import com.example.libraryapp.ui.theme.LibraryAppTheme
import com.example.libraryapp.viewmodel.BookViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LibraryAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        Column (
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ){
                            val mContext = LocalContext.current
                            val db = BooksDB.getInstance(mContext)
                            val repository = Repository(db)
                            val myViewModel = BookViewModel(repository = repository)
                            MainScreen(myViewModel)
                        }

                    }
                }
            }
        }
    }
}


@Composable
fun MainScreen(viewModel: BookViewModel) {

    var inputBook by remember { mutableStateOf("") }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        OutlinedTextField(
            value = inputBook,
            onValueChange = {
                enteredText -> inputBook = enteredText
            },
            label = {Text(text = "Book name")},
            placeholder = {Text(text = "Enter Your book name")}
        )

        Button(onClick = {
            viewModel.addBook(BookEntity(0, inputBook))
        }) {
            Text(text = "Insert book in db")
        }
    }
}
