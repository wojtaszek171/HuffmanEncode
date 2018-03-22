package com.example.pawel.huffman;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    public static HashMap data;
    public static String text;
    public static String encoded;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        if((HashMap<String, HashMap>)intent.getSerializableExtra("data")!=null) {
            HashMap<String, HashMap> hashMap = (HashMap<String, HashMap>) intent.getSerializableExtra("data");
            data = hashMap;
            text = intent.getStringExtra("text");
            encoded = intent.getStringExtra("encoded");
        }
        Bundle bundle = new Bundle();
        if(data!=null){
            bundle.putSerializable("data",data);
            bundle.putString("text",text);
            bundle.putString("encoded",encoded);
        }
        mTextMessage = (TextView) findViewById(R.id.message);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        InputEncodeFragment fragment = new InputEncodeFragment();
        fragment.setArguments(bundle);
        transaction.replace(R.id.content,fragment);
        transaction.commit();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                switch (item.getItemId()){
                    case R.id.navigation_home:
                        InputEncodeFragment fragment = new InputEncodeFragment();
                        fragment.setArguments(bundle);
                        transaction.replace(R.id.content,fragment);
                        transaction.commit();
                        break;
                    case R.id.navigation_dashboard:
                        StaticticsFragment fragment2 = new StaticticsFragment();
                        fragment2.setArguments(bundle);
                        transaction.replace(R.id.content,fragment2);
                        transaction.commit();
                        break;
                    case R.id.navigation_notifications:
                        TreeFragment fragment3 = new TreeFragment();
                        fragment3.setArguments(bundle);
                        transaction.replace(R.id.content,fragment3);
                        transaction.commit();
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
//            case R.id.activity_main_update_menu_item:
//                Toast.makeText(this, "update clicked", Toast.LENGTH_SHORT).show();
//                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

}
