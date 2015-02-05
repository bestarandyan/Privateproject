package com.qingfengweb.common.utils;

import java.util.ArrayList;
import java.util.List;

public class Utils<T> {
	private T t;

	/**
	 * @return the t
	 */
	public T getT() {
		return t;
	}

	/**
	 * @param t the t to set
	 */
	public void setT(T t) {
		this.t = t;
	}
	
	public String join(T[] arr,String separator){
		StringBuilder builder=new StringBuilder();
		for(int i=0;i<arr.length;i++){
			builder.append(arr[i]);
			if(i<arr.length-1)
				builder.append(separator);
		}
		return builder.toString();
	}
}
