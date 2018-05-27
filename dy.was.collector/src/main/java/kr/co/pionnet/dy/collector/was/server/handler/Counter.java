package kr.co.pionnet.dy.collector.was.server.handler;

import java.util.concurrent.atomic.AtomicInteger;

public class Counter {

	public static AtomicInteger atomicTrackerCount = new  AtomicInteger();
	public static long recv_count = 0;
	public static long textbag_count = 0;
	public static long  traker_count = 0;
	public static long   service_count = 0;
	public static long   querystring_count = 0;
	public static long   useragent_count = 0;
	public static long   sql_count = 0;
	public static long   sqlparam_count = 0;
	public static long   profile_count = 0;
	public static long   profile_count2 = 0;
	
	
	public static void print() {
//		System.out.println("#############################################################################");
//		
//		
//		System.out.println("recv_count    : "+recv_count);
//		System.out.println("profile_count    : "+profile_count);
//		System.out.println("profile_count2    : "+profile_count2);
//		System.out.println("--------------------------------");
		//System.out.println("textbag_count : "+textbag_count +" ==> [service_count :"+service_count +", useragent_count :"+useragent_count+", sql_count:"+sql_count+", profilehash_count : "+profilehash_count+", sqlparam_count : "+sqlparam_count+"]");
		//System.out.println("traker_count  : "+traker_count);		
//		System.out.println("#############################################################################");
	}
	
}

