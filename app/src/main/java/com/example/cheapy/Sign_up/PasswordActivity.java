package com.example.cheapy.Sign_up;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cheapy.API.UserAPI;
import com.example.cheapy.R;
import com.example.cheapy.databinding.ActivityPasswordBinding;
import com.example.cheapy.isReturn;

import java.io.ByteArrayOutputStream;

public class PasswordActivity extends AppCompatActivity {
    private ActivityPasswordBinding binding;
    private String profilePic;
    private String username;
    private String displayName;
    private String password;
    private Bitmap selectedImageBitmap;
    private String selectedImage;

    private  String encodedImage;
    private ImageView imageView; // Declare the ImageView as a class member

    private static final int GALLERY_REQUEST_CODE = 1;
    private static final int CAMERA_REQUEST_CODE = 2;

    private ActivityResultLauncher<Intent> galleryLauncher;
    private ActivityResultLauncher<Intent> cameraLauncher;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private TextView errorMessageTextView;
    private ImageView passwordVisibility1;
    private ImageView passwordVisibility2;

    private boolean showPassword1 = false;
    private boolean showPassword2 = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        CardView cardView = binding.cardViewProfileImage;
        imageView = binding.profileImage; // Assign the ImageView reference
        Intent intent = getIntent();
        if (intent != null) {
            username = intent.getStringExtra("username");
            displayName = intent.getStringExtra("name");
            selectedImage = getIntent().getStringExtra("selectedImage");
            selectedImageBitmap = decodeImage(selectedImage); // Convert string to bitmap
            if (selectedImageBitmap != null) {
                imageView.setImageBitmap(selectedImageBitmap);
            }
        }
        passwordEditText = binding.editPassword;
        confirmPasswordEditText = binding.confirmPassword;
        errorMessageTextView = binding.errorMsg;

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

        Button submitButton = binding.buttonSubmit;
        Button prevButton = binding.buttonPrev;
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        passwordVisibility1 = binding.visibilityPassword;
        passwordVisibility2 = binding.visibilityConfirmPassword;

        passwordEditText.addTextChangedListener(passwordTextWatcher);
        confirmPasswordEditText.addTextChangedListener(confirmPasswordTextWatcher);

        passwordVisibility1.setOnClickListener(v -> {
            showPassword1 = !showPassword1;
            togglePasswordVisibility(passwordEditText, passwordVisibility1, showPassword1);
        });

        passwordVisibility2.setOnClickListener(v -> {
            showPassword2 = !showPassword2;
            togglePasswordVisibility(confirmPasswordEditText, passwordVisibility2, showPassword2);
        });

        submitButton.setOnClickListener(v -> validatePasswords());
    }

    private void togglePasswordVisibility(EditText editText, ImageView visibilityImageView, boolean showPassword) {
        if (showPassword) {
            editText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            visibilityImageView.setImageResource(R.drawable.ic_action_eye);
        } else {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            visibilityImageView.setImageResource(R.drawable.ic_action_eye);
        }
        editText.setSelection(editText.getText().length()); // Maintain cursor position at the end
    }

    private TextWatcher passwordTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            errorMessageTextView.setText("");
        }
    };

    private TextWatcher confirmPasswordTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            errorMessageTextView.setText("");
        }
    };

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

    private void validatePasswords() {
        int validPassword=0;
        password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();

        if (!password.equals(confirmPassword)) {
            errorMessageTextView.setText(R.string.error_passwords_do_not_match);
            errorMessageTextView.setTextColor(getResources().getColor(R.color.black, null));
            return;
        }

        int caps = password.replaceAll("[^A-Z]", "").length();
        int small = password.replaceAll("[^a-z]", "").length();
        int num = password.replaceAll("[^0-9]", "").length();
        int specialSymbol = password.replaceAll("[^@$!%*?&]", "").length();

        if (caps < 1) {
            errorMessageTextView.setText(R.string.error_missing_uppercase);
            errorMessageTextView.setTextColor(getResources().getColor(R.color.black, null));
        } else if (small < 1) {
            errorMessageTextView.setText(R.string.error_missing_lowercase);
            errorMessageTextView.setTextColor(getResources().getColor(R.color.black, null));
        } else if (num < 1) {
            errorMessageTextView.setText(R.string.error_missing_number);
            errorMessageTextView.setTextColor(getResources().getColor(R.color.black, null));
        } else if (specialSymbol < 1) {
            errorMessageTextView.setText(R.string.error_missing_special_symbol);
            errorMessageTextView.setTextColor(getResources().getColor(R.color.black, null));
        } else {
            validateAll();
        }
    }
    private void validateAll(){
        UserAPI userAPI = new UserAPI();
        userAPI.register(username, password, displayName, selectedImage, (int callback) -> {
            if (callback == 200) {
                // Implement your logic here when the callback is complete and the boolean is true
                Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show();
                isReturn.getInstance().setIsReturn(true);
                finish();
            }
            else if (callback == 409) {
                Toast.makeText(this, "User name is already exist", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "error in sign up", Toast.LENGTH_SHORT).show();
            }
        });
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
