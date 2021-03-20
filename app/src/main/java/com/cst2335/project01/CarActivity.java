package com.cst2335.project01;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class CarActivity extends AppCompatActivity {

    MyListAdapter myAdapter;
    SQLiteDatabase db;
    EditText eText;
    ListView listView;
    Button btnTrivia;
    Button btnSongster;
    Button btnCardata;
    Button btnSoccer;

    private ArrayList<Message> elements = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_database);


        myAdapter = new MyListAdapter();
        ListView myList = (ListView) findViewById(R.id.ListView);
        myList.setAdapter(myAdapter = new MyListAdapter());
        eText = findViewById(R.id.eText);

        listView = findViewById(R.id.listView);
        btnCardata = findViewById(R.id.btnCardata);

        btnCardata.setOnClickListener( bt->{
//            startActivity(goToCar);
        });

        protected void showContact(int position)
        {
            Message selectedContact = elements.get(position);
            View extraStuff = getLayoutInflater().inflate(R.layout.empyty, null);
            //get the TextViews
            EditText rowMsg = extraStuff.findViewById(R.id.eText);
//        EditText rowSend = extraStuff.findViewById(R.id.sBtn);
            TextView rowId = extraStuff.findViewById(R.id.row_id);

            //set the fields for the alert dialog
//eText.setText(selectedContact.getMessage());
//        rowSend.setText(selectedContact.getIsSend());
            rowMsg.setText(selectedContact.getMessage());
            rowId.setText("id:" + selectedContact.getId());

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("You clicked on item #" + position)

                    //What is the message:
                    .setMessage("The selected row is: " + (position + 1) +
                            "\n" +
                            "You can update the fields and then click update to save in the database")
                    // add extra layout elements:showing the contact information
                    .setView(extraStuff)
                    //what the update button does:
                    .setPositiveButton("Update", (click, arg) -> {
//                        elements.add("HELLO");
                        selectedContact.update(rowMsg.getText().toString());
                        updateContact(selectedContact);
                        myAdapter.notifyDataSetChanged();
                    })
                    //What the delete button does:
                    .setNegativeButton("Delete", (click, arg) -> {
                        deleteContact(selectedContact); //remove the contact from database
                        elements.remove(position); //remove the contact from contact list
                        myAdapter.notifyDataSetChanged(); //there is one less item so update the list

                    })

//                    An optional third button:
                    .setNeutralButton("Dissmiss", (click, arg) -> {  })

                    //Show the dialog
                    .create().show();
        }
    }
}