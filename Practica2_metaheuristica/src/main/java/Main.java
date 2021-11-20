import java.util.ArrayList;
import java.util.Random;

public class Main {

    public static void main(String[] args) {

        Configurador config = new Configurador(args[0]);
        ArrayList<int[]> prueba = new ArrayList<>();
        for (int i = 0; i < config.getArchivos().size(); i++) {

            for(int j = 0; j < config.getSemillas().size(); j++) {
                Random aleatorio = new Random(config.getSemillas().get(j));
                ArchivoDatos archivo = new ArchivoDatos(config.getArchivos().get(i));

                AlgGRE_Clase2_Grupo1 greedy = new AlgGRE_Clase2_Grupo1(archivo.getMatrizFlujo(), archivo.getMatrizDistancias(), 5, 50, aleatorio);
                prueba = greedy.calculoGreedyAleatorio();

                AGE_Clase2_Grupo2 prueba2 = new AGE_Clase2_Grupo2(prueba, 50000, 1, 0.001, aleatorio, 3, 4, archivo.getMatrizFlujo(), archivo.getMatrizDistancias());
                ArrayList<int[]> poblacion = prueba2.estacionarioOX();

                ArrayList<Long> fitnes = new ArrayList<>();

                for (int k = 0; k < poblacion.size(); k++) {
                    fitnes.add(prueba2.calculoFitnes(poblacion.get(k)));
                }

                fitnes.sort((o1, o2) -> o1.compareTo(o2));

                System.out.println(fitnes.get(0));
            }

        }


    }
}
