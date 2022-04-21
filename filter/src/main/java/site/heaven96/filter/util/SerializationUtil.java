package site.heaven96.filter.util;



import java.io.*;

public class SerializationUtil {
    public static byte[] serialize(Object state) {
        ObjectOutputStream oos = null;
        byte abyte[];
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            oos.writeObject(state);
            oos.flush();
            abyte = bos.toByteArray();
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
        if (oos != null)
            try {
                oos.close();
            } catch (IOException ioexception) {
            }
        return abyte;
    }
}