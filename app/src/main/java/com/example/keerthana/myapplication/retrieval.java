package com.example.keerthana.myapplication;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class retrieval extends AppCompatActivity {

    private ListView listView;
    //FirebaseDatabase database=FirebaseDatabase.getInstance();

    List<product>productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieval);

        listView=findViewById(R.id.list);
        //FirebaseDatabase database=FirebaseDatabase.getInstance();
        //DatabaseReference myRef = database.getReference("product");

        productList=new ArrayList<>();




    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("product");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot productSnapshot: dataSnapshot.getChildren()){
                    product p=productSnapshot.getValue(product.class);
                    productList.add(p);
                }
                productInfoAdapter InfoAdapter=new productInfoAdapter(retrieval.this,productList);
                listView.setAdapter(InfoAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}