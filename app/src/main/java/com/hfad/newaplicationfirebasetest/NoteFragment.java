package com.hfad.newaplicationfirebasetest;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
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
    String NOTE_COLLECTION = "1NewCOllections";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference collectionReference = db.collection(NOTE_COLLECTION);
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
        readdb();
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
                deleteforAllert(position);

                readdb();
            }
        });
    }

    private void deleteforAllert(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.titleAllert)
                .setMessage(R.string.messageAllert)
                .setIcon(R.mipmap.ic_launcher_round)
                .setCancelable(false)
                .setPositiveButton(R.string.button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        collectionReference.document(arrayList.get(position).getId()).delete();
                    }
                })
                .setNegativeButton(R.string.buttonoff, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }


    //инициализация кнопок
    private void initEnterInfo(View view) {
        name = view.findViewById(R.id.editName);
        description = view.findViewById(R.id.editDescription);
        clearBtn = view.findViewById(R.id.deleteNote);
        addBtn = view.findViewById(R.id.addNote);
        //saveBtn = view.findViewById(R.id.saveNote);
    }

    //инициализация слушателя
    private void initInputData(View view) {
        arrayList = new ArrayList<>();
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("add Note", arrayList.size() + "");
                //arrayList.clear();
                Log.d("add Note", arrayList.size() + "");
                initclickadd();

            }
        });
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteDB();
            }
        });
    }

    private void deleteDB() {
        for (Note note : arrayList){
            collectionReference.document(note.getId()).delete();
        }

        readdb();
    }

    void initclickadd() {

        nameInput = (String) name.getText().toString();
        descriptionInput = (String) description.getText().toString();
        Note note = new Note();
        note.setName(nameInput);
        note.setDescription(descriptionInput);
        arrayList.add(note);
        readdb();
        initDB(note);

    }

    void initDB(Note note) {
        collectionReference
                .add(NoteDataMapping.toDocument(note))
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

        collectionReference.orderBy(NoteDataMapping.Fields.NAME, Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                           arrayList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> documentMap = document.getData();
                                note = NoteDataMapping.toNote(document.getId(), documentMap);
                                arrayList.add(note);
                                String name = note.getName();

                                Log.d("OUTPUT", name + "");

                                Log.d("ответ от БД", document.getId() + " => " + document.getData());
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Log.w("ошибка", "Error getting documents.", task.getException());
                        }
                    }
                });
    }


}