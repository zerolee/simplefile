package com.example.readanytext.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

import android.net.Uri;
import android.util.Log;

public class FileUtils {	
	
	// 获取文件
	public static String getStringFromFile(String filename) {
	//	Log.d("FileUtils", filename);
		return getStringFromFile(new File(filename));
	}

	public static String getStringFromFile(Uri uri) {
		return getStringFromFile(uri.toString().substring(7));
	}
	// 或许我应该准备一个二进制的读取文件
    // 内存映射文件
	// 记住读取的区间，下次来的时候继续读取
	// 设置一个总长度
	private static ByteBuffer getByteFromFileByMemoryMap(File file){
		long i = 0, size = file.length();
		MappedByteBuffer out = null;
		try {
			out = new RandomAccessFile(file, "rw").
					getChannel().map(FileChannel.MapMode.READ_WRITE, i, size);
		} catch (Exception e){
			
		}
		return out;
	}
	//
	public static String getStringFromFile(File file){
		return Charset.forName(CharSetText(file)).decode(getByteFromFileByMemoryMap(file)).toString();
	}
	
	public static byte[] getByteFromFile(File file){
		return getByteFromFileByMemoryMap(file).toString().getBytes();
	}
	//
	public static String getStringFromFile2(File file) {
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
		if (file.isFile() || file.listFiles().length == 0) {
			file.delete();
		} else {
			deleteDirectory(file);
			deleteFile(file);
		}
	}

	// 删除目录
	private static void deleteDirectory(File file) {
		for (File f : file.listFiles()) {
			deleteFile(f);
		}
	}

	// 重命名
	public static void renameFile(File file, File renamefile) {
		file.renameTo(renamefile);
	}

	// 复制
	private static void copyFileByChannel(File file, File newFile)
			throws IOException {
		if (file.isFile()) {
			FileInputStream in = null;
			FileOutputStream out = null;
			try {
				in = new FileInputStream(file);
				out = new FileOutputStream(newFile);
				FileChannel inChannel = in.getChannel(), outChannel = out
						.getChannel();
				inChannel.transferTo(0, inChannel.size(), outChannel);
			} finally {
				if (in != null) {
					in.close();
					in = null;
				}
				if (out != null) {
					out.close();
					out = null;
				}
			}
		} else {
			newFile.mkdir();
			for (File f : file.listFiles()){
				copyFileByChannel(f, new File(newFile.getCanonicalFile() +"/" + f.getName()));
			}
		}

	}

	@SuppressWarnings("unused")
	private static void copyFileBySimple(File file, File newFile) {
		byte[] buffer = null;
		if (file.length() > 1024 * 1024 * 10) {
			buffer = new byte[1024 * 1024 * 10];
		} else {
			buffer = new byte[(int) file.length()];
		}
		FileInputStream fromFile = null;
		FileOutputStream toFile = null;

		try {
			fromFile = new FileInputStream(file);
			toFile = new FileOutputStream(newFile);

			while (fromFile.available() > 0) {
				fromFile.read(buffer);
				toFile.write(buffer);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fromFile != null) {
					fromFile.close();
					fromFile = null;
				}
				if (toFile != null) {
					toFile.close();
					toFile = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void copyFile(File file, File newFile) throws IOException {
		copyFileByChannel(file, newFile);
	}
	// 获取文件阅读进度
	// 保存文件阅读进度
	
	
	/*
	 * 判断文件的编码格式
	 * @return 文件编码格式
	 */
	private static String CharSetText(File file){
		BufferedInputStream bis = null;
		int p = 0, p1 , p2, p3, p4;
		try {
			bis = new BufferedInputStream(new FileInputStream(file));
			p1 = bis.read();
			p2 = bis.read();
			p3 = bis.read();
			p4 = bis.read();
			p = (p1 << 8) + p2;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (bis != null){
				try {
					bis.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}

		if (((p1 >>> 7) == 0 )
				|| ((p1 >>> 5) == 6 && (p2 >>> 6) == 2)
				|| ((p1 >>> 4) == 15 && (p2 >>> 6) == 2 && (p3 >>> 6) == 2)
				|| ((p1 >>> 3) == 31 && (p2 >>> 6) == 2 && (p3 >>> 6) == 2 &&(p4 >>> 6) == 2)){
			return "UTF-8";
		}
		
		switch (p) {
		case 0xefbb:
			return "UTF-8";
		case 0xfffe:
			return "Unicode";
		case 0xfeff:
			return "UTF-16BE";
		default:
			return "GBK";
		}
	}
}

/*
 * UTF-8
 * 0xxxxxxx
 * 110xxxxx 10xxxxxx
 * 1110xxxx 10xxxxxx 10xxxxxx
 * 11110xxx 10xxxxxx 10xxxxxx 10xxxxxxx
 */