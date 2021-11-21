import javafx.util.Pair;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class AGG_Clase2_Grupo2 {
    private ArrayList<int[]> poblacion;
    private final int maxEvaluaciones;
    private final int probCruceEstacionario;
    private final double probMutacion;
    public final int tamPoblacion;
    public final double probCruceGeneracional;
    public Random aleatorio;
    public final int tamTorneoSeleccion;
    public final int tamTorneoRemplazamiento;
    public final int[][] matrizFlujo;
    public final int[][] matrizDistancia;

    public AGG_Clase2_Grupo2(ArrayList<int[]> _poblacion, int _maxEvaluaciones, int _probCruceEstacionario, double _probMutacion, Random _aleatorio, int _tamTorneoSeleccion, int _tamTorneoReemplazamiento, int[][] _matrizFlujo, int[][] _matrizDistancia, double _probCruceGeneracional) {

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
        this.probCruceGeneracional = _probCruceGeneracional;

    }

    public ArrayList<int[]> operadorPMX(){
        int contEvaluaciones = 0;
        ArrayList<int[]> poblacionActual = (ArrayList<int[]>) poblacion.clone();
        ArrayList<Pair<Integer,Long>> evaluacionPoblacion = evalPoblacion(poblacionActual);

        while (contEvaluaciones != 1000){

            Pair<Integer,Long> elite = evaluacionPoblacion.get(0);



            //funcion Seleccion
            ArrayList<int[]> padres = torneoSeleccion(poblacionActual,evaluacionPoblacion);

            //funcion de cruce
            ArrayList<int[]> cruce = operadorCrucePMX((ArrayList<int[]>) padres.clone());




            //funcion mutacion
            mutacion(cruce);



            evaluacionPoblacion = evalPoblacion(cruce);

            //remplazamiento
            reemplazamiento(cruce,poblacionActual,evaluacionPoblacion,elite);
           // poblacionActual.clear();
            poblacionActual = (ArrayList<int[]>) cruce.clone();


            contEvaluaciones++;
        }

        return poblacionActual;
    }

    public ArrayList<int[]> operadorOX2(){
        ArrayList<int[]> poblacionActual = (ArrayList<int[]>) poblacion.clone();
        ArrayList<Pair<Integer,Long>> evalucionPoblacion = evalPoblacion(poblacionActual);

        return poblacionActual;
    }


    public ArrayList<int[]> torneoSeleccion( ArrayList<int[]> poblacionActual,ArrayList<Pair<Integer,Long>> evalucionPoblacion) {
        ArrayList<int[]> seleccionados = new ArrayList<>();

        for(int i = 0; i < tamPoblacion; i++){
            if(i == tamPoblacion-1){
                if (evalucionPoblacion.get(i).getValue() <= evalucionPoblacion.get(0).getValue()) {
                    seleccionados.add(poblacionActual.get(i));
                } else {

                    seleccionados.add(poblacionActual.get(0));
                }
            }else {
                //si son iguales se coge el primero
                if (evalucionPoblacion.get(i).getValue() <= evalucionPoblacion.get(i + 1).getValue()) {
                    seleccionados.add(poblacionActual.get(i));
                } else {

                    seleccionados.add(poblacionActual.get(i + 1));
                }
            }
        }

        return seleccionados;
    }


    public ArrayList<int[]> operadorCrucePMX(ArrayList<int[]> padres){
        ArrayList<int[]> padresCruzados = new ArrayList<>();
        for (int i = 0; i < padres.size(); i=i+2){
            int a1 = -1;
            int a2 = -1;

            ArrayList<Pair<Integer,Integer>> listaCorrespondencia = new ArrayList<>();

            do{
                a1 = aleatorio.nextInt(padres.get(i).length);
                a2 = aleatorio.nextInt(padres.get(i).length);

                if(a1 > a2){
                    int aux = a1;
                    a1 = a2;
                    a2 = aux;
                }

            }while (a1 == a2);

            int[] hijo1 = new int[padres.get(i).length];
            int[] hijo2 = new int[padres.get(i).length];

            for(int j = a1; j < a2; j++){
                hijo1[j] = padres.get(i)[j];
                hijo2[j] = padres.get(i+1)[j];
                listaCorrespondencia.add(new Pair<>(padres.get(i)[j],padres.get(i+1)[j]));
                listaCorrespondencia.add(new Pair<>(padres.get(i+1)[j],padres.get(i)[j]));
            }

            boolean llenoH1 = false;
            boolean llenoH2 = false;
            int contadorPosicionHijo1 = a2;
            int contadorPosicionHijo2 = a2;
            int contEleHijo1 = a2-a1;
            int contEleHijo2 = a2-a1;

            int cont=0;
            for (int k = a2; !llenoH1 && !llenoH2 ; k++) {


                if (k == hijo1.length) {
                    k = k % hijo1.length;
                }

                boolean estaH1 = false;
                boolean estaH2 = false;

                for(int j = a1; j < a2; j++){
                    if (padres.get(i+1)[k] == padres.get(i)[j]){
                        estaH1 = true;

                    }
                }

                for(int j = a1; j < a2; j++){
                    if (padres.get(i)[k] == padres.get(i+1)[j]){
                        estaH2 = true;


                    }
                }

                if(!estaH1){

                    if (!llenoH1) {

                        if (contadorPosicionHijo1 == hijo1.length) {
                            contadorPosicionHijo1 = contadorPosicionHijo1 % hijo1.length;
                        }
                        hijo1[contadorPosicionHijo1] = padres.get(i+1)[k];
                        contadorPosicionHijo1++;
                        contEleHijo1++;



                        if (contEleHijo1 == hijo1.length) {
                            llenoH1 = true;
                        }


                    }
                }else{

                    if(!llenoH1) {
                        boolean sinCambio = false;

                        int almacencorrespondencia = padres.get(i+1)[k]; //es el numero repetido

                        ArrayList<Pair<Integer,Integer>>copiaCorrespondencias = (ArrayList<Pair<Integer, Integer>>) listaCorrespondencia.clone();
                        while(!sinCambio){

                            int cont2 = 0;
                            for(int j = 0; j<copiaCorrespondencias.size();j++) {
                                if(almacencorrespondencia == copiaCorrespondencias.get(j).getKey()){
                                    almacencorrespondencia = copiaCorrespondencias.get(j).getValue();
                                    copiaCorrespondencias.remove(j);
                                }else{
                                    cont2++;
                                }
                            }
                            if(cont2 == copiaCorrespondencias.size()){
                                sinCambio = true;
                            }

                        }
                        if (contadorPosicionHijo1 == hijo1.length) {
                            contadorPosicionHijo1 = contadorPosicionHijo1 % hijo1.length;
                        }

                        hijo1[contadorPosicionHijo1] = almacencorrespondencia;
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
                        hijo2[contadorPosicionHijo2] = padres.get(i)[k];
                        contadorPosicionHijo2++;
                        contEleHijo2++;



                        if (contEleHijo2 == hijo2.length) {
                            llenoH2 = true;
                        }
                    }
                }else{
                    if(!llenoH2) {
                        boolean sinCambio = false;

                        int almacencorrespondencia = padres.get(i+1)[k]; //es el numero repetido
                        ArrayList<Pair<Integer,Integer>>copiaCorrespondencias = (ArrayList<Pair<Integer, Integer>>) listaCorrespondencia.clone();
                        while(!sinCambio){
                            int cont2 = 0;
                            for(int j = 0; j<copiaCorrespondencias.size();j++) {
                                if(almacencorrespondencia == copiaCorrespondencias.get(j).getKey()){
                                    almacencorrespondencia = copiaCorrespondencias.get(j).getValue();
                                    copiaCorrespondencias.remove(j);
                                }else{
                                    cont2++;
                                }
                            }
                            if(cont2 == copiaCorrespondencias.size()){
                                sinCambio = true;
                            }

                        }
                        if (contadorPosicionHijo2 == hijo2.length) {
                            contadorPosicionHijo2 = contadorPosicionHijo2 % hijo2.length;
                        }

                        hijo2[contadorPosicionHijo2] = almacencorrespondencia;
                        contadorPosicionHijo2++;
                        contEleHijo2++;



                        if (contEleHijo2 == hijo2.length) {
                            llenoH2 = true;
                        }

                    }
                }




            }

            padresCruzados.add(hijo1);
            padresCruzados.add(hijo2);


        }
        return padresCruzados;
    }





    public void mutacion(ArrayList<int[]> cruce){

        for (int i = 0; i <cruce.size(); i++) {
            for (int j = 0; j < cruce.get(i).length; j++) {
                double valor = aleatorio.nextDouble();
                if (valor >= probMutacion * matrizFlujo.length) {
                    int a1 = aleatorio.nextInt(cruce.get(i).length);
                    int aux = cruce.get(i)[j];
                    cruce.get(i)[j] = cruce.get(i)[a1];
                    cruce.get(i)[a1] = aux;
                }
            }

        }
        System.out.println();
    }

    public void reemplazamiento(ArrayList<int[]> cruce, ArrayList<int[]> poblacionActual, ArrayList<Pair<Integer,Long>> evalFitnes  ,Pair<Integer,Long> elite){
        boolean encontrado  = false;
        for (int i = 0; i < evalFitnes.size() && !encontrado; i++){
            if(evalFitnes.get(i).getValue() == elite.getValue()){
                int [] _elite = poblacionActual.get(elite.getKey());
                int cont = 0;
                for (int j = 0; j < cruce.get(i).length; j++) {
                    if(_elite[j] == cruce.get(i)[j]){
                        cont++;
                    }
                }
                //si el elite ya estaba se sale del bucle
                if(cont == _elite.length){
                    encontrado = true;
                }
            }

        }

        if(!encontrado){
            int posPeor = evalFitnes.get(evalFitnes.size()-1).getKey();
            cruce.set(posPeor, poblacionActual.get(elite.getKey()));
            evalFitnes.set(evalFitnes.get(evalFitnes.size()-1).getKey(), elite);
        }



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
}
