package kr.co.pionnet.dy.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

public class NetUtil {
	
	
	public static String getLocalServerIp() {
	    InetAddress localAddress = getLocalAddress();
	    if (localAddress == null) {
	        try {
	            return Inet4Address.getLocalHost().getHostAddress();
	        } catch (UnknownHostException e) {
	            ;
	        }
	    } else {
	        return localAddress.getHostAddress();
	    }
	 
	 
	    return "";
	}
	 
	
	/** 
	 * 현재 서버의 IP 주소를 가져옵니다. 
	 *  
	 * @return IP 주소 
	 */ 
	public static InetAddress getLocalAddress() { 
		
        try 
        { 
        	
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) 
            { 
                NetworkInterface intf = en.nextElement(); 
    
                
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) 
                { 
                    InetAddress inetAddress = enumIpAddr.nextElement(); 
                    
                    if (inetAddress.isSiteLocalAddress())
                    { 
                    	
                        return inetAddress;                                                 
                    } 
                } 
            } 
        } 
        catch (SocketException ex) {
        	ex.printStackTrace();
        } 
        return null; 
	}
	public static void main(String[] args) {
		//System.out.println(NetUtil.getLocalServerIp().getHostAddress());
	}
}

