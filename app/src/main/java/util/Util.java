package util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Util {
	
	public static String getSystemTime(){
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy/MM/dd----hh:mm:ss", Locale.getDefault());
		Date date = new Date(System.currentTimeMillis());
		String dateTime = sDateFormat.format(date);
		return dateTime;
	}

	public static String bytesToHexString(byte[] src) {

		StringBuilder stringBuilder = new StringBuilder();
		if (src == null || src.length <= 0) {
			return null;
		}
		//for (int i = 0; i < src.length; i++) {
		for (byte aSrc : src) {
			int v = aSrc & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString().toUpperCase();
	}

	public static void writeLog(String pkgName, List<String> logList){
		if(SDUtils.isSdCardAvailable()){
			File logFile = SDUtils.createFile("Appmonitor/AppLog", pkgName);
			FileWriter fw;
			try{
				fw = new FileWriter(logFile, true);
				for(String log : logList){
					fw.write(log+"\n");
				}
				fw.write("\n");
				fw.flush();
				fw.close();
			}catch (FileNotFoundException e) {
				System.out.println("file not found!");
			} catch (IOException e) {
				System.out.println("Output error!");
			}
		}
	}

	public static void writeLog(String pkgName, String log){
		if(SDUtils.isSdCardAvailable()){
			File logFile = SDUtils.createFile("Appmonitor/AppLog", pkgName);
			FileWriter fw;
			try{
				fw = new FileWriter(logFile, true);
				fw.write(log+"\n");
				fw.flush();
				fw.close();
			}catch (FileNotFoundException e) {
				System.out.println("file not found!");
			} catch (IOException e) {
				System.out.println("Output error!");
			}
		}
	}
	
	public static void writeNetLog(String pkgName, List<String> logList){
		if(SDUtils.isSdCardAvailable()){
			File logFile = SDUtils.createFile("Appmonitor/NetLog", pkgName);
			FileWriter fw;
			try{
				fw = new FileWriter(logFile, true);
				for(String log : logList){
					fw.write(log+"\n");
				}
				fw.write("\n");
				fw.flush();
				fw.close();
			}catch (FileNotFoundException e) {
				System.out.println("file not found!");
			} catch (IOException e) {
				System.out.println("Output error!");
			}
		}
	}

}
