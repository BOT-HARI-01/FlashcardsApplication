package com.example.flashcards_application;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class FlashcardDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Flashcards.db";

    private static final String TABLE_FLASHCARDS = "flashcards";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_QUESTION = "question";
    private static final String COLUMN_ANSWER = "answer";

    public FlashcardDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_FLASHCARDS_TABLE = "CREATE TABLE " + TABLE_FLASHCARDS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_QUESTION + " TEXT," +
                COLUMN_ANSWER + " TEXT)";
        db.execSQL(SQL_CREATE_FLASHCARDS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FLASHCARDS);
        onCreate(db);
    }

    public long insertFlashcard(String question, String answer) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_QUESTION, question);
        values.put(COLUMN_ANSWER, answer);
        long id = db.insert(TABLE_FLASHCARDS, null, values);
        db.close();
        return id;
    }

    public List<Flashcard> getAllFlashcards() {
        List<Flashcard> flashcards = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_FLASHCARDS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {

                int idIndex = cursor.getColumnIndex(COLUMN_ID);
                int questionIndex = cursor.getColumnIndex(COLUMN_QUESTION);
                int answerIndex = cursor.getColumnIndex(COLUMN_ANSWER);

                if (idIndex == -1 || questionIndex == -1 || answerIndex == -1) {
                    throw new IllegalArgumentException("Column not found");
                }

                long id = cursor.getLong(idIndex);
                String question = cursor.getString(questionIndex);
                String answer = cursor.getString(answerIndex);
                flashcards.add(new Flashcard(id, question, answer));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return flashcards;
    }

    public boolean isDatabaseEmpty() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_FLASHCARDS, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int count = cursor.getInt(0);
            cursor.close();
            return count == 0;
        }
        return true;
    }

    public Flashcard getFlashcardById(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_FLASHCARDS, null, COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)}, null, null, null);
        Flashcard flashcard = null;
        if (cursor != null) {
            cursor.moveToFirst();
            int questionIndex = cursor.getColumnIndex(COLUMN_QUESTION);
            int answerIndex = cursor.getColumnIndex(COLUMN_ANSWER);

            if (questionIndex == -1 || answerIndex == -1) {
                throw new IllegalArgumentException("Column not found");
            }

            String question = cursor.getString(questionIndex);
            String answer = cursor.getString(answerIndex);
            flashcard = new Flashcard(id, question, answer);
            cursor.close();
        }
        db.close();
        return flashcard;
    }

    public void updateFlashcard(long id, String question, String answer) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_QUESTION, question);
        values.put(COLUMN_ANSWER, answer);
        db.update(TABLE_FLASHCARDS, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }

    public void deleteFlashcard(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FLASHCARDS, COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }
}
