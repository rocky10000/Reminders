package com.apress.gerber.reminders;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class RemindersActivity extends AppCompatActivity {
    private ListView mListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView=findViewById(R.id.reminds_list_view);
        //The arrayAdapter is the controller in our model-view-controller relationship.(controller)
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(this,R.layout.reminders_row,R.id.row_text,new String[]{"First record","second record","third record"});
        mListView.setAdapter(arrayAdapter);
    }
    //Reminder of the class listing omitted for brevity
}
