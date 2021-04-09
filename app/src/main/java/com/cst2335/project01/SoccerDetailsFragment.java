package com.cst2335.project01;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 */
public class SoccerDetailsFragment extends Fragment implements NavigationView.OnNavigationItemSelectedListener {

    private Button saveOrRemoveBtn, goToFavBtn, goToUrl;
    private ProgressBar pb2;

    private TextView gameTitle;
    private TextView  gameDate;
    private TextView url;
    private TextView description;
    private ImageView gameimage;
    private String imageUrl;

    private SQLiteDatabase db;
    private Bundle dataFromActivity;
    public SharedPreferences prefs;
    private AppCompatActivity parentActivity;


    /**
     * no-arg constructor
     */
    public SoccerDetailsFragment() {
    }

    /**
     * this method is used to set the content of detail page after user select a game.
     * If the source page is the GameList, then the saveOrRemoveBtn will be saveBtn to save a
     * favorite game.
     * If the source page is the FavoriteGameList, then the saveOrRemoveBtn will be removeBtn
     * to remove a game from the favorite list.
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dataFromActivity = getArguments();
        View result = inflater.inflate(R.layout.fragment_soccer_details, container, false);
        Toolbar tbar = result.findViewById(R.id.soc_fragdetail_toolbar);
//        ((AppCompatActivity) getActivity()).setSupportActionBar(tbar);


        DrawerLayout drawer = result.findViewById(R.id.fragtail_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this.getActivity(),
                drawer, tbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = result.findViewById(R.id.soc_fragdetail_nav);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);

        goToUrl = result.findViewById(R.id.gotourl);
        gameTitle =result.findViewById(R.id.gametitle);
        gameimage = result.findViewById(R.id.image);
        gameDate = result.findViewById(R.id.date);
        url = result.findViewById(R.id.url);
        description = result.findViewById(R.id.description);

        gameTitle.setText(dataFromActivity.getString("gametitle"));
        gameDate.setText(dataFromActivity.getString("gamedate"));
        url.setText(dataFromActivity.getString("gameurl"));
        description.setText(dataFromActivity.getString("newsdescription"));
        imageUrl = dataFromActivity.getString("imageurl");

        GameImageHttpRequest req = new GameImageHttpRequest();
        req.execute(imageUrl);
        SoccerDB soccerDB = new SoccerDB(this.getContext());
        db = soccerDB.getWritableDatabase();

        String gt = gameTitle.getText().toString();
        String descrip = description.getText().toString();
        String date = gameDate.getText().toString();
        String gameUrl = url.getText().toString();

        String source = dataFromActivity.getString("sourcePage");

        saveOrRemoveBtn = result.findViewById(R.id.soc_saveBtn);
        boolean isTablet = result.findViewById(R.id.soc_fav_fragmentLocation) != null;
        if (source.equals("listPage")) {
            //saveOrRemoveBtn.setText(@string/save);
            saveOrRemoveBtn.setOnClickListener(b -> {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this.getContext());
                alertDialog.setTitle("Save as favorite? ").setMessage("would you like save this game to your favorite list?"
                ).setPositiveButton("Yes", (click, arg) -> {
                    ContentValues newRowValue = new ContentValues();
                    newRowValue.put(SoccerDB.TITLE_COL, gt);
                    newRowValue.put(SoccerDB.DATE_COL, date);
                    newRowValue.put(SoccerDB.URL_COL, gameUrl);
                    newRowValue.put(SoccerDB.IMG_COL, imageUrl);
                    newRowValue.put(SoccerDB.DES_COL,descrip);
                    long id = db.insert(SoccerDB.TABLE_NAME, null, newRowValue);
                    if (id > 0)//if database insertion fails, id = -1
                        Toast.makeText(this.getContext(), "saved successfully", Toast.LENGTH_SHORT).show();
                }).setNegativeButton("No", (click, arg) -> {
                    Snackbar.make(saveOrRemoveBtn, "you selected no", Snackbar.LENGTH_SHORT).show();
                }).create().show();
            });
        } else if (source.equals("favList")) {
            saveOrRemoveBtn.setText("Remove From the favorite list");
            saveOrRemoveBtn.setOnClickListener(click -> {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this.getContext());
                alertDialog.setTitle((R.string.soc_removeQue)).setMessage(R.string.soc_removeMsg
                ).setPositiveButton("Yes", (c, arg) -> {
                    db.delete(SoccerDB.TABLE_NAME, SoccerDB.TITLE_COL + "=?", new String[]{gt});
                    parentActivity.getSupportFragmentManager().beginTransaction().remove(this).commit();
                    Toast.makeText(this.getContext(), R.string.soc_removeToast, Toast.LENGTH_SHORT).show();
                }).setNegativeButton("No", (c, arg) -> {
                    Snackbar.make(saveOrRemoveBtn, "you selected no", Snackbar.LENGTH_SHORT).show();
                }).create().show();
            });

        }
        goToFavBtn = result.findViewById(R.id.soc_goFav);
        goToFavBtn.setOnClickListener(b -> {
            Intent goToFav = new Intent(this.getContext(), Favorite_Game_List.class);
            startActivity(goToFav);
        });

        prefs = this.getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);

        goToUrl.setOnClickListener(click -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("gameUrl", gameUrl);
            editor.commit();
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(gameUrl));
            startActivity(i);
        });

        setHasOptionsMenu(true);
        pb2 = result.findViewById(R.id.pb2);
        return result;
    }


    /**
     * Inflate the menu items for use in the action bar
     *
     * @param menu
     * @param inflater
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.toolbar_menu, menu);

    }


    /**
     * this method is used to finish different function when user
     * selected different menu on toolbar
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:

                break;
            case R.id.item2:

                break;
            case R.id.item3:

                break;
            case R.id.item4:
                Toast.makeText(this.getContext(), R.string.soc_tbar_msg, Toast.LENGTH_LONG).show();
                break;
        }

        return true;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        parentActivity = (AppCompatActivity) context;
    }

    /**
     * this method is used to finish different function when user
     * selected different menu on navigation drawer
     *
     * @param item
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.api:
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://www.goal.com/en/feeds/news?fmt=rss"));
                startActivity(i);
                break;
            case R.id.instruction:
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this.getContext());
                alertDialog.setTitle(R.string.soc_instruction_title).setMessage(R.string.soc_intro_msg
                ).setPositiveButton(R.string.soc_intro_positive, (click, arg) -> {
                })
                        .create().show();
                break;
            case R.id.rate:
                final EditText et = new EditText(this.getContext());
                et.setHint("...");

                new AlertDialog.Builder(this.getContext()).setTitle(R.string.rate_alert_msg).setMessage(R.string.rate_msg)
                        .setView(et)
                        .setPositiveButton("Thank you", (click, arg) -> {

                        })
                        .setNegativeButton("cancel", null)
                        .show();
                  break;
        }
        //DrawerLayout drawerLayout = findViewById(R.id.list_drawer_layout);
        //drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    /**
     * request game iamge URL and get the image back
     */
    private class GameImageHttpRequest extends AsyncTask<String, Integer, String> {
        private Bitmap image = null;

        @Override
        protected String doInBackground(String... strings) {
            try {

                URL imgUrl = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) imgUrl.openConnection();
                urlConnection.connect();
                publishProgress(25);
                int responseCode = urlConnection.getResponseCode();
                if (responseCode == 200) {
                    image = BitmapFactory.decodeStream(urlConnection.getInputStream());
                    publishProgress(50);
                }
                publishProgress(100);
                return "Done";
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "done";
        }

        @Override
        public void onProgressUpdate(Integer... value) {
            pb2.setVisibility(View.VISIBLE);
            pb2.setProgress(value[0]);

        }

        @Override
        public void onPostExecute(String fromDoInBackground) {
            gameimage.setImageBitmap(image);

            pb2.setVisibility(View.INVISIBLE);

        }
    }



}
