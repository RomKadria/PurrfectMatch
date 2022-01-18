package com.example.purrfectmatch.model;

import android.graphics.Bitmap;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
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

    FirebaseStorage storage = FirebaseStorage.getInstance();

    public void saveImage(Bitmap imageBitmap, String imageName, Model.SaveImageListener listener) {
        StorageReference storageRef = storage.getReference();
        StorageReference imgRef = storageRef.child("pet_images/" + imageName);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imgRef.putBytes(data);
        uploadTask.addOnFailureListener(exception -> listener.onComplete(null))
                .addOnSuccessListener(taskSnapshot ->
                        imgRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    Uri downloadUrl = uri;
                    listener.onComplete(downloadUrl.toString());
                }));
    }

    public void saveImage(Bitmap imageBitmap, String imageName) {
        StorageReference storageRef = storage.getReference();
        StorageReference imgRef = storageRef.child("pet_images/" + imageName);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = imgRef.putBytes(data);
    }

//    public void getStudentById(String studentId, Model.GetStudentById listener) {
//        db.collection(Student.COLLECTION_NAME)
//                .document(studentId)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                        Student student = null;
//                        if (task.isSuccessful() & task.getResult()!= null){
//                            student = Student.create(task.getResult().getData());
//                        }
//                        listener.onComplete(student);
//                    }
//                });
//    }
}
