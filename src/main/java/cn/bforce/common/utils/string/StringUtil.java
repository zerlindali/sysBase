package cn.bforce.common.utils.string;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;


public class StringUtil
{

    /**
     * 描述：字符串判空
     * 
     * @param
     * @return boolean true：为空，false：非空
     */
    public static boolean isNullOrEmpty(String value)
    {
        return value == null || "".equals(value);
    }

    /**
     * <p class="detail"> 功能：非空判断: 包含 " " </p>
     * 
     * @author wuxw
     * @param value
     * @return
     * @throws
     */
    public static boolean isNotBlank(String value)
    {
        return StringUtils.isBlank(value) == false;
    }

    /**
     * 描述：获取UUID值
     * 
     * @param
     * @return String
     */
    public static String getUUID()
    {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /****
     * 验证字符串是否为纯数字
     * 
     * @param number
     * @return
     */
    public static boolean isValidInteger(String str)
    {
        if (StringUtil.isNullOrEmpty(str)) return false;
        boolean flag = false;
        Pattern p = Pattern.compile("^\\d*$");
        if (str != null)
        {
            Matcher match = p.matcher(str.trim());
            flag = match.matches();
        }
        return flag;
    }

    /**
     * <p class="detail"> 功能：生成随机数 </p>
     * 
     * @author liuwh
     * @date 2016-2-23
     * @return
     */
    public static String number()
    {
        int machineId = 1;// 最大支持1-9个集群机器部署
        int hashCodeV = UUID.randomUUID().toString().hashCode();
        if (hashCodeV < 0)
        {// 有可能是负数
            hashCodeV = -hashCodeV;
        }
        // 0 代表前面补充0
        // 4 代表长度为4
        // d 代表参数为正数型
        return machineId + String.format("%015d", hashCodeV);
    }

    public static String moneyS(Double money)
    {
        DecimalFormat dcmFmt = new DecimalFormat("0.00");
        String n = dcmFmt.format(money);
        return n;
    }

    public static String format(String str, String last)
    {
        if (str.lastIndexOf(last) > 0)
        {
            return str.substring(0, str.lastIndexOf(last));
        }
        return str;
    }

    /***
     * 比较大小 int类型,-1表示小于,0是等于,1是大于
     * 
     * @param m1
     * @param m2
     * @return
     */
    public static int compareSizes(String m1, String m2)
    {
        BigDecimal d1 = new BigDecimal(m1);
        BigDecimal d2 = new BigDecimal(m2);
        return d1.compareTo(d2);
    }

    /***
     * 货币相加
     * 
     * @param m1
     * @param m2
     * @return
     */
    public static String addCount(String m1, String m2)
    {
        BigDecimal d1 = new BigDecimal(m1);
        BigDecimal d2 = new BigDecimal(m2);
        return d1.add(d2).toString();
    }

    /***
     * 两数相减
     * 
     * @param m1
     * @param m2
     * @return
     */
    public static String subCount(String m1, String m2)
    {
        BigDecimal d1 = new BigDecimal(m1);
        BigDecimal d2 = new BigDecimal(m2);
        return d1.subtract(d2).toString();
    }

    /***
     * 两数相乘
     * 
     * @param m1
     * @param m2
     * @return
     */
    public static String mulMoney(String m1, String m2)
    {
        BigDecimal d1 = new BigDecimal(m1);
        BigDecimal d2 = new BigDecimal(m2);
        return d1.multiply(d2).setScale(2).toString();
    }

    /***
     * 两数相乘
     * 
     * @param m1
     * @param m2
     * @param newScale
     *            保留几位小数
     * @return
     */
    public static String mulMoney(String m1, String m2, int newScale)
    {
        BigDecimal d1 = new BigDecimal(m1);
        BigDecimal d2 = new BigDecimal(m2);
        return d1.multiply(d2).setScale(newScale, BigDecimal.ROUND_HALF_UP).toString();
    }

    /**
     * 相除取四舍五入
     * 
     * @param m1
     * @param m2
     * @return
     */
    public static String divMoney(String m1, String m2)
    {
        BigDecimal d1 = new BigDecimal(m1);
        BigDecimal d2 = new BigDecimal(m2);
        return d1.divide(d2, 2, BigDecimal.ROUND_HALF_UP).toString();
    }

    /**
     * <p class="detail"> 功能：Gson对象统一获取，统一处理时间格式或null值字段无法转化的问题 </p>
     * 
     * @author tangy
     * @date 2014-10-23
     * @return
     */
    public static Gson getGson()
    {
        Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss").registerTypeAdapter(
            String.class, new TypeAdapter<String>()
            {
                @Override
                public String read(JsonReader reader)
                    throws IOException
                {
                    return reader.nextString();
                }

                @Override
                public void write(JsonWriter writer, String str)
                    throws IOException
                {
                    writer.value(str == null ? "" : str);
                }
            }).create();
        return gson;
    }

    /**
     * <p class="detail"> 功能：XML格式化输出到控制台 </p>
     * 
     * @author liuwh
     * @date 2016-2-1
     * @param document
     */
    public static void xmlOutput(org.dom4j.Document document)
    {
        try
        {
            // 设置输出格式
            OutputFormat format = new OutputFormat("    ", true);// 设置缩进为4个空格，并且另起一行为true
            // 输出到控制台
            XMLWriter xmlWriter = new XMLWriter(format);
            xmlWriter.write(document);
            xmlWriter.close();
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * <p class="detail"> 功能：app接口返回的json数据换行符等统一替换成字符 </p>
     * 
     * @author tangy
     * @date 2014-12-23
     * @param string
     * @return
     */
    public static final String quote(String string)
    {
        if (string == null || string.length() == 0)
        {
            return "";
        }
        char b;
        char c = 0;
        int i;
        int len = string.length();
        StringBuffer sb = new StringBuffer(len + 4);
        String t;

        // sb.append('"');
        for (i = 0; i < len; i += 1)
        {
            b = c;
            c = string.charAt(i);
            switch (c)
            {
                case '\\':
                case '"':
                    sb.append('\\');
                    sb.append(c);
                    break;
                case '/':
                    if (b == '<')
                    {
                        sb.append('\\');
                    }
                    sb.append(c);
                    break;
                case '\b':
                    sb.append("\\b");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\f':
                    sb.append("\\f");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                default:
                    if (c < ' ' || (c >= '\u0080' && c < '\u00a0')
                        || (c >= '\u2000' && c < '\u2100'))
                    {
                        t = "000" + Integer.toHexString(c);
                        sb.append("\\u" + t.substring(t.length() - 4));
                    }
                    else
                    {
                        sb.append(c);
                    }
            }
        }
        return sb.toString();
    }

    /**
     * <p class="detail"> 功能：获得请求路径 </p>
     * 
     * @author wangb
     * @date 2015-1-22
     * @param request
     * @param type
     * @return
     */
    public static String getContextURL(HttpServletRequest request, int type)
    {
        String strBackUrl = "http://" + request.getServerName() // 服务器地址
                            + ":" + request.getServerPort(); // 端口号
        if (type == 1) strBackUrl += request.getContextPath(); // 项目名称
        return strBackUrl;
    }

    /**
     * <p class="detail"> 功能：app接口返回富文本编辑器编辑的html代码时调用该方法将图片宽度替换为100%，以适配手机端屏幕(按照内容适应) </p>
     * 
     * @author tangy
     * @date 2015-9-16
     * @param htmlText
     * @return
     */
    public static String appHtmlImgWidth(Object htmlText)
    {
        if (htmlText == null)
        {
            return "";
        }
        else
        {
            return htmlText.toString().replaceAll("width=\"350px\"", "width=\"100%\"");
        }
    }

    /**
     * <p class="detail"> 功能：去除html中的标签，返回一段纯文本 </p>
     * 
     * @author liuwh
     * @date 2015-9-18
     * @param sourceStr
     *            HTML字符串
     * @param tagName
     *            指定要被去除标签的名称，如 img，默认会去除所有<br/>
     * @param replaceStr
     *            指定要替换成的字符串，最多被替换${count}次
     * @param count
     *            最多被替换的次数，如果超过了实际去除标签的个数，那么按实际的数目替换
     * @return 纯文本
     */
    public static String removeTags(String sourceStr, String tagName, String replaceStr, int count)
    {
        int temp = 0;
        StringBuilder docBuilder = new StringBuilder();
        Document doc = Jsoup.parse(sourceStr);
        doc.select("br").remove();// 去除所有<br/>
        Elements els = doc.select(tagName);// 去除指定标签
        for (Element e : els)
        {
            e.remove();
            if (temp < count) temp++ ; // 最多追加三次 [图片]
        }
        docBuilder.append(doc.body().text());
        for (int i = 0; i < temp; i++ )
        {
            docBuilder.append(replaceStr);
        }
        return docBuilder.toString();
    }

    /**
     * <p class="detail"> 功能：spring mvc接收中文参数乱码问题 </p>
     * 
     * @author wuxw
     * @param str
     * @return
     * @throws
     */
    public static String encodeStr(String str)
    {
        if (StringUtils.isBlank(str))
        {
            return null;
        }
        try
        {
            return new String(str.getBytes("ISO-8859-1"), "UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 首字母大写
     * 
     * @param srcStr
     * @return
     */
    public static String firstCharacterToUpper(String srcStr)
    {
        System.out.println("文字" + srcStr);
        return srcStr.substring(0, 1).toUpperCase() + srcStr.substring(1);
    }

    /**
     * <p class="detail"> 功能：将字符串分隔得到List </p>
     * 
     * @author liu.weihao
     * @date 2016年4月20日
     * @param str
     *            要分隔的字符串
     * @param seperator
     *            分隔符，默认是逗号
     * @return
     */
    public static List<String> String2List(String str, String seperator)
    {
        if (StringUtils.isBlank(str)) return null;
        seperator = isNullOrEmpty(seperator) ? "," : seperator;
        List<String> resultList = new ArrayList<String>();
        String[] arr = str.split(seperator);
        for (String s : arr)
        {
            resultList.add(s);
        }
        return resultList;
    }
}

