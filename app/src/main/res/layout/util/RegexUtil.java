package util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 使用正则表达式验证输入格式
 * 
 * @author liuxing
 *
 */
public class RegexUtil {
	public static void main(String[] args) {
		System.out.println(checkEmail("14_8@qw.df"));
		System.out.println(checkCharacter("sdf&dhdsSDF12"));
	}
	
	/**
	 * 验证合法字符
	 * @return
	 */
	public static boolean checkCharacter(String character) {
		boolean flag = false;
		try {
			String check = "[a-zA-Z0-9_]{6,16}";
			Pattern regex = Pattern.compile(check);
			Matcher matcher = regex.matcher(character);
			flag = matcher.matches();
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}

	/**
	 * 验证邮箱
	 * 
	 * @param email
	 * @return
	 */
	public static boolean checkEmail(String email) {
		boolean flag = false;
		try {
			String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
			Pattern regex = Pattern.compile(check);
			Matcher matcher = regex.matcher(email);
			flag = matcher.matches();
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}
}