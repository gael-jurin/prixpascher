package org.nuvola.mobile.prixpascher.dto;

public class ContactVO {
    private String voie;
    private String ville;
    private String phone;
    private String mail;
    private LocationVO loc;

    public ContactVO() {
    }

    public ContactVO(String voie,
                     String ville,
                     LocationVO loc) {
        this.voie = voie;
        this.ville = ville;
        this.loc = loc;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getVoie() {
        return voie;
    }

    public void setVoie(String voie) {
        this.voie = voie;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public LocationVO getLoc() {
        return loc;
    }

    public void setLoc(LocationVO loc) {
        this.loc = loc;
    }
}
