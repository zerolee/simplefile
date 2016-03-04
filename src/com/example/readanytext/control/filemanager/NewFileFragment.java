package com.example.readanytext.control.filemanager;



import com.example.readanytext.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class NewFileFragment extends DialogFragment {
	@SuppressWarnings("unused")
	private static final String TAG = "NewFileFragment"; 
	@SuppressLint("InflateParams")
	@Override
	@NonNull
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		boolean isFile = getArguments().getBoolean("isFile");
		String title;
		String hint;
		String miniTitle;
		String filename = getArguments().getString("filename", null);
		if (filename != null) {
			title = getString(R.string.rename);
			miniTitle = getString(R.string.newName);
			hint = "";
		} else if(isFile) {
			title = getString(R.string.newFile);
			hint = getString(R.string.newFile);
			miniTitle = getString(R.string.pleaseInput, getString(R.string.filename));
		} else {
			title = getString(R.string.newDirectory);
			hint = getString(R.string.newDirectory);
			miniTitle = getString(R.string.pleaseInput, getString(R.string.filedirectoryname));
		}
		
		filename = getArguments().getString("filename", title);
		
		View v = getActivity().getLayoutInflater().inflate(R.layout.input_fragment, null);
		TextView textTitle = (TextView)v.findViewById(R.id.input_fragment_title);
		textTitle.setText(miniTitle);
		final EditText textEdit = (EditText) v.findViewById(R.id.input_fragment_edit);
		textEdit.setHint(hint);
		textEdit.setText(filename);
		textEdit.setSelectAllOnFocus(true);
		return new AlertDialog.Builder(getActivity())
		.setTitle(title)
		.setView(v)
		.setPositiveButton(getString(R.string.OK), new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String getFilename = textEdit.getText().toString();
				if (TextUtils.isEmpty(getFilename)){
					Toast.makeText(getActivity(), "文件名不能为空", Toast.LENGTH_SHORT).show();
				} else {
					sendResult(Activity.RESULT_OK, getFilename);
				}
				
			}
		})
		.setNegativeButton(getString(R.string.CANCEL), new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				sendResult(Activity.RESULT_CANCELED, null);
			}
		})
		.create();
	}

	public static NewFileFragment newInstance(boolean isFile, String filename) {
		Bundle args = new Bundle();
		args.putBoolean("isFile", isFile);
		args.putString("filename", filename);
		NewFileFragment fragment = new NewFileFragment();
		fragment.setArguments(args);
		return fragment;
	}
	
	private void sendResult(int result, String value) {
		if (getTargetFragment() == null) {
			return;
		}
		Intent i = new Intent();
		
		i.putExtra(FileManagerFragment.EXTRA_FILENAME, value);
		getTargetFragment().onActivityResult(getTargetRequestCode(), result, i);
	}

}
