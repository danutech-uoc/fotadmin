package com.example.fotadmin;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.fotadmin.R;
import com.example.fotadmin.models.Article;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;

public class UploadNewsActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 101;

    private EditText etTitle, etSummary, etDate;
    private Button btnSelectImage, btnUpload;
    private ImageView ivSelectedImage;
    private Spinner spinnerCategory;

    private Uri selectedImageUri;

    private FirebaseStorage storage;
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_news_activity);

        etTitle = findViewById(R.id.etTitle);
        etSummary = findViewById(R.id.etSummary);
        etDate = findViewById(R.id.etDate);
        btnSelectImage = findViewById(R.id.btnSelectImage);
        btnUpload = findViewById(R.id.btnUpload);
        ivSelectedImage = findViewById(R.id.ivSelectedImage);
        spinnerCategory = findViewById(R.id.spinnerCategory);

        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance().getReference("articles");

        String[] categories = {"sports", "academic", "events"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        btnSelectImage.setOnClickListener(v -> openFileChooser());

        etDate.setOnClickListener(v -> showDatePickerDialog());

        btnUpload.setOnClickListener(v -> uploadArticle());
    }

    private void showDatePickerDialog() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (DatePicker view, int selectedYear, int selectedMonth, int selectedDay) -> {
                    // Months are 0-based in Calendar, so add 1
                    String date = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay);
                    etDate.setText(date);
                }, year, month, day);
        datePickerDialog.show();
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            ivSelectedImage.setVisibility(ImageView.VISIBLE);
            Glide.with(this).load(selectedImageUri).into(ivSelectedImage);
        }
    }

    private void uploadArticle() {
        String title = etTitle.getText().toString().trim();
        String summary = etSummary.getText().toString().trim();
        String date = etDate.getText().toString().trim();
        String category = spinnerCategory.getSelectedItem().toString();

        if (title.isEmpty() || summary.isEmpty() || date.isEmpty() || selectedImageUri == null) {
            Toast.makeText(this, "Please fill all fields and select an image", Toast.LENGTH_SHORT).show();
            return;
        }

        StorageReference storageRef = storage.getReference().child("article_images/" + System.currentTimeMillis() + ".jpg");

        storageRef.putFile(selectedImageUri)
                .addOnSuccessListener(taskSnapshot -> storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    String imageUrl = uri.toString();

                    String articleId = database.child(category).push().getKey();
                    Article article = new Article(articleId, title, summary, date, imageUrl);

                    if (articleId != null) {
                        database.child(category).child(articleId).setValue(article)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(UploadNewsActivity.this, "Article uploaded successfully", Toast.LENGTH_SHORT).show();
                                    clearForm();
                                })
                                .addOnFailureListener(e -> Toast.makeText(UploadNewsActivity.this, "Failed to upload article", Toast.LENGTH_SHORT).show());
                    } else {
                        Toast.makeText(this, "Failed to generate article ID", Toast.LENGTH_SHORT).show();
                    }
                }))
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show());
    }

    private void clearForm() {
        etTitle.setText("");
        etSummary.setText("");
        etDate.setText("");
        ivSelectedImage.setImageURI(null);
        ivSelectedImage.setVisibility(ImageView.GONE);
        selectedImageUri = null;
    }
}
