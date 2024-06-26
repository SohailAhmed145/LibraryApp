package com.example.libraryapp.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import com.example.libraryapp.room.BookEntity
import com.example.libraryapp.viewmodel.BookViewModel

@Composable
fun UpdateScreen(viewModel: BookViewModel, bookId: String?, navController: NavController){

    var inputBook by remember { mutableStateOf("") }

    Column {

        OutlinedTextField(
            value = inputBook,
            onValueChange = {enteredText -> inputBook = enteredText},
            label = { Text("Update Book Name")  },
            placeholder = {
                Text("New Book Name")
            }
        )
        
        Button(onClick = {
            val newBook = BookEntity(bookId!!.toInt(), inputBook)
            viewModel.updateBook(newBook)
            navController.popBackStack()
        }) {
            Text(text = "Update Book")
        }

    }
}