package com.example.readanytext.control.textreader;

import com.example.readanytext.R;
import com.example.readanytext.control.filemanager.FileManagerFragment;
import com.example.readanytext.utils.FileUtils;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;


public class CodeViewFragment extends Fragment {
	
	private static final String TAG = "CodeViewFragment";
	private static final int REQUEST_FILESYSTEM = 0;
	
	private TextView codeView;
	private ScrollView readSpeed;
	
	private String file;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		file = getArguments().getString(FileManagerFragment.EXTRA_FILE);
		setHasOptionsMenu(true);
	}
	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_code, container, false);
		if (NavUtils.getParentActivityName(getActivity()) != null) {
			getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
		}
		codeView = (TextView)v.findViewById(R.id.text_content);
		codeView.setText(FileUtils.getStringFromFile(file));
		readSpeed = (ScrollView) v.findViewById(R.id.read_speed);
		return v;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_code, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch (item.getItemId()) {
		case R.id.menu_item_open:
			Intent i = new Intent(Intent.ACTION_GET_CONTENT);
			i.setType("text/plain");
		//	i.addCategory(Intent.CATEGORY_OPENABLE);
			startActivityForResult(i, REQUEST_FILESYSTEM);
			return true;
		case android.R.id.home:
			getActivity().finish();
			/*
			 * if (NavUtils.getParentActivityName(getActivity()) != null) {
				NavUtils.navigateUpFromSameTask(getActivity());
			} */
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if ( resultCode != Activity.RESULT_OK)
			return;
		if ( requestCode == REQUEST_FILESYSTEM ) {
			Uri fileUri = data.getData();
			Log.d(TAG, fileUri.toString());
			codeView.setText(FileUtils.getStringFromFile(fileUri));
			
		}
	}
	
	
	@Override
	public void onResume() {
		super.onResume();
		readSpeed.post(new Runnable() {
			
			@Override
			public void run() {
				SharedPreferences sref = getActivity().getSharedPreferences(file.replace('/', '.'), 0);
				readSpeed.scrollTo(sref.getInt("X", 0),  sref.getInt("Y", 0));
			}
		});
	}
	@Override
	public void onPause() {
		super.onPause();
		SharedPreferences.Editor editor = getActivity().getSharedPreferences(file.replace('/', '.'), 0).edit();
		editor.putInt("X", readSpeed.getScrollX());
		editor.putInt("Y", readSpeed.getScrollY());
		editor.commit();
	}
	public static CodeViewFragment newInstance(String file) {
		Bundle args = new Bundle();
		args.putString(FileManagerFragment.EXTRA_FILE, file);
		CodeViewFragment fragment = new CodeViewFragment();
		fragment.setArguments(args);
		return fragment;
	}
	
	
	
}
