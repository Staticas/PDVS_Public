package com.example.pdvs;

import android.app.Activity;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;


import com.example.pdvs.Model.DocModel;
import com.example.pdvs.Utils.DataBaseHandler;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;


public class AddNewDoc extends BottomSheetDialogFragment {
    private static final int RESULT_LOAD_IMAGE = 99;
    public static final String TAG = "ActionBottomDialog";
    private EditText newDocText;
    private Button newDocSaveButton;
    private DataBaseHandler db;

    private ImageView newImageView;

    private Button addNewImageButton;


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


        boolean isUpdate = false;
        final Bundle bundle = getArguments();
        if(bundle != null){
            isUpdate = true;
            String docInfo = bundle.getString("docInfo");
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

        addNewImageButton.setOnClickListener(v-> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, RESULT_LOAD_IMAGE);
        });


        boolean finalIsUpdate = isUpdate;
        newDocSaveButton.setOnClickListener(v -> {
            String text = newDocText.getText().toString();
            if (finalIsUpdate){
                db.updateDocInfo(bundle.getInt("id"), text);
                Bitmap bm=((BitmapDrawable)newImageView.getDrawable()).getBitmap();
                db.updateImage(bundle.getInt("id"), convertBitmapToByteArray(bm));
            }else{
                DocModel docInfo = new DocModel();
                docInfo.setDocInfo(text);
                docInfo.setStatus(0);
                Bitmap bm=((BitmapDrawable)newImageView.getDrawable()).getBitmap();
                docInfo.setImage(convertBitmapToByteArray(bm));
                db.insertDoc(docInfo);

            }
            dismiss();
        });
    }

    public static byte[] convertBitmapToByteArray(Bitmap bitmap){
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            return baos.toByteArray();
        }finally {
            if(baos != null){
                try {
                    baos.close();
                } catch (IOException e) {
                    Log.e("this", "ByteArrayOutputStream was not closed");
                }
            }
        }
    }

    public static byte[] convertBitmapToByteArrayUncompressed(Bitmap bitmap){
        ByteBuffer byteBuffer = ByteBuffer.allocate(bitmap.getByteCount());
        bitmap.copyPixelsToBuffer(byteBuffer);
        byteBuffer.rewind();
        return byteBuffer.array();
    }

    public static Bitmap convertCompressedByteArrayToBitmap(byte[] src){
        return BitmapFactory.decodeByteArray(src, 0, src.length);
    }

    public Bitmap decodeBitmap(Uri selectedImage) throws FileNotFoundException {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(selectedImage), null, o);

        final int REQUIRED_SIZE = 100;

        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if ( width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE ) {
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
    @Override
    public void onDismiss(@NonNull DialogInterface dialog){
        Activity activity = getActivity();
        if ( activity instanceof DialogCloseListener ) {
           ((DialogCloseListener)activity).handleDialogClose(dialog);

        }
    }
}
