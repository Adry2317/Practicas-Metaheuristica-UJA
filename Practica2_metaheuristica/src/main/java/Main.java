import java.util.ArrayList;
import java.util.Random;

public class Main {

    public static void main(String[] args) {
        Random aleatorio = new Random(System.currentTimeMillis());
        Configurador config = new Configurador(args[0]);
        ArrayList<int[]> prueba = new ArrayList<>();
        for (int i = 0; i < config.getArchivos().size(); i++) {
                ArchivoDatos archivo = new ArchivoDatos(config.getArchivos().get(i));

                AlgGRE_Clase2_Grupo1 greedy = new AlgGRE_Clase2_Grupo1(archivo.getMatrizFlujo(), archivo.getMatrizDistancias(),5,50,aleatorio);
                prueba = greedy.calculoGreedyAleatorio();


        }


    }
}
