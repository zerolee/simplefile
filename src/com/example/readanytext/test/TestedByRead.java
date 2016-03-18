package com.example.readanytext.test;
/*
 * double int
 */
import org.junit.Test;
//System.out.println(content);
import com.example.readanytext.utils.ProcessFileUtils;

public class TestedByRead {
	@Test
	public void test1(){
		
		// private static String keyword =
		// "(\\bpublic\\b)|(\\bprotected\\b)|(\\bprivate\\b)|(\\bstatic\\b)|(\\bfinal\\b)|(\\bextends\\b)|(\\bimplements\\b)|(\\bsuper\\b)|(\\bthis\\b)|(\\bnull\\b)|(\\btrue\\b)|(\\bfalse\\b)|(\\breturn\\b)|(\\bnew\\b)|(\\bvoid\\b)|(\\bint\\b)|(\\blong\\b)|(\\bdouble\\b)|(\\bbyte\\b)|(\\bboolean\\b)|(\\bimport\\b)|(\\bpackage\\b)|(\\btry\\b)|(\\bcatch\\b)|(\\bfinally\\b)|(\\bif\\b)|(\\bfor\\b)|(\\bwhile\\b)|(\\belse\\b)|(\\bswitch\\b)|(\\bcase\\b)|(\\bbreak\\b)|(\\bcontinue\\b)|(\\bclass\\b)";

	//   System.out.println(content);
	   System.out.println(ProcessFileUtils.getProcessStringFromFile("src/com/example/readanytext/test/TestFileRead.java"));
	}
	
}
