package kgb.plum.todolist

import android.os.Bundle
import android.widget.CheckBox
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

    val onToggle: (Int, Boolean) -> Unit = {key, checked ->
        val index = todoList.indexOfFirst { it.key == key }
        todoList[index] = todoList[index].copy(done = checked)
    }

    val onEdit: (Int, String) -> Unit = {key, content ->
        val index = todoList.indexOfFirst { it.key == key }
        todoList[index] = todoList[index].copy(content = content)
    }

    val onDelete : (Int) -> Unit = {key ->
        val index = todoList.indexOfFirst {it.key == key}
        todoList.removeAt(index)
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
                    TodoItem(
                        todoData = todoData,
                        onToggle = onToggle,
                        onEdit = onEdit,
                        onDelete = onDelete
                    )
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

@Composable
fun TodoItem(
    todoData: TodoData,
    onToggle: (Int, Boolean) -> Unit = {_,_ -> },
    onEdit: (Int, String) -> Unit = {_,_ ->},
    onDelete: (Int) -> Unit = {_ -> }
) {
    var isEditing by remember {mutableStateOf(false)}

    Card(
        modifier = Modifier.padding(4.dp)
    ) {
        Crossfade(targetState = isEditing) {
            when(it) {
                false -> {
                    Row(
                        modifier = Modifier.padding(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Text(
                            text = todoData.content,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "완료"
                        )
                        Checkbox(
                            checked = todoData.done,
                            onCheckedChange = {
                                onToggle(todoData.key, it)
                            }
                        )
                        Button(
                            onClick = {
                                isEditing = true
                            },
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.DarkGray),
                            modifier = Modifier.background(color = Color.DarkGray, shape = RoundedCornerShape(6.dp))
                        ) {
                            Text(
                                text = "수정",
                                color = Color.White,
                                fontSize = 12.sp
                                )
                        }
                        Spacer(modifier = Modifier.size(6.dp))
                        Button(
                            onClick = {
                                onDelete(todoData.key)
                            },
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.DarkGray),
                            modifier = Modifier
                                .background(
                                    color = Color.DarkGray,
                                    shape = RoundedCornerShape(6.dp)
                                )
                                .wrapContentSize(unbounded = true)
                        ) {
                            Text(
                                text = "삭제",
                                color = Color.White,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
                true -> {
                    val (newText, setText) = remember { mutableStateOf(todoData.content) }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        OutlinedTextField(
                            value = newText,
                            onValueChange = setText,
                            modifier = Modifier.weight(1f),
                            colors = TextFieldDefaults.outlinedTextFieldColors(focusedBorderColor = Color.DarkGray)
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        Button(
                            onClick = {
                                onEdit(todoData.key, newText)
                                isEditing = false
                            },
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.DarkGray),
                            modifier = Modifier.background(color = Color.DarkGray, shape = RoundedCornerShape(6.dp))
                        ) {
                            Text(
                                text = "수정",
                                color = Color.White,
                            )
                        }
                    }
                }
            }
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
        TodoItem(TodoData(1,"TodoList 구현하기"))
    }
}