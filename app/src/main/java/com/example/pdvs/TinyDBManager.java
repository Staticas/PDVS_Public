package com.example.pdvs;

import android.content.Context;

import com.example.pdvs.Model.DocModel;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class TinyDBManager {
     private TinyDB tinyDB;



    public TinyDBManager(TinyDB tinyDB) {
        this.tinyDB = tinyDB;
//        tinyDB.remove("first");
//        tinyDB.putInt("DocListLength", 5);

    }

    public void putDocList(ArrayList<DocModel> docList) {
        putDocList(docList,"DocListLength");
    }

    private void putDocList(ArrayList<DocModel> docList, String key){
        tinyDB.checkForNullKey(key);
//        Gson gson = new Gson();
//        ArrayList<String> objStrings = new ArrayList<String>();
        tinyDB.putInt(key, docList.toArray().length);
        int k =1;
        for(DocModel docModel : docList){
//            objStrings.add(gson.toJson(docModel));
            docModel.setId(k);
            tinyDB.putObject(String.valueOf(docModel.getId()), docModel);
            k++;
        }
//        tinyDB.putListString(key, objStrings);
    }

    public List<DocModel> getDocList(){
        int length = tinyDB.getInt("DocListLength");
        List<DocModel> LisDocModel = new ArrayList<DocModel>();
        for (int k =1; k<=length; k++){
          LisDocModel.add(tinyDB.getObject(String.valueOf(k), DocModel.class));
        }
        return LisDocModel;
    }

    public void inserObject(DocModel docModel){
        int length = tinyDB.getInt("DocListLength") +1;
        docModel.setId(length);
        tinyDB.putObject(String.valueOf(length), docModel);
        tinyDB.putInt("DocListLength", length);
    }

    public void updateObject(DocModel docModel){


        tinyDB.putObject(String.valueOf(docModel.getId()), docModel);

    }




}
