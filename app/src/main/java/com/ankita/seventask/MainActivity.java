package com.ankita.seventask;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.ankita.seventask.adapter.ItemCamaraImageAdapter;
import com.ankita.seventask.adapter.ItemImageAdapter;
import com.ankita.seventask.adapter.Photo;
import com.ankita.seventask.repository.APIClient;
import com.ankita.seventask.repository.APIInterface;
import com.ankita.seventask.repository.CreateResponse;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission_group.CAMERA;

public class MainActivity extends AppCompatActivity {

    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private static final int RESULT_LOAD_IMAGE = 12;
    private static final int CAMERA_REQUEST = 10;
    RecyclerView rvItemImages;
    ArrayList<CreateResponse.User> userArrayList;
    ItemImageAdapter itemImageAdapter;
    ItemCamaraImageAdapter itemCamaraImageAdapter;
    ArrayList<Photo> arrayList=new ArrayList<>();
    APIInterface apiInterface;
    MyViewModel viewModel;
    FloatingActionButton fabSelect,fabCamara,fabGalary;
    boolean isFabOpen=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        getData();
        fabSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!isFabOpen){
                    showFABMenu();
                }else{
                    closeFABMenu();
                }
            }
        });
        fabCamara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissionAndAddImage();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });
        fabGalary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });
    }

    private void checkPermissionAndAddImage() {
        if (!checkPermissions()) {
            requestPermissionss();


        } else {
            Toast.makeText(this, " permission already granted", Toast.LENGTH_LONG).show();

        }
    }

    private void requestPermissionss() {
        ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.CAMERA},MY_CAMERA_REQUEST_CODE );

    }

    private boolean checkPermissions() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);

        return result == PackageManager.PERMISSION_GRANTED ;
    }

    private void showFABMenu() {
        isFabOpen=true;
        fabCamara.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
        fabGalary.animate().translationX(-getResources().getDimension(R.dimen.standard_105));
    }
    private void closeFABMenu() {
        isFabOpen=false;
        fabCamara.animate().translationY(0);
        fabGalary.animate().translationX(0);
    }

    private void getData() {
       viewModel.init();
       viewModel.getNewsRepository().observe(this, new Observer<List<CreateResponse.User>>() {
           @Override
           public void onChanged(List<CreateResponse.User> users) {
               userArrayList.addAll(users);
               setAdapter();
           }
       });
    }

    private void setAdapter() {
        itemImageAdapter=new ItemImageAdapter(this,userArrayList);
        rvItemImages.setAdapter(itemImageAdapter);
    }

    private void init() {
        rvItemImages=findViewById(R.id.rvItemImages);
        apiInterface= APIClient.getClient().create(APIInterface.class);
        fabSelect=findViewById(R.id.fabSelect);
        fabCamara= findViewById(R.id.fabCamara);
        fabGalary= findViewById(R.id.fabGallary);
        userArrayList=new ArrayList<>();
        viewModel = ViewModelProviders.of(this).get(MyViewModel.class);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_CAMERA_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if ( cameraAccepted){}
                    else {

                        Toast.makeText(this, "Permission Denied, You cannot access location data and camera.", Toast.LENGTH_LONG).show();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                                showMessageOKCancel("You need to allow access to both the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{ Manifest.permission.CAMERA},
                                                            MY_CAMERA_REQUEST_CODE);
                                                }
                                            }
                                        });
                                return;
                            }
                        }
                        else {
                            Toast.makeText(this, "lower api level", Toast.LENGTH_LONG).show();

                        }

                    }
                }


                break;
        }
    }
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==12){
            if(data.getData()!=null) {
                Uri selectedImage = data.getData();
                Bitmap photo = null;
                try {
                     photo=getBitmapFromUri(selectedImage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                if (selectedImage != null) {
                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    Photo pht=new Photo();
                    pht.setUri(photo);
                    arrayList.add(pht);
                    itemCamaraImageAdapter.notifyDataSetChanged();
                    Log.d("___@___",picturePath);
                    cursor.close();
                }
            }
        }
        else if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            Uri tempUri = getImageUri(getApplicationContext(), photo, "abc.jpg");
            String picturePath = getRealPathFromURI(tempUri);
            Log.d("___@___",picturePath);
            Photo photo1=new Photo();
            photo1.setUri(photo);
            arrayList.add(photo1);
            setAdapter1();


        }
    }

    private void setAdapter1() {
        itemCamaraImageAdapter=new ItemCamaraImageAdapter(this,arrayList);
        rvItemImages.setAdapter(itemCamaraImageAdapter);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage, String imageName) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, imageName, null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }
    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }
}
