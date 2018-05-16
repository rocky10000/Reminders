package com.apress.gerber.reminders;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

public class RemindersActivity extends AppCompatActivity {
    private ListView mListView;
    private RemindersDbAdapter mDbAdapter;
    private RemindersSimpleCursordapter mCursordapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminders);
        mListView=findViewById(R.id.reminds_list_view);
        mListView.setDivider(null);
        mDbAdapter=new RemindersDbAdapter(this);
        mDbAdapter.open();
        if(savedInstanceState==null)
        {
            //Clear all data
            mDbAdapter.deletellReminders();
            //Add some data
            insertSomeReminders();
        }

        Cursor cursor=mDbAdapter.fetchAllReminders();

        //from columns defined in the db
        String[] from=new String[]{RemindersDbAdapter.COL_CONTENT};
        //to the ids of views in the layout
        int[] to=new int[]{R.id.row_text};

        mCursordapter=new RemindersSimpleCursordapter(this
        ,R.layout.reminders_row
        ,cursor
        ,from
        ,to
        ,0);

        //The arrayAdapter is the controller in our model-view-controller relationship.(controller)
        //ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(this,R.layout.reminders_row,R.id.row_text,new String[]{"First record","second record","third record"});
        //mListView.setAdapter(arrayAdapter);

        //the cursorAdapter(controller) is now updating the listView(view) with data from the db (model)
        mListView.setAdapter(mCursordapter);
    }

    private void insertSomeReminders() {
        mDbAdapter.createReminder("Buy Learn Android",true);
        mDbAdapter.createReminder("Send Dad birthday gift",false);
        mDbAdapter.createReminder("Dinner at the Gage on Friday",false);
        mDbAdapter.createReminder("String squash racket",false);
        mDbAdapter.createReminder("Shovel and salt walkways",false);
        mDbAdapter.createReminder("Prepare Advanced Android syllabus",true);
        mDbAdapter.createReminder("Buy new office chair",false);
        mDbAdapter.createReminder("Call Auto-body shop for quote",false);
        mDbAdapter.createReminder("Renew membership to club",false);
        mDbAdapter.createReminder("Buy new Galasy Android phone",true);
        mDbAdapter.createReminder("Sell old Android phone - auction",false);
        mDbAdapter.createReminder("Buy new paddles for kayaks",false);
        mDbAdapter.createReminder("Call accountant about tax returns",false);
        mDbAdapter.createReminder("Buy 300,000 shares of Google",false);
        mDbAdapter.createReminder("Call the Dalai Lama back",true);
    }
    //Reminder of the class listing omitted for brevity

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_reminders,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_new:
                //create new Reminder
                Log.d(getLocalClassName(), "Create new Reminder");
                break;
            case R.id.action_exit:
                finish();
                break;
            default:
                break;
        }
        return true;
    }
}
