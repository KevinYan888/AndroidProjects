package com.cst2335.project01;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import static android.view.View.VISIBLE;


public class SongActivity extends AppCompatActivity {
    ArrayList<SongEntity> songList = new ArrayList<>( );
    MyListAdapter mySongAdapter;
    //ArrayList<Contact> contactsList = new ArrayList<>();
    private static int ACTIVITY_VIEW_SONGLIST = 33;
    int positionClicked = 0;
    //MyOwnAdapter myAdapter;
    SQLiteDatabase db;
    ProgressBar progressBarSong;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_songstar);

        ImageView songHeadImage= findViewById( R.id.imageViewSong );
        EditText searchView =  findViewById( R.id.editTextSearch );
        ImageButton searchBtn= findViewById(R.id.song_searchButton);
        progressBarSong=findViewById(R.id.progressBarSong);

        searchBtn.setOnClickListener( click ->                {
            String searchterm=searchView.getText().toString();
            // http://www.songsterr.com/a/ra/songs.xml?pattern=XXX
            // http://www.songsterr.com/a/ra/songs.json?pattern=XXX
            String songJsonURL="http://www.songsterr.com/a/ra/songs.json?pattern="+searchterm;
            progressBarSong.setVisibility( VISIBLE );
            MySongHTTPRequest songReq = new MySongHTTPRequest();
            songReq.execute(songJsonURL);  //Type 1

                 /*
                    ContentValues newRowValues = new ContentValues();
                    //put string name in the NAME column:
                    newRowValues.put(SongOpener.COL_NAME, name);
                    newRowValues.put(SongOpener.COL_NAME, name);


                    //put string email in the EMAIL column:
                    newRowValues.put(SongOpener.COL_EMAIL, email);

                    //Now insert in the database:
                    long newId = db.insert(SongOpener.TABLE_NAME, null, newRowValues);

                    //now you have the newId, you can create the Contact object
                    Contact newContact = new Contact(name, email, newId);

                    //add the new contact to the list:
                    contactsList.add(newContact);
                    //update the listView:
                    myAdapter.notifyDataSetChanged();

                    //clear the EditText fields:
                    nameEdit.setText("");
                    emailEdit.setText("");

                    //Show the id of the inserted item:
                    Toast.makeText(this, "Inserted item id:"+newId, Toast.LENGTH_LONG).show();
                 */
                });


        ListView songListV = findViewById(R.id.listViewSong);
      //  loadDataFromDatabase(); //get any previously saved songlist objects

        songListV.setAdapter( mySongAdapter = new MyListAdapter());
        songListV.setOnItemClickListener( (parent, view, position, id) -> {
          showSong (position);
        }   );



    }
/*
    private void loadDataFromDatabase()
    {
        //get a database connection:
        SongOpener dbOpener = new SongOpener( this);
        db = dbOpener.getWritableDatabase(); //This calls onCreate() if you've never built the table before, or onUpgrade if the version here is newer


        // We want to get all of the columns. Look at MyOpener.java for the definitions:
       //String [] columns = {MyOpener.COL_ID, MyOpener.COL_EMAIL, MyOpener.COL_NAME};
        String [] columns = {SongOpener.COL_ID, SongOpener.COL_SONGTITLE, SongOpener.COL_SONGID,
                SongOpener.COL_ARTISTNAME, SongOpener.COL_ARTISTID };

        //query all the results from the database:
        Cursor results = db.query(false, SongOpener.TABLE_NAME, columns, null, null, null, null, null, null);

        //Now the results object has rows of results that match the query.
        //find the column indices:
        int songTitleColumnIndex = results.getColumnIndex(SongOpener.COL_SONGTITLE);
        int songIdColIndex = results.getColumnIndex(SongOpener.COL_SONGID);
        int artistNameColumnIndex = results.getColumnIndex(SongOpener.COL_ARTISTNAME);
        int artistIdColIndex = results.getColumnIndex(SongOpener.COL_ARTISTID);
        int idColIndex = results.getColumnIndex(SongOpener.COL_ID);

        //iterate over the results, return true if there is a next item:
        while(results.moveToNext())
        {
            String songTitle = results.getString(songTitleColumnIndex );
            int songId = results.getInt(songIdColIndex);
            String artistName = results.getString(artistNameColumnIndex);
            int artistId = results.getInt(artistIdColIndex );
            long id = results.getLong(idColIndex);

            //add the new Contact to the array list:
           // contactsList.add(new Contact(name, email, id));
            songList.add(new SongEntity( songTitle, songId, artistName, artistId, id));

        }

        //At this point, the contactsList array has loaded every row from the cursor.
    }
*/

    protected void showSong(int position)
    {   /*
        Contact selectedContact = contactsList.get(position);

        View contact_view = getLayoutInflater().inflate(R.layout.contact_edit, null);
        //get the TextViews
        EditText rowName = contact_view.findViewById(R.id.row_name);
        EditText rowEmail = contact_view.findViewById(R.id.row_email);
        TextView rowId = contact_view.findViewById(R.id.row_id);

        //set the fields for the alert dialog
        rowName.setText(selectedContact.getName());
        rowEmail.setText(selectedContact.getEmail());
        rowId.setText("id:" + selectedContact.getId());

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("You clicked on item #" + position)
                .setMessage("You can update the fields and then click update to save in the database")
                .setView(contact_view) //add the 3 edit texts showing the contact information
                .setPositiveButton("Update", (click, b) -> {
                    selectedContact.update(rowName.getText().toString(), rowEmail.getText().toString());
                    updateContact(selectedContact);
                    myAdapter.notifyDataSetChanged(); //the email and name have changed so rebuild the list
                })
                .setNegativeButton("Delete", (click, b) -> {
                    deleteContact(selectedContact); //remove the contact from database
                    contactsList.remove(position); //remove the contact from contact list
                    myAdapter.notifyDataSetChanged(); //there is one less item so update the list
                })
                .setNeutralButton("dismiss", (click, b) -> { })
                .create().show();

        */
    }
/*
    protected void updateSong(SongEntity s)
    {
        //Create a ContentValues object to represent a database row:
        ContentValues updatedValues = new ContentValues();
        updatedValues.put(MyOpener.COL_NAME, c.getName());
        updatedValues.put(MyOpener.COL_EMAIL, c.getEmail());

        //now call the update function:
        db.update(MyOpener.TABLE_NAME, updatedValues, MyOpener.COL_ID + "= ?", new String[] {Long.toString(c.getId())});
    }

    protected void deleteContact(Contact c)
    {
        db.delete(MyOpener.TABLE_NAME, MyOpener.COL_ID + "= ?", new String[] {Long.toString(c.getId())});
    }

*/

private class MyListAdapter extends BaseAdapter {

        public int getCount() { return songList.size();}

        public Object getItem(int position) { return songList.get( position); }

       // public long getItemId(int position) { return (long) position; }

        public View getView(int position, View old, ViewGroup parent)
        {
            LayoutInflater inflater = getLayoutInflater();

            //make a new row:
             View newView = inflater.inflate(R.layout.row_listview_song_layout, parent, false);

            //set what the text should be for this row:
            SongEntity songRow= (SongEntity) getItem(position);

            TextView songTitle = (TextView)newView.findViewById(R.id.row_songTitle);
            TextView songId = (TextView)newView.findViewById(R.id.row_songId);
            TextView artistName = (TextView)newView.findViewById(R.id.row_artistName);
            TextView artistId = (TextView)newView.findViewById(R.id.row_artistId);
            TextView rowId = (TextView)newView.findViewById(R.id.row_id);


            songTitle.setText(  songRow.getSongTitle());
            songId.setText(  songRow.getSongId());
            artistName.setText(  songRow.getArtistName());
            artistId.setText(  songRow.getArtistId());
            rowId.setText("id:" + songRow.getId());

            return newView;
        }
        public long getItemId(int position)
        {
            return getItem(position).getId();
        }

    }

    private class MySongHTTPRequest extends AsyncTask< String, Integer, String>
    {
        //Type3                Type1
        public String doInBackground(String ... args)
        {
            try {

                //create a URL object of what server to contact:
                URL url = new URL(args[0]);
                //open the connection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                publishProgress(10);
                //wait for data:
                InputStream response = urlConnection.getInputStream();
                publishProgress(50);
                //JSON reading:   Look at slide 26
                //Build the entire string response:
                BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                String result = sb.toString(); //result is the whole string
                publishProgress(100);

            }
            catch (Exception e)
            {

            }

            return "Done";
        }

        //Type 2
        public void onProgressUpdate(Integer ... args)
        {
            progressBarSong.setProgress(args[0]);
        }
        //Type3
        public void onPostExecute(String result)
        {   // convert string to JSON: Look at slide 27:
            Log.i("HTTP", result);

            try {
                JSONObject songReport = new JSONObject(result);
                Log.i("songactivity", "The json: " + songReport) ;


            } catch (JSONException e) {
                e.printStackTrace();
            }

            //get the double associated with "value"
            //double uvRating = songReport.getDouble("value");
            // Log.i("MainActivity", "The uv is now: " + uvRating) ;


        }
    }













}