package GridSE;

import java.math.RoundingMode;
import java.security.SecureRandom;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.IllegalFormatWidthException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import GridSE.*;
import Tools.FileReadWrite;

public class main {
	
	public static void main(String args[]) throws Exception {
	
	String fOUT = "/Users/Grykie/eclipse-workspace/GridSE_RAM/src/Record.txt"; 
	
	
	NumberFormat nf = NumberFormat.getNumberInstance();
    nf.setMaximumFractionDigits(15); 
    nf.setRoundingMode(RoundingMode.DOWN);
	/**@Step-1 Build the encrypted index
	 * @Output file2: dataOut.Server/cipherIndex.txt
	 * @author Rykie
	 * */
    //String sk_DEC = "good";  
    
    setting setup =new setting();
    
    
    /*update all blocks from DB*/
	update eIndex = new update(setup); 
	 Map<Character, byte[]>  mSV = setup.Gen_mapKt();
	        long timEnc1= System.currentTimeMillis();
	 Map<BitSet, List<BitSet>> InxDict = eIndex.updateAllInx();
	        long timEnc2= System.currentTimeMillis();
	        double timEnc=timEnc2-timEnc1;
	 
	 
	 /*update single block*/
	        long timUp1= System.nanoTime();
	 eIndex.updateAnEntryInx("9", "add", "9mudjubez0");
	        long timUp2= System.nanoTime();
	        double timUpdate=timUp2-timUp1;
	 
	 /*search "9mus6w2650: 7-1650, " "9mudjubez0: 7-2800"  "9muqfm49en: 7-2250" "9mudn4e"*/ 
	 // String w_p ="9mus"; int d_p = 4; //44910
	  //  String w_p ="9muqfm"; int d_p = 6; //8910
	 String w_p ="9mus6"; int d_p = 5; //14940   
      // String w_p ="9mudn4e"; int d_p=7; //1530
	//   String w_p="ucky777"; int d_p = 7; //130

	 search ssearch = new search(setup);
	      long timSrh1= System.currentTimeMillis(); 
	List<String> I_w_p= ssearch.decryptAtClient(ssearch.searchAtServer(w_p, d_p, InxDict));
	      long timSrh2= System.currentTimeMillis();
      	  double timSrh=timSrh2-timSrh1;	              		
      		
      	  

     int delta_num = InxDict.size();
		//sweep R
     double sizeR_w_p = (setup.getLenPRF()* I_w_p.size())/8.0;  //Byte
     double sizeUpdate = (setup.getLenPRF() + setup.getLenH())/8.0;
     
     double sizeLocal = ((delta_num*setup.getF()*setup.getD())/8.0+ delta_num*2)/1024.0; //KB
      	  
      	String record0 ="Settings-- |DB|="+setup.getN()+"; |R|="+ I_w_p.size()+"; |w_p|=" +d_p +"; |w|="+setup.getD()+ "; f="+setup.getF()+"; \n";
    	String record1 ="1. size of Communication :"+ sizeR_w_p+"Byte.\n";
      	String record2 = "2. time of Encryption : "+nf.format(timEnc/6) +"ms.\n"; 
      	String record3 = "3. time of a single search is : "+nf.format(timSrh/6) +"ms.\n";
      	String record4 = "4. time of Update : "+nf.format(timUpdate/6) +"ns;" +" size of Update : "+nf.format(sizeUpdate/6) +"Byte;" ; 
      	
      	String record5 = "5. number of distinct index-key |W| : "+nf.format(InxDict.size()) +";" ; 
   
          		
          System.out.print(record0+record1+record2+record3+record4+record5);
          
         	System.out.print("\n num of local delta, also as |W| :"+delta_num + ";  size of local delta :"+ sizeLocal);
      	 
	}

}
