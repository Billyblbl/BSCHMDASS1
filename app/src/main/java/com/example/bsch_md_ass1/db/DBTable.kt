package com.example.bsch_md_ass1.db

open class DBTable(val name : String, val columnFormat : Array<Pair<String, String>>) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DBTable

        if (name != other.name) return false
        if (!columnFormat.contentEquals(other.columnFormat)) return false

        return true
    }

    val columnNames : Array<String>
        get() = columnFormat.map { it.first }.toTypedArray()

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + columnFormat.contentHashCode()
        return result
    }
}