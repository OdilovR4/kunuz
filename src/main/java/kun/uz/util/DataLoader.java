package kun.uz.util;

import kun.uz.entity.ProfileEntity;
import kun.uz.enums.ProfileRole;
import kun.uz.enums.ProfileStatus;
import kun.uz.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataLoader implements CommandLineRunner {
    @Autowired
    ProfileRepository profileRepository;

    @Override
    public void run(String... args) throws Exception {
        ProfileEntity admin = profileRepository.getByUsername("admin");
        if (admin == null) {
            admin = new ProfileEntity();
            admin.setUsername("admin");
            admin.setVisible(true);
            admin.setPassword(MD5Util.md5("admin"));
            admin.setName("admin");
            admin.setSurname("admin");
            admin.setStatus(ProfileStatus.ACTIVE);
            admin.setCreatedDate(LocalDateTime.now());
            admin.setRole(ProfileRole.ROLE_ADMIN);
            profileRepository.save(admin);
        }
    }
}
