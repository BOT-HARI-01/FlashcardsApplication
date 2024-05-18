package com.example.flashcards_application;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class FlashcardAdapter extends RecyclerView.Adapter<FlashcardAdapter.FlashcardViewHolder> {
    private  List<Flashcard> flashcards;
    protected OnItemClickListener listener;



    public interface OnItemClickListener {
        void onItemClick(int position);
        void onDeleteClick(int position);
    }

    public FlashcardAdapter(List<Flashcard> flashcards) {
        this.flashcards = flashcards;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public FlashcardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_flashcard, parent, false);
        return new FlashcardViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull FlashcardViewHolder holder, int position) {
        Flashcard currentFlashcard = flashcards.get(position);
        holder.questionTextView.setText(currentFlashcard.getQuestion());
        holder.answerTextView.setText(currentFlashcard.getAnswer());

        //To Handle click event to show or hide answer
        holder.itemView.setOnClickListener(v -> {
            // Toggle visibility of answer
            if (holder.answerTextView.getVisibility() == View.VISIBLE) {
                holder.answerTextView.setVisibility(View.GONE);
            } else {
                holder.answerTextView.setVisibility(View.VISIBLE);
            }


        });
        holder.itemView.setOnLongClickListener(v -> {
            if (listener != null) {
                int pos = holder.getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    listener.onDeleteClick(pos);
                }
            }
            return true;
        });
    }


    @Override
    public int getItemCount() {
        return flashcards.size();
    }

    public static class FlashcardViewHolder extends RecyclerView.ViewHolder {
        public TextView questionTextView;
        public TextView answerTextView;

//        private OnItemClickListener listener;

        public FlashcardViewHolder(View itemView) {
            super(itemView);
            questionTextView = itemView.findViewById(R.id.textViewQuestion);
            answerTextView = itemView.findViewById(R.id.textViewAnswer);
        }
    }
}