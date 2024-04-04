package com.example.pdvs;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.pdvs.Model.DocModel;
import com.example.pdvs.Utils.DataBaseHandler;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.example.pdvs.DialogCloseListener;

import org.jetbrains.annotations.NotNull;

public class AddNewDoc extends BottomSheetDialogFragment {
    public static final String TAG = "ActionBottomDialog";
    private EditText newDocText;
    private Button newDocSaveButton;
    private DataBaseHandler db;

    public static AddNewDoc newInstance(){
        return new AddNewDoc();
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.DialogStyle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.new_doc, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        newDocText = getView().findViewById(R.id.newDocText);
        newDocSaveButton = getView().findViewById(R.id.newDocButton);


        boolean isUpdate = false;
        final Bundle bundle = getArguments();
        if(bundle != null){
            isUpdate = true;
            String doc = bundle.getString("doc");
            newDocText.setText(doc);
            if(doc.length()>0){
                newDocSaveButton.setTextColor(ContextCompat.getColor(getContext(),R.color.colorPrimary));
            }
        }

        db = new DataBaseHandler(getActivity());
        db.openDatabase();

        newDocText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")){
                    newDocSaveButton.setEnabled(false);
                    newDocSaveButton.setTextColor(Color.GRAY);
                }else{
                    newDocSaveButton.setEnabled(true);
                    newDocSaveButton.setTextColor(ContextCompat.getColor(getContext(),R.color.colorPrimary));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        boolean finalIsUpdate = isUpdate;
        newDocSaveButton.setOnClickListener(v -> {
            String text = newDocText.getText().toString();
            if (finalIsUpdate){
                db.updateDocInfo(bundle.getInt("id"), text);
            }else{
                DocModel docInfo = new DocModel();
                docInfo.setDocInfo(text);
                docInfo.setStatus(0);
                db.insertDoc(docInfo);
            }
            dismiss();
        });
    }
    @Override
    public void onDismiss(@NonNull DialogInterface dialog){
        Activity activity = new Activity();
//        if (activity instanceof DialogCloseListener){
           ((DialogCloseListener)activity).handleDialogClose(dialog);

//        }
    }
}
