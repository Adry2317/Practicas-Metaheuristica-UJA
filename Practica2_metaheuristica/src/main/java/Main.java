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

                AGE_Clase2_Grupo2 prueba2 = new AGE_Clase2_Grupo2(prueba, 50000,1,0.001,aleatorio,3,4, archivo.getMatrizFlujo(), archivo.getMatrizDistancias());
                ArrayList<int[]> poblacion = prueba2.estacionarioOX();

                ArrayList<Long> fitnes = new ArrayList<>();

                for (int j = 0 ; j< poblacion.size(); j++ ){
                    fitnes.add(prueba2.calculoFitnes(poblacion.get(i)));
                }

                fitnes.sort((o1, o2) ->o1.compareTo(o2) );

                System.out.println(fitnes.get(0));


        }


    }
}
