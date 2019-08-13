package com.example.demo;

import java.lang.reflect.Field;

public class Status {

	static int BOUGHT = 2;
	static int BOOKED = 1;
	static int FREE = 0;
	
	public static String status(int num) {
		try {
		for(Field field : Status.class.getDeclaredFields()) {
			if(field.getInt(Status.class.newInstance())==num) {
				return field.getName();
			}
		}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
