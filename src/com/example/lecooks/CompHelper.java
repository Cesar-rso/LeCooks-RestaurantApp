package com.example.lecooks;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class CompHelper extends SQLiteOpenHelper {
	
	public static final String TABLE_COMP = "Complemento";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_DESCRICAO = "descricao";
	public static final String COLUMN_QTD = "quantidade";
	public static final String COLUMN_CAT = "categoria";
	
	public static final String TABLE_PRODCOMP = "Prod_Complemento";
	public static final String ID_COMP = "_id";
	public static final String COMP_DESC = "descricao";
	public static final String COMP_PRODID = "PROD_id";

	private static final String DATABASE_NAME = "garcom.db";
	private static final int DATABASE_VERSION = 1;
	
	// Criando tabelas
		  private static final String TABLE_CREATE = "create table if not exists "
		      + TABLE_COMP + "(" + COLUMN_ID
		      + " integer primary key autoincrement, " + COLUMN_DESCRICAO
		      + " text not null, " +COLUMN_QTD+ " integer not null, "
		      + COLUMN_CAT +" text not null);";
		  
		  private static final String TABLE_CREATE2 = "create table if not exists "
				  + TABLE_PRODCOMP + "(" + ID_COMP + " integer primary key autoincrement, "
				  + COMP_DESC + " text not null, " + COMP_PRODID + " text not null);";

	
	public CompHelper(Context context) {
	    super(context, DATABASE_NAME, null, DATABASE_VERSION);
	  }
	
	 @Override
		public void onOpen(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			super.onOpen(db);
		}

		@Override
		  public void onCreate(SQLiteDatabase database) {
		    try {
				database.execSQL(TABLE_CREATE);
				database.execSQL(TABLE_CREATE2);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		  }
		
		@Override
		  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		    Log.w(SQLiteHelper.class.getName(),
		        "Upgrading database from version " + oldVersion + " to "
		            + newVersion + ", which will destroy all old data");
		    db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMP);
		    onCreate(db);
		  }

}
