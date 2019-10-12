package com.example.cardmaker.MyCardEdit;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.example.cardmaker.BuildConfig;
import com.example.cardmaker.ImageEdit.EditLogo3;
import com.example.cardmaker.R;

import com.example.cardmaker.Send;
import com.example.cardmaker.fragments.dialogfragments.DEditCA;
import com.example.cardmaker.fragments.dialogfragments.DEditDSC1;
import com.example.cardmaker.fragments.dialogfragments.DEditDSC2;
import com.example.cardmaker.fragments.dialogfragments.DEditDSC3;
import com.example.cardmaker.fragments.dialogfragments.DEditPD;
import com.example.cardmaker.fragments.dialogfragments.DEditPN;
import com.example.cardmaker.fragments.dialogfragments.DEditPP;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class McEdit3 extends FragmentActivity implements DEditPN.DialogEditPNListener,
        DEditPD.DialogEditPDListener, DEditPP.DialogEditPPListener,
        DEditCA.DialogEditCAListener, DEditDSC1.DialogEditDSC1Listener,
        DEditDSC2.DialogEditDSC2Listener, DEditDSC3.DialogEditDSC2Listener {

    private static final String TAG = "McEdit3";

    TextView name,address,phone,designation,dec1,dec2,dec3,optionMenu;
    ImageView logo;

    Context mContext;

    LinearLayout sl;
    Button save,share,send;
    File file;
    File picDir;

    String tempLogo = "";

    private UploadTask uploadTask;
    private StorageReference mStorageRef;
    private String photoStringLink;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    private String name1,address1,designation1,phone1,dec11,dec22,dec33,logo1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mc_edit3);

        mContext = McEdit3.this;

        mStorageRef = FirebaseStorage.getInstance().getReference("editcard");
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("usertemplate").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("four");

        sl = findViewById(R.id.container);
        save = findViewById(R.id.save);

        send = findViewById(R.id.send);

        //optionMenu = findViewById(R.id.optionMenu);
        name = findViewById(R.id.name);
        address = findViewById(R.id.address);
        phone = findViewById(R.id.phone);
        designation = findViewById(R.id.designation);
        dec1 = findViewById(R.id.dsc1);
        dec2 = findViewById(R.id.dsc2);
        dec3 = findViewById(R.id.dsc3);
        logo = findViewById(R.id.logo);

        //optionMenu.setVisibility(View.GONE);

        share = findViewById(R.id.share);

        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogName();
            }
        });
        designation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogDesignation();
            }
        });
        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogAddress();
            }
        });
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogPhone();
            }
        });
        dec1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogDSC1();
            }
        });
        dec2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogDSC2();
            }
        });
        dec3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogDSC3();
            }
        });


        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, EditLogo3.class);
                intent.putExtra("logo",tempLogo);
                startActivity(intent);
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //File file = saveBitMap(mContext, sl);
                sl.post(new Runnable() {
                    @Override
                    public void run() {
                        Bitmap pic = takeScreenshot(sl);
                        try {
                            if (pic != null){
                                saveScreenshoot(pic);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sl.post(new Runnable() {
                    @Override
                    public void run() {
                        Bitmap pic = takeScreenshot(sl);
                        try {
                            if (pic != null){
                                Intent intent = new Intent(mContext,Send.class);
                                ByteArrayOutputStream bs = new ByteArrayOutputStream();
                                pic.compress(Bitmap.CompressFormat.PNG, 50, bs);
                                intent.putExtra("byteArray", bs.toByteArray());
                                startActivity(intent);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (file != null){
                    //uploadFile(file);
                    shareImage(file);
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(McEdit3.this);
                    builder.setMessage("Please Save the File First......");
                    builder.setTitle("Message: ");
                    builder.setCancelable(false);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null){
                    name1 = dataSnapshot.child("name").getValue().toString();
                    if (name1 != null){
                        name.setText(name1);
                    }else {
                        name.setText("Name");
                    }
                    Log.d(TAG, "onDataChange: LOL" + name);

                    designation1 = dataSnapshot.child("designation").getValue().toString();
                    if (designation1 != null){
                        designation.setText(designation1);
                    }else {
                        designation.setText("Designation");
                    }

                    phone1 = dataSnapshot.child("phone").getValue().toString();
                    if (phone1 != null){
                        phone.setText(phone1);
                    }else {
                        phone.setText("Contact No");
                    }

                    address1 = dataSnapshot.child("email").getValue().toString();
                    if (address1 != null){
                        address.setText(address1);
                    }else {
                        address.setText("Address");
                    }

                    dec11 = dataSnapshot.child("dec1").getValue().toString();
                    if (dec11 != null){
                        dec1.setText(dec11);
                    }else {
                        dec1.setText("Description 1");
                    }

                    dec22 = dataSnapshot.child("dec2").getValue().toString();
                    if (dec22 != null){
                        dec2.setText(dec22);
                    }else {
                        dec2.setText("Description 2");
                    }

                    dec33 = dataSnapshot.child("dec3").getValue().toString();
                    if (dec33 != null){
                        dec3.setText(dec33);
                    }else {
                        dec3.setText("Description 3");
                    }

                    logo1 = dataSnapshot.child("logo").getValue().toString();
                    if (logo1 != null){
                        Glide.with(mContext).load(logo1).into(logo);
                    }
                    else {
                        Glide.with(mContext).load(tempLogo).into(logo);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Failed to read value
                Log.w("Database Error!!!", "Failed to read value.", databaseError.toException());
            }
        });
    }

    private Bitmap takeScreenshot(View v){
        Bitmap screenshoot = null;
        try {
            int width = v.getMeasuredWidth();
            int hight = v.getMeasuredHeight();
            screenshoot = Bitmap.createBitmap(width,hight, Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(screenshoot);
            v.draw(c);
        }catch (Exception e){
            e.printStackTrace();
        }
        return screenshoot;
    }

    private void scanGallery(Context cntx, String path) {
        try {
            MediaScannerConnection.scanFile(cntx, new String[]{path}, null, new MediaScannerConnection.OnScanCompletedListener() {
                public void onScanCompleted(String path, Uri uri) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("TAG", "There was an issue scanning gallery.");
        }
    }

    protected boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    protected void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(this, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 100:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //do your work
                    File file1 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"CardMaker");
                    if (!file1.exists()){
                        file1.mkdirs();
                        Toast.makeText(mContext,"Done",Toast.LENGTH_SHORT).show();
                    }
                    Bitmap pic = takeScreenshot(sl);
                    try {
                        if (pic != null){
                            saveScreenshoot(pic);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                } else {
                    Log.e("value", "Permission Denied, You cannot use local drive .");
                }
                break;

            default:
                break;
        }
    }

    private void saveScreenshoot(Bitmap bitmap){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPermission()) {
                //do your work
                picDir = null;
                picDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"CardMaker");

                if (!picDir.exists()){
                    picDir.mkdirs();
                    Toast.makeText(mContext,"Done",Toast.LENGTH_SHORT).show();
                }
            } else {
                requestPermission();
            }
        }

        ByteArrayOutputStream bao = null;
        file = null;
        try {
            bao = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bao);
            String filename = picDir.getPath() +File.separator+ System.currentTimeMillis()+".jpg";
            file = new File(filename);
            file.createNewFile();

            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bao.toByteArray());
            fos.close();

            if (file != null){
                uploadFile(file);
                Toast.makeText(mContext,"Added to Mycard",Toast.LENGTH_SHORT).show();
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        scanGallery(mContext,file.getAbsolutePath());
    }

    private void shareImage(File file){
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Uri uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID, file);
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");

        intent.putExtra(Intent.EXTRA_SUBJECT, "Sending Image of My Created Cards.");
        intent.putExtra(Intent.EXTRA_TEXT, "PLEASE CHECK THE LINK TO DOWNLOAD THE APP \n https://drive.google.com/open?id=1P0mxx_nJgQpbubXDDb7iP49irJadIACX");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        try {
            startActivity(Intent.createChooser(intent, "Share My Cards"));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(mContext, "No App Available", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadFile(File file){
        Uri uri = Uri.fromFile(file);

        if (uri != null){
            final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(uri));
            uploadTask = fileReference.putFile(uri);

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {

                        Uri downloadUri = task.getResult();
                        Toast.makeText(mContext, "Successfully uploaded", Toast.LENGTH_SHORT).show();
                        if (downloadUri != null) {
                            photoStringLink = downloadUri.toString(); //YOU WILL GET THE DOWNLOAD URL HERE !!!!
                            Log.d(TAG, "onComplete: LLLLLLLLLL" + photoStringLink);

                            if (photoStringLink != null){

                                String key = FirebaseDatabase.getInstance().getReference("mycardtemp")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).push().getKey();

                                DatabaseReference tempRef = FirebaseDatabase.getInstance()
                                        .getReference("mycardtemp")
                                        .child(FirebaseAuth.getInstance()
                                                .getCurrentUser().getUid()).child(key);

                                HashMap<String, Object> result = new HashMap<>();
                                result.put("image", photoStringLink);
                                tempRef.updateChildren(result);

                                HashMap<String, Object> result1 = new HashMap<>();
                                result1.put("id", key);
                                tempRef.updateChildren(result1);
                            }
                        }
                    } else {
                        // Handle failures
                        // ...
                        Toast.makeText(mContext, "upload failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }


    @Override
    public void applyTextPN(String input) {
        DatabaseReference tempRef = FirebaseDatabase.getInstance().getReference("usertemplate")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("four");

        HashMap<String, Object> result1 = new HashMap<>();
        result1.put("name", input);
        tempRef.updateChildren(result1);
        Log.d(TAG, "onClick: Temp.....CAddress updated");

        name.setText(input);

        Toast.makeText(mContext,"Name Successfully Updated",Toast.LENGTH_SHORT).show();
    }
    @Override
    public void applyTextPD(String input) {

        DatabaseReference tempRef = FirebaseDatabase.getInstance().getReference("usertemplate")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("four");

        HashMap<String, Object> result1 = new HashMap<>();
        result1.put("designation", input);
        tempRef.updateChildren(result1);
        Log.d(TAG, "onClick: Temp.....CAddress updated");
        designation.setText(input);
        Toast.makeText(mContext,"Designation Successfully Updated",Toast.LENGTH_SHORT).show();
    }
    @Override
    public void applyTextPP(String input) {
        DatabaseReference tempRef = FirebaseDatabase.getInstance().getReference("usertemplate")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("four");

        HashMap<String, Object> result1 = new HashMap<>();
        result1.put("phone", input);
        tempRef.updateChildren(result1);
        Log.d(TAG, "onClick: Temp.....CAddress updated");
        phone.setText(input);
        Toast.makeText(mContext,"Phone Successfully Updated",Toast.LENGTH_SHORT).show();
    }
    @Override
    public void applyTextCA(String input) {
        DatabaseReference tempRef = FirebaseDatabase.getInstance().getReference("usertemplate")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("four");

        HashMap<String, Object> result1 = new HashMap<>();
        result1.put("email", input);
        tempRef.updateChildren(result1);
        Log.d(TAG, "onClick: Temp.....CAddress updated");
        address.setText(input);
        Toast.makeText(mContext,"Address Successfully Updated",Toast.LENGTH_SHORT).show();
    }
    @Override
    public void applyTextDSC1(String input) {
        DatabaseReference tempRef = FirebaseDatabase.getInstance().getReference("usertemplate")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("four");

        HashMap<String, Object> result1 = new HashMap<>();
        result1.put("dec1", input);
        tempRef.updateChildren(result1);
        Log.d(TAG, "onClick: Temp.....CAddress updated");
        dec1.setText(input);
        Toast.makeText(mContext,"Description Successfully Updated",Toast.LENGTH_SHORT).show();
    }
    @Override
    public void applyTextDSC2(String input) {
        DatabaseReference tempRef = FirebaseDatabase.getInstance().getReference("usertemplate")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("four");

        HashMap<String, Object> result1 = new HashMap<>();
        result1.put("dec2", input);
        tempRef.updateChildren(result1);
        Log.d(TAG, "onClick: Temp.....CAddress updated");
        dec2.setText(input);
        Toast.makeText(mContext,"Description Successfully Updated",Toast.LENGTH_SHORT).show();
    }
    @Override
    public void applyTextDSC3(String input) {
        DatabaseReference tempRef = FirebaseDatabase.getInstance().getReference("usertemplate")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("four");

        HashMap<String, Object> result1 = new HashMap<>();
        result1.put("dec3", input);
        tempRef.updateChildren(result1);
        Log.d(TAG, "onClick: Temp.....CAddress updated");
        dec3.setText(input);
        Toast.makeText(mContext,"Description Successfully Updated",Toast.LENGTH_SHORT).show();
    }

    private void openDialogAddress() {
        DEditCA editCA = new DEditCA();
        editCA.show(getSupportFragmentManager(),"edit_address");
    }
    private void openDialogName() {
        DEditPN editCA = new DEditPN();
        editCA.show(getSupportFragmentManager(),"edit_name");
    }
    private void openDialogPhone() {
        DEditPP editCA = new DEditPP();
        editCA.show(getSupportFragmentManager(),"edit_phone");
    }
    private void openDialogDesignation() {
        DEditPD editCA = new DEditPD();
        editCA.show(getSupportFragmentManager(),"edit_designation");
    }
    private void openDialogDSC1() {
        DEditDSC1 editCA = new DEditDSC1();
        editCA.show(getSupportFragmentManager(),"edit_dec1");
    }
    private void openDialogDSC2() {
        DEditDSC2 editCA = new DEditDSC2();
        editCA.show(getSupportFragmentManager(),"edit_dec2");
    }
    private void openDialogDSC3() {
        DEditDSC3 editCA = new DEditDSC3();
        editCA.show(getSupportFragmentManager(),"edit_dec3");
    }
}
