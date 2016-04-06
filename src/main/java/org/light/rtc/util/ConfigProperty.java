package org.light.rtc.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * @function: 读取配置文件，返回一个属性组
 * @author guang_wang
 *
 */
public class ConfigProperty {
	
	private static Properties dbProps;
	
	private static synchronized void init_prop(){
		dbProps = new Properties();	
		String path = ConfigProperty.class.getClassLoader().getResource("").getPath();
		path = path.replaceAll("%20", " ");
		InputStream fileinputstream = null;
		try {
			fileinputstream = new FileInputStream(path+"rtc_conf.properties");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			dbProps.load(fileinputstream);
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("不能读取数据库配置文件, 请确保rtc_conf.properties在CLASSPATH指定的路径中!");
		}
	}
	
	public static Properties getProps(){
		if(dbProps==null)
			init_prop();
		return dbProps;
	}
	
	public static String getCurDateTime() {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//"yyyyMMddHHmmss"
		return df.format(new Date());
	}
	
	public static void main(String[] args) {
		System.out.println(ConfigProperty.getProps().getProperty("cluterName"));
		System.out.println(ConfigProperty.getProps().getProperty("clusterHosts"));
		System.out.println(ConfigProperty.getProps().getProperty("serverPort"));
		
		System.out.println("=============================================");
		
		System.out.println(ConfigProperty.getProps().getProperty("minThreadNum"));
		System.out.println(ConfigProperty.getProps().getProperty("maxThreadNum"));
		
	}
	
}
