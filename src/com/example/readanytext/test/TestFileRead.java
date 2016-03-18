package com.example.readanytext.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

import com.example.readanytext.utils.FileUtils;
import com.example.readanytext.utils.ProcessFileUtils;


public class TestFileRead {
	@Test
	public void test1(){
	System.out.println(System.getProperty("user.dir"));	
	String content = FileUtils.getStringFromFile("src/com/example/readanytext/test/TestFileRead.java");
    //System.out.println(content);
	String keyword = 
			"(\\bpublic\\b)|(\\bprotected\\b)|(\\bprivate\\b)" +
			"|(\\bstatic\\b)|(\\bfinal\\b)|(\\bextends\\b)" +
			"|(\\bimplements\\b)|(\\bsuper\\b)|(\\bthis\\b)" +
			"|(\\bnull\\b)|(\\btrue\\b)|(\\bfalse\\b)" +
			"|(\\breturn\\b)|(\\bnew\\b)|(\\bvoid\\b)" +
			"|(\\bint\\b)|(\\blong\\b)|(\\bdouble\\b)" +
			"|(\\bbyte\\b)|(\\bboolean\\b)|(\\bimport\\b)" +
			"|(\\bpackage\\b)|(\\btry\\b)|(\\bcatch\\b)" +
			"|(\\bfinally\\b)|(\\bif\\b)|(\\bfor\\b)" +
			"|(\\bwhile\\b)|(\\belse\\b)|(\\bswitch\\b)" +
			"|(\\bcase\\b)|(\\bbreak\\b)|(\\bcontinue\\b)" +
			"|(\\bclass\\b)";

	Pattern p1 = Pattern.compile(keyword);
	   Matcher m1 = p1.matcher(content);
	   while (m1.find()){
		   content = content.replaceAll("\\b"+ m1.group()+ "\\b", "<font color=red>"+m1.group()+"<font>");
	   }
	   
	//   System.out.println(content);
	//String result = "public static void main(String[] args)";
	//System.out.println(result.replaceAll("\\bpublic\\b", "<font color=red>public</font>"));
	   //File f = new File("src/com/example/readanytext/test/TestFileRead.java");
	  // System.out.println(f.getName());
	//   System.out.println(f.getAbsolutePath());
//	   System.out.println("hello.java".split("\\.")[1]);
	   System.out.println(ProcessFileUtils.getProcessStringFromFile("src/com/example/readanytext/test/TestedByRead.java"));
	}
	
	
}
