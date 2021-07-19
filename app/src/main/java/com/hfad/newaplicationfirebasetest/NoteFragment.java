package com.hfad.newaplicationfirebasetest;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.hfad.newaplicationfirebasetest.adapter.NoteAdapter;
import com.hfad.newaplicationfirebasetest.data.Note;

import java.util.ArrayList;


public class NoteFragment extends Fragment {
    private EditText name, description;
    private Button clearBtn, addBtn, saveBtn;
    private String nameInput, descriptionInput;
    ArrayList<Note> arrayList;
    RecyclerView recyclerView;
    NoteAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewContainer);
        initEnterInfo(view);
        initInputData(view);
        initRecyclerView(recyclerView);


        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
    }

    //инициализация адаптера
    private void initRecyclerView(RecyclerView recyclerView) {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new NoteAdapter(arrayList);
        recyclerView.setAdapter(adapter);
        adapter.SetOnClickListener(new NoteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //initclickadd();
                adapter.notifyItemChanged(position);
                Log.d("click","ftftftf");
            }
        });
    }


    //инициализация кнопок
    private void initEnterInfo(View view) {
        name = view.findViewById(R.id.editName);
        description = view.findViewById(R.id.editDescription);
        clearBtn = view.findViewById(R.id.deleteNote);
        addBtn = view.findViewById(R.id.addNote);
        saveBtn = view.findViewById(R.id.saveNote);
    }

    //инициализация слушателя
    private void initInputData(View view) {
        arrayList = new ArrayList<>();
        saveBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                initclickadd();
                Log.d("add Note", "Якобы сохранили" );
            }
        });

    }
    void initclickadd(){

        nameInput = (String) name.getText().toString();
        descriptionInput = (String) description.getText().toString();
        Note note = new Note();
        note.setNamee(nameInput);
        note.setDescription(descriptionInput);
        arrayList.add(note);
        adapter.notifyDataSetChanged();

    }


}