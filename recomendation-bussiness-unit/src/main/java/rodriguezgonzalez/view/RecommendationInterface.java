package rodriguezgonzalez.view;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class RecommendationInterface extends JFrame implements ActionListener {
    private JLabel checkInLabel, checkOutLabel, climateConditionLabel;
    private JComboBox<String> checkInBox, checkOutBox,  climateConditionComboBox;
    private JButton enviarButton;
    private static Connection conn;
    private JTable resultTable;

    public RecommendationInterface(Connection conn) {
        this.conn = conn;

        setTitle("CONSULT LODGINGS");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600); // Ajusta el tamaño inicial del JFrame
        setLocationRelativeTo(null);

        // Panel principal
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // Panel de entrada
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Componentes de la interfaz
        checkInLabel = new JLabel("Check-In: ");
        checkInBox = createComboBox();
        checkOutLabel = new JLabel("Check-Out: ");
        checkOutBox = createComboBox();
        climateConditionLabel = new JLabel("Condición Climática: ");
        String[] climateOptions = {" ", "Clouds", "Rain", "Clear", "Snow"};
        climateConditionComboBox = new JComboBox<>(climateOptions);
        enviarButton = new JButton("Consultar");
        enviarButton.addActionListener(this);

        // Añadir componentes al panel de entrada
        inputPanel.add(checkInLabel);
        inputPanel.add(checkInBox);
        inputPanel.add(checkOutLabel);
        inputPanel.add(checkOutBox);
        inputPanel.add(climateConditionLabel);
        inputPanel.add(climateConditionComboBox);
        inputPanel.add(enviarButton);

        // Añadir opciones de fechas a los ComboBox
        populateDateComboBox(checkInBox);
        populateDateComboBox(checkOutBox);

        // Panel de salida con tabla
        resultTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(resultTable);

        // Añadir panel de entrada y salida al panel principal
        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Añadir panel principal a la ventana
        add(mainPanel);
        setVisible(true);
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
        if (e.getSource() == enviarButton) {
            String checkIn = checkInBox.getSelectedItem().toString();
            String checkOut = checkOutBox.getSelectedItem().toString();
            String climateCondition = climateConditionComboBox.getSelectedItem().toString();

            // Llamada a métodos para realizar consultas SQL y obtener resultados
            SQLQueryManager queryManager = null;
            try {
                queryManager = new SQLQueryManager(conn);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            ArrayList<String> selectedHotels = queryManager.getUbicationsByClimate(climateCondition);
            ArrayList<String> stayInfo = queryManager.getHotelsInformation(selectedHotels, checkIn, checkOut);

            // Convertir los datos a un modelo de tabla
            DefaultTableModel tableModel = new DefaultTableModel();
            tableModel.addColumn("Ubication");
            tableModel.addColumn("Hotel Name");
            tableModel.addColumn("Website");
            tableModel.addColumn("Total price");

            for (String info : stayInfo) {
                String[] rowData = info.split(" -> ");
                tableModel.addRow(rowData);
            }

            // Establecer el modelo de tabla en la JTable
            resultTable.setModel(tableModel);

            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
            resultTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
            resultTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);

            DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
            rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
            resultTable.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);

            // Establecer tamaños fijos para cada columna
            TableColumnModel columnModel = resultTable.getColumnModel();
            columnModel.getColumn(0).setPreferredWidth(30); // Ancho fijo para la primera columna
            columnModel.getColumn(1).setPreferredWidth(400); // Ancho fijo para la segunda columna
            columnModel.getColumn(2).setPreferredWidth(100); // Ancho fijo para la tercera columna
            columnModel.getColumn(3).setPreferredWidth(30); // Ancho fijo para la cuarta columna

            // Repintar la tabla para aplicar los cambios
            resultTable.repaint();
        }
    }
}
