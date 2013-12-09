package com.worldpcs.tiendecitas3.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.worldpcs.tiendecitas3.R;
import com.worldpcs.tiendecitas3.activities.CommonStoreActivity;

public class StoreCommentFragment extends Fragment {



	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		CommonStoreActivity activity=(CommonStoreActivity) getActivity();
		activity.setCommentAdapter();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_store_comment,container,false);
	}

}
