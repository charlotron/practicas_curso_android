package com.worldpcs.tiendecitas4.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.worldpcs.tiendecitas4.R;
import com.worldpcs.tiendecitas4.data.adapters.GalleryFragmentAdapter;

public class GalleryFragment extends Fragment {
	private ViewPager vp;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		vp.setAdapter(new GalleryFragmentAdapter(getChildFragmentManager()));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState){
		//Genero el viewPager
		View v = inflater.inflate(R.layout.fragment_gallery,container,false);
		vp=(ViewPager) v.findViewById(R.id.gallery_view_pager);
		return v;
	}
}
