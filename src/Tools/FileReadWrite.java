package Tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;



public class FileReadWrite {
	
	/**
     * ���ܣ�Java��ȡtxt�ļ������� ���裺1���Ȼ���ļ���� 2������ļ��������������һ���ֽ���������Ҫ��������������ж�ȡ
     * 3����ȡ������������Ҫ��ȡ�����ֽ��� 4��һ��һ�е������readline()�� ��ע����Ҫ���ǵ����쳣���
     * 
     * @param filePath
     *            �ļ�·��[�����ļ�:�磺 D:\aa.txt]
     * @return ������ļ�����ÿһ���и�������ŵ�list�С�
     */
    public List<String> readFileToStringList(String filePath)
    {

    	List<String> list = new ArrayList<String>();
        try
        {
            String encoding = "GBK";
            File file = new File(filePath);
            if (file.isFile() && file.exists())
            { // �ж��ļ��Ƿ����
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file), encoding);// ���ǵ������ʽ
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;

                while ((lineTxt = bufferedReader.readLine()) != null)
                {
                	//System.out.println(lineTxt);
                    list.add(lineTxt);
                }
                bufferedReader.close();
                read.close();
            }
            else
            {
                System.out.println("�Ҳ���ָ�����ļ�");
            }
        }
        catch (Exception e)
        {
            System.out.println("��ȡ�ļ����ݳ���");
            e.printStackTrace();
        }

        return list;
    }
    
 public static void StringToFile(String file, String conent) {
    	BufferedWriter out = null;
    	try {
    	out = new BufferedWriter(new OutputStreamWriter(
    	new FileOutputStream(file, true)));
    	out.write(conent+"\r\n");
    	} catch (Exception e) {
    	e.printStackTrace();
    	} finally {
    	try {
    	out.close();
    	} catch (IOException e) {
    	e.printStackTrace();
    	}
      }
    } 

}
