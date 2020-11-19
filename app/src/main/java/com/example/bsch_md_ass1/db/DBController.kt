package com.example.bsch_md_ass1.db

import android.content.ContentValues.TAG
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.CursorFactory
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log


class DBController(context: Context?, name: String?, factory: CursorFactory?, version: Int)
    : SQLiteOpenHelper(context, name, factory, version) {

    override fun onCreate(db: SQLiteDatabase) {
        db.createTable(BooksTable)
        db.createTable(CopiesTable)
        db.createTable(LoanersTable)
    }

    //TODO implement backup in here
    override fun onUpgrade(db: SQLiteDatabase, version_old: Int, version_new: Int) {
        db.dropAll()
        onCreate(db)
    }

    fun SQLiteDatabase.dropAll() {
        dropTable(BooksTable)
        dropTable(CopiesTable)
        dropTable(LoanersTable)
    }

    fun dropAll() {
        writableDatabase.dropAll()
    }

    fun SQLiteDatabase.createTable(table : DBTable) {
        var str = "create table ${table.name}("
        for (column in table.columnFormat) {
            str += column.first + " " + column.second
            if (column != table.columnFormat.last())
                str += ','
        }
        str += ")"
        execSQL(str)
    }

    fun SQLiteDatabase.dropTable(table : DBTable) {
        execSQL("drop table ${table.name}")
    }

    fun Cursor.toBook() : Book {
        return Book(
                id = getInt(BooksTable.ID),
                title = getString(BooksTable.Title),
                author = getString(BooksTable.Author),
                genre = getString(BooksTable.Genre),
                copiesAmount = getInt(BooksTable.CopiesAmount),
                loanedCopies = getInt(BooksTable.LoanedCopies)
        )
    }

    fun Cursor.toLoaner(): Loaner {
        return Loaner(
                ID = getInt(LoanersTable.ID),
                fullname = getString(LoanersTable.FullName),
                loanedBooks = getInt(LoanersTable.LoanedBooks)
        )
    }

    fun Cursor.toBookCopy(): BookCopy {
        return BookCopy(
                ID = getInt(CopiesTable.ID),
                book = Book(id = getInt(CopiesTable.BookID)),
                loaner = Loaner(ID = getInt(CopiesTable.Loaner))
        )
    }

    fun addBook(book : Book) {
        val db = writableDatabase
        db.beginTransaction()
        book.loanedCopies = 0
        book.copiesAmount = 0
        try {
            db.insertOrThrow(BooksTable.name, null, book.toContentValues())
            db.setTransactionSuccessful()
        } catch (e: Exception) {
            Log.e(TAG, "Error while trying to add book to database")
        } finally {
            db.endTransaction()
        }
    }

    fun getBook(ID : Int) : Book {
        val db = writableDatabase
        val cursor = db.query(
                BooksTable.name,
                BooksTable.columnNames,
//                arrayOf("*"),
                "${BooksTable.columnNames[BooksTable.ID]} =?",
                arrayOf("$ID"),
                null,
                null,
                null
        )
        cursor.moveToFirst()
        val book = cursor.toBook()
        cursor.close()
        return book
    }

    fun addLoaner(loaner : Loaner) {
        val db = writableDatabase
        db.beginTransaction()
        try {
            db.insertOrThrow(LoanersTable.name, null, loaner.toContentValues())
            db.setTransactionSuccessful()
        } catch (e: Exception) {
            Log.e(TAG, "Error while trying to add loaner to database")
        } finally {
            db.endTransaction()
        }
    }

    fun getLoaner(ID : Int) : Loaner {
        val db = writableDatabase
        val cursor = db.query(
                LoanersTable.name,
                LoanersTable.columnNames,
//                arrayOf("*"),
                "${LoanersTable.columnNames[LoanersTable.ID]} =?",
                arrayOf("$ID"),
                null,
                null,
                null
        )
        cursor.moveToFirst()
        val book = cursor.toLoaner()
        cursor.close()
        return book
    }

    fun addCopy(copy : BookCopy) {
        addCopy(copy.book.id!!)
    }

    fun addCopy(bookID : Int) {
        val db = writableDatabase
        val book = getBook(bookID)
        book.copiesAmount = book.copiesAmount?.plus(1)
        db.beginTransaction()
        try {
            db.insertOrThrow(CopiesTable.name, null, BookCopy(book).toContentValues())
            db.update(
                    BooksTable.name,
                    book.toContentValues(),
                    "${BooksTable.columnNames[BooksTable.ID]} =?",
                    arrayOf("${book.id}")
            )
            db.setTransactionSuccessful()
        } catch (e: Exception) {
            Log.e(TAG, "Error while trying to add copy to database")
        } finally {
            db.endTransaction()
        }
    }

    fun getCopy(ID : Int) : BookCopy {
        val db = writableDatabase
        val cursor = db.query(
                CopiesTable.name,
                CopiesTable.columnNames,
//                arrayOf("*"),
                "${CopiesTable.columnNames[CopiesTable.ID]} =?",
                arrayOf("$ID"),
                null,
                null,
                null
        )
        cursor.moveToFirst()
        val copy = cursor.toBookCopy()
        cursor.close()
        copy.book = getBook(copy.book.id!!)
        copy.loaner = getLoaner(copy.loaner!!.ID!!)
        return copy
    }

    fun getCopiesOf(book : Book) : List<BookCopy> {
        val db = writableDatabase
        val cursor = db.query(
                CopiesTable.name,
                CopiesTable.columnNames,
                "${CopiesTable.columnNames[CopiesTable.BookID]} =?",
                arrayOf("${book.id}"),
                null, null, null
        )
        cursor.moveToFirst()
        val list = ArrayList<BookCopy>()
        for (i in 0 until cursor.count) {
            val copy = cursor.toBookCopy()
            copy.book = getBook(copy.book.id!!)
            copy.loaner = getLoaner(copy.loaner!!.ID!!)
            list.add(copy)
        }
        cursor.close()
        return list
    }

    fun loan(loaner : Loaner, copy : BookCopy) {
        val db = writableDatabase

        val book = copy.book
        book.loanedCopies = book.loanedCopies?.plus(1)
        loaner.loanedBooks += 1

        copy.loaner = loaner
        db.beginTransaction()
        try {
            db.update(
                    CopiesTable.name,
                    copy.toContentValues(),
                    "${CopiesTable.columnNames[CopiesTable.ID]} =?",
                    arrayOf("${copy.ID}")
            )
            db.update(
                    BooksTable.name,
                    book.toContentValues(),
                    "${BooksTable.columnNames[BooksTable.ID]} =?",
                    arrayOf("${book.id}")
            )
            db.update(
                    LoanersTable.name,
                    loaner.toContentValues(),
                    "${LoanersTable.columnNames[LoanersTable.ID]} =?",
                    arrayOf("${loaner.ID}")
            )
            db.setTransactionSuccessful()
        } catch (e: Exception) {
            Log.e(TAG, "Error while trying to add loan to database")
        } finally {
            db.endTransaction()
        }
    }

    fun returnLoan(copy : BookCopy) {
        val db = writableDatabase

        val loaner = copy.loaner
        val book = copy.book
        book.loanedCopies = book.loanedCopies?.minus(1)
        loaner?.loanedBooks = loaner?.loanedBooks?.minus(1)!!

        copy.loaner = null
        db.beginTransaction()
        try {
            db.update(
                    CopiesTable.name,
                    copy.toContentValues(),
                    "${CopiesTable.columnNames[CopiesTable.ID]} =?",
                    arrayOf("${copy.ID}")
            )
            db.update(
                    BooksTable.name,
                    book.toContentValues(),
                    "${BooksTable.columnNames[BooksTable.ID]} =?",
                    arrayOf("${book.id}")
            )
            db.update(
                    LoanersTable.name,
                    loaner.toContentValues(),
                    "${LoanersTable.columnNames[LoanersTable.ID]} =?",
                    arrayOf("${loaner.ID}")
            )
            db.setTransactionSuccessful()
        } catch (e: Exception) {
            Log.e(TAG, "Error while trying to remove loan from database")
        } finally {
            db.endTransaction()
        }
    }

    fun getCopiesLoanedBy(loaner : Loaner): ArrayList<BookCopy> {
        val db = writableDatabase
        val cursor = db.query(
                CopiesTable.name,
                CopiesTable.columnNames,
                "${CopiesTable.columnNames[CopiesTable.Loaner]} =?",
                arrayOf("${loaner.ID}"),
                null, null, null
        )
        cursor.moveToFirst()
        val list = ArrayList<BookCopy>()
        for (i in 0 until cursor.count) {
            val copy = cursor.toBookCopy()
            copy.book = getBook(copy.book.id!!)
            copy.loaner = getLoaner(copy.loaner!!.ID!!)
            list.add(copy)
        }
        cursor.close()
        return list
    }

    val allBooks : ArrayList<Book>
        get() {
            val list = ArrayList<Book>()
            val cursor = writableDatabase.query(
                    BooksTable.name,
                    BooksTable.columnNames,
//                arrayOf("*"),
                    null,null,null,null,null
            )
            cursor.moveToFirst()
            for (i in 0 until cursor.count) {
                list.add(cursor.toBook())
                cursor.moveToNext()
            }
            cursor.close()
            return list
        }

    object BooksTable : DBTable("books", arrayOf(
                "ID" to "integer primary key autoincrement",
                "Title" to "string",
                "Author" to "string",
                "Genre" to "string",
                "CopiesAmount" to "integer DEFAULT 0",
                "LoanedCopies" to "integer DEFAULT 0"
    )) {
            const val ID = 0
            const val Title = 1
            const val Author = 2
            const val Genre = 3
            const val CopiesAmount = 4
            const val LoanedCopies = 5
    }

    object CopiesTable : DBTable("copies", arrayOf(
                "ID" to "integer primary key autoincrement",
                "BookID" to "integer",
                "Loaner" to "integer",
    )) {
            const val ID = 0
            const val BookID = 1
            const val Loaner = 2
    }

    object LoanersTable : DBTable("loaners", arrayOf(
                "ID" to "integer primary key autoincrement",
                "Fullname" to "string",
                "LoanedBooks" to "integer DEFAULT 0"
    )) {
            const val ID = 0
            const val FullName = 1
            const val LoanedBooks = 2
    }
}
