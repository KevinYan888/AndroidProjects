package com.cst2335.project01;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class TriviaHighScoreLeaderboard extends AppCompatActivity {
    public static final String ITEM_NAME = "NAME";
    public static final String ITEM_SCORE = "SCORE";
    public static final String ITEM_DIFFICULTY = "DIFFICULTY";
    public static final String ITEM_ID = "ID";

    ArrayList<TriviaHighScoreRecord> listHighScore = new ArrayList<>();
//    private static int ACTIVITY_VIEW_CONTACT = 33;
//    int positionClicked = 0;
    private MyOwnAdapter myAdapter;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trivia_high_score_leaderboard);



        //Get the fields from the screen:
        TextView textHighScoreLeaderBoard = findViewById(R.id.textHighScoreLeaderBoard);
        Button btnContinuePlay = (Button)findViewById(R.id.btnContinuePlay);
        Button btnExit = (Button)findViewById(R.id.btnExit);
        ListView theList = (ListView)findViewById(R.id.listHighScore);

      //  loadDataFromDatabase(); //get any previously saved Contact objects
        //get a database connection:
        TriviaMyOpener dbOpener = new TriviaMyOpener(this);
        db = dbOpener.getWritableDatabase(); //This calls onCreate() if you've never built the table before, or onUpgrade if the version here is newer
        //create an adapter object and send it to the listVIew
        theList.setAdapter(myAdapter = new MyOwnAdapter());


        //This listens for items being clicked in the list view
        theList.setOnItemClickListener(( parent,  view,  position,  id) -> {
          //  showContact( position );

        });


        //Listen for an insert button click event:

            //get the email and name that were typed
//            String name = "hongwen";
//            double score = 98.99;
//            String difficulty = "Easy";

            Intent intent = getIntent();
            //get info from loadQuestions page: namePlayer,strDifficultyOfQuestion,scoreOfPlayer
            Bundle bundleFromLoadQuestions = intent.getExtras();

            //add to the database and get the new ID
            ContentValues newRowValues = new ContentValues();

            //Now provide a value for every database column defined in MyOpener.java:
            //put string name in the NAME column:
            newRowValues.put(TriviaMyOpener.COL_NAME, bundleFromLoadQuestions.getString("namePlayer"));
            newRowValues.put(TriviaMyOpener.COL_SCORE, bundleFromLoadQuestions.getDouble("scoreOfPlayer"));
            newRowValues.put(TriviaMyOpener.COL_DIFFICULTY, bundleFromLoadQuestions.getString("strDifficultyOfQuestion"));

            //Now insert in the database:
            long newId = db.insert(TriviaMyOpener.TABLE_NAME, null, newRowValues);

            //now you have the newId, you can create the Contact object
            TriviaHighScoreRecord newRecord = new TriviaHighScoreRecord(
                    bundleFromLoadQuestions.getString("namePlayer"),
                    bundleFromLoadQuestions.getDouble("scoreOfPlayer"),
                    bundleFromLoadQuestions.getString("strDifficultyOfQuestion"),
                    newId);

            //add the new contact to the list:
            listHighScore.add(newRecord);
            //update the listView:
            myAdapter.notifyDataSetChanged();
        loadDataFromDatabase();
    }






    private void loadDataFromDatabase()
    {

        // We want to get all of the columns. Look at MyOpener.java for the definitions:
        String [] columns = {TriviaMyOpener.COL_ID, TriviaMyOpener.COL_NAME, TriviaMyOpener.COL_SCORE,TriviaMyOpener.COL_DIFFICULTY};
        //query all the results from the database:
        Cursor results = db.query(false, TriviaMyOpener.TABLE_NAME, columns, null, null, null, null, null, null);

       // printCursor(results,db.getVersion());
        //Now the results object has rows of results that match the query.
        //find the column indices:
        int idColIndex = results.getColumnIndex(TriviaMyOpener.COL_ID);
        int nameColIndex = results.getColumnIndex(TriviaMyOpener.COL_NAME);
        int scoreColIndex = results.getColumnIndex(TriviaMyOpener.COL_SCORE);
        int difficultyColIndex = results.getColumnIndex(TriviaMyOpener.COL_DIFFICULTY);

        //iterate over the results, return true if there is a next item:
        while(results.moveToNext())
        {
            String name = results.getString(nameColIndex);
            double score = results.getDouble(scoreColIndex);
            String difficulty = results.getString(difficultyColIndex);
            long id = results.getLong(idColIndex);

            //add the new Contact to the array list:
            listHighScore.add(new TriviaHighScoreRecord(name, score, difficulty, id));
        }

        //At this point, the contactsList array has loaded every row from the cursor.
    }

//
//    protected void showContact(int position)
//    {
//        Contact selectedContact = contactsList.get(position);
//
//        View contact_view = getLayoutInflater().inflate(R.layout.contact_edit, null);
//        //get the TextViews
//        EditText rowName = contact_view.findViewById(R.id.row_name);
//        EditText rowEmail = contact_view.findViewById(R.id.row_email);
//        TextView rowId = contact_view.findViewById(R.id.row_id);
//
//        //set the fields for the alert dialog
//        rowName.setText(selectedContact.getName());
//        rowEmail.setText(selectedContact.getEmail());
//        rowId.setText("id:" + selectedContact.getId());
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("You clicked on item #" + position)
//                .setMessage("You can update the fields and then click update to save in the database")
//                .setView(contact_view) //add the 3 edit texts showing the contact information
//                .setPositiveButton("Update", (click, b) -> {
//                    selectedContact.update(rowName.getText().toString(), rowEmail.getText().toString());
//                    updateContact(selectedContact);
//                    myAdapter.notifyDataSetChanged(); //the email and name have changed so rebuild the list
//                })
//                .setNegativeButton("Delete", (click, b) -> {
//                    deleteContact(selectedContact); //remove the contact from database
//                    contactsList.remove(position); //remove the contact from contact list
//                    myAdapter.notifyDataSetChanged(); //there is one less item so update the list
//                })
//                .setNeutralButton("dismiss", (click, b) -> { })
//                .create().show();
//    }
//
//    protected void updateContact(Contact c)
//    {
//        //Create a ContentValues object to represent a database row:
//        ContentValues updatedValues = new ContentValues();
//        updatedValues.put(MyOpener.COL_NAME, c.getName());
//        updatedValues.put(MyOpener.COL_EMAIL, c.getEmail());
//
//        //now call the update function:
//        db.update(MyOpener.TABLE_NAME, updatedValues, MyOpener.COL_ID + "= ?", new String[] {Long.toString(c.getId())});
//    }
//
//    protected void deleteContact(Contact c)
//    {
//        db.delete(MyOpener.TABLE_NAME, MyOpener.COL_ID + "= ?", new String[] {Long.toString(c.getId())});
//    }

    //This class needs 4 functions to work properly:
    protected class MyOwnAdapter extends BaseAdapter
    {
        @Override
        public int getCount() {
            return listHighScore.size();
        }

        public TriviaHighScoreRecord getItem(int position){
            return listHighScore.get(position);
        }

        public View getView(int position, View old, ViewGroup parent)
        {
            View newView = getLayoutInflater().inflate(R.layout.trivia_empty_row, parent, false );

            TriviaHighScoreRecord thisRow = getItem(position);

            //get the TextViews
            TextView rowId = (TextView)newView.findViewById(R.id.id);
            TextView rowName = (TextView)newView.findViewById(R.id.name);
            TextView rowScore = (TextView)newView.findViewById(R.id.score);
            TextView rowDifficulty = (TextView)newView.findViewById(R.id.difficulty);


            //update the text fields:
            rowId.setText("id: " + Long.toString(thisRow.getId()));
            rowName.setText("Player name: "+thisRow.getName());
            rowScore.setText("Score: "+Double.toString(thisRow.getScore()));
            rowDifficulty.setText("Difficulty: "+thisRow.getDifficulty());


            //return the row:
            return newView;
        }

        //last week we returned (long) position. Now we return the object's database id that we get from line 71
        public long getItemId(int position)
        {
            return getItem(position).getId();
        }
    }
}
