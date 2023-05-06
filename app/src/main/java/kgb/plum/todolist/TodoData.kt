package kgb.plum.todolist

data class TodoData(
    val key : Int,
    val content: String,
    val done: Boolean = false
)
