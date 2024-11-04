package kun.uz.util;

import kun.uz.config.CustomUserDetails;
import kun.uz.enums.ProfileRole;
import kun.uz.exceptions.AppBadForbiddenException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SpringSecurityUtil {

    public static void checkRoleExists(String profileRole, ProfileRole...requiredRoles) {
        for(ProfileRole role : requiredRoles){
            if(role.name().equals(profileRole)){
                return;
            }
        }
        throw new AppBadForbiddenException("Forbidden");
    }

    public static CustomUserDetails getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return userDetails;
    }
}
