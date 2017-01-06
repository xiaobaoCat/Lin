package util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneIsWright {
    /**
     * 判断电话号码是否符合格式.
     *
     * @param inputText the input text
     * @return true, if is phone
     */
    public static boolean isPhone(String inputText) {
        Pattern p = Pattern.compile("^((14[0-9])|(13[0-9])|(15[0-9])|(18[0-9])|(17[0-9]))\\d{8}$");
        Matcher m = p.matcher(inputText);
        return m.matches();
    }




    /**
     * 验证身份证号是否符合规则
     * @param text 身份证号
     * @return
     */
    public static boolean IdCardIsWright(String text) {
        String regx = "[0-9]{17}X";
        String rege = "[0-9]{17}x";
        String reg1 = "[0-9]{15}";
        String regex = "[0-9]{18}";
        return text.matches(regx) || text.matches(reg1) || text.matches(regex)||text.matches(rege);
    }
    //判断，返回布尔值
    public static boolean isPhonetelNumber(String input){
        String regex="1([\\d]{10})|((\\+[0-9]{2,4})?\\(?[0-9]+\\)?-?)?[0-9]{7,8}";
        return input.matches(regex);
    }
}