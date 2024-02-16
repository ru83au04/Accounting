package com.example.newproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

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

        databaseHelper = DatabaseHelper.instanceOfDatabase(this, expenseManager);;

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, CreateFragment.newInstance())
                .commit();

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if(id == R.id.create_page){
                replaceFragment(new CreateFragment());
            } else if( id == R.id.list_page){
                replaceFragment(new ListFragment());
            } else if(id == R.id.calculate_page){
                replaceFragment(new CalculateFragment());
            }
            return true;
        });
    }

    public DatabaseHelper getDatabaseHelper(){
        return databaseHelper;
    }

//    private DatabaseHelper loadFromDBToMemory() {
//        DatabaseHelper databaseHelper =
//        databaseHelper.populateExpenseListArray();
//        return databaseHelper;
//    }

    public void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    public ExpenseManager getExpenseManager() {
        return expenseManager;
    }
}