import java.util.Arrays;

public class Pesos {
    private Double[] _pesos;

    Pesos(Double... pesos) {
        _pesos = pesos;
    }

    public Double obtenerPeso(int i) {
        return _pesos[i];
    }

    public int size() {
        return _pesos.length;
    }

    public String toString() {
        return Arrays.toString(_pesos);
    }

}
