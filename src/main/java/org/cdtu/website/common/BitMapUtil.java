package org.cdtu.website.common;


import java.util.ArrayList;
import java.util.List;

public class BitMapUtil {
    public final static int BITMAP_SIZE = 2000; //注意同步修改数据库bitmap的varchar的大小

    public static void SetByOffset(Byte[] bitMap, int offset) {
        int byteIndex = offset >> 3;
        int bitIndex = offset % 8;
        // 设置第offset位为1
        bitMap[byteIndex] = (byte) (bitMap[byteIndex] | (1 << bitIndex));
    }

    public static boolean existByOffset(Byte[] bitMap, int offset) {
        int byteIndex = offset >> 3;
        int bitIndex = offset % 8;
        // 设置第offset位为1
        return ((bitMap[byteIndex] >> bitIndex) & 1) == 1;
    }

    public static List<Integer> getAllOffsetPos(Byte[] bitMap) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < bitMap.length; i++) {
            for (int k = 0; k < 8; k++) {
                if (((bitMap[i] >> k) & 1) == 1) {
                    list.add((i << 3) + k);
                }
            }
        }
        return list;
    }

    private static int byteBitCount(int i) {
        i = i - ((i >>> 1) & 0x55);
        i = (i & 0x33) + ((i >>> 2) & 0x33);
        i = (i + (i >>> 4)) & 0x0f;
        //i = i + (i >>> 8);
        //i = i + (i >>> 16);
        return i & 0x3f;
    }

    public static int getBitCount(Byte[] bitMap) {
        int res = 0;
        for (byte b : bitMap) {
            res += byteBitCount(b);

        }
        return res;
    }

    public static Byte[] getXORBitMap(Byte[] bitMap1, Byte[] bitMap2) {
        Byte[] res = new Byte[BITMAP_SIZE];
        for (int i = 0; i < BITMAP_SIZE; i++) {
            res[i] = (byte) (bitMap1[i] ^ bitMap2[i]);
        }
        return res;
    }

    public static Byte[] getANDBitMap(Byte[] bitMap1, Byte[] bitMap2) {
        //System.out.println("bit len "+bitMap1.length+ "  " + bitMap2.length);
        Byte[] res = new Byte[BITMAP_SIZE];
        for (int i = 0; i < BITMAP_SIZE; i++) {
            res[i] = (byte) (bitMap1[i] & bitMap2[i]);
        }
        return res;
    }
}

