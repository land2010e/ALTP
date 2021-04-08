package com.t3h.demoaltp

import android.content.ContentValues
import android.content.Context
import android.content.res.AssetManager
import android.database.sqlite.SQLiteDatabase
import android.os.Environment
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class MrgDatabase {
    private val context:Context
    private var database:SQLiteDatabase?=null
    constructor(context:Context){
        this.context = context
        copyDB()
        createTableHightScore()
    }

    private fun createTableHightScore(){
        val sql = "CREATE TABLE IF NOT EXISTS hight_score " +
                "(id INTEGER NOT NULL, " +
                "name TEXT, " +
                "level_pass INTEGER, " +
                "money TEXT, " +
                "PRIMARY KEY(id AUTOINCREMENT)" +
                ")"
        open()
        //mo ra phien
        database!!.beginTransaction()
        database!!.execSQL(sql)
        database!!.setTransactionSuccessful()
        //setTransactionSuccessful = commit
        close()
    }
    private fun copyDB(){
        //quan ly cac file trong asset: AssetManager
        //de lay duoc AssetManager thi phai co context
        val pathSave = Environment.getDataDirectory().path+"/data/"+
                context.packageName+"/latrieuphu"
        if ( File(pathSave).exists()){
            return
        }

        val assetManager = context.assets
        val input:InputStream = assetManager.open("latrieuphu")

        val out = FileOutputStream(pathSave)
        val b = ByteArray(1024)
        var le = input.read(b)
        while (le >=0){
            out.write(b, 0, le)
            le = input.read(b)
        }
        out.close()
        input.close()
    }

    private fun open(){
        if (database != null && database!!.isOpen){
            return
        }
        val pathSave = Environment.getDataDirectory().path+"/data/"+
                context.packageName+"/latrieuphu"
        database = SQLiteDatabase.openDatabase(pathSave, null,
            SQLiteDatabase.OPEN_READWRITE)
    }

    private fun close(){
        if (database != null && database!!.isOpen){
            database!!.close()
            database = null
        }
    }

    fun get15Question(): MutableList<Question>{
        val listQuestion = mutableListOf<Question>()
        val sql = "select * from (select * from Question order by random()) as t " +
                "GROUP by level order by level"
        open()
        val cursor = database!!.rawQuery(sql,
            null, null)
        cursor.moveToFirst()
        val indexId = cursor.getColumnIndex("_id")
        val indexQuestion = cursor.getColumnIndex("question")
        val  indexLevel = cursor.getColumnIndex("level")
        val  indexCaseA = cursor.getColumnIndex("casea")
        val  indexCaseB = cursor.getColumnIndex("caseb")
        val  indexCaseC = cursor.getColumnIndex("casec")
        val  indexCaseD = cursor.getColumnIndex("cased")
        val  indexTrueCase = cursor.getColumnIndex("truecase")

        while (!cursor.isAfterLast){
            val id = cursor.getInt(indexId)
            val question = cursor.getString(indexQuestion)
            val caseA = cursor.getString(indexCaseA)
            val caseB = cursor.getString(indexCaseB)
            val caseC = cursor.getString(indexCaseC)
            val caseD = cursor.getString(indexCaseD)
            val level = cursor.getInt(indexLevel)
            val trueCase = cursor.getInt(indexTrueCase)

            Log.d("MrgDatabase", "get15Question id: "+id)
            Log.d("MrgDatabase", "get15Question level: "+level)
            Log.d("MrgDatabase", "get15Question question: "+question)
            Log.d("MrgDatabase", "get15Question caseA: "+caseA)
            Log.d("MrgDatabase", "get15Question caseB: "+caseB)
            Log.d("MrgDatabase", "get15Question caseC: "+caseC)
            Log.d("MrgDatabase", "get15Question caseD: "+caseD)
            Log.d("MrgDatabase", "get15Question trueCase: "+trueCase)
            Log.d("MrgDatabase", "get15Question ======================================")
            //nho phai co cai nay
            cursor.moveToNext()

            listQuestion.add(
                Question(question, caseA, caseB, caseC, caseD, level, trueCase)
            )
        }




        close()
        return listQuestion
    }

    fun insertHight(name:String, level:Int, money:String){
        //ContentValues: la key-value
        val content = ContentValues()
        content.put("name", name)
        content.put("level_pass", level)
        content.put("money", money)
        open()
        database!!.beginTransaction()
        database!!.insert("hight_score", null, content)
        database!!.setTransactionSuccessful()
        close()
    }

    fun insertUpdate(id:Int, level:Int, money:String){
        //ContentValues: la key-value
        val content = ContentValues()
        content.put("level_pass", level)
        content.put("money", money)
        open()
        database!!.beginTransaction()
        database!!.update("hight_score", content,
        "id = "+id.toString(), null)
        database!!.setTransactionSuccessful()
        close()
    }
}