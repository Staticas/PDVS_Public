package com.example.pdvs.Utils;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.pdvs.Model.DocModel;

import java.io.ByteArrayOutputStream;
import java.io.Console;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

public class DataBaseHandler extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String NAME = "PDVSDatabase";
    private static final String DOCINFO_TABLE = "doc";
    private static final String ID = "id";
    private static final String DOC_INFO = "docInfo";
    private static final String STATUS = "status";

    private static final String DOC_PHOTO = "docPhoto";
    private static final String CREATE_DOCINFO_TABLE ="CREATE TABLE " +
            DOCINFO_TABLE + " (" +
            ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            DOC_INFO + " TEXT, " +
            STATUS + " INTEGER, " +
            DOC_PHOTO + " BLOB );";
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
        db.execSQL("DROP TABLE IF EXISTS " + DOCINFO_TABLE);
        onCreate(db);
    }

    // convert from bitmap to byte array
    public static byte[] getBytes(Bitmap bitmap) {
        if ( bitmap == null ){return null;}
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    // convert from byte array to bitmap
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    public void openDatabase(){
        db = this.getWritableDatabase();
    }
    public void insertDoc(DocModel docInfo){
        ContentValues cv = new ContentValues();
        cv.put(DOC_INFO, docInfo.getDocInfo());
        cv.put(STATUS, 0);
        if ( docInfo.getImageView()!= null ){
            cv.put(DOC_PHOTO, docInfo.getImageView());
        }
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
                        docInfo.setDocInfo(cur.getString(cur.getColumnIndex(DOC_INFO)));
                        docInfo.setStatus(cur.getInt(cur.getColumnIndex(STATUS)));
                        byte[] blob = cur.getBlob(cur.getColumnIndex(DOC_PHOTO));
                        if ( blob != null ){
                            Log.i("blon not null", "og");
                            docInfo.setImageView(blob);
                        }
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

//    public void updateImage(int id, Bitmap imageView ){
//        ContentValues cv = new ContentValues();
//
//        cv.put(DOC_PHOTO, getBytes(imageView));
//        db.update(DOCINFO_TABLE, cv, ID + "=?", new String[] {String.valueOf(id)});
//    }

    public void updateImageBytes(int id, byte[] image ){
        ContentValues cv = new ContentValues();

        cv.put(DOC_PHOTO, image);
        db.update(DOCINFO_TABLE, cv, ID + "=?", new String[] {String.valueOf(id)});
    }

    public void updateStatus(int id, int status){
        ContentValues cv = new ContentValues();
        cv.put(STATUS, status);
        db.update(DOCINFO_TABLE, cv, ID + "=?", new String[] {String.valueOf(id)});

    }

    public void updateDocInfo(int id, String docInfo){
        ContentValues cv = new ContentValues();
        cv.put(DOC_INFO, docInfo);
        db.update(DOCINFO_TABLE, cv, ID + "=?", new String[] {String.valueOf(id)});
    }

    public void deleteDocs(int id){
      db.delete(DOCINFO_TABLE, ID + "=?", new String[] {String.valueOf(id)});
    }
}
