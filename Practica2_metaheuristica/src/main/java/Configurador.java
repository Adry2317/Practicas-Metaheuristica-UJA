/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * @author joseantonio
 */
public class Configurador {
    private ArrayList<String> archivos;
    private ArrayList<String> algoritmos;
    private ArrayList<Long> semillas;
    private int TamPoblacion;
    private int MaxEvaluaciones;
    private int probEstacionario;
    private double probGeneracional;
    private double probMutacion;
    private int tamLRC;


    public Configurador(String ruta){
        archivos = new ArrayList<>();
        algoritmos = new ArrayList<>();
        semillas = new ArrayList<>();

        String linea;
        FileReader f = null;

        try {
            f = new FileReader(ruta);
            BufferedReader buff = new BufferedReader(f);
            while ((linea = buff.readLine()) != null) {
                String[] split = linea.split("=");
                switch(split[0]){
                    case "Archivos":
                        String[] v = split[1].split(" ");
                        for (int i = 0; i < v.length; i++) {
                            archivos.add(v[i]);

                        }
                        break;
                    case "Semillas":
                        String[] vSemillas = split[1].split(" ");
                        for (int i = 0; i < vSemillas.length; i++) {
                            semillas.add(Long.parseLong(vSemillas[i]));

                        }
                        break;
                    case "Algoritmos":
                        String[] vAlgoritmos = split[1].split(" ");
                        for (int i = 0; i < vAlgoritmos.length; i++) {
                            algoritmos.add(vAlgoritmos[i]);

                        }
                        break;
                    case "TamPoblacion":
                        TamPoblacion = Integer.parseInt(split[1]);
                        break;
                    case "MaxEvaluaciones":
                        MaxEvaluaciones = Integer.parseInt(split[1]);
                        break;

                    case "probEstacionario":
                        probEstacionario = Integer.parseInt(split[1]);
                        break;

                    case "probGeneracional":
                        probGeneracional = Double.parseDouble(split[1]);
                        break;


                    case "probMutacion":
                        probMutacion = Double.parseDouble(split[1]);
                        break;

                    case "TamLRC":
                        tamLRC = Integer.parseInt(split[1]);
                        break;
                }

            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public ArrayList<String> getArchivos() {
        return archivos;
    }

    public ArrayList<String> getAlgoritmos() {
        return algoritmos;
    }

    public ArrayList<Long> getSemillas() {
        return semillas;
    }

    public int getTamPoblacion() {
        return TamPoblacion;
    }

    public int getMaxEvaluaciones() {
        return MaxEvaluaciones;
    }

    public int getProbEstacionario() {
        return probEstacionario;
    }

    public double getProbGeneracional() {
        return probGeneracional;
    }

    public double getProbMutacion() {
        return probMutacion;
    }

    public int getTamLRC() {
        return tamLRC;
    }
}