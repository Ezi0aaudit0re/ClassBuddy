package com.example.amannagpal.classbuddyv2;

import android.content.Intent;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.amannagpal.classbuddyv2.database.models.Files;

import java.util.List;

public class FilesAdapter extends RecyclerView.Adapter<FilesAdapter.MyViewHolder> {

    private List<Files> mDataset;
    private Listener listener;

    interface Listener{
        abstract int setFileID(int id);
    }




    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView nameTextView, lengthTextView, lectureTextView;
        public CardView cardView;

        public MyViewHolder(View v) {
            super(v);
            nameTextView = (TextView)v.findViewById(R.id.name_text_view);
            lengthTextView = (TextView)v.findViewById(R.id.len_text_view);
            lectureTextView = (TextView)v.findViewById(R.id.lecture_text_view);
            cardView = v.findViewById(R.id.files_card_view);



        }





    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public FilesAdapter(List<Files> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public FilesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.file_card_view, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.nameTextView.setText(mDataset.get(position).getName());
        holder.lengthTextView.setText(mDataset.get(position).getLecture());
        holder.lectureTextView.setText(mDataset.get(position).getLecture());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), TranscriptActivity.class);
                intent.putExtra("FileID", mDataset.get(position).getFileID());
                v.getContext().startActivity(intent);
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }


}
