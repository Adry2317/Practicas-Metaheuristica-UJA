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

    public ArrayList<int[]> estacionarioOX() {
        int contEvaluaciones = 0;
        ArrayList<int[]> poblacionActual = (ArrayList<int[]>) poblacion.clone();
        ArrayList<Pair<Integer,Long>> evalucionPoblacion;

        long aux = 0;
        long aux2 = 0;
        long aux3 = 0;
        while (contEvaluaciones!=maxEvaluaciones){


            //funcion seleccion
            ArrayList<int[]> padres = seleccionTorneo(poblacionActual);

            long tiempo = System.currentTimeMillis();
            //funcion cruce
            ArrayList<int[]> cruce = operadorCruceOX(padres);
            aux += (System.currentTimeMillis()-tiempo);

            long tiempo2 = System.currentTimeMillis();
            //funcion mutacion
            for (int[] hijos: cruce) {
                double valor = Math.random()*1;
                //float a1 = aleatorio.nextFloat(1.0);
                if(valor>= probMutacion*matrizFlujo.length){
                    int a1 = aleatorio.nextInt(hijos.length);
                    int a2 = aleatorio.nextInt(hijos.length);
                    mutacion(a1,a2, hijos);
                }

            }
            aux2 += (System.currentTimeMillis()-tiempo2);

            long tiempo3 = System.currentTimeMillis();
            evalucionPoblacion = evalPoblacion(poblacionActual);
            //funcion reemplazamiento
            reemplazamiento(cruce, poblacionActual, evalucionPoblacion);

            aux3 += (System.currentTimeMillis()-tiempo3);
            contEvaluaciones++;
        }
        System.out.println(aux);
        System.out.println(aux2);
        System.out.println(aux3);
        return poblacionActual;
    }


    public ArrayList<Pair<Integer,Long>> evalPoblacion(ArrayList<int[]> poblacionActual){
        ArrayList<Pair<Integer,Long>> fitnessIndividuos = new ArrayList<>();
        for(int i = 0; i < poblacionActual.size(); i++){
            Long fitnes = calculoFitnes(poblacionActual.get(i));
            fitnessIndividuos.add(new Pair<>(i,fitnes));
        }

        //ordena de menor a mayor
        fitnessIndividuos.sort((o1, o2) -> o1.getValue().compareTo(o2.getValue()));

        return fitnessIndividuos;
    }



    public void reemplazamiento(ArrayList<int[]> hijos, ArrayList<int[]> poblacion,ArrayList<Pair<Integer,Long>> fitnessPoblacion) {
        ArrayList<int[]> copiaHijos = (ArrayList<int[]>) hijos.clone();
        ArrayList<Integer> peores = new ArrayList<>();
        int a1;
        for (int i = 0; i < 2; i++) {

            ArrayList<Integer> seleccionTorneo = new ArrayList<>();
            for (int j = 0; j < tamTorneoRemplazamiento; j++){

                a1 = aleatorio.nextInt(hijos.size());

                long fitnes = fitnessPoblacion.get(a1).getValue();
                seleccionTorneo.add(a1);

            }

            ArrayList<Integer> auxiliar = new ArrayList<>();
            for(int j = 0; j < seleccionTorneo.size(); j++){
                for (int k = 0; k < fitnessPoblacion.size(); k++){
                    if(seleccionTorneo.get(i) == fitnessPoblacion.get(k).getKey()){
                        auxiliar.add(seleccionTorneo.get(i));
                    }
                }
            }

            auxiliar.sort((o1, o2) ->(o1.compareTo(o2)));
            peores.add(auxiliar.get(auxiliar.size()-1-i));

        }

        long fitnesCopiahijo0 = calculoFitnes(copiaHijos.get(0));
        long fitnesCopiahijo1 = calculoFitnes(copiaHijos.get(1));

        if(fitnesCopiahijo0 < fitnessPoblacion.get(peores.get(0)).getValue()){
            poblacion.set(peores.get(0), copiaHijos.get(0));
        }
        if(fitnesCopiahijo1 < fitnessPoblacion.get(peores.get(1)).getValue() ){
            poblacion.set(peores.get(1), copiaHijos.get(1));
        }



    }

    /*
    public ArrayList<int[]> operadorCrucePMX(ArrayList<int[]> padres) {

        int a1 = -1;
        int a2 = -1;

        ArrayList<Pair<Integer,Integer>> listaCorrespondencia = new ArrayList<>();

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
        int[] hijo2 = new int[padres.get(1).length];

        for(int i = a1; i < a2; i++){
            hijo1[i] = padres.get(0)[i];
            hijo2[i] = padres.get(1)[i];
            listaCorrespondencia.add(new Pair<>(padres.get(0)[i],padres.get(1)[i]));
            listaCorrespondencia.add(new Pair<>(padres.get(1)[i],padres.get(0)[i]));
        }

        boolean llenoH1 = false;
        boolean llenoH2 = false;
        int contadorPosicionHijo1 = a2+1;
        int contadorPosicionHijo2 = a2+1;
        int contEleHijo1 = a2-a1;
        int contEleHijo2 = a2-a1;

        for (int i = a2+1; !llenoH1 && !llenoH2 ; i++) {

            if (i == hijo1.length) {
                i = i % hijo1.length;
            }

            boolean estaH1 = false;
            boolean estaH2 = false;
            int correspondencia1;
            int correspondencia2;
            for(int j = a1; j < a2; j++){
                if (padres.get(1)[i] == padres.get(0)[j]){
                    estaH1 = true;
                    correspondencia1 = padres.get(0)[j];
                }
            }

            for(int j = a1; j < a2; j++){
                if (padres.get(0)[i] == padres.get(1)[j]){
                    estaH2 = true;
                    correspondencia2 = padres.get(1)[j];

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
            }else{
                
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
            }else{
                
            }




        }

    }
    ***/

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
        int[] hijo2 = new int[padres.get(1).length];

        for(int i = a1; i < a2; i++){
            hijo1[i] = padres.get(0)[i];
            hijo2[i] = padres.get(1)[i];
        }

        boolean llenoH1 = false;
        boolean llenoH2 = false;
        int contadorPosicionHijo1 = a2+1;
        int contadorPosicionHijo2 = a2+1;
        int contEleHijo1 = a2-a1;
        int contEleHijo2 = a2-a1;


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
        while (rondas < 2) {//CAMBIAR
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
