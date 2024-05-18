package com.example.flashcards_application;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import android.app.AlertDialog;

public class MainActivity extends AppCompatActivity implements FlashcardAdapter.OnItemClickListener {
    private List<Flashcard> flashcards;
    private FlashcardAdapter adapter;
    private FlashcardDbHelper dbHelper;
    protected int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new FlashcardDbHelper(this);

        // Check if the database is empty and insert sample flashcards if necessary
        if (dbHelper.isDatabaseEmpty()) {
            dbHelper.insertFlashcard("What is the capital of India?", "New Delhi");
            dbHelper.insertFlashcard("What is the capital of USA?", "Washington D.C.");
            dbHelper.insertFlashcard("What is the capital of Japan?", "Tokyo");
        }

        loadFlashcards();

        // Set up RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerviewflashcards);
        adapter = new FlashcardAdapter(flashcards);
        adapter.setOnItemClickListener(this); // Listener for long click event to delete flashcard
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Setting up Add Flashcard button click listener to add a new flashcard
        Button addFlashcardButton = findViewById(R.id.addFlashcardButton);
        addFlashcardButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View v) {
                long newFlashcardId = dbHelper.insertFlashcard("New Question", "New Answer");   // Add a new flashcard to the list
                Flashcard newFlashcard = new Flashcard(newFlashcardId, "New Question", "New Answer");
                flashcards.add(newFlashcard);
                adapter.notifyDataSetChanged();    // Notify the adapter that the data set has changed

                int newPosition = flashcards.size() - 1; // Index of the newly added flashcard
                onItemClick(newPosition); // Trigger item click for the new flashcard
            }
        });
    }

    private void loadFlashcards() {
        flashcards = dbHelper.getAllFlashcards();
    }

    @Override
    public void onItemClick(int position) {
        // Handling item click for opening of the editing interface at initial creation of the flashcard
        Flashcard flashcard = flashcards.get(position);
        Intent intent = new Intent(MainActivity.this, activity_edit.class);
        intent.putExtra("question", flashcard.getQuestion());
        intent.putExtra("answer", flashcard.getAnswer());
        startActivity(intent);
    }

    // Flashcard deletion in the MainActivity
    @Override
    public void onDeleteClick(int position) {
        // Handling item click for deleting the flashcard
        // Show a confirmation dialog before deleting
        new AlertDialog.Builder(this)
                .setTitle("Delete Flashcard")
                .setMessage("Are you sure you want to delete this flashcard?")
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    // Continue with deletion
                    Flashcard flashcard = flashcards.get(position);
                    dbHelper.deleteFlashcard(flashcard.getId());
                    flashcards.remove(position);
                    adapter.notifyItemRemoved(position);
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            String editedQuestion = data.getStringExtra("editedQuestion");
            String editedAnswer = data.getStringExtra("editedAnswer");
            position = data.getIntExtra("position", -1);

            if (position != -1 && editedQuestion != null && editedAnswer != null) {
                Flashcard flashcard = flashcards.get(position);
                flashcards.get(position).setQuestion(editedQuestion);
                flashcards.get(position).setAnswer(editedAnswer);
                dbHelper.updateFlashcard(flashcard.getId(), editedQuestion, editedAnswer);
                adapter.notifyItemChanged(position);
            }
        }
    }
}
