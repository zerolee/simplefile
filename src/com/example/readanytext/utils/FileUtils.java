package com.example.readanytext.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.net.Uri;
import android.util.Log;

public class FileUtils {
	// 获取文件
	public static String getStringFromFile(String filename) {
		Log.d("FileUtils", filename);
		return getStringFromFile(new File(filename));
	}

	public static String getStringFromFile(Uri uri) {
		return getStringFromFile(uri.toString().substring(7));
	}

	public static String getStringFromFile(File file) {
		FileInputStream in = null;
		BufferedReader reader = null;
		StringBuilder content = new StringBuilder();
		try {
			in = new FileInputStream(file);
			reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			String line = "";
			while ((line = reader.readLine()) != null) {
				content.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (reader != null) {
					reader.close();
					reader = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return content.toString();
	}

	// 写入文件
	public static void saveStringToFile(String filecontent, File file) {
		FileOutputStream out = null;
		BufferedWriter writer = null;
		try {
			out = new FileOutputStream(file);
			writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
			writer.write(filecontent);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
			if (writer != null) {
				writer.close();
				writer = null;
			}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// 创建文件
	public static void createFile(String filename) {
		File file = new File(filename);
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 创建文件夹
	public static void createDirectory(String directoryname) {
		File directory = new File(directoryname);
		directory.mkdir();
	}

	// 删除文件
	public static void deleteFile(File file) {
		file.delete();
	}

	// 重命名
	public static void renameFile(File file, File renamefile) {
		file.renameTo(renamefile);
	}

	// 复制
	public static void copyFile(File file, File newFile) {
		saveStringToFile(getStringFromFile(file), newFile);
	}
	// 获取文件阅读进度
	// 保存文件阅读进度
}
