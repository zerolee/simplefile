package com.example.readanytext.control.textreader;

import com.example.readanytext.R;
import com.example.readanytext.control.filemanager.FileManagerFragment;
import com.example.readanytext.utils.FileUtils;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.text.Html;
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
		new FetchItemTask().execute();
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
		codeView.setText("");
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
	
	
	private class FetchItemTask extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			
			return FileUtils.getStringFromFile(file);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			//codeView.setText(result);
			result = result.replaceAll("\\bpublic\\b", "<font color=red>public</font>");
			result = result.replaceAll("\\bprotected\\b", "<font color=red>protected</font>");
			result = result.replaceAll("\\bprivate\\b", "<font color=red>private</font>");
			
			result = result.replaceAll("\\bstatic\\b", "<font color=red>static</font>");
			result = result.replaceAll("\\bfinal\\b", "<font color=red>final</font>");
			result = result.replaceAll("\\bextends\\b", "<font color=red>extends</font>");
			result = result.replaceAll("\\bimplements\\b", "<font color=red>implements</font>");
			result = result.replaceAll("\\bsuper\\b", "<font color=red>super</font>");
			result = result.replaceAll("\\bthis\\b", "<font color=red>this</font>");
			
			result = result.replaceAll("\\bnull\\b", "<font color=red>null</font>");
			
			result = result.replaceAll("\\btrue\\b", "<font color=red>true</font>");
			result = result.replaceAll("\\bfalse\\b", "<font color=red>false</font>");
			
			result = result.replaceAll("\\breturn\\b", "<font color=red>return</font>");
			result = result.replaceAll("\\bnew\\b", "<font color=red>new</font>");
			
			result = result.replaceAll("\\bvoid\\b", "<font color=red>void</font>");
			result = result.replaceAll("\\bint\\b", "<font color=red>int</font>");
			result = result.replaceAll("\\blong\\b", "<font color=red>long</font>");
			result = result.replaceAll("\\bdouble\\b", "<font color=red>double</font>");
			result = result.replaceAll("\\bbyte\\b", "<font color=red>byte</font>");
			result = result.replaceAll("\\bboolean\\b", "<font color=red>boolean</font>");
			
			result = result.replaceAll("\\bimport\\b", "<font color=red>import</font>");
			result = result.replaceAll("\\bpackage\\b", "<font color=red>package</font>");
			
			result = result.replaceAll("\\btry\\b", "<font color=red>try</font>");
			result = result.replaceAll("\\bcatch\\b", "<font color=red>catch</font>");
			result = result.replaceAll("\\bfinally\\b", "<font color=red>finally</font>");
			
			result = result.replaceAll(" ", "&nbsp;");
			result = result.replaceAll("\t", "&nbsp;&nbsp;&nbsp;&nbsp;");
			result = result.replaceAll("\r", "<br/>");
			
			
			
			
			result = result.replaceAll("\\bif\\b", "<font color=red>if</font>");
			result = result.replaceAll("\\bfor\\b", "<font color=red>for</font>");
			result = result.replaceAll("\\bwhile\\b", "<font color=red>while</font>");
			result = result.replaceAll("\\belse\\b", "<font color=red>else</font>");
			result = result.replaceAll("\\bswitch\\b", "<font color=red>switch</font>");
			result = result.replaceAll("\\bcase\\b", "<font color=red>case</font>");
			result = result.replaceAll("\\bbreak\\b", "<font color=red>break</font>");
			result = result.replaceAll("\\bcontinue\\b", "<font color=red>continue</font>");
			result = result.replaceAll("\\bclass\\b", "<font color=red>class</font>");
			
			result = result.replaceAll("\n", "<br/>");
			
			//
			
			codeView.setText(Html.fromHtml(result));
		}
		
		
	}
	public static CodeViewFragment newInstance(String file) {
		Bundle args = new Bundle();
		args.putString(FileManagerFragment.EXTRA_FILE, file);
		CodeViewFragment fragment = new CodeViewFragment();
		fragment.setArguments(args);
		return fragment;
	}
	
	
	
}
