/**
 * 
 */
package com.niubaisui.main;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

/**
 * @author Administrator
 *
 */
public class SplitMergeFile {
	
	public static void main(String[] argv) throws NumberFormatException, NoSuchAlgorithmException, IOException {
		if(argv.length<2){
			System.out.println("error:");
			System.out.println("-s  srcFilePath、splitToFilePath、srcFileName  and splitcount!");
			System.out.println("-m  srcFilePath and mergeFilePath");
			System.exit(0);
		}
		if(argv[0].equals("-s")){
			if(argv.length<5){
				System.out.println("-s  srcFilePath、splitToFilePath、srcFileName  and splitcount!");
				System.exit(0);
			}
			SplitFile  splitfile=new SplitFile();
			splitfile.setSrcFilePath(argv[1]);
			splitfile.setSplitFilePath(argv[2]);
			splitfile.setSrcFileName(argv[3]);
			splitfile.splitFile(Integer.valueOf(argv[4]));
		}
		if(argv[0].equals("-m")){
			if(argv.length<3){
				System.out.println("-m  srcFilePath and mergeFilePath");
				System.exit(0);
			}
			MergeFile mergefile=new MergeFile();
			mergefile.setSrcFilePath(argv[1]);
			mergefile.setMergeFilePath(argv[2]);
			mergefile.mergeFile();	
		}
	}

}
