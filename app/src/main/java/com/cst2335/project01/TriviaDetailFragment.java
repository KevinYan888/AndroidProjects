package com.cst2335.project01;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


public class TriviaDetailFragment extends Fragment {

    // from activity: ITEM_STR_QUESTION,ITEM_DIFFICULTY,ITEM_CORRECT,ITEM_NUMBER

    private Bundle dataFromActivity;
    private AppCompatActivity parentActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        dataFromActivity = getArguments();

        // Inflate the layout for this fragment
        View result =  inflater.inflate(R.layout.trivia_detail_fragment, container, false);

        //show the numAndQuestion
        TextView numAndQuestion = (TextView)result.findViewById(R.id.numAndQuestion);
        numAndQuestion.setText("Question "+(dataFromActivity.getInt(TriviaQuestionItemsActivity.ITEM_NUMBER)+1)+
                ". "+ dataFromActivity.getString(TriviaQuestionItemsActivity.ITEM_STR_QUESTION));

        //show the difficulty:
        TextView difficulty = (TextView)result.findViewById(R.id.difficulty);
        difficulty.setText("Difficulty: "+dataFromActivity.getString(TriviaQuestionItemsActivity.ITEM_DIFFICULTY));

        //show correct answer:
        TextView correct = (TextView)result.findViewById(R.id.correct);
        correct.setText("Correct answer: "+dataFromActivity.getString(TriviaQuestionItemsActivity.ITEM_CORRECT));

        // get the hint button, and add a click listener:
        Button btnHint = (Button)result.findViewById(R.id.btnHint);

        btnHint.setOnClickListener( clk -> {
//            if() { //both the list and details are on the screen:
//                ChatRoomActivity parent = (ChatRoomActivity) getActivity();
////                parent.deleteMessageId((int)id); //this deletes the item and updates the list
//                //now remove the fragment since you deleted it from the database:
//                // this is the object to be removed, so remove(this):
//                parent.getSupportFragmentManager().beginTransaction().remove(this).commit();
//            }
//            //for Phone:
//            else //You are only looking at the details, you need to go back to the previous list page
//            {
//                EmptyActivity parent = (EmptyActivity) getActivity();
//                Intent backToFragmentExample = new Intent();
//                backToFragmentExample.putExtra(ChatRoomActivity.ITEM_ID, dataFromActivity.getLong(ChatRoomActivity.ITEM_ID ));
//
//                parent.setResult(Activity.RESULT_OK, backToFragmentExample); //send data back to FragmentExample in onActivityResu
//            //Tell the parent activity to remove
            parentActivity.getSupportFragmentManager().beginTransaction().remove(this).commit();
        });
        return result;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        //context will either be FragmentExample for a tablet, or EmptyActivity for phone
        parentActivity = (AppCompatActivity)context;
    }
}
