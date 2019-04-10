package com.example.keerthana.myapplication;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.GetChars;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDate;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity
{
    private int Gallery_intent=2;
    private NotificationManagerCompat notificationManager;
    private DatePickerDialog.OnDateSetListener pDateSetListener;
    public EditText p_name;
    private Button p_save;
    static String name;
    String pdate;
    private TextView p_date;
    private Button notif;
    private Button p_retrieve;
    private TextView t;
    private Calendar c;
    product p;
    static int i;
    private String TAG="date";
    Spinner spinner;
    String option1[]={"1","2","3","4","5","6"};
    ArrayAdapter<String> adapter1;
    ArrayAdapter<String> adapter2;
    int op,op2;
    int eday;
    int emonth;
    int eyear;
    Calendar cal;
    //private StorageReference mStorageRef;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        p_name = findViewById(R.id.name);
        p_save = findViewById(R.id.save);
        p_date=findViewById(R.id.date);
        p=new product();
        spinner=findViewById(R.id.spinner1);
        p_retrieve=findViewById(R.id.retrieve);
        adapter1=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,option1);
        spinner.setAdapter(adapter1);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
             @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                switch(i)
                {
                    case 0:op=1;
                            break;
                    case 1:op=2;
                            break;
                    case 2:op=3;
                        break;
                    case 3:op=4;
                        break;
                    case 4:op=5;
                        break;
                    case 5:op=6;
                        break;
                    case 6:op=7;
                        break;

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        }
        );


        p_retrieve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MainActivity.this,retrieval.class);
                startActivity(i);
            }
        });


        //CHOOSING DATE OF EXPIRY
        //CALENDER
        p_date.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Calendar cal=Calendar.getInstance();
                int year=cal.get(Calendar.YEAR);
                int month=cal.get(Calendar.MONTH);
                int day=cal.get(Calendar.DATE);

                DatePickerDialog dialog=new DatePickerDialog(
                        MainActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        pDateSetListener,
                        year,month,day
                );
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            }
        });

        //STORING DATE IN STRING AND DISPLAYING
        pDateSetListener=new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                String date=day+"-"+(month+1)+"-"+year;
                eday=day;
                emonth=month;
                eyear=year;
                p_date.setText(date);


            }

        };




        //SAVING DATA TO FIREBASE
        p_save.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                FirebaseDatabase database=FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("product");
                name=p_name.getText().toString();
                pdate=p_date.getText().toString();

                if(name.length()==0)
                    p_name.setError("Required");
                if(pdate.length()==0)
                    p_date.setError("Required");
                if(name.length()!=0 && pdate.length()!=0)
                {
                    p.setName(name);
                    p.setCdate(pdate);
                    p.setOption(op);
                    myRef.child("product" + (++i)).setValue(p);
                    Toast.makeText(MainActivity.this, "Data inserted successfully", Toast.LENGTH_SHORT).show();

                    c=Calendar.getInstance();
                    c.set(Calendar.DATE,eday);
                    //c.set(Calendar.MONTH,emonth);
                    //c.set(Calendar.YEAR,eyear);
/*                    c.set(Calendar.HOUR_OF_DAY,23);
                    c.set(Calendar.MINUTE,32);
                    c.set(Calendar.SECOND,0);
  */                  c.add(Calendar.DATE,-op);

                    //cdat.setText(String.valueOf(c.get(Calendar.DATE)));
                    //cdat2.setText(String.valueOf(cal.get(Calendar.DATE)));
                    //cal=Calendar.getInstance();
                    // if(cal.get(Calendar.DATE)<c.get(Calendar.DATE)) {

                    Intent intent = new Intent(getApplicationContext(), NotificationReceiver.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
                    //Toast.makeText(MainActivity.this, "Alarm set successfully", Toast.LENGTH_SHORT).show();
                    //}



                    Intent i=new Intent(MainActivity.this,retrieval.class);


                    startActivity(i);
                }
            }
        }
        );


        p_retrieve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(MainActivity.this,retrieval.class);
                startActivity(i);
            }
        });

    }

}


