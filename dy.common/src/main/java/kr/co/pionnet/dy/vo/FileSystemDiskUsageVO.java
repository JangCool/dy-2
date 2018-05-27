package kr.co.pionnet.dy.vo;

import java.io.Serializable;

public class FileSystemDiskUsageVO  implements Serializable {
	public String aid;
	public String devName;
	public String dirName;
	public long fsDiskTotal;
	public long fsDiskFree;
	public long fsDiskAvail;
	public long fsDiskUsed;
	public short fsDiskUsePercent;

}
