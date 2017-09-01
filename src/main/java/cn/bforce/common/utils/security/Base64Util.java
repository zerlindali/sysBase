package cn.bforce.common.utils.security;



/**
 * Title:        Password.java
 * Description:  密码转化操作
 * Copyright:    Copyright (c) 2013
 * <p>Company: Yuyanhui</p>
 * @author zf
 * @version 1.0
 */

public class Base64Util
{
        public static String put(String s)
        {
                if(s==null||s.equals(""))
                        return "";
                else
                return base64Encode(s);
        }
        public static String get(String s)
        {
                if(s==null||s.equals(""))
                        return "";
                else

                return base64Decode(s);


        }
        private static char base2Char(int n) {
        n &= 0x3F;
        if (n < 26) return (char)(n + 'A');
        else if (n < 52) return (char)(n - 26 + 'a');
        else if (n < 62) return (char)(n - 52 + '0');
        else if (n == 62) return '+';
        else return '/';
    }
    private static byte char2Base(char c) {
        if (c >= 'A' && c <= 'Z') return (byte)(c - 'A');
        else if (c >= 'a' && c <= 'z') return (byte)(c - 'a' + 26);
        else if (c >= '0' && c <= '9') return (byte)(c - '0' + 52);
        else if (c == '+') return 62;
        else return 63;
    }

    private static String base64Encode(String str) {
        byte c;
        int t = 0;
        byte[] s = str.getBytes();
        StringBuffer temp = new StringBuffer(((s.length+2)/3)*4);
        for (int i=1 ; i<=s.length ; i++) {
            c = s[i-1];
            if (i%3 == 1) {
                temp.append(base2Char(c >>> 2));
                t = (c << 4) & 0x3F;
            } else if (i%3 == 2) {
                temp.append(base2Char(t | (c >>> 4) & 0x0F));
                t = (c << 2) & 0x3F;
            } else {
                temp.append(base2Char(t | (c >>> 6) & 0x03));
                temp.append(base2Char((int)c));
            }
        }
        if (s.length%3 != 0) {
            temp.append(base2Char(t));
            if (s.length%3 == 1) temp.append("==");
            else temp.append("=");
        }
        return temp.toString();
    }

    private static String base64Decode(String str) {
        byte c;
        int t = 0;
        byte[] s = str.getBytes();
        StringBuffer temp = new StringBuffer(s.length*3/4 - 2);
        for (int i=1 ; i<=s.length ; i++) {
            if (s[i-1] == '=') break;
            c = char2Base((char)s[i-1]);
            if (i%4 == 1) t = (c << 2) & 0xFC;
            else if (i%4 == 2) {
                temp.append((char)(t | (c >> 4) & 0x0F));
                t = (c << 4) & 0xF0;
            } else if (i%4 == 3) {
                temp.append((char)(t | (c >> 2) & 0x3F));
                t = (c << 6) & 0xC0;
            } else temp.append((char)(t | c));
        }
        try {
            str = new String(temp.toString().getBytes("iso8859-1"));
        } catch (Exception e) {
            str = temp.toString();
        }
        return str;
    }
    public static void main(String args[]) {

    	System.out.println(Base64Util.base64Encode("tsou"));
    	System.out.println(Base64Util.base64Encode("123456"));
    	System.out.println(Base64Util.base64Encode("root"));
    	System.out.println(Base64Util.base64Encode("root"));
    	
    }

}
