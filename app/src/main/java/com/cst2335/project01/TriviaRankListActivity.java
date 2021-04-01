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
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class TriviaRankListActivity extends AppCompatActivity {

    ArrayList<TriviaRankItems> listHighScore = new ArrayList<>();

    MyOwnAdapter myAdapter;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trivia_high_score_leaderboard);

        //Get the fields from the screen:
        TextView textRankNumber = findViewById(R.id.rankNumber);
        TextView textScore = findViewById(R.id.score);
        TextView textName = findViewById(R.id.name);
        TextView textID = findViewById(R.id.id);
        Button btnContinuePlay = findViewById(R.id.btnContinuePlay);
        Button btnBackHome = findViewById(R.id.btnBackHome);
        ListView theList = findViewById(R.id.listHighScore);

        //get a database connection:
        TriviaMyOpener dbOpener = new TriviaMyOpener(this);
        db = dbOpener.getWritableDatabase(); //This calls onCreate() if you've never built the table before, or onUpgrade if the version here is newer


        Intent intent = getIntent();
        Bundle bundleFromAnywhere = intent.getExtras();
        if(!bundleFromAnywhere.containsKey("NO_INSERT")){

            //add to the database and get the new ID
            ContentValues newRowValues = new ContentValues();
            //put string name in the NAME column:
            newRowValues.put(TriviaMyOpener.COL_NAME, bundleFromAnywhere.getString("namePlayer"));
            newRowValues.put(TriviaMyOpener.COL_SCORE, bundleFromAnywhere.getDouble("scoreOfPlayer"));
            newRowValues.put(TriviaMyOpener.COL_DIFFICULTY, bundleFromAnywhere.getString("strDifficultyOfQuestion"));

            //Now insert in the database:
            long newId = db.insert(TriviaMyOpener.TABLE_NAME, null, newRowValues);

//            now you have the newId, you can create the Contact object
//            TriviaHighScoreRecord newRecord = new TriviaHighScoreRecord(
//                    bundleFromAnywhere.getString("namePlayer"),
//                    bundleFromAnywhere.getDouble("scoreOfPlayer"),
//                    bundleFromAnywhere.getString("strDifficultyOfQuestion"),
//                    newId);

//            add the new contact to the list:
//               listHighScore.add(newRecord);
//            update the listView:
           //  myAdapter.notifyDataSetChanged();
        }

        //loadRank
        loadDataFromDatabase();

        //create an adapter object and send it to the listVIew
        theList.setAdapter(myAdapter = new MyOwnAdapter());

        //This listens for items being clicked in the list view
        theList.setOnItemClickListener(( parent,  view,  position,  id) -> {
            showOneItem(position);
        });

      btnContinuePlay.setOnClickListener(c->{
          Intent goToActivity = new Intent(TriviaRankListActivity.this, TriviaSettingActivity.class);
          startActivity(goToActivity);
      });
        btnBackHome.setOnClickListener(k->{
            Intent goToMainActivity = new Intent(TriviaRankListActivity.this, MainActivity.class);
            startActivity(goToMainActivity);
//            android.os.Process.killProcess(android.os.Process.myPid());
//            System.exit(0);

        });
    }



private void deleteOneHighScoreItem(TriviaRankItems oneRecord){

    db.delete(TriviaMyOpener.TABLE_NAME, TriviaMyOpener.COL_ID + "= ?", new String[] {Long.toString(oneRecord.getId())});
}


    private void loadDataFromDatabase()
    {
        // We want to get all of the columns. Look at MyOpener.java for the definitions:
        String [] columns = {TriviaMyOpener.COL_ID, TriviaMyOpener.COL_NAME, TriviaMyOpener.COL_SCORE,TriviaMyOpener.COL_DIFFICULTY};
        //query all the results from the database:
        Cursor results = db.query(false, TriviaMyOpener.TABLE_NAME, columns, null, null, null, null,TriviaMyOpener.COL_SCORE+" DESC", "10");

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
            listHighScore.add(new TriviaRankItems(name, score, difficulty, id));
        }

        //At this point, the contactsList array has loaded every row from the cursor.
    }

    protected void showOneItem(int position){
        //got the selected object(a high score item)
        TriviaRankItems selectedScoreItem = listHighScore.get(position);

        //show a result item on a dialog
        View dialogResultView = getLayoutInflater().inflate(R.layout.trivia_high_score_one_item, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Your score:  " + Double.toString(selectedScoreItem.getScore()) + "(*%)")
                .setMessage("Do you want delete this record? ")
                .setView(dialogResultView) //add texts showing the contact information
                .setPositiveButton("Cancel ", (click, b) -> {

                })
                .setNegativeButton("Exit", (click, b) -> {
                    android.os.Process.killProcess(android.os.Process.myPid());
                })
                .setNeutralButton("Delete", (click, b) -> {

                    deleteOneHighScoreItem(selectedScoreItem); //remove the content from database
                    listHighScore.remove(position); //remove the content from content list
                    myAdapter.notifyDataSetChanged(); //there is one less item so update the list
                })
                .create().show();
        return;
    }

    //MyOwnAdapter can load all the Ttems to the ListView. This class needs 4 functions to work properly:
    protected class MyOwnAdapter extends BaseAdapter
    {
        @Override
        public int getCount() {
            return listHighScore.size();
        }

        public TriviaRankItems getItem(int position){
            return listHighScore.get(position);
        }

        public View getView(int position, View old, ViewGroup parent)
        {
            View newView = getLayoutInflater().inflate(R.layout.trivia_empty_row, parent, false );

            TriviaRankItems thisRow = getItem(position);

            //get the TextViews
            TextView rowId = (TextView)newView.findViewById(R.id.id);
            TextView rowName = (TextView)newView.findViewById(R.id.name);
            TextView rowScore = (TextView)newView.findViewById(R.id.score);
            TextView rowDifficulty = (TextView)newView.findViewById(R.id.difficulty);
            TextView rankNumber = (TextView)newView.findViewById(R.id.rankNumber);


            //update the text fields:
            rowId.setText(Long.toString(thisRow.getId()));
            rowName.setText(thisRow.getName());
            rowScore.setText(String.format("%.1f", thisRow.getScore()));
            rowDifficulty.setText(thisRow.getDifficulty());
            rankNumber.setText(Integer.toString(position+1));

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
