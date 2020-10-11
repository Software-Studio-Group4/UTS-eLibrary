package com.example.utselibrary;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.utselibrary.Model.Documents;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Picasso;

public class DocumentAdapter extends FirestoreRecyclerAdapter<Documents, DocumentAdapter.DocumentsHolder> {



    public DocumentAdapter(@NonNull FirestoreRecyclerOptions<Documents> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull DocumentsHolder holder, int position, @NonNull Documents model) {
        holder.title.setText(model.getTitle());
        holder.author.setText(model.getAuthor());
        Picasso.get().load(model.getCoverImageUrl()).into(holder.bookCover);
    }

    @NonNull
    @Override
    public DocumentsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_list_layout, parent, false);
        return new DocumentsHolder(v);
    }

    class DocumentsHolder extends RecyclerView.ViewHolder{
        TextView title, author;
        ImageView bookCover;
       public DocumentsHolder(@NonNull View itemView) {
           super(itemView);
           title = itemView.findViewById(R.id.bookTitleText);
           author = itemView.findViewById(R.id.authorNameText);
           bookCover = itemView.findViewById(R.id.coverImage);
       }
   }
}
