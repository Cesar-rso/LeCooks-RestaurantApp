package com.example.lecooks;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Senha extends Activity{

	Button Btn1, Btn2, Btn3,
	Btn4, Btn5, Btn6, Btn7,
	Btn8, Btn9, Btn0, BtnOK, BtnCancel;
	
	TextView txtSenha;
	
	static boolean getNum = false;
	
	@SuppressWarnings("deprecation")
	@Override
	 protected void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
	  setContentView(R.layout.senha);
	  
	  Btn1 = (Button) findViewById(R.id.Btn1);
	  Btn2 = (Button) findViewById(R.id.Btn2);
	  Btn3 = (Button) findViewById(R.id.Btn3);
	  Btn4 = (Button) findViewById(R.id.Btn4);
	  Btn5 = (Button) findViewById(R.id.Btn5);
	  Btn6 = (Button) findViewById(R.id.Btn6);
	  Btn7 = (Button) findViewById(R.id.Btn7);
	  Btn8 = (Button) findViewById(R.id.Btn8);
	  Btn9 = (Button) findViewById(R.id.Btn9);
	  Btn0 = (Button) findViewById(R.id.Btn0);
	  BtnOK = (Button) findViewById(R.id.BtnOK);
	  BtnCancel = (Button) findViewById(R.id.BtnCancel);
	  
	  txtSenha = (TextView) findViewById(R.id.txtSenha);
	  
	  File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/LeCooks/config.properties");
	  if(!f.exists()){
		 // f.mkdirs();
		  AlertDialog dialog = new AlertDialog.Builder(this).create();
  		dialog.setMessage("Arquivo de configurações não encontrado!\nAplicativo será encerrado!");
  		dialog.setButton("Ok", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					android.os.Process.killProcess(android.os.Process.myPid());
				}
			});
  		dialog.show();
	  }
	  
	  SenhaDataSource senha = new SenhaDataSource(this);
	  senha.open();
	  senha.checarDB();
	  senha.close();
	  
	 }
	
	public void BotaoSenha (View v){
		
		int botaoSenha = v.getId();
		String senha = new String();
		
		switch (botaoSenha){
		
		case (R.id.Btn0):
			senha = "0";
			break;
			
		case (R.id.Btn1):
			senha = "1";
			break;
			
		case (R.id.Btn2):
			senha = "2";
			break;
			
		case (R.id.Btn3):
			senha = "3";
			break;
			
		case (R.id.Btn4):
			senha = "4";
			break;
			
		case (R.id.Btn5):
			senha = "5";
			break;
			
		case (R.id.Btn6):
			senha = "6";
			break;
			
		case (R.id.Btn7):
			senha = "7";
			break;
			
		case (R.id.Btn8):
			senha = "8";
			break;
			
		case (R.id.Btn9):
			senha = "9";
			break;
		
		}

		txtSenha.setText(txtSenha.getText() + senha);
		
	}
	
	@SuppressWarnings("deprecation")
	public void BotaoOK(View v){
		if(!getNum){
			SenhaDataSource senha = new SenhaDataSource(this);
			senha.open();
			Cursor curs = senha.getUsuarioSenha(txtSenha.getText().toString());
			if(curs.getCount()<=0){
				AlertDialog dialog = new AlertDialog.Builder(this).create();
	    		dialog.setMessage("Senha invalida!");
	    		dialog.setButton("Ok", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
	    		dialog.show();
	    		senha.close();
			}else{
				curs.moveToFirst();
				Garcom.nomeGarcom = curs.getString(curs.getColumnIndex(SenhaHelper.COLUMN_USUARIO));
				Garcom.senhaGarcom = txtSenha.getText().toString();
				senha.close();
				Intent intent = new Intent(this,Garcom.class);
				startActivity(intent);
				finish();
			}
		}else{
			Intent intent = new Intent();
			intent.putExtra("valor", txtSenha.getText());
			setResult(RESULT_OK,intent);
			finish();
		}
	}
	
	public void Cancelar(View v){
		String senhadg;
		senhadg = txtSenha.getText().toString();
		if(senhadg.length()>1){
			txtSenha.setText(senhadg.subSequence(0, senhadg.length()-1));
		}else{
			txtSenha.setText("");
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		if(!getNum){
			finish();
		}else{
			setResult(Activity.RESULT_CANCELED);
			finish();
		}
	}

}
