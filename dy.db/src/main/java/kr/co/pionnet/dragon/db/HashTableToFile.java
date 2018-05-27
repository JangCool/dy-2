package kr.co.pionnet.dragon.db;

import java.util.Enumeration;

import kr.co.pionnet.dy.util.structure.IntKeyMap;

public class HashTableToFile extends Thread  {
	
	private static HashTableToFile instance = null;
	private boolean running = true;
	
	private IntKeyMap<Item> table = new IntKeyMap<Item>();
	
	private static class Item {
		long lastFlushTime = System.currentTimeMillis();
		Object object;
		public Item(Object object) {
			this.object = object;
		}

	}
	public static HashTableToFile getInstance() {
		
		try {
			if(instance == null ) {
				instance = new HashTableToFile();				
				instance.setDaemon(true);
				instance.start();
			}	
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return instance;
	}
	
	
	@Override
	public void run() {		
		
		// TODO Auto-generated method stub
		while(true) {
			try {				
				
				int size = table.size();
				save(size);
				
				Thread.sleep(1000);
			} catch(Exception e) {
				e.printStackTrace();
			}
			
			
		}
	}
	public synchronized  void put(Object item) {
		table.put(System.identityHashCode(item),new Item(item));
	}
		
	private void save(int size) {		
		try {
			Enumeration<Item> en = table.values();
			while (en.hasMoreElements()) {				
				Item item = en.nextElement();
				
				KeyMapMng htm = (KeyMapMng) item.object;
				long now = System.currentTimeMillis();
				
				if(htm.isFlush() && now >= item.lastFlushTime + htm.interval() ) {				
						
					item.lastFlushTime = now;
					htm.saveTableToFile();
				    htm.close();

				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public synchronized  void remove(Object item) {		
		table.remove(System.identityHashCode(item));
		KeyMapMng htm = (KeyMapMng) item;
		if(htm.flush) {
			htm.saveTableToFile();
		}
	}
	
	public static void main(String[] args) {
		HashTableToFile.getInstance();
		
		
		//HashTableToFile.getInstance();
	}

}
