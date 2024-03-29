import javafx.application.Application;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
public class ListViewDemo extends Application {
    private String[] flagTitles = {"Canada", "China", "Denmark",
            "France", "Germany", "India", "Norway", "United Kingdom",
            "United States of America"};
    // Declare an ImageView array for the national flags of 9 countries
    private ImageView[] ImageViews = {
            new ImageView("https://upload.wikimedia.org/wikipedia/commons/a/a0/Sunflower_as_gif_websafe.gif"),
            new ImageView("https://upload.wikimedia.org/wikipedia/commons/a/a0/Sunflower_as_gif_websafe.gif"),
            new ImageView("https://upload.wikimedia.org/wikipedia/commons/a/a0/Sunflower_as_gif_websafe.gif"),
            new ImageView("https://upload.wikimedia.org/wikipedia/commons/a/a0/Sunflower_as_gif_websafe.gif"),
            new ImageView("https://upload.wikimedia.org/wikipedia/commons/a/a0/Sunflower_as_gif_websafe.gif"),
            new ImageView("https://upload.wikimedia.org/wikipedia/commons/a/a0/Sunflower_as_gif_websafe.gif"),
            new ImageView("https://upload.wikimedia.org/wikipedia/commons/a/a0/Sunflower_as_gif_websafe.gif"),
            new ImageView("https://upload.wikimedia.org/wikipedia/commons/a/a0/Sunflower_as_gif_websafe.gif"),
            new ImageView("https://upload.wikimedia.org/wikipedia/commons/a/a0/Sunflower_as_gif_websafe.gif")
    };
    @Override // Override the start method in the Application class
    public void start(Stage primaryStage) {
        ListView<String> lv = new ListView<>
                (FXCollections.observableArrayList(flagTitles));
        lv.setPrefSize(400, 400);
        lv.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        // Create a pane to hold image views
        FlowPane imagePane = new FlowPane(10, 10);
        BorderPane pane = new BorderPane();
        pane.setLeft(new ScrollPane(lv));
        pane.setCenter(imagePane);
        lv.getSelectionModel().selectedItemProperty().addListener(
                ov -> {
                    imagePane.getChildren().clear();
                    for (Integer i: lv.getSelectionModel().getSelectedIndices()) {
                        imagePane.getChildren().add(ImageViews[i]);
                    }
                });
        // Create a scene and place it in the stage
        Scene scene = new Scene(pane, 450, 170);
        primaryStage.setTitle("ListViewDemo"); // Set the stage title
        primaryStage.setScene(scene); // Place the scene in the stage
        primaryStage.show(); // Display the stage
    }
}