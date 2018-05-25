package com.shubham.tripin1.officehandleruser.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.florent37.viewanimator.AnimationListener;
import com.github.florent37.viewanimator.ViewAnimator;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import com.shubham.tripin1.officehandleruser.Managers.SharedPrefManager;
import com.shubham.tripin1.officehandleruser.R;
import com.shubham.tripin1.officehandleruser.Utils.CircleTransform;
import com.shubham.tripin1.officehandleruser.Utils.ImageUtils;


@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
public class RegActivity extends AppCompatActivity implements ImageUtils.ImagePickerListener {

    private EditText mEditTextFname;
    private EditText mEditTextLname;

    private ImageView mImgBtn;
    private FloatingActionButton fab;
    private Boolean mBoolImgSet;
    private Context mContext;
    private SharedPrefManager mSharedPrefManager;
    private static final int PICK_IMAGE_REQUEST = 234;
    private Uri mFilePath;
    private ImageUtils mImageUtils;
    private StorageReference mStorageRef;
    private TextView mTxtInfo, mTxtUploadPic;


    static final int REQUEST_CODE_RECOVER_PLAY_SERVICES = 1001;
    static final int REQUEST_CODE_ASK_PERMISSIONS = 1002;
    final int PIC_CROP = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);
        initView();
        setListeners();
        mContext = getApplicationContext();
        mSharedPrefManager = new SharedPrefManager(mContext);
        mImageUtils = new ImageUtils(this);
        mBoolImgSet = false;

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivity(new Intent(this, SplashActivity.class));
            finish();
        }
        mStorageRef = FirebaseStorage.getInstance().getReference();

        getSupportActionBar().setTitle("User Profile - Offee");


    }


    private void setListeners() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                if (mEditTextLname.getText().toString().isEmpty()
                        || mEditTextFname.getText().toString().isEmpty()) {

                    Toast.makeText(mContext, "Fill up all fields human!", Toast.LENGTH_LONG).show();

                } else {

                    if (mBoolImgSet) {
                        uploadFile();
                    } else {
                        Toast.makeText(getApplicationContext(), "Select Image", Toast.LENGTH_LONG).show();
                    }

                }
            }
        });

        mImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mImageUtils.imagepicker(1);
            }
        });


    }

    private void hideKeyboard() {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    private void initView() {
        mEditTextFname = (EditText) findViewById(R.id.editTextfname);
        mEditTextLname = (EditText) findViewById(R.id.editTextlaname);

        mImgBtn = (ImageView) findViewById(R.id.imageButton);
        fab = (FloatingActionButton) findViewById(R.id.floatingActionButton);

        mTxtInfo = (TextView) findViewById(R.id.text_info);
        mTxtUploadPic = (TextView) findViewById(R.id.textViewUploadpic);

    }

    //handling the image chooser activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_CANCELED) {
            if (requestCode == 0) {
                mImageUtils.onActivityResult(requestCode, resultCode, data);
            }

            if (requestCode == 1) {
                mImageUtils.onActivityResult(requestCode, resultCode, data);
            }

        }

    }

    @Override
    public void onPicked(int from, String filename, Bitmap file, Uri uri) {
        mFilePath = uri;
        Glide.with(getApplicationContext())
                .load(uri)
                .centerCrop().crossFade().bitmapTransform(new CircleTransform(mContext))
                .placeholder(R.drawable.profile)
                .into(mImgBtn);
        ViewAnimator.animate(mImgBtn).rubber().start().onStop(new AnimationListener.Stop() {
            @Override
            public void onStop() {
                ViewAnimator.animate(fab).flash().start();
                mTxtInfo.setText("press next button!");
            }
        });
        mBoolImgSet = true;
        mTxtUploadPic.setText("Looking good there! :D ");
    }

    //this method will upload the file
    private void uploadFile() {
        //if there is a file to upload
        if (mFilePath != null) {
            //displaying a progress dialog while upload is going on
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading Image");
            progressDialog.show();
            progressDialog.setCancelable(false);

            StorageReference riversRef = mStorageRef.child("userImages/" + FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber() + ".jpg");
            riversRef.putFile(mFilePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //if the upload is successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();
                            //and displaying a success toast
                            registrationSuccess();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            //if the upload is not successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();
                            //and displaying error message
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //calculating progress percentage
                            UploadTask.TaskSnapshot t = taskSnapshot;
                            @SuppressWarnings("VisibleForTests")
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            //displaying percentage in progress dialog
                            progressDialog.setMessage("Uploaded " + progress + "%...");
                        }
                    });
        }
        //if there is not any file
        else {
            Toast.makeText(mContext, "Some error in pic", Toast.LENGTH_LONG).show();
        }
    }

    private void registrationSuccess() {
        Toast.makeText(getApplicationContext(), "Registration Success", Toast.LENGTH_LONG).show();
        startActivity(new Intent(mContext, MainActivity.class));
        mSharedPrefManager.setMobileNo(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
        mSharedPrefManager.setUserName(mEditTextFname.getText().toString());
        mSharedPrefManager.setUserLastName(mEditTextLname.getText().toString());
        mSharedPrefManager.setUserReg("1");
        finish();
    }


}
