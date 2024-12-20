package com.alexey.shortcuts_helper;

import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.table.JBTable;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ShortcutsDialog extends DialogWrapper {

    private JTable table;
    private DefaultTableModel model;
    private JTextField searchField;

    private final Object[][] hotkeysData = {
            // Editing Shortcuts
            {"Copy", "Ctrl + C"},
            {"Paste", "Ctrl + V"},
            {"Cut", "Ctrl + X"},
            {"Undo", "Ctrl + Z"},
            {"Redo", "Ctrl + Shift + Z"},
            {"Duplicate Line", "Ctrl + D"},
            {"Delete Line", "Ctrl + Y"},
            {"Select All", "Ctrl + A"},
            {"Find", "Ctrl + F"},
            {"Find in Files", "Ctrl + Shift + F"},
            {"Replace", "Ctrl + R"},
            {"Replace in Files", "Ctrl + Shift + R"},

            // Navigation Shortcuts
            {"Go to Class", "Ctrl + N"},
            {"Go to File", "Ctrl + Shift + N"},
            {"Go to Symbol", "Ctrl + Alt + Shift + N"},
            {"Recent Files", "Ctrl + E"},
            {"Navigate Back", "Ctrl + Alt + Left"},
            {"Navigate Forward", "Ctrl + Alt + Right"},
            {"Search Everywhere", "Double Shift"},
            {"Show Navigation Bar", "Alt + Home"},

            // Code Editing Shortcuts
            {"Reformat Code", "Ctrl + Alt + L"},
            {"Optimize Imports", "Ctrl + Alt + O"},
            {"Comment/Uncomment Line", "Ctrl + /"},
            {"Comment/Uncomment Block", "Ctrl + Shift + /"},
            {"Generate Code", "Alt + Insert"},
            {"Auto-Complete", "Ctrl + Space"},
            {"Smart Auto-Complete", "Ctrl + Shift + Space"},
            {"Quick Fix", "Alt + Enter"},
            {"Surround with...", "Ctrl + Alt + T"},

            // Run and Debug Shortcuts
            {"Run", "Shift + F10"},
            {"Debug", "Shift + F9"},
            {"Step Over", "F8"},
            {"Step Into", "F7"},
            {"Step Out", "Shift + F8"},
            {"Resume Program", "F9"},
            {"Stop", "Ctrl + F2"},

            // Refactoring Shortcuts
            {"Rename", "Shift + F6"},
            {"Extract Method", "Ctrl + Alt + M"},
            {"Extract Variable", "Ctrl + Alt + V"},
            {"Extract Field", "Ctrl + Alt + F"},
            {"Inline", "Ctrl + Alt + N"},

            // Tool Windows
            {"Project View", "Alt + 1"},
            {"Terminal", "Alt + F12"},
            {"Run Window", "Alt + 4"},
            {"Debug Window", "Alt + 5"},
            {"Version Control", "Alt + 9"},

            // Miscellaneous
            {"Toggle Full Screen", "Ctrl + Shift + F11"},
            {"Toggle Distraction Free Mode", "Ctrl + Alt + Shift + F12"},
            {"Quick Documentation", "Ctrl + Q"},
            {"Open Settings", "Ctrl + Alt + S"},
            {"Open Keymap Settings", "Ctrl + Alt + Shift + S"}
    };

    private static final boolean IS_MAC = System.getProperty("os.name").toLowerCase().contains("mac");

    private void adjustShortcutsForMac() {
        for (int i = 0; i < hotkeysData.length; i++) {
            hotkeysData[i][1] = hotkeysData[i][1].toString().replace("Ctrl", "⌘");
        }
    }

    public ShortcutsDialog() {
        super(true); // use current window as parent
        init();
        setTitle("Shortcuts Helper");
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        // Create the search field
        searchField = new JTextField();
        searchField.setToolTipText("Search for a shortcut...");
        searchField.setPreferredSize(new Dimension(400, 30));
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        addPlaceholder(searchField);
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filterTable();
            }
        });

        // Add filters to panel
        filterPanel.add(searchField);

        // Table
        String[] columns = {"Action", "Shortcut"};
        if (IS_MAC) {
            adjustShortcutsForMac();
        }
        model = new DefaultTableModel(hotkeysData, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make the table read-only
            }
        };

        table = new JBTable(model);

        // Customize the appearance of the "Shortcut" column
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        renderer.setFont(new Font("Monospaced", Font.BOLD, 12));
        renderer.setForeground(JBColor.YELLOW); // Highlight shortcuts in blue
        table.getColumnModel().getColumn(1).setCellRenderer(renderer);

        table.setRowHeight(25);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JBScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(400, 250));

        // Add the search field and the table to the panel
        mainPanel.add(searchField, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        return mainPanel;
    }

    private void addPlaceholder(JTextField textField) {
        Color defaultTextColor = textField.getForeground();

        textField.setForeground(JBColor.GRAY);
        textField.setText("Search for a shortcut...");

        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textField.getText().equals("Search for a shortcut...")) {
                    textField.setText("");
                    textField.setForeground(defaultTextColor);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setForeground(JBColor.GRAY);
                    textField.setText("Search for a shortcut...");
                }
            }
        });
    }

    private void filterTable() {
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        String searchQuery = searchField.getText().trim();

        RowFilter<DefaultTableModel, Object> searchFilter = searchQuery.isEmpty()
                ? null
                : RowFilter.regexFilter("(?i)" + searchQuery);

        if (searchFilter != null) {
            sorter.setRowFilter(searchFilter);
        }
    }

}
