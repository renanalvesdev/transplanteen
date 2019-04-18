package br.com.transplanteen.tcc.transplanteen;

public enum TipoUsuario {
        PACIENTE("P", 0),
        ENFERMEIRO("E", 1);

        private String stringValue;
        private int intValue;


        private TipoUsuario(String toString, int value) {
            stringValue = toString;
            intValue = value;
        }

        @Override
        public String toString() {
            return stringValue;
        }
}
