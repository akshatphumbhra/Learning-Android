package hu.ait.recycledemo.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todoTable")
data class Todo(
    @PrimaryKey(autoGenerate = true) var id:Long?,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "createDate") var createDate: String,
    @ColumnInfo(name = "done") var done: Boolean
    )