package com.cst2335.project01;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.os.Bundle;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import com.google.android.material.snackbar.Snackbar;
import java.util.ArrayList;
import java.util.List;


/**
 */
public class Favorite_Game_List extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {

    /**
     * The constant favSoccerList.
     */
    public static List<FavSoccerDetails>  favSoccerList = new ArrayList();
    private SQLiteDatabase db;
    private TextView favTV;
    /**
     * The constant myAdapter.
     */
    public static   MyListAdapter myAdapter;
    private Button goToListbtn;

    /**
     *this method is used to  accomplish the function of all buttons, and to determine whether to use fragment
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_game_list);
        boolean isTablet = findViewById(R.id.soc_fav_fragmentLocation) != null;
        if(!isTablet){
            Toolbar tBar = findViewById(R.id.soc_favlist_toolbar);
//            setSupportActionBar(tBar);
            DrawerLayout drawer = findViewById(R.id.favlist_drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                    drawer, tBar, R.string.open, R.string.close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();
            NavigationView navigationView = findViewById(R.id.soc_favlist_nav);
            navigationView.setItemIconTintList(null);
            navigationView.setNavigationItemSelectedListener(this);
        }
        loadfromDB();

        ListView myList = findViewById(R.id.soc_fav_list);
        myList.setAdapter( myAdapter = new MyListAdapter());
        goToListbtn = findViewById(R.id.soc_gohome_btn);
        goToListbtn.setOnClickListener(b->{
            Intent goToList = new Intent(this,GameList.class);
            startActivity(goToList);
        });
        myList.setOnItemClickListener(((parent, view, position, id) -> {
            Bundle dataToPass = new Bundle();

            String gtitle = favSoccerList.get(position).title;
            String gdate = favSoccerList.get(position).date;
            String gurl = favSoccerList.get(position).newsUrl;
            String iurl = favSoccerList.get(position).imgUrl;
            String gdes = favSoccerList.get(position).newsdes;

            String source = "favList";
            dataToPass.putString("gametitle", gtitle);
            dataToPass.putString("gamedate", gdate);
            dataToPass.putString("gameurl", gurl);
            dataToPass.putString("imageurl", iurl);
            dataToPass.putString("newsdescription",gdes);
            dataToPass.putString("sourcePage",source);
            if(isTablet){
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle(getResources().getString(R.string.soccer_alert_title) + gtitle).setMessage(R.string.soccer_alert_msg
                ).setPositiveButton(R.string.soccer_postive, (click, arg) -> {
                    SoccerDetailsFragment dFragment = new SoccerDetailsFragment(); //add a DetailFragment
                    dFragment.setArguments(dataToPass); //pass it a bundle for information
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.soc_fav_fragmentLocation, dFragment) //Add the fragment in FrameLayout
                            .commit();
                    Toast.makeText(this, R.string.soccer_toast_txt, Toast.LENGTH_SHORT).show();
                }).setNegativeButton(R.string.soccer_negative, (click, arg) -> {
                    Snackbar.make(myList, R.string.soccer_snackbar_msg, Snackbar.LENGTH_SHORT).show();
                }).setNeutralButton(R.string.soccer_neu,(click, arg)->{
                    db.delete(SoccerDB.TABLE_NAME,SoccerDB.TITLE_COL + "=?",new String[]{favSoccerList.get(position).title});
                    favSoccerList.remove(position);
                    Snackbar.make(myList, R.string.soc_delete_msg, Snackbar.LENGTH_SHORT).show();
                    myAdapter.notifyDataSetChanged();
                    if (isTablet) {
                        if (getSupportFragmentManager().findFragmentById(R.id.soc_fav_fragmentLocation) != null)
                            getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentById(R.id.soc_fav_fragmentLocation)).commit();
                    }
                }).create().show();



            }else{
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle(getResources().getString(R.string.soccer_alert_title) + gtitle).setMessage(R.string.soccer_alert_msg
                ).setPositiveButton(R.string.soccer_postive,(click, arg)->{
                    Intent nextActivity = new Intent(Favorite_Game_List.this, GameDetailActivity.class);
                    nextActivity.putExtras(dataToPass); //send data to next activity
                    startActivity(nextActivity); //make the transition
                    Toast.makeText(this, R.string.soccer_toast_txt, Toast.LENGTH_SHORT).show();
                }).setNegativeButton(R.string.soccer_negative,(click, arg)->{
                    Snackbar.make(myList, R.string.soccer_snackbar_msg, Snackbar.LENGTH_SHORT).show();
                }) .setNeutralButton(R.string.soccer_neu,(click, arg)->{
                    db.delete(SoccerDB.TABLE_NAME,SoccerDB.TITLE_COL + "=?",new String[]{favSoccerList.get(position).title});
                   favSoccerList.remove(position);
                    Snackbar.make(myList, R.string.soc_delete_msg, Snackbar.LENGTH_SHORT).show();
                   myAdapter.notifyDataSetChanged();
                }).create().show();

            }
        }));
        goToListbtn = findViewById(R.id.soc_gohome_btn);
        goToListbtn.setOnClickListener(b->{
            GameList.isShowDialog = true;
            Intent goToList = new Intent(this,GameList.class);
            startActivity(goToList);
        });
    }
    /**
     * Inflate the menu items for use in the action bar
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.item1:

                break;
            case R.id.item2:

                break;
            case R.id.item3:

                break;
            case R.id.item4:
                Toast.makeText(this, R.string.soc_tbar_msg, Toast.LENGTH_LONG).show();
                break;
        }

        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.api:
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData( Uri.parse("https://www.goal.com/en/feeds/news?fmt=rss") );
                startActivity(i);
                break;
            case R.id.instruction:
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle(R.string.soc_instruction_title).setMessage(R.string.soc_intro_msg
                ).setPositiveButton(R.string.soc_intro_positive, (click, arg) -> {})
                        .create().show();
                break;
            case R.id.rate:
                final EditText et = new EditText(this);
                et.setHint("...");

                new AlertDialog.Builder(this).setTitle(R.string.rate_alert_msg).setMessage(R.string.rate_msg)
                        .setView(et)
                        .setPositiveButton("Thank you", (click,arg) ->{

                        })
                        .setNegativeButton("cancel", null)
                        .show();
                break;
        }

        DrawerLayout drawerLayout = findViewById(R.id.list_drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;

    }


    /**
     * The type Fav soccer details.
     */
    class FavSoccerDetails{
            /**
             * The Title.
             */
            String title;
            /**
             * The Date.
             */
            String date;
            /**
             * The news article URL.
             */
            String newsUrl;
            /**
             * The Img url.
             */
            String imgUrl;
            /**
             * The news description.
             */
            String newsdes;

            /**
             * Instantiates a new Soccer details.
             *
             * @param title    the title
             * @param date     the date
             * @param newsUrl the news article URL
             * @param imgUrl   the img url
             */
            public FavSoccerDetails(String title, String date, String newsUrl,String imgUrl,String newsdes){
                this.date = date;
                this.title = title;
                this.newsUrl = newsUrl;
                this.imgUrl = imgUrl;
                this.newsdes = newsdes;
            }

        }

    /**
     * load date from soccerDB
     */

    private void loadfromDB(){
        SoccerDB dbOpener = new SoccerDB(this);
        db = dbOpener.getWritableDatabase();

        String[] columns = {SoccerDB.TITLE_COL, SoccerDB.DATE_COL, SoccerDB.IMG_COL, SoccerDB.URL_COL, SoccerDB.DES_COL};
        Cursor results = db.query(false, SoccerDB.TABLE_NAME, columns, null, null, null, null, null, null);

        int titleColIndx = results.getColumnIndex(SoccerDB.TITLE_COL);
        int dateColIndx = results.getColumnIndex(SoccerDB.DATE_COL);
        int urlColIndx = results.getColumnIndex(SoccerDB.URL_COL);
        int imgColIndx = results.getColumnIndex(SoccerDB.IMG_COL);
        int descolIndx =results.getColumnIndex(SoccerDB.DES_COL);

        while (results.moveToNext()){
            String title = results.getString(titleColIndx);
            String  date = results.getString(dateColIndx);
            String url = results.getString(urlColIndx);
            String img = results.getString(imgColIndx);
            String des =results.getString(descolIndx);
            //long id = results.getLong(idColIndx);
            favSoccerList.add(new FavSoccerDetails(title,date,url,img, des));}
    }
    private class MyListAdapter extends BaseAdapter {

        public int getCount() { return favSoccerList.size();}

        public FavSoccerDetails getItem(int position) { return  favSoccerList.get(position); }

        public long getItemId(int position) { return position ; }

        /**
         * set the text of each view based on user's favorite game title
         * @param position
         * @param old
         * @param parent
         * @return
         */
        @Override
        public View getView(int position, View old, ViewGroup parent)
        {
            LayoutInflater inflater = getLayoutInflater();

            //make a new row:
            View newView = inflater.inflate(R.layout.soc_fav_list_rowlayout, parent, false);

            //set what the text should be for this row:
            favTV = newView.findViewById(R.id.soc_fav_detailTV);
            favTV.setText( getItem(position).title );

            //return it to be put in the table
            return newView;
        }
    }
}
