package com.cst2335.project01;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

/**
 * this page content the database to show the favourate song saved ,
 * a pop dialoge will show if cklick the song list it self
 */
public class SongThirdActivity extends AppCompatActivity {
    ArrayList<SongEntity> songFavourateList = new ArrayList<>( );
    ListView songFaveListV ;
    FavoSongListAdapter myFaveSongAdapter;
    SQLiteDatabase db;
    private AppCompatActivity parentActivity;

    public static final String ITEM_SONG_TITLE = "SONGTITLE";
    public static final String ITEM_SONG_ID = "SONGID";
    public static final String ITEM_ARTIST_NAME = "ARTISTNAME";
    public static final String ITEM_ARTIST_ID = "ARTISTID";
    public static final String ITEM_ID = "ITEMID";
    public static final String ISBACK = "ISBACK";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_song_third );

        songFaveListV = findViewById( R.id.Favourate_listViewSong);
        songFaveListV.setAdapter( myFaveSongAdapter = new FavoSongListAdapter() );
        loadDataFromDatabase();
        myFaveSongAdapter.notifyDataSetChanged();

        songFaveListV.setOnItemClickListener( (parent, view, position, id) -> {
           showSong (position);
        }   );

    }

    /**
     * nest class song adapter for ListView extended baseadapter
     */
    class FavoSongListAdapter extends BaseAdapter {

        public int getCount() { return songFavourateList.size();}

        public Object getItem(int position) { return songFavourateList.get( position); }

        public long getItemId(int position) { return (long) position; }

        public View getView(int position, View old, ViewGroup parent)
        {
            LayoutInflater inflater = getLayoutInflater();

            //make a new row:
            View newView = inflater.inflate( R.layout.row_song_layout, parent, false);

            //set what the text should be for this row:
            SongEntity songRow= (SongEntity) getItem(position);

            TextView songTitle = (TextView)newView.findViewById( R.id.row_songTitle);
            TextView songId = (TextView)newView.findViewById( R.id.row_songId);
            TextView artistName = (TextView)newView.findViewById( R.id.row_artistName);
            TextView artistId = (TextView)newView.findViewById( R.id.row_artistId);
            TextView rowId = (TextView)newView.findViewById( R.id.row_id);


            songTitle.setText(songRow.getSongTitle());
            songId.setText( "songID: " +String.valueOf(songRow.getSongId()));
            artistName.setText( "artist name: "+ songRow.getArtistName());
            artistId.setText( "artistID: "+ String.valueOf(songRow.getArtistId()));
            rowId.setText("songDB No:" +String.valueOf(songRow.getId()) );

            return newView;
        }
    }

    /**
     * hi is the pup up dialoge reflect the click on item on listview
     * @param position is the item been clicked on the songList 
     */
    protected void showSong(int position)
    {

        SongEntity selectedSong = songFavourateList.get(position);
        View song_view = getLayoutInflater().inflate( R.layout.selected_song_dialog, null);

        TextView songId=song_view.findViewById( R.id.row_songId_d);
        TextView songtitle=song_view.findViewById( R.id.row_songTitle_d);
        TextView artistName=song_view.findViewById( R.id.row_artistName_d);
        TextView artistId=song_view.findViewById( R.id.row_artistId_d);

        songId.setText("songID:"+String.valueOf( selectedSong.getSongId()));
        songtitle.setText("Title: "+selectedSong.getSongTitle());
        artistId.setText("artistID: "+ String.valueOf(selectedSong.getArtistId()));
        artistName.setText("artist name: "+ selectedSong.getArtistName());
        long idDB= selectedSong.getId();
        long iiRow = myFaveSongAdapter.getItemId( position );

        Bundle dataToPass = new Bundle();
        dataToPass.putString( ITEM_SONG_TITLE, selectedSong.getSongTitle() );
        dataToPass.putInt( ITEM_SONG_ID, selectedSong.getSongId() );
        dataToPass.putString( ITEM_ARTIST_NAME, selectedSong.getArtistName() );
        dataToPass.putInt( ITEM_ARTIST_ID, selectedSong.getArtistId() );
        dataToPass.putLong( ITEM_ID, idDB );
        dataToPass.putBoolean( ISBACK, true );

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("You clicked onFavorite Song row#" + iiRow+"DB id"+idDB)
                .setMessage("You like remove it from your favorite?")
                .setView(song_view) //add the 3 edit texts showing the contact information
                .setPositiveButton("Yes", (click, b) -> {
                    deleteSongEntityFromDB(selectedSong );
                    songFavourateList.remove(position);
                    myFaveSongAdapter.notifyDataSetChanged();
                })
                .setNegativeButton("No", (click, b) -> {

                })
                .setNeutralButton("back to pre-page", (click, b) -> {

                    Intent backActivity = new Intent( SongThirdActivity.this, SongSecondActivity.class );
                    backActivity.putExtras( dataToPass ); //send data to next activity
                    startActivity(backActivity);

                })
                .create().show();

    }

    /**
     * song list favoritate database setup and save data
     * once invoke this activity will load song list to listarray and show out through listView
     */
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
        int songTitleColumnIndex = results.getColumnIndex( SongOpener.COL_SONGTITLE);
        int songIdColIndex = results.getColumnIndex( SongOpener.COL_SONGID);
        int artistNameColumnIndex = results.getColumnIndex( SongOpener.COL_ARTISTNAME);
        int artistIdColIndex = results.getColumnIndex( SongOpener.COL_ARTISTID);
        int idColIndex = results.getColumnIndex( SongOpener.COL_ID);

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
            songFavourateList.add(new SongEntity( songTitle, songId, artistName, artistId, id));

        }

        //At this point, the contactsList array has loaded every row from the cursor.
    }

    /**
     * do database update fresh
     * @param s
     */
    protected void updateSong(SongEntity s)
    {
        //Create a ContentValues object to represent a database row:
        ContentValues updatedValues = new ContentValues();
        updatedValues.put( SongOpener.COL_SONGID, s.getSongId());
        updatedValues.put( SongOpener.COL_SONGTITLE, s.getSongTitle());
        updatedValues.put( SongOpener.COL_ARTISTID, s.getArtistId());
        updatedValues.put( SongOpener.COL_ARTISTNAME, s.getArtistName());


        //now call the update function:
        db.update( SongOpener.TABLE_NAME, updatedValues, SongOpener.COL_ID + "= ?", new String[] {Long.toString(s.getId())});
    }

    /**
     * delet raw from database
     * @param s
     */
    public  void  deleteSongEntityFromDB(SongEntity s)
    {
        db.delete( SongOpener.TABLE_NAME, SongOpener.COL_ID + "= ?", new String[] {Long.toString(s.getId())});
    }

}