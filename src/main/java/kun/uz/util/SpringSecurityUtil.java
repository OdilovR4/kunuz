package kun.uz.util;

import kun.uz.enums.ProfileRole;
import kun.uz.exceptions.AppBadForbiddenException;

public class SpringSecurityUtil {

    public static void checkRoleExists(String profileRole, ProfileRole...requiredRoles) {

        for(ProfileRole role : requiredRoles){
            if(role.name().equals(profileRole)){
                return;
            }
        }
        throw new AppBadForbiddenException("Forbidden");
    }
}
