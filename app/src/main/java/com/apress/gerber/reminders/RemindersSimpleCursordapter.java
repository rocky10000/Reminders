package com.apress.gerber.reminders;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;

public class RemindersSimpleCursordapter extends SimpleCursorAdapter {
    public RemindersSimpleCursordapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
    }
    //to use a viewholder, you must override the following two methods and define a Viewholder class

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return super.newView(context, cursor, parent);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        super.bindView(view, context, cursor);
        ViewHolder holder=(ViewHolder)view.getTag();
        if(holder==null)
        {
            holder=new ViewHolder();
            holder.colImp=cursor.getColumnIndexOrThrow(RemindersDbAdapter.COL_IMPORTNT);
            holder.listTab=view.findViewById(R.id.row_tab);
            view.setTag(holder);
        }
        int id=cursor.getInt(holder.colImp);
        //Log.w("CC",String.valueOf(holder.colImp+"  "+id));
        if(id>0)
        {
            holder.listTab.setBackgroundColor(context.getResources().getColor(R.color.orange));
        }
        else
        {
            holder.listTab.setBackgroundColor(context.getResources().getColor(R.color.green));
        }
    }
    static class ViewHolder
    {
        //store the column index
        int colImp;
        //store the view
        View listTab;
    }
}
