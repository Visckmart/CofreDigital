package Authentication;

import java.security.cert.Certificate;
import java.security.PrivateKey;

public class UserState {
    public static String emailAddress;
    public static String username;
    public static PrivateKey privateKey;
    public static Certificate certificate;
    public static int attempts;
    public static int queries;
    public static int totalUsers;
    public static UserGroup group;
}
