package com.example.cheapy.Sign_up;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.cheapy.R;
import com.example.cheapy.databinding.ActivitySignUpBinding;
import com.example.cheapy.isReturn;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class SignUpActivity extends AppCompatActivity {

    private ActivitySignUpBinding binding;
    private String imageString = "";

    private Bitmap selectedImageBitmap;
    private boolean checkInput = false;
    private ImageView imageView; // Declare the ImageView as a class member

    private ActivityResultLauncher<Intent> galleryLauncher;
    private ActivityResultLauncher<Intent> cameraLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Button nextButton = binding.buttonNext;
        CardView cardView = binding.cardViewProfileImage;
        imageView = binding.profileImage;
        imageView.setImageResource(R.drawable.profilepic);// Assign the ImageView reference
        //in the first time that i entered to the sign-up
        isReturn.getInstance().setIsReturn(false);
        ImageButton backButton = binding.backButton;
        backButton.setOnClickListener(v -> onBackPressed());

        galleryLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            // Image selected from the gallery
                            Uri imageUri = data.getData();
                            try {
                                // Convert Uri to Bitmap
                                Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                                imageView.setImageBitmap(bitmap);
                                selectedImageBitmap = bitmap;
                                // Use the bitmap as needed
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                });

        cameraLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            // Photo captured from the camera
                            Bitmap photo = (Bitmap) data.getExtras().get("data");
                            imageView.setImageBitmap(photo);
                            selectedImageBitmap = photo;
                        }
                    }
                });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open a dialog or activity to upload a new picture
                openUploadDialog();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInputValid()) {
                    EditText editTextName = binding.SignUpName;
                    EditText editTextUserName = binding.SignUpUsername;
                    String inputName = editTextName.getText().toString().trim();
                    String inputUserName = editTextUserName.getText().toString().trim();
                    // Start the next activity
                    Intent intent = new Intent(SignUpActivity.this, ContactInfoActivity.class);
                    intent.putExtra("name", inputName);
                    intent.putExtra("username", inputUserName);
                    if (selectedImageBitmap != null) {
                        imageString = encodeImage(selectedImageBitmap); // Convert bitmap to string
                        intent.putExtra("imageBitmap", imageString);
                    }
                    else {
                        Drawable drawable = imageView.getDrawable();
                        if (drawable instanceof BitmapDrawable) {
                            // Extract the Bitmap from the Drawable
                            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                            imageView.setDrawingCacheEnabled(false);
                            imageString = encodeImage(bitmap);
                            // Pass the bitmap to the next intent if required
                            intent.putExtra("imageBitmap", imageString);
                        }
                    }
                    startActivity(intent);
                }
                else {
                    Toast.makeText(SignUpActivity.this, "Please enter all required fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isInputValid() {
        EditText editTextName = binding.SignUpName;
        EditText editTextUserName = binding.SignUpUsername;
        String inputName = editTextName.getText().toString().trim();
        String inputUserName = editTextUserName.getText().toString().trim();
        if ((!inputName.isEmpty()) && (!inputUserName.isEmpty())){
            checkInput = true;
        }
        return checkInput;
    }

    private void openUploadDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Upload Picture");

        // Set the dialog view with options to select an image from the gallery or take a photo
        builder.setItems(new CharSequence[]{"Choose from Gallery", "Take a Photo"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        // Choose from Gallery option
                        openGallery();
                        break;
                    case 1:
                        // Take a Photo option
                        openCamera();
                        break;
                }
            }
        });

        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryLauncher.launch(intent);
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraLauncher.launch(intent);
    }
    private String encodeImage(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        byte[] imageBytes = outputStream.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return encodedImage;
    }
    @Override
    protected void onResume() {
        super.onResume();
        if(isReturn.getInstance().getIsReturn()){
            finish();
        }
    }

}
