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
