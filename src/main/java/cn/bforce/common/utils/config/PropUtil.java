package cn.bforce.common.utils.config;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;


public class PropUtil
{

    public static final String loadValue(String fileName, String Key)
    {
        try
        {
            Properties properties = new Properties();

            InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(
                fileName);

            // 处理中文乱码
            BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            properties.load(bf);

            return properties.getProperty(Key);
        }
        catch (IOException ex)
        {}
        return "";
    }

    public static final String loadValue(String fileName, String Key, String defaultVal)
    {
        try
        {
            Properties properties = new Properties();

            InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(
                fileName);

            // 处理中文乱码
            BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            properties.load(bf);

            return properties.getProperty(Key, defaultVal);
        }
        catch (IOException ex)
        {}
        return "";
    }

    public static final Properties loadConfig(String fileName)
    {
        try
        {
            Properties properties = new Properties();
            properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(
                fileName));
            return properties;
        }
        catch (IOException ex)
        {}
        return null;
    }
}
