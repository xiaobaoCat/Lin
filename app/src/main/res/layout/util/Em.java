package util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public class Em {
    private static final String MATCHER = "[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]";
    public static String filterEmoji(String source) {
        if(source != null) {
            Pattern emoji = Pattern.compile (MATCHER, Pattern.UNICODE_CASE | Pattern. CASE_INSENSITIVE ) ;
            Matcher emojiMatcher = emoji.matcher(source);
            if ( emojiMatcher.find())  {
                source = emojiMatcher.replaceAll("");
                return source ;
            }
            return source;
        }
        return source;
    }
}
