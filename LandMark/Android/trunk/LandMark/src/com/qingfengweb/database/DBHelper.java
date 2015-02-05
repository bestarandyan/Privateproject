package com.qingfengweb.database;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import com.qingfengweb.constant.ConstantClass;
import com.qingfengweb.constant.DatabaseSql;

/**
 * Created by 13-5-24.
 * 鏁版嵁搴撳府鍔╃被
 */
public class DBHelper extends SQLiteOpenHelper{
    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    public DBHelper(Context context,String name,int version){
        this(context, name, null, version);
    }
    public DBHelper(Context context,String name){
        this(context,name,1);
    }
    public DBHelper(Context context){
        this(context, ConstantClass.DATABASE_NAME);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DatabaseSql.CUXIAO_SQL_STRING);
        sqLiteDatabase.execSQL(DatabaseSql.BRAND_SQL_STRING);
        sqLiteDatabase.execSQL(DatabaseSql.FLOOR_BRAND_SQL_STRING);
        sqLiteDatabase.execSQL(DatabaseSql.FOOD_SQL_STRING);
        sqLiteDatabase.execSQL(DatabaseSql.SPECIALGOODS_SQL_STRING);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }
}
