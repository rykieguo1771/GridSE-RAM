package Tools;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.BitSet;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

//import org.omg.CosNaming.BindingIterator;


/*
 * THIS is only for simulating! It it totally not secure in application!!!
 */
public class PRFUtils {
	  
	
	

	  
	  public static BitSet genFixedKey(String sd, int len) throws Exception {   
		 
		  
		  KeyGenerator kgen = KeyGenerator.getInstance("AES");   
		  SecureRandom random = SecureRandom.getInstance("SHA1PRNG");  
          random.setSeed(sd.getBytes());  
          kgen.init(len, random);
          
	      //  kgen.init(128, new SecureRandom(sk.getBytes()));    
	      byte[] kbyte = kgen.generateKey().getEncoded();
	        
		 /* byte[] value = new byte[len/8];
			SecureRandom random = new SecureRandom();
			random.nextBytes(value);	*/			
			BitSet key = Bitset_ByteArray.fromByteArray(kbyte);
			//System.out.print(key.toString()+"\n");
			return key;
	  }
	  
	  
	    
	  public static BitSet encByPRF(String message, BitSet key, int len) throws Exception {   
		  
		  
		//message-String to Byte
		  byte[] mbyte = message.getBytes(StandardCharsets.UTF_8);//string to byte[] 
		  
		  //enc: (bitset to Byte)
		  BitSet ciBitSet = BitSet.valueOf(mbyte);
		  ciBitSet.xor(key); 
         // byte[] ciByte = mbitSet.toByteArray();
          
		//Byte to base64-string
		 // String ciStr = Base64.encode(ciByte);
		  return ciBitSet;
	        
	    } 
	  
	  
	  public static String decByPRF(String ci, BitSet key, int len) throws Exception {   
		  
		  
		  
		   //base64-string to Byte
		    byte[] cibyte = Base64.decode(ci);
           
		   //dec: (Byte to bitset)
		    BitSet ciBitSet = BitSet.valueOf(cibyte);
		    ciBitSet.xor(key); //record it to local
			byte[] mbyte = ciBitSet.toByteArray();

			
		   //Byte to Message-string
			String mstr = new String(mbyte, StandardCharsets.UTF_8);
			
			return mstr;
	        
	    } 
	  
	  
	  
	
	  
	 
	 /* public static void main (String args[]) throws Exception {
		  
		  String rec = "45,add";
		  int len=128;
		  
		  BitSet sk_1 =genFixedKey("sdsds",len);
		  BitSet sk =genFixedKey("sdsds",len);
		  
		  String cipher = encByPRF(rec, sk, len);
		  System.out.println(cipher);	
		  
		  String m = decByPRF(cipher, sk, len);
		  
		  System.out.println(m);	
		  
		  
	  }*/
}
