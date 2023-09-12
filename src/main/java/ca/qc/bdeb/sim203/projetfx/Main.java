package ca.qc.bdeb.sim203.projetjavafx;

import ca.qc.bdeb.sim203.projetfx.QuelMario;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    private String[] arrierePlans = {"bg.png", "bg-marioworld.png"};
    private String[] marioDroite = {"mario-droite.png", "mario2-droite.png"};
    private String[] marioGauche = {"mario-gauche.png", "mario2-gauche.png"};

    private Image[] imagesArrierePlan = new Image[arrierePlans.length];
    private Image[] imagesMarioDroite = new Image[marioDroite.length];
    private Image[] imagesMarioGauche = new Image[marioGauche.length];

    private ImageView arrierePlan;
    private ImageView mario;
    private QuelMario selectionCourante = QuelMario.MARIO_CLASSIQUE;
    private boolean marioRegardeVersLaDroite = true;


    Pane imagePane;


    private void creerImages() {
        for (int i = 0; i < imagesArrierePlan.length; i++) {
            if (imagesArrierePlan[i] == null) {
                imagesArrierePlan[i] = new Image(arrierePlans[i], 950, 534, true, true);
            }
            if (imagesMarioDroite[i] == null) {
                imagesMarioDroite[i] = new Image(marioDroite[i], 64, 64, true, true);
            }
            if (imagesMarioGauche[i] == null) {
                imagesMarioGauche[i] = new Image(marioGauche[i], 64, 64, true, true);
            }
        }
    }


    private Pane creerImagePane() {
        creerImages();

        imagePane = new Pane();

        arrierePlan = new ImageView(imagesArrierePlan[selectionCourante.ordinal()]);
        imagePane.getChildren().add(arrierePlan);


        mario = new ImageView(imagesMarioDroite[selectionCourante.ordinal()]);
        imagePane.getChildren().add(mario);
        imagePane.setOnMouseMoved(event -> {
            double nouveauX = event.getX() - mario.getImage().getWidth() / 2;
            double nouveauY = event.getY() - mario.getImage().getHeight() / 2;
            if (nouveauY < 0) {
                nouveauY = 0;
            }
            if (nouveauX < mario.getX()) {
                mario.setImage(imagesMarioGauche[selectionCourante.ordinal()]);
                marioRegardeVersLaDroite = false;
            } else if (nouveauX > mario.getX()) {
                mario.setImage(imagesMarioDroite[selectionCourante.ordinal()]);
                marioRegardeVersLaDroite = true;
            }
            mario.setX(nouveauX);
            mario.setY(nouveauY);
        });
        imagePane.setPrefSize(arrierePlan.getImage().getWidth(), arrierePlan.getImage().getHeight());
        return imagePane;

    }

    private void ajusterImages() {
        arrierePlan.setImage(imagesArrierePlan[selectionCourante.ordinal()]);
        mario.setImage(marioRegardeVersLaDroite
                ? imagesMarioDroite[selectionCourante.ordinal()]
                : imagesMarioGauche[selectionCourante.ordinal()]);
    }
    private Pane creerMenu() {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        Button bouton = new Button("Quitter");
        bouton.setOnAction(event -> {
            Platform.exit();
        });
        hBox.getChildren().add(bouton);
        bouton = new Button("Mario classique");
        bouton.setOnAction(event -> {
            selectionCourante = QuelMario.MARIO_CLASSIQUE;
            ajusterImages();
        });
        hBox.getChildren().add(bouton);
        bouton = new Button("Mario World");
        bouton.setOnAction(event -> {
            selectionCourante = QuelMario.MARIO_WORLD;
            ajusterImages();
        });
        hBox.getChildren().add(bouton);

        return hBox;
    }
    @Override
    public void start(Stage stage) throws Exception {
        BorderPane root = new BorderPane();
        Pane hBox = creerMenu();

        root.setTop(hBox);
        Pane imagePane = creerImagePane();
        root.setCenter(imagePane);
        Scene scene = new Scene(root);
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                Platform.exit();
            }
        });
        stage.setScene(scene);
        stage.setTitle("Mario");
        stage.show();
    }
}