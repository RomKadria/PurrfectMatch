package com.example.purrfectmatch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavHost;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.example.purrfectmatch.model.Model;
import com.example.purrfectmatch.model.Pet;

public class MainActivity extends AppCompatActivity {
    NavController navCtl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        NavHost navHost = (NavHost)getSupportFragmentManager().findFragmentById(R.id.nav_graph);
//        navCtl = navHost.getNavController();
//        NavigationUI.setupActionBarWithNavController(this,navCtl);

//        Pet pet = new Pet("blazing","123");
//        Model.instance.addPet(pet);

    }
}