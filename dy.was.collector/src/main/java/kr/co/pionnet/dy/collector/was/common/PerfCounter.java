package kr.co.pionnet.dy.collector.was.common;

import java.util.concurrent.TimeUnit;

import kr.co.pionnet.dy.util.NumUtil;

public class PerfCounter {

	
	public static class PerfBag {
		public long eTime;
		public double tps;
		public double trs;
		long active;
		int dbActive;		
		float osUsedCpu;
		float jvmMemUsed;
		long jvmMemTotal;
		double concurentUsers;
		
		public PerfBag(long etime, double tps, double trs, long active2, int dbActive ,float osUsedCpu, float jvmMemUsed, long jvmMemTotal, double cusers) {
			this.eTime = etime;
			this.tps = tps;
			this.trs = trs;
			this.active = active2;
			this.dbActive = dbActive;
			this.osUsedCpu = osUsedCpu;
			this.jvmMemUsed = jvmMemUsed;
			this.jvmMemTotal = jvmMemTotal;
			this.concurentUsers = cusers;
		}
		
		
	}
	
	public enum PERF_TYPE {		
		
	     TPS_ALL("TPS_ALL"), TPS_AVR("TPS_AVR"), TRS_ALL("TRS_ALL"), TRS_AVR("TPS_AVR"), ACTIVE("ACTIVE"), ACTIVE_SUM("ACTIVE_S"), ASPEED("ASPEED"),DB_CON_ACTIVE_SUM("ADS"), CONCURENT_USERS("CUSERS");
		
		final private String name;
		
		private PERF_TYPE(String name){
			this.name = name;
		}
		
		public String getName(){
			return name;
		}
		
	}

	int size = 0;
	int indexCount = 0;

	private  PerfBag[] objArray = null;	
	
	public PerfCounter(int size) {
		this.size = size;		
		objArray = new PerfBag[size];		
	}
	
	public void set(long etime, double tps, double trs ,long active, int dbActive, float osUsedCpu, float jvmMemUsed, long jvmMemTotal, double cusers) {
		PerfBag  p = new PerfBag(etime, tps, trs, active, dbActive, osUsedCpu,  jvmMemUsed, jvmMemTotal, cusers );
			
		add(p);
	}
	
	private void add(PerfBag object) {
		if(objArray != null )
		{		
			if(indexCount == size)  indexCount = 0;
			//System.out.println(indexCount);
			
			//objArray[IndexPosition(object)] = object;
			objArray[indexCount] = object;
			indexCount++;
		}		
	}
	
	private int IndexPosition(Object object) {		
		PerfBag p = (PerfBag)object;
		long millis = p.eTime;		
		return (int) ((TimeUnit.MILLISECONDS.toSeconds(millis))%size);
	}
	
	public int sum(PERF_TYPE type) {
		int sum = 0;
		
		switch (type) {
		
		case DB_CON_ACTIVE_SUM:			
			for(PerfBag pb :  objArray) {				
				if(pb == null )	  break;
				sum +=pb.dbActive;							
			}
			break;
		case ACTIVE_SUM:			
			for(PerfBag pb :  objArray) {				
				if(pb == null )	  break;
				sum +=pb.active;							
			}
			break;
		}
		return sum;
	}
	
	public  double tpsAvg = 0.0;
	public  double trsAvg = 0.0;
	public  double activeAvg = 0.0;
	public  double dbActiveAvg = 0.0;
	public double osUseCpuAvg = 0;
	public double jvmMemUseAvg = 0;
	public long jvmMemTotalAvg = 0;
	public double jvmMemUsedPercent= 0.0; 
	public double cUsers = 0.0;
	
	
	public void computePerf() {
		double count = 0.0;		
		double tpsSum = 0.0;
		double trsSum = 0.0;
		double activeSum = 0.0;
		int dbActiveSum = 0;
		double osUseCpuSum = 0;
		double jvmMemUseSum = 0;
		long jvmMemTotal = 0;		
		long currTime = System.currentTimeMillis();
		double cUsersSum = 0.0;
		
		tpsAvg = 0.0; 
		trsAvg   = 0.0;		
		activeAvg = 0.0;
		dbActiveAvg = 0.0;
		osUseCpuAvg = 0;
		jvmMemUseAvg = 0;
		jvmMemTotalAvg = 0;
		jvmMemUsedPercent= 0.0;
		cUsers = 0.0;
		for(PerfBag pb :  objArray) {			
			if(pb == null )	  break;
			
//			System.out.println("currTime ==>"+currTime + ":: eTime ==> "+pb.eTime+ ":: "+(currTime - 5000 < pb.eTime));
			if(currTime - 10000 < pb.eTime) {
				tpsSum += pb.tps;
				trsSum += pb.trs;
				activeSum += pb.active;
				dbActiveSum += pb.dbActive;
				osUseCpuSum += pb.osUsedCpu;
				jvmMemUseSum += pb.jvmMemUsed;
				jvmMemTotal += pb.jvmMemTotal;
				cUsersSum  += pb.concurentUsers;
				count++;				
			}
		}
		
//		System.out.println("dbActiveSum ==>"+dbActiveSum + ":: count ==> "+count);
		
	
		if( count > 0) {
			tpsAvg = tpsSum/count;
			trsAvg = trsSum/count;
			activeAvg = activeSum/count;
			dbActiveAvg = dbActiveSum/count;
			osUseCpuAvg = osUseCpuSum/count;
			jvmMemUseAvg = jvmMemUseSum/count;
			jvmMemTotalAvg = (long) (jvmMemTotal/count);
			cUsers = cUsersSum/count;
			
			if(jvmMemUseAvg != 0){
				jvmMemUsedPercent = NumUtil.roundPoint(jvmMemUseAvg / (jvmMemTotalAvg/ (1024 * 1024)) * 100,1);	
			}
			
		}
//		System.out.println("dbActiveAvg ==>"+dbActiveAvg );
//		System.out.println("cUsers ==>"+cUsers );

		
		
		
	}
	

	
	public  Object[] getArray() {
		
		return objArray;
	}
	
	/*public static void main(String[] args) {
		
		PerfCounter pc = new PerfCounter(10);
		
		for(int i=0; i < 24; i++) {		
			PerfBag  p = new PerfBag(System.currentTimeMillis()-i*1000, i, i+1);
			pc.add(p);
		}		
		
		Object[] object = pc.getArray();		
		
		for(int i=0; i < object.length; i++) {
			PerfBag p1 =(PerfBag)object[i];
			System.out.println("P["+i+"] : "+(new Date(p1.eTime)));
		}
		
		
		
	}*/
	

	
	
}
