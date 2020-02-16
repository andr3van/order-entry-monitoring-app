package application;

import com.jfoenix.controls.JFXButton;
import database.Connection;
import database.Entry;
import database.Query;
import database.Table;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import orders.OrderEntry;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AppController implements Initializable {

    @FXML
    private Label connStat;
    @FXML
    private Button connect;
    @FXML
    private Button disconnect;
    @FXML
    private JFXButton tableDelete;
    @FXML
    private JFXButton entryInsert;
    @FXML
    private Button exit;
    @FXML
    private TreeView<String> tableTreeView;
    @FXML
    private JFXButton tableCreate;

    //table details
    @FXML
    private TableView<OrderEntry> itemDetailsTable;
    @FXML
    private TableColumn<OrderEntry, Integer> orderNoCol;
    @FXML
    private TableColumn<OrderEntry, String> itemCol;
    @FXML
    private TableColumn<OrderEntry, Double> unitPriceCol;
    @FXML
    private TableColumn<OrderEntry, Integer> qtyCol;
    @FXML
    private TableColumn<OrderEntry, Double> totalAmtCol;

    private static AppController instance;
    private static Stage stage;

    private final String disconnectedLabel = "Currently not connected...";
    private TreeItem<String> rootItem = new TreeItem<>("Items");

    private FilteredList<OrderEntry> filteredList;

    //current selection details
    private TreeItem<String> selectedTreeItem;
    private OrderEntry selectedOrderEntry;
    private String selectedTable;
    private String selectedItem;
    private String selectedColumn;
    private int selectedRowID;
    private int selectedColIndex;
    private int selectedRowIndex;

    private ObservableList<OrderEntry> data;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        selectedTreeItem = rootItem;
        itemDetailsTable.setPlaceholder(new Label(""));
        connStat.setStyle("-fx-text-fill: red");
        connStat.setText(disconnectedLabel);
        disableButtons(true);

        /*
          Tooltips
         */
        Tooltip addItemTooltip = new Tooltip("Add item");
        addItemTooltip.setStyle("-fx-font-size: 12px");
        Tooltip.install(tableCreate, addItemTooltip);

        Tooltip addEntryTooltip = new Tooltip("Add order");
        addEntryTooltip.setStyle("-fx-font-size: 12px");
        Tooltip.install(entryInsert, addEntryTooltip);

        Tooltip tableDeleteTooltip = new Tooltip("Delete selection");
        tableDeleteTooltip.setStyle("-fx-font-size: 12px");
        Tooltip.install(tableDelete, tableDeleteTooltip);

        data = FXCollections.observableArrayList(orderEntry -> new Observable[]{
                orderEntry.orderNoProperty(),
                orderEntry.itemNameProperty(),
                orderEntry.unitPriceProperty(),
                orderEntry.quantityProperty(),
                orderEntry.totalAmtProperty(),
        });

        filteredList = new FilteredList<>(data);
        itemDetailsTable.setItems(filteredList);
        itemDetailsTable.setEditable(true);
        itemDetailsTable.getSelectionModel().setCellSelectionEnabled(true);

        orderNoCol.setEditable(true);
        orderNoCol.setCellValueFactory(new PropertyValueFactory<>("orderNo"));
        orderNoCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

        itemCol.setEditable(true);
        itemCol.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        itemCol.setCellFactory(TextFieldTableCell.forTableColumn());

        unitPriceCol.setEditable(true);
        unitPriceCol.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        unitPriceCol.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));

        qtyCol.setEditable(true);
        qtyCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        qtyCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

        totalAmtCol.setEditable(true);
        totalAmtCol.setCellValueFactory(new PropertyValueFactory<>("totalAmt"));
        totalAmtCol.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));

        /*
          Listens for any changes made in the table
         */
        filteredList.addListener((ListChangeListener.Change<? extends OrderEntry> c) -> {
            while (c.next()) {
                //add entry listener
                if (c.wasAdded()) {
                    System.out.println("Added:");
                    c.getAddedSubList().forEach(System.out::println);
                    System.out.println();

                }

                //delete entry listener
                if (c.wasRemoved()) {
                    System.out.println("Deleted:");
                    c.getRemoved().forEach(System.out::println);
                    System.out.println();
                }

                //update entry listener
                if (c.wasUpdated()) {
                    Entry entry = new Entry();
                    if (selectedRowID == 0) {
                        //add entry
                        entry.insertOrder(selectedTreeItem.getParent().getValue().concat("-") + selectedTreeItem.getValue(),
                                filteredList.subList(c.getFrom(), c.getTo()).get(0));

                        System.out.println("Inserted:");
                    } else {
                        //update entry
                        entry.updateOrder(filteredList.subList(c.getFrom(), c.getTo()).get(0), selectedRowID,
                                selectedTreeItem.getParent().getValue().concat("-") + selectedTreeItem.getValue());

                        System.out.println("Updated:");
                    }
                    filteredList.subList(c.getFrom(), c.getTo()).forEach(System.out::println);
                    System.out.println();

                    data.clear();
                    Query query = new Query();
                    for (String id : query.getColumnEntries(selectedTable, "ID")) {
                        data.add(query.getRowEntry(id, selectedTable));

                        if (query.getColumnEntries(selectedTable, "ID").size() == (query.getColumnEntries(selectedTable, "ID").indexOf(id) + 1)) {
                            if (query.getRowEntry(id, selectedTable).getOrderNo() != 0 && !query.getRowEntry(id, selectedTable).getItemName().equals("-")) {
                                //add empty row at the end of the list
                                data.add(new OrderEntry());
                            }
                        }
                    }

                    setTableCellFocus();
                }
            }

        });

        /*
          Focus listener for the selected item in the TableTreeView.
         */
        tableTreeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            Query query = new Query();
            if (newValue != null && newValue.getParent() != null) {
                if (query.isMainItem(newValue.getValue()) || !newValue.isLeaf()) {
                    System.out.println("Selected item: " + newValue.getValue());

                    data.clear();
                    itemDetailsTable.setPlaceholder(new Label("Please select an item to view order details."));
                } else {
                    System.out.println("Selected item type: " + newValue.getParent().getValue() + "-" + newValue.getValue());
                    selectedTable = newValue.getParent().getValue() + "-" + newValue.getValue();

                    data.clear();
                    for (String id : query.getColumnEntries(selectedTable, "ID")) {
                        data.add(query.getRowEntry(id, selectedTable));
                    }

                    //add empty row at the end of the list
                    data.add(new OrderEntry());
                }
                selectedTreeItem = newValue;
            }
        });

        /*
          Focus listener for the selected cell in the TableView.
         */
        itemDetailsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                Query query = new Query();

                selectedOrderEntry = newSelection;
                selectedItem = newSelection.getItemName();
                System.out.println("Selected item name: " + selectedItem);

                selectedRowID = query.getRowIdNumber(selectedTreeItem.getParent().getValue().concat("-") + selectedTreeItem.getValue(), selectedItem);
                System.out.println("Selected row ID: " + selectedRowID);

                selectedRowIndex = itemDetailsTable.getItems().indexOf(newSelection);
                System.out.println("currRowIndex: " + selectedRowIndex);
            }
        });

        /*
          Focus listener for the selected column in the TableView.
         */
        final ObservableList<TablePosition> selectedCells = itemDetailsTable.getSelectionModel().getSelectedCells();
        selectedCells.addListener(new ListChangeListener<TablePosition>() {
            @Override
            public void onChanged(Change change) {
                for (TablePosition pos : selectedCells) {
                    selectedColumn = pos.getTableColumn().getText();
                    System.out.println("Selected column: " + selectedColumn);

                    selectedColIndex = pos.getColumn();
                    System.out.println("currColIndex: " + selectedColIndex);
                }
            }
        });

        orderNoCol.setOnEditCommit((TableColumn.CellEditEvent<OrderEntry, Integer> i) -> {
            if (i.getNewValue().equals(i.getOldValue())) {
                setTableCellFocus();
            } else {
                int in = i.getNewValue();
                i.getTableView().getItems().get(i.getTablePosition().getRow()).setOrderNo(in);
            }
        });

        itemCol.setOnEditCommit((TableColumn.CellEditEvent<OrderEntry, String> t) -> {
            if (t.getNewValue().equals(t.getOldValue())) {
                setTableCellFocus();
            } else {
                String str = t.getNewValue();
                t.getTableView().getItems().get(t.getTablePosition().getRow()).setItemName(str);
            }
        });

        unitPriceCol.setOnEditCommit((TableColumn.CellEditEvent<OrderEntry, Double> d) -> {
            if (d.getNewValue().equals(d.getOldValue())) {
                setTableCellFocus();
            } else {
                double db = d.getNewValue();
                d.getTableView().getItems().get(d.getTablePosition().getRow()).setUnitPrice(db);
            }
        });

        qtyCol.setOnEditCommit((TableColumn.CellEditEvent<OrderEntry, Integer> i) -> {
            if (i.getNewValue().equals(i.getOldValue())) {
                setTableCellFocus();
            } else {
                int in = i.getNewValue();
                i.getTableView().getItems().get(i.getTablePosition().getRow()).setQuantity(in);
            }
        });

        totalAmtCol.setOnEditCommit((TableColumn.CellEditEvent<OrderEntry, Double> d) -> {
            if (d.getNewValue().equals(d.getOldValue())) {
                setTableCellFocus();
            } else {
                double db = d.getNewValue();
                d.getTableView().getItems().get(d.getTablePosition().getRow()).setTotalAmt(db);
            }
        });

        tableTreeView.setCellFactory(param -> {
            TreeCell<String> treeCell = new TreeCell<String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText("");
                        setGraphic(null);
                        return;
                    }

                    setText(item);
                }
            };

            treeCell.addEventFilter(MouseEvent.MOUSE_PRESSED, (MouseEvent e) -> {
                if (e.getClickCount() % 2 == 0 && e.getButton().equals(MouseButton.PRIMARY)) {
                    Query query = new Query();

                    if (query.isMainItem(selectedTreeItem.getValue()) || !selectedTreeItem.isLeaf()) { //to rename items
                        System.out.println("Selected Item: " + selectedTreeItem.getValue());
                        try {
                            RenameItemController.getInstance(selectedTreeItem.getValue(), true).showAndWait();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    } else { //to rename entries
                        System.out.println("Selected Item Entry: " + selectedTreeItem.getParent().getValue().concat("-")
                                + selectedTreeItem.getValue());
                        try {
                            RenameItemController.getInstance(selectedTreeItem.getParent().getValue().concat("-")
                                    + selectedTreeItem.getValue(), false).showAndWait();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            });
            return treeCell;
        });
    }

    public static AppController getInstance() throws IOException {
        if (instance == null) {
            FXMLLoader loader = new FXMLLoader(AppController.class.getResource("/application/app.fxml"));
            Parent root = loader.load();
            instance = loader.getController();

            stage = Main.getStage();
            stage.setScene(new Scene(root));
        }

        return instance;
    }

    public void show() {
        stage.show();
    }

    @FXML
    private void onConnect() {
        Connection.INSTANCE.createConnectionPool();

        //create root table if not exists
        Table table = new Table();
        table.createRootTable();

        //populate and update tree view contents
        updateTreeView();

        //update connection status label
        String connectedLabel = "Connected...";
        connStat.setText(connectedLabel);
        connStat.setStyle("-fx-text-fill: blue");

        //enable buttons on successful connect
        disableButtons(false);

        itemDetailsTable.setPlaceholder(new Label("Please select an item to view order details."));
    }

    @FXML
    private void onDisconnect() throws IOException {
        Connection.INSTANCE.disposeConnectionPool();
        connStat.setText(disconnectedLabel);
        connStat.setStyle("-fx-text-fill: red");

        //clear data on successful disconnect
        CreateTableController.getInstance().getItemNameTextField().clear();
        rootItem.getChildren().clear();

        //disable buttons on successful disconnect
        disableButtons(true);

        data.clear();
        itemDetailsTable.setPlaceholder(new Label(""));
    }

    @FXML
    private void onExit() {
        exit.getScene().getWindow().hide();
    }

    @FXML
    private void onTableCreate() throws IOException {
        CreateTableController.getInstance().showAndWait();
    }

    @FXML
    private void onDelete() {
        String selectedItem = selectedTreeItem.getValue();

        Entry entry = new Entry();
        Table table = new Table();
        Query query = new Query();

        Alert alert;
        if (query.isMainItem(selectedTreeItem.getValue()) || !selectedTreeItem.isLeaf()) { //to delete items
            alert = new Alert(AlertType.NONE, "Are you sure you want to delete item " + selectedItem + "?",
                    ButtonType.OK, ButtonType.CANCEL);
            alert.showAndWait();

            if (alert.getResult() == ButtonType.OK) {
                table.delete(selectedItem);
                entry.deleteItem("Items", selectedItem);

                //also delete the table's children
                for (TreeItem<String> child : selectedTreeItem.getChildren()) {
                    table.delete(child.getParent().getValue().concat("-") + child.getValue());
                }
            } else if (alert.getResult() == ButtonType.CANCEL) {
                return;
            }
        } else { //to delete entries
            alert = new Alert(AlertType.NONE, "Are you sure you want to delete entry " + selectedItem + "?",
                    ButtonType.OK, ButtonType.CANCEL);
            alert.showAndWait();

            if (alert.getResult() == ButtonType.OK) {
                table.delete(selectedTreeItem.getParent().getValue().concat("-") + selectedItem);
                entry.deleteItem(selectedTreeItem.getParent().getValue(), selectedTreeItem.getParent().getValue().concat("-") + selectedItem);
            } else if (alert.getResult() == ButtonType.CANCEL) {
                return;
            }
        }

        //update tree view after successful delete
        updateTreeView();
    }

    @FXML
    private void onEntryCreate() throws IOException {
        CreateEntryController.getInstance().showAndWait();
    }

    public void updateTreeView() {
        rootItem.getChildren().clear();
        tableTreeView.setRoot(rootItem);

        Query query = new Query();
        TreeItem<String> tables;
        for (String tableName : query.getColumnEntries("Items", "ENTRY")) {
            //add item to the root
            tables = new TreeItem<>(tableName);
            rootItem.getChildren().add(tables);

            //add children to the item
            for (String entry : query.getColumnEntries(tableName, "ENTRY")) {
                tables.getChildren().add(new TreeItem<>(entry.replace(tables.getValue().concat("-"), "")));
            }

            tables.setExpanded(true);
        }

        rootItem.setExpanded(true);
    }

    @FXML
    void onDeleteEntry() {
        Entry entry = new Entry();
        Query query = new Query();

        entry.deleteOrder(query.getRowIdNumber(selectedTable, selectedItem), selectedTable);

        data.clear();
        for (String id : query.getColumnEntries(selectedTable, "ID")) {
            data.add(query.getRowEntry(id, selectedTable));
        }

        //add empty row at the end of the list
        data.add(new OrderEntry());
    }

    public void setTreeViewFocus(String treeItem) {
        Query query = new Query();

        for (TreeItem<String> ti : rootItem.getChildren()) {
            if (ti.getValue().equals(treeItem)) {
                tableTreeView.getSelectionModel().select(ti);
                selectedTreeItem = ti;
                continue;
            }

            for (TreeItem<String> cti : ti.getChildren()) {
                if (query.isMainItem(selectedTreeItem.getValue()) || !selectedTreeItem.isLeaf()) {
                    if (selectedTreeItem.getValue().equals(cti.getParent().getValue()) && cti.getValue().equals(treeItem)) {
                        tableTreeView.getSelectionModel().select(cti);
                        selectedTreeItem = cti;
                    }
                } else {
                    if (selectedTreeItem.getParent().getValue().equals(cti.getParent().getValue()) && cti.getValue().equals(treeItem)) {
                        tableTreeView.getSelectionModel().select(cti);
                        selectedTreeItem = cti;
                    }
                }
            }
        }
    }

    public void setTableCellFocus() {
        //Set focus on next cell or the next row's first cell in the case of last cell edit.
        if (selectedRowID == 0 || selectedOrderEntry.getOrderNo() == 0 || selectedOrderEntry.getItemName().equals("-")) {
            System.out.println(itemDetailsTable.getColumns().size());
            if ((selectedColIndex + 1) < itemDetailsTable.getColumns().size()) {
                itemDetailsTable.getSelectionModel().select(selectedRowIndex, itemDetailsTable.getColumns().get(selectedColIndex + 1));
            } else {
                itemDetailsTable.getSelectionModel().select(selectedRowIndex, itemDetailsTable.getColumns().get(0));
            }
        } else if (itemDetailsTable.getColumns().size() == selectedColIndex + 1) {
            itemDetailsTable.getSelectionModel().select(selectedRowIndex + 1, itemDetailsTable.getColumns().get(0));
        } else {
            itemDetailsTable.getSelectionModel().select(selectedRowIndex, itemDetailsTable.getColumns().get(selectedColIndex + 1));
        }
    }

    public void disableButtons(boolean bool) {
        connect.setDisable(!bool);
        disconnect.setDisable(bool);
        tableCreate.setDisable(bool);
        tableDelete.setDisable(bool);
        entryInsert.setDisable(bool);
    }

    public TreeItem<String> getSelectedTreeItem() {
        return selectedTreeItem;
    }

    public TreeView<String> getTableTreeView() {
        return tableTreeView;
    }
}

