package ca.qc.bdeb.sim203.projetjavafx;

import ca.qc.bdeb.sim203.projetfx.QuelMario;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {

    final static double TAILLE = 15;
    public static void main(String[] args) {
        launch(args);
    }

    private String[] arrierePlans = {"bg.png", /*"bg-marioworld.png"*/ "level1-1.png"};
    private String[] marioDroite = {"mario-droite.png", "mario2-droite.png"};
    private String[] marioGauche = {"mario-gauche.png", "mario2-gauche.png"};

    private Image[] imagesArrierePlan = new Image[arrierePlans.length];
    private Image[] imagesMarioDroite = new Image[marioDroite.length];
    private Image[] imagesMarioGauche = new Image[marioGauche.length];

    // deux arrierePlans et je ne vais que basculer entre les deux
    private ImageView[] arrierePlanViews;
    private ImageView mario;
    private QuelMario selectionCourante = QuelMario.MARIO_WORLD;

    // la hauteur de la premiere image
    private double hauteurArrierePlanMarioClassique;
    private double largeurArrierePlanMarioClassique;
    private double largeurArrierePlanMarioWorld;
    private boolean marioRegardeVersLaDroite = true;
    private double positionSourisX = 0;
    private double positionSourisY = 0;


    Pane imagePane;
    ScrollPane scrollPane;

    private void creerImages() {

        for (int i = 0; i < imagesArrierePlan.length; i++) {
            if (imagesArrierePlan[i] == null) {
                imagesArrierePlan[i] = new Image(arrierePlans[i]);
                if (i == QuelMario.MARIO_CLASSIQUE.ordinal()) {
                    hauteurArrierePlanMarioClassique = imagesArrierePlan[i].getHeight();
                    largeurArrierePlanMarioClassique = imagesArrierePlan[i].getWidth();
                } else {
                    Image image = new Image(arrierePlans[i]);
                    largeurArrierePlanMarioWorld = imagesArrierePlan[i].getWidth() / imagesArrierePlan[i].getHeight() *  hauteurArrierePlanMarioClassique;
                    imagesArrierePlan[i] = new Image(arrierePlans[i], largeurArrierePlanMarioWorld, hauteurArrierePlanMarioClassique, true, true);
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

        scrollPane = new ScrollPane();
        imagePane = new Pane();

        arrierePlanViews = new ImageView[QuelMario.values().length];
        for (var quel: QuelMario.values()) {
            arrierePlanViews[quel.ordinal()] = new ImageView(imagesArrierePlan[quel.ordinal()]);
            arrierePlanViews[quel.ordinal()].setFitHeight(hauteurArrierePlanMarioClassique);
        }
        scrollPane.setContent(arrierePlanViews[selectionCourante.ordinal()] );

        scrollPane.setMaxWidth(largeurArrierePlanMarioClassique);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        imagePane.getChildren().add(scrollPane);


        mario = new ImageView(imagesMarioDroite[selectionCourante.ordinal()]);
        imagePane.getChildren().add(mario);
        imagePane.setOnMouseMoved(event -> {
            double nouveauX = event.getX() - mario.getImage().getWidth() / 2;
            double nouveauY = event.getY() - mario.getImage().getHeight() / 2;
            if (nouveauY < 0) {
                nouveauY = 0;
            }

             if (nouveauY > hauteurArrierePlanMarioClassique - mario.getImage().getHeight() - TAILLE) {
                nouveauY = hauteurArrierePlanMarioClassique - mario.getImage().getHeight() - TAILLE;
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
        return imagePane;

    }

    private void ajusterImages() {
        scrollPane.setContent(arrierePlanViews[selectionCourante.ordinal()] );
        scrollPane.setMaxWidth(largeurArrierePlanMarioClassique);
        arrierePlanViews[selectionCourante.ordinal()].setX(0);
        arrierePlanViews[selectionCourante.ordinal()].setY(0);
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
        stage.getIcons().add(imagesMarioDroite[QuelMario.MARIO_CLASSIQUE.ordinal()]);
        stage.setTitle("Mario");
        stage.show();
    }
}