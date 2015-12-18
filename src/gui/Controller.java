package gui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.paint.Color;
import sort.DerChaot;
import sort.Sortiermethode;

import java.util.ArrayList;

public class Controller implements Runnable {
    private boolean running = false;
    private boolean paused = true;
    private Worker worker;

    @FXML
    private Button btnStart;
    @FXML
    private Button btnReset;
    @FXML
    private Button btnStep;
    @FXML
    private Canvas canvas;
    @FXML
    private ComboBox cboxMethods;

    public void init() {
        // Liste der verfügbaren Sortiermethoden setzen
        ArrayList<Sortiermethode> methodList = new ArrayList<>();
        methodList.add(new DerChaot());
        // füge hier weitere Methoden ein:
        // methodList.add(new MeineEigeneSortierMethode());
        ObservableList obList = FXCollections.observableList(methodList);
        cboxMethods.setItems(obList);
        cboxMethods.setValue(methodList.get(0));

        worker = new Worker(this);
        updateButtons(); // run one update cycle in current thread
        worker.reset((Sortiermethode)cboxMethods.getValue());
        redraw();
    }

    public void singleStep(ActionEvent actionEvent) {
        if(!paused || running)
            throw new RuntimeException("singleStep button activated while worker is not paused or not running!");

        worker.step();
    }

    public void pauseUnpause(ActionEvent actionEvent) {
        worker.pauseUnpause();
        running = !running;

        updateButtons();
    }

    public void resetSort(ActionEvent actionEvent) {
        running = false;
        paused = true;
        btnStart.setText("Start");
        btnStep.setDisable(false);
        worker.reset((Sortiermethode)cboxMethods.getValue());
    }

    public void switchSortMethod(ActionEvent actionEvent) {
        worker.halt();
        worker.reset((Sortiermethode)cboxMethods.getValue());
    }

    /**
     * Rückruf-Funktion für Worker. Löst das Bemalen des Canvas in einem Verzögerten Thread aus.
     */
    public void update() {
        Platform.runLater(this);
    }

    private void redraw() {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        ArrayList<Integer> zahlenreihe = ((Sortiermethode) cboxMethods.getValue()).getZahlenreihe();
        int lineWidth = (int)Math.max(canvas.getWidth() / zahlenreihe.size(), 1.0);

        int height = (int)canvas.getHeight();
        int width = (int)canvas.getWidth();

        gc.setLineWidth(lineWidth);

        // finde höchsten Wert für die Skalierung der x-Achse
        float max = 0.1f;
        for(int x: zahlenreihe) {
            if(x > max)
                max = x;
        }

        for(int i=0; i<zahlenreihe.size(); i++) {
            int x = i*lineWidth;
            int y = height - (int)(height*(zahlenreihe.get(i)/max));

            // male weisse linie über dem Strich
            gc.setStroke(Color.WHITE);
            gc.strokeLine(x, y, x, 0);

            // male Strich
            gc.setStroke(Color.BLUE);
            gc.strokeLine(x, height, x, y);
        }
    }

    public void remix(ActionEvent actionEvent) {
        ((Sortiermethode) cboxMethods.getValue()).mischen();
        update();
    }

    void sortCompleted() {
        running = false;
        paused = true;
        update();
    }

    private void updateButtons() {
        if (!running) {
            btnStart.setText("Start");
            btnStep.setDisable(false);
            paused = true;
        } else {
            btnStart.setText("Pause");
            btnStep.setDisable(true);
            paused = false;
        }
    }

    @Override
    public void run() {
        redraw();
        updateButtons();
    }
}
