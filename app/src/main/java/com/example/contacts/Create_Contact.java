package com.example.contacts;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.example.contacts.databinding.ActivityCreateContactBinding;

import java.io.File;
import java.io.Serializable;

public class Create_Contact extends AppCompatActivity {
    private static final int IMAGE_CAPTURE_CODE =1001 ;
    private ActivityCreateContactBinding binding;
    private static final int PERMISSION_CODE=1000;
    private int idContact=-1;
    Uri image_uri=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateContactBinding.inflate(getLayoutInflater());
        View viewRoot = binding.getRoot();
        setContentView(viewRoot);
        Intent receivedIntent=getIntent();
       if(receivedIntent.getExtras()!=null){
            int id=receivedIntent.getIntExtra("id",-1);
            String name=receivedIntent.getStringExtra("name");
            String email=receivedIntent.getStringExtra("email");
            String phone=receivedIntent.getStringExtra("phone");
            String avt=receivedIntent.getStringExtra("avt");
            idContact=id;
           binding.edtFirstName.setText(name);
            binding.edtEmail.setText(email);
            binding.edtFoneNumber.setText(phone);
            if(avt.equals("None")==false){
                image_uri=Uri.parse(avt);
                binding.imvAvt.setImageURI(image_uri);
            }
        }
        binding.tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=binding.edtFirstName.getText().toString();
                String email=binding.edtEmail.getText().toString();
                String phone=binding.edtFoneNumber.getText().toString();
                if(name.isEmpty()==false &&email.isEmpty()==false &&phone.isEmpty()==false){
                    Intent intent=new Intent();
                    if(idContact!=-1){
                        intent.putExtra("id",idContact);
                    }
                    intent.putExtra("name",name);
                    intent.putExtra("email",email);
                    intent.putExtra("phone",phone);
                    if(image_uri==null) {
                        intent.putExtra("avt","None");
                    }else{
                        String avt=image_uri.toString();
                        intent.putExtra("avt",avt);
                }
                   setResult(RESULT_OK,intent);
                   finish();}
                else {
                    Context context=getApplicationContext();
                    Toast.makeText(context,"Please fill out application", Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding.btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                    if(checkSelfPermission(Manifest.permission.CAMERA)== PackageManager.PERMISSION_DENIED ||
                            checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_DENIED) {
                        String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permission, PERMISSION_CODE);
                    }
                    else{
                        openCamera();
                    }
                }
                else{
                    openCamera();
                }
            }
        });
    }

    private void openCamera() {
        ContentValues values=new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION,"From camera");
        image_uri=getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);
        Intent cameraIntent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,image_uri);
        startActivityForResult(cameraIntent,IMAGE_CAPTURE_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                } else {
                    Toast.makeText(this, "Permission denied..", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            binding.imvAvt.setImageURI(image_uri);
        }
    }
}