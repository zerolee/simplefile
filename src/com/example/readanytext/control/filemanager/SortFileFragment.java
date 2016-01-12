package com.example.readanytext.control.filemanager;

import com.example.readanytext.R;
import com.example.readanytext.model.FilenameLab;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class SortFileFragment extends DialogFragment {
	public static final String EXTRA_SORT_STYLE = "com.example.readanytext.filemanager.DialogFragment";
	private int sort_style;
	private RadioButton sortByName;
	private RadioButton sortByTime;
	private RadioButton sortBySize;
	private RadioGroup sortFileSet;

	@SuppressLint("InflateParams")
	@Override
	@NonNull
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		sort_style = getArguments().getInt("style");
		View v = getActivity().getLayoutInflater().inflate(
				R.layout.dialog_sortfiles, null);
		sortByName = (RadioButton) v.findViewById(R.id.sortfile_by_name);
		sortByTime = (RadioButton) v.findViewById(R.id.sortfile_by_time);
		sortBySize = (RadioButton) v.findViewById(R.id.sortfile_by_size);
		sortFileSet = (RadioGroup) v.findViewById(R.id.sortfile_set);

		switch (sort_style) {
		case FilenameLab.SORT_BY_NAME:
			sortByName.setChecked(true);
			break;
		case FilenameLab.SORT_BY_TIME:
			sortByTime.setChecked(true);
			break;
		case FilenameLab.SORT_BY_SIZE:
			sortBySize.setChecked(true);
			break;
		default:
			break;
		}

		sortFileSet.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.sortfile_by_name:
					sort_style = FilenameLab.SORT_BY_NAME;
					break;
				case R.id.sortfile_by_time:
					sort_style = FilenameLab.SORT_BY_TIME;
					break;
				case R.id.sortfile_by_size:
					sort_style = FilenameLab.SORT_BY_SIZE;
					break;
				default:
					break;
				}
				if (getTargetFragment() != null) {
					Intent i = new Intent();
					i.putExtra(EXTRA_SORT_STYLE, sort_style);
					getTargetFragment().onActivityResult(
							getTargetRequestCode(), Activity.RESULT_OK, i);
				}

			}
		});
		return new AlertDialog.Builder(getActivity()).setTitle("排序方式")
				.setView(v).create();
	}

	public static SortFileFragment newInstance(int style) {
		Bundle bundle = new Bundle();
		bundle.putInt("style", style);
		SortFileFragment fragment = new SortFileFragment();
		fragment.setArguments(bundle);
		return fragment;
	}
}
