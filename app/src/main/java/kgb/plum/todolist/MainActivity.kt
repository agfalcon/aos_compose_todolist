package kgb.plum.todolist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kgb.plum.todolist.ui.theme.TodoListTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TodoListTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    TodoList()
                }
            }
        }
    }
}

@Composable
fun TodoList(){
    val (text, setText) = remember{mutableStateOf("")}
    val todoList = remember{mutableStateListOf<TodoData>()}

    val onSubmit: (String) -> Unit = { text ->
        submit(text, todoList)
        setText("")
    }

    Scaffold{
        Column(modifier = Modifier.padding(4.dp)){
            InputData(
                text = text,
                onTextChanged = setText,
                onSubmit = onSubmit
            )
            LazyColumn {
                items(todoList, key = {it.key}) { todoData ->
                    Text(todoData.content)
                }
            }
        }
    }
}

fun submit(text: String, todoList: SnapshotStateList<TodoData>){
    val key = (todoList.lastOrNull()?.key ?: 0) +1
    todoList.add(TodoData(key, text))
}

@Composable
fun InputData(
    text: String,
    onTextChanged: (String) -> Unit = { _ -> },
    onSubmit: (String) -> Unit = { _ -> }
    ){
    Row(verticalAlignment = Alignment.CenterVertically) {
        OutlinedTextField(
            value = text, 
            onValueChange = onTextChanged,
            modifier = Modifier.weight(1f),
            colors = TextFieldDefaults.outlinedTextFieldColors(focusedBorderColor = Color.DarkGray)
        )
        Spacer(modifier = Modifier.size(8.dp))
        Button(
            onClick = {
                onSubmit(text)
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.DarkGray),
            modifier = Modifier.background(color = Color.DarkGray, shape = RoundedCornerShape(6.dp))
        ) {
            Text(
                text = "입력",
                color = Color.White,
                )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TodoListTheme {
        TodoList()
    }
}

@Preview(showBackground = true)
@Composable
fun InputDataPreview(){
    TodoListTheme {
        InputData(text = "TodoList 구현하기")
    }
}

@Preview(showBackground = true)
@Composable
fun TodoData(){
    TodoListTheme{

    }
}