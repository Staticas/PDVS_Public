package com.example.pdvs.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.recyclerview.widget.RecyclerView;

import com.example.pdvs.AddNewDoc;
import com.example.pdvs.MainActivity;
import com.example.pdvs.Model.DocModel;
import com.example.pdvs.R;
import com.example.pdvs.Utils.DataBaseHandler;

import java.util.List;

public class DocsAdapter extends RecyclerView.Adapter<DocsAdapter.ViewHolder> {

    private List<DocModel> docsList;
    private MainActivity activity;
    private DataBaseHandler db;
    public DocsAdapter(DataBaseHandler db, MainActivity activity){
        this.db = db;
        this.activity = activity;
    }
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.doc_layout, parent, false);
        return  new ViewHolder(itemView);
    }

    public void onBindViewHolder(ViewHolder holder, int position){
        db.openDatabase();
        DocModel item = docsList.get(position);
        holder.doc.setText(item.getDocInfo());
        holder.doc.setChecked(toBoolean(item.getStatus()));
        holder.doc.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                db.updateStatus(item.getId(),1);
            }else{
                db.updateStatus(item.getId(),0);
            }
        });
    }

    public int getItemCount(){
        return docsList.size();
    }

    private boolean toBoolean(int n){
        return n!=0;
    }

    public void setDocs(List<DocModel>docsList){
        this.docsList = docsList;
        notifyDataSetChanged();
    }

    public Context getContext(){return activity;}

    public void deleteItem(int position){
        DocModel item = docsList.get(position);
        db.deleteDocs(item.getId());
        docsList.remove(position);
        notifyItemRemoved(position);
    }


    public void editItem(int position){
        DocModel item = docsList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("docInfo", item.getDocInfo());
        AddNewDoc fragment = new AddNewDoc();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(), AddNewDoc.TAG);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox doc;

        ViewHolder(View view){
            super(view);
            doc = view.findViewById(R.id.docCheckBox);
        }
    }
}
