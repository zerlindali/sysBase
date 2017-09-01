package cn.bforce.common.persistence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

/**
 * 接口命令的返回结果
 * @author LTJ
 *
 */
public class CommandResult implements  Serializable {

	private long commandResultId;

	// 是否出错
	private boolean hasErrors;
	
	private int resultCode = 0;
	
	private String resultMsg;
	
	private boolean success = true;
	
	private boolean isEnd;
	
	private List dataList;
	
	private int totalRows;
	
	public CommandResult(){
		
	}

	public CommandResult(int resultCode, String resultMsg){
		this.resultCode = resultCode;
		this.resultMsg = resultMsg;
	}

	public void setResult(int resultCode, String resultMsg) {
		this.resultCode = resultCode;
		this.resultMsg = resultMsg;
	}
	/**
	 * 返回结果代码
	 * @return
	 */
	public int getResultCode() {
		return resultCode;
	}

	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}
	/**
	 * 返回结果信息
	 * @return
	 */
	public String getResultMsg() {
		return resultMsg;
	}

	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}
	
	/**
	 * 是否成功
	 * @return
	 */
	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
	
	/**
	 * 是否结束，当有多个命令串连时使用
	 * @return
	 */
	public boolean isEnd() {
		return isEnd;
	}

	public void setEnd(boolean isEnd) {
		this.isEnd = isEnd;
	}
	
	/**
	 * 返回的结果表
	 */
	public List getDataList() {
		if (dataList == null){
			dataList = new ArrayList();
		}
		return dataList;
	}

	public void setDataList(List dataList) {
		this.dataList = dataList;
	}
	
	
	public boolean isHasErrors() {
		return hasErrors;
	}

	public void setHasErrors(boolean hasErrors) {
		this.hasErrors = hasErrors;
	}
	
	
	/**
	 * 返回错误信息
	 * @return
	 */
	public String toJsonString(){
		JSONObject jsonResult = new JSONObject();		
		jsonResult.put("resultCode",  this.resultCode);
		jsonResult.put("resultMsg" ,  this.resultMsg);		
		return jsonResult.toJSONString();
	}
	
	public Map toMap(){
		Map resultMap = new HashMap();		
		resultMap.put("resultCode",  this.resultCode);
		resultMap.put("resultMsg" ,  this.resultMsg);		
		return resultMap;
	}

	public long getCommandResultId() {
		return commandResultId;
	}

	public void setCommandResultId(long commandResultId) {
		this.commandResultId = commandResultId;
	}

	/**
	 * 结果集的总行数
	 * @return
	 */
	public int getTotalRows() {
		return totalRows;
	}

	public void setTotalRows(int totalRows) {
		this.totalRows = totalRows;
	}
	
	
	public static CommandResult buildResult(int resultCode, String resultMsg){
		CommandResult cmdResult = new CommandResult();
		cmdResult.setResultCode(resultCode);
		cmdResult.setResultMsg(resultMsg);
		return cmdResult;
	}
	
}
