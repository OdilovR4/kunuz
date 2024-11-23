package kun.uz.config;

import kun.uz.enums.AppLanguage;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class AppLanguageConverter implements Converter<String, AppLanguage> {

    @Override
    public AppLanguage convert(String source) {
        if (source == null || source.isEmpty()) {
            return AppLanguage.uz;  // Standart qiymat
        }

        // Tillarni va ularning ustuvorligini qayta ishlash
        switch (source.toLowerCase()) {
            case "uz":
                return AppLanguage.uz;
            case "ru":
                return AppLanguage.ru;
            case "en":
                return AppLanguage.en;
            default:
                return AppLanguage.uz;  // Standart fallback
        }
    }
}
