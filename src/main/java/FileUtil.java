
import java.io.*;
import java.util.Arrays;
import java.util.List;

public class FileUtil {

    public static String readFile(String file) {
        StringBuffer sb = new StringBuffer("");

        FileReader reader;
        try {
            reader = new FileReader(file);
            BufferedReader br = new BufferedReader(reader);

            String str = null;

            while ((str = br.readLine()) != null) {
                sb.append(str);
            }

            br.close();
            reader.close();

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return sb.toString();

    }

    public static void fileWriter(String filePath,String content) {
        try {
            File file = new File(filePath);

            // if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            // true = append file
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static byte[] toByteArray(String filePath){

        ByteArrayOutputStream bos=null;
        BufferedInputStream in = null;
        try {
            File f = new File(filePath);
            if(f.exists()){
                in = new BufferedInputStream(new FileInputStream(f));
                bos = new ByteArrayOutputStream((int) f.length());

                int buf_size = 1024;
                byte[] buffer = new byte[buf_size];
                int len = 0;
                while (-1 != (len = in.read(buffer, 0, buf_size))) {
                    bos.write(buffer, 0, len);
                }
                //return bos.toByteArray();
            }else{
                System.out.println("file not exists");
            }

        } catch (IOException e) {
            e.printStackTrace();
           // Log.error("toByteArray() Exception", e);
        } finally {
            try {
                in.close();
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
                //Log.error("toByteArray() Exception",e);
            }
        }
        return bos.toByteArray();
    }

    public static void writeBytesToFile(String filePath,byte[] bytes){
        File file = new File(filePath);
        FileOutputStream fop = null;

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        try {
            fop = new FileOutputStream(file);
            fop.write(bytes);

            fop.flush();
            fop.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                if (fop != null) {
                    fop.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 序列化,List
     */
    public static <T> boolean writeObject(List<T> list,File file)
    {
        T[] array = (T[]) list.toArray();
        try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file)))
        {
            out.writeObject(array);
            out.flush();
            return true;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 反序列化,List
     */
    public static <E> List<E> readObjectForList(File file)
    {
        E[] object;
        try(ObjectInputStream out = new ObjectInputStream(new FileInputStream(file)))
        {

            object = (E[]) out.readObject();
            return Arrays.asList(object);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}