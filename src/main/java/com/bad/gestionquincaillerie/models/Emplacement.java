/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bad.gestionquincaillerie.models;

/**
 *
 * @author Badra Aliou Dembele
 */
public class Emplacement {
    private int idEmplacement;
    private String typeEmplacement;
    private String designationEmplacement;

    public Emplacement() {
    }

    public Emplacement(String typeEmplacement, String designationEmplacement) {
        this.typeEmplacement = typeEmplacement;
        this.designationEmplacement = designationEmplacement;
    }
    
    public Emplacement(int idEmplacement, String typeEmplacement, String designationEmplacement) {
        this.idEmplacement = idEmplacement;
        this.typeEmplacement = typeEmplacement;
        this.designationEmplacement = designationEmplacement;
    }

    public int getIdEmplacement() { return idEmplacement; }
    public void setIdEmplacement(int idEmplacement) { this.idEmplacement = idEmplacement; }

    public String getTypeEmplacement() { return typeEmplacement; }
    public void setTypeEmplacement(String typeEmplacement) { this.typeEmplacement = typeEmplacement; }

    public String getDesignationEmplacement() { return designationEmplacement; }
    public void setDesignationEmplacement(String designationEmplacement) { this.designationEmplacement = designationEmplacement; }

    @Override
    public String toString() {
        return designationEmplacement;
    }
}

