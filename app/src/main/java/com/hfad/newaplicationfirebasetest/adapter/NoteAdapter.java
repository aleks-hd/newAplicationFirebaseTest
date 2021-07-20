package com.hfad.newaplicationfirebasetest.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hfad.newaplicationfirebasetest.R;
import com.hfad.newaplicationfirebasetest.data.Note;

import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    ArrayList<Note> arrayList;


    private OnItemClickListener itemClickListener;
    public NoteAdapter(ArrayList<Note> arrayList) {
        this.arrayList = arrayList;

    }

    @NonNull
    @Override
    public NoteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteAdapter.ViewHolder holder, int position) {
        if (arrayList.size()!=0){
        holder.setTextq(arrayList.get(position));}
        else {}
    }

    @Override
    public int getItemCount() {
        if (arrayList.size()!=0){
            return arrayList.size();
        }else {return 0;}


    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,description;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.nameNote);
            description = itemView.findViewById(R.id.DescriptionNote);


        }
        public void setTextq(Note note){
            String namee = note.getName();
           name.setText(namee);
           String desc = note.getDescription();
           description.setText(desc);
        }
    }

    //на будущее
    public void SetOnClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
