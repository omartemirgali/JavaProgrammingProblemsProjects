import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Button loadFileButton = new Button("Load File");
        StackPane loadFilePane = new StackPane(loadFileButton);
        loadFilePane.setPrefSize(250,250);
        Scene scene = new Scene(loadFilePane);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Quiz Viewer");
        loadFileButton.setOnAction(event -> {
            try {
                FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("text files","*txt"));
                File file = fileChooser.showOpenDialog(null);
                if (file == null) return;
                Quiz quiz = Quiz.loadFromFile(file.getAbsolutePath());
                if (quiz.getQuestions().size() == 0) throw new InvalidQuizFormatException("");
                else {
                    Scene scene1 = new Scene(questionPane(quiz));
                    primaryStage.setScene(scene1);
                    primaryStage.setTitle("Quiz Viewer");
                    primaryStage.setResizable(false);
                    primaryStage.show();
                }
            } catch (InvalidQuizFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Quiz");
                alert.setTitle("Quiz Viewer: Error");
                alert.setHeaderText("InvalidQuizFormatException");
                alert.setContentText("The file selected does not fit the requirements for a standard Quiz text file format...");
                alert.showAndWait();
            }
        });
        primaryStage.show();
    }
    private TestPane testPane;
    private FillInPane fillInPane;
    private ListView<String> listView;
    private VBox[] panes;
    private BorderPane questionPane(Quiz quiz) {
        BorderPane questionPane = new BorderPane();
        questionPane.setPrefSize(600,230);
        questionPane.setPadding(new Insets(10));
        Text start = new Text("Welcome to QUIZ!!! To start quiz choose any question in the list.");
        start.setFont(Font.font(28));
        start.setWrappingWidth(350);
        questionPane.setCenter(start);
        List<Question> questions = quiz.getQuestions();
        Collections.shuffle(questions);
        panes = new VBox[questions.size()];
        for (int i = 0; i < panes.length; i++) {
            panes[i] = new VBox();
            if (questions.get(i) instanceof Test) {
                testPane = new TestPane(questions.get(i).getDescription());
                panes[i] = testPane;
                testPane.setAnswersOnRadioButton(options(questions, i));
            } else if (questions.get(i) instanceof FillIn) {
                fillInPane = new FillInPane(questions.get(i).toString());
                panes[i] = fillInPane;
            }
        }
        listView = new ListView<String>(FXCollections.observableArrayList(questionNumbers(questions)));
        listView.setMaxSize(100,150);
        questionPane.setLeft(listView);
        listView.getSelectionModel().selectedItemProperty().addListener(ov -> {
            for (Integer i: listView.getSelectionModel().getSelectedIndices()) {
                questionPane.setCenter(panes[i]);
                panes[i].setPadding(new Insets(10));
            }
        });
        questionPane.setBottom(bottomPane(quiz));
        return questionPane;
    }
    private String[] questionNumbers(List<Question> questions) {
        String[] questionNumbers = new String[questions.size()];
        for (int i = 0; i < questionNumbers.length; i++) {
            questionNumbers[i] = "Question #" + (i + 1);
        }
        return questionNumbers;
    }
    private String[] options(List<Question> questions, int i) {
        String description = questions.get(i).getDescription();
        String opt1 = questions.get(i).toString().replaceAll(description, "Description");
        String opt2 = opt1.replaceAll("A\\) ", "");
        String opt3 = opt2.replaceAll("B\\) ", "");
        String opt4 = opt3.replaceAll("C\\) ", "");
        String opt5 = opt4.replaceAll("D\\) ", "");
        String[] arrau = opt5.split("\n");
        List<String> arr = new LinkedList<String>(Arrays.asList(arrau));
        arr.remove(0);
        Collections.shuffle(arr);
        String[] options = arr.toArray(new String[arr.size()]);
        return options;
    }
    private HBox bottomPane(Quiz quiz) {
        HBox bottomPane = new HBox();
        Label status = new Label("STATUS");
        Button finish = new Button("FINISH");
        bottomPane.setSpacing(150);
        bottomPane.setAlignment(Pos.CENTER);
        finish.setStyle("-fx-base: #ADFF2F");
        bottomPane.setSpacing(100);
        bottomPane.setAlignment(Pos.CENTER);
        bottomPane.getChildren().addAll(status, finish);
        return bottomPane;
    }
}
class TestPane extends VBox {
    private RadioButton a, b, c, d;
    private Text clearSelection;

    TestPane(String description) {
        a = new RadioButton(null);
        b = new RadioButton(null);
        c = new RadioButton(null);
        d = new RadioButton(null);
        setSpacing(8);
        clearSelection = new Text("Clear selection");
        clearSelection.setFont(Font.font(11));
        clearSelection.setFill(Color.BLUE);
        Text questionDescription = new Text(description);
        questionDescription.setWrappingWidth(400);
        getChildren().addAll(questionDescription, a, b, c, d, clearSelection);
        ToggleGroup group = new ToggleGroup();
        a.setToggleGroup(group);
        b.setToggleGroup(group);
        c.setToggleGroup(group);
        d.setToggleGroup(group);
        clearSelection.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                clearSelection.setUnderline(true);
            }
        });
        clearSelection.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                clearSelection.setUnderline(false);
            }
        });
        clearSelection.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                a.setSelected(false);
                b.setSelected(false);
                c.setSelected(false);
                d.setSelected(false);
            }
        });
    }
    public void setAnswersOnRadioButton(String[] options) {
        List<String> optionsAns = Arrays.asList(options);
        Collections.shuffle(optionsAns);
        a.setText(optionsAns.get(0));
        b.setText(optionsAns.get(1));
        c.setText(optionsAns.get(2));
        d.setText(optionsAns.get(3));

    }
}
class FillInPane extends VBox {
    private TextField textField;
    private Text clearAnswer;

    FillInPane(String description) {
        textField = new TextField();
        textField.setPromptText("Type your answer ...");
        Text questionDescription = new Text(description);
        questionDescription.setWrappingWidth(370);
        clearAnswer = new Text("Clear answer");
        clearAnswer.setFill(Color.BLUE);
        clearAnswer.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                clearAnswer.setUnderline(true);
            }
        });
        clearAnswer.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                clearAnswer.setUnderline(false);
            }
        });
        clearAnswer.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                textField.setText(null);
            }
        });
        setSpacing(10);
        getChildren().addAll(questionDescription,textField, clearAnswer);
    }
}
