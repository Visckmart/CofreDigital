package Utilities;

import Authentication.UserGroup;
import Authentication.UserState;
import Database.DatabaseHandler;

public class FileInfo {

    public String nomeProtegido;
    public String nomeOriginal;
    public String dono;
    public String grupo;

    public FileInfo(String indexLine) {
        String[] lineInfo = indexLine.split(" ");
        this.nomeProtegido = lineInfo[0];
        this.nomeOriginal = lineInfo[1];
        this.dono = lineInfo[2];
        this.grupo = lineInfo[3];
    }
    
    public boolean checkAccess() {
        DatabaseHandler.getInstance().updateUserState(UserState.emailAddress);
        boolean usuarioAllowed = this.grupo.equals("usuario") && UserState.group == UserGroup.USER;
        boolean adminAllowed = this.grupo.equals("administrador") && UserState.group == UserGroup.ADMIN;
        boolean correctGroup = usuarioAllowed || adminAllowed;
        return UserState.username.equals(this.dono) || correctGroup;
    }

    public String toString() {
        return String.format("%s (%s) \t[ %s | %s ]", nomeOriginal, nomeProtegido, dono, grupo);
    }
}
