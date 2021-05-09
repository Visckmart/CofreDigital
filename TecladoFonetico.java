import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TecladoFonetico {
    static String[] fonemas = {
        "BA", "BE", "BO", "CA", "CE", "CO", "DA", "DE", "DO", "FA", "FE", "FO", "GA", "GE", "GO", "HA", "HE", "HO"
    };
    
    List<List<String>> combinacoes;
    List<List<String>> gruposDigitados = new ArrayList<List<String>>();
    
    TecladoFonetico() {
        atualizarCombinacoes();
    }

    void limparDigitacao() {
        gruposDigitados.clear();
        atualizarCombinacoes();
    }
    void registrarDigitacao(int indiceBotao) {
        ArrayList<String> x = new ArrayList<String>(combinacoes.get(indiceBotao));
        System.out.println("Registrando " + x.toString());
        gruposDigitados.add(x);
    }
    boolean verificarFonemas(List<String> fonemasDaSenha) {
        // for (int i = 0; i < fonemasDaSenha.size(); i++) {
        //     if (fonemas.contains(fonemasDaSenha.get(i)) == false) {
        //         return false;
        //     }
        // }
        // System.out.println(UserLoginHandler.generatePasswordCombinations(gruposDigitados));
        return true;
    }

    void atualizarCombinacoes() {
        List<String> fonemasDisponiveis = Arrays.asList(fonemas.clone());
        Collections.shuffle(fonemasDisponiveis);
        fonemasDisponiveis.toArray();
        ArrayList<List<String>> combinacoes = new ArrayList<List<String>>();
        for (int i = 0; i < fonemasDisponiveis.size()/3; i++) {
            List<String> grupo = fonemasDisponiveis.subList(i*3, (i+1)*3);
            combinacoes.add(i, grupo);
        }
        this.combinacoes = combinacoes;
    }

    List<String> obterTextoDosBotoes() {
        ArrayList<String> textoBotoes = new ArrayList<String>();
        for (List<String> comb : combinacoes) {
            textoBotoes.add(String.join(" ", comb));
        }
        return textoBotoes;
    }

    boolean checarSenha(String senha) {
        List<String> fonemasDaSenha = new ArrayList<String>();
        for (int i = 0; i < senha.length()/2; i++) {
            String fonema = senha.substring(i*2, i*2+2);
            fonemasDaSenha.add(fonema);
        }
        assert verificarFonemas(fonemasDaSenha) : "A senha contém fonemas inválidos.";
        System.out.println(fonemasDaSenha);
        if (fonemasDaSenha.size() != this.gruposDigitados.size()) {
            return false;
        }
        for (int i = 0; i < fonemasDaSenha.size(); i++) {
            // System.out.println(this.gruposDigitados.get(i) + " | " + fonemasDaSenha.get(i));
            if (!this.gruposDigitados.get(i).contains(fonemasDaSenha.get(i))) {
                return false;
            }
        }
        return true;
    }
}
