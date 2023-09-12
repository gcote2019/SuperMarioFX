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

    private String[] arrierePlans = {"bg.png", /*"bg-marioworld.png"*/ "level1-1.png"};
    private String[] marioDroite = {"mario-droite.png", "mario2-droite.png"};
    private String[] marioGauche = {"mario-gauche.png", "mario2-gauche.png"};

    private Image[] imagesArrierePlan = new Image[arrierePlans.length];
    private Image[] imagesMarioDroite = new Image[marioDroite.length];
    private Image[] imagesMarioGauche = new Image[marioGauche.length];

    private ImageView arrierePlan;
    private ImageView mario;
    private QuelMario selectionCourante = QuelMario.MARIO_CLASSIQUE;
    private boolean marioRegardeVersLaDroite = true;
    // la hauteur de la premiere image
    private double hauteurArrierePlanMarioClassique;
    // la largeur calculée
    private double largeurCalculeeArrierePlanMarioWorld;
    private double rapportImageVersusPane;
    private double positionSourisX = 0;
    private double positionSourisY = 0;


    Pane imagePane;


    private void creerImages() {

        for (int i = 0; i < imagesArrierePlan.length; i++) {
            if (imagesArrierePlan[i] == null) {
                imagesArrierePlan[i] = new Image(arrierePlans[i]);
                if (i == QuelMario.MARIO_CLASSIQUE.ordinal()) {
                    hauteurArrierePlanMarioClassique = imagesArrierePlan[i].getHeight();
                } else {
                    imagesArrierePlan[i] = new Image(arrierePlans[i]);
                }
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
            if (selectionCourante == QuelMario.MARIO_CLASSIQUE) {
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
            } else {
                double diff = (event.getX() - positionSourisX) * rapportImageVersusPane;
                mario.setImage(diff < 0
                        ? imagesMarioGauche[selectionCourante.ordinal()]
                        : imagesMarioDroite[selectionCourante.ordinal()]);
                double nouveauX = arrierePlan.getX() - diff;
                // one ne veut pas avoir l'image avec du blanc à la gauche
                if (nouveauX > 0)
                    nouveauX = 0;
                // on ne veut pas trop décaler l'image vers la gauche
                if (nouveauX < -largeurCalculeeArrierePlanMarioWorld + imagePane.getWidth())
                    nouveauX = -largeurCalculeeArrierePlanMarioWorld + imagePane.getWidth();

                arrierePlan.setX(nouveauX);
            }
            positionSourisX = event.getX();
            positionSourisY = event.getY();
        });
        imagePane.setPrefSize(arrierePlan.getImage().getWidth(), arrierePlan.getImage().getHeight());
        return imagePane;

    }

    private void ajusterImages() {
        arrierePlan.setImage(imagesArrierePlan[selectionCourante.ordinal()]);
        arrierePlan.setX(0);
        arrierePlan.setY(0);
        if (selectionCourante == QuelMario.MARIO_WORLD) {
            arrierePlan.setFitHeight(hauteurArrierePlanMarioClassique);
            arrierePlan.setPreserveRatio(true);
            System.out.println("imagesArrierePlan[selectionCourante.ordinal()].getWidth() = " + imagesArrierePlan[selectionCourante.ordinal()].getWidth());
            System.out.println("imagesArrierePlan[selectionCourante.ordinal()].getHeight() = " + imagesArrierePlan[selectionCourante.ordinal()].getHeight());
            System.out.println("arrierePlan.getFitWidth() = " + arrierePlan.getFitWidth());
            System.out.println("arrierePlan.getFitHeight() = " + arrierePlan.getFitHeight());
            System.out.println("imagePane.getWidth() = " + imagePane.getWidth());
            System.out.println("imagePane.getHeight() = " + imagePane.getHeight());
            // la largeur de l'arriere plan
            largeurCalculeeArrierePlanMarioWorld = imagesArrierePlan[selectionCourante.ordinal()].getWidth() / imagesArrierePlan[selectionCourante.ordinal()].getHeight() *  imagePane.getHeight();
            rapportImageVersusPane =  largeurCalculeeArrierePlanMarioWorld / imagePane.getWidth();
        }
        mario.setImage(marioRegardeVersLaDroite
                ? imagesMarioDroite[selectionCourante.ordinal()]
                : imagesMarioGauche[selectionCourante.ordinal()]);
        if (selectionCourante == QuelMario.MARIO_WORLD) {
            double nouveauX = (imagePane.getWidth() - mario.getImage().getWidth()) / 2;
            double nouveauY = (imagePane.getHeight() - mario.getImage().getHeight()) / 2;
            mario.setX(nouveauX);
            mario.setY(nouveauY);
        }
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
        stage.getIcons().add(imagesMarioDroite[QuelMario.MARIO_CLASSIQUE.ordinal()]);
        stage.setTitle("Mario");
        stage.show();
    }
}