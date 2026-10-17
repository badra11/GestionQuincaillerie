/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bad.gestionquincaillerie.models;

/**
 *
 * @author Badra Aliou Dembele
 */

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class FormatUtilitaire {

    private static final DecimalFormat FORMAT_MONTANT;

    static {
        DecimalFormatSymbols symboles = new DecimalFormatSymbols(Locale.FRANCE);
        symboles.setGroupingSeparator('.'); // point comme séparateur de milliers
        FORMAT_MONTANT = new DecimalFormat("#,##0", symboles);
    }

    /**
     * Formate un montant en FCFA sans décimales, avec point comme séparateur de milliers.
     * Exemple : 130000.0 -> "130.000"
     */
    public static String formaterMontant(double montant) {
        return FORMAT_MONTANT.format(montant);
    }
}
