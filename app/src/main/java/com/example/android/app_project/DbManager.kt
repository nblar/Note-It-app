package com.example.android.app_project

import android.app.DownloadManager
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder
import android.widget.Toast
import java.nio.ByteOrder

class DbManager {
     var dbName="Mynotes" //database name
    var dbtable="NOtesTable" // database table
    var colID="ID"
    var colTitle = "Title"
    var colDes = "Description"
     var dbVersion =1 ; //db version

    // create table if mynotes doesn't exists
    val sqlCreateTable ="CREATE TABLE IF NOT EXISTS"+ dbtable+ "("+ colID+"INTEGER PRIMARY KEY,"+ colTitle+"TEXT,"+ colDes+"TEXT);"
    var sqlDB: SQLiteDatabase?=null;

    constructor(context: Context)
    {
        var db= DatabaseHelperNotes(context)
        sqlDB= db.writableDatabase
    }
    inner class DatabaseHelperNotes : SQLiteOpenHelper{


        var context : Context? = null;
        constructor(context: Context):super(context,dbName,null,dbVersion){
            this.context= context;
        }

        override fun onCreate(db: SQLiteDatabase?)
        {
        db!!.execSQL(sqlCreateTable)
            Toast.makeText(this.context, "DATABASE CREATED", Toast.LENGTH_SHORT).show();
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
           db!!.execSQL("Drop table if exists"+ dbtable);
        }

    }

    fun insert(values: ContentValues): Long{
        val ID=sqlDB!!.insert(dbtable,"",values)
        return ID
    }

    fun Query(projection:Array<String>, selection:String,selectionArgs:Array<String>,sorOrder:String):Cursor{
        val qb= SQLiteQueryBuilder();
        qb.tables = dbtable
        val cursor= qb.query(sqlDB, projection,selection,selectionArgs,null,null,null,sorOrder)
        return cursor
    }
    fun delete(selection: String, selectionArgs: Array<String>):Int{
        val count=sqlDB!!.delete(dbtable,selection,selectionArgs)
        return count;
    }

    fun update(values: ContentValues, selection: String, selectionArgs: Array<String>) : Int {
      val count= sqlDB!!.update(dbtable, values,selection,selectionArgs)
        return  count;

    }
}