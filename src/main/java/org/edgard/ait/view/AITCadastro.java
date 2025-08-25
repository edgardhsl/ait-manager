package org.edgard.ait.view;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

import org.edgard.ait.pojo.Ait;
import org.edgard.ait.pojo.Infracao;
import org.edgard.ait.pojo.Marca;
import org.edgard.ait.data.InfracaoRepository;
import org.edgard.ait.data.MarcaRepository;

public class AITCadastro {
    private static List<Ait> aitList = new ArrayList<>();
    private static int nextId = 1;

    private static final InfracaoRepository infracaoRepository = new InfracaoRepository();
    private static final MarcaRepository marcaRepository = new MarcaRepository();

    public static void criarTelaListagem() {
        JFrame frame = new JFrame("Sistema de AIT - Listagem");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 600);
        frame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());

        JLabel titulo = new JLabel("Auto de Infração de Trânsito - AIT", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        titulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnCadastrar = new JButton("Cadastrar");
        JButton btnEditar = new JButton("Editar");
        JButton btnRemover = new JButton("Remover");

        buttonPanel.add(btnCadastrar);
        buttonPanel.add(btnEditar);
        buttonPanel.add(btnRemover);

        String[] colunas = {"ID", "Data Infração", "Placa", "Infração", "Valor", "Marca", "Proprietário", "Condutor"};
        AitTableModel tableModel = new AitTableModel(aitList, colunas);
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        mainPanel.add(titulo, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        btnCadastrar.addActionListener(e -> {
            criarTelaCadastro(null);
            frame.dispose();
        });

        btnEditar.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                Ait ait = aitList.get(selectedRow);
                criarTelaCadastro(ait);
                frame.dispose();
            } else {
                JOptionPane.showMessageDialog(frame, "Selecione um AIT para editar.");
            }
        });

        btnRemover.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int confirm = JOptionPane.showConfirmDialog(frame,
                        "Tem certeza que deseja remover este AIT?",
                        "Confirmar Remoção",
                        JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    aitList.remove(selectedRow);
                    tableModel.fireTableDataChanged();
                    JOptionPane.showMessageDialog(frame, "AIT removido com sucesso!");
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Selecione um AIT para remover.");
            }
        });

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private static void criarTelaCadastro(Ait aitExistente) {
        JFrame frame = new JFrame(aitExistente == null ? "Cadastrar AIT" : "Editar AIT");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(650, 500);
        frame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblId = new JLabel("ID:");
        JTextField txtId = new JTextField();
        txtId.setEditable(false);

        JLabel lblData = new JLabel("Data da Infração:");
        JFormattedTextField txtData;
        try {
            MaskFormatter dataMask = new MaskFormatter("##/##/####");
            dataMask.setPlaceholderCharacter('_');
            txtData = new JFormattedTextField(dataMask);
            txtData.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        } catch (Exception e) {
            txtData = new JFormattedTextField();
        }

        JLabel lblEndereco = new JLabel("Endereço da Infração:");
        JTextField txtEndereco = new JTextField();

        JLabel lblInfracao = new JLabel("Infração:");
        List<Infracao> infracoes = infracaoRepository.findAll();
        JComboBox<Infracao> cmbInfracao = new JComboBox<>(infracoes.toArray(new Infracao[0]));
        cmbInfracao.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Infracao) {
                    setText(((Infracao) value).getDescricao());
                }
                return this;
            }
        });

        JLabel lblValor = new JLabel("Valor:");
        JLabel lblValorDisplay = new JLabel("R$ 0,00");
        lblValorDisplay.setForeground(Color.RED);
        lblValorDisplay.setFont(new Font("Arial", Font.BOLD, 12));

        JLabel lblPlaca = new JLabel("Placa:");
        JTextField txtPlaca = new JTextField();
        txtPlaca.setColumns(8);
        ((AbstractDocument) txtPlaca.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                replace(fb, offset, 0, string, attr);
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text == null) return;
                StringBuilder sb = new StringBuilder(fb.getDocument().getText(0, fb.getDocument().getLength()));
                sb.replace(offset, offset + length, text);
                String value = sb.toString().toUpperCase();
                if (value.length() > 7) return;
                if (isValidPlaca(value)) {
                    super.replace(fb, offset, length, text.toUpperCase(), attrs);
                }
            }

            @Override
            public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
                super.remove(fb, offset, length);
            }

            private boolean isValidPlaca(String value) {
                for (int i = 0; i < value.length(); i++) {
                    char c = value.charAt(i);
                    if (i < 3) {
                        if (!Character.isLetter(c)) return false;
                    } else if (i == 3) {
                        if (!Character.isLetterOrDigit(c)) return false;
                    } else if (i == 4) {
                        if (!Character.isLetterOrDigit(c)) return false;
                    } else {
                        if (!Character.isDigit(c)) return false;
                    }
                }
                return true;
            }
        });

        JLabel lblMarca = new JLabel("Marca do Veículo:");
        List<Marca> marcas = marcaRepository.findAll();
        JComboBox<Marca> cmbMarca = new JComboBox<>(marcas.toArray(new Marca[0]));
        cmbMarca.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Marca) {
                    setText(((Marca) value).getNome());
                }
                return this;
            }
        });

        JLabel lblAnoFab = new JLabel("Ano de Fabricação:");
        JTextField txtAnoFab = new JTextField();

        JLabel lblAnoMod = new JLabel("Ano do Modelo:");
        JTextField txtAnoMod = new JTextField();

        JLabel lblProprietario = new JLabel("Proprietário:");
        JTextField txtProprietario = new JTextField();

        JLabel lblCondutor = new JLabel("Condutor:");
        JTextField txtCondutor = new JTextField();

        JLabel lblCnh = new JLabel("CNH do Condutor:");
        JFormattedTextField txtCnh;
        try {
            MaskFormatter cnhMask = new MaskFormatter("###########");
            cnhMask.setPlaceholderCharacter('_');
            txtCnh = new JFormattedTextField(cnhMask);
        } catch (Exception e) {
            txtCnh = new JFormattedTextField();
        }

        int row = 0;
        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(lblId, gbc);
        gbc.gridx = 1;
        gbc.gridy = row++;
        formPanel.add(txtId, gbc);

        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(lblData, gbc);
        gbc.gridx = 1;
        gbc.gridy = row++;
        formPanel.add(txtData, gbc);

        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(lblEndereco, gbc);
        gbc.gridx = 1;
        gbc.gridy = row++;
        formPanel.add(txtEndereco, gbc);

        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(lblInfracao, gbc);
        gbc.gridx = 1;
        gbc.gridy = row++;
        formPanel.add(cmbInfracao, gbc);

        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(lblValor, gbc);
        gbc.gridx = 1;
        gbc.gridy = row++;
        formPanel.add(lblValorDisplay, gbc);

        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(lblPlaca, gbc);
        gbc.gridx = 1;
        gbc.gridy = row++;
        formPanel.add(txtPlaca, gbc);

        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(lblMarca, gbc);
        gbc.gridx = 1;
        gbc.gridy = row++;
        formPanel.add(cmbMarca, gbc);

        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(lblAnoFab, gbc);
        gbc.gridx = 1;
        gbc.gridy = row++;
        formPanel.add(txtAnoFab, gbc);

        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(lblAnoMod, gbc);
        gbc.gridx = 1;
        gbc.gridy = row++;
        formPanel.add(txtAnoMod, gbc);

        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(lblProprietario, gbc);
        gbc.gridx = 1;
        gbc.gridy = row++;
        formPanel.add(txtProprietario, gbc);

        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(lblCondutor, gbc);
        gbc.gridx = 1;
        gbc.gridy = row++;
        formPanel.add(txtCondutor, gbc);

        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(lblCnh, gbc);
        gbc.gridx = 1;
        gbc.gridy = row++;
        formPanel.add(txtCnh, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSalvar = new JButton("Salvar");
        JButton btnCancelar = new JButton("Cancelar");
        buttonPanel.add(btnSalvar);
        buttonPanel.add(btnCancelar);

        if (aitExistente != null) {
            txtId.setText(String.valueOf(aitExistente.getId()));
            txtData.setText(aitExistente.getDataInfracao());
            txtEndereco.setText(aitExistente.getEnderecoInfracao());
            for (int i = 0; i < cmbInfracao.getItemCount(); i++) {
                if (cmbInfracao.getItemAt(i).getId() == aitExistente.getIdInfracao()) {
                    cmbInfracao.setSelectedIndex(i);
                    break;
                }
            }
            for (int i = 0; i < cmbMarca.getItemCount(); i++) {
                if (cmbMarca.getItemAt(i).getId() == aitExistente.getIdMarca()) {
                    cmbMarca.setSelectedIndex(i);
                    break;
                }
            }
            txtPlaca.setText(aitExistente.getPlaca());
            txtAnoFab.setText(String.valueOf(aitExistente.getAnoFabricacao()));
            txtAnoMod.setText(String.valueOf(aitExistente.getAnoModelo()));
            txtProprietario.setText(aitExistente.getProprietario());
            txtCondutor.setText(aitExistente.getCondutor());
            txtCnh.setText(aitExistente.getCnhCondutor());
        }

        JFormattedTextField finalTxtData = txtData;
        JTextField finalTxtPlaca = txtPlaca;
        JFormattedTextField finalTxtCnh = txtCnh;

        cmbInfracao.addActionListener(e -> {
            Infracao selected = (Infracao) cmbInfracao.getSelectedItem();
            if (selected != null) {
                lblValorDisplay.setText(String.format("R$ %.2f", selected.getValor()));
            }
        });
        cmbInfracao.setSelectedIndex(0);

        btnSalvar.addActionListener(e -> {
            try {
                Ait ait = new Ait();
                ait.setId(aitExistente != null ? aitExistente.getId() : nextId++);
                ait.setDataInfracao(finalTxtData.getText());
                ait.setEnderecoInfracao(txtEndereco.getText());
                ait.setIdInfracao(((Infracao) cmbInfracao.getSelectedItem()).getId());
                ait.setPlaca(finalTxtPlaca.getText().toUpperCase());
                ait.setIdMarca(((Marca) cmbMarca.getSelectedItem()).getId());
                ait.setAnoFabricacao(Integer.parseInt(txtAnoFab.getText()));
                ait.setAnoModelo(Integer.parseInt(txtAnoMod.getText()));
                ait.setProprietario(txtProprietario.getText());
                ait.setCondutor(txtCondutor.getText());
                ait.setCnhCondutor(finalTxtCnh.getText());

                if (aitExistente == null) {
                    aitList.add(ait);
                } else {
                    int index = aitList.indexOf(aitExistente);
                    if (index >= 0) {
                        aitList.set(index, ait);
                    }
                }

                JOptionPane.showMessageDialog(frame, "AIT salvo com sucesso!");
                criarTelaListagem();
                frame.dispose();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Erro ao salvar: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCancelar.addActionListener(e -> {
            criarTelaListagem();
            frame.dispose();
        });

        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        frame.add(mainPanel);
        frame.setVisible(true);
    }
}