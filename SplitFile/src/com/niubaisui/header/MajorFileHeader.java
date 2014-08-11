/**
 * 
 */
package com.niubaisui.header;

import com.niubaisui.util.SplitFileUtil;

/**
 * @author Administrator
 *
 */
public class MajorFileHeader {

	private int magic=SplitFileUtil.magic;
	private int splitFileCount;
	private int filenameSize;
	private String filename;
	public int getMagic() {
		return magic;
	}
	public void setMagic(int magic) {
		this.magic = magic;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public int getFilenameSize() {
		return filenameSize;
	}
	public void setFilenameSize(int filenameSize) {
		this.filenameSize = filenameSize;
	}
	public int getSplitFileCount() {
		return splitFileCount;
	}
	public void setSplitFileCount(int splitFileCount) {
		this.splitFileCount = splitFileCount;
	}
	
}
