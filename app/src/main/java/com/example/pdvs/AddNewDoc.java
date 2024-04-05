package com.example.pdvs;

import static android.provider.MediaStore.Images.Media.getBitmap;
import static com.example.pdvs.Utils.DataBaseHandler.getBytes;
import static com.example.pdvs.Utils.DataBaseHandler.getImage;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;


import com.example.pdvs.Model.DocModel;
import com.example.pdvs.Utils.DataBaseHandler;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.FileNotFoundException;


public class AddNewDoc extends BottomSheetDialogFragment {
    public static final String TAG = "ActionBottomDialog";
    private static final  int RESULT_LOAD_IMAGE = 99;
    private EditText newDocText;
    private Button newDocSaveButton;

    private Button addNewImageButton;

    private Button galleryButton;
    private ImageView newImageView;
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
        addNewImageButton = getView().findViewById(R.id.addNewImageButton);
        newImageView = getView().findViewById(R.id.newImageView);

//        addNewImageButton.findViewById();
        addNewImageButton.setOnClickListener(v-> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, RESULT_LOAD_IMAGE);
        });

        boolean isUpdate = false;
        final Bundle bundle = getArguments();
        if(bundle != null){
            isUpdate = true;
            String docInfo = bundle.getString("docInfo");
            byte[ ]  photo =bundle.getByteArray("docPhoto");
            if (photo != null  && photo.length>0) {
                newImageView.setImageBitmap(getImage(photo));
            }
            newDocText.setText(docInfo);
            if(docInfo.length()>0){
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

//            newImageView.invalidate();
//            BitmapDrawable drawable = (BitmapDrawable) newImageView.getDrawable();
//            Bitmap bitmap = drawable.getBitmap();

            //byte[] img = img.get
            if (finalIsUpdate){
                db.updateDocInfo(bundle.getInt("id"), text);

                DocModel docInfo = new DocModel();
                byte[]  photo =bundle.getByteArray("docPhoto");
                if (photo != null  && photo.length>0) {
                    docInfo.setImageView(photo);
                    db.updateImageBytes(bundle.getByte("docPhoto"), photo);
                }
                else {
                    docInfo.setImageView(null);
                    db.updateImageBytes(bundle.getByte("docPhoto"), null);
                }
                docInfo.setDocInfo(text);
                docInfo.setStatus(0);

                db.insertDoc(docInfo);

            }
            dismiss();
        });
    }
    @Override
    public void onDismiss(@NonNull DialogInterface dialog){
        Activity activity = getActivity();
        if ( activity instanceof DialogCloseListener ) {
           ((DialogCloseListener)activity).handleDialogClose(dialog);

        }
    }

    public  Bitmap decodeBitmap(Uri selectedImage) throws FileNotFoundException {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(selectedImage), null, o);

        final int REQUIRED_SIZE = 100;

        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE) {
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(selectedImage), null, o2);
    }


    @Deprecated
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
//        Toast.makeText( getContext(), "be4 try catch", Toast.LENGTH_SHORT).show();
        switch (requestCode) {
            case RESULT_LOAD_IMAGE:
                if ( resultCode == Activity.RESULT_OK ) {
//                    Toast.makeText( getContext(), "outside try catch", Toast.LENGTH_SHORT).show();
                    Uri selectedImage = intent.getData();
                    Bitmap bitmapImage = null;
                    try {
                        bitmapImage = decodeBitmap(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
//                    Toast.makeText( getContext(), "inside try catch", Toast.LENGTH_SHORT).show();
                    // Show the Selected Image on ImageView
//                    ImageView imageView = newImageView.findViewById(R.id.newImageView);
//                    imageView.setImageBitmap(bitmapImage);
                    newImageView.setImageBitmap(bitmapImage);
//                    imageView2.setImageBitmap(bitmapImage);

                }
        }
    }

}
