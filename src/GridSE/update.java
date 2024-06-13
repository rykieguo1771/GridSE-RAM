package GridSE;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import SP2E.SP2E;
import Tools.Base64;
import Tools.Bitset_ByteArray;
import Tools.FileReadWrite;
import Tools.GeoHash_etd;
import Tools.PRFUtils;
import Tools.SecureHash;

public class update {
	

	private static setting sSetting;
	
	private  static List<String> EncValList = new ArrayList<String>();
	
    private static String DB="/Users/Grykie/eclipse-workspace/GridSE_RAM/src/testDB.txt"; //initCode
	
	  
	public update(setting ss)  throws Exception{  
		sSetting =ss;
	}
	
	private static 	int d =sSetting.getD();
	private static 	int lenH= sSetting.getLenH();
	private static Map<String, Integer> BCnt =sSetting.getBCnt();
	private static Map<BitSet, List<BitSet>> inxDict;
	private  static List<BitSet> deltaL = sSetting.getDelta();
	
	/**@Step-1 gen InxDict from DB
	 *    InxDict <key, VAL>
	 * @return 
	 */    
	public static Map<BitSet, List<BitSet>> updateAllInx() throws Exception{   //String DB, BitSet key, BitSet eleval
		
		inxDict = new LinkedHashMap<BitSet, List<BitSet>>();
		
		
		FileReadWrite fileOP = new FileReadWrite();
		List<String> DBList = fileOP.readFileToStringList(DB);
		String Bid, lat, lon, code,  op; 
		
		
		Iterator<String> it = DBList.iterator();
		while(it.hasNext()) {
			String line = it.next();
			String[] eachstr =line.split("\\s+");
			Bid = eachstr[0];
			code = eachstr[1].substring(0, d);
			op = eachstr[2];
			
			updateAnEntryInx(Bid, op, code);
			
		}
		
		//test
		
		Iterator iter = BCnt.entrySet().iterator();
	    while (iter.hasNext()) {
	    	Map.Entry entry = (Map.Entry) iter.next();
	             //  System.out.println("\n test"+entry.getKey() + "=" + entry.getValue());
	    }
	    
	    
	    sSetting.setN(DBList.size()); 
		//System.out.println("delta:"+deltaL);  
	
		return inxDict;
				
	}
	
	



	/**@Step-2 update InxDict
	 *  add a location into InxDict
	 * @return 
	 */    //l: code length for inx
	public static void updateAnEntryInx( String Bid, String op, String code) throws Exception{   //String DB, BitSet key, BitSet eleval
			
		String w = code.substring(0, d);
		
		 //System.out.println("plain_key:"+subcode);
		BitSet addr = new BitSet(); 
		
		
		
		int curseq =  BCnt.size();; 
		
		if(BCnt.containsKey(w)){ 
			
			 int curcnt = BCnt.get(w);

			/* from local BCnt, to find the sequence of the target w */
			int target_seq=0, i = 0; 
			Iterator iter1 = BCnt.entrySet().iterator();
		    while (iter1.hasNext()) {
		    	Map.Entry entryBCnt = (Map.Entry) iter1.next();
		    	     
	            if(entryBCnt.getKey().equals(w)) {
	            	target_seq= i;  // identify the key sequence in InxDict
	            	break;
	            }
	            i++;
		    }
		    
		    /* from server InxDict, to get the pair of that sequence */
		    int j=0; List<BitSet> exvalList = new  ArrayList<BitSet>();
		    Iterator iter2 = inxDict.entrySet().iterator();
		    while (iter2.hasNext()) {
		    	Map.Entry entryInx = (Map.Entry) iter2.next();
	            if(j==target_seq) {
	            	addr= (BitSet) entryInx.getKey();  // get the pair at that sequence
	            	exvalList =(List<BitSet>) entryInx.getValue();
	             break;
	            }
	            j ++;
		        //System.out.println(entry.getKey() + "=" + entry.getValue());
		    }
//		              / System.out.print("\n ex:: target_seq"+target_seq+"|| curcnt"+curcnt);
			
		    BitSet val= genEleinVal(Bid,op, target_seq, curcnt);
		    
		    BCnt.put(w, curcnt+1);
		    exvalList.add(val);
			inxDict.put(addr, exvalList);
		
			
		}else {// this keyword not existed, so insert (w, its updates num)
			
			/* from local BCnt, to add (w, cnt=1)*/
			
			int curcnt=0; 	
			/* from server InxDict, to add new generated (addr, val)*/
			addr = genKey(curseq, w);
			List<BitSet> valList = new ArrayList<BitSet>();
			BitSet val= genEleinVal(Bid,op, curseq, curcnt);
			valList.add(val);
			
			BCnt.put(w, curcnt+1);
			inxDict.put(addr, valList);
			
			
			         //System.out.print("\n nex:: curseq="+curseq+"|| curcnt="+curcnt+"|| w="+w);
		}
	
		
	}

	
	
	
	/**@Step-3 gen an entry for a location
	 * @author Rykie
	 * @return  a location's cell code + record delta(list)
	 * @throws Exception 
	 * */
	public static BitSet genKey(int seq, String scode) throws Exception{
	
		
		SP2E sp2e = new SP2E(sSetting);
		BitSet tao = (BitSet) sp2e.PreEnc(scode, String.valueOf(seq)).clone();
		     //System.out.println("tao:"+tao.get(1,5));
		
		List<BitSet> ci_d = sp2e.Enc(tao); 
		
		BitSet key = (BitSet) ci_d.get(0).clone();
		BitSet delta =(BitSet)  ci_d.get(1).clone();
		
		deltaL.add(delta);     
		
		return key;
	}
	
	

	/**@Step-3 gen an entry for a location
	 * @author Rykie
	 * @i i is the sequence, curcnt is the cnt for its position in Val
	 * @return a location's bitset
	 * @throws Exception 
	 * */
	public static BitSet genEleinVal(String Bid, String op, int curseq, int curcnt) throws Exception{
				
		   
		   int lenPRF = sSetting.getLenPRF();
		   Bitset_ByteArray convert = new Bitset_ByteArray();
		   
		   
		   String mes = Bid+"|"+op;
	
		   String seed = curseq+"|"+curcnt;
		   BitSet G_k= PRFUtils.genFixedKey(seed, lenPRF);
		   BitSet eleVal =PRFUtils.encByPRF(mes, G_k, lenPRF);	   
		
		   
		    //System.out.print("eleval"+eleVal);
		
		return eleVal;
	}
   
	
	


	

	
	
}
