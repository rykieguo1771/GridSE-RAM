package Tools;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class test_linkedmap {
	

	public static void main(String args[]) throws Exception {
		
		
		Map<String, Integer> BCnt = new LinkedHashMap<String, Integer>();
		
		BCnt.put("a", 3);
		
		BCnt.put("c", 2);
		

		BCnt.put("e", 5);
		
		
		Iterator iter = BCnt.entrySet().iterator();
	    while (iter.hasNext()) {
	        Map.Entry entry = (Map.Entry) iter.next();
	        System.out.println(entry.getKey() + "=" + entry.getValue());
	    }
	
		
        
		BCnt.put("c", BCnt.get("c")+1);
		
    
        int i =1;
   		Iterator iter1 = BCnt.entrySet().iterator();
	    while (iter1.hasNext()) {
	    	Map.Entry entry = (Map.Entry) iter1.next();
	    if(i==1){
	    	System.out.println(entry.getKey() + "=" + entry.getValue());
	    	break;
	    }
	        System.out.println(entry.getKey() + "=" + entry.getValue());
	    }
		
		
	
	
		
	}
	

}
