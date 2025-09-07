package bootcamp.reto.poweup.model;

import java.util.*;

public class ConstanstsModel {
    public static final String USER_NO_FOUND = "User no found in BD";
    public static String INVALID_CREDENTIALS = "Invalid Credentials";
  public static String ROLE_NO_EXIST = "Role Not Found";
    public static final Map<Long, String> ROLES_MAP;
    static {
        ROLES_MAP = Map.of(1L, "ADMIN", 2L, "ASESOR", 3L, "CLIENTE");
    }
}
