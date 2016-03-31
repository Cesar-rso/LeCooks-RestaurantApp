package com.example.lecooks;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

public class CompDataSource {
	
	// Campos da tabela
	  private SQLiteDatabase database;
	  private CompHelper dbHelper;
	  private String[] allColumns = { CompHelper.COLUMN_ID,
	      CompHelper.COLUMN_DESCRICAO, CompHelper.COLUMN_QTD, CompHelper.COLUMN_CAT};
	  private String[] allColumnsProd = {CompHelper.ID_COMP,CompHelper.COMP_DESC,CompHelper.COMP_PRODID};
	  
	  private Context con;
	  private String DATABASE_PATH;
	  File dbProduto = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/LeCooks/garcom.db");
	  File InternalDB;

	  public CompDataSource(Context context) {		  
		  con = context;
	  }

	@SuppressWarnings("deprecation")
	public void open() throws SQLException {
		  dbHelper = new CompHelper(con);
		  database = dbHelper.getWritableDatabase();
		  dbHelper.onCreate(database);
		  DATABASE_PATH = database.getPath();
		  InternalDB = new File(DATABASE_PATH);
		  if(!database.isOpen()){
			  AlertDialog dialog = new AlertDialog.Builder(con).create();
	    		dialog.setMessage("Erro ao abrir banco de dados!");
	    		dialog.setButton("Ok", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
	    		dialog.show();
		  }
		  
	  }
	
	public void checarDB (){
		if(dbProduto.exists()){
			close();
			 boolean checkDelete = InternalDB.delete();
			 if(checkDelete){
				 try {
					copyDataBase();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 }
		  }
		open();
	}
	  
	  private void copyDataBase() throws IOException
	    {
		  //Arquivo com o caminho para o arquivo de banco de dados no cartão SD
		  	FileInputStream db = new FileInputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + "/LeCooks/garcom.db");
		  	
	        String outFileName = DATABASE_PATH; //String com o caminho para o arquivo de banco de dados no sistema de arquivos interno
	 
	        //Abrindo o banco de dados interno numa stream de memória
	        OutputStream myOutput = new FileOutputStream(outFileName);
	 
	        //transferindo bytes do SD para o arquivo interno
	        byte[] buffer = new byte[1024];
	        int length;
	        while ((length = db.read(buffer))>0)
	        {
	            myOutput.write(buffer, 0, length);
	            myOutput.flush();
	        }
	        //Fechando as streams de memória
	        myOutput.close();
	        db.close();
	    }

	  public void close() {
	    dbHelper.close();
	  }

	  public void criaComplemento(String descricao, int quantidade, String Categoria) {
	    ContentValues values = new ContentValues();
	    values.put(CompHelper.COLUMN_DESCRICAO, descricao);
	    values.put(CompHelper.COLUMN_QTD, quantidade);
	    values.put(CompHelper.COLUMN_CAT, Categoria);
	    long insertId = database.insert(CompHelper.TABLE_COMP, CompHelper.COLUMN_ID,
	        values);
	    Cursor cursor = database.query(CompHelper.TABLE_COMP,
	        allColumns, CompHelper.COLUMN_ID + " = " + insertId, null,
	        null, null, null);
	    cursor.moveToFirst();
	    cursor.close();
	  }
	  
	  public void criaComplementoProd (int complemento, String descricao, int produto){
		  ContentValues values = new ContentValues();
		  values.put(CompHelper.ID_COMP, complemento);
		  values.put(CompHelper.COMP_DESC, descricao);
		  values.put(CompHelper.COMP_PRODID, produto);
		  database.insert(CompHelper.TABLE_PRODCOMP,null,values);
	  }
	  
	  public void deletaTodosComplementoProd (){
		  database.delete(CompHelper.TABLE_PRODCOMP, null, null);
	  }
	  
	  public void deletaComplementoProd (int id){
		  database.delete(CompHelper.TABLE_PRODCOMP, " _id = " + id, null);
	  }

	  public void deletaComplemento(String descricao) {
	    database.delete(CompHelper.TABLE_COMP, CompHelper.COLUMN_DESCRICAO
	        + " = " + descricao, null);
	  }
	  
	  public Cursor getComplProd(int prod_id){
		  Cursor cursor = database.query( CompHelper.TABLE_PRODCOMP, allColumnsProd, "PROD_id = " + prod_id, null, null, null, null);
		  return cursor;
	  }

	
	public Cursor getComplementoCat(String Catg) {
 
	    Cursor cursor = database.query(CompHelper.TABLE_COMP, allColumns, "categoria = '" + Catg + "'", null, null, null, null);
	    return cursor;
	  
	}
	
	public Cursor getComplementoProd(String Catg) {
		 
	    Cursor cursor = database.query(CompHelper.TABLE_COMP, allColumns, "quantidade > 0 and categoria = '" + Catg + "'", null, null, null, null);
	    return cursor;
	  
	}
	
	public void AtualizaComplementoQTD (String id){
		ContentValues values = new ContentValues();
		Cursor cursor = database.query(CompHelper.TABLE_COMP, allColumns, "_id = " + id, null, null, null, null);
		cursor.moveToFirst();
		values.put(CompHelper.COLUMN_QTD, Integer.parseInt(cursor.getString(cursor.getColumnIndex("quantidade")))+1);
		database.update(CompHelper.TABLE_COMP, values, "_id = " + id, null);
		cursor.close();
		
	}
	
	public void AtualizaComplementoMenosQTD (String id){
		
		ContentValues values = new ContentValues();
		Cursor cursor = database.query(CompHelper.TABLE_COMP, allColumns, "_id = " + id, null, null, null, null);
		cursor.moveToFirst();
		if(Integer.parseInt(cursor.getString(cursor.getColumnIndex("quantidade")))>0){
			values.put(CompHelper.COLUMN_QTD, Integer.parseInt(cursor.getString(cursor.getColumnIndex("quantidade")))-1);
		}else{
			values.put(CompHelper.COLUMN_QTD, 0);
		}
		database.update(CompHelper.TABLE_COMP, values, "_id = " + id, null);
		cursor.close();
		
	}
	
	public void AtualizaTodosComplementos () {
		
		ContentValues values = new ContentValues();
		values.put(CompHelper.COLUMN_QTD, 0);
		database.update(CompHelper.TABLE_COMP, values,null, null);
	}

}
