import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Random;

public class AGE_Clase2_Grupo2 {


    private ArrayList<int[]> poblacion;
    private final int maxEvaluaciones;
    private final int probCruceEstacionario;
    private final double probMutacion;
    public final int tamPoblacion;
    public Random aleatorio;
    public final int tamTorneoSeleccion;
    public final int tamTorneoRemplazamiento;
    public final int[][] matrizFlujo;
    public final int[][] matrizDistancia;

    public AGE_Clase2_Grupo2(ArrayList<int[]> _poblacion, int _maxEvaluaciones, int _probCruceEstacionario, double _probMutacion, Random _aleatorio, int _tamTorneoSeleccion, int _tamTorneoReemplazamiento, int[][] _matrizFlujo, int[][] _matrizDistancia) {

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

    public ArrayList<int[]> estacionario() {
        int contEvaluaciones = 0;
        ArrayList<int[]> poblacionActual = (ArrayList<int[]>) poblacion.clone();

        while (contEvaluaciones!=50000){

            //funcion seleccion
            ArrayList<int[]> padres = seleccionTorneo(poblacionActual);

            //funcion cruce
            ArrayList<int[]> cruce = operadorCruceOX(padres);


            //funcion mutacion
           // System.out.println("iteracion "+contEvaluaciones);

            for (int[] hijos: cruce) {
                double valor = Math.random()*1;
                //float a1 = aleatorio.nextFloat(1.0);
                if(valor>= probMutacion*matrizFlujo.length){
                    int a1 = aleatorio.nextInt(hijos.length);
                    int a2 = aleatorio.nextInt(hijos.length);
                    mutacion(a1,a2, hijos);
                }

            }

            //funcion reemplazamiento
            reemplazamiento(cruce, poblacionActual);


            contEvaluaciones++;
        }

        return poblacionActual;
    }

    public void reemplazamiento(ArrayList<int[]> hijos, ArrayList<int[]> poblacion) {
        ArrayList<int[]> copiaHijos = (ArrayList<int[]>) hijos.clone();
        ArrayList<Pair<Integer,Long>> peores = new ArrayList<>();
        int a1;
        for (int i = 0; i < 2; i++) {

            ArrayList<Pair<Integer,Long>> seleccionTorneo = new ArrayList<>();
            for (int j = 0; j < tamTorneoRemplazamiento; j++){

                a1 = aleatorio.nextInt(hijos.size());

                long fitnes = calculoFitnes(poblacion.get(a1));
                seleccionTorneo.add(new Pair<>(a1,fitnes));

            }

            seleccionTorneo.sort((Pair<Integer, Long> o1, Pair<Integer, Long> o2) -> o1.getValue().compareTo(o2.getValue()));
            peores.add(seleccionTorneo.get(seleccionTorneo.size()-1));

        }
        int cont = 0;

        if(calculoFitnes(copiaHijos.get(0)) < peores.get(0).getValue() && calculoFitnes(copiaHijos.get(1)) < peores.get(0).getValue()){
            cont++;
        }
        if(calculoFitnes(copiaHijos.get(0)) < peores.get(1).getValue() && calculoFitnes(copiaHijos.get(1)) < peores.get(1).getValue()){
            cont++;
        }

        if(cont == 2) {
            poblacion.set(peores.get(0).getKey(), copiaHijos.get(0));
            poblacion.set(peores.get(1).getKey(), copiaHijos.get(1));
        }

    }

    public ArrayList<int[]> operadorCruceOX(ArrayList<int[]> padres){
        int a1 = -1;
        int a2 = -1;

        do{
             a1 = aleatorio.nextInt(padres.get(0).length);
             a2 = aleatorio.nextInt(padres.get(0).length);

            if(a1 > a2){
                int aux = a1;
                a1 = a2;
                a2 = aux;
            }

        }while (a1 == a2);

        int[] hijo1 = new int[padres.get(0).length];
        int[] hijo2 = new int[padres.get(0).length];

        for(int i = a1; i < a2; i++){
            hijo1[i] = padres.get(0)[i];
            hijo2[i] = padres.get(1)[i];
        }

        boolean llenoH1 = false;
        boolean llenoH2 = false;
        int contadorPosicionHijo1 = a2+1;
        int contadorPosicionHijo2 = a2+1;
        int contEleHijo1 = 3;
        int contEleHijo2 = 3;


        for (int i = a2+1; !llenoH1 && !llenoH2 ; i++){

            if(i == hijo1.length){
                i = i % hijo1.length;
            }

            boolean estaH1 = false;
            boolean estaH2 = false;
            for(int j = a1; j < a2; j++){
                if (padres.get(1)[i] == padres.get(0)[j]){
                    estaH1 = true;
                }
            }

            for(int j = a1; j < a2; j++){
                if (padres.get(0)[i] == padres.get(1)[j]){
                    estaH2 = true;
                }
            }

            if(!estaH1){

                if (!llenoH1) {
                    //System.out.println("hijo1Contador"+contadorPosicionHijo1);
                    if (contadorPosicionHijo1 == hijo1.length) {
                        contadorPosicionHijo1 = contadorPosicionHijo1 % hijo1.length;
                    }
                    hijo1[contadorPosicionHijo1] = padres.get(1)[i];
                    contadorPosicionHijo1++;
                    contEleHijo1++;



                    if (contEleHijo1 == hijo1.length) {
                        llenoH1 = true;
                    }


                }
            }

            if(!estaH2){
                if (!llenoH2) {
                    if (contadorPosicionHijo2 == hijo2.length) {
                        contadorPosicionHijo2 = contadorPosicionHijo2 % hijo2.length;
                    }
                    hijo2[contadorPosicionHijo2] = padres.get(0)[i];
                    contadorPosicionHijo2++;
                    contEleHijo2++;



                    if (contEleHijo2 == hijo2.length) {
                        llenoH2 = true;
                    }
                }
            }




        }

        ArrayList<int[]> padresCruzados = new ArrayList<>();
        padresCruzados.add(hijo1);
        padresCruzados.add(hijo2);

        return padresCruzados;
    }

    public void mutacion(int a1, int a2, int[] hijo){
        int aux = hijo[a1];
        hijo[a1] = hijo[a2];
        hijo[a2] = aux;
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
                    if (candidatos.get(j).getKey() == seleccionado) {
                        existe = true;
                    }
                }

                if (!existe) {
                    //Añadimos el valor fitnes y la posicion del padre en la matriz
                    long fitnes = calculoFitnes(poblacion.get(seleccionado));
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
    public long calculoFitnes(int[] padre) {

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
