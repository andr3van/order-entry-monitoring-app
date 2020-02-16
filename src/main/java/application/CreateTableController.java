package application;

import database.Entry;
import database.Query;
import database.Table;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;

public class CreateTableController {

    @FXML
    private TextField itemNameTextField;
    @FXML
    private Button createButton;
    @FXML
    private Button cancelButton;

    private static CreateTableController instance;
    private static Stage stage;

    public static CreateTableController getInstance() throws IOException {
        if (instance == null) {
            FXMLLoader loader = new FXMLLoader(AppController.class.getResource("/application/createTable.fxml"));
            Parent root = loader.load();
            instance = loader.getController();

            stage = new Stage();
            stage.setTitle("Input new item");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
        }

        return instance;
    }

    public void showAndWait() {
        itemNameTextField.requestFocus();
        stage.showAndWait();
    }

    @FXML
    void onCreate() throws IOException {
        //create item only if no duplicate found
        Query query = new Query();
        if (query.isDuplicateEntry("Items", "ENTRY", itemNameTextField.getText())) {
            //create entry to the root table
            Entry entry = new Entry();
            entry.insertItem("Items", itemNameTextField.getText());

            //create table for the created item
            Table table = new Table();
            table.createItemTable(itemNameTextField.getText());

            //update tree view after successfully creating the new item
            AppController.getInstance().updateTreeView();
        } else {
            //entered item name already exist in the record
            System.out.println("Item " + "\"" + itemNameTextField.getText() + "\"" + " already existing...");
        }

        //set TreeView focus to the newly created item
        AppController.getInstance().getTableTreeView().requestFocus();
        AppController.getInstance().setTreeViewFocus(itemNameTextField.getText());

        //clear table name text field after successful create
        itemNameTextField.clear();

        createButton.getScene().getWindow().hide();

    }

    @FXML
    void onCancel() {
        cancelButton.getScene().getWindow().hide();
    }

    public TextField getItemNameTextField() {
        return itemNameTextField;
    }
}