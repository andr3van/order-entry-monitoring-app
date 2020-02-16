package database;

import orders.OrderEntry;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Query {

    public int getRowIdNumber(String tableName, String itemName) {
        PreparedStatement prep = null;
        java.sql.Connection conn = null;
        int rowID = 0;

        String table = String.format("\"%s\"", tableName);
        String item = String.format("'%s'", itemName);

        try {
            //open a connection
            conn = Connection.INSTANCE.getJdbcConnectionPool().getConnection();

            //execute a query
            String sql = String.format("SELECT ID FROM %s WHERE%s =%s", table, " \"Item Name\"", item);
            prep = conn.prepareStatement(sql);
            ResultSet rs = prep.executeQuery();

            //extract data from result set
            while (rs.next()) {
                //retrieve by table name
                rowID = rs.getInt("ID");
            }

            //clean-up environment
            prep.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (prep != null) prep.close();
            } catch (SQLException ignored) {
            } // nothing we can do
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            } //end finally try
        } //end try

        return rowID;
    }

    public ArrayList<String> showAllTables() {
        java.sql.Connection conn = null;
        Statement stmt = null;
        ArrayList<String> tableList = null;

        try {
            //open a connection
            conn = Connection.INSTANCE.getJdbcConnectionPool().getConnection();

            //execute a query
            String sql = "SHOW TABLES";
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            //extract data from result set
            tableList = new ArrayList<>();
            while (rs.next()) {
                //retrieve by table name
                String data = rs.getString("TABLE_NAME");
                tableList.add(data);
            }

            //clean-up environment
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException ignored) {
            } // nothing we can do
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            } //end finally try
        } //end try

        return tableList;
    }

    public ArrayList<String> getColumnEntries(String table, String column) {
        java.sql.Connection conn = null;
        Statement stmt = null;
        ArrayList<String> entryList = null;

        try {
            //open a connection
            conn = Connection.INSTANCE.getJdbcConnectionPool().getConnection();

            //execute a query
            String sql = "SELECT " + column + " FROM \"" + table + "\" ORDER BY " + column;
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            //extract data from result set
            entryList = new ArrayList<>();
            while (rs.next()) {
                //retrieve by column name
                String rec = rs.getString(column);
                entryList.add(rec);
            }

            //clean-up environment
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException ignored) {
            } // nothing we can do
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            } //end finally try
        } //end try

        return entryList;
    }

    public OrderEntry getRowEntry(String id, String table) {
        String orderNoCol = "Order No.";
        String itemNameCol = "Item Name";
        String unitPriceCol = "Unit Price";
        String qtyCol = "Quantity";
        String totalAmtCol = "Total Amount";

        java.sql.Connection conn = null;
        Statement stmt = null;
        OrderEntry orderEntry = null;

        try {
            //open a connection
            conn = Connection.INSTANCE.getJdbcConnectionPool().getConnection();

            //execute a query
            //SELECT "Item Name" FROM "A-1" WHERE ID=1
            String sql = "SELECT \"" + orderNoCol + "\", \"" + itemNameCol + "\", \"" + unitPriceCol + "\", \"" + qtyCol + "\", \"" + totalAmtCol
                    + "\" FROM \"" + table + "\" WHERE ID=" + id;
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            //extract data from result set
            while (rs.next()) {
                //retrieve data by row ID
                int orderNo = rs.getInt(orderNoCol);
                String itemName = rs.getString(itemNameCol);
                double unitPrice = rs.getDouble(unitPriceCol);
                int quantity = rs.getInt(qtyCol);
                double totalAmount = rs.getDouble(totalAmtCol);

                orderEntry = new OrderEntry(orderNo, itemName, unitPrice, quantity, totalAmount);
            }

            //clean-up environment
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException ignored) {
            } // nothing we can do
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            } //end finally try
        } //end try

        return orderEntry;
    }

    public boolean isDuplicateEntry(String table, String column, String entry) {
        return !getColumnEntries(table, column).contains(entry);
    }

    public boolean isMainItem(String item) {
        return getColumnEntries("Items", "ENTRY").contains(item);
    }
}
