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

public class SenhaDataSource {
	
	// Campos da tabela
		  private SQLiteDatabase database;
		  private SenhaHelper dbHelper;
		  private String[] allColumns = { SenhaHelper.COLUMN_ID,
		      SenhaHelper.COLUMN_USUARIO, SenhaHelper.COLUMN_SENHA};
		  
		  private Context con;
		  private String DATABASE_PATH;
		  File dbProduto = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/LeCooks/garcom.db");
		  File InternalDB;

		  public SenhaDataSource(Context context) {		  
			  con = context;
		  }

		@SuppressWarnings("deprecation")
		public void open() throws SQLException {
			  dbHelper = new SenhaHelper(con);
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

		  public void criaSenha(String usuario, String senha) {
		    ContentValues values = new ContentValues();
		    values.put(SenhaHelper.COLUMN_USUARIO, usuario);
		    values.put(SenhaHelper.COLUMN_SENHA, senha);
		    long insertId = database.insert(SenhaHelper.TABLE_SENHAS, SenhaHelper.COLUMN_ID,
		        values);
		    Cursor cursor = database.query(SenhaHelper.TABLE_SENHAS,
		        allColumns, SenhaHelper.COLUMN_ID + " = " + insertId, null,
		        null, null, null);
		    cursor.moveToFirst();
		    cursor.close();
		  }

		  public void deletaSenha(String senha) {
		    database.delete(SenhaHelper.TABLE_SENHAS, SenhaHelper.COLUMN_SENHA
		        + " = " + senha, null);
		  }

		
		public Cursor getUsuarioSenha(String Catg) {
	   
		    Cursor cursor = database.query(SenhaHelper.TABLE_SENHAS, allColumns, "senha = '" + Catg + "'", null, null, null, null);
		    return cursor;
		  
		}

}
