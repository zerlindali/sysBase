package cn.bforce.common.persistence;

import java.io.Serializable;
import java.util.Date;
import java.util.Random;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cn.bforce.common.utils.ExDateUtils;
import cn.bforce.common.utils.ExStringUtils;


/**
 * 接口调用命令对象
 * 封装接口调用常用的非业务参数，
 * 如appKey,appToken,fromIP,createdTime,nonce,signature
 * 
 */
public class Command implements  Serializable {
	
	private static final Logger log = LogManager.getLogger(Command.class);

	private long  appId;
	
	private String appKey;
	
	private String appCode;
	
	private String commandName;

	private String commandType;

	private String pathInfo;
	
	private long createdTime;
	
	private String fromIP;
		
	private String accessToken;
		
	private String timeStamp;
	
	private String nonce;
	
	private String signature;
	
	private String signature1;
	
	private String apiVer;
	
	private String bizData;
	
	
	public Command(){
		this.createdTime =  System.currentTimeMillis();
		this.commandType = "pass";
	}
	
	/**
	 * appId, 接口请求方的身份标志
	 * @return
	 */
	public long getAppId() {
		return appId;
	}
	

	/**
	 * appId, 接口请求方的身份标志
	 * @param appId
	 */
	public void setAppId(long appId) {
		this.appId = appId;
	}

	public void setAppId(String sAppId) {
		this.appId = ExStringUtils.parseLong(sAppId, -1);
	}
	
	/**
	 * 命令名称
	 * @return
	 */
	public String getCommandName() {
		return commandName;
	}

	/**
	 * 接口请求方的来源IP
	 * @return
	 */
	public String getFromIP() {
		return fromIP;
	}


	public void setFromIP(String fromIP) {
		this.fromIP = fromIP;
	}

	/**
	 * 业务数据包，通常为JSON数据或者XML数据
	 * @return
	 */
	public String getBizData() {
		return bizData;
	}


	public void setBizData(String bizData) {
		this.bizData = bizData;
	}

	/**
	 * 访问令牌，用于请求方的身份认证
	 * @return
	 */
	public String getAccessToken() {
		return accessToken;
	}


	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	
	/**
	 * 请求时间 
	 * @return
	 */
	public long getCreatedTime() {
		return createdTime;
	}

	/**
	 * 接口请求方的密钥,与AppID是成对生成的
	 * @return
	 */
	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	/**
	 * 接口请求方的代码，与AppID是相对应的，AppID是分配纯数字，AppCode是预定义编码
	 * @return
	 */
	public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}
	
	/**
	 * 请求参数的数字签名值
	 * @return
	 */
	public String getSignature() {
		return signature;
	}

	/**
	 * 接口版本口
	 * @return
	 */
	public String getApiVer() {
		return apiVer;
	}

	
	public void setApiVer(String apiVer) {
		this.apiVer = apiVer;
	}
	
	/**
	 * 请求时间戳
	 * @return
	 */
	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	/**
	 * 请求的随机数，用于数字签名
	 * @return
	 */
	public String getNonce() {
		return nonce;
	}

	public void setNonce(String nonce) {
		this.nonce = nonce;
	}

	public void setCreatedTime(long createdTime) {
		this.createdTime = createdTime;
	}

	/**
	 * 是否需要校验TOKEN
	 * @return
	 */
	public boolean isNeedCheckToken(){
		// 获取TOKEN的指令不需要验证TOKEN
		if ("token.create".equalsIgnoreCase(commandName)){
			return false;
		}
		return true;
	}

	/**
	 * 业务数据做MD5指纹
	 */
	public String buildSignature(){		
		if (this.nonce == null){
			this.nonce = String.valueOf(new Random().nextLong());
		}
		if (this.timeStamp == null){
			this.timeStamp = String.valueOf(System.currentTimeMillis());
		}
		if (this.bizData == null){
			this.bizData = "";
		}
		
		// 生成MD5摘要
		this.signature = DigestUtils.md5Hex(nonce + bizData + timeStamp);
		
		return this.signature;
	}

	public boolean checkDataSign(String inputSign){
		
		String newSign = this.buildSignature();
		if (!newSign.equalsIgnoreCase(inputSign)){
			return false;
		}
		return true;
	}
	
	public boolean checkTimestamp(String timestamp){
		long time1 = System.currentTimeMillis();
		long time2 = ExStringUtils.parseLong(timestamp, 0);
		
		if ((time1 - time2) < 600000 ){ // 相差10分钟内为正常
			return true;
		}
		
		return false;
	}
	
	public String getCommandType() {
		return commandType;
	}
	
	public String getPathInfo() {
		return pathInfo;
	}

	public void setPathInfo(String pathInfo) {
		this.pathInfo = pathInfo;		
		this.parseCommandPath(pathInfo);
	}

	private void parseCommandPath(String urlPath){
		
		if (urlPath.startsWith("/")){
			urlPath = urlPath.substring(1);
		}
		//
		urlPath =  urlPath.replaceAll("/", ".");	
		
		if (urlPath.startsWith("access_token")){
			this.commandName = urlPath.substring(13);
			this.commandType = "access_token";
		}else if (urlPath.startsWith("pass")){
			this.commandName = urlPath.substring(5);
			this.commandType = "pass";
		}else if (urlPath.startsWith("bp")){
			this.commandName = urlPath.substring(3);
			this.commandType = "bp";
		}
		
	}

	
	public String getSignature1() {
		return signature1;
	}

	public void setSignature1(String signature1) {
		this.signature1 = signature1;
	}

	public String getDebugInfo(){
		
		StringBuffer buf = new StringBuffer();
		
		buf.append("commandType:" + this.commandType +"\r\n");
		buf.append("commandName:" + this.commandName +"\r\n");
		buf.append("pathInfo:" + this.pathInfo +"\r\n");
		buf.append("appId:" + this.appId +"\r\n");
		buf.append("appKey:" + this.appKey +"\r\n");
		buf.append("accessToken:" + this.accessToken +"\r\n");
		buf.append("bizData:" + this.bizData +"\r\n");
		buf.append("signature:" + this.signature +"\r\n");
		buf.append("createdTime:" + ExDateUtils.formatDateTime(new Date(this.createdTime)) +"\r\n");		
		return buf.toString();
		
	}
	
}
