package mpis_mariadb_2;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

public class TableHelper {

    private String query = "";

    public JTable autoResizeColWidth(JTable table, DefaultTableModel model) {
        table.setAutoResizeMode(0);
        table.setModel(model);
        int margin = 5;
        for (int i = 0; i < table.getColumnCount(); i++) {
            int vColIndex = i;
            DefaultTableColumnModel colModel = (DefaultTableColumnModel) table.getColumnModel();
            TableColumn col = colModel.getColumn(vColIndex);
            int width = 0;
            TableCellRenderer renderer = col.getHeaderRenderer();
            if (renderer == null) {
                renderer = table.getTableHeader().getDefaultRenderer();
            }
            Component comp = renderer.getTableCellRendererComponent(table, col.getHeaderValue(), false, false, 0, 0);
            width = (comp.getPreferredSize()).width;
            for (int r = 0; r < table.getRowCount(); r++) {
                renderer = table.getCellRenderer(r, vColIndex);
                comp = renderer.getTableCellRendererComponent(table, table.getValueAt(r, vColIndex), false, false, r, vColIndex);
                width = Math.max(width, (comp.getPreferredSize()).width);
            }
            width += 2 * margin;
            col.setPreferredWidth(width);
        }
        ((DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(2);
        table.getTableHeader().setReorderingAllowed(false);
        return table;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getQuery() {
        return this.query;
    }
}
