package com.example.lecooks;

import android.content.Context;
import android.database.Cursor;
import android.widget.SimpleCursorTreeAdapter;

public class ListaExpAdapter extends SimpleCursorTreeAdapter {
	
	Context con;
	Cursor cursor;
	
	public ListaExpAdapter (Cursor cursor, Context context,int groupLayout, 
	        int childLayout, String[] groupFrom, int[] groupTo, String[] childrenFrom, 
	        int[] childrenTo){
		
		super(context, cursor, groupLayout, groupFrom, groupTo,
                childLayout, childrenFrom, childrenTo);
		
		con = context;
	}

	@Override
	protected Cursor getChildrenCursor(Cursor groupCursor) {
		// TODO Auto-generated method stub
		CompDataSource comp = new CompDataSource(con);
		comp.open();
		
		Cursor cursor = comp.getComplProd(Integer.parseInt(groupCursor.getString(groupCursor.getColumnIndex("_id"))));
		return cursor;
	}

}
