package org.light.rtc.util;

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

public class Murmurs {
	
	/**
     * murmur hash算法实现
     */
    public static Long hash(byte[] key) {
        ByteBuffer buf = ByteBuffer.wrap(key);
        int seed = 0x1234ABCD;

        ByteOrder byteOrder = buf.order();
        buf.order(ByteOrder.LITTLE_ENDIAN);

        long m = 0xc6a4a7935bd1e995L;
        int r = 47;

        long h = seed ^ (buf.remaining() * m);
        long k;
        while (buf.remaining() >= 8) {
            k = buf.getLong();

            k *= m;
            k ^= k >>> r;
            k *= m;

            h ^= k;
            h *= m;
        }

        if (buf.remaining() > 0) {
            ByteBuffer finish = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);
            finish.put(buf).rewind();
            h ^= finish.getLong();
            h *= m;
        }

        h ^= h >>> r;
        h *= m;
        h ^= h >>> r;

        buf.order(byteOrder);
        return h;
    }

    public static Long hash(String key) {
        return hash(key.getBytes());
    }



    /**
     * Long转换成无符号长整型（C中数据类型）
     */
    public static BigDecimal readUnsignedLong(long value) {
        if (value >= 0)
            return new BigDecimal(value);
        long lowValue = value & 0x7fffffffffffffffL;
        return BigDecimal.valueOf(lowValue).add(BigDecimal.valueOf(Long.MAX_VALUE)).add(BigDecimal.valueOf(1));
    }

    /**
     * 返回无符号murmur hash值
     */
    public static BigDecimal hashUnsigned(String key) {
        return readUnsignedLong(hash(key));
    }
    
    /**
     * 返回长整形murmur hash值
     */
    public static long hashLong(String key) {
        return readUnsignedLong(hash(key)).longValue();
    }
    
    public static BigDecimal hashUnsigned(byte[] key) {
        return readUnsignedLong(hash(key));
    }
    
    /**
     * 返回字符串所对应字符相关数字之和
     * @param key
     * @return
     */
    public static long getCharSum(String key){
    	if(key!=null){
    		long sum = 0;
    		for(int t : key.toCharArray()){
    			sum += t;
    		}
    		return sum;
    	}else{
    		return -1;
    	}
    }
    
    private static HashFunction m3128 = Hashing.murmur3_128();
    
    /**
     * 返回字符串所对应的Google Guava中的Murmurs的哈希值。
     * @param key
     * @return
     */
    public static long getGuavaMurmurs(String key){
    	long rtNum = m3128.hashUnencodedChars(key).padToLong();
    	if(rtNum<0){
    		rtNum = Math.abs(rtNum);
    	}
    	return rtNum;
    }
	
}
