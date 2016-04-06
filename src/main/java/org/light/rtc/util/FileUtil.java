package org.light.rtc.util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class FileUtil {
	
	private String newLineSep = "\n";
	
	public void writeActions(List<String> userActions, String fileDir){
		try {
			BufferedWriter bwr = Files.newBufferedWriter(Paths.get(fileDir), StandardCharsets.UTF_8);
			for(String ua : userActions){
				bwr.write(ua+newLineSep);
			}
			bwr.flush();
			bwr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public List<String> readActions(String fileDir){
		List<String> datas = null;
		try {
			datas = Files.readAllLines(Paths.get(fileDir), StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return datas;
	}
	
	public void delActionFile(String fileDir){
		try {
			Files.delete(Paths.get(fileDir));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

//	public static void main(String[] args) {
//		
//	}

}
