package kr.co.pionnet.dy.util.structure;

import java.util.Comparator;
import java.util.concurrent.PriorityBlockingQueue;

public class BlockingQueue extends PriorityBlockingQueue{
	
	private int max;
	
	public BlockingQueue(int capacity) {
		super(capacity);
	}
	
	public BlockingQueue(int capacity,Comparator comparator) {
		super(capacity,comparator);
	}
	

	public void setMax(int max){
		this.max = max;		
	}
	
	
	@Override
	public boolean offer(Object e) {
	
		if(max == size()){
			poll();
		}
		
		return super.offer(e);
	}
	
	
}
