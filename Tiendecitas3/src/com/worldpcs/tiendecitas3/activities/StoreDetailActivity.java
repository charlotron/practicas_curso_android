package com.worldpcs.tiendecitas3.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.util.Linkify;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;
import com.worldpcs.tiendecitas3.R;
import com.worldpcs.tiendecitas3.data.DataProvider;
import com.worldpcs.tiendecitas3.data.pojo.Store;
import com.worldpcs.tiendecitas3.fragments.StoreCommentFragment;

public class StoreDetailActivity extends CommonStoreActivity {
	/**
	 * Texto compartido al pulsar en "compartir"
	 */
	private String share_txt=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_detail);

		//Evitamos que se apague la pantalla mientras hacemos las practs.
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		//Recuperamos la tienda actual
		Store store=DataProvider.getInstance().getCurrentStore();
		
		//Establecemos los valores a los textviews
		TextView descTitleStore=	((TextView)findViewById(R.id.descTitleStore));
		TextView descDescStore=		((TextView)findViewById(R.id.descDescStore));
		TextView descDirStore=		((TextView)findViewById(R.id.descDirStore));
		TextView descTlfStore=		((TextView)findViewById(R.id.descTlfStore));
		TextView descTimes=			((TextView)findViewById(R.id.descTimes));
		TextView descWeb=			((TextView)findViewById(R.id.descWeb));
		TextView descEmail=			((TextView)findViewById(R.id.descEmail));


		descTitleStore.setText(	store.getName());
		descDescStore.setText(	store.getDesc());
		descDirStore.setText(	store.getAddress());
		descTlfStore.setText(	store.getTelephone());
		descTimes.setText(		store.getTimes());
		descWeb.setText(		store.getWebsite());
		descEmail.setText(		store.getEmail());


		//Cargamos la imagen para el botón para ver imagenes de la tienda
		NetworkImageView galButton=	((NetworkImageView)findViewById(R.id.galButton));
		//Le establecemos la url
		String url=store.getPhotosList().get(0).getURL();
		galButton.setDefaultImageResId(R.drawable.ic_photo);
		galButton.setImageUrl(	url,
								imageLoader);


		//Generamos el share_txt por si lo queremos compartir
		share_txt=getString(	R.string.share_store_txt,
								store.getName(),
								store.getDesc(),
								store.getWebsite(),
								store.getTelephone(),
								store.getEmail()
								);

		//Lo hacemos linkeable
		Linkify.addLinks(descTlfStore,Linkify.PHONE_NUMBERS);
		Linkify.addLinks(descWeb,Linkify.WEB_URLS);
		Linkify.addLinks(descEmail,Linkify.EMAIL_ADDRESSES);

		//Creamos el evento de llamar
		findViewById(R.id.callButton).setOnClickListener(new OnCallClickListener(this));

		//Creamos el evento de hacer click en el icono de la galería
		galButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(),ShotActivity.class);
		        startActivity(intent);
			}
	    });
		
		//Cargamos el fragmento de los comentarios con el parametro indicado
		FragmentManager fm=getSupportFragmentManager();
		StoreCommentFragment f=new StoreCommentFragment();
		Bundle args=new Bundle();
		//Le indico que los comentarios son de detalle de tienda no de "foto"
		args.putString(CommonStoreActivity.TYPE, CommonStoreActivity.DETAIL);
		f.setArguments(args);
		fm.beginTransaction().add(R.id.store_detail_comments_container, f).commit();
		fm.executePendingTransactions();
		
		//Ahora añadimos la función de enviar comentario
		ImageButton sendComment=((ImageButton)findViewById(R.id.sendDetailCommentButton));
		sendComment.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				((CommonStoreActivity) v.getContext()).sendComment();
			}
		});
		//Quitamos el focus al editext
		((EditText)findViewById(R.id.sendDetailComment)).clearFocus();
		
		//Guardamos el tipo
		comment_type=CommonStoreActivity.DETAIL;
		
		//Guardamos el editext del comentario
		et=(EditText)findViewById(R.id.sendDetailComment);
	}

	/**
	 * Marcar una tienda para llamarla
	 */
	public void callStore(){
		String tlf = ((TextView) findViewById(R.id.descTlfStore)).getText().toString();
		Toast.makeText(this, tlf, Toast.LENGTH_SHORT).show();
		tlf="tel:"+tlf;
		Intent callIntent=new Intent(Intent.ACTION_DIAL, Uri.parse(tlf));
		startActivity(callIntent);
	}
	/**
	 * Ejecutada al crear el menú
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_store_detail, menu);
		
		updateFavCount(menu);
		
		return true;
	}
	
	/**
	 * Actualiza el botón de Favoritos
	 */
	protected void updateFavCount(Menu menu) {
		//Actualizamos 
		MenuItem favourites_button= menu.findItem(R.id.action_favourite);
		favourites_button.setTitle("x"+DataProvider.getInstance().getCurrentStore().getFav_count());
	}	
	


	/**
	 * Clase que implementa el click en el botón de llamada
	 * @author Xarly
	 *
	 */
	private class OnCallClickListener implements OnClickListener{
			StoreDetailActivity instance=null;


			public OnCallClickListener(StoreDetailActivity instance) {
				super();
				this.instance = instance;
			}

			@Override
			public void onClick(View v) {
				instance.callStore();
		    }

	}
	/**
	 * Se le llama al pulsar en una opción del menú
	 */
	public boolean onOptionsItemSelected(MenuItem menu){
		//Esto ocurre cuando se pulsa sobre el botón de la imagen de la app
		switch(menu.getItemId()){
			case R.id.action_favourite:
				//Toast.makeText(this, getResources().getString(R.string.favourite_opt), Toast.LENGTH_SHORT).show();
				DataProvider.getInstance().incStoreFavCount(DataProvider.getInstance().getCurrentStoreId(), this);
				break;
			case R.id.action_share:
				//Share current text
				Intent intent=new Intent();
				intent.setAction(Intent.ACTION_SEND);
				intent.setType("text/plain");
				intent.putExtra(Intent.EXTRA_TEXT,share_txt);
				startActivity(intent);
				break;
			case R.id.action_call:
				//llamar
				callStore();
				break;
		}
		return super.onOptionsItemSelected(menu);

	}
}
