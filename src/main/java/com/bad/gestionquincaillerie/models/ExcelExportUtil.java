/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bad.gestionquincaillerie.models;

/**
 *
 * @author Badra Aliou Dembele
 */

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ExcelExportUtil {

    public static boolean exporter(String cheminFichier, String[] entetes, List<Object[]> lignes) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet feuille = workbook.createSheet("Rapport");

            Row ligneEntete = feuille.createRow(0);
            for (int i = 0; i < entetes.length; i++) {
                ligneEntete.createCell(i).setCellValue(entetes[i]);
            }

            int numeroLigne = 1;
            for (Object[] ligne : lignes) {
                Row row = feuille.createRow(numeroLigne++);
                for (int i = 0; i < ligne.length; i++) {
                    Object valeur = ligne[i];
                    if (valeur instanceof Number) {
                        row.createCell(i).setCellValue(((Number) valeur).doubleValue());
                    } else {
                        row.createCell(i).setCellValue(valeur != null ? valeur.toString() : "");
                    }
                }
            }

            for (int i = 0; i < entetes.length; i++) {
                feuille.autoSizeColumn(i);
            }

            try (FileOutputStream fos = new FileOutputStream(cheminFichier)) {
                workbook.write(fos);
            }
            return true;

        } catch (IOException e) {
            System.err.println("Erreur export Excel : " + e.getMessage());
            return false;
        }
    }
}
