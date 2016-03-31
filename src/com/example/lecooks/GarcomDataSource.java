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

public class GarcomDataSource {

	// Campos da tabela
	  private SQLiteDatabase database;
	  private SQLiteHelper dbHelper;
	  private String[] allColumns = { SQLiteHelper.COLUMN_ID,
	      SQLiteHelper.COLUMN_DESCRICAO, SQLiteHelper.COLUMN_CATEGORIA, 
	      SQLiteHelper.COLUMN_PRECO, SQLiteHelper.COLUMN_QTD, SQLiteHelper.COLUMN_COMPL };
	  
	  private Context con;
	  private String DATABASE_PATH;
	  File dbProduto = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/LeCooks/garcom.db");
	  File InternalDB; 

	  public GarcomDataSource(Context context) {		  
		  con = context;
	  }

	@SuppressWarnings("deprecation")
	public void open() throws SQLException {
		  dbHelper = new SQLiteHelper(con);
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

	  public void criaProduto(String descricao, float preco, String Categoria, int qtd, String compl) {
	    ContentValues values = new ContentValues();
	    values.put(SQLiteHelper.COLUMN_DESCRICAO, descricao);
	    values.put(SQLiteHelper.COLUMN_PRECO, preco);
	    values.put(SQLiteHelper.COLUMN_CATEGORIA, Categoria);
	    values.put(SQLiteHelper.COLUMN_QTD, qtd);
	    values.put(SQLiteHelper.COLUMN_COMPL, compl);
	    long insertId = database.insert(SQLiteHelper.TABLE_PRODUTOS, SQLiteHelper.COLUMN_ID,
	        values);
	    Cursor cursor = database.query(SQLiteHelper.TABLE_PRODUTOS,
	        allColumns, SQLiteHelper.COLUMN_ID + " = " + insertId, null,
	        null, null, null);
	    cursor.moveToFirst();
	    cursor.close();
	  }

	  public void deletaProduto(String produto) {
	    database.delete(SQLiteHelper.TABLE_PRODUTOS, SQLiteHelper.COLUMN_DESCRICAO
	        + " = " + produto, null);
	  }

	
	public Cursor getProdutosCategoriaCs(String Catg) {
   
	    Cursor cursor = database.query(SQLiteHelper.TABLE_PRODUTOS, allColumns, "categoria = '" + Catg + "'", null, null, null, null);
	    return cursor;
	  
	}
	
	public Cursor getProdutosPedido() {
		   
	    Cursor cursor = database.query(SQLiteHelper.TABLE_PRODUTOS, allColumns, "quantidade > 0", null, null, null, null);
	    return cursor;
	  
	}
	
	public Cursor getProdutoComp(String descricao, String categoria){
		Cursor cursor = database.query(SQLiteHelper.TABLE_PRODUTOS, allColumns, "descricao = '" + descricao 
				+ "' and categoria = '" + categoria + "'", null, null, null, null);		
		return cursor;
	}
	
	public void AtualizaProdutoQTD(String Categoria, String descricao){
		ContentValues values = new ContentValues();
		Cursor cursor = database.query(SQLiteHelper.TABLE_PRODUTOS, allColumns, "categoria = '" + Categoria + "' and descricao = '" +descricao+ "'", 
				null, null, null, null);
		cursor.moveToFirst();
		values.put(SQLiteHelper.COLUMN_QTD, Integer.parseInt(cursor.getString(cursor.getColumnIndex("quantidade")))+1);
		database.update(SQLiteHelper.TABLE_PRODUTOS, values, "categoria = '" + Categoria + "' and descricao = '" +descricao+ "'", null);
		cursor.close();
		
	}
	
	public void AtualizaProdutoMenosQTD(String Categoria, String descricao){
		ContentValues values = new ContentValues();
		Cursor cursor = database.query(SQLiteHelper.TABLE_PRODUTOS, allColumns, "categoria = '" + Categoria + "' and descricao = '" +descricao+ "'",
				null, null, null, null);
		cursor.moveToFirst();
		if(Integer.parseInt(cursor.getString(cursor.getColumnIndex("quantidade")))>0){
			values.put(SQLiteHelper.COLUMN_QTD, Integer.parseInt(cursor.getString(cursor.getColumnIndex("quantidade")))-1);
		}else{
			values.put(SQLiteHelper.COLUMN_QTD, 0);
		}
		database.update(SQLiteHelper.TABLE_PRODUTOS, values, "categoria = '" + Categoria + "' and descricao = '" +descricao+ "'", null);
		cursor.close();
		
	}
	
	public void AtualizaTodosProdutos(){
		ContentValues values = new ContentValues();
		values.put(SQLiteHelper.COLUMN_QTD, 0);
		values.put(SQLiteHelper.COLUMN_COMPL, "");
		database.update(SQLiteHelper.TABLE_PRODUTOS, values, null, null);
		
	}
	
	public void AtualizaProdutoCompl(String Categoria, String descricao, String complemento){
		ContentValues values = new ContentValues();
		Cursor cursor = database.query(SQLiteHelper.TABLE_PRODUTOS, allColumns, "categoria = '" + Categoria + "' and descricao = '" +descricao+ "'",
				null, null, null, null);
		cursor.moveToFirst();
		if(cursor.getString(cursor.getColumnIndex("complemento"))==""||cursor.getString(cursor.getColumnIndex("complemento"))=="00"){
			values.put(SQLiteHelper.COLUMN_COMPL, complemento);
		}else{		
			values.put(SQLiteHelper.COLUMN_COMPL, cursor.getString(cursor.getColumnIndex("complemento")) + complemento);
		}
		database.update(SQLiteHelper.TABLE_PRODUTOS, values, "categoria = '" + Categoria + "' and descricao = '" +descricao+ "'", null);
	}
	
	public void AtualizaProdutoTiraCompl(String Categoria, String descricao){
		ContentValues values = new ContentValues();
		values.put(SQLiteHelper.COLUMN_COMPL, "");
		database.update(SQLiteHelper.TABLE_PRODUTOS, values, "categoria = '" + Categoria + "' and descricao = '" +descricao+ "'", null);
		
	}

}
