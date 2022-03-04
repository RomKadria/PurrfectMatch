package com.example.purrfectmatch.model;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ModelFirebase {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public ModelFirebase(){
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .build();
        db.setFirestoreSettings(settings);
    }
//    public interface GetAllStudentsListener{
//        void onComplete(List<Student> list);
//    }

//    public void getAllStudents(Long lastUpdateDate, GetAllStudentsListener listener) {
//        db.collection(Student.COLLECTION_NAME)
//                .whereGreaterThanOrEqualTo("updateDate",new Timestamp(lastUpdateDate,0))
//                .get()
//                .addOnCompleteListener(task -> {
//                    List<Student> list = new LinkedList<Student>();
//                    if (task.isSuccessful()){
//                        for (QueryDocumentSnapshot doc : task.getResult()){
//                            Student student = Student.create(doc.getData());
//                            if (student != null){
//                                list.add(student);
//                            }
//                        }
//                    }
//                    listener.onComplete(list);
//                });
//    }

    public void addPet(Pet pet, Model.AddPetListener listener) {
        Map<String, Object> json = pet.toJson();
        db.collection(Pet.COLLECTION_NAME)
                .document(pet.getId())
                .set(json)
                .addOnSuccessListener(unused -> listener.onComplete())
                .addOnFailureListener(e -> listener.onComplete());
    }

    public void addPet(Pet pet) {
        Map<String, Object> json = pet.toJson();
        db.collection(Pet.COLLECTION_NAME)
                .document(pet.getId())
                .set(json);
    }

    public void getPetByEmail(String email, Model.getPetByEmailListener listener) {
        db.collection(Pet.COLLECTION_NAME)
                .document(email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        Pet pet = null;
                        if (task.isSuccessful() & task.getResult()!= null){
                            pet = Pet.create(task.getResult().getData());
                        }
                        listener.onComplete(pet);
                    }
                });
    }
}
