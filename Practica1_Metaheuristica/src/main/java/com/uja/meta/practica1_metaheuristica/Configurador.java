/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uja.meta.practica1_metaheuristica;

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
    private int tamLRC;
    private int iteracionesDLB;
    private int mejoresLRC;
    private double porcentajeOscilacion;
    private int tamListaTabu;
    private float coeficienteOscilacion;

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
                    case "TamLRC":
                        tamLRC = Integer.parseInt(split[1]);
                        break;
                    case "IteracionesDLB":
                        iteracionesDLB = Integer.parseInt(split[1]);
                        break;

                    case "MejoresLRC":
                        mejoresLRC = Integer.parseInt(split[1]);
                        break;

                    case "PorcentajeOscilacion":
                        porcentajeOscilacion = Double.parseDouble(split[1]);
                        break;


                    case "TamListaTabu":
                        tamListaTabu = Integer.parseInt(split[1]);
                        break;

                    case "CoeficienteOscilacion":
                        coeficienteOscilacion = Float.parseFloat(split[1]);
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

    public int getTamLRC() {
        return tamLRC;
    }

    public int getIteracionesDLB() {
        return iteracionesDLB;
    }

    public int getMejoresLRC() {
        return mejoresLRC;
    }

    public double getPorcentajeOscilacion() {
        return porcentajeOscilacion;
    }

    public int getTamListaTabu() {
        return tamListaTabu;
    }

    public float getCoeficienteOscilacion() {
        return coeficienteOscilacion;
    }
}