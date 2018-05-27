package kr.co.pionnet.dy.collector.was.common;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public  class ReceiverQueue<E>  {
		
		int maxSize = 1024;
		
		private LinkedList<E> queue = null;
		public ReceiverQueue(int maxSize) {
			this.maxSize = maxSize;
			this.queue = new LinkedList<E>();
		}
		
		public synchronized boolean enqueue(E item) {
			if(size() < maxSize) {
				queue.addLast(item);
				return true;
			} else {
				return false;
			}
		}
		
		public LinkedList<E>  getList( ){
		
			return queue;
		}
		public synchronized E dequeue() {
				return queue.poll();			
		}
		
		public boolean hasItems() {
			return !queue.isEmpty();
		}
		
		public int size() {
			return queue.size();
		}
		
		public void addItems(ReceiverQueue<? extends E> q) {
			while (q.hasItems())
				queue.addLast(q.dequeue());
		}
		
	
		
		public synchronized List convertToList() {
			List list = new ArrayList();
			try {
				
				
				int size = queue.size();
				for(int i=0; i < size; i++) {

					E e = dequeue();					
					list.add(e);	
					
				}
				
			} catch(Exception e) {
				e.printStackTrace();
			}
			
			
			return list;
		}
		
		
		
		
		public static void main(String[] args) {
			
//			ReceiverQueue<String> q = new ReceiverQueue<String>(4);
//			q.enqueue("1111");
//			q.enqueue("2222");
//			q.enqueue("3333");
//			q.enqueue("4444");
//			System.out.println(q.size());
//			System.out.println(q.enqueue("5555"));			
//			System.out.println(q.size());
//			System.out.println(q.dequeue());
//			System.out.println(q.dequeue());
//			System.out.println(q.size());
//			System.out.println(q.dequeue());
//			System.out.println(q.dequeue());
//			System.out.println(q.dequeue());
//			System.out.println("77^8494715160960125014^1467079014283^11".getBytes().length);
		}
	
}
