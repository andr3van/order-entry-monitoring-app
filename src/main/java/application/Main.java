package application;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {


    private static Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        setStage(primaryStage);
        AppController.getInstance().show();

        primaryStage.setOnCloseRequest(evt -> primaryStage.hide());
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static Stage getStage() {
        return stage;
    }

    public static void setStage(Stage stage) {
        Main.stage = stage;
    }
}

