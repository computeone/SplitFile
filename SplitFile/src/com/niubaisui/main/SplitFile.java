/**
 * 
 */
package com.niubaisui.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import com.niubaisui.header.MajorFileHeader;
import com.niubaisui.header.SplitHeader;
import com.niubaisui.util.SplitFileUtil;

/**
 * @author Administrator
 *
 */
public class SplitFile {
	private String srcFilePath;
	private String splitFilePath;
	private String srcFileName;
	
	
	public String getSrcFileName() {
		return srcFileName;
	}
	public void setSrcFileName(String srcFileName) {
		this.srcFileName = srcFileName;
	}

	public String getSrcFilePath() {
		return srcFilePath;
	}
	
	public void setSrcFilePath(String srcFilePath) {
		this.srcFilePath = srcFilePath;
	}
	
	public String getSplitFilePath() {
		return splitFilePath;
	}
	
	public void setSplitFilePath(String splitFilePath) {
		this.splitFilePath = splitFilePath;
	}
	
	public SplitFile(){
		
	}
	public SplitFile(String srcfilename,String srcfilepath,String dstfilepath){
		this.srcFileName=srcfilename;
		this.splitFilePath=dstfilepath;
		this.srcFilePath=srcfilepath;
	}
	
	public void splitFile(int count) throws IOException, NoSuchAlgorithmException{
		File file=new File(this.srcFilePath+"\\"+this.srcFileName);
		FileInputStream input=new FileInputStream(file);
		
		long space=file.length();
		long splitSize=space/count;
		
		/*
		 * 创建主文件
		 */
		File main_dir=new File(this.splitFilePath);
		main_dir.mkdirs();
		File mainfile=new File(this.splitFilePath+"\\"+SplitFileUtil.generateSHA(SplitFileUtil.generateRandomString()));
		System.out.println("生成主文件的路径:"+mainfile.getAbsolutePath());
		FileOutputStream mainoutput=new FileOutputStream(mainfile);
		MajorFileHeader major_header=SplitFileUtil.createMajorFileHeader(this.srcFileName,count);
		SplitFileUtil.writeMajorFileHeader(major_header, mainoutput);
		
		long sumSize=0;
		/*
		 * 分割成一系列文件
		 */
		for(int i=0;i<count-1;i++){
			byte[] content=new byte[(int) (splitSize)];
			input.read(content);
			SplitHeader header=SplitFileUtil.createSplitHeader(i);
			File dstfile=new File(this.splitFilePath,header.getFileName());
			FileOutputStream output=new FileOutputStream(dstfile);
			System.out.println("生成片段文件:"+header.getFileName());
			/*
			 * 写头数据和内容数据
			 */
			SplitFileUtil.writerSplitHeader(header, mainoutput);
			output.write(content);
			output.close();
			sumSize=sumSize+content.length;
		}
		
		byte[] content=new byte[(int) (space-sumSize)];
		input.read(content);
		SplitHeader header=SplitFileUtil.createSplitHeader(count-1);
		File dstfile=new File(this.splitFilePath,header.getFileName());
		FileOutputStream output=new FileOutputStream(dstfile);
		
		
		SplitFileUtil.writerSplitHeader(header, mainoutput);
		output.write(content);
		output.close();
		mainoutput.close();
		input.close();
		
		
		System.out.println("分片大小:"+splitSize);
		System.out.println("分片处理完成！");
	}

	
}
