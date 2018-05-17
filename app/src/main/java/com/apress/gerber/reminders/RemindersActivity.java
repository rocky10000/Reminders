package com.apress.gerber.reminders;

import android.annotation.TargetApi;
import android.support.v7.app.ActionBar;
import android.app.Dialog;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class RemindersActivity extends AppCompatActivity {
    private ListView mListView;
    private RemindersDbAdapter mDbAdapter;
    private RemindersSimpleCursordapter mCursordapter;


    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminders);
        mListView=findViewById(R.id.reminds_list_view);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.mipmap.ic_launcher);

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

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,final int masterListposition, long id) {
                Toast.makeText(RemindersActivity.this, "clicked "+masterListposition, Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder=new AlertDialog.Builder(RemindersActivity.this);
                ListView modeListView=new ListView(RemindersActivity.this);
                String[] modes=new String[]{"Edit Reminder","Delete Reminder"};
                ArrayAdapter<String> modeAdapter=new ArrayAdapter<>(RemindersActivity.this
                ,android.R.layout.simple_list_item_1,android.R.id.text1,modes);
                modeListView.setAdapter(modeAdapter);
                builder.setView(modeListView);
                final Dialog dialog=builder.create();
                dialog.show();
                modeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //edit reminder
                        if(position==0)
                        {
                            int nId=getIdFromPosition(masterListposition);
                            Reminder reminder=mDbAdapter.fetchReminderById(nId);
                            fireCustomDialog(reminder);
                            //delete reminder
                        }else{
                            mDbAdapter.deleteReminderById(getIdFromPosition(masterListposition));
                            mCursordapter.changeCursor(mDbAdapter.fetchAllReminders());
                        }
                        dialog.dismiss();
                    }
                });
            }
        });
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB)
        {
            mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
            mListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
                @Override
                public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

                }

                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    MenuInflater inflater=mode.getMenuInflater();
                    inflater.inflate(R.menu.cam_menu,menu);
                    return true;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    switch (item.getItemId())
                    {
                        case R.id.menu_item_delete_remind:
                            for(int nC=mCursordapter.getCount()-1;nC>=0;nC--)
                            {
                                if(mListView.isItemChecked(nC))
                                {
                                    mDbAdapter.deleteReminderById(getIdFromPosition(nC));
                                }
                            }
                            mode.finish();
                            mCursordapter.changeCursor(mDbAdapter.fetchAllReminders());
                            return true;
                    }
                    return false;
                }

                @Override
                public void onDestroyActionMode(ActionMode mode) {

                }
            });
        }
    }
    private int getIdFromPosition(int nC)
    {
        return (int)mCursordapter.getItemId(nC);
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

    private void fireCustomDialog(final Reminder reminder)
    {
        //custom dialog
        final Dialog dialog=new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_custom);

        TextView titleView=dialog.findViewById(R.id.custom_title);
        final EditText editCustom=dialog.findViewById(R.id.custom_edit_reminder);
        Button commitButton=dialog.findViewById(R.id.custom_button_commit);
        final CheckBox checkBox=dialog.findViewById(R.id.custom_check_box);
        LinearLayout rootLayout=dialog.findViewById(R.id.custom_root_layout);
        final boolean isEditOperation=reminder!=null;
        //this is for an edit
        if(isEditOperation)
        {
            titleView.setText("Edit Reminder");
            checkBox.setChecked(reminder.getImportant()==1);
            editCustom.setText(reminder.getContent());
            rootLayout.setBackgroundColor(getResources().getColor(R.color.blue));
        }
        commitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String  reminderText=editCustom.getText().toString();
                if(isEditOperation)
                {
                    Reminder reminderEdited=new Reminder(reminder.getId(),reminderText,checkBox.isChecked()?1:0);
                    mDbAdapter.updateReminder(reminderEdited);
                }else
                {
                    //this is for new reminder
                    mDbAdapter.createReminder(reminderText,checkBox.isChecked());
                }
                mCursordapter.changeCursor(mDbAdapter.fetchAllReminders());
                dialog.dismiss();
            }
        });
        Button buttonCancel=dialog.findViewById(R.id.custom_button_cancel);
        buttonCancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
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
                fireCustomDialog(null);
                break;
            case R.id.action_exit:
                finish();
                break;
            default:
                return false;
        }
        return true;
    }
}
