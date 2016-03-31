package com.example.lecooks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class PreContaComanda extends Activity{

	
	@Override
	 protected void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
	  setContentView(R.layout.precontacomanda);
	}
	
	public void OKCom (View v){
		
		Intent intent = new Intent(this,PreContaProdutos.class);
		startActivity(intent);
		
	}
}
