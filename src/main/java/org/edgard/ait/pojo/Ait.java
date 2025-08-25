package org.edgard.ait.pojo;

    import java.util.Objects;

    public class Ait {
        private int id;
        private String dataInfracao;
        private int idInfracao;
        private String placa;
        private int idMarca;
        private int anoFabricacao;
        private int anoModelo;
        private String enderecoInfracao;
        private String proprietario;
        private String condutor;
        private String cnhCondutor;

        public int getId() { return id; }
        public void setId(int id) { this.id = id; }

        public String getDataInfracao() { return dataInfracao; }
        public void setDataInfracao(String dataInfracao) { this.dataInfracao = dataInfracao; }

        public int getIdInfracao() { return idInfracao; }
        public void setIdInfracao(int idInfracao) { this.idInfracao = idInfracao; }

        public String getPlaca() { return placa; }
        public void setPlaca(String placa) { this.placa = placa; }

        public int getIdMarca() { return idMarca; }
        public void setIdMarca(int idMarca) { this.idMarca = idMarca; }

        public int getAnoFabricacao() { return anoFabricacao; }
        public void setAnoFabricacao(int anoFabricacao) { this.anoFabricacao = anoFabricacao; }

        public int getAnoModelo() { return anoModelo; }
        public void setAnoModelo(int anoModelo) { this.anoModelo = anoModelo; }

        public String getEnderecoInfracao() { return enderecoInfracao; }
        public void setEnderecoInfracao(String enderecoInfracao) { this.enderecoInfracao = enderecoInfracao; }

        public String getProprietario() { return proprietario; }
        public void setProprietario(String proprietario) { this.proprietario = proprietario; }

        public String getCondutor() { return condutor; }
        public void setCondutor(String condutor) { this.condutor = condutor; }

        public String getCnhCondutor() { return cnhCondutor; }
        public void setCnhCondutor(String cnhCondutor) { this.cnhCondutor = cnhCondutor; }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Ait ait = (Ait) obj;
            return id == ait.id;
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }
    }