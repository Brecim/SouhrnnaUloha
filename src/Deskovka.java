public class Deskovka {
    private String nazev;
    private boolean jeKoupena;
    private int oblibenost;

    public Deskovka(String nazev, boolean jeKoupena, int oblibenost) {
        this.nazev = nazev;
        this.jeKoupena = jeKoupena;
        this.oblibenost = oblibenost;
    }

    public String getNazev() {
        return nazev;
    }

    public void setNazev(String nazev) {
        this.nazev = nazev;
    }

    public boolean isJeKoupena() {
        return jeKoupena;
    }

    public void setJeKoupena(boolean jeKoupena) {
        this.jeKoupena = jeKoupena;
    }

    public int getOblibenost() {
        return oblibenost;
    }

    public void setOblibenost(int oblibenost) {
        this.oblibenost = oblibenost;
    }
}
