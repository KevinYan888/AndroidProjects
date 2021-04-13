package com.cst2335.project01;//package com.cst2335.project01;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static android.view.View.VISIBLE;


public class SongActivity extends AppCompatActivity {
    ArrayList<SongEntity> songList = new ArrayList<>();
    MySongListAdapter mySongAdapter;
    private static int ACTIVITY_VIEW_SONGLIST = 33;
    int positionClicked = 0;

    ProgressBar progressBarSong;
    ListView songListV;
    EditText searchView;


    public static final String ITEM_SONG_TITLE = "SONGTITLE";
    public static final String ITEM_SONG_ID = "SONGID";
    public static final String ITEM_ARTIST_NAME = "ARTISTNAME";
    public static final String ITEM_ARTIST_ID = "ARTISTID";
    public static final String ITEM_ID = "ITEMID";
    public static final String ISBACK = "ISBACK";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_songstar );

        ImageView songHeadImage = findViewById( R.id.imageViewSong );
        searchView = findViewById( R.id.editTextSearch );
        searchView.requestFocus();
        ImageButton searchBtn = findViewById( R.id.song_searchButton );
        progressBarSong = findViewById( R.id.progressBarSong );

        searchBtn.setOnClickListener( click -> {

           // String searchterm = "";
            String searchterm = searchView.getText().toString();
            closeKeyboard();
            // http://www.songsterr.com/a/ra/songs.xml?pattern=XXX
            // http://www.songsterr.com/a/ra/songs.json?pattern=XXX
            String songJsonURL = "https://www.songsterr.com/a/ra/songs.json?pattern=" + searchterm;
            progressBarSong.setVisibility( VISIBLE );
            MySongHTTPRequest songReq = new MySongHTTPRequest();
            songReq.execute( songJsonURL );  //Type 1
        } );


        songListV = findViewById( R.id.listViewSong );
        songListV.setAdapter( mySongAdapter = new MySongListAdapter() );
        mySongAdapter.notifyDataSetChanged();


        boolean isTablet = findViewById( R.id.fragmentLocation_song ) != null; //check if the FrameLayout is loaded
        songListV.setOnItemClickListener( (parent, view, position, id) -> {

            SongEntity selectedSong = songList.get( position );
            long ii = mySongAdapter.getItemId( position );

            Bundle dataToPass = new Bundle();
            dataToPass.putString( ITEM_SONG_TITLE, selectedSong.getSongTitle() );
            dataToPass.putInt( ITEM_SONG_ID, selectedSong.getSongId() );
            dataToPass.putString( ITEM_ARTIST_NAME, selectedSong.getArtistName() );
            dataToPass.putInt( ITEM_ARTIST_ID, selectedSong.getArtistId() );
            dataToPass.putLong( ITEM_ID, ii );
            dataToPass.putBoolean( ISBACK, false );

            if (isTablet) {
                SongDetailFragment dFragment = new SongDetailFragment(); //add a DetailFragment
                dFragment.setArguments( dataToPass ); //pass it a bundle for information
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace( R.id.fragmentLocation, dFragment ) //Add the fragment in FrameLayout
                        .commit(); //actually load the fragment. Calls onCreate() in DetailFragment
            } else //isPhone
            {
                Intent nextActivity = new Intent( SongActivity.this, SongSecondActivity.class );
                nextActivity.putExtras( dataToPass ); //send data to next activity
                startActivity( nextActivity ); //make the transition
            }
        } );
    }

    /**
     * set shareperference in onresume() once back to this activity , value will presented
     */
    protected void onResume(){
        super.onResume();
        SharedPreferences sh= getSharedPreferences( "MySharedPref", MODE_PRIVATE );
        String s1=sh.getString("searchView", " ");
        searchView.setText(s1);
    }

    /**
     * once onPause() invoked . data will kept in sharepreference for late use
     */
    protected void onPause(){
        super.onPause();
        SharedPreferences sharedPreferences=getSharedPreferences( "MySharedPref", MODE_PRIVATE );
        SharedPreferences.Editor myEdit=sharedPreferences.edit();
        myEdit.putString("searchView", searchView.getText().toString());
        myEdit.apply();

    }

    /**
     * it will close softkeyboard once shearch btn clicked
     */
    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService( Context.INPUT_METHOD_SERVICE );
            imm.hideSoftInputFromWindow( view.getWindowToken(), 0 );
        }
    }

    /**
     * adapter for listview to show song items shearched out
     */
    class MySongListAdapter extends BaseAdapter {

        public int getCount() {
            return songList.size();
        }

        public Object getItem(int position) {
            return songList.get( position );
        }

        public long getItemId(int position) {
            return (long) position;
        }

        public View getView(int position, View old, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();

            //make a new row:
            View newView = inflater.inflate( R.layout.row_song_layout, parent, false );

            //set what the text should be for this row:
            SongEntity songRow = (SongEntity) getItem( position );

            TextView songTitle = (TextView) newView.findViewById( R.id.row_songTitle );
            TextView songId = (TextView) newView.findViewById( R.id.row_songId );
            TextView artistName = (TextView) newView.findViewById( R.id.row_artistName );
            TextView artistId = (TextView) newView.findViewById( R.id.row_artistId );
            TextView rowId = (TextView) newView.findViewById( R.id.row_id );


            songTitle.setText( songRow.getSongTitle() );
            songId.setText( "songID: " + String.valueOf( songRow.getSongId() ) );
            artistName.setText( "artist name: " + songRow.getArtistName() );
            artistId.setText( "artistID: " + String.valueOf( songRow.getArtistId() ) );
            rowId.setText( "list No:" + String.valueOf( songRow.getId() ) );

            return newView;
        }
    }

    /**
     * nested class to put search in backtread
     */
    class MySongHTTPRequest extends AsyncTask<String, Integer, String> {
        //Type3                Type1
        public String doInBackground(String... args) {
            String result = null;
            try {
                URL url = null;
                url = new URL( args[0] );
                //open the connection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                publishProgress( 10 );
                Log.i( "HTTP", "do in background step one url" + url );
                //wait for data:
                InputStream response = urlConnection.getInputStream();
                publishProgress( 50 );
                //JSON reading:   Look at slide 26
                //Build the entire string response:
                BufferedReader reader = new BufferedReader( new InputStreamReader( response, "UTF-8" ), 8 );

                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append( line + "\n" );
                }
                result = sb.toString(); //result is the whole string
                Log.i( "HTTP", "do in background " + result );
                publishProgress( 100 );
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        //Type 2
        public void onProgressUpdate(Integer... args) {
            progressBarSong.setProgress( args[0] );
        }

        //Type3
        public void onPostExecute(String str) {   // convert string to JSON: Look at slide 27:
            Log.i( "HTTP", str.toString() );
            //Log.d("data", s.toString());
            JSONArray jsonArray = null;
            songList.clear();
            try {

                jsonArray = new JSONArray( str );
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonObject = null;
                    try {
                        jsonObject = jsonArray.getJSONObject( i );
                        String songIdStr = jsonObject.getString( "id" );
                        String songTitle = jsonObject.getString( "title" );
                        JSONObject jsonObjectAtrist = jsonObject.getJSONObject( "artist" );
                        String artistIdStr = jsonObjectAtrist.getString( "id" );
                        String artistName = jsonObjectAtrist.getString( "name" );
                        Integer songId = Integer.valueOf( songIdStr );
                        int artistId = Integer.parseInt( artistIdStr );
                        SongEntity songrow = new SongEntity( songTitle, songId, artistName, artistId, i );
                        songList.add( songrow );
                    } catch (JSONException jsonException) {
                        jsonException.printStackTrace();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //songListV.setAdapter( mySongAdapter = new MySongListAdapter() );
            mySongAdapter.notifyDataSetChanged();
            progressBarSong.setVisibility( View.GONE );
           // searchView.setText( "" );
        }
    }

}