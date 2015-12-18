package sort;

import java.util.ArrayList;

/**
 * Created by Roman on 18.12.2015.
 */
public abstract class Sortiermethode {
    private int MAX_WERT = 1000;
    private int laenge = 150;
    private int MAX_MISCH_SCHRITT = laenge * 1000;

    protected String name = "unbekannt";
    protected ArrayList<Integer> zahlenreihe;

    private int mischSchritte = 0;
    private int sortierSchritte = 0;

    public Sortiermethode() {
        bereit();
    }

    public int getMischSchritte() {
        return mischSchritte;
    }

    public int getSortierSchritte() {
        return sortierSchritte;
    }

    public void bereit() {
        zahlenreihe = new ArrayList<>();
        for(int i=0; i<laenge; i++)
            zahlenreihe.add(i+1);

        mischSchritte = 0;
        sortierSchritte = 0;
    }

    public void mischen() {
        mischSchritte = 0;
        for(int i=0; i<MAX_MISCH_SCHRITT; i++) {
            mischSchritt();
        }
    }

    /**
     * Führt einen einzelnen Mischschritt durch, nämlich durch vertauschen zweier zufällig gewählter Positionen
     * der Zahlenreihe.
     * @return Fortschritt (zwischen 0.0 und 1.0)
     */
    public float mischSchritt() {
        // wähle zwei zufällige Positionen
        int j1 = (int)(Math.random()*laenge);
        int j2 = (int)(Math.random()*laenge);

        vertausche(j1, j2);

        // Fortschritt verzeichnen
        mischSchritte++;
        return mischSchritte / (float)MAX_MISCH_SCHRITT;
    }

    public void vertausche(int j1, int j2) {
        // vertausche die beiden Zahlen an den Positionen in der Zahlenreihe
        int zahl1 = zahlenreihe.get(j1);
        int zahl2 = zahlenreihe.get(j2);
        zahlenreihe.set(j1, zahl2);
        zahlenreihe.set(j2, zahl1);
    }

    public ArrayList<Integer> getZahlenreihe() {
        return zahlenreihe;
    }

    @Override
    public String toString() {
        return name;
    }

    public void sortierSchritt() {
        sortierSchritte++;
    };
}
