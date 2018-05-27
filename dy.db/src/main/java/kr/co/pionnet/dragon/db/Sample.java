package kr.co.pionnet.dragon.db;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.google.common.collect.Ordering;
import com.google.common.collect.TreeMultimap;

public class Sample {

	static Map<String, ArrayList<String>> hashMap = new HashMap<String, ArrayList<String>>();
	
	
	public static void main(String[] args) {
	/*	TreeMap<Integer, String>  tm = new TreeMap<Integer, String>();
		tm.put(1, "1");
		tm.put(2, "1");
		tm.put(3, "1");
		tm.put(1, "2");
		
		 Set set = tm.entrySet();
	      Iterator iterator = set.iterator();
	      while(iterator.hasNext()) {
	         Map.Entry mentry = (Map.Entry)iterator.next();
	         System.out.print("key is: "+ mentry.getKey() + " & Value is: ");
	         System.out.println(mentry.getValue());
	      }*/
	      
	    /*
	      
	      Multiset<String> multiset = HashMultiset.create();
	      
	      multiset.add("a");
	      multiset.add("b");
	      multiset.add("c");
	      multiset.add("d");
	      multiset.add("a");
	      multiset.add("b");
	      multiset.add("c");
	      multiset.add("b");
	      multiset.add("b");
	      multiset.add("b");
	      
	      //print the occurrence of an element
	      System.out.println("Occurrence of 'b' : "+multiset.count("b"));
	    //print the total size of the multiset
	      System.out.println("Total Size : "+multiset.size());
	      
	      //get the distinct elements of the multiset as set
	      Set<String> set = multiset.elementSet();
	      
	      //display the elements of the set
	      System.out.println("Set [");
	      
	      for (String s : set) {			
	         System.out.println(s);		    
	      }
	      
	      System.out.println("]");
	      
	      
	      //display all the elements of the multiset using iterator
	      Iterator<String> iterator  = multiset.iterator();
	      System.out.println("MultiSet [");
	      
	      while(iterator.hasNext()){
	         System.out.println(iterator.next());
	      }
	      
	      System.out.println("]");
	      
	      
	    //display the distinct elements of the multiset with their occurrence count
	      System.out.println("MultiSet [");
	      
	      for (Multiset.Entry<String> entry : multiset.entrySet())
	      {
	         System.out.println("Element: " + entry.getElement() + ", Occurrence(s): " + entry.getCount());		    
	      }
	      System.out.println("]");		*/
		
		
		
		/*// Add data with duplicate keys
		   addValues("A", "a1");
		   addValues("A", "a2");
		   addValues("B", "b");
		   // View data.
		   Iterator it = hashMap.keySet().iterator();
		   ArrayList<String> tempList = null;

		   while (it.hasNext()) {
		      String key = it.next().toString();             
		      tempList = hashMap.get(key);
		      if (tempList != null) {
		         for (String value: tempList) {
		            System.out.println("Key : "+key+ " , Value : "+value);
		         }
		      }
		   }*/

		
		TreeMultimap multimap = TreeMultimap.create(Ordering.natural(), new Comparator<Long>() { 
            @Override 
            public int compare(Long o1, Long o2) { 
                return o1 > o2 ? 1:-1; 
            } 
        }); 
		
		multimap.put(1l, 3l);
		multimap.put(1l, 1l);
		multimap.put(1l, 2l);
		multimap.put(1l, 4l);
		multimap.put(2l, 1l);
		multimap.put(3l, 1l);
		
		
		  Map<Long, Collection<Long>> map = multimap.asMap();
	      for (Map.Entry<Long,  Collection<Long>> entry : map.entrySet()) {
	         long key = entry.getKey();
	         Collection<Long> values =  multimap.get(key);
	         
	         //동일 건이 존재 할수 있음
	         for(long value:values) {
	        	 System.out.println(key + ":" + value);
	        	 
	         }
	      }
		
	}
	
	

	private static void addValues(String key, String value) {
	   ArrayList tempList = null;
	   if (hashMap.containsKey(key)) {
	      tempList = hashMap.get(key);
	      if(tempList == null)
	         tempList = new ArrayList();
	      tempList.add(value);  
	   } else {
	      tempList = new ArrayList();
	      tempList.add(value);               
	   }
	   hashMap.put(key,tempList);
	}
}
