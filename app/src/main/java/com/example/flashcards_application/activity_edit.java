package com.example.flashcards_application;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;



public class activity_edit extends AppCompatActivity {
    private EditText editTextQuestion;
    private EditText editTextAnswer;
    private Button buttonSave;

    private FlashcardDbHelper dbHelper;
    private long id = -1 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        // Retrieve the flashcard data passed from MainActivity
        Intent intent = getIntent();
        String question = intent.getStringExtra(""); // The "" helps in displaying the question and answer in the EditActivity
        String answer = intent.getStringExtra("");  // when user clicks on the flashcard in MainActivity
        int position = intent.getIntExtra("", -1); // The -1 helps in displaying the question and answer in the EditActivity

        // Initialize UI components
        editTextQuestion = findViewById(R.id.editTextQuestion);
        editTextAnswer = findViewById(R.id.editTextAnswer);
        buttonSave = findViewById(R.id.buttonSave);

        // Set the initial text for question and answer EditTexts
        //related to line 24 and25 in the activity_edit.java file.
        // If the question and answer are not null, set the text
        if (question != null && !question.isEmpty()) {
            editTextQuestion.setText(question);
        }
        if (answer != null && !answer.isEmpty()) {
            editTextAnswer.setText(answer);
        }

        // Set up button click listener to save edited data
        buttonSave.setOnClickListener(v -> {
            // Get the edited question and answer text
            String editedQuestion = editTextQuestion.getText().toString();
            String editedAnswer = editTextAnswer.getText().toString();

            // Pass the edited data back to MainActivity
            Intent resultIntent = new Intent();
            resultIntent.putExtra("editedQuestion", editedQuestion);
            resultIntent.putExtra("editedAnswer", editedAnswer);
            resultIntent.putExtra("position", position);
            setResult(RESULT_OK, resultIntent);
            finish(); // Close EditActivity
        });
    }
}
