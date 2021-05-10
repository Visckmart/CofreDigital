package Utilities;
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
    
    public boolean checkAccess(String user, String group) {
        // System.out.printf("%s %s  %s %s\n", user, this.dono, group, this.grupo);
        return user.equals(this.dono) || group.equals(this.grupo);
    }

    public String toString() {
        return String.format("%s (%s) \t[ %s | %s ]", nomeOriginal, nomeProtegido, dono, grupo);
    }
}