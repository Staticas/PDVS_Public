package com.example.pdvs;

import android.content.Context;

import com.example.pdvs.Model.DocModel;
import com.google.gson.Gson;

import java.util.ArrayList;

public class TinyDBManager {
     private TinyDB tinyDB;


    public TinyDBManager(TinyDB tinyDB) {
        this.tinyDB = tinyDB;

    }

    public void putDocList(ArrayList<DocModel> docList) {
        putDocList(docList,"first");
    }

    public void putDocList(ArrayList<DocModel> docList, String key){
        tinyDB.checkForNullKey(key);
        Gson gson = new Gson();
        ArrayList<String> objStrings = new ArrayList<String>();
        for(DocModel docModel : docList){
            objStrings.add(gson.toJson(docModel));
        }
        tinyDB.putListString(key, objStrings);
    }
}
