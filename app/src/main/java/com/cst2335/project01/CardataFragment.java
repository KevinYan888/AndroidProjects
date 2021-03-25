package com.cst2335.project01;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

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
    private AppCompatActivity parentActivity;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CardataFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CardataFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CardataFragment newInstance(String param1, String param2) {
        CardataFragment fragment = new CardataFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_cardata, container, false);

        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_details, container, false);
        dataFromActivity = getArguments();
        id = dataFromActivity.getLong(SearchActivity.MODEL_ID );
        View extraStuff = inflater.inflate(R.layout.fragment_cardata, container, false);
//        getLayoutInflater().inflate(R.layout.activity_empty, container, false);
        //get the TextViews
        TextView rowName = extraStuff.findViewById(R.id.modelName);
        rowName.setText(dataFromActivity.getString(SearchActivity.MODEL_NAME));
        TextView rowId = extraStuff.findViewById(R.id.modelID);
        rowId.setText("ID="+id);

//        Button saving = extraStuff.findViewById(R.id.saving);
//        saving.setOnClickListener( sb->{
//            Intent goToSave = new Intent(CardataFragment.this, SavingActivity.class);
//            startActivity(goToSave);
//        });
//        Button viewing = extraStuff.findViewById(R.id.viewing);
//        viewing.setOnClickListener(vb-> {
//            Intent goToView = new Intent(CardataFragment.this, ViewingActivity.class);
//            startActivity(goToView);
//        });
//        Button shopping = extraStuff.findViewById(R.id.shopping);
//        shopping.setOnClickListener( shb->{
//            Intent goToShop = new Intent(CardataFragment.this, ShoppingActivity.class);
//            startActivity(goToShop);
//        });


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