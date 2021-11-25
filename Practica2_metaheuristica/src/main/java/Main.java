import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

public class Main {

    public static void main(String[] args) {

        Configurador config = new Configurador(args[0]);
        ArrayList<int[]> prueba;
        for (int i = 0; i < config.getArchivos().size(); i++) {

          //  for(int j = 0; j < config.getSemillas().size(); j++) {

                //System.out.println("Semilla: "+config.getSemillas().get(j));
                ArchivoDatos archivo = new ArchivoDatos(config.getArchivos().get(i));

                Random aleatorio = new Random(config.getSemillas().get(0));
                AlgGRE_Clase2_Grupo1 greedy = new AlgGRE_Clase2_Grupo1(archivo.getMatrizFlujo(), archivo.getMatrizDistancias(), 5, 50, aleatorio);
                prueba = greedy.calculoGreedyAleatorio();





                AGE_Clase2_Grupo2 prueba2 = new AGE_Clase2_Grupo2(prueba, 50000, 1, 0.001, aleatorio, 3, 4, archivo.getMatrizFlujo(), archivo.getMatrizDistancias());
                System.out.println("algortimoOX");
                ArrayList<int[]> poblacion = prueba2.estacionarioOX();

                ArrayList<Long> fitnes = new ArrayList<>();

                for (int[] ints : poblacion) {
                    fitnes.add(prueba2.calculoFitnes(ints));
                }

                fitnes.sort(Comparator.naturalOrder());

                System.out.println(fitnes.get(0));

                System.out.println("Algoritmo PMX");

                AGE_Clase2_Grupo2 prueba3 = new AGE_Clase2_Grupo2(prueba, 50000, 1, 0.001, aleatorio, 3, 4, archivo.getMatrizFlujo(), archivo.getMatrizDistancias());

                ArrayList<int[]> poblacion2 = prueba3.estacionarioPMX();

                ArrayList<Long> fitnes2 = new ArrayList<>();

                for (int[] ints : poblacion2) {
                    fitnes2.add(prueba3.calculoFitnes(ints));
                }

                fitnes.sort(Comparator.naturalOrder());

                System.out.println(fitnes2.get(0));

                System.out.println("-------------------------------------");



                /*
                AGG_Clase2_Grupo2 generacional = new AGG_Clase2_Grupo2(prueba, 50000, 1, 0.001, aleatorio, 3, 4, archivo.getMatrizFlujo(), archivo.getMatrizDistancias(),0.7);

                ArrayList<int[]> prueba7 = generacional.operadorPMX();
                ArrayList<Long> fitnes2 = new ArrayList<>();

                for (int[] ints : prueba7) {
                    fitnes2.add(generacional.calculoFitnes(ints));
                }

                fitnes2.sort(Comparator.naturalOrder());

                System.out.println(fitnes2.get(0));

                System.out.println("-------------------------------------");
                */




            }

        //}


    }
}
