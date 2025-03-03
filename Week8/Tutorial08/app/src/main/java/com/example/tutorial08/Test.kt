package com.example.tutorial08
import java.io.File

fun main() {
    print("\n----- Task 1 -----\n")

    File("C:/Users/Admin/Documents/kwan.txt").forEachLine {
        print(it)
    }

    print("\n----- Task 2 -----\n")
    val lines = File("C:/Users/Admin/Documents/kwan.txt").readLines()
    lines.forEach {
        print(it)
    }

    print("\n----- Task 3 -----\n")
    val list = mutableListOf<ListItem>()
    File("C:/Users/Admin/Documents/kwan.txt").forEachLine {
        list.add(ListItem(it))
    }

    list.forEach {
        print(it.line)
    }

    print("\n----- Task 4 -----\n")
    val listTask = mutableListOf<ListItemDouble>()
    File("C:/Users/Admin/Documents/kwan2.txt").forEachLine {
        val data = it.split(",")
        listTask.add(ListItemDouble(data[0], data[1]))
    }

    listTask.forEach {
        print(it.columnA + " - " + it.columnB + "\n")
    }

}