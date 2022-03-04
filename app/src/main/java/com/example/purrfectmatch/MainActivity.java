package com.example.purrfectmatch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavHost;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.Window;

import com.example.purrfectmatch.model.Model;
import com.example.purrfectmatch.model.Pet;

public class MainActivity extends AppCompatActivity {
    NavController navCtl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        NavHost navHost = (NavHost)getSupportFragmentManager().findFragmentById(R.id.base_navhost);
        navCtl = navHost.getNavController();

        NavigationUI.setupActionBarWithNavController(this,navCtl);
    }
}