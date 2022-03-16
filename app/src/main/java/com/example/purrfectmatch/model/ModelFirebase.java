package com.example.purrfectmatch.model;

import android.graphics.Bitmap;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ModelFirebase {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public ModelFirebase() {
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .build();
        db.setFirestoreSettings(settings);
    }

    public interface GetAllPetsListener {
        void onComplete(List<Pet> list);
    }

    public interface GetAllChatsListener {
        void onComplete(List<ChatMessage> list);
    }

    public void getAllPets(Long lastUpdateDate, GetAllPetsListener listener) {
        db.collection(Pet.COLLECTION_NAME)
                .whereGreaterThanOrEqualTo("updateDate", new Timestamp(lastUpdateDate, 0))
                .get()
                .addOnCompleteListener(task -> {
                    List<Pet> list = new LinkedList<Pet>();
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            Pet pet = Pet.create(doc.getData());
                            if (pet != null) {
                                list.add(pet);
                            }
                        }
                    }
                    listener.onComplete(list);
                });
    }

    public void addPet(Pet pet, Model.AddPetListener listener) {
        Map<String, Object> json = pet.toJson();
        db.collection(Pet.COLLECTION_NAME)
                .document(pet.getEmail())
                .set(json)
                .addOnSuccessListener(unused -> listener.onComplete())
                .addOnFailureListener(e -> listener.onComplete());
    }

    public void addPet(Pet pet) {
        Map<String, Object> json = pet.toJson();
        db.collection(Pet.COLLECTION_NAME)
                .document(pet.getEmail())
                .set(json);
    }

    public void addChatMessage(ChatMessage chatMessage, ChatMessagesModel.AddChatMessageListener listener) {
        Map<String, Object> json = chatMessage.toJson();
        db.collection(ChatMessage.COLLECTION_NAME)
                .document()
                .set(json)
                .addOnSuccessListener(unused -> listener.onComplete())
                .addOnFailureListener(e -> listener.onComplete());
    }

    public void addChatMessage(ChatMessage chatMessage) {
        Map<String, Object> json = chatMessage.toJson();
        db.collection(ChatMessage.COLLECTION_NAME)
                .document()
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

    public void getPetById(String petId, Model.GetPetById listener) {
        db.collection(Pet.COLLECTION_NAME)
                .document(petId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        Pet pet = null;
                        if (task.isSuccessful() & task.getResult() != null) {
                            pet = Pet.create(task.getResult().getData());
                        }
                        listener.onComplete(pet);
                    }
                });
    }

    public void getAllChats(Long lastUpdateDate, String petId, GetAllChatsListener listener) {
        db.collection(ChatMessage.COLLECTION_NAME)
                .whereGreaterThanOrEqualTo("updateDate", new Timestamp(lastUpdateDate, 0))
                .whereEqualTo("sendingId", petId)
                .get()
                .addOnCompleteListener(task -> {
                    List<ChatMessage> list = new LinkedList<ChatMessage>();
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            ChatMessage message = ChatMessage.create(doc.getData());
                            if (message != null) {
                                list.add(message);
                            }
                        }
                    }
                    listener.onComplete(list);
                });
    }

    public void getAllChatMessages(Long lastUpdateDate, String sendingId, String receivingId, GetAllChatsListener listener) {
        db.collection(ChatMessage.COLLECTION_NAME)
                .whereGreaterThanOrEqualTo("messageTime", new Timestamp(lastUpdateDate, 0))
                .whereIn("sendingId", Arrays.asList(sendingId, receivingId))
                .get()
                .addOnCompleteListener(task -> {
                    List<ChatMessage> list = new LinkedList<ChatMessage>();
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            ChatMessage message = ChatMessage.create(doc.getData());

                            if (message != null && (message.receivingId.equals(receivingId) || message.receivingId.equals(sendingId))) {
                                list.add(message);
                            }
                        }
                    }
                    listener.onComplete(list);
                });
    }
}
