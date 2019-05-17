package com.example.golovin_ikbo_01_17_lab3_ver1;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    EditText name;
    EditText second_name;
    EditText surname;
    Button print_table;
    Button add_new;
    Button add_Ivan;
    static SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DB dbhelp = new DB(MainActivity.this);
        db = dbhelp.getWritableDatabase();
        db.delete("Student",null,null);
        String RandStudents[] = {"Ivanov Petr Sergeevich", "Petrov Sergei  Ivanovich", "Petrov Ivan Sergeevich", "Sergeev Petr Ivanovich", "Sergeev Ivan Petrovich", "Ivanov Sergei Petrovich"};


        NewData(RandStudents);

        //Initialisation
        print_table = findViewById(R.id.printInfo);
        add_new = findViewById(R.id.addLine);
        add_Ivan = findViewById(R.id.ChangeToIvanov);
        name = findViewById(R.id.name_field);
        second_name = findViewById(R.id.secondName);
        surname = findViewById(R.id.fam_field);

        add_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((!(name.getText().toString().equals(""))) && (!(second_name.getText().toString().equals(""))) && (!(surname.getText().toString().equals(""))))
                {
                    String fio;
                    ContentValues contentvalues = new ContentValues();
                    fio = surname.getText().toString() + " " + name.getText().toString() + " " + second_name.getText().toString();
                    contentvalues.put("FIO",fio);
                    Date currentTime = Calendar.getInstance().getTime();
                    String currentDate = DateFormat.getDateInstance().format(currentTime);
                    contentvalues.put("Date", currentDate);
                    db.insert("Student",null,contentvalues);
                    name.setText("");
                    second_name.setText("");
                    surname.setText("");
                }
            }
        });

        add_Ivan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor cursor = db.query("Student",null,null,null,null,null,null);

                int count = cursor.getCount();
                ContentValues cv = new ContentValues();
                String countString = "" + count;
                cv.put("FIO","Ivanov Ivan Ivanovich");
                Date currentTime = Calendar.getInstance().getTime();
                String currentDate = DateFormat.getDateInstance().format(currentTime);
                cv.put("Date", currentDate);
                db.update("Student",cv,"id = ?", new String[] {countString});
                cursor.close();
            }
        });

        print_table.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Table.class);
                startActivity(intent);
            }
        });

    }

    void NewData(String names[])
    {
        ContentValues contentvalues = new ContentValues();
        for (int i=1; i <= 5; i ++)
        {
            Date currentTime = Calendar.getInstance().getTime();
            String currentDate = DateFormat.getDateInstance().format(currentTime);
            contentvalues.put("Date", currentDate);
            contentvalues.put("FIO",names[new Random().nextInt(names.length)]);
            db.insert("Student",null,contentvalues);
        }
    }
}

class DB extends SQLiteOpenHelper
{
    static final int DB_VERSION = 1;
    static final String DB_NAME = "Student";
    static final String Fio = "FIO";
    static final String DATE = "Date";
    public DB(Context context)
    {
        super(context, DB_NAME ,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + DB_NAME + " (id integer primary key, " + Fio + " text, " + DATE + " text);"  );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
