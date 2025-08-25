package org.edgard.ait.view;

import org.edgard.ait.pojo.Ait;
import org.edgard.ait.pojo.Infracao;
import org.edgard.ait.pojo.Marca;
import org.edgard.ait.data.InfracaoRepository;
import org.edgard.ait.data.MarcaRepository;

import javax.swing.table.AbstractTableModel;
import java.util.List;
import java.util.Optional;

class AitTableModel extends AbstractTableModel {
    private final List<Ait> aitList;
    private final String[] colunas;
    private final InfracaoRepository infracaoRepository = new InfracaoRepository();
    private final MarcaRepository marcaRepository = new MarcaRepository();

    public AitTableModel(List<Ait> aitList, String[] colunas) {
        this.aitList = aitList;
        this.colunas = colunas;
    }

    @Override
    public int getRowCount() {
        return aitList.size();
    }

    @Override
    public int getColumnCount() {
        return colunas.length;
    }

    @Override
    public String getColumnName(int column) {
        return colunas[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Ait ait = aitList.get(rowIndex);

        switch (columnIndex) {
            case 0: return ait.getId();
            case 1: return ait.getDataInfracao();
            case 2: return ait.getPlaca();
            case 3: // Descrição da infração
                Optional<Infracao> infracao = infracaoRepository.findById(ait.getIdInfracao());
                return infracao.map(Infracao::getDescricao).orElse("Desconhecida");
            case 4: // Valor da infração
                Optional<Infracao> infracaoValor = infracaoRepository.findById(ait.getIdInfracao());
                return infracaoValor.map(i -> String.format("R$ %.2f", i.getValor())).orElse("R$ 0,00");
            case 5: // Marca do veículo
                Optional<Marca> marca = marcaRepository.findById(ait.getIdMarca());
                return marca.map(Marca::getNome).orElse("Desconhecida");
            case 6: return ait.getProprietario();
            case 7: return ait.getCondutor();
            default: return null;
        }
    }
}