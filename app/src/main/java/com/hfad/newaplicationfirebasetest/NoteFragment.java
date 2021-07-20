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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.hfad.newaplicationfirebasetest.adapter.NoteAdapter;
import com.hfad.newaplicationfirebasetest.data.Note;
import com.hfad.newaplicationfirebasetest.data.NoteDataMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class NoteFragment extends Fragment {
    private EditText name, description;
    private Button clearBtn, addBtn, saveBtn;
    private String nameInput, descriptionInput;
    ArrayList<Note> arrayList;
    RecyclerView recyclerView;
    NoteAdapter adapter;
    String NOTE_COLLECTION = "notes";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference collectionReference = db.collection(NOTE_COLLECTION);
    Map<String, Note> notesfordb;
    ArrayList<Note> notewithDB;
    Note note;
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
                Log.d("add Note", "Якобы сохранили");
            }
        });

    }

    void initclickadd() {

        nameInput = (String) name.getText().toString();
        descriptionInput = (String) description.getText().toString();
        Note note = new Note();
        notesfordb = new HashMap<>();
        notesfordb.put("object_1", note);
        note.setName(nameInput);
        note.setDescription(descriptionInput);
        arrayList.add(note);
        adapter.notifyDataSetChanged();
        initDB();
        readdb();

    }

    void initDB() {
        collectionReference
                .add(notesfordb)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("Снапшот добавленв с ID", documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("ошибка при добавлении", "Error adding document", e);
                    }
                });

    }

    void readdb() {

        collectionReference
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            notewithDB = new ArrayList<Note>();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> documentMap = document.getData();
                                note = new Note();
                              //  note = (Note) documentMap.values();
                                notewithDB.add(note);

                              //  Log.d("oUTPUT", note.getName()+"");
                                Log.d("ответ от БД", document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.w("ошибка", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

}