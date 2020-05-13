package com.example.bazy_danych_dobrze;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Activity01 extends AppCompatActivity {

    public  static String[] comRange;
    public  static String[] comRange1;
    String currentItemName=null;
    public static String opis_opcja=null;
    Integer currentItemQuantity=null;

    TextView stateTV=null;
    EditText changeET=null;
    EditText opis=null;
    TextView alert=null;
    int i=0;
    String przed=null;
    Integer pozycja=null;
    TextView stateTextView;
    String tab[]=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_01);

        stateTV=(TextView)findViewById(R.id.stateTextView);
        changeET=(EditText)findViewById(R.id.editText);
        stateTextView=(TextView)findViewById(R.id.stateTextView);
        stateTextView.setTextColor(Color.WHITE);
        comRange=getResources().getStringArray(R.array.Asortyment);

        //tu zmieniam:!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        comRange1=getResources().getStringArray(R.array.Description);
        opis=(EditText)findViewById(R.id.description);
        alert=(TextView)findViewById(R.id.infoTextView);
        final Button setButton = (Button) findViewById(R.id.setButton);
        final SQLiteOpenHelper DBHelper=new MarketDatabaseHelper(this);
        ArrayAdapter<CharSequence>adapter=ArrayAdapter.createFromResource(
                this,R.array.Asortyment,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final Spinner spinner=(Spinner)findViewById(R.id.Spinner);
        spinner.setAdapter(adapter);







        spinner.setOnItemSelectedListener(

                new AdapterView.OnItemSelectedListener()
                {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                    {
                        pozycja=position;
                        currentItemName=comRange[position];
                        //tu zmieniam: !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                        opis_opcja=comRange1[position];
                        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                        try
                        {

                            SQLiteDatabase DB=DBHelper.getReadableDatabase();
                            Cursor cursor=DB.query(
                                    "STAND",
                                    new String[]{"QUANTITY"},
                                    "Name=?",
                                    new String[]{currentItemName},
                                    null, null, null);

                            Cursor cursor1=null;
                            if(MarketDatabaseHelper.DBVER!=1)
                            {
                                  cursor1 = DB.query(
                                        "STAND",
                                        new String[]{"DESCRIPTION"},
                                        "Name=?",
                                        new String[]{currentItemName},
                                        null, null, null);
                            }


                            cursor.moveToFirst();

                            currentItemQuantity = cursor.getInt(0);
                            cursor.close();

                            if(DB.getVersion()!=1){
                                cursor1.moveToFirst();
                                opis.setText(cursor1.getString(0));
                                cursor1.close();

                            }

                            DB.close();
                        }
                        catch (SQLiteException e)
                        {
                            Toast.makeText(Activity01.this,"EXCEPTION: SPINNER",Toast.LENGTH_SHORT).show();

                        }
                        stateTV.setText("Stan magazynu dla "+currentItemName+": "+currentItemQuantity);



                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                }
        );

        SQLiteDatabase DB=DBHelper.getReadableDatabase();
        if(DB.getVersion()!=1) {
            opis.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {


                    spinner.setBackgroundColor(Color.WHITE);
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    String nowy = s.toString();
                    try {
                        SQLiteDatabase DB = DBHelper.getWritableDatabase();

                        spinner.setBackgroundColor(Color.CYAN);
                        opis.setTextColor(Color.MAGENTA);
                        alert.setTextColor(Color.WHITE);


                        DB.execSQL("UPDATE " + "STAND" + " SET DESCRIPTION = " + "'" + nowy + "' " + "WHERE _id = " + "'" + (pozycja + 1) + "'");//!!!!!!!!!

                    } catch (SQLiteException e) {
                        Toast.makeText(Activity01.this, "EXCEPTION: text watcher", Toast.LENGTH_SHORT).show();
                        opis.setTextColor(Color.GREEN);

                        spinner.setBackgroundColor(Color.YELLOW);
                    }
                }
            });
        }





                //Skladaj

            setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Integer newItemQuantity;
                Integer changeItemQuantity;
                if(changeET.getText().toString().equals(""))
                {
                    changeItemQuantity=0;
                    newItemQuantity=currentItemQuantity;

                }
                else
                {
                     changeItemQuantity = Integer.parseInt(changeET.getText().toString());
                     newItemQuantity = currentItemQuantity + changeItemQuantity;
                }
                try
                {
                    SQLiteDatabase DB=DBHelper.getWritableDatabase();
                    ContentValues itemValues=new ContentValues();
                    itemValues.put("QUANTITY",newItemQuantity.toString());

                    DB.update("STAND",
                            itemValues,
                            "NAME=?",
                            new String[]{currentItemName});


                    DB.close();
                }
                catch (SQLiteException e)
                {
                    Toast.makeText(Activity01.this,"EXCEPTION:SET",Toast.LENGTH_SHORT).show();
                }

                stateTV.setText("Stan magazynu dla "+currentItemName+" : "+newItemQuantity);
                changeET.setText("");
                currentItemQuantity=newItemQuantity;
            }
        });
        //przycisk wydaj
        Button getButton=(Button)findViewById(R.id.getButton);
        getButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Integer newItemQuantity;
                        Integer changeItemQuantity;
                        if(changeET.getText().toString().equals(""))
                        {
                            changeItemQuantity=0;
                            newItemQuantity=currentItemQuantity;

                        }
                        else
                        {
                            changeItemQuantity=Integer.parseInt(changeET.getText().toString());
                            newItemQuantity=currentItemQuantity-changeItemQuantity;
                        }


                        try
                        {
                            SQLiteDatabase DB=DBHelper.getWritableDatabase();
                            ContentValues itemValues=new ContentValues();
                            itemValues.put("QUANTITY",newItemQuantity.toString());
                            DB.update("STAND",
                                    itemValues,
                                    "NAME=?",
                                    new String[]{currentItemName});
                            DB.close();
                        }
                        catch (SQLiteException e)
                        {
                            Toast.makeText(Activity01.this,"EXCEPTION: GET",Toast.LENGTH_SHORT).show();
                        }
                        stateTV.setText("Stan magazynu dla "+currentItemName+" :"+newItemQuantity);
                        changeET.setText("");
                        currentItemQuantity=newItemQuantity;

                    }

                }


        );


    }
}
