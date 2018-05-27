package kr.co.pionnet.dragon.db;

import java.io.IOException;
import java.util.Calendar;

import kr.co.pionnet.dy.type.DataType;

public class FileDBReadTest {

	public static void main(String[] args) throws IOException {
		
		
		DBFileReader dfr1 = new DBFileReader(DataType.TRACKER);
		
		
		/*Calendar startCal = Calendar.getInstance();
		startCal.set(2016, Calendar.OCTOBER, 13, 21, 10, 57);
		
		Calendar endCal = Calendar.getInstance();
		endCal.set(2016, Calendar.OCTOBER, 13, 12, 40, 01);
		*/
		
		
		dfr1.readTracker(4693179443240622006l, 1476440916054l, 1476440916061l, 110);


	}

}
