package com.example.medisant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sharedPreferences = this.getSharedPreferences("token", MODE_PRIVATE);
        if (!sharedPreferences.contains("token")) {
            Intent intent = new Intent(this.getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }
        Toolbar toolbar = findViewById(R.id.myToolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.menu_carrito:
                        Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment).navigate(R.id.cartFragment);
                        break;
                }
                return false;
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            NavController navController = Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment);
            switch (item.getItemId()) {
                case R.id.menu_products:
                    navController.navigate(R.id.productsFragment);
                    return true;
                case R.id.menu_orders:
                    navController.navigate(R.id.ordersFragment);
                    return true;
                case R.id.menu_profile:
                    navController.navigate(R.id.profileFragment);
                    return true;
            }
            return false;
        });

    }
}