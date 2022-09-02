/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.merlin;

import java.awt.Color;
import java.awt.Component;
import java.awt.Insets;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Lucky
 */
public class TextAreaCellRenderer extends JTextArea implements TableCellRenderer {
  private final List<List<Integer>> rowAndCellHeights = new ArrayList<>();

  // public static class UIResource extends TextAreaCellRenderer implements UIResource {}

  @Override public void updateUI() {
    super.updateUI();
    setLineWrap(true);
    setWrapStyleWord(true);
    setBorder(BorderFactory.createEmptyBorder(2, 10, 2, 10));
    // setBorder(BorderFactory.createLineBorder(Color.RED, 2));
//     setMargin(new Insets(2, 50, 2, 20));
    // setBorder(BorderFactory.createEmptyBorder());
    
                
    setName("Table.cellRenderer");
  }

  @Override public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    setFont(table.getFont());
    setText(Objects.toString(value, ""));
//    Color alternate = UIManager.getColor("Table.alternateRowColor");
    Color alternate = new Color(239,246,250);
    if (row % 2 == 1) {
        setBackground(alternate);
    } else {
        setBackground(Color.WHITE);
    }
    if (isSelected) {
        setBackground(Color.DARK_GRAY);
        setForeground(Color.WHITE);
    } else {
        setForeground(Color.black);
        if (row % 2 == 1) {
            setBackground(alternate);
        } else {
            setBackground(Color.WHITE);
        }
    }
    adjustRowHeight(table, row, column);
    return this;
  }

  /**
   * Calculate the new preferred height for a given row, and sets the height on the table.
   * http://blog.botunge.dk/post/2009/10/09/JTable-multiline-cell-renderer.aspx
   */
  private void adjustRowHeight(JTable table, int row, int column) {
    // The trick to get this to work properly is to set the width of the column to the
    // textarea. The reason for this is that getPreferredSize(), without a width tries
    // to place all the text in one line. By setting the size with the with of the column,
    // getPreferredSize() returnes the proper height which the row should have in
    // order to make room for the text.
    // int cWidth = table.getTableHeader().getColumnModel().getColumn(column).getWidth();
    // int cWidth = table.getCellRect(row, column, false).width; // Ignore IntercellSpacing
    // setSize(new Dimension(cWidth, 1000));

    setBounds(table.getCellRect(row, column, false));
    // doLayout();

    int preferredHeight = getPreferredSize().height;
    while (rowAndCellHeights.size() <= row) {
      rowAndCellHeights.add(new ArrayList<>(column));
    }
    List<Integer> list = rowAndCellHeights.get(row);
    while (list.size() <= column) {
      list.add(0);
    }
    list.set(column, preferredHeight);
    int max = list.stream().max(Integer::compare).get();
    if (table.getRowHeight(row) != max) {
      table.setRowHeight(row, max);
    }
  }

  // Overridden for performance reasons. ---->
  @Override public boolean isOpaque() {
    Color back = getBackground();
    Object o = SwingUtilities.getAncestorOfClass(JTable.class, this);
    if (o instanceof JTable) {
      JTable table = (JTable) o;
      boolean colorMatch = Objects.nonNull(back) && back.equals(table.getBackground()) && table.isOpaque();
      return !colorMatch && super.isOpaque();
    } else {
      return super.isOpaque();
    }
  }

  @Override protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
    // String literal pool
    // if (propertyName == "document" || ((propertyName == "font" || propertyName == "foreground") && oldValue != newValue)) {
    if ("document".equals(propertyName)) {
      super.firePropertyChange(propertyName, oldValue, newValue);
    } else if (("font".equals(propertyName) || "foreground".equals(propertyName)) && !Objects.equals(oldValue, newValue)) {
      super.firePropertyChange(propertyName, oldValue, newValue);
    }
  }

  @Override public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) { /* Overridden for performance reasons. */ }

  @Override public void repaint(long tm, int x, int y, int width, int height) { /* Overridden for performance reasons. */ }

  @Override public void repaint(Rectangle r) { /* Overridden for performance reasons. */ }

  @Override public void repaint() { /* Overridden for performance reasons. */ }

  @Override public void invalidate() { /* Overridden for performance reasons. */ }

  @Override public void validate() { /* Overridden for performance reasons. */ }

  @Override public void revalidate() { /* Overridden for performance reasons. */ }
  // <---- Overridden for performance reasons.

}
