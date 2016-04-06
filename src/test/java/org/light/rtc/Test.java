package org.light.rtc;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.TreeMap;

import org.light.rtc.util.Constants;
import org.light.rtc.util.Murmurs;

public class Test {
	
	public void test1(){
//		char c = 'A';
//
//		System.out.println(c-64);
//		String str = "A000";
//		System.out.println(str.charAt(0)-64);
//		char[] arr = {'T','U','V'};
//		for(int a : arr){
//			System.out.println(a);
//		}
		String testStr = "light-2016-bsfas";
		long begin = System.nanoTime();
		long begin1 = System.currentTimeMillis();
		System.out.println(Murmurs.getCharSum(testStr)%2);
		System.out.println(Murmurs.getCharSum("light-2016-asfas－dffd")%2);
		long rtNum = Murmurs.hashUnsigned(testStr).longValue();
		System.out.println(rtNum +"\t"+rtNum%2);
		testStr = "light20160424王";
		rtNum = Murmurs.hashUnsigned(testStr).longValue();
		System.out.println(rtNum +"\t"+ rtNum%2);
		rtNum = Murmurs.hashUnsigned(testStr).longValueExact();
		System.out.println(rtNum +"\t"+ rtNum%2);
		long end = System.nanoTime();
		long end1 = System.currentTimeMillis();
//		System.out.println(sum+"\t"+rt+"\t"+(double)(end-begin)/(1000*1000*1000)+"\t"+(double)(end1-begin1)/1000);
		System.out.println((double)(end-begin)/(1000*1000*1000)+"\t"+(double)(end1-begin1)/1000);
		long bg = System.currentTimeMillis();
		long curTime = (long)20160331182835.0;
		long afterTime = 20160331182835L;
		System.out.println(curTime+"\t"+Long.MAX_VALUE+"\t"+Integer.MAX_VALUE);
		
		System.out.println((int)Math.ceil(7.0/3));
		TreeMap<Long,Integer> delayTasks = new TreeMap<Long,Integer>();
		delayTasks.put(2016040322103901L, 234);
		delayTasks.put(2016040321503901L, 345);
		delayTasks.put(2016040321453901L, 456);
		delayTasks.put(2016040320453901L, 367);
		System.out.println(delayTasks.keySet()+"\t"+delayTasks.firstKey()+"\t"+delayTasks.pollFirstEntry());
	}
	
//	private TreeMap<Long,Integer> delayTasks = new TreeMap<Long,Integer>();
//	
//	public void writeFile(){
//		try {
//			BufferedWriter bwr = Files.newBufferedWriter(Paths.get(Constants.delayTaskDir+delayTasks.firstKey()+".txt"), StandardCharsets.UTF_8);
//			bwr.write("123");
//			bwr.write("test");
//			bwr.write("lrtdc");
//			bwr.flush();
//			bwr.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//	
//	public void readFile(){
//		List<String> datas = null;
//		try {
//			datas  = Files.readAllLines(Paths.get(Constants.delayTaskDir+delayTasks.firstKey()+".txt"), StandardCharsets.UTF_8);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}  
//		System.out.println(datas);
//	}

//	public void testWRFile(){
//		delayTasks.put(2016040322103901L, 234);
//		delayTasks.put(2016040321503901L, 345);
//		delayTasks.put(2016040321453901L, 456);
//		delayTasks.put(2016040320453901L, 367);
//		this.writeFile();
//		this.readFile();
//	}
	
	public static void main(String[] args) {
		Test t = new Test();
		t.test1();
//		t.testWRFile();
	}

}
