package dataTransferTest;


import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class MyDTO {

	// 解决泛型擦除类型信息
	public static <T> List<T> toObjectList(String string, Class<T> T) {
		try {
			Gson gson = new Gson();
			List<T> lst = new ArrayList<T>();
			JsonArray array = new JsonParser().parse(string).getAsJsonArray();
			System.out.println(gson.toJson(array));
			for (final JsonElement element : array) {
				System.out.println(gson.toJson(element));
				lst.add(gson.fromJson(element, T));
				System.out.println(gson.toJson(lst));
			}
			return lst;
		} catch (Exception e) {
			return null;
		}
	}
	public static <T> T toObject(String str, Class<T> c) {
		Gson gson = new Gson();
		T t = gson.fromJson(str, c);
		return t;
	}

}
