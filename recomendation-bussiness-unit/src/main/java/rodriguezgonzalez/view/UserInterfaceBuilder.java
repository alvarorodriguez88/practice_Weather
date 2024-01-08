package rodriguezgonzalez.view;

import rodriguezgonzalez.control.SQLQueryManager;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class UserInterfaceBuilder extends JFrame implements ActionListener {
    private JLabel checkInLabel, checkOutLabel, climateConditionLabel;
    private JComboBox<String> checkInBox, checkOutBox, climateConditionComboBox;
    private JButton sendButton;
    private static Connection conn;
    private JTable resultTable;

    public UserInterfaceBuilder(Connection conn) {
        UserInterfaceBuilder.conn = conn;
        init();
    }

    public void init() {
        initialJFrame();
        UserInterfaceBuilder.entrancePanel entrancePanel = getEntrancePanel();
        interfaceComponents();
        addComponents(entrancePanel);
        addPopulateDateComboBox();
        JScrollPane scrollPane = getjScrollPane();
        addToMainPanel(entrancePanel, scrollPane);
    }

    private void addToMainPanel(entrancePanel entrancePanel, JScrollPane scrollPane) {
        entrancePanel.mainPanel().add(entrancePanel.inputPanel(), BorderLayout.NORTH);
        entrancePanel.mainPanel().add(scrollPane, BorderLayout.CENTER);
        add(entrancePanel.mainPanel());
        setVisible(true);
    }

    private JScrollPane getjScrollPane() {
        resultTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(resultTable);
        return scrollPane;
    }

    private void addPopulateDateComboBox() {
        populateDateComboBox(checkInBox);
        populateDateComboBox(checkOutBox);
    }

    private void addComponents(entrancePanel entrancePanel) {
        entrancePanel.inputPanel().add(checkInLabel);
        entrancePanel.inputPanel().add(checkInBox);
        entrancePanel.inputPanel().add(checkOutLabel);
        entrancePanel.inputPanel().add(checkOutBox);
        entrancePanel.inputPanel().add(climateConditionLabel);
        entrancePanel.inputPanel().add(climateConditionComboBox);
        entrancePanel.inputPanel().add(sendButton);
    }

    private void interfaceComponents() {
        checkInLabel = new JLabel("Check-In: ");
        checkInBox = createComboBox();
        checkOutLabel = new JLabel("Check-Out: ");
        checkOutBox = createComboBox();
        climateConditionLabel = new JLabel("Condición Climática: ");
        String[] climateOptions = {" ", "Clouds", "Rain", "Clear", "Snow"};
        climateConditionComboBox = new JComboBox<>(climateOptions);
        sendButton = new JButton("Consultar");
        sendButton.addActionListener(this);
    }

    private static entrancePanel getEntrancePanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        return new entrancePanel(mainPanel, inputPanel);
    }

    private record entrancePanel(JPanel mainPanel, JPanel inputPanel) {
    }

    private void initialJFrame() {
        setTitle("CONSULT LODGINGS");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
    }

    private JComboBox<String> createComboBox() {
        return new JComboBox<>();
    }

    private void populateDateComboBox(JComboBox<String> comboBox) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate currentDate = LocalDate.now().plusDays(1);
        for (int i = 0; i < 6; i++) {
            comboBox.addItem(currentDate.format(formatter));
            currentDate = currentDate.plusDays(1);
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == sendButton) {
            String checkIn = checkInBox.getSelectedItem().toString();
            String checkOut = checkOutBox.getSelectedItem().toString();
            String climateCondition = climateConditionComboBox.getSelectedItem().toString();
            SQLQueryManager queryManager = null;
            try {
                queryManager = new SQLQueryManager(conn);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            ArrayList<String> selectedHotels = queryManager.getUbicationsByClimate(climateCondition);
            ArrayList<String> stayInfo = queryManager.getHotelsInformation(selectedHotels, checkIn, checkOut);
            DefaultTableModel tableModel = convertToTableModel(stayInfo);
            tableModelestablishment(tableModel);
            establishColumns();
            resultTable.repaint();
        }
    }

    private void establishColumns() {
        TableColumnModel columnModel = resultTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(30);
        columnModel.getColumn(1).setPreferredWidth(400);
        columnModel.getColumn(2).setPreferredWidth(100);
        columnModel.getColumn(3).setPreferredWidth(30);
    }

    private void tableModelestablishment(DefaultTableModel tableModel) {
        resultTable.setModel(tableModel);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        resultTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        resultTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        resultTable.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
    }

    private static DefaultTableModel convertToTableModel(ArrayList<String> stayInfo) {
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("Ubication");
        tableModel.addColumn("Hotel Name");
        tableModel.addColumn("Website");
        tableModel.addColumn("Total price");
        for (String info : stayInfo) {
            String[] rowData = info.split(" -> ");
            tableModel.addRow(rowData);
        }
        return tableModel;
    }
}
