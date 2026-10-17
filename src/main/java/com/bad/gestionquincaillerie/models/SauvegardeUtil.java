/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bad.gestionquincaillerie.models;

/**
 *
 * @author Badra Aliou Dembele
 */

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SauvegardeUtil {

    public static boolean sauvegarder(String cheminMysqldump, String dossierDestination, String motDePasseMysql) {
        String horodatage = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        String nomFichier = "sauvegarde_gestion_quincaillerie_" + horodatage + ".sql";
        File fichierSortie = new File(dossierDestination, nomFichier);

        try {
            ProcessBuilder pb;
            if (motDePasseMysql == null || motDePasseMysql.isEmpty()) {
                pb = new ProcessBuilder(cheminMysqldump, "-u", "root", "gestion_quincaillerie");
            } else {
                pb = new ProcessBuilder(cheminMysqldump, "-u", "root", "-p" + motDePasseMysql, "gestion_quincaillerie");
            }
            pb.redirectOutput(fichierSortie);
            Process process = pb.start();
            int code = process.waitFor();
            return code == 0;

        } catch (IOException | InterruptedException e) {
            System.err.println("Erreur sauvegarde : " + e.getMessage());
            return false;
        }
    }
}
