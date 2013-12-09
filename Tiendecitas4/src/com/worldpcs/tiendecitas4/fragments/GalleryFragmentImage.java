package com.worldpcs.tiendecitas4.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.worldpcs.tiendecitas4.R;

public class GalleryFragmentImage extends Fragment {
	public static final String GALLERY_IMAGE_KEY="gal_img_key";
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState){
		//Generamos todo el layout y lo recuperamos para modificarlo
		View v= inflater.inflate(R.layout.fragment_gallery_image,container,false);
		ImageView iv=(ImageView) v.findViewById(R.id.gallery_image_item);
		
		//Recuperamos la id de la imagen y la mostramos
		String image = "gal"+getArguments().getInt(GALLERY_IMAGE_KEY);
		
		int resource=getResources().getIdentifier(	image, 
													"drawable", 
													v.getContext().getPackageName());
		iv.setImageResource(resource);
		 
		//Recuperamos la vista
		return v;
	}
}
