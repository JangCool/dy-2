package kr.co.pionnet.dy;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class CommonThreadFactory implements ThreadFactory {
	
   final AtomicInteger poolNumber = new AtomicInteger(1);
   final ThreadGroup group;
   final AtomicInteger threadNumber = new AtomicInteger(1);
   
   private String namePrefix;
   boolean isDaemon;
   

	public CommonThreadFactory() {
		this("DyThreadFactory");
	}

	public CommonThreadFactory(boolean isDaemon) {
		this();
		this.isDaemon = isDaemon;
	}
	
	public CommonThreadFactory(String name) {
		group = new ThreadGroup(name);
		namePrefix = name+"-pool-" + poolNumber.getAndIncrement() + "-thread-";
	}   
	
	public CommonThreadFactory(String name,boolean isDaemon) {
		this(name);
		this.isDaemon = isDaemon;
	}
	
    public Thread newThread(Runnable r,String name) {
        return  newThread(r, name+"-pool-" + poolNumber.getAndIncrement() + "-thread-", false);
    }
    
    public Thread newThread(Runnable r, boolean isDaemon) {
        return newThread(r, namePrefix, isDaemon);
    }
    
    public Thread newThread(Runnable r,String name, boolean isDaemon) {
        Thread thread = new Thread(group,r, namePrefix + name +"-"+ threadNumber.getAndIncrement());
        thread.setDaemon(isDaemon);

        return thread;
    }
    
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(group,r, namePrefix + threadNumber.getAndIncrement());
        thread.setDaemon(this.isDaemon);
        
        return thread;
    }
    
    public ThreadGroup getThreadGroup() {
    	return group;
    }
    
    public void destory() {
    	try {

    		if(group.activeGroupCount() > 0) {
    			group.destroy();	    			
    		}

		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    public boolean isDestroyed() {
    	return group.isDestroyed();
    }
}
