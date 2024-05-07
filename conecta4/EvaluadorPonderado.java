public class EvaluadorPonderado  extends Evaluador{
    private int MAX_ADYACENTES = 84;
    private int MAX_TRIOS = 11;
    private int MAX_TOPS = 7;
    private int MAX_CENTROS = 20;
    
    public Pesos _pesos;

    public EvaluadorPonderado(Double... pesos) {
        _pesos = new Pesos(pesos);
    }

    @Override
    public double valoracion(Tablero tablero, int jugador) {
        double valoracion = 0;
        valoracion = (
            valoracionPorAdyacentes(tablero, jugador) * _pesos.obtenerPeso(0)
            + valoracionPorTrios(tablero, jugador) * _pesos.obtenerPeso(1)
            + valoracionPorTops(tablero, jugador) * _pesos.obtenerPeso(2)
            + valoracionPorCentros(tablero, jugador) * _pesos.obtenerPeso(3)
        );
        return(valoracion);
    }

    private double valoracionPorAdyacentes(Tablero tablero, int jugador) {
        double valoracion = 0;
        for (int col=0; col < Tablero.NCOLUMNAS; col++) {
            for (int fila=0; fila < Tablero.NFILAS; fila++) {
                if (tablero.obtenerCasillas()[col][fila] == jugador) {
                    valoracion += tablero.contarAdyacentes(col,fila,jugador);
                }
            }
        }
        valoracion /= MAX_ADYACENTES;  // Normalizar de 0 a 1
        return valoracion;
    }

    private double valoracionPorTrios(Tablero tablero, int jugador) {
        int noObjetivo = 3;
        double valoracion = 0;
        for (int col=0; col < Tablero.NCOLUMNAS; col++) {
            for (int fila=0; fila < Tablero.NFILAS; fila++) {
                valoracion += tablero.contarLineaVertical(col,fila,jugador, noObjetivo)
                    + tablero.contarLineaHorizontal(col,fila,jugador, noObjetivo)
                    + tablero.contarLineaDiagonal(col,fila,jugador, noObjetivo);
            }
        }
        valoracion /= MAX_TRIOS;  // Normalizar de 0 a 1
        return valoracion;
    }

    public double valoracionPorTops(Tablero tablero, int jugador) {
        double valoracion = 0;

        for (int col = 0; col < Tablero.NCOLUMNAS; col++) {
            if (tablero.comprobarUltimaFicha(col) == jugador) {
                valoracion++;
            }
        }

        valoracion /= MAX_TOPS;  // Normalizar de 0 a 1
        return valoracion;
    }

    private double valoracionPorCentros(Tablero tablero, int jugador) {
        double valoracion = 0;
        for (int col=0; col < Tablero.NCOLUMNAS; col++) {
            for (int fila=0; fila < Tablero.NFILAS; fila++) {
                if (tablero.obtenerCasillas()[col][fila] == jugador) {
                    if (col == 3) {
                        valoracion += 2;
                    } else if (col == 2 || col == 4) {
                        valoracion += 1;
                    }
                }
            }
        }

        valoracion /= MAX_CENTROS;  // Normalizar de 0 a 1
        return valoracion;
    }

    public Pesos obtenerPesos() {
        return _pesos;
    }

    public void establecerPesos(Pesos pesos) {
        this._pesos = pesos;
    }
}
