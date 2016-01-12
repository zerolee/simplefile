package com.example.readanytext.model;

import java.io.File;
import java.util.ArrayList;
import com.example.readanytext.utils.FileManagerUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class FilenameLab {
	public static final int SORT_BY_NAME = 0;
	public static final int SORT_BY_TIME= 1;
	public static final int SORT_BY_SIZE= 2;
	private static final String TAG = "FilenameLab";
	private File current = null;
	private File selectFile = null;
	private Context mAppContext;
	private boolean showHidden;
	private int sortStyle;

	// 文件系统
	private static FilenameLab sFilenameLab;
	private ArrayList<File> files = new ArrayList<>();

	private FilenameLab(Context appContext) {
		mAppContext = appContext;
		// 是否显示隐藏文件 默认不显示 
		SharedPreferences pref = appContext.getSharedPreferences("setting", 0);
		showHidden = pref.getBoolean("showHidden", false);
		// 文件排序方式 默认按名称排序
		sortStyle = pref.getInt("sort", SORT_BY_NAME);
		
	}

	public static FilenameLab get(Context c) {
		if (sFilenameLab == null) {
			sFilenameLab = new FilenameLab(c.getApplicationContext());
		}
		return sFilenameLab;
	}

	public ArrayList<File> getFiles() {
		return files;
	}

	public FilenameLab updateFiles(File directory) {
		current = directory;

		if (!files.isEmpty()) {
			files.clear();
		}
		for (File file : directory.listFiles()) {
			if ( !showHidden && file.isHidden()) 
				continue;
			files.add(file);
		}

		FileManagerUtils.filesort(files, sortStyle);
		return sFilenameLab;
	}

	public File getCurrentfile() {
		return current;
	}
	
	public boolean isShowHidden() {
		return showHidden;
	}
	
	
	public void setHidden() {
		showHidden = !showHidden;
	}
	public int getSortStyle(){
		return sortStyle;
	}
	
	public void setSortStyle(int sortStyle) {
		this.sortStyle = sortStyle;  
	}
	
	public void saveSetting(){
		Log.d(TAG, "showHidden " + showHidden);
		SharedPreferences.Editor editor = mAppContext.getSharedPreferences("setting", 0).edit();
		editor.putInt("sort", sortStyle);
		editor.putBoolean("showHidden", showHidden);
		editor.commit();
		Log.d(TAG, "showHidden 2" + showHidden);
	}

	public File getSelectFile() {
		return selectFile;
	}

	public void setSelectFile(File selectFile) {
		this.selectFile = selectFile;
	}
}
