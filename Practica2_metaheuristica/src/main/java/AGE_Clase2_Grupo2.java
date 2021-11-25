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
        ArrayList<Pair<Integer,Long>> evalucionPoblacion = evalPoblacion(poblacionActual);



        while (contEvaluaciones!=maxEvaluaciones){


            //funcion seleccion
            ArrayList<int[]> padres = seleccionTorneo(poblacionActual);
            


            //funcion cruce
            ArrayList<int[]> cruce = operadorCruceOX(padres);


            
            //funcion mutacion
            for (int[] hijos: cruce) {
                mutacion(hijos);
            }



            //funcion reemplazamiento
            reemplazamiento(cruce, poblacionActual, evalucionPoblacion);


            contEvaluaciones++;
        }


        return poblacionActual;
    }

    public ArrayList<int[]> estacionarioPMX() {
        int contEvaluaciones = 0;
        ArrayList<int[]> poblacionActual = (ArrayList<int[]>) poblacion.clone();
        ArrayList<Pair<Integer,Long>> evalucionPoblacion = evalPoblacion(poblacionActual);



        while (contEvaluaciones!=maxEvaluaciones){


            //funcion seleccion
            ArrayList<int[]> padres = seleccionTorneo(poblacionActual);



            //funcion cruce
            ArrayList<int[]> cruce = operadorPMX(padres);



            //funcion mutacion

            for (int[] hijos: cruce) {
                //funcion mutacion
                mutacion(hijos);

            }




            //funcion reemplazamiento
            reemplazamiento(cruce, poblacionActual, evalucionPoblacion);


            contEvaluaciones++;
        }


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



    public void reemplazamiento(ArrayList<int[]> hijos, ArrayList<int[]> poblacionActual,ArrayList<Pair<Integer,Long>> fitnessPoblacion) {
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
            poblacionActual.set(peores.get(0), copiaHijos.get(0));
            Pair<Integer,Long> hijo = new Pair(peores.get(0),fitnesCopiahijo0);
            fitnessPoblacion.set(peores.get(0),hijo);

        }
        if(fitnesCopiahijo1 < fitnessPoblacion.get(peores.get(1)).getValue() ){
            Pair<Integer,Long> hijo1 = new Pair(peores.get(1),fitnesCopiahijo1);
            fitnessPoblacion.set(peores.get(1),hijo1);
            poblacionActual.set(peores.get(1), copiaHijos.get(1));
        }



    }

    public ArrayList<int[]> operadorPMX(ArrayList<int[]> padres){

        ArrayList<int[]> hijos = new ArrayList<>();
        //Listas de conmutaciones
        ArrayList<Pair<Integer,Integer>> listaP1 = new ArrayList<>();
        ArrayList<Pair<Integer,Integer>> listaP2 = new ArrayList<>();
        //Para cada padre
        for(int i = 0; i < padres.size(); i=i+2) {
            int a1 = -1;
            int a2 = -1;
            //Mientras loa aleatorios sean distinto
            while(a2 <= a1) {
                a1 = aleatorio.nextInt(padres.get(i).length);
                a2 = aleatorio.nextInt(padres.get(i+1).length);
            }




            int[] hijo1 = new int[padres.get(i).length];
            int[] hijo2 = new int[padres.get(i+1).length];
            //Se llenan los hijos y listas
            for (int j = a1; j < a2; j++) {
                hijo1[j] = padres.get(i+1)[j];
                hijo2[j] = padres.get(i)[j];

                listaP1.add(new Pair<Integer,Integer>(padres.get(i)[j],padres.get(i+1)[j]));
                listaP2.add(new Pair<Integer,Integer>(padres.get(i+1)[j],padres.get(i)[j]));
            }
            //Mientras que no este lleno
            for(int j = 0; j < hijo1.length; j++){
                //Si llegamos al primer corte empezamos en el segundo
                if(j == a1){
                    j = a2;
                }

                //Primer hijo
                int valor1 = padres.get(i)[j];//Valor inicial a meter
                boolean encontrado = true;//Suponemos que el valor esta en la lista de intercambios
                int cont = 0;//Contador moverse lista
                while(encontrado){//Mientras que esta
                    //Si lo encuentra la correspondencia es el nuevo valor
                    if (valor1 == listaP2.get(cont).getKey()){
                        valor1 = listaP2.get(cont).getValue();
                        cont = 0;//Empezamos a buscar de nuevo esa correspondencia
                    }else{
                        cont++;//Si no examinamos la siguiente
                    }
                    //Si lo recorremos todo es que no esta
                    if(cont == listaP2.size()){
                        encontrado = false;
                    }


                }
                hijo1[j] = valor1;//Se asigna el actual

                //Para hijo 2
                int valor2 = padres.get(i+1)[j];
                boolean encontrado2 = true;
                int cont2 = 0;
                while(encontrado2){

                    if (valor2 == listaP1.get(cont2).getKey()){
                        valor2 = listaP1.get(cont2).getValue();
                        cont2 = 0;
                    }else{
                        cont2++;
                    }

                    if(cont2 == listaP1.size()){
                        encontrado2 = false;
                    }


                }
                hijo2[j] = valor2;
            }
            //Se añaden a la lista con todos lops hijos∫
            hijos.add(hijo1);
            hijos.add(hijo2);


        }

        return hijos;
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
        int[] hijo2 = new int[padres.get(1).length];

        for(int i = a1; i < a2; i++){
            hijo1[i] = padres.get(0)[i];
            hijo2[i] = padres.get(1)[i];
        }

        boolean llenoH1 = false;
        boolean llenoH2 = false;
        int contadorPosicionHijo1 = a2;
        int contadorPosicionHijo2 = a2;
        int contEleHijo1 = a2-a1;
        int contEleHijo2 = a2-a1;


        for (int i = 0; !llenoH1 && !llenoH2 ; i++){

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

    public void mutacion(int[] hijo){

            for(int i = 0; i < hijo.length; i++ ) {
                double valor = aleatorio.nextDouble();
                if(valor>= probMutacion*matrizFlujo.length) {
                    int a1 = aleatorio.nextInt(hijo.length);
                    int aux = hijo[i];
                    hijo[i] = hijo[a1];
                    hijo[a1] = aux;
                }
            }


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
                    long fitnes = calculoFitnes(poblacionActual.get(seleccionado));
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
