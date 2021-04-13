package com.cst2335.project01;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CardataFragment} factory method to
 * create an instance of this fragment.
 */
public class CardataFragment extends Fragment {


    private static final String NULL = null;
    SQLiteDatabase db;
    private Bundle dataFromActivity;
    private int modelId;
    private String  modelName;
    private String  make;
    private AppCompatActivity parentActivity;
    public ArrayList<CarListItem> list = new ArrayList<>();

    public CardataFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_cardata, container, false);

        dataFromActivity = getArguments();
        String source = dataFromActivity.getString("sourcePage");

        modelId = dataFromActivity.getInt("modelId");
        make = dataFromActivity.getString("make");
        modelName = dataFromActivity.getString("name");
        View extraStuff = inflater.inflate(R.layout.car_fragment, container, false);
//        getLayoutInflater().inflate(R.layout.activity_empty, container, false);
        //get the TextViews
        TextView rowName = extraStuff.findViewById(R.id.modelName);
        rowName.setText(modelName);
        TextView rowMake = extraStuff.findViewById(R.id.makeName);
        rowMake.setText(make);
        TextView rowId = extraStuff.findViewById(R.id.modelID);
        rowId.setText("modelID= "+modelId);

        CarOpener myOpener = new CarOpener(container.getContext());
        db = myOpener.getWritableDatabase();


        Button saving =extraStuff.findViewById(R.id.saving);
        if(source.equals("search")) {
            saving.setText(R.string.saving_to_databases);
            saving.setOnClickListener(sb -> {
                Snackbar snackbar = Snackbar.make(saving,"Save to database",Snackbar.LENGTH_LONG);
                snackbar.show();
                ContentValues newRow = new ContentValues();
                newRow.put(myOpener.COL_MODELID, modelId);
                newRow.put(myOpener.COL_MAKE, make);
                newRow.put(myOpener.COL_NAME, modelName);
                db.insert(myOpener.TABLE_NAME, NULL, newRow);
                Intent goToSave = new Intent(getActivity(), CarSavingActivity.class);
                startActivity(goToSave);
//            Toast.makeText(saving,"save car type in database",Toast.LENGTH_LONG).show();

            });
//        Button moving =extraStuff.findViewById((R.id.moving);
        }else if (source.equals("move")){
            saving.setText(R.string.remove_from_databases);
            saving.setOnClickListener(sb -> {
                Snackbar snackbar = Snackbar.make(saving,"Save to database",Snackbar.LENGTH_LONG);
                snackbar.show();
                        ContentValues newRow = new ContentValues();
                        newRow.put(myOpener.COL_MODELID, modelId);
                        newRow.put(myOpener.COL_MAKE, make);
                        newRow.put(myOpener.COL_NAME, modelName);
                        db.delete(myOpener.TABLE_NAME, myOpener.COL_MODELID+ "=?", new String[]{String.valueOf(modelId)});
//                        list.remove(id);

                        Intent goToMove = new Intent(getActivity(), CarSavingActivity.class);
                        startActivity(goToMove);

            });
        }


//

        Button viewing = extraStuff.findViewById(R.id.viewing);
        viewing.setOnClickListener(vb-> {
            Snackbar snackbar = Snackbar.make(viewing,"Save to database",Snackbar.LENGTH_LONG);
            snackbar.show();
            Intent goToView = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com/search?q="+make+"+"+modelName));
            startActivity(goToView);

        });
        Button shopping = extraStuff.findViewById(R.id.shopping);
        shopping.setOnClickListener( shb->{
            Snackbar snackbar = Snackbar.make(shopping,"go to shopping",Snackbar.LENGTH_LONG);
            snackbar.show();
            Intent goToShop = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.autotrader.ca/cars/?mdl="+modelName+"&make="+make+"&loc=K2G1V8"));
            startActivity(goToShop);

        });


        // get the delete button, and add a click listener:
        Button hide = (Button)extraStuff.findViewById(R.id.hide);
        hide.setOnClickListener( clk -> {
            //Tell the parent activity to remove
            Snackbar snackbar = Snackbar.make(hide,"Save to database",Snackbar.LENGTH_LONG);
            snackbar.show();
            parentActivity.getSupportFragmentManager().beginTransaction().remove(this).commit();
        });
        return extraStuff;
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        parentActivity = (AppCompatActivity)context;

    }



}