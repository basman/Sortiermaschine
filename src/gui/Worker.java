package gui;

import sort.Sortiermethode;

import java.util.ArrayList;

/**
 * Created by Roman on 18.12.2015.
 */
public class Worker implements Runnable {
    private boolean pause = true;
    private Sortiermethode sort;
    private Thread myThread = null;
    private Controller controller;

    Worker(Controller controller) {
        this.controller = controller;
    }

    public void step() {
        haltThread();
        pause = true; // unnötig, aber zur Sicherheit schreib ich's doch.

        myThread = new Thread(this);
        myThread.start();
    }

    public void pauseUnpause() {
        boolean origPause = pause;
        haltThread(); // überschreibt pause, deshalb die Umstände mit Kopie origPause.

        if (origPause) {
            pause = false;
            myThread = new Thread(this);
            myThread.start(); // starte neuen Thread
        } else {
            pause = true;
        }
    }

    private void haltThread() {
        try {
            // wenn noch ein Arbeiter läuft, warte, bis er beendet
            if (myThread != null) {
                pause = true; // Haltesignal setzen
                myThread.join(); // warten, bis der Arbeiter das Signal sieht und run() beendet.
                myThread = null; // nur aus Nettigkeit für den Garbage Collector.
            }
        } catch (InterruptedException e) {
        }
    }

    public void halt() {
        haltThread();
    }

    public void reset(Sortiermethode sort) {
        this.sort = sort;
        this.pause = true;

        haltThread();
        this.sort = sort;
        sort.bereit();
        sort.mischen();
        controller.update();
    }

    /**
     * Untersuche, ob alle Zahlen sortiert sind.
     * @return True or False
     */
    public boolean isSorted() {
        if (sort == null)
            return true;

        ArrayList<Integer> zahlenreihe = sort.getZahlenreihe();
        for(int i = 0; i< zahlenreihe.size(); i++)
            if(zahlenreihe.get(i) != i+1)
                return false;
        return true;
    }

    @Override
    public void run() {
        while(sort != null && !isSorted()) { // führe immer einen Schritt aus, auch wenn pause gesetzt (true) ist.
            sort.sortierSchritt();
            controller.update();

            if (pause)
                return;
            try {
                Thread.sleep(10); // halte für einen Sekundenbruchteil an. Wert ist in Millisekunden.
            } catch (InterruptedException e) {
            }
        }

        controller.sortCompleted();
        pause = true;
    }

    public float getProgress() {
        if(sort == null)
            return 0;

        float c = 0; // gemittelte Entfernung zur Zielposition
        ArrayList<Integer> zahlenreihe = sort.getZahlenreihe();
        for(int i = 0; i< zahlenreihe.size(); i++) {
            Integer value = zahlenreihe.get(i);
            if (value == i + 1)
                c++;
        }
        return c / zahlenreihe.size();
    }
}
