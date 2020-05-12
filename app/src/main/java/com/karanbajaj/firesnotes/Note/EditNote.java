package com.karanbajaj.firesnotes.Note;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.karanbajaj.firesnotes.MainActivity;
import com.karanbajaj.firesnotes.R;

import java.util.HashMap;
import java.util.Map;

public class EditNote extends AppCompatActivity {

    Intent data;
    EditText editNoteTitle, editNoteContent;
    Toolbar toolbar;
    FirebaseFirestore fStore;
    ProgressBar progressBarSave;
    FloatingActionButton fab;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        fStore = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        data = getIntent();

        String noteTitle = data.getStringExtra("title");
        String noteContent = data.getStringExtra("content");

        editNoteContent = findViewById(R.id.editNoteContent);
        editNoteTitle = findViewById(R.id.editNoteTitle);
        progressBarSave = findViewById(R.id.progressBar2);
        fab = findViewById(R.id.saveEditedNote);


        editNoteTitle.setText(noteTitle);
        editNoteContent.setText(noteContent);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                String nContent = editNoteContent.getText().toString();
                String nTitle = editNoteTitle.getText().toString();

                if (nTitle.isEmpty() || nContent.isEmpty()){
//                    Toast.makeText(EditNote.this, "can not save with empty field", Toast.LENGTH_SHORT).show();
                    Snackbar.make(view, "can not save with empty field", Snackbar.LENGTH_LONG).show();
//                            .setAction("Action", null).show();
                    return;
                }

                progressBarSave.setVisibility(View.VISIBLE);

                DocumentReference docRef = fStore.collection("notes").document(user.getUid()).collection("myNotes").document(data.getStringExtra("noteId"));

                Map<String, Object> note = new HashMap<>();
                note.put("title", nTitle);
                note.put("content", nContent);

                docRef.update(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
//                        Toast.makeText(EditNote.this, "added", Toast.LENGTH_SHORT).show();
                        Snackbar.make(view, "Update", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditNote.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        progressBarSave.setVisibility(View.VISIBLE);
                    }
                });
            }
        });

    }
}
