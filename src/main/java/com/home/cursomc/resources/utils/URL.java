package com.home.cursomc.resources.utils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.net.URLDecoder;

public class URL {

	public static String decodeParam(String s) {
		try {			
			return URLDecoder.decode(s, "UTF-8");
		} catch (Exception e) {
			return "";
		}
	}
	public static List<Integer> decodeIntList(String s) {
		return Arrays
				.asList(s.split(","))
				.stream()
				.map(x -> Integer.parseInt(x))
				.collect(Collectors.toList());
	}
}