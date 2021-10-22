package br.com.smartzu.acolherapp;

import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;

public class Visitante implements Serializable {


    private String idVisitante;
    private String nomeVisitante;
    private String sexoVisitante;
    private String companhiaVisitante;
    private String dddVisitante;
    private String telVisitante;
    private String emailVisitante;
    private String endVisitante;
    private String receberVisita;
    private String dataVisita;


    public Visitante() {
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebase();

        DatabaseReference produtoRef = firebaseRef
                .child("visitantes");
        setIdVisitante(produtoRef.push().getKey());

    }

    public void setIdVisitante(String idVisitante) {
        this.idVisitante = idVisitante;
    }

    public String getIdVisitante() {
        return idVisitante;
    }

    public String getNomeVisitante() {
        return nomeVisitante;
    }

    public void setNomeVisitante(String nomeVisitante) {
        this.nomeVisitante = nomeVisitante;
    }

    public String getSexoVisitante() {
        return sexoVisitante;
    }

    public void setSexoVisitante(String sexoVisitante) {
        this.sexoVisitante = sexoVisitante;
    }

    public String getCompanhiaVisitante() {
        return companhiaVisitante;
    }

    public void setCompanhiaVisitante(String companhiaVisitante) {
        this.companhiaVisitante = companhiaVisitante;
    }

    public String getDddVisitante() {
        return dddVisitante;
    }

    public void setDddVisitante(String dddVisitante) {
        this.dddVisitante = dddVisitante;
    }

    public String getTelVisitante() {
        return telVisitante;
    }

    public void setTelVisitante(String telVisitante) {
        this.telVisitante = telVisitante;
    }

    public String getEmailVisitante() {
        return emailVisitante;
    }

    public void setEmailVisitante(String emailVisitante) {
        this.emailVisitante = emailVisitante;
    }

    public String getEndVisitante() {
        return endVisitante;
    }

    public void setEndVisitante(String endVisitante) {
        this.endVisitante = endVisitante;
    }

    public String getReceberVisita() {
        return receberVisita;
    }

    public void setReceberVisita(String receberVisita) {
        this.receberVisita = receberVisita;
    }

    public String getDataVisita() {
        return dataVisita;
    }

    public void setDataVisita(String dataVisita) {
        this.dataVisita = dataVisita;
    }

}
