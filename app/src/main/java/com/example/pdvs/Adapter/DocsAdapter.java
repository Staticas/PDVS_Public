package com.example.pdvs.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.pdvs.AddNewDoc;
import com.example.pdvs.MainActivity;
import com.example.pdvs.Model.DocModel;
import com.example.pdvs.R;
import com.example.pdvs.TinyDBManager;
import com.example.pdvs.Utils.DataBaseHandler;

import java.util.List;

public class DocsAdapter extends RecyclerView.Adapter<DocsAdapter.ViewHolder> {

    private List<DocModel> docsList;
    private MainActivity activity;
    private DataBaseHandler db;

    private TinyDBManager TDB;

//    public DocsAdapter(DataBaseHandler db, MainActivity activity){
//        this.db = db;
//        this.activity = activity;
//    }

    public DocsAdapter(TinyDBManager TDB, MainActivity activity){
        this.TDB = TDB;
        this.activity = activity;
    }
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.doc_layout, parent, false);
        return  new ViewHolder(itemView);
    }

    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    public void onBindViewHolder(ViewHolder holder, int position){
//        db.openDatabase();
        DocModel item = docsList.get(position);
        holder.checkBox.setText(item.getDocInfo());
        holder.checkBox.setChecked(toBoolean(item.getStatus()));
        if ( item.getImage() == null ){
            Toast.makeText(activity, "Img NULL", Toast.LENGTH_SHORT).show();

        }else {
            holder.imageView2.setImageBitmap(getImage(item.getImage()));
        }

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
//                db.updateStatus(item.getId(),1);
                item.setStatus(1);
            }else{
//                db.updateStatus(item.getId(),0);
                item.setStatus(0);
            }
            notifyItemChanged(position);
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
        bundle.putByteArray("docPhoto", item.getImage());
        AddNewDoc fragment = new AddNewDoc(TDB);
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(), AddNewDoc.TAG);
        notifyItemChanged(position);
    }



    public static class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox checkBox;
        ImageView imageView2;

        ViewHolder(View view){
            super(view);
            checkBox = view.findViewById(R.id.docCheckBox);
            imageView2 = view.findViewById(R.id.imageView2);
        }
    }
}
