package com.example.newproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.Toast;

import com.example.newproject.database.DatabaseHelper;
import com.example.newproject.databinding.ActivityMainBinding;
import com.example.newproject.helper.ExpenseManager;


public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    DatabaseHelper databaseHelper;
    ExpenseManager expenseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new CreateFragment());

        expenseManager = new ExpenseManager(this);

        databaseHelper = DatabaseHelper.instanceOfDatabase(this);

        replaceFragment(new CreateFragment());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            Fragment fragment = getSupportFragmentManager().findFragmentById(id);
            if(fragment == null){
                if(id == R.id.create_page){
                    replaceFragment(new CreateFragment());
                } else if(id == R.id.list_page){
                    replaceFragment(new ListFragment());
                } else if(id == R.id.calculate_page){
                    Toast.makeText(this, "施工中...", Toast.LENGTH_SHORT).show();
//                replaceFragment(new CalculateFragment());
                }
            }
            else{
                replaceFragment(fragment);
            }
            return true;
        });
    }

    public DatabaseHelper getDatabaseHelper(){
        return databaseHelper;
    }

    public void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public ExpenseManager getExpenseManager() {
        return expenseManager;
    }
}