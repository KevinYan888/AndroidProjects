package com.cst2335.project01;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CardataFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CardataFragment extends Fragment {

    private Bundle dataFromActivity;
    private long id;
    private String  modelName;
    private String  make;
    private AppCompatActivity parentActivity;


    public CardataFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_cardata, container, false);

        dataFromActivity = getArguments();
        id = dataFromActivity.getLong("id");
        make = dataFromActivity.getString("make");
        modelName = dataFromActivity.getString("name");
        View extraStuff = inflater.inflate(R.layout.fragment_cardata, container, false);
//        getLayoutInflater().inflate(R.layout.activity_empty, container, false);
        //get the TextViews
        TextView rowName = extraStuff.findViewById(R.id.modelName);
        rowName.setText(modelName);
        TextView rowMake = extraStuff.findViewById(R.id.makeName);
        rowMake.setText(make);
        TextView rowId = extraStuff.findViewById(R.id.modelID);
        rowId.setText("ID="+id);


//        Button saving = extraStuff.findViewById(R.id.saving);
//        saving.setOnClickListener( sb->{
//            Intent goToSave = new Intent(CardataFragment.this, SavingActivity.class);
//            startActivity(goToSave);
//        });
        Button viewing = extraStuff.findViewById(R.id.viewing);
        viewing.setOnClickListener(vb-> {
            Intent goToView = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com/search?q="+make+"+"+modelName));
            startActivity(goToView);
        });
        Button shopping = extraStuff.findViewById(R.id.shopping);
        shopping.setOnClickListener( shb->{
            Intent goToShop = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.autotrader.ca/cars/?mdl="+modelName+"&make="+make+"&loc=K2G1V8"));
            startActivity(goToShop);
        });


        // get the delete button, and add a click listener:
        Button hide = (Button)extraStuff.findViewById(R.id.hide);
        hide.setOnClickListener( clk -> {
            //Tell the parent activity to remove
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