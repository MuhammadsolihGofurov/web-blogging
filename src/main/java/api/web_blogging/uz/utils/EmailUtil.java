package api.web_blogging.uz.utils;

import java.util.regex.Pattern;

public class EmailUtil {
    public static boolean isValidEmail(String email) {
        String emailRegex = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$";
        return Pattern.matches(emailRegex, email);
    }
}
