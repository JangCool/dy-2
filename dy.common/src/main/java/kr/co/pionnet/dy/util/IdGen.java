package kr.co.pionnet.dy.util;

import java.util.Random;
import java.util.UUID;

public class IdGen {

	static Random random = null;
    private static long seed;
    static {
        seed = System.currentTimeMillis();
        random = new Random(seed);
    }
    public static long getSeed() {
        return seed;
    }
    
    public static long next() {
    	return random.nextLong();
    }
     
    
    
    final static char[] digits = {
    		
            '0', '1', '2', '3', '4', '5', '6', '7',	
    	    '8', '9', 'a', 'b', 'c', 'd', 'e', 'f',	
    	    'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',	
    	    'o', 'p', 'q', 'r', 's', 't', 'u', 'v',	
    	    'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D',	
    	    'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',	
    	    'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',	
    	    'U', 'V', 'W', 'X', 'Y', 'Z', '_', '*' // '.', '-'
    };
    	    

	public static String toUnsignedString(long i, int shift) {

        char[] buf = new char[64];

        int charPos = 64;	
        int radix = 1 << shift;	
        long mask = radix - 1;	
        long number = i;
        
        do {	
            buf[--charPos] = digits[(int) (number & mask)];
            number >>>= shift;

        } while (number != 0);

        	return new String(buf, charPos, (64 - charPos));

	}
	
	public static String getUid() {
		
		//getLeastSignificantBits() : UUID 의 128 비트치의 최하정도 64 비트를 돌려줍니다.
		//getMostSignificantBits() : UUID 의 128 비트치의 최상정도 64 비트를 돌려줍니다.
		
		UUID uuid = UUID.randomUUID();		
		return toUnsignedString(uuid.getMostSignificantBits(), 6).concat(toUnsignedString(uuid.getLeastSignificantBits(), 6));
	}

	
	
	public static void main(String[] args) {
		System.out.println(getUid());
	}
	
	
	
    
}
