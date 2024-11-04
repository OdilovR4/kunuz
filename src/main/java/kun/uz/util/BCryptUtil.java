package kun.uz.util;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptUtil {
    public static String bcrypt(String password) {
        // BCryptPasswordEncoder ob'ektini yaratamiz
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }
}
