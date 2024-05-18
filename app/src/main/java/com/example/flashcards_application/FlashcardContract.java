package com.example.flashcards_application;

import android.provider.BaseColumns;

public final class FlashcardContract {
    private FlashcardContract() {} // To prevent instantiation

    public static class FlashcardEntry implements BaseColumns {
        public static final String TABLE_NAME = "flashcards";
        public static final String COLUMN_QUESTION = "question";
        public static final String COLUMN_ANSWER = "answer";
    }
}
