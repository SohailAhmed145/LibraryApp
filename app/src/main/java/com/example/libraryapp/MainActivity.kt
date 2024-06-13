package com.example.libraryapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Update
import com.example.libraryapp.repository.Repository
import com.example.libraryapp.room.BookEntity
import com.example.libraryapp.room.BooksDB
import com.example.libraryapp.screens.UpdateScreen
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

                            val navController = rememberNavController()

                            NavHost(navController = navController, startDestination = "MainScreen" ){
                                composable("MainScreen"){
                                    MainScreen(viewModel = myViewModel, navController)
                                }
                                composable("UpdateScreen/{bookId}"){
                                    UpdateScreen(viewModel = myViewModel, bookId = it.arguments?.getString("bookId"), navController)
                                }
                            }

                        }

                    }
                }
            }
        }
    }
}


@Composable
fun MainScreen(viewModel: BookViewModel, navController: NavHostController) {

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

        BookList(viewModel = viewModel, navController)
    }
}

@Composable
fun BookCard(viewModel: BookViewModel, book: BookEntity,navController: NavHostController){
    Card(
        modifier = Modifier
            .padding(horizontal = 18.dp, vertical = 8.dp)
            .fillMaxWidth()
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = book.id.toString(),
                fontSize = 20.sp,
                modifier = Modifier.padding(start = 12.dp, end = 12.dp)
            )
            Text(
                text = book.title,
                modifier = Modifier.fillMaxSize(0.7f),
                fontSize = 20.sp,
            )
            Row (
                horizontalArrangement = Arrangement.End
            ){
                IconButton(onClick = { viewModel.deleteBook(book) }){
                    Icon(
                        imageVector = Icons.Default.Delete ,
                        contentDescription = "Delete"
                    )
                }
                IconButton(onClick = {
                    navController.navigate("UpdateScreen/${book.id}")
                }){
                    Icon(
                        imageVector = Icons.Default.Edit ,
                        contentDescription = "Edit"
                    )
                }
            }

        }

    }
}


@Composable
fun BookList(viewModel: BookViewModel, navController: NavHostController) {
    val books by viewModel.books.collectAsState(initial = emptyList())
    LazyColumn {
        items(items = books) {
            item -> BookCard(
            viewModel = viewModel,
            book = item,
            navController
            )
        }
    }
}
