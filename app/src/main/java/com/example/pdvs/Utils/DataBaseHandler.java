package com.example.pdvs.Utils;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.pdvs.Model.DocModel;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHandler extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String NAME = "PDVSDatabase";
    private static final String DOCINFO_TABLE = "Doc";
    private static final String ID = "id";
    private static final String DOC = "docinfo";
    private static final String STATUS = "status";
    private static final String CREATE_DOCINFO_TABLE ="CREATE TABLE " + DOCINFO_TABLE + "(" +
            ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + DOC + "TEXT, " + STATUS + " INTEGER)";
    private SQLiteDatabase db;

    public DataBaseHandler(Context context){
        super(context, NAME, null, VERSION );
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_DOCINFO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + DOCINFO_TABLE);
        onCreate(db);
    }

    public void openDatabase(){
        db = this.getWritableDatabase();
    }
    public void insertDoc(DocModel docinfo){
        ContentValues cv = new ContentValues();
        cv.put(DOC, docinfo.getDocInfo());
        cv.put(STATUS, 0);
        db.insert(DOCINFO_TABLE, null, cv);
    }
    @SuppressLint("Range")
    public List<DocModel> getAllDoc(){
        List<DocModel> docsList = new ArrayList<>();
        Cursor cur = null;
        db.beginTransaction();
        try{
            cur= db.query(DOCINFO_TABLE, null,null,null,null,null,null,null);
            if(cur!= null){
                if (cur.moveToFirst()){
                    do {
                        DocModel docInfo = new DocModel();
                        docInfo.setId(cur.getInt(cur.getColumnIndex(ID)));
                        docInfo.setDocInfo(cur.getString(cur.getColumnIndex(DOC)));
                        docInfo.setStatus(cur.getInt(cur.getColumnIndex(STATUS)));
                        docsList.add(docInfo);
                    }while(cur.moveToNext());
                }
            }
        }finally {
            db.endTransaction();
            cur.close();
        }
        return docsList;

    }

    public void updateStatus(int id, int status){
        ContentValues cv = new ContentValues();
        cv.put(STATUS, status);
        db.update(DOCINFO_TABLE, cv, ID + "=?", new String[] {String.valueOf(ID)});

    }

    public void updateDocInfo(int id, String docinfo){
        ContentValues cv = new ContentValues();
        cv.put(DOC, docinfo);
        db.update(DOCINFO_TABLE, cv, ID + "=?", new String[] {String.valueOf(ID)});
    }

    public void deleteDocs(int id){
      db.delete(DOCINFO_TABLE, ID + "=?", new String[] {String.valueOf(ID)});
    }
}
