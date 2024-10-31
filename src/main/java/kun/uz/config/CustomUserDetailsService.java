package kun.uz.config;

import kun.uz.entity.ProfileEntity;
import kun.uz.exceptions.ResourceNotFoundException;
import kun.uz.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private ProfileRepository profileRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ProfileEntity entity = profileRepository.findByUsername(username);
        if (entity == null) {
            throw new ResourceNotFoundException("Resource not found ");
        }
        return new CustomUserDetails(entity);
    }
}
