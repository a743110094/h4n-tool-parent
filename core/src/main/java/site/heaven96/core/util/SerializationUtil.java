package site.heaven96.core.util;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * 序列化工具
 *
 * @author heaven96
 * @date 2022/04/21
 */
public class SerializationUtil {
    public static byte[] serialize(Serializable state) {
        //fixed issue 01 NPE @ 2022-4-22 20:15
        if (null == state) {
            return new byte[0];
        }
        //
        ObjectOutputStream oos;
        byte[] b;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            oos.writeObject(state);
            oos.flush();
            b = bos.toByteArray();
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
        if (oos != null) {
            try {
                oos.close();
            } catch (IOException ioexception) {
            }
        }
        return b;
    }
}