package com.worldpcs.tiendecitas2.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.worldpcs.tiendecitas2.R;
import com.worldpcs.tiendecitas2.activities.CommentsActionBarActivity;

public class ShopCommentFragment extends Fragment {
	


	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);	
		CommentsActionBarActivity activity=(CommentsActionBarActivity) getActivity();
		activity.setCommentAdapter();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_shop_comment,container,false);
	}

}
