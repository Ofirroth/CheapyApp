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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.cheapy.databinding.ActivityGenderBinding;
import com.example.cheapy.isReturn;

import java.io.ByteArrayOutputStream;

public class GenderActivity extends AppCompatActivity {
    private ActivityGenderBinding binding;
    private String imageString = "";
    private Bitmap selectedImageBitmap;
    private String selectedImage;
    private String username;
    private String name;
    private ImageView imageView; // Declare the ImageView as a class member

    private ActivityResultLauncher<Intent> galleryLauncher;
    private ActivityResultLauncher<Intent> cameraLauncher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGenderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        CardView cardView = binding.cardViewProfileImage;
        imageView = binding.profileImage; // Assign the ImageView reference
        Intent intent = getIntent();
            if (intent != null) {
                username = intent.getStringExtra("username");
                name = intent.getStringExtra("name");
                selectedImage = getIntent().getStringExtra("selectedImage");
                selectedImageBitmap = decodeImage(selectedImage); // Convert string to bitmap
                if (selectedImageBitmap != null) {
                    imageView.setImageBitmap(selectedImageBitmap);
                }

            }

        galleryLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            // Image selected from the gallery
                            Uri imageUri = data.getData();
                            imageView.setImageURI(imageUri);
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
        Button nextButton = binding.buttonNext;
        Button prevButton = binding.buttonPrev;

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the next activity
                Intent intent = new Intent(GenderActivity.this, PasswordActivity.class);
                intent.putExtra("username", username);
                intent.putExtra("name", name);
                if (selectedImageBitmap != null) {
                    Log.e("photo", selectedImageBitmap.toString());
                    imageString = encodeImage(selectedImageBitmap); // Convert bitmap to string
                    intent.putExtra("selectedImage", imageString);
                }
                else {
                    Drawable drawable = imageView.getDrawable();
                    if (drawable instanceof BitmapDrawable) {
                        // Extract the Bitmap from the Drawable
                        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                        imageView.setDrawingCacheEnabled(false);
                        imageString = encodeImage(bitmap);
                        // Pass the bitmap to the next intent if required
                        intent.putExtra("selectedImage", imageString);
                    }
                }
                startActivity(intent);
            }
        });

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the previous activity
                finish();
            }
        });
        return;
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
    private Bitmap decodeImage(String imageString) {
            try {
                byte[] imageBytes = Base64.decode(imageString, Base64.DEFAULT);
                return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
    }
    private String encodeImage(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        byte[] imageBytes = outputStream.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }
    @Override
    protected void onResume() {
        super.onResume();
        if(isReturn.getInstance().getIsReturn()){
            finish();
        }
    }
}