package com.example.pdvs.Utils;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.example.pdvs.Model.DocModel;
import com.example.pdvs.TinyDB;
import com.example.pdvs.TinyDBManager;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHandler extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String NAME = "PDVSDatabase";
    private static final String DOCINFO_TABLE = "doc";
    private static final String ID = "id";
    private static final String DOC_INFO = "docInfo";
    private static final String STATUS = "status";

    private static final String IMG = "docPhoto";
    private static final String CREATE_DOCINFO_TABLE ="CREATE TABLE " + DOCINFO_TABLE + " (" +
            ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + DOC_INFO + " TEXT, " + STATUS + " INTEGER," + IMG + " BLOB)";
    private SQLiteDatabase db;
    private TinyDBManager tinyDBManager;


    public DataBaseHandler(Context context){
        super(context, NAME, null, VERSION );
//        TinyDB tinyDB = new TinyDB(context);
//        tinyDBManager = new TinyDBManager(tinyDB);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
//        db.execSQL(CREATE_DOCINFO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("DROP TABLE IF EXISTS " + DOCINFO_TABLE);
//        onCreate(db);
    }

    public void openDatabase(){
        db = this.getWritableDatabase();
    }
    public void insertDoc(DocModel docInfo){
//        ContentValues cv = new ContentValues();
//        cv.put(DOC_INFO, docInfo.getDocInfo());
//        cv.put(STATUS, 0);
//        cv.put(IMG,docInfo.getImage());
//        db.insert(DOCINFO_TABLE, null, cv);


    }

    public  void insertDocList(List<DocModel> docModelList){
//        tinyDBManager.putDocList(new ArrayList<>(docModelList));


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
                        docInfo.setDocInfo(cur.getString(cur.getColumnIndex(DOC_INFO)));
                        docInfo.setStatus(cur.getInt(cur.getColumnIndex(STATUS)));
                        docInfo.setImage(cur.getBlob(cur.getColumnIndex(IMG)));
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

    public void updateImage(int id,byte[] image ){
//        ContentValues cv = new ContentValues();
//        cv.put(IMG, image);
//        db.update(DOCINFO_TABLE, cv, ID + "=?", new String[] {String.valueOf(id)});

    }

    public void updateStatus(int id, int status){
//        ContentValues cv = new ContentValues();
//        cv.put(STATUS, status);
//        db.update(DOCINFO_TABLE, cv, ID + "=?", new String[] {String.valueOf(id)});

    }

    public void updateDocInfo(int id, String docInfo){
//        ContentValues cv = new ContentValues();
//        cv.put(DOC_INFO, docInfo);
//        db.update(DOCINFO_TABLE, cv, ID + "=?", new String[] {String.valueOf(id)});
    }

    public void deleteDocs(int id){
//      db.delete(DOCINFO_TABLE, ID + "=?", new String[] {String.valueOf(id)});
    }
}
