package application;

import database.Entry;
import database.Query;
import database.Table;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;

public class RenameItemController {

    @FXML
    private TextField newName;
    @FXML
    private Button cancelButton;
    @FXML
    private Button renameButton;

    private static RenameItemController instance;
    private static Stage stage;

    private static String table;
    private static boolean isMainItem;

    public static RenameItemController getInstance(String tableName, boolean isMainItem) throws IOException {
        if (instance == null) {
            FXMLLoader loader = new FXMLLoader(AppController.class.getResource("/application/renameItem.fxml"));
            Parent root = loader.load();
            instance = loader.getController();

            stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
        }

        stage.setTitle("Rename \"" + tableName + "\"");
        setTable(tableName);
        setIsMainItem(isMainItem);

        return instance;
    }

    public void showAndWait() {
        newName.setText("");
        newName.requestFocus();
        stage.showAndWait();
    }

    @FXML
    void onCancel() {
        cancelButton.getScene().getWindow().hide();
    }

    @FXML
    void onRename() throws IOException {
        Table table = new Table();
        Entry entry = new Entry();
        Query query = new Query();

        if (isMainItem) {
            entry.updateItem("Items", getTable(), newName.getText()); //update table "Items" entry
            table.renameTable(getTable(), newName.getText()); //update table name

            for (String itemName : query.getColumnEntries(newName.getText(), "ENTRY")) {
                //update table entry list
                String[] strArr = itemName.split("-", 2);
                entry.updateItem(newName.getText(), itemName, newName.getText().concat("-" + strArr[1]));

                //update entry list table name
                table.renameTable(itemName, newName.getText().concat("-" + strArr[1]));
            }
        } else {
            //update entry table name
            String[] strArr = getTable().split("-", 2);
            table.renameTable(getTable(), getTable().replace(strArr[1], newName.getText()));

            //update entry name from the parent's table entry list
            entry.updateItem(strArr[0], getTable(), getTable().replace(strArr[1], newName.getText()));
        }

        //update tree view after successfully renaming the item
        AppController.getInstance().updateTreeView();

        //set TreeView focus to the renamed item
        Platform.runLater(() -> {
            try {
                AppController.getInstance().getTableTreeView().requestFocus();
                AppController.getInstance().setTreeViewFocus(newName.getText());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        renameButton.getScene().getWindow().hide();
    }

    public static String getTable() {
        return table;
    }

    public static void setTable(String table) {
        RenameItemController.table = table;
    }

    public static void setIsMainItem(boolean isMainItem) {
        RenameItemController.isMainItem = isMainItem;
    }

}
