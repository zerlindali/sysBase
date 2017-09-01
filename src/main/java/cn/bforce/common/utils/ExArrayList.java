package cn.bforce.common.utils;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 扩展的数据结果集 增加了一个total的属性，用于表示分页模式下的总记录行数
 * 
 * @author LTJ
 * 
 * @param <T>
 */
public class ExArrayList<T> extends ArrayList<T> {

	private int total;

	public ExArrayList() {

	}

	public ExArrayList(Collection<? extends T> c) {
		super(c);
	}
	
	/**
	 * 数据集中的记录总数
	 * @return
	 */
	public int getTotal() {
		if (total == 0 &&  this.size() > 0){
			return this.size();
		}
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

}
