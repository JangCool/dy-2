package kr.co.pionnet.dy.vo;

import java.io.Serializable;

public  class PerfVO implements Serializable {
	
	// 0 : sun, 1:ibm	
	public String aid;
	public double tps;	
	public double trs;	
	public int active;	
	public int[] aspeed;
	public long eTime; 
	
	public float osUsedCpu;
	public float osSysCpu;
	public float osUserCpu;
	
	public long osTotalMem;
	public long osFreeMem;
	public long osUsedMem;
	public float osMemRate;
	
	public long jvmMemTotal;
	public long jvmMemFree;
	public long jvmMemUsed;
	
	//영역별 힙메모리 사용량
	public long[] areaMemoryUsage;
	// 0 : 전체사용량, 1:남은사용량, 2: 사용량
	public long[] heapMemoryUsage;
	public long noneHeapMemoryUsage;
	
	public long[] youngAreaGcInfo;
	public long[] oldAreaGcInfo;
	
	//index => 0 : gcTime, 1: gcCount 
	public long[] gcInfo;
	public float processCpu;
	
	

	//db 관련
	public int dbMaxActive;
	public int dbActive;
	public int dbIdle;
	
	//uniq users
	public double concurrentUsers;
	
	
}
