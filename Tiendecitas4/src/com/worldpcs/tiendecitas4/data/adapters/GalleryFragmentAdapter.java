package com.worldpcs.tiendecitas4.data.adapters;

import com.worldpcs.tiendecitas4.fragments.GalleryFragmentImage;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class GalleryFragmentAdapter extends FragmentPagerAdapter {

	public static final int NUM_IMAGES=6;
	
	public GalleryFragmentAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int pos) {
		//Generamos el fragmento
		GalleryFragmentImage fi=new GalleryFragmentImage();
		Bundle args=new Bundle();
		args.putInt(GalleryFragmentImage.GALLERY_IMAGE_KEY, pos);
		fi.setArguments(args);
		return fi;
	}

	@Override
	public int getCount() {
		return NUM_IMAGES;
	}

}
