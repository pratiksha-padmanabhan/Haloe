package com.example.plantdiseasedetection;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    ImageView mImageView;
    Button mChooseBtn,snap;
    Bitmap picture = null;
    Classifier classifier;
    TextView result;

    private final int IMAGE_PICK_CODE=1000;
    private final int PERMISSION_CODE=1001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        classifier = new Classifier(Utils.assetFilePath(this,"model.pt"));
        mImageView = findViewById(R.id.image_view);
        result = findViewById(R.id.resulttv);
        mChooseBtn = findViewById(R.id.choose_image_btn);
        snap=findViewById(R.id.btn_snap);
        snap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent,1888);
            }
        });
        mChooseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        requestPermissions(permissions, PERMISSION_CODE);
                    } else {
                        pickImageFromGallery();
                    }
                } else {

                }
            }


        });

    }
    private void pickImageFromGallery(){
        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String pred="";
        if(resultCode==RESULT_OK && requestCode==IMAGE_PICK_CODE){
            mImageView.setImageURI(data.getData());
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
             pred = classifier.predict(bitmap);
            Toast.makeText(this,"Prediction done",Toast.LENGTH_SHORT).show();
            if(pred.equalsIgnoreCase("Rust")) {
                //Toast.makeText(this," txt one equalsic yes",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity.this, rustSoln.class);
                startActivity(intent);

            }
            else if(pred.equalsIgnoreCase("Scab")) {
               // Toast.makeText(this,"gone into else",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity.this, scabSoln.class);
                startActivity(intent);
            }
            else if(pred.equalsIgnoreCase("Healthy")){
                Intent intent = new Intent(MainActivity.this, healthySoln.class);
                startActivity(intent);
            }
            else {
                Intent intent = new Intent(MainActivity.this, mdSoln.class);
                startActivity(intent);
            }
           result.setText(pred);
        }
        else if(requestCode == 1888) {
            picture = (Bitmap) data.getExtras().get("data");
            mImageView.setImageBitmap(picture);

             pred = classifier.predict(picture);

            result.setText(pred.length());
            System.out.println("******************************************************;");
            System.out.println(pred.length());
            result.setText(pred.length());
            if(pred.equalsIgnoreCase("Rust")) {
                //Toast.makeText(this," txt one equalsic yes",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity.this, rustSoln.class);
                startActivity(intent);

            }
            else if(pred.equalsIgnoreCase("Scab")) {
                // Toast.makeText(this,"gone into else",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity.this, scabSoln.class);
                startActivity(intent);
            }
            else if(pred.equalsIgnoreCase("Healthy")){
                Intent intent = new Intent(MainActivity.this, healthySoln.class);
                startActivity(intent);
            }
            else {
                Intent intent = new Intent(MainActivity.this, mdSoln.class);
                startActivity(intent);
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode){
            case PERMISSION_CODE:{
                if(grantResults.length>0&& grantResults[0]== PackageManager.PERMISSION_GRANTED)
                    pickImageFromGallery();
                else{
                    Toast.makeText(this,"Permission denied",Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
