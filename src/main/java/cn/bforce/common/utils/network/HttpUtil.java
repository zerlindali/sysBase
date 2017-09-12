package cn.bforce.common.utils.network;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cn.bforce.common.utils.string.StringUtil;


public class HttpUtil
{

    static Logger logger = LogManager.getLogger(HttpUtil.class);

    private static String ENCODING_UTF_8 = "UTF-8";

    /**
     * <p class="detail"> 功能：发送post请求 </p>
     * 
     * @author liuwh
     * @date 2016-1-31
     * @param hostUrl
     *            请求链接，不带参数
     * @param data
     *            请求参数，不带？，多个参数用&分隔
     * @return
     */
    public static String post(String hostUrl, String data)
    {
        StringBuffer Result = new StringBuffer();
        int code = 0;
        HttpURLConnection httpConn = null;
        try
        {
            URL url = new URL(hostUrl);
            httpConn = (HttpURLConnection)url.openConnection();
            httpConn.setConnectTimeout(4000);
            httpConn.setReadTimeout(2000);
            httpConn.setDoOutput(true);
            httpConn.setRequestMethod("POST");
            httpConn.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            httpConn.setRequestProperty("User-Agent", "tsou.cn");
            OutputStreamWriter osw = new OutputStreamWriter(httpConn.getOutputStream(), "utf-8");
            osw.write(data);
            osw.flush();
            osw.close();

            code = httpConn.getResponseCode();
            if (code == 200)
            {
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                    httpConn.getInputStream(), "UTF-8"));
                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    Result.append(line + "\n");
                }
                reader.close();
            }
            else
            {
                Result.append("Error:" + code);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (httpConn != null)
            {
                httpConn.disconnect();
            }
        }
        return Result.toString();
    }

    /**
     * 向指定URL发送GET方法的请求
     * 
     * @param url
     *            发送请求的URL
     * @param parameterMap
     *            请求参数
     * @return 所代表远程资源的响应结果
     */
    public static String sendGet(String urlStr, Map<String, Object> parameterMap)
    {
        String result = "";
        String encoding = "";
        InputStream is = null;
        BufferedReader in = null;
        try
        {
            String params = assembleUrl(parameterMap);
            // 建立连接
            URL realUrl = new URL(urlStr + (StringUtil.isNullOrEmpty(params) ? "" : params));
            logger.info(realUrl.toString());
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            is = connection.getInputStream();
            encoding = connection.getContentEncoding();
            result = IOUtils.toString(is, encoding);
        }
        catch (Exception e)
        {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if (in != null)
                {
                    in.close();
                }
            }
            catch (Exception e2)
            {
                e2.printStackTrace();
            }
        }
        return result;
    }

    /**
     * <p class="detail"> 功能：以表单形式发送POST请求 </p>
     * 
     * @author liuwh
     * @date 2015-12-10
     * @param urlStr
     *            不带参数的url
     * @param parameterMap
     *            参数列表
     * @return
     */
    public static String sendPostWithForm(String urlStr, Map<String, Object> parameterMap)
    {
        BufferedReader responseReader = null;
        try
        {
            String params = assembleUrl(parameterMap);
            // 建立连接
            URL url = new URL(urlStr + (StringUtil.isNullOrEmpty(params) ? "" : params));
            HttpURLConnection httpConn = (HttpURLConnection)url.openConnection();
            // //设置连接属性
            httpConn.setDoOutput(true);// 使用 URL 连接进行输出
            httpConn.setDoInput(true);// 使用 URL 连接进行输入
            httpConn.setUseCaches(false);// 忽略缓存
            httpConn.setRequestMethod("POST");// 设置URL请求方法

            // 设置请求属性
            // 获得数据字节数据，请求数据流的编码，必须和下面服务器端处理请求流的编码一致
            httpConn.setRequestProperty("Content-Type", "application/octet-stream");
            httpConn.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
            httpConn.setRequestProperty("Charset", "UTF-8");

            // 获得响应状态
            int responseCode = httpConn.getResponseCode();
            if (HttpURLConnection.HTTP_OK == responseCode)
            {// 连接成功
                // 当正确响应时处理数据
                StringBuffer buffer = new StringBuffer();
                String readLine;
                // 处理响应流，必须与服务器响应流输出的编码一致
                responseReader = new BufferedReader(new InputStreamReader(
                    httpConn.getInputStream(), ENCODING_UTF_8));
                while ((readLine = responseReader.readLine()) != null)
                {
                    buffer.append(readLine).append("\n");
                }
                responseReader.close();
                return buffer.toString();
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            try
            {
                if (responseReader != null) responseReader.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static String sendPostWithRaw(String urlStr, String data)
    {
        BufferedReader responseReader = null;
        PrintWriter out = null;
        try
        {
            // 建立连接
            URL url = new URL(urlStr);
            HttpURLConnection httpConn = (HttpURLConnection)url.openConnection();
            // //设置连接属性
            httpConn.setDoOutput(true);// 使用 URL 连接进行输出
            httpConn.setDoInput(true);// 使用 URL 连接进行输入
            httpConn.setUseCaches(false);// 忽略缓存
            httpConn.setRequestMethod("POST");// 设置URL请求方法

            // 设置请求属性
            // 获得数据字节数据，请求数据流的编码，必须和下面服务器端处理请求流的编码一致
            httpConn.setRequestProperty("Content-Type", "text/plain");
            httpConn.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
            httpConn.setRequestProperty("Charset", "UTF-8");

            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(httpConn.getOutputStream());
            // 发送请求参数
            out.write(data);
            // flush输出流的缓冲
            out.flush();
            // 获得响应状态
            int responseCode = httpConn.getResponseCode();
            if (HttpURLConnection.HTTP_OK == responseCode)
            {// 连接成功
                // 当正确响应时处理数据
                StringBuffer buffer = new StringBuffer();
                String readLine;
                // 处理响应流，必须与服务器响应流输出的编码一致
                responseReader = new BufferedReader(new InputStreamReader(
                    httpConn.getInputStream(), ENCODING_UTF_8));
                while ((readLine = responseReader.readLine()) != null)
                {
                    buffer.append(readLine).append("\n");
                }
                return buffer.toString();
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            try
            {
                if (out != null) out.close();
                if (responseReader != null) responseReader.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * <p class="detail"> 功能：发送post请求，可上传文件 </p>
     * 
     * @author liuwh
     * @date 2015-12-10
     * @param urlStr
     *            不带参数的url
     * @param parameterMap
     *            参数列表
     * @param formName
     *            上传文件的表单名，如 <input type="file" name="img"/>
     * @param filePath
     *            文件的绝对路径
     * @return
     */
    @SuppressWarnings({"deprecation", "resource"})
    public static String sendPostWithFile(String urlStr, Map<String, Object> parameterMap,
                                          String formName, String filePath)
    {
        String paramsUrl = assembleUrl(parameterMap);
        HttpClient httpClient = new DefaultHttpClient();// 实例化http客户端
        HttpPost postMan = new HttpPost(urlStr
                                        + (StringUtil.isNullOrEmpty(paramsUrl) ? "" : paramsUrl));// 实例化post提交方式
        postMan.addHeader("Charset", "UTF-8");
        try
        {
            MultipartEntity params = new MultipartEntity();// 实例化参数对象
            if (!StringUtil.isNullOrEmpty(formName) && !StringUtil.isNullOrEmpty(filePath))
            {
                File file = new File(filePath);// 设置上传文件
                if (file.exists())
                { // 商品图片文件存在
                    FileBody fileBody = new FileBody(file);
                    params.addPart(formName, fileBody); // 添加文件参数
                    postMan.setEntity(params);// 将参数加入post请求体中
                }
            }
            HttpResponse resp = httpClient.execute(postMan); // 执行请求
            HttpEntity entity = resp.getEntity();// 解析返回请求结果
            InputStream is = entity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            StringBuffer buffer = new StringBuffer();
            String temp;
            while ((temp = reader.readLine()) != null)
            {
                buffer.append(temp).append("\n");
            }
            return buffer.toString();
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        catch (ClientProtocolException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (IllegalStateException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * <p class="detail"> 功能：组装请求链接 </p>
     * 
     * @author liuwh
     * @date 2015-12-7
     * @param urlStr
     *            不带参数的url
     * @param parameterMap
     *            请求参数
     * @return
     */
    public static String assembleUrl(Map<String, Object> parameterMap)
    {
        if (parameterMap == null || parameterMap.size() == 0) return "";
        StringBuilder urlBuilder = new StringBuilder();
        Set<String> keySet = parameterMap.keySet();
        for (String key : keySet)
        {
            if (urlBuilder.length() != 0) urlBuilder.append("&");
            urlBuilder.append(key);
            urlBuilder.append("=");
            urlBuilder.append(parameterMap.get(key));
        }
        return urlBuilder.toString();
    }
}
