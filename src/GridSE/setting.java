package GridSE;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class setting {

	private static final char[] CHARS = {'0', '1', '2', '3', '4', '5', '6', '7',   
            '8', '9', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'm', 'n',   
            'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'}; 
	 

    
	
	private  static int f =20;  // f: 16, 20
	private   static int d =7; // index code length
	private   static int lenByte_sk =16; //the length for all keys, 128
	private   static int lenByte_id =5; //id-Bytes
	private  static int lenH =256; //bits-lengt;
	private  static int lenPRF = 128;
	private static int N; //for DB

	public static int getN() {
		return N;
	}




	public static void setN(int n) {
		N = n;
	}






	private  static Map<Character, byte[]> seeds = Gen_mapKt();
    private  static String skPRF = "lucky2023sp";
    private static List<BitSet> delta =  new ArrayList<BitSet>(); 
    

    private static Map<String, Integer> BCnt =new LinkedHashMap<String, Integer>();;
    
    

    
	public static int getLenPRF() {
		return lenPRF;
	}




	public static void setLenPRF(int lenPRF) {
		setting.lenPRF = lenPRF;
	}




	public static Map<String, Integer> getBCnt() {
		return BCnt;
	}




	public static void setBCnt(Map<String, Integer> bCnt) {
		BCnt = bCnt;
	}




	public static Map<Character, byte[]> getSeeds() {
		return seeds;
	}




	public static List<BitSet> getDelta() {
		return delta;
	}



	public static void setDelta(List<BitSet> delta) {
		setting.delta = delta;
	}



	


	public static String getSkPRF() {
		return skPRF;
	}




	public static void setSkPRF(String skPRF) {
		setting.skPRF = skPRF;
	}





	public static void setSeeds(Map<Character, byte[]> seeds) {
		setting.seeds = seeds;
	}






	public static int getF() {
		return f;
	}






	public static void setF(int f) {
		setting.f = f;
	}






	public static int getD() {
		return d;
	}






	public static void setD(int d) {
		setting.d = d;
	}






	public static int getLenByte_sk() {
		return lenByte_sk;
	}






	public static void setLenByte_sk(int lenByte_sk) {
		setting.lenByte_sk = lenByte_sk;
	}






	public static int getLenByte_id() {
		return lenByte_id;
	}






	public static void setLenByte_id(int lenByte_id) {
		setting.lenByte_id = lenByte_id;
	}






	public static int getLenH() {
		return lenH;
	}






	public static void setLenH(int lenH) {
		setting.lenH = lenH;
	}






	public static Map<Character, byte[]> Gen_mapKt() {
		
		Map<Character, byte[]>  mKt = new HashMap<Character, byte[]>();
		
				for(int i=0; i<32; i++){		
			byte[] value = new byte[lenByte_sk];
			SecureRandom random = new SecureRandom();
			random.nextBytes(value);			
			mKt.put(CHARS[i], value);
			
		}
		return mKt;
	}
}
