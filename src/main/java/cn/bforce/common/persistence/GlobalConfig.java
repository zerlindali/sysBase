package cn.bforce.common.persistence;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Properties;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.appender.RollingFileAppender;
import org.apache.logging.log4j.core.layout.PatternLayout;

import cn.bforce.common.utils.ExStringUtils;


/**
 * 
 * 全局配置参数
 * 
 * 配置文件是 config.properties 或 company.properties
 * 
 * @author LTJ
 *
 */
public class GlobalConfig {

	private static Logger logger = LogManager.getLogger(GlobalConfig.class);
	private static Properties companyProperties = null;
	private static Properties configProperties = null;

	public static String DEFAULT_DATASOURCE = "SysDB";
	
	public static int DEFAULT_CLIENT_ID = -1;
	
	public static final String KEY_LOGIN_ID = "LTJ_WEBOA_LOGIN_ID";

	public static final String KEY_LOGIN_TOKEN = "LTJ_WEBOA_LOGIN_TOKEN";

	public static final String KEY_MEMBER_ID = "LTJ_WEBOA_MEMBER_ID";
		

	static {
		reloadCompany();
		reloadConfig();
	}

	public synchronized static void reloadCompany() {
		if (companyProperties == null) {
			companyProperties = new Properties();
		} else {
			companyProperties.clear();
		}
		loadProperties(companyProperties, "company.properties");
	}

	public synchronized static void reloadConfig() {
		if (configProperties == null) {
			configProperties = new Properties();
		} else {
			configProperties.clear();
		}
		loadProperties(configProperties, "config.properties");
	}

	public static void loadProperties(Properties properties, String fileName) {
		if (properties == null || fileName == null) {
			return;
		}
		try {
			String root = GlobalConfig.class.getResource("/").getPath();
			
			root = decodeFilePath(root);//added on 20121205
			String file = root + fileName;
			logger.debug("loadProperties()-- fileName=[" + file + "]");
			InputStream is = new FileInputStream(file);
			properties.load(is);
			is.close();
		} catch (Exception e) {
			logger.error("loadProperties()[" + fileName + "]:" + e);
		}
	}

	public static String getCompanyValue(String key) {
		if (companyProperties == null) {
			return null;
		}
		return companyProperties.getProperty(key);
	}

	public static String getConfigValue(String key, String defaultValue) {
		String value = configProperties.getProperty(key);
		if (value == null){
			value = defaultValue;
		}
		return value;
	}
	
	public static String getConfigValue(String key) {
		if (configProperties == null) {
			return null;
		}
		return configProperties.getProperty(key);
	}

	public static String getCompanyName() {
		String name = getCompanyValue("company.name");
		if (name == null){
			name = "[公司名称]";
		}
		return name;
	}

	public static boolean isTestEnv() {
		String value = getCompanyValue("isTestEnv");
		if ("Yes".equalsIgnoreCase(value)){
			return true;
		}
		return false;
	}
	
	public static String getAppTitle() {
		return getCompanyValue("app.title");
	}

	public static String getAppName() {
		return getCompanyValue("app.name");
	}

	public static String getLic() {
		return getCompanyValue("lic1");
	}
	
	public static String getHomeUrl() {
		String strReturn = getConfigValue("home.url");
		if (strReturn != null) {
			return strReturn;
		}
		return "/index.htm";
	}

	
	public static String getOpenIdUserTable() {
		String strReturn = getConfigValue("openId.userTable");
		if (strReturn != null) {
			return strReturn;
		}
		return null;
	}
	
	public static String getOpenIdUrl() {
		String strReturn = getConfigValue("openId.url");
		if (strReturn != null) {
			return strReturn;
		}
		return null;
	}

	public static String getOpenIdServiceClassName() {
		String strReturn = getConfigValue("openId.serviceClassName");
		if (strReturn != null) {
			return strReturn;
		}
		return null;
	}

	/**
	 * 文档中心的根目录
	 * 
	 * @return
	 */
	public static String getDocRoot() {
		return getConfigValue("docRoot");
	}

	public static long getDmsWebResMaxUploadSize() {
		String value = getConfigValue("dms.webres.maxSize");
		if (value != null) {
			return ExStringUtils.parseFileSize(value);
						
		}
		// 2M
		return 2048000;
	}
	
	public static long getDmsFileMaxUploadSize() {
		String value = getConfigValue("dms.file.maxSize");
		if (value != null) {
			return ExStringUtils.parseFileSize(value);								
		}
		// 10M
		return 10240000;
	}
	
	public static String getDmsViewFileInline() {
		String value = getConfigValue("dms.viewFileInline");
		if (value == null){
			value = ".pdf";
		}else{
			value = value.toLowerCase();
		}
		return value;
	}
	

	
	public static String getIsWebDir() {
		return getConfigValue("isWebDir");
	}
	
	public static String getShowMainMenu() {
		String strReturn = getConfigValue("showMainMenu");
		if (strReturn != null) {
			return strReturn;
		}
		return "Yes";
	}

	public static String getShowDmsMenu() {
		String strReturn = getConfigValue("showDmsMenu");
		if (strReturn != null) {
			return strReturn;
		}
		return "No";
	}

	public static String getAppVer() {
		String appVersion = getConfigValue("app.version");
		if (appVersion == null){
			appVersion = "1.0.0";
		}
		return appVersion;
	}

	
	public static String getShowTodoMenu() {
		String strReturn = getConfigValue("showTodoMenu");
		if (strReturn != null) {
			return strReturn;
		}
		return "No";
	}

	public static String getSkinPath(String remoteHost) {
		String value = getConfigValue("skin.path." + remoteHost);
		if (value == null) {
			value = getConfigValue("skin.path");
		}
		return value;
	}

	public static String getSkinPath() {
		return getConfigValue("skin.path");
	}

	public static String getPageTemplatePath(String remoteHost) {
		String value = getConfigValue("pageTemplate.path." + remoteHost);
		if (value == null) {
			value = getConfigValue("pageTemplate.path");
		}
		return value;
	}

	public static String getStyleTable() {
		String strReturn = getConfigValue("style.table");
		if (strReturn != null) {
			return strReturn;
		}
		return "its";
	}

	public static String getTableCssUrl() {
		return getConfigValue("table.css.url");
	}

	public static String getToolbarCssUrl() {
		return getConfigValue("toolbar.css.url");
	}
	
	public static String getStyleTableWidth() {
		return getConfigValue("style.table.width");
	}

	public static String getStyleTheme() {
		String strReturn = getConfigValue("style.theme");
		if (strReturn != null) {
			return strReturn;
		}
		return "base";
	}

	public static boolean isShowToolbarButtonLable() {
		String configValue = getConfigValue("toolbar.showBtnLabel");
		return ExStringUtils.getBoolean(configValue, false);
	}
	
	public static String getLicenseServerURL() {
		String serverUrl = getConfigValue("license.server");
		if (serverUrl == null || serverUrl.length() == 0){
			serverUrl = "http://lic.51pme.com";
		}
		return serverUrl;
	}
	
	
	public static boolean isCaptchaServiceEnabled() {
		String configValue = getConfigValue("captcha.enabled");
		return ExStringUtils.getBoolean(configValue, false);
	}

	/**
	 * 前端js加解密密钥是否打开
	 * 
	 * @return
	 */
	public static boolean isJSEncryptEnabled() {
		String configValue = getConfigValue("encrypt.enabled");
		return ExStringUtils.getBoolean(configValue, false);
	}

	/**
	 * 加解密公钥
	 * 
	 * @return
	 */
	public static String getPublicKey() {
		String strReturn = getConfigValue("publicKey");
		if (strReturn != null) {
			return strReturn;
		}
		return "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDjVIm2J8DwrwLNnsWDkDZv5kal5wDGoA51Lod6LzdHPX4AIPCvMnr9FLLWoSN6wmiHUgq935FhCAfZPxBQTGUCw/0QvjfNs/IPIsnWg4QYGctXzjRyaaFW519UhMuUc8yqEASPSNo495zM1gbWh3Bj2FqG4CN5tRvZJOGwQ1C4FwIDAQAB";
	}
	
	/**
	 * 加解密私钥
	 * @return
	 */
	public static String getPrivateKey() {
		String strReturn = getConfigValue("privateKey");
		if (strReturn != null) {
			return strReturn;
		}
		return "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAONUibYnwPCvAs2exYOQNm/mRqXnAMagDnUuh3ovN0c9fgAg8K8yev0UstahI3rCaIdSCr3fkWEIB9k/EFBMZQLD/RC+N82z8g8iydaDhBgZy1fONHJpoVbnX1SEy5RzzKoQBI9I2jj3nMzWBtaHcGPYWobgI3m1G9kk4bBDULgXAgMBAAECgYBvmXfFibyJK5F3ugMCtFBVHLoABeh2meqfEkmF7Rn5CjIKBCHEEEcaaQfmn9KYGEpSbC9DyTZIaTXZK8Fpeid3iA2cq7JK7xuFiOmRKpCJdJhRuejmtELXIkWErlkYcxl8R2i1JNX6MzONxiY1agJUvVDoWcw/5JlZsyhdIkZm0QJBAPsF8AjuGJ5p+vm/HDRcVtTOospsuva4v3ayi7ztiobTbZy/E+WmQY/RB93wkgbzrmDCFfibSIlEOL+9NUwtKrkCQQDn1ljhyqc4SLZR9r9F96OQpNfxsF2tSNwl1ZaMoyihT6dmosXwq1IlPN7CkYZUCp2dbP+hk8/NiMEqdZFPvFFPAkB1no6eYJIAiEAlDbs1fZ7iEACPQOtJ978e6wI3AEbMbf0KWS4FiSVnMdax06tYvo2SoN6SopAWYXUFwq5ahz9hAkABkv4FgnfCNRQv2EoSMo1ExSnGNVldfNop05pzGASFm+HaG47WhINJR3GHxq8v0OajFANkWHmXvq75F9VFwlSXAkEAyZpZJ/q8Xzt/+LL4Wwj5irkMDRcXnoVx/rdTyT9MC24szIs4spFgxe5qAdPy3TQLeF8cc8ycnsFQ50MpQYWNuw==";
	}
		
	public static String decodeFilePath(String root){
		
		try {
			root = URLDecoder.decode(root, "UTF-8");
		} catch (UnsupportedEncodingException e) {
		
		}
		
		if (root != null && root.toLowerCase().indexOf("http:") == -1
				&& root.indexOf("%20") != -1) {
			root = root.replace("%20", " ");
		}
		return root;
	}
	
}
