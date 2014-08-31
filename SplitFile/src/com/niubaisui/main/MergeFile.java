/**
 * 
 */
package com.niubaisui.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import com.niubaisui.header.MajorFileHeader;
import com.niubaisui.header.SplitHeader;
import com.niubaisui.util.SplitFileUtil;

/**
 * @author Administrator
 *
 */
public class MergeFile {

	private String srcFilePath;
	private String mergeFilePath;
	
	public String getSrcFilePath() {
		return srcFilePath;
	}

	public void setSrcFilePath(String srcFilePath) {
		this.srcFilePath = srcFilePath;
	}
	
	public String getMergeFilePath() {
		return mergeFilePath;
	}

	public void setMergeFilePath(String mergeFilePath) {
		this.mergeFilePath = mergeFilePath;
	}
	
	public void mergeFile() throws IOException {
		String mainFileName=SplitFileUtil.getMainFileName(srcFilePath);
		File mainfile=new File(srcFilePath+"\\"+mainFileName);
		
		
		FileInputStream maininput=new FileInputStream(mainfile);
		MajorFileHeader major_header=SplitFileUtil.readMajorFileHeader(maininput);
		
		File mergeFileDir=new File(mergeFilePath);
		mergeFileDir.mkdirs();
		System.out.println("源文件目录:"+mainfile.getAbsolutePath());
		
		File mergeFile=new File(mergeFilePath+"\\"+major_header.getFilename());
		FileOutputStream merge_output=new FileOutputStream(mergeFile);
		System.out.println("合成文件的目录:"+mergeFile.getAbsolutePath());
		/*
		 * 读取分片头
		 */
		for(int i=0;i<major_header.getSplitFileCount();i++){
			
			SplitHeader split_header=SplitFileUtil.readerSplitHeader(maininput);
			File split_file=new File(srcFilePath+"\\"+split_header.getFileName());
			FileInputStream split_input=new FileInputStream(split_file);
			byte[] bb=new byte[(int) split_file.length()];
			System.out.println("读取片段文件:"+split_header.getFileName());
			split_input.read(bb);
			split_input.close();
			merge_output.write(bb);
			
		}
		
		merge_output.close();
		System.out.println("合成文件成功!");
	}

}
