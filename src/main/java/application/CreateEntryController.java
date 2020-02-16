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
import javafx.scene.control.TreeItem;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;

public class CreateEntryController {

    @FXML
    private TextField entryNameTextField;
    @FXML
    private Button createButton;
    @FXML
    private Button cancelButton;

    private static CreateEntryController instance;
    private static Stage stage;

    public static CreateEntryController getInstance() throws IOException {
        if (instance == null) {
            FXMLLoader loader = new FXMLLoader(AppController.class.getResource("/application/createEntry.fxml"));
            Parent root = loader.load();
            instance = loader.getController();

            stage = new Stage();
            stage.setTitle("Input new entry");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
        }

        return instance;
    }

    public void showAndWait() {
        entryNameTextField.requestFocus();
        stage.showAndWait();
    }

    @FXML
    void onCreate() throws IOException {
        TreeItem<String> selectedItem = AppController.getInstance().getSelectedTreeItem();
        Query query = new Query();

        if (selectedItem != null && selectedItem.getParent() != null) {
            if (query.isMainItem(selectedItem.getValue()) || !selectedItem.isLeaf()) {
                /*
                  This will execute if the tree view focus is with the 'items'.
                 */

                //create entry only if no duplicate found
                if (query.isDuplicateEntry(selectedItem.getValue(), "ENTRY",
                        selectedItem.getValue() + "-" + entryNameTextField.getText())) {
                    Entry entry = new Entry();
                    entry.insertItem(selectedItem.getValue(), selectedItem.getValue() + "-" + entryNameTextField.getText());

                    //create table for the created entry
                    Table table = new Table();
                    table.createEntryTable(selectedItem.getValue() + "-" + entryNameTextField.getText());

                    //update tree view after successfully creating the new entry
                    AppController.getInstance().updateTreeView();
                } else {
                    //entered entry name already exist in the record
                    System.out.println("\"" + selectedItem.getValue() + "-" + entryNameTextField.getText() + "\"" + " already existing...");
                }
            } else {
                /*
                  This will execute if the tree view focus is with the 'entries'.
                 */

                //create entry only if no duplicate found
                if (query.isDuplicateEntry(selectedItem.getParent().getValue(), "ENTRY",
                        selectedItem.getParent().getValue() + "-" + entryNameTextField.getText())) {
                    Entry entry = new Entry();
                    entry.insertItem(selectedItem.getParent().getValue(), selectedItem.getParent().getValue() + "-" + entryNameTextField.getText());

                    //create table for the created entry
                    Table table = new Table();
                    table.createEntryTable(selectedItem.getParent().getValue() + "-" + entryNameTextField.getText());

                    //update tree view after successfully creating the new entry
                    AppController.getInstance().updateTreeView();
                } else {
                    //entered entry name already exist in the record
                    System.out.println("\"" + selectedItem.getParent().getValue() + "-" + entryNameTextField.getText() + "\"" + " already existing...");
                }
            }

            //set TreeView focus to the newly created entry
            AppController.getInstance().getTableTreeView().requestFocus();
            AppController.getInstance().setTreeViewFocus(entryNameTextField.getText());

            //clear record text field after successful create
            entryNameTextField.clear();
        } else {
            System.out.println("Please select where to add entry.");
        }

        createButton.getScene().getWindow().hide();
    }

    @FXML
    void onCancel() {
        cancelButton.getScene().getWindow().hide();
    }

}