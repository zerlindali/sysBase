package cn.bforce.common.utils;


import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class ExStringUtils
{

    public static boolean isEmail(String email)
    {
        if (email == null)
        {
            return false;
        }
        String str = "^([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    public static boolean isMobile(String mobile)
    {
        if (mobile == null)
        {
            return false;
        }
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^[1][3,4,5,8][0-9]{9}$"); // 验证手机号
        m = p.matcher(mobile);
        b = m.matches();
        return b;
    }

    public static String concat(String[] values)
    {
        if (values == null)
        {
            return null;
        }
        StringBuffer buffer = new StringBuffer();
        for (String value : values)
        {
            if (buffer.length() > 0)
            {
                buffer.append(",");
            }
            buffer.append(value);
        }
        return buffer.toString();
    }

    public static String trimAllSpace(String str)
    {
        return str == null ? str : str.replaceAll("^[\\s　]*|[\\s　]*$", "");
    }

    public static String[] split(String aStr, String split)
    {

        String[] columnNames = StringUtils.split(aStr, split);
        if (columnNames != null)
        {
            for (int i = 0; i < columnNames.length; i++ )
            {
                if (columnNames[i] != null)
                {
                    columnNames[i] = columnNames[i].trim();
                }
                else
                {
                    columnNames[i] = "";
                }
            }
        }

        return columnNames;
    }

    public static boolean isInclude(String item, String[] items)
    {
        if (items != null && item != null)
        {
            for (int i = 0; i < items.length; i++ )
            {
                if (item.equalsIgnoreCase(items[i]))
                {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isBlank(String argStr)
    {
        return StringUtils.isBlank(argStr);
    }

    public static boolean isNotBlank(String argStr)
    {
        return StringUtils.isNotBlank(argStr);
    }

    public static boolean isEmpty(String argStr)
    {
        return StringUtils.isEmpty(argStr);
    }

    public static boolean isNotEmpty(String argStr)
    {
        return StringUtils.isNotEmpty(argStr);
    }

    public static String getDocDesc(String docValue)
    {
        if (docValue != null)
        {
            int beginIndex = docValue.indexOf("[");
            return docValue.substring(0, beginIndex);
        }
        return null;
    }

    public static String getDocNum(String docValue)
    {
        if (docValue != null)
        {
            int beginIndex = docValue.indexOf("[");
            int endIndex = docValue.indexOf("]");
            if (endIndex > beginIndex)
            {
                return docValue.substring(beginIndex + 1, endIndex);
            }
        }
        return null;
    }

    public static int parseInt(String str)
    {
        return parseInt(str, -1);
    }

    public static int parseInt(String str, int defaultValue)
    {
        int intValue = defaultValue;
        try
        {
            if ("null".equalsIgnoreCase(str))
            {
                intValue = defaultValue;
            }
            else
            {
                intValue = Integer.parseInt(str);
            }

        }
        catch (Exception ex)
        {

        }
        return intValue;
    }

    public static long parseLong(String str, long defaultValue)
    {
        long longValue = defaultValue;
        try
        {
            if ("null".equalsIgnoreCase(str))
            {
                longValue = defaultValue;
            }
            else
            {
                longValue = Long.parseLong(str);
            }

        }
        catch (Exception ex)
        {

        }
        return longValue;
    }

    public static int parseVerNum(String str)
    {
        int verNum = 200;
        try
        {
            str = str.replaceAll("\\.", "");
            verNum = Integer.parseInt(str);
        }
        catch (Exception ex)
        {

        }
        return verNum;
    }

    public static String getOption(String optionsStr, String keyName, String defValue)
    {
        if (optionsStr != null)
        {
            String[] options = split(optionsStr, ",");
            for (int i = 0; i < options.length; i++ )
            {
                if (options[i] != null && options[i].startsWith(keyName))
                {
                    String value = options[i].substring(keyName.length() + 1);
                    return value;
                }
            }
        }
        return defValue;
    }

    /**
     * 随机码
     * 
     * @param codeLen
     * @return
     */
    public static String getVerifyCode(int codeLen)
    {
        int count = 0;
        char str[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        StringBuffer verifyCode = new StringBuffer("");
        Random r = new Random();
        while (count < codeLen)
        {
            int i = Math.abs(r.nextInt(10));
            if (i >= 0 && i < str.length)
            {
                verifyCode.append(str[i]);
                count++ ;
            }
        }
        return verifyCode.toString();
    }

    /**
     * 首字母大写
     * 
     * @param srcStr
     * @return
     */
    public static String firstCharToUpper(String srcStr)
    {
        return srcStr.substring(0, 1).toUpperCase() + srcStr.substring(1);
    }

    /**
     * 替换字符串并让它的下一个字母为大写
     * 
     * @param srcStr
     * @param org
     * @param ob
     * @return
     */
    public static String replaceUnderlineAndfirstToUpper(String srcStr)
    {
        String newString = "";
        String org = "_";
        String ob = "";

        int first = 0;
        while (srcStr.indexOf(org) != -1)
        {
            first = srcStr.indexOf(org);
            if (first != srcStr.length())
            {
                newString = newString + srcStr.substring(0, first) + ob;
                srcStr = srcStr.substring(first + org.length(), srcStr.length());
                srcStr = firstCharToUpper(srcStr);
            }
        }
        newString = newString + srcStr;
        return newString;
    }

    public static String parseTableAlias(String tableName)
    {

        int beginIndex = tableName.indexOf("_");
        int endIndex = tableName.lastIndexOf("_");
        if (beginIndex < 0)
        {
            return tableName;
        }
        else if (beginIndex == endIndex)
        {
            return tableName.substring((beginIndex + 1));
        }

        int tmpInt = -1;
        String suffix = tableName.substring(endIndex);
        if (suffix != null && suffix.length() > 1)
        {
            try
            {
                tmpInt = Integer.parseInt(suffix.substring(1));
            }
            catch (Exception e)
            {
                tmpInt = -1;
            }
        }
        if (tmpInt > 0)
        {
            return tableName.substring((beginIndex + 1), endIndex);
        }

        return tableName.substring((beginIndex + 1));
    }

    public static String getTableAlias(String tableName)
    {

        String aliasName = parseTableAlias(tableName);

        if ("user".equalsIgnoreCase(aliasName))
        {
            aliasName = "app" + aliasName;
        }

        return aliasName;
    }

    public static String[] mergeStrArray(String[] array1, String[] array2)
    {

        List<String> list = new ArrayList<String>();
        if (array1 != null)
        {
            for (String s : array1)
            {
                list.add(s);
            }
        }
        if (array2 != null)
        {
            for (String s : array2)
            {
                list.add(s);
            }
        }

        return list.toArray(new String[0]);

    }

    public static String[] mergeStrArray(String[] array1, String[] array2, String[] array3)
    {

        String[] tmpArray = mergeStrArray(array1, array2);

        return mergeStrArray(tmpArray, array3);

    }

    public static boolean isBoolean(Object value, boolean defaultVal)
    {

        if (value != null && value instanceof Integer)
        {
            if ((Integer)value == 1)
            {
                return true;
            }
            else if ((Integer)value == 0)
            {
                return false;
            }
        }
        else if (value != null && value instanceof Boolean)
        {
            return (Boolean)value;
        }
        else if (value != null)
        {
            return getBoolean(value.toString(), defaultVal);
        }

        return defaultVal;
    }

    public static boolean getBoolean(String val, boolean defaultVal)
    {
        if ("No".equalsIgnoreCase(val))
        {
            return false;
        }
        else if ("N".equalsIgnoreCase(val))
        {
            return false;
        }
        else if ("false".equalsIgnoreCase(val))
        {
            return false;
        }
        else if ("0".equalsIgnoreCase(val))
        {
            return false;
        }
        else if ("Yes".equalsIgnoreCase(val))
        {
            return true;
        }
        else if ("Y".equalsIgnoreCase(val))
        {
            return true;
        }
        else if ("true".equalsIgnoreCase(val))
        {
            return true;
        }
        else if ("1".equalsIgnoreCase(val))
        {
            return true;
        }
        return defaultVal;
    }

    public static boolean containIgnoreCase(String s1, String s2)
    {

        if (s1 == null && s2 == null)
        {
            return true;
        }

        if (s1 != null && s2 != null)
        {
            s1 = s1.toLowerCase();
            s2 = s2.toLowerCase();
            if (s1 != null && s1.contains(s2))
            {
                return true;
            }
        }

        return false;
    }

    /**
     * 文件字节数换算
     * 
     * @param sizeDesc
     * @return
     */
    public static long parseFileSize(String sizeDesc)
    {

        if (sizeDesc != null)
        {

            long maxSize = 1024000;
            int size = 2;
            if (sizeDesc.toUpperCase().endsWith("M"))
            {
                try
                {
                    size = Integer.parseInt(sizeDesc.substring(0, (sizeDesc.length() - 1)));
                }
                catch (Exception ex)
                {
                    size = 2;
                }
            }

            return maxSize * size;
        }

        return 0;

    }

    public static String parseFileName(String fullPath)
    {
        String fileName = null;
        if (fullPath == null)
        {
            return null;
        }
        else
        {
            int index = fullPath.lastIndexOf("/");
            if (index >= 0)
            {
                fileName = fullPath.substring(index + 1);
            }

        }

        return fileName;

    }

    /**
     * 生成随机码
     * 
     * @param codeLen
     * @return
     */
    public static String genIndexCode(int codeLen)
    {

        StringBuffer tmpBuff = new StringBuffer(
            "a,b,c,d,e,f,g,h,i,g,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z");
        tmpBuff.append(",A,B,C,D,E,F,G,H,I,G,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z");
        tmpBuff.append(",1,2,3,4,5,6,7,8,9,0");

        java.util.Random r = new Random();
        String[] strArray = tmpBuff.toString().split(",");

        StringBuffer resultBuff = new StringBuffer();

        for (int i = 0; i < codeLen; i++ )
        {
            int k = r.nextInt();
            resultBuff.append(String.valueOf(strArray[Math.abs(k % 62)]));
        }

        return resultBuff.toString();

    }

    /**
     * 生成随机数
     * 
     * @param codeLen
     * @return
     */
    public static long genRandNum(int codeLen)
    {

        StringBuffer tmpBuff = new StringBuffer("1,2,3,4,5,6,7,8,9,0");

        java.util.Random r = new Random();
        String[] strArray = tmpBuff.toString().split(",");

        StringBuffer resultBuff = new StringBuffer();

        for (int i = 0; i < codeLen; i++ )
        {
            int k = r.nextInt();
            resultBuff.append(String.valueOf(strArray[Math.abs(k % 10)]));
        }
        return Long.parseLong(resultBuff.toString());

    }

    /**
     * 生成流水序号
     * 
     * @param seqNum
     * @param length
     * @return
     */
    public static String genSerialNo(int seqNum, int length)
    {

        String seqStr = String.valueOf(seqNum);
        while (seqStr.length() < length)
        {
            seqStr = "0" + seqStr;
        }

        return seqStr;
    }

    /**
     * 解析URL参数
     * 
     * @param inputStr
     * @return
     */
    public static Map parseUrlParameters(String inputStr)
    {
        Map dataMap = new HashMap();
        if (inputStr == null)
        {
            return null;
        }
        int charCount = countChar(inputStr, '%');

        if (inputStr.contains("%26") || inputStr.contains("%23") || inputStr.contains("%40")
            || inputStr.contains("%3D") || charCount > 2)
        {
            try
            {
                inputStr = URLDecoder.decode(inputStr, "UTF-8");
            }
            catch (UnsupportedEncodingException e)
            {}
        }
        String[] segments = ExStringUtils.split(inputStr, "&");
        int begin;
        for (int i = 0; i < segments.length; i++ )
        {
            begin = segments[i].indexOf("=");
            if (begin > 0)
            {
                dataMap.put(segments[i].substring(0, begin), segments[i].substring(begin + 1));
            }
        }
        return dataMap;
    }

    /**
     * JSON转Map
     * 
     * @param inputStr
     * @return
     */
    public static Map parseJsonParameters(String inputStr)
    {

        if (inputStr == null)
        {
            return null;
        }

        try
        {

            Map dataMap = null;
            JSONObject jsonData = JSON.parseObject(inputStr);
            if (jsonData != null)
            {
                dataMap = new HashMap();
                for (String key : jsonData.keySet())
                {
                    dataMap.put(key, jsonData.get(key));
                }
            }
            return dataMap;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return null;
        }

    }

    /**
     * JSONArray转List<Map>
     * 
     * @param inputStr
     * @return
     */
    public static List<Map> parseJsonArrayParameters(String inputStr)
    {
        List<Map> dataList = new ArrayList<Map>();
        if (inputStr == null)
        {
            return null;
        }

        try
        {
            List<JSONObject> jsonList = JSON.parseArray(inputStr, JSONObject.class);  
            if (jsonList != null)
            {
                for (JSONObject jsonData : jsonList)
                {
                    if (jsonData != null)
                    {
                        Map dataMap = new HashMap();
                        for (String key : jsonData.keySet())
                        {
                            dataMap.put(key, jsonData.get(key));
                        }
                        dataList.add(dataMap);
                    }
                }
            }
            return dataList;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return null;
        }

    }

    public static int countChar(String input, char ch)
    {
        int count = 0;

        if (input == null)
        {
            return 0;
        }

        for (int i = 0; i < input.length(); i++ )
        {
            if (input.charAt(i) == ch)
            {
                count++ ;
            }
        }
        return count;
    }

    /**
     * <p class="detail">
     * 功能：校验字段是否为空
     * </p>
     * @author Zerlinda
     * @date 2017年9月13日 
     * @param keyArr 需要校验的必传字段名
     * @param valueArr 需要校验的必传字段值
     * @param paramMap 参数
     * @return
     */
    public static String checkParams(String[] keyArr, String[] valueArr, Map paramMap){
        String result = "";
        for(int i = 0; i < keyArr.length; i++){
           String key = keyArr[i];
           if(!paramMap.containsKey(key)){
               result += valueArr[i]+",";
           }
        }
        if(isNotBlank(result)){
            result = result.substring(0, result.length()-1)+"不能为空";
        }
        return result;
    }
    
    public static Map buildResultMessage(int resultCode, String resultMsg)
    {
        Map resultMap = new HashMap();
        resultMap.put("resultCode", resultCode);
        resultMap.put("resultMsg", resultMsg);
        return resultMap;
    }

    public static Long getLongValue(Object value)
    {

        if (value != null)
        {
            if (value instanceof BigDecimal)
            {
                return ((BigDecimal)value).longValue();
            }
            else if (value instanceof Integer)
            {
                return ((Integer)value).longValue();
            }
            else if (value instanceof Double)
            {
                return ((Double)value).longValue();
            }
            else if (value instanceof String)
            {
                return Long.parseLong((String)value);
            }
            else
            {
                return (Long)value;
            }
        }

        return null;
    }

    public static Integer getIntValue(Object value)
    {

        if (value != null)
        {
            if (value instanceof BigDecimal)
            {
                return ((BigDecimal)value).intValue();
            }
            else if (value instanceof Long)
            {
                return ((Long)value).intValue();
            }
            else if (value instanceof Double)
            {
                return ((Double)value).intValue();
            }
            else if (value instanceof String)
            {
                if (isBlank((String)value))
                {
                    return -1;
                }
                return Integer.parseInt((String)value);
            }
            else
            {
                return (Integer)value;
            }
        }

        return null;
    }
}
