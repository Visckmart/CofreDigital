package UserAuthentication;
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
    
    public TecladoFonetico() {
        renovarCombinacoes();
    }

    public void registrarDigitacao(int indiceBotao) {
        ArrayList<String> grupoDigitado = new ArrayList<String>(combinacoes.get(indiceBotao));
        System.out.println("Registrando " + grupoDigitado.toString());
        gruposDigitados.add(grupoDigitado);
        renovarCombinacoes();
    }

    public int getFonemasDigitados() {
        return gruposDigitados.size();
    }

    public void limparDigitacao() {
        gruposDigitados.clear();
        System.out.println("Limpando senha");
    }

    void renovarCombinacoes() {
        // Copia e muda a ordem da lista de fonemas fonemas
        List<String> fonemasDisponiveis = Arrays.asList(fonemas.clone());
        Collections.shuffle(fonemasDisponiveis);
        
        // Faz a divisão em grupos de 3 fonemas
        ArrayList<List<String>> combinacoes = new ArrayList<List<String>>();
        for (int i = 0; i < fonemasDisponiveis.size()/3; i++) {
            List<String> grupo = fonemasDisponiveis.subList(i*3, (i+1)*3);
            combinacoes.add(i, grupo);
        }
        
        this.combinacoes = combinacoes;
    }

    public List<String> obterTextoDosBotoes() {
        ArrayList<String> textoBotoes = new ArrayList<String>();
        for (List<String> comb : combinacoes) {
            textoBotoes.add(String.join(" ", comb));
        }
        return textoBotoes;
    }
}
