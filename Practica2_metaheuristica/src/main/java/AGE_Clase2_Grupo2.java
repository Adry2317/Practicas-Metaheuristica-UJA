import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

public class AGE_Clase2_Grupo2 {


    private ArrayList<int[]> poblacion;
    private final int maxEvaluaciones;
    private final int probCruceEstacionario;
    private final int probMutacion;
    public final int tamPoblacion;
    public Random aleatorio;
    public final int tamTorneoSeleccion;
    public final int tamTorneoRemplazamiento;
    public final int[][] matrizFlujo;
    public final int[][] matrizDistancia;

    public AGE_Clase2_Grupo2(ArrayList<int[]> _poblacion, int _maxEvaluaciones, int _probCruceEstacionario, int _probMutacion, Random _aleatorio, int _tamTorneoSeleccion, int _tamTorneoReemplazamiento, int[][] _matrizFlujo, int[][] _matrizDistancia) {

        this.poblacion = _poblacion;
        this.maxEvaluaciones = _maxEvaluaciones;
        this.probCruceEstacionario = _probCruceEstacionario;
        this.probMutacion = _probMutacion;
        this.tamPoblacion =  this.poblacion.size();
        this.aleatorio = _aleatorio;
        this.tamTorneoSeleccion = _tamTorneoSeleccion;
        this.tamTorneoRemplazamiento = _tamTorneoReemplazamiento;
        this.matrizFlujo = _matrizFlujo;
        this.matrizDistancia = _matrizDistancia;

    }

    public ArrayList<int[]> estacionario(){

        ArrayList<int[]> poblacionActual = (ArrayList<int[]>) poblacion.clone();

        //funcion seleccion
        ArrayList<int[]> padres = seleccionTorneo(poblacionActual);

        //funcion cruce

        return poblacionActual;
    }


    public ArrayList<int[]> seleccionTorneo(ArrayList<int[]> poblacionActual) {
        //clave->posicion, valor -> fitnes
        ArrayList<Pair<Integer, Long>> candidatos = new ArrayList<>();
        ArrayList<Pair<Integer, Long>> padresSeleccionados = new ArrayList<>();

        int rondas = 0;
        while (rondas < 2) {
            for (int i = 0; i < tamTorneoSeleccion; i++) {
                int seleccionado = aleatorio.nextInt(tamPoblacion);

                boolean existe = false;
                for (int j = 0; j < candidatos.size() && !existe; j++) {
                    if (candidatos.get(i).getKey() == seleccionado) {
                        existe = true;
                    }
                }

                if (!existe) {
                    //Añadimos el valor fitnes y la posicion del padre en la matriz
                    long fitnes = calculaCoste(poblacion.get(seleccionado));
                    candidatos.add(new Pair<>(seleccionado, fitnes));
                }

            }

            //seleccionamos al mejor de entre los 3
            rondas++;
            candidatos.sort((Pair<Integer, Long> o1, Pair<Integer, Long> o2) -> o1.getValue().compareTo(o2.getValue()));
            padresSeleccionados.add(candidatos.get(0));

            //vaciamos para la sguiente ejecucion
            candidatos.clear();

        }

       ArrayList<int[]> padres = new ArrayList<>();
       for(int i = 0; i < padresSeleccionados.size(); i++) {
           padres.add(poblacion.get(padresSeleccionados.get(i).getKey()));
       }

       return padres;
    }



    /**
     * Funcion que calcula el coste de la solucion pasada
     *
     * @param padre: Vector con solucion para calculo del coste
     * @return coste calculado
     */
    public long calculaCoste(int[] padre) {

        long coste = 0;

        for (int i = 0; i < padre.length; i++) {
            for (int j = 0; j < padre.length; j++) {
                if (i != j) {
                    coste = coste + (matrizFlujo[i][j] * matrizDistancia[padre[i]][padre[j]]);
                }
            }
        }
        return coste;
    }


}
