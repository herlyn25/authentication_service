package bootcamp.reto.poweup.model;

import java.util.*;

public class ConstanstsModel {
  public static String INVALID_CREDENTIALS = "Invalid Credentials";
    public static final Map<Long, String> ROLES_MAP;
    static {
        Map<Long, String> roles = new HashMap<>();
        roles.put(1L, "ADMIN");
        roles.put(2L, "ASESOR");
        roles.put(3L, "CLIENTE");
        ROLES_MAP = Collections.unmodifiableMap(roles);
    }
}
