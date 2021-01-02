package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;

/**
 * This class is an implementation of social network GUI
 * 
 * @author ateam 69
 *
 */

public class Main extends Application {
    // window measurements
    private static final int WINDOW_WIDTH = 900;
    private static final int WINDOW_HEIGHT = 600;
    // title of GUI Window
    private static final String APP_TITLE = "MyNet";
    // text file for friend's log
    private static final String LOG_FILE_NAME = "log.txt";

    // list of friends
    private ListView<Button> friendsList;
    // scene of friends
    private Scene friendScene;
    // scence for friends list
    private Pane friendRoot;

    // data structure used in GUI
    private Network network;
    // vertical list of mutual friends
    private ListView<Button> mutualList = new ListView<>();
    // mutual users for DFS search
    private ComboBox<String> mutualA = new ComboBox<>();

    private ComboBox<String> mutualB = new ComboBox<>();

    // Source and target user for djikstra
    private ComboBox<String> start = new ComboBox<>();

    private ComboBox<String> target = new ComboBox<>();

    private ComboBox<String> targetCentral = new ComboBox<>();
    // vertical list of shortest path of friends
    private ListView<String> shortestList = new ListView<>();
    // stores history
    ListView<String> history = new ListView<>();
    // reset button
    Button reset = new Button("Reset");
    // tells us who central user is
    Label centralUser;

    /**
     * This method shows a graph visualization of central user to friends
     */
    private void updateGraphVisualization() {

        // Friends of the central user
        List<Person> friendList;

        friendRoot.getChildren().clear();

        try {
            friendList = this.network.getFriends(centralUser.getText());
            // Central User node
            Button user = friendButton(centralUser.getText());
            // set width of button and styling
            user.setPrefWidth(50);
            user.setFont(Font.font("Brush Script", 12.5));
            // create containers
            StackPane userStackPane = new StackPane();
            Rectangle userRectangle = new Rectangle();
            // in-line styles
            userRectangle.setFill(Color.AQUA);
            userRectangle.setStroke(Color.BLUEVIOLET);
            userRectangle.setStrokeWidth(3);
            userRectangle.setWidth(user.getText().length() * 15);
            userRectangle.setHeight(30);
            // add nodes to graph visualization
            userStackPane.getChildren().addAll(userRectangle, user);
            userStackPane.setLayoutX(
                    friendScene.getWidth() / 2 - userRectangle.getWidth() / 2);
            userStackPane.setLayoutY(25);
            friendRoot.getChildren().add(userStackPane);

            // Friends of user node
            for (int i = 0; i < friendList.size(); ++i) {

                // The circle
                Circle circle = new Circle();
                circle.setFill(Color.AQUAMARINE);

                circle.setRadius(25);
                circle.setStroke(Color.MEDIUMAQUAMARINE);
                circle.setStrokeWidth(3);

                // The text in the circle
                Button text = friendButton(friendList.get(i).getName());
                // text.setStyle("-fx-background-radius:10px;");
                VBox box = new VBox(new Label(""), text);

                StackPane stack = new StackPane(circle, box);

                stack.setLayoutX(
                        i * friendScene.getWidth() / friendList.size());
                stack.setLayoutY(75);

                stack.setPickOnBounds(false);

                // Start of the Line
                Line line = new Line();
                line.setStartX(userStackPane.getLayoutX()
                        + userRectangle.getWidth() / 2);
                line.setStartY(
                        userStackPane.getLayoutY() + userRectangle.getHeight());

                // End of the Line
                line.setEndX(i * friendScene.getWidth() / friendList.size()
                        + circle.getRadius());
                line.setEndY(75);

                friendRoot.getChildren().add(line);
                friendRoot.getChildren().add(stack);
            }
        } catch (VertexNotFoundException e) {
        }

    }

    /**
     * This method updates comboBox
     */
    private void updateComboBox() {
        // clear and set mutual A
        mutualA.getItems().clear();
        for (Person friend : network.getAllUsers()) {
            mutualA.getItems().add(friend.getName());
        }
        mutualA.setValue("--select person A--");

        mutualB.getItems().clear();
        for (Person friend : network.getAllUsers()) {
            mutualB.getItems().add(friend.getName());
        }
        mutualB.setValue("--select person B--");

        start.getItems().clear();
        // Clear and set mutual B and change friendLIst
        for (Person friend : network.getAllUsers()) {
            start.getItems().add(friend.getName());
        }
        start.setValue("--select starting person--");

        target.getItems().clear();
        for (Person friend : network.getAllUsers()) {
            target.getItems().add(friend.getName());
        }
        target.setValue("--select target person--");

        targetCentral.getItems().clear();
        targetCentral.setValue("--select new central user--");
        for (Person friend : network.getAllUsers()) {
            targetCentral.getItems().add(friend.getName());
        }
    }

    /**
     * This method creates a button of friends of central user in the GUI
     * 
     * @param str name of central user's friends
     * @return button of friends
     */
    private Button friendButton(String str) {
        // create button and add stylse
        Button friend = new Button(str);
        friend.setStyle("-fx-background-color: transparent;");
        friend.setOnAction(new EventHandler<ActionEvent>() {
            // when friend is clicked change central user and change friend list
            @Override
            public void handle(ActionEvent arg0) {
                centralUser.setText(str);
                try {
                    network.setCentralUser(str);
                } catch (VertexNotFoundException e1) {
                }
                List<Person> allFriends;
                try {

                    allFriends = network.getFriends(centralUser.getText());
                    List<Button> list = new LinkedList<>();
                    Button centralUserButton =
                            friendButton(centralUser.getText());
                    list.add(centralUserButton);

                    for (Person friend : allFriends) {
                        list.add(friendButton(friend.getName()));
                    }
                    friendsList.getItems().setAll(list);
                    shortestList.getItems().clear();

                    mutualList.getItems().clear();
                    
                    updateFriendList();
                    reset.fire();

                    updateGraphVisualization();
                } catch (VertexNotFoundException e) {
                }
            }
        });
        return friend;
    }

    /**
     * This method updates the central user's friends list when switching to a
     * central user
     */
    private void updateFriendList() {
        // save user's friends
        List<Person> allFriends;

        try {
            // update friends list to new central user's
            allFriends = network.getFriends(centralUser.getText());
            List<Button> list = new LinkedList<>();
            Button centralUserButton = friendButton(centralUser.getText());
            list.add(centralUserButton);

            for (Person friend : allFriends) {
                list.add(friendButton(friend.getName()));
            }
            friendsList.getItems().setAll(list);
            // add graph visualization of new friend list
            updateGraphVisualization();
            updateComboBox();
            history.getItems().setAll(network.getLog());
        } catch (VertexNotFoundException e) {
        }
    }

    @Override
    /**
     * sets up the GUI Window when application is run
     * 
     * @param primaryStage where all the control objects are found
     * @throws Exception when an exceptional circumstance is encountered
     */
    public void start(Stage primaryStage) throws Exception {
        // root of the scene
        BorderPane root = new BorderPane();
        // root of the login page
        BorderPane loginRoot = new BorderPane();
        friendRoot = new Pane();
        // set dimensions of main, login and friend
        Scene mainScene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        Scene loginScene = new Scene(loginRoot, WINDOW_WIDTH, WINDOW_HEIGHT);
        friendScene = new Scene(friendRoot, 300, 125);
        // use these stylesheets
        mainScene.getStylesheets().add("application/ateam69.css");
        loginScene.getStylesheets().add("application/login.css");

        // PROFILE
        // set central user and inline stule
        centralUser = new Label("NO_CENTRAL_USER");
        centralUser.setFont(new Font("Arial", 30));

        network = new Network();
        // add welcome lable to center and top of login page
        Label label = new Label("Welcome to MyNet!");
        label.setFont(new Font("Arial", 30));
        HBox welcomehbox = new HBox(label);
        welcomehbox.setAlignment(Pos.CENTER);
        loginRoot.setTop(welcomehbox);

        // LOGIN
        // create loging user and password labels
        Label usernameLabel = new Label("Username: ");
        Label pwLabel = new Label("Password:  ");
        Label statusLabel = new Label("");
        // create prompts and forms for password and label
        TextField usernameText = new TextField("");
        usernameText.setPromptText("Enter Username");

        PasswordField pwText = new PasswordField();
        pwText.setPromptText("Enter Password");
        // create login button and inline style
        Button login = new Button("login");
        login.setPrefWidth(Double.MAX_VALUE);
        login.setOnMouseEntered(e -> login.setStyle("-fx-font-size:11pt;"));
        login.setOnMouseExited(e -> login.setStyle("-fx-font-size:10pt;"));
        login.setOnAction(new EventHandler<ActionEvent>() {
            // when user clicks login change scene
            @Override
            public void handle(ActionEvent arg0) {
                String username = usernameLabel.getText().toString();
                String pw = pwLabel.getText().toString();
                if (true) {

                    primaryStage.setScene(mainScene);
                    primaryStage.show();
                    updateGraphVisualization();
                } else {
                    // else show error message
                    statusLabel.setText(
                            "Couldn't find username or password! Try again later!");
                    statusLabel.setTextFill(Color.RED);
                }
            }
        });
        // add username and password to the same container
        HBox usernameBox = new HBox(usernameLabel, usernameText);
        HBox pwBox = new HBox(pwLabel, pwText);
        VBox loginBox = new VBox(usernameBox, pwBox, login, statusLabel);
        loginBox.setMaxWidth(250);
        loginBox.setMaxHeight(150);
        loginBox.setStyle("-fx-background-color: #add8e6;");
        loginBox.setAlignment(Pos.CENTER_LEFT);
        loginBox.setSpacing(10);
        loginBox.setBackground(new Background(new BackgroundFill(
                Color.DARKGREEN, CornerRadii.EMPTY, Insets.EMPTY)));
        loginRoot.setCenter(loginBox);

        // UPLOAD & SAVE MENU BAR
        Menu file = new Menu("File");
        // upload create upload and save button
        MenuItem fileUploadButton = new MenuItem("Upload (ctrl + U)");
        MenuItem fileSaveButton = new MenuItem("Save (ctrl + S)");
        fileUploadButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                // if user clicks show file directory
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Open Resource File");
                fileChooser.getExtensionFilters().add(
                        new ExtensionFilter("Select a .txt file", "*.txt"));
                File myFile = fileChooser.showOpenDialog(primaryStage);
                if (myFile != null) {
                    try {
                        // parse file if it is in correct format
                        String newCentralName = network.parse(myFile);
                        if (newCentralName != null) {
                            centralUser.setText(newCentralName);
                        } else {
                            if (network.getAllUsers().size() > 0) {
                                String name =
                                        network.getAllUsers().get(0).getName();
                                centralUser.setText(name);
                                try {
                                    network.setCentralUser(name);
                                } catch (VertexNotFoundException e) {
                                }
                            }
                        }
                        // show success window and save file
                        updateFriendList();
                        Alert success = new Alert(Alert.AlertType.INFORMATION,
                                myFile.getName()
                                        + " has been successfully uploaded!");
                        success.showAndWait();
                    } catch (FileNotFoundException e) {
                    } catch (InvalidSyntaxException e) {
                        Alert error = new Alert(Alert.AlertType.ERROR,
                                "Invalid format at line " + e.getLineNum()
                                        + "! Please try again with a different file!");
                        error.showAndWait();
                    }
                }
            }
        });
        // create save file button
        fileSaveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                FileChooser fileChooser = new FileChooser();
                // save file if user clicks button
                fileChooser.getExtensionFilters().add(
                        new FileChooser.ExtensionFilter("Text File (*.txt)",
                                "*.txt"));

                File file = fileChooser.showSaveDialog(primaryStage);

                if (file != null) {
                    try {
                        // save network
                        network.save(file.getAbsolutePath());
                        Alert success = new Alert(Alert.AlertType.INFORMATION,
                                "Network has been successfully saved to "
                                        + file.getName());
                        success.showAndWait();
                    } catch (IOException e) {
                    }
                }
            }
        });
        // add buttons and file upload to menu
        file.getItems().add(fileUploadButton);
        file.getItems().add(fileSaveButton);
        MenuBar menu = new MenuBar(file);

        // LOGOUT
        EventHandler<ActionEvent> saveFileEventHandler = event -> {
            Alert saveConfirmation =
                    // prompt user to save file
                    new Alert(Alert.AlertType.CONFIRMATION,
                            "Do you want to save your current network?");

            saveConfirmation.setHeaderText("Save Confirmation");
            // add save, dont save and cancel button to window
            ButtonType saveButton = new ButtonType("Save");
            ButtonType dontSaveButton = new ButtonType("Don't Save");
            ButtonType cancelButton = new ButtonType("Cancel");

            saveConfirmation.getButtonTypes().setAll(cancelButton,
                    dontSaveButton, saveButton);
            // dont save when cancel is pressed
            Button cancelBut = (Button) saveConfirmation.getDialogPane()
                    .lookupButton(cancelButton);
            cancelBut.setDefaultButton(false);
            // save when save button is clicked
            Button saveBut = (Button) saveConfirmation.getDialogPane()
                    .lookupButton(saveButton);
            saveBut.setDefaultButton(true);
            // create save response button
            Optional<ButtonType> saveResponse = saveConfirmation.showAndWait();
            if (saveResponse.get() == saveButton) {
                fileSaveButton.fire();
                // show information window
                Alert info = new Alert(Alert.AlertType.INFORMATION,
                        LOG_FILE_NAME + " has been successfully saved!");
                info.showAndWait();
                primaryStage.setScene(loginScene);
                primaryStage.show();
            } else if (saveResponse.get() == dontSaveButton) {
                // dont save graph when dont save is clicked
                primaryStage.setScene(loginScene);
                primaryStage.show();
            } else {
                // kill process
                event.consume();
            }
        };

        // create LOGOUT button
        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(saveFileEventHandler);

        // Create FRIEND LIST and set dimensions
        this.friendsList = new ListView<>();
        // set friends list
        friendsList.setMaxHeight(225);
        friendsList.setMinWidth(275);

        // Create SEARCH button
        TextField search = new TextField();
        search.setPromptText("Seach for a friend");
        Label underSearch = new Label("");
        Button searchButton = new Button("Search");
        reset.setVisible(false);

        // when search is clicked
        searchButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                List<Person> allFriends;
                try {
                    // get all of central user friends
                    allFriends = network.getFriends(centralUser.getText());
                    List<Button> list = new LinkedList<>();
                    // traverse friends list and add friends to list
                    for (Person friend : allFriends) {
                        if (friend.getName().toLowerCase().contains(search
                                .getCharacters().toString().toLowerCase())) {
                            list.add(friendButton(friend.getName()));
                        }
                    }
                    // update friends list with friends that user wants to
                    // searched
                    friendsList.getItems().setAll(list);
                    underSearch.setText(
                            "Search under \"" + search.getCharacters() + "\"");
                    reset.setVisible(true);
                } catch (VertexNotFoundException e) {
                }

            }
        });

        // when reset button is clicked
        reset.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                List<Person> allFriends;
                try {
                    // reset friends list of central user
                    allFriends = network.getFriends(centralUser.getText());
                    List<Button> list = new LinkedList<>();

                    for (Person friend : allFriends) {
                        list.add(friendButton(friend.getName()));
                    }
                    list.add(friendButton(centralUser.getText()));
                    friendsList.getItems().setAll(list);

                    underSearch.setText("");
                    // hide reset button
                    reset.setVisible(false);
                    // set search button text to empty
                    search.setText("");
                } catch (VertexNotFoundException e) {
                }
            }
        });
        // add friends list to hBox
        HBox friendListBoxSub = new HBox(friendsList);
        // add reset and search button to same container
        HBox resetPack = new HBox(underSearch, reset);
        HBox searchPackSub = new HBox(search, searchButton);
        VBox searchPack = new VBox(searchPackSub, resetPack);
        // set friends list box to my connections
        VBox friendListBox = new VBox(new Label("My Connections:"),
                friendListBoxSub, searchPack);

        // CLOSE OR EXIT
        EventHandler<WindowEvent> confirmCloseEventHandler = event -> {
            Alert closeConfirmation =
                    // show exit window
                    new Alert(Alert.AlertType.CONFIRMATION,
                            "Do you want to exit Social Network?");
            // create ok and exit buttons and exit confirmation
            Button exitButton = (Button) closeConfirmation.getDialogPane()
                    .lookupButton(ButtonType.OK);
            exitButton.setText("Exit");
            closeConfirmation.setHeaderText("Exit Confirmation");
            Optional<ButtonType> closeResponse =
                    closeConfirmation.showAndWait();
            // if user clicks ok close interface
            if (!ButtonType.OK.equals(closeResponse.get())) {
                event.consume();
            } else {
                if (primaryStage.getScene().equals(mainScene)) {
                    // show confirmation window
                    Alert saveConfirmation = new Alert(
                            Alert.AlertType.CONFIRMATION,
                            "Do you want to save your current network?");

                    saveConfirmation.setHeaderText("Save Confirmation");
                    // add the following buttons to the confirmation window
                    ButtonType saveButton = new ButtonType("Save");
                    ButtonType dontSaveButton = new ButtonType("Don't Save");
                    ButtonType cancelButton = new ButtonType("Cancel");
                    // save confirmation button
                    saveConfirmation.getButtonTypes().setAll(cancelButton,
                            dontSaveButton, saveButton);
                    // close window if cancel is clicked
                    Button cancelBut = (Button) saveConfirmation.getDialogPane()
                            .lookupButton(cancelButton);
                    cancelBut.setDefaultButton(false);
                    // save window if button is clicked
                    Button saveBut = (Button) saveConfirmation.getDialogPane()
                            .lookupButton(saveButton);
                    saveBut.setDefaultButton(true);

                    Optional<ButtonType> saveResponse =
                            saveConfirmation.showAndWait();
                    // if sacve button is clicked show information window
                    if (saveResponse.get() == saveButton) {
                        fileSaveButton.fire();
                        Alert info = new Alert(Alert.AlertType.INFORMATION,
                                LOG_FILE_NAME
                                        + " has been successfully saved!");
                        info.setHeaderText(
                                "Thank you for using our Social Network!");
                        info.showAndWait();
                    } else if (saveResponse.get() == dontSaveButton) {
                        // show thank you window
                        Alert info = new Alert(Alert.AlertType.INFORMATION);
                        info.setHeaderText(
                                "Thank you for using our Social Network!");
                        info.showAndWait();
                    } else {
                        // kill window
                        event.consume();
                    }
                } else {
                    // show thanks window
                    Alert info = new Alert(Alert.AlertType.INFORMATION);
                    info.setHeaderText(
                            "Thank you for using our Social Network!");
                    info.showAndWait();
                }
            }
        };
        // trigger close event handler
        primaryStage.setOnCloseRequest(confirmCloseEventHandler);

        // HELP
        Menu helpMenu = new Menu("Help"); // creates an empty menu
        MenuItem helpMenuButton = new MenuItem("Help (ctrl + H)"); // creates
                                                                   // "Help"
                                                                   // menu item

        // sets the actions when Menu item is pressed
        helpMenuButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                final Stage dialog = new Stage(); // pop-up window for menue
                dialog.initModality(Modality.APPLICATION_MODAL);
                dialog.initOwner(primaryStage);
                VBox dialogVbox = new VBox(20);
                // file of instructions to be displayed when "Help" is clicked
                File helpFile = new File("application/instructions.txt");
                Scanner scnr;
                String str = "";
                try {
                    scnr = new Scanner(helpFile);
                    while (scnr.hasNextLine()) { // scans as concatenates
                                                 // contents of "Help" file
                        str += scnr.nextLine() + "\n";
                    }
                } catch (FileNotFoundException e) {
                }

                // adds concatenated string of instructions to the VBox pop-up
                dialogVbox.getChildren().add(new Text(str));

                // sets the size of the pop-up
                Scene dialogScene = new Scene(dialogVbox, 300, 600);
                dialog.setScene(dialogScene);
                dialog.show();
            }
        });
        helpMenu.getItems().add(helpMenuButton); // adds the help button to the
                                                 // Menu
        menu.getMenus().add(helpMenu); // adds Help menu item to the Menu

        // create label called "Number of Connected Groups:"
        Label numGroups = new Label("Number of Connected Groups: "
                + network.numConnectedComponents());
        HBox numGroupsBox = new HBox(numGroups); // add the label to a HBox

        // adds the table containing the list of friends and the label for
        // "Number of connected components" to a VBox to be set to the right of
        // the scene
        VBox rightBox =
                new VBox(friendListBox, numGroupsBox, friendScene.getRoot());
        // setting the dimensions of the VBox
        rightBox.setMaxHeight(450);
        rightBox.setMaxWidth(375);
        rightBox.setSpacing(5);


        // MUTUAL CONNECTION
        // loop to get all users to be displayed in the mutualA ComboBox
        for (Person friend : network.getAllUsers()) {
            mutualA.getItems().add(friend.getName());
        }
        mutualA.setValue("--select person A--"); // sets what to display on
                                                 // ComboBox
        mutualA.setVisibleRowCount(5);

        // loop to get all users to be displayed in the mutualB ComboBox
        for (Person friend : network.getAllUsers()) {
            mutualB.getItems().add(friend.getName());
        }
        mutualB.setValue("--select person B--"); // sets what to display on
                                                 // ComboBox
        mutualB.setVisibleRowCount(5);

        // creates a VBox containing labels of Person A and B (to name the
        // ComboBoxes in the GUI)
        VBox mutualBoxSub = new VBox(new HBox(new Label("Person A:"), mutualA),
                new HBox(new Label("Person B:"), mutualB));
        mutualBoxSub.setSpacing(5);

        mutualList.setPrefSize(300, 100); // sets the dimensions of the JavaFX
                                          // ListView
        Button searchMutualButton = new Button("Search"); // creates button
                                                          // called "Search"

        // sets action for the button above to find the mutual friends between
        // userA and userB
        searchMutualButton.setOnAction(event -> {
            // check that there are two users to compare
            if (mutualA.getValue().equals("--select person A--")
                    || mutualB.getValue().equals("--select person B--")) {
                // show error when exception caught
                Alert error = new Alert(Alert.AlertType.ERROR,
                        "One or both of the responses are invalid");

                // click "OK" to kill error window
                Optional<ButtonType> response = error.showAndWait();
                if (!ButtonType.OK.equals(response.get())) {
                    event.consume();
                }
            } else {
                try {
                    // check for mutual connections using mutualConnections()
                    List<String> strName = network.mutualConnections(
                            mutualA.getValue(), mutualB.getValue());
                    List<Button> btnName = new LinkedList<Button>();
                    // adds mutual friends to a list
                    for (String str : strName) {
                        btnName.add(friendButton(str));
                    }
                    mutualList.getItems().setAll(btnName);
                } catch (VertexNotFoundException e1) { // exception handling
                }
            }
        });

        HBox mutualSub = new HBox(mutualList); // create a HBox for list of
                                               // mutual friends
        // creates a VBox to hold all info for mutual friends (ComboBox, Labels,
        // ListViews)
        VBox mutualBox = new VBox(new Label("Find a mutual connection: "),
                mutualBoxSub, searchMutualButton, mutualSub);
        mutualBox.setSpacing(5);

        // SHORTEST PATH
        // loop to get all users to be displayed in the "start" ComboBox
        for (Person friend : network.getAllUsers()) {
            start.getItems().add(friend.getName());
        }
        start.setValue("--select starting person--"); // sets what to display on
                                                      // ComboBox
        start.setVisibleRowCount(5);

        // loop to get all users to be displayed in the "target" ComboBox
        for (Person friend : network.getAllUsers()) {
            target.getItems().add(friend.getName());
        }
        target.setValue("--select target person--"); // sets what to display on
                                                     // ComboBox
        target.setVisibleRowCount(5);

        // creates a VBox containing labels of Start and Target to identify the
        // ComboBoxes
        VBox shortestBoxSub = new VBox(new HBox(new Label("Start:"), start),
                new HBox(new Label("Target:"), target));
        shortestBoxSub.setSpacing(5);

        Button shortestButton = new Button("Search"); // button called "Search"

        // sets action for the button above to find the shortest connection
        // between 2 users
        shortestButton.setOnAction(event -> {
            // check that there are two users to compare
            if (start.getValue().equals("--select starting person--")
                    || target.getValue().equals("--select target person--")) {
                // show error when exception caught
                Alert error = new Alert(Alert.AlertType.ERROR,
                        "One or both of the responses are invalid");

                // click "OK" to kill error window
                Optional<ButtonType> response = error.showAndWait();
                if (!ButtonType.OK.equals(response.get())) {
                    event.consume();
                }
            } else {
                try {
                    // find shortest connection between 2 users using
                    // djikstraSort() and adding it
                    // to the ViewLsit called "shortestList"
                    shortestList.getItems().setAll(network
                            .djikstraSort(start.getValue(), target.getValue()));
                } catch (VertexNotFoundException e) { // exception handling
                }
            }
        });

        shortestList.setPrefSize(300, 100); // setting dimensions for the
                                            // ListView
        HBox shortestSub = new HBox(shortestList);
        // adds all components for shortest connection in a VBox
        VBox shortestBox = new VBox(new Label("Find the shortest connection: "),
                shortestBoxSub, shortestButton, shortestSub);
        shortestBox.setSpacing(5);

        // to remove line from GUI (color line same as background color)
        Line line1 = new Line(0, 0, 350, 0);
        line1.scaleXProperty().bind(mainScene.widthProperty().multiply(0.001));
        line1.scaleYProperty().bind(mainScene.heightProperty().multiply(0.001));
        line1.setStyle("-fx-stroke-width:100%;");
        line1.setStroke(Color.LIGHTBLUE);

        // for users to pick the central user on thier own
        for (Person friend : network.getAllUsers()) { // loop to get all users
            targetCentral.getItems().add(friend.getName()); // add all users to
                                                            // ComboBox
        }
        targetCentral.setValue("--select new central user--"); // labels the
                                                               // ComboBox
        targetCentral.setVisibleRowCount(5);

        // HERE
        // for "Set Central User" button (actions)
        Button setCentralButton = new Button("Set Central User"); // create
                                                                  // button
        // set action for the button
        setCentralButton.setOnAction(event -> {
            // make sure a user is selected
            if (targetCentral.getValue()
                    .equals("--select new central user--")) {
                // show error when exception caught
                Alert error = new Alert(Alert.AlertType.ERROR,
                        "Please select a central user");
                Optional<ButtonType> response = error.showAndWait();

                // click "OK" to kill error window
                if (!ButtonType.OK.equals(response.get())) {
                    event.consume();
                }
            } else {
                centralUser.setText(targetCentral.getValue()); // updates
                                                               // Central User
                                                               // label in GUI
                try {
                    // changes central user and adds it to the log file
                    network.setCentralUser(targetCentral.getValue());
                } catch (VertexNotFoundException e1) { // exception handling
                }
                updateFriendList(); // updates the friendList displayed
            }
        });

        // add all "Set Central User" components and logout button to a HBox
        HBox setCentralBox =
                new HBox(logoutButton, setCentralButton, targetCentral);
        // adds the HBox above to a VBox to be set to the left of the GUI
        VBox leftBoxSub = new VBox(centralUser, setCentralBox, line1, mutualBox,
                shortestBox);
        leftBoxSub.setSpacing(10);

        // for removing line in GUI (set line color as background color)
        Line line2 = new Line(0, 0, 0, 535);
        line2.setStroke(Color.LIGHTBLUE);

        // set dimensions of the left box
        HBox leftBox = new HBox(leftBoxSub, line2);
        leftBox.setMaxHeight(450);

        // MIDDLE / CENTER BUTTON
        // user actions (clear and add button)
        Button clearButton = new Button("Clear All");
        Button addNewButton = new Button("Add New User");

        clearButton.setMaxWidth(180);
        addNewButton.setMaxWidth(180);
        TextField newUser = new TextField();
        newUser.setPromptText("Type Name Here");
        newUser.setMaxWidth(180);

        // CLEAR
        // When the clear button is clicked
        clearButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                // If there are no central user
                if (centralUser.getText().equals("NO_CENTRAL_USER")) {
                    Alert error = new Alert(Alert.AlertType.ERROR,
                            "No central user found! Please upload a new file or add new user!");

                    // Display the error
                    Optional<ButtonType> response = error.showAndWait();
                    if (!ButtonType.OK.equals(response.get())) {
                        event.consume();
                    }

                    // Otherwise reset the social network
                } else {
                    reset.fire();

                    // Loop to remove all the friends in the network
                    for (Person friend : network.getAllUsers()) {
                        try {
                            network.removeUser(friend.getName());
                        } catch (VertexNotFoundException e) {
                        }
                    }

                    // Reset the central user to none
                    centralUser.setText("NO_CENTRAL_USER");

                    // Empty the friend list
                    friendsList.getItems().clear();
                    
                    history.getItems().setAll(network.getLog());
                    
                    // Reset friendList
                    updateFriendList();

                    // Reset the combo box
                    updateComboBox();

                    // Reset the number of connected components displayed
                    numGroups.setText("Number of Connected Groups: "
                            + network.numConnectedComponents());

                    // Prompt
                    Alert info = new Alert(Alert.AlertType.INFORMATION,
                            "Network has been successfully cleared!");
                    info.showAndWait();

                    // Reset the graph visualization of the friend list
                    updateGraphVisualization();
                }
            }
        });

        // When the "add new user" button is clicked
        addNewButton.setOnAction(event -> {

            // If the user tries to input an empty string
            if (newUser.getText() == null || newUser.getText().equals("")) {
                Alert error = new Alert(Alert.AlertType.ERROR,
                        "Please add username to the corresponding field");

                // Display the error
                Optional<ButtonType> response = error.showAndWait();
                if (!ButtonType.OK.equals(response.get())) {
                    event.consume();
                }

                // Add new user if there is input
            } else {

                try {
                    network.addUser(newUser.getText());

                    // If there are no central user/ First user added
                    if (centralUser.getText().equals("NO_CENTRAL_USER")) {
                        centralUser.setText(newUser.getText());
                    }

                    // Update the number of connected components
                    numGroups.setText("Number of Connected Groups: "
                            + network.numConnectedComponents());

                    // Prompt when user is added successfully
                    Alert info = new Alert(Alert.AlertType.INFORMATION, newUser
                            .getText()
                            + " has been successfully added to the network!");
                    info.showAndWait();
                    updateFriendList();

                    // If the user already exist
                } catch (DuplicateKeyException e1) {
                    Alert error = new Alert(Alert.AlertType.ERROR,
                            "The username already exists in the network");

                    Optional<ButtonType> response = error.showAndWait();
                    if (!ButtonType.OK.equals(response.get())) {
                        event.consume();
                    }
                }
            }

        });

        // FRIEND ACTION
        // Add and remove friend/user button
        Button addButton = new Button("Add Friend");
        Button removeButton = new Button("Remove User");

        TextField newFriend1 = new TextField();
        newFriend1.setPromptText("User A");
        newFriend1.setMaxWidth(90);

        TextField newFriend2 = new TextField();
        newFriend2.setPromptText("User B");
        newFriend2.setMaxWidth(90);

        // Adding friendship
        addButton.setOnAction(event -> {

            // If one of the friends input box is empty
            if (newFriend1.getText() == null || newFriend2.getText() == null
                    || newFriend1.getText().equals("")
                    || newFriend2.getText().equals("")) {
                Alert error = new Alert(Alert.AlertType.ERROR,
                        "Please add username to both fields");

                Optional<ButtonType> response = error.showAndWait();
                if (!ButtonType.OK.equals(response.get())) {
                    event.consume();
                }

                // If both friends to add friendship is the same user
            } else if (newFriend1.getText().equals(newFriend2.getText())) {
                Alert error = new Alert(Alert.AlertType.ERROR,
                        "Please add 2 different usernames");

                Optional<ButtonType> response = error.showAndWait();
                if (!ButtonType.OK.equals(response.get())) {
                    event.consume();
                }

                // Add the friendship between the two friends
            } else {
                try {
                    reset.fire();
                    network.addFriend(newFriend1.getText(),
                            newFriend2.getText());

                    // If there is no central user, set the first friend as
                    // central user
                    if (centralUser.getText().equals("NO_CENTRAL_USER")) {
                        centralUser.setText(newFriend1.getText());
                    }

                    // Update the friendlist
                    updateFriendList();

                    // Update the number of connected components
                    numGroups.setText("Number of Connected Groups: "
                            + network.numConnectedComponents());

                    // Prompt when the friendship is successfully added
                    Alert info = new Alert(Alert.AlertType.INFORMATION,
                            newFriend1.getText() + " and "
                                    + newFriend2.getText()
                                    + " has been successfully added to the network!");
                    info.showAndWait();

                    // If the friendship between two friends already exist
                } catch (DuplicateEdgeException e1) {
                    Alert error = new Alert(Alert.AlertType.ERROR,
                            "The friendship already exists in the network");

                    Optional<ButtonType> response = error.showAndWait();
                    if (!ButtonType.OK.equals(response.get())) {
                        event.consume();
                    }
                }
            }
        });

        // Remove friend
        TextField removeFriend = new TextField();
        removeFriend.setPromptText("Type Name Here");
        removeFriend.setMaxWidth(180);

        // When the remove friend button is clicked
        removeButton.setOnAction(event -> {

            // If there is no user/friends in the network
            if (centralUser.getText().equals("NO_CENTRAL_USER")) {

                // Prompt error
                Alert error = new Alert(Alert.AlertType.ERROR,
                        "No central user found! Please upload a new file or add new user!");
                Optional<ButtonType> response = error.showAndWait();
                if (!ButtonType.OK.equals(response.get())) {
                    event.consume();
                }

                // If the remove friend box is empty/ has no input
            } else if (removeFriend.getText() == null
                    || removeFriend.getText().equals("")) {
                Alert error = new Alert(Alert.AlertType.ERROR,
                        "Please add username to the corresponding field");

                // Prompt error
                Optional<ButtonType> response = error.showAndWait();
                if (!ButtonType.OK.equals(response.get())) {
                    event.consume();
                }

                // If the friend intended to remove is the central user
            } else if (removeFriend.getText().equals(centralUser.getText())) {
                Alert error = new Alert(Alert.AlertType.ERROR,
                        "Removing central user is not allowed");

                // Prompt error
                Optional<ButtonType> response = error.showAndWait();
                if (!ButtonType.OK.equals(response.get())) {
                    event.consume();
                }

                // Remove the friend
            } else {
                try {
                    reset.fire();

                    // Remove the user/friend
                    network.removeUser(removeFriend.getText());

                    numGroups.setText("Number of Connected Groups: "
                            + network.numConnectedComponents());

                    // Prompt when successfully removed
                    Alert info = new Alert(Alert.AlertType.INFORMATION,
                            removeFriend.getText()
                                    + " has been successfully removed from the network!");
                    updateFriendList();

                    info.showAndWait();

                    // If the user does not exist
                } catch (VertexNotFoundException e1) {
                    Alert error = new Alert(Alert.AlertType.ERROR,
                            "The username doesn't exist in the network");

                    // Prompt error
                    Optional<ButtonType> response = error.showAndWait();
                    if (!ButtonType.OK.equals(response.get())) {
                        event.consume();
                    }
                }
            }

        });

        // REMOVE FRIENDSHIP
        Button removeFriendshipButton = new Button("Remove Friendship");

        // Text field for the first friend
        TextField removeFriend1 = new TextField();
        removeFriend1.setPromptText("User A");
        removeFriend1.setMaxWidth(90);

        // Text field for the second friend
        TextField removeFriend2 = new TextField();
        removeFriend2.setPromptText("User B");
        removeFriend2.setMaxWidth(90);

        // When the removeFriendship button is clicked
        removeFriendshipButton.setOnAction(event -> {

            // If there are no users in the network
            if (centralUser.getText().equals("NO_CENTRAL_USER")) {
                Alert error = new Alert(Alert.AlertType.ERROR,
                        "No central user found! Please upload a new file or add new user!");

                // Prompt error
                Optional<ButtonType> response = error.showAndWait();
                if (!ButtonType.OK.equals(response.get())) {
                    event.consume();
                }

                // If one of the text field is empty
            } else if (removeFriend1.getText() == null
                    || removeFriend2.getText() == null
                    || removeFriend1.getText().equals("")
                    || removeFriend2.getText().equals("")) {
                Alert error = new Alert(Alert.AlertType.ERROR,
                        "Please add username to both fields");

                // Prompt error
                Optional<ButtonType> response = error.showAndWait();
                if (!ButtonType.OK.equals(response.get())) {
                    event.consume();
                }

                // If both users are the same
            } else if (removeFriend1.getText()
                    .equals(removeFriend2.getText())) {
                Alert error = new Alert(Alert.AlertType.ERROR,
                        "Please add 2 different usernames or use \"Remove Friend\" instead");

                // Prompt error
                Optional<ButtonType> response = error.showAndWait();
                if (!ButtonType.OK.equals(response.get())) {
                    event.consume();
                }

                // Remove friendship
            } else {
                try {
                    reset.fire();

                    // Update network
                    network.removeFriend(removeFriend1.getText(),
                            removeFriend2.getText());
                    updateFriendList();

                    // Update the number of connected components
                    numGroups.setText("Number of Connected Groups: "
                            + network.numConnectedComponents());

                    // Prompt when friendship is successfully removed
                    Alert info = new Alert(Alert.AlertType.INFORMATION,
                            removeFriend1.getText() + " and "
                                    + removeFriend2.getText()
                                    + " are no longer friend!");
                    info.showAndWait();

                    // If any of the users cannot be found
                } catch (VertexNotFoundException e1) {
                    Alert error = new Alert(Alert.AlertType.ERROR,
                            "One or both of the users couldn't be found");

                    // Prompt error
                    Optional<ButtonType> response = error.showAndWait();
                    if (!ButtonType.OK.equals(response.get())) {
                        event.consume();
                    }

                    // If friendship does not exist in the first place
                } catch (EdgeNotFoundException e1) {
                    Alert error = new Alert(Alert.AlertType.ERROR,
                            "Friendship not found");

                    // Prompt error
                    Optional<ButtonType> response = error.showAndWait();
                    if (!ButtonType.OK.equals(response.get())) {
                        event.consume();
                    }
                }
            }
        });

        // Adjusting the size of the buttons
        addButton.setMaxWidth(180);
        removeButton.setMaxWidth(180);
        removeFriendshipButton.setMaxWidth(180);

        // Setting up the main scene
        VBox addNewUserBox = new VBox(addNewButton, newUser);
        addNewUserBox.setAlignment(Pos.CENTER);

        HBox newFriendBox = new HBox(newFriend1, newFriend2);
        newFriendBox.setAlignment(Pos.CENTER);

        VBox addFriendsBox = new VBox(addButton, newFriendBox);
        addFriendsBox.setAlignment(Pos.CENTER);

        VBox removeFriendBox = new VBox(removeButton, removeFriend);
        removeFriendBox.setAlignment(Pos.CENTER);

        HBox removeFriendshipBoxSub = new HBox(removeFriend1, removeFriend2);
        removeFriendshipBoxSub.setAlignment(Pos.CENTER);

        VBox removeFriendshipBox =
                new VBox(removeFriendshipButton, removeFriendshipBoxSub);
        removeFriendshipBox.setAlignment(Pos.CENTER);

        // The dimension of the history box
        history.setMinSize(100, 150);
        Label historyLabel = new Label("History");
        VBox historyListBox = new VBox(historyLabel, history);
        historyListBox.setPrefSize(150, 180);
        historyListBox.setSpacing(5);

        VBox centerBox = new VBox();
        centerBox.getChildren().addAll(clearButton, addNewUserBox,
                addFriendsBox, removeFriendBox, removeFriendshipBox,
                historyListBox);
        centerBox.setMaxWidth(200);
        centerBox.setSpacing(20);
        centerBox.setMaxHeight(450);
        centerBox.setAlignment(Pos.TOP_CENTER);

        // SET ALL MNEUMONIC
        // Shortcut for upload (CTRL-U)
        mainScene.addEventFilter(KeyEvent.KEY_PRESSED,
                new EventHandler<KeyEvent>() {
                    public void handle(KeyEvent ke) {
                        final KeyCombination keyComb = new KeyCodeCombination(
                                KeyCode.U, KeyCombination.CONTROL_DOWN);
                        if (keyComb.match(ke)) {
                            fileUploadButton.fire();
                        }
                    }
                });
        // Shortcut for save (CTRL-S)
        mainScene.addEventFilter(KeyEvent.KEY_PRESSED,
                new EventHandler<KeyEvent>() {
                    public void handle(KeyEvent ke) {
                        final KeyCombination keyComb = new KeyCodeCombination(
                                KeyCode.S, KeyCombination.CONTROL_DOWN);
                        if (keyComb.match(ke)) {
                            fileSaveButton.fire();
                        }
                    }
                });

        // Shortcut for help menu (CTRL-H)
        mainScene.addEventFilter(KeyEvent.KEY_PRESSED,
                new EventHandler<KeyEvent>() {
                    public void handle(KeyEvent ke) {
                        final KeyCombination keyComb = new KeyCodeCombination(
                                KeyCode.H, KeyCombination.CONTROL_DOWN);
                        if (keyComb.match(ke)) {
                            helpMenuButton.fire();
                        }
                    }
                });

        // Shortcut for exiting (CTRL-F)
        mainScene.addEventFilter(KeyEvent.KEY_PRESSED,
                new EventHandler<KeyEvent>() {
                    public void handle(KeyEvent ke) {
                        final KeyCombination keyComb = new KeyCodeCombination(
                                KeyCode.F, KeyCombination.CONTROL_DOWN);
                        if (keyComb.match(ke)) {
                            Platform.exit();
                            System.exit(0);
                        }
                    }
                });

        // The bottom of the scene with our ateam number / semester/ year
        Line bottomLine = new Line(0, 0, WINDOW_WIDTH, 0);
        bottomLine.setStroke(Color.LIGHTBLUE);
        VBox bottomBox = new VBox(new Label(
                "@ateam-69 CS400 Fall 2019 University of Wisconsin - Madison"));
        bottomBox.setSpacing(20);
        
        // SET LAYOUT
        root.setLeft(leftBox);
        root.setTop(menu);
        root.setRight(rightBox);
        root.setBottom(bottomBox);
        root.setCenter(centerBox);

        // SHOW
        primaryStage.setTitle(APP_TITLE);
        primaryStage.setScene(loginScene);
        primaryStage.show();
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }
}
