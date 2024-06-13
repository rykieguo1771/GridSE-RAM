package SP2E;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Map;

import GridSE.setting;
import Tools.Bitset_ByteArray;
import Tools.SecureHash;

public class SP2E {
	
	
	private static setting sSetting;
	
	public SP2E(setting ss)  throws Exception{  
		sSetting =ss;
	}
	
	

	private static int d= sSetting.getD();
	private static int lenByte_id= sSetting.getLenByte_id();
	private static int lenByte_key =sSetting.getLenByte_sk();
	private static int lenH =sSetting.getLenH();
	private static int f = sSetting.getF();
	private static  Map<Character, byte[]> mKt = sSetting.getSeeds();
	
  
	
	
	public static BitSet PrefDec(int d_p, BitSet tk, BitSet ct) throws Exception{
		
		//System.out.print("\n f:"+f);
		
		tk.xor(ct);
		BitSet flag = (BitSet) tk.get((d_p-1)*f,d_p*f).clone();
		 
		return flag;
		
	}
	
	
	
	/**@author Rykie
	 * @return
	 * (BitSet) tk=genTK()
	 * */
    public static BitSet genTK(String w_p, int d_p, int seq, BitSet delta) throws Exception {
    	
         		
 		
 			
 			SecureHash secureHash = new SecureHash();
 			 BitSet entry = new BitSet();
 			for (int g=1; g<=d_p; g++){
 				byte[] sv_g = mKt.get(w_p.charAt(g-1));
 				byte[] Bytseq =String.valueOf(seq).getBytes();
 				int lenpreH = lenByte_key+lenByte_id+1;		
 				byte[] preH = new byte[lenpreH]; //preH :preimage of H
 				for (int i=0; i<lenByte_key;i++){
 						preH[i]=sv_g[i];
 				}
 				for (int i=lenByte_key; i<lenByte_key+Bytseq.length;i++){
 					preH[i]=Bytseq[i-lenByte_key];
 				}
 				preH[lenpreH-1]=(byte)g;
 				
 				//compute H and current EncryptedInx on g-th of P_{id}
 				 BitSet H = secureHash.getHash(preH);
 				 BitSet curCell = new BitSet();
 					for (int i=0; i<lenH; i++){
 						curCell.set((g-1)*f+i, H.get(i));
 					}
 					curCell.set((g-1)*f+lenH, (d_p-1)*f+lenH, false);
 					entry.xor(curCell);
 					                //System.out.println("the "+g+"-th cell: "+curCell);
 			}
 			
 			BitSet oriTK = (BitSet) entry.get(0,lenH).clone();
 			                        //System.out.println("\n TK before ran: "+subTK.get(120,160));
 			
 			 //1--get \eta1_{i}
 			  // 2--\delta_{i}
 			  // 3--\eta2_{i}
 			byte[] value1 = new byte[(d_p-1)*f/8];
 			SecureRandom random1 = new SecureRandom();
 			random1.nextBytes(value1);				
 			BitSet eta1 = Bitset_ByteArray.fromByteArray(value1);
 			
 			BitSet curSub_delta = (BitSet) delta.get((d_p-1)*f, d_p*f).clone(); 			
 			byte[] value2 = new byte[(int) Math.ceil((lenH-d_p*f)/8-0.0001)];
 			SecureRandom random2 = new SecureRandom();
 			random2.nextBytes(value2);				
 			BitSet eta20 = Bitset_ByteArray.fromByteArray(value2);
 			BitSet eta2=(BitSet) eta20.get(0, lenH-d_p*f).clone();	
 			
 			
 			
 			 //generate \eta1_{i}||\delta_{i}||-\eta2_{i}
 			BitSet ranTK=new BitSet();
 			for(int i=0; i<(d_p-1)*f; i++){
 				ranTK.set(i,eta1.get(i));
 			}
 			for(int i= (d_p-1)*f; i<d_p*f; i++){
 				ranTK.set(i,curSub_delta.get(i-(d_p-1)*f));
 			}
 			for(int i= d_p*f; i<lenH; i++){
 				ranTK.set(i,eta2.get(i-d_p*f));
 			}
 			                         //System.out.println("ranTK: "+ranTK.get(120,160));
 			
 			oriTK.xor(ranTK);
 			                          //System.out.println("The final TK, the "+j+"-th entryTK :"+subTK.get(120,160));
    	
    	
		return oriTK;	
    }
	
	
	
	
	
	
	/**@author Rykie
	 * @return
	 * (BitSet) ct=Enc()
	 * */
	public static List<BitSet> Enc(BitSet ttao) throws Exception {
		
		byte[] value = new byte[lenH/8];
		SecureRandom random = new SecureRandom();
		random.nextBytes(value);				
		BitSet delta = Bitset_ByteArray.fromByteArray(value);
		                    //System.out.println("deltaLT:"+delta.get(120, 160));		
		
		ttao.xor(delta); 
		
		
		List<BitSet> ct_and_delta = new ArrayList<BitSet>();
		ct_and_delta.add(ttao);
		ct_and_delta.add(delta);
		

		return ct_and_delta;
		
	}
	
	
	
	/**@author Rykie
	 * @return
	 * (BitSet)tau =preEnc()
	 * */
	public static BitSet PreEnc(String ccurCode, String ccurBid) throws Exception {
		
		
				
			
			SecureHash secureHash = new SecureHash();
			 BitSet EncInx = new BitSet();
			for (int g=1; g<=d; g++){		
				byte[] sv_g = mKt.get(ccurCode.charAt(g-1));
				byte[] BytcurCnt =ccurBid.getBytes();
				int lenpreH = lenByte_key+lenByte_id+1;		
				byte[] preH = new byte[lenpreH]; //preH :preimage of H
				for (int i=0; i<lenByte_key;i++){
						preH[i]=sv_g[i];
				}
				for (int i=lenByte_key; i<lenByte_key+BytcurCnt.length;i++){
					preH[i]=BytcurCnt[i-lenByte_key];
				}
				preH[lenpreH-1]=(byte)g;
				
				 
				 //compute H and current EncryptedInx on g-th of P_{id}
				 BitSet H = secureHash.getHash(preH);
				 BitSet curEncInx = new BitSet();
					for (int i=0; i<lenH; i++){
						curEncInx.set((g-1)*f+i, H.get(i));
					}
					curEncInx.set((g-1)*f+lenH, (d-1)*f+lenH, false);
					                 //System.out.println("the "+g+"-h INXcell:"+ curEncInx);
					EncInx.xor(curEncInx);
			}
			//subEncInx is a single inxEntry
			BitSet tao = (BitSet) EncInx.get(0,lenH).clone();
		                            //	System.out.println("INX before ran: "+subEncInx.get(120, 160));
			
			
			return tao;
			

		
	}




}
