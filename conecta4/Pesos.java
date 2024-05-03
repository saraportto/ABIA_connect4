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

    public Pesos[] mutar() {
        Pesos[] mutaciones = new Pesos[_pesos.length*2];
        for (int i = 0; i < _pesos.length; i++) {

            Double[] mutacion1 = _pesos.clone();
            Double[] mutacion2 = _pesos.clone();

            mutacion1[i] += mutacion1[i] * 0.1;
            mutacion2[i] -= mutacion2[i] * 0.1;

            mutaciones[i] = new Pesos(mutacion1);
            mutaciones[i + _pesos.length] = new Pesos(mutacion2);
        }
        return mutaciones;
    }
}
