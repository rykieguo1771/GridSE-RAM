package GridSE;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.IllegalFormatWidthException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import SP2E.SP2E;
import Tools.Base64;
import Tools.PRFUtils;

public class search {
	
	private static setting sSetting;

	
    private static String DB="/Users/Grykie/eclipse-workspace/GridSE_RAM/src/testDB.txt"; //initCode
     
	  
	public search(setting ss)  throws Exception{  
		sSetting =ss;
	}
	
	
	
	private static Map<String, Integer>  BCnt = sSetting.getBCnt();
	public static void search_decrypt(String w_p, int d_p,  Map<BitSet, List<BitSet>> InxDict) throws Exception{
		
		
	
		decryptAtClient(searchAtServer(w_p, d_p, InxDict));
	}
	
	
	
	/**@Step-2 decrypt At client
	 * @return I (List<String>) result
	 */ 
	public static List<String> decryptAtClient(Map<Integer, List<BitSet>> R) throws Exception{
		
		int seqinBCnt =0;
		List<String> I_w_p = new ArrayList<String>();
		//sweep R
		 Iterator iterR = R.entrySet().iterator();
	    	Iterator iterBCnt = BCnt.entrySet().iterator();
		    while (iterR.hasNext()) {
		    	
		    	Map.Entry entryR = (Map.Entry) iterR.next();
		    	int curseqinResult = (int) entryR.getKey();
		    	
		    	//sweep BCnt
			    while (iterBCnt.hasNext()) {
			    	if (seqinBCnt == curseqinResult) {
			    		//get val for entryR
			    		List<BitSet> valListinR = (List<BitSet>) entryR.getValue();
			    		
			    		//decrypt for each val
			    		            //System.out.print("\n Rvalsize:"+valListinR.size());
			    	    for(int i =0; i<valListinR.size(); i++) {  	
			    	    	String v= decEleinVal(valListinR.get(i), curseqinResult, i);
			   
			    	    	I_w_p.add(v);
			    	    	
			    	    }
			    		seqinBCnt++;
			    		break;
			    	}else {	    	
			    		seqinBCnt++;
			    	}
			    }		    	
		    }
		    
		   // System.out.print("I_w_p:"+I_w_p);
		    return I_w_p;
	}
	
	
	
	/**@Step-2 search At Server
	 * @return R (List<Bitset>) result
	 */ 
	public static Map<Integer, List<BitSet>> searchAtServer(String w_p, int d_p,  Map<BitSet, List<BitSet>> InxDict) throws Exception{
		
	  SP2E sp2e = new SP2E(sSetting); 
		
	  List<BitSet> TK=	genTK(w_p, d_p); 
	  Map<Integer, List<BitSet>> R = new LinkedHashMap<Integer, List<BitSet>>();
		
	 
	  
	  if (TK.size()!=InxDict.size()){
          System.out.println("Server: token is with error size!");
}else {
	  int i=0;
	  Iterator iter = InxDict.entrySet().iterator();
	    while (iter.hasNext()) {
	    	Map.Entry entry = (Map.Entry) iter.next();
	    	BitSet curtk = (BitSet) TK.get(i).clone();
	            /* String tkStr = Base64.encode(curtk.toByteArray());
	             String inxStr = Base64.encode(((BitSet) entry.getKey()).toByteArray());
	             System.out.print("\n tk:"+tkStr+" inx:"+ inxStr);*/
	    	BitSet f = 	sp2e.PrefDec(d_p, curtk, (BitSet) entry.getKey());
	    	 //System.out.print("\n f:"+f);
	    	 
	    	if(f.isEmpty()){
	    		    //System.out.print("\n vsize:"+((List<BitSet>) entry.getValue()).size());
                R.put(i, (List<BitSet>) entry.getValue());    
	    	}
	    	i++;
	    	
	    }
	    }
	  		
		System.out.print("R.size"+R.size());
		
		return R;		
	}
	
	

	/**@Step-1 gen token
	 * @return tk (List<Bitset>)
	 */    
	public static List<BitSet> genTK(String w_p, int d_p) throws Exception{
		
		int addr_num = BCnt.size();
		
		List<BitSet> deltaList = sSetting.getDelta();
 		List<BitSet> tk = new ArrayList<BitSet>();
 		
 		SP2E sp2e = new SP2E(sSetting); 		
 		for (int j =0; j< addr_num; j++){			
 			tk.add(sp2e.genTK(w_p, d_p, j, deltaList.get(j)));	
 			
 		}		
		         //System.out.print("tk:"+tk);
		return tk;   
	}
	
	

	public static String decEleinVal(BitSet val, int curseq, int curcnt) throws Exception {
		
		int lenPRF = sSetting.getLenPRF();
		
		String seed = curseq+"|"+curcnt;
		   BitSet G_k= PRFUtils.genFixedKey(seed, lenPRF);
		   val.xor(G_k);	
		   
		   byte[] valbyte = val.toByteArray();
	          
			//Byte to base64-string
			 String valStr =  new String(valbyte, StandardCharsets.UTF_8);
		   
		return valStr;
	}

	
	
	

}
