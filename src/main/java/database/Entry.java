package database;

import orders.OrderEntry;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Entry {

    public void insertItem(String table, String record) {
        PreparedStatement prep = null;
        java.sql.Connection conn = null;

        try {
            //open a connection
            conn = Connection.INSTANCE.getJdbcConnectionPool().getConnection();

            //execute a query
            String sql = "INSERT INTO \"" + table + "\" (ENTRY)  values (?)";
            prep = conn.prepareStatement(sql);
            prep.setString(1, record);
            prep.executeUpdate();

            System.out.println("Successfully inserted entry: \"" + record + "\"" + " in table: " + "\"" + table + "\".");

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
    }

    public void updateItem(String table, String oldValue, String newValue) {

        PreparedStatement prep = null;
        java.sql.Connection conn = null;

        try {
            //open a connection
            conn = Connection.INSTANCE.getJdbcConnectionPool().getConnection();

            //execute a query
            //UPDATE table_name SET column_name = 'new_value' where column_name = 'old_value';
            String sql = "UPDATE \"" + table + "\" SET ENTRY = ? WHERE ENTRY = ?";
            prep = conn.prepareStatement(sql);
            prep.setString(1, newValue);
            prep.setString(2, oldValue);
            prep.executeUpdate();

            System.out.println("Successfully updated from \"" + oldValue + "\" to \"" + newValue + "\" in table " + "\"" + table + "\".");

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
    }

    public void insertOrder(String table, OrderEntry orderEntry) {
        PreparedStatement prep = null;
        java.sql.Connection conn = null;

        try {
            //open a connection
            conn = Connection.INSTANCE.getJdbcConnectionPool().getConnection();

            //execute a query
            String sql = "INSERT INTO \"" + table + "\" (\"Order No.\", \"Item Name\", \"Unit Price\", \"Quantity\", \"Total Amount\")  values (?, ?, ?, ?, ?)";
            prep = conn.prepareStatement(sql);
            prep.setInt(1, orderEntry.getOrderNo());
            prep.setString(2, orderEntry.getItemName());
            prep.setDouble(3, orderEntry.getUnitPrice());
            prep.setInt(4, orderEntry.getQuantity());
            prep.setDouble(5, orderEntry.getTotalAmt());
            prep.executeUpdate();

            System.out.println("Successfully inserted entry: \"" + orderEntry.getItemName() + "\"" + " in table: " + "\"" + table + "\".");

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
    }

    public void updateOrder(OrderEntry orderEntry, int id, String table) {
        String orderNoCol = "Order No.";
        String itemNameCol = "Item Name";
        String unitPriceCol = "Unit Price";
        String qtyCol = "Quantity";
        String totalAmtCol = "Total Amount";

        PreparedStatement prep = null;
        java.sql.Connection conn = null;

        try {
            //open a connection
            conn = Connection.INSTANCE.getJdbcConnectionPool().getConnection();

            //execute a query
            String sql = "UPDATE \"" + table + "\" SET \"" + orderNoCol + "\"= ?"
                    + ", \"" + itemNameCol + "\"= ?"
                    + ", \"" + unitPriceCol + "\"= ?"
                    + ",\"" + qtyCol + "\"= ?"
                    + ",\"" + totalAmtCol + "\"= ?"
                    + "WHERE ID= ?";
            prep = conn.prepareStatement(sql);
            prep.setInt(1, orderEntry.getOrderNo());
            prep.setString(2, orderEntry.getItemName());
            prep.setDouble(3, orderEntry.getUnitPrice());
            prep.setInt(4, orderEntry.getQuantity());
            prep.setDouble(5, orderEntry.getTotalAmt());
            prep.setInt(6, id);
            prep.executeUpdate();

            System.out.println("Successfully updated item: \"" + orderEntry.getItemName() + "\"" + " in table: " + "\"" + table + "\".");

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
    }

    public void deleteItem(String table, String record) {
        PreparedStatement prep = null;
        java.sql.Connection conn = null;

        try {
            //open a connection
            conn = Connection.INSTANCE.getJdbcConnectionPool().getConnection();

            //execute a query
            String sql = "DELETE FROM \"" + table + "\" WHERE ENTRY=?";
            prep = conn.prepareStatement(sql);
            prep.setString(1, record);
            prep.executeUpdate();

            System.out.println("Successfully deleted \"" + record + "\"" + " from " + "\"" + table + "\".");

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
    }

    public void deleteOrder(int id, String table) {
        PreparedStatement prep = null;
        java.sql.Connection conn = null;

        try {
            //open a connection
            conn = Connection.INSTANCE.getJdbcConnectionPool().getConnection();

            //execute a query
            String sql = "DELETE FROM \"" + table + "\" WHERE ID=?";
            prep = conn.prepareStatement(sql);
            prep.setInt(1, id);
            prep.executeUpdate();

            System.out.println("Successfully deleted ID \"" + id + "\"" + " from " + "\"" + table + "\".");

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
    }
}
