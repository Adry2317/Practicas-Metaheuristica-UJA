import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

public class Main {

    public static void main(String[] args) {

        Configurador config = new Configurador(args[0]);
        ArrayList<int[]> poblacion;
        for (int i = 0; i < config.getArchivos().size(); i++) {
            for(int j = 0; j < config.getSemillas().size(); j++) {
                System.out.println("------------------------------");
                System.out.println("Semilla: "+config.getSemillas().get(j));
                ArchivoDatos archivo = new ArchivoDatos(config.getArchivos().get(i));
                System.out.println("Archivo: "+archivo.getNombreArchivo());
                System.out.println("-------------------------------");
                Random aleatorio = new Random(config.getSemillas().get(j));
                AlgGRE_Clase2_Grupo1 greedy = new AlgGRE_Clase2_Grupo1(archivo.getMatrizFlujo(), archivo.getMatrizDistancias(), 5, 50, aleatorio);
                poblacion = greedy.calculoGreedyAleatorio();



                for (int k = 0; k < config.getAlgoritmos().size(); k++) {
                    switch (config.getAlgoritmos().get(k)){
                        case "EstacionarioOX":


                            System.out.println("EstacionarioOX");
                            AGE_Clase2_Grupo2 estacionarioOX = new AGE_Clase2_Grupo2(poblacion, config.getMaxEvaluaciones(), config.getProbEstacionario(), config.getProbMutacion(), aleatorio, config.get_tamTorneoSeleccionAGE(), config.get_tamTorneoRemplazamientoAGE(), archivo.getMatrizFlujo(), archivo.getMatrizDistancias(),config.getNumeroIndividuosEstacionario(),config.getRondasTorneoEstacionario());

                            ArrayList<int[]> poblacionOX = estacionarioOX.estacionarioOX();

                            ArrayList<Long> fitnesOX = new ArrayList<>();

                            for (int[] ints : poblacionOX) {
                                fitnesOX.add(estacionarioOX.calculoFitnes(ints));
                            }

                            fitnesOX.sort(Comparator.naturalOrder());

                            System.out.println(fitnesOX.get(0));

                            guardarArchivo("log/EstacionarioOX/" + config.getAlgoritmos().get(k) + "_" + archivo.getNombreArchivo() + "_" + config.getSemillas().get(j) + ".txt", estacionarioOX.getLog());



                            break;
                        case "EstacionarioPMX":

                            System.out.println("EstacionarioPMX");
                            AGE_Clase2_Grupo2 estacionarioPMX = new AGE_Clase2_Grupo2(poblacion, config.getMaxEvaluaciones(), config.getProbEstacionario(), config.getProbMutacion(), aleatorio, config.get_tamTorneoSeleccionAGE(), config.get_tamTorneoRemplazamientoAGE(), archivo.getMatrizFlujo(), archivo.getMatrizDistancias(),config.getNumeroIndividuosEstacionario(),config.getRondasTorneoEstacionario());

                            ArrayList<int[]> poblacionPMX = estacionarioPMX.estacionarioPMX();

                            ArrayList<Long> fitnesPMX = new ArrayList<>();

                            for (int[] ints : poblacionPMX) {
                                fitnesPMX.add(estacionarioPMX.calculoFitnes(ints));
                            }

                            fitnesPMX.sort(Comparator.naturalOrder());

                            System.out.println(fitnesPMX.get(0));




                            guardarArchivo("log/EstacionarioPMX/" + config.getAlgoritmos().get(k) + "_" + archivo.getNombreArchivo() + "_" + config.getSemillas().get(j) + ".txt", estacionarioPMX.getLog2());

                            break;

                        case "GeneracionalOX2":

                            System.out.println("GeneracionalOX2");
                            AGG_Clase2_Grupo2 generacionalOX2 = new AGG_Clase2_Grupo2(poblacion,config.getMaxEvaluaciones(),config.getProbMutacion(),aleatorio,config.get_tamTorneoSeleccionAGG(),archivo.getMatrizFlujo(),archivo.getMatrizDistancias(),config.getProbGeneracional(), config.get_posicionesOX2());
                            ArrayList<int[]> poblacionOX2 = generacionalOX2.operadorOX2();
                            ArrayList<Long> fitnesOX2GG = new ArrayList<>();

                            for (int[] ints : poblacionOX2) {
                                fitnesOX2GG.add(generacionalOX2.calculoFitnes(ints));
                            }

                            fitnesOX2GG.sort(Comparator.naturalOrder());

                            System.out.println(fitnesOX2GG.get(0));
                            guardarArchivo("log/GeneracionalOX2/" + config.getAlgoritmos().get(k)+ "-" + archivo.getNombreArchivo()+"-"+ config.getSemillas().get(j)+".txt", generacionalOX2.getLog2());
                            break;
                        case "GeneracioanlPMX":

                            System.out.println("GeneracionalPMX");
                            AGG_Clase2_Grupo2 generacionalPMX = new AGG_Clase2_Grupo2(poblacion,config.getMaxEvaluaciones(),config.getProbMutacion(),aleatorio,config.get_tamTorneoSeleccionAGG(),archivo.getMatrizFlujo(),archivo.getMatrizDistancias(),config.getProbGeneracional(), config.get_posicionesOX2());
                            ArrayList<int[]> poblacionPMXGG = generacionalPMX.operadorPMX();
                            ArrayList<Long> fitnesPMXGG = new ArrayList<>();

                            for (int[] ints : poblacionPMXGG) {
                                fitnesPMXGG.add(generacionalPMX.calculoFitnes(ints));
                            }

                            fitnesPMXGG.sort(Comparator.naturalOrder());

                            System.out.println(fitnesPMXGG.get(0));

                            guardarArchivo("log/GeneracionalPMX/" + config.getAlgoritmos().get(k)+ "-" + archivo.getNombreArchivo()+"-"+ config.getSemillas().get(j)+".txt", generacionalPMX.getLog());
                            break;

                    }
                }

            }

        }
        System.out.println("-------------------------------------------------");


    }

    public static void guardarArchivo(String ruta, String texto) {
        FileWriter fichero = null;
        PrintWriter pw = null;

        try {
            fichero = new FileWriter(ruta);
            pw = new PrintWriter(fichero);

            pw.print(texto);
        } catch (IOException ex) {
        } finally {
            try {
                if (null != fichero) {
                    fichero.close();
                }
            } catch (IOException e2) {
            }
        }

    }
}
