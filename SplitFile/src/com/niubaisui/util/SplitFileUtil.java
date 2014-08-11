/**
 * 
 */
package com.niubaisui.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import com.niubaisui.header.MajorFileHeader;
import com.niubaisui.header.SplitHeader;

/**
 * @author Administrator
 *
 */
public class SplitFileUtil {

	public static int magic=0x12169361;
	public static MajorFileHeader createMajorFileHeader(String orginFileName,int splitFileCount){
		MajorFileHeader header=new MajorFileHeader();
		header.setFilename(orginFileName);
		header.setFilenameSize(orginFileName.getBytes().length);
		header.setSplitFileCount(splitFileCount);
		return header;
	}
	
	public static void writeMajorFileHeader(MajorFileHeader header,FileOutputStream output) throws IOException{
		output.write(SplitFileUtil.encodeInteger(header.getMagic()).getBytes());
		output.write(SplitFileUtil.encodeInteger(header.getSplitFileCount()).getBytes());
		output.write(SplitFileUtil.encodeInteger(header.getFilenameSize()).getBytes());
		output.write(header.getFilename().getBytes());
		
	}
	public static MajorFileHeader readMajorFileHeader(FileInputStream input) throws IOException{
		MajorFileHeader header=new MajorFileHeader();
		byte[] bc=new byte[10];
		input.read(bc);
		header.setMagic(SplitFileUtil.decodeInteger(new String(bc)));
		input.read(bc);
		header.setSplitFileCount(SplitFileUtil.decodeInteger(new String(bc)));
		input.read(bc);
		header.setFilenameSize(SplitFileUtil.decodeInteger(new String(bc)));
		byte[]  bb=new byte[header.getFilenameSize()];
		input.read(bb);
		header.setFilename(new String(bb));
		return header;
	}
	public static String generateRandomString(){
		Random random=new Random();
		String s="";
		for(int i=0;i<100;i++){
			s=s+random.nextInt();
		}
		return s;
	}
	public static SplitHeader createSplitHeader(int serialNumber) throws NoSuchAlgorithmException{
		SplitHeader header=new SplitHeader();
		header.setSerialNumber(serialNumber);
		header.setFileName(SplitFileUtil.generateSHA(SplitFileUtil.generateRandomString()));
		return header;
	}
	
	public static void writerSplitHeader(SplitHeader header,FileOutputStream output) throws IOException{
		output.write(SplitFileUtil.encodeInteger(header.getSerialNumber()).getBytes());
		output.write(header.getFileName().getBytes());
	}
	public static SplitHeader readerSplitHeader(FileInputStream input) throws IOException{
		SplitHeader header=new SplitHeader();
		byte[] bc=new byte[10];
		input.read(bc);
		header.setSerialNumber(SplitFileUtil.decodeInteger(new String(bc)));
		byte[] bb=new byte[160/4];
		input.read(bb);
		header.setFileName(new String(bb));
		return header;
	}
	
	public static String generateSHA(String str) throws NoSuchAlgorithmException{
		MessageDigest digest=MessageDigest.getInstance("SHA"); 
		byte[] bb=digest.digest(generateRandomString().getBytes());
		String s="";
		for(byte b:bb){
			String ss=Integer.toHexString(b);
			if(ss.length()>2){
				s=s+ss.substring(ss.length()-2);
			}
			else if(ss.length()==1){
				s=s+"0"+ss;
			}
			else{
				s=s+ss;
			}
		}
		return s;
	}
	
	public static String encodeInteger(int a){
		String s=String.valueOf(a);
		int size=10-s.length();
		String ss="";
		for(int i=0;i<size;i++){
			ss=ss+"0";
		}
		ss=ss+s;
		return ss;
	}
	
	public static int decodeInteger(String s){
		if(s.length()==10){
			int j=0;
			for(int i=0;i<10;i++){
				if(s.charAt(i)=='0'){
					j++;
					continue;
				}
				break;
			}
			if(j==10){
				s="0";
			}
			else{
				s=s.substring(j,s.length());
			}
			return Integer.valueOf(s);
		}
		return -1;
		
	}
	
	public static String getMainFileName(String path) throws IOException{
		File file=new File(path);
		if(file.isDirectory()){
			String[] filenames=file.list();
			
			for(String filename:filenames){
				File tmpfile=new File(file.getPath()+"\\"+filename);
				FileInputStream input=new FileInputStream(tmpfile);
				byte[] bc=new byte[10];
				input.read(bc);
				String filemagic=new String(bc);
				
				if(filemagic.equals(SplitFileUtil.encodeInteger(SplitFileUtil.magic))){
					input.close();
					return filename;
				}
				input.close();
			}
			
		}
		return null;
	}
	
}
