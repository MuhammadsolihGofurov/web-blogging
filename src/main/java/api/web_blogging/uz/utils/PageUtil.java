package api.web_blogging.uz.utils;

public class PageUtil {
    public static int page(int value) {
        return value == 0 ? 1 : value - 1;
    }
}
