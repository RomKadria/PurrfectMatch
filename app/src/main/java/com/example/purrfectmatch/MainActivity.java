package com.example.purrfectmatch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavHost;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.example.purrfectmatch.model.SaveSharedPreference;

public class MainActivity extends AppCompatActivity {
    NavController navCtl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavHost navHost = (NavHost) getSupportFragmentManager().findFragmentById(R.id.base_navhost);
        navCtl = navHost.getNavController();

        NavGraph navGraph = navCtl.getNavInflater().inflate(R.navigation.nav_graph);

        if (SaveSharedPreference.getLogin(this.getApplicationContext())) {
            navGraph.setStartDestination(R.id.petListRvFragment);
        } else {
            navGraph.setStartDestination(R.id.loginFragment);
        }

        navCtl.setGraph(navGraph);

        NavigationUI.setupActionBarWithNavController(this, navCtl);
    }
}