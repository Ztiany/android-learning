package me.ztiany.android.database.origin.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.database.sqlite.SQLiteDatabase
import android.os.Environment
import android.util.Log
import me.ztiany.android.database.ORIGIN_TAG
import java.io.File


/** 查看手机中由SQLiteDatabase创建的的数据库文件 */
fun showDataBase(sqLiteDatabase: SQLiteDatabase) {
    val ll = sqLiteDatabase.attachedDbs
    for (i in ll.indices) {
        val p = ll[i]
        Log.d(ORIGIN_TAG, p.first + "=" + p.second)
    }
}


/**
 * Context.openOrCreateDatabase 打开或创建数据库，可以指定数据库文件的操作模式
 *
 * @param database 数据库名称
 * @param context 上下文
 */
fun openOrCreateDatabase(context: Context, database: String) {
    /**指定数据库的名称为 info2.db，并指定数据文件的操作模式为MODE_PRIVATE */
    val sqLiteDatabase = context.openOrCreateDatabase(database, MODE_PRIVATE, null)
    /**查看改对象所创建的数据库 */
    showDataBase(sqLiteDatabase)
}

/**
 * SQLiteDatabase.openOrCreateDatabase 打开或创建数据库
 *
 * @param fileName 可以指定数据库文件的路径
 */
fun sQLiteDatabase(fileName: String) {
    val dataBaseFile = File(Environment.getExternalStorageDirectory(), "/sqlite$fileName")
    if (!dataBaseFile.parentFile.exists()) {
        dataBaseFile.mkdirs()
    }
    val sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(dataBaseFile, null)
    showDataBase(sqLiteDatabase)
}