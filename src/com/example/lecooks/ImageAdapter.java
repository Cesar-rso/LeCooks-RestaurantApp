package com.example.lecooks;


import android.content.Context;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.*;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ImageAdapter extends BaseAdapter {
	private Context context;
	private final String[] mobileValues;
	private String[] vColors; 

	public ImageAdapter(Context context, String[] mobileValues, String[] vCores) {
		this.context = context;
		this.mobileValues = mobileValues;
		this.vColors = vCores;
	}

	@SuppressWarnings("deprecation")
	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View gridView;

		if (convertView == null) {

			gridView = new View(context);

			// procura o layout do arquivo mobile.xml
			gridView = inflater.inflate(R.layout.mobile, null);

			// coloca os valores no textview
			TextView btnCategoria = (TextView) gridView
					.findViewById(R.id.grid_item_label);
			btnCategoria.setText(mobileValues[position]);
			ShapeDrawable shDraw = new ShapeDrawable(new RectShape());
	        shDraw.getPaint().setShader(new LinearGradient(40,0,40,40,
	        		Color.parseColor(vColors[position]), Color.parseColor("#FFFFFFFF"), Shader.TileMode.CLAMP));
	        btnCategoria.setBackgroundDrawable(shDraw);

			// escolhe a imagem baseado no texto selecionado
			//ImageView imageView = (ImageView) gridView
			//		.findViewById(R.id.grid_item_image);

			//String mobile = mobileValues[position];

			/*if (mobile.equals("Carnes")) {
				int resID = imageView.getResources().getIdentifier(MLow, id, pacote);
				imageView.setImageResource(resID);
			} else if (mobile.equals("Saladas")) {
				imageView.setImageResource(R.drawable.salada);
			} else if (mobile.equals("Bebidas")) {
				imageView.setImageResource(R.drawable.bebidas);
			} else if (mobile.equals("Sobremesa")) {
				imageView.setImageResource(R.drawable.sobremesa);
			} else if(mobile.equals("Pratos")){
				imageView.setImageResource(R.drawable.pratos);
			} else {
				imageView.setImageResource(R.drawable.android_logo);
			}*/
			
			/*try{	
				//int resID = gridView.getResources().getIdentifier(MLow, id, pacote);
				//imageView.setImageResource(resID);
				Resources res = new Resources(null, null, null);
				BitmapDrawable bit = new BitmapDrawable(res,Environment.getExternalStorageDirectory().getAbsolutePath()+"/downloads/all_downloads/1/Churrasco_-_reproducao.jpg");
				imageView.setImageDrawable(bit);
			}catch(Exception e){
				imageView.setImageResource(R.drawable.android_logo);
			}*/
			

		} else {
			gridView = (View) convertView;
		}

		return gridView;
	}

	@Override
	public int getCount() {
		return mobileValues.length;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

}
