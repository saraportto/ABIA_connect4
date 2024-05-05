public class EvaluadorPonderado  extends Evaluador{
    private int MAX_ADYACENTES = 84;
    private int MAX_TRIOS = 11;
    private int MAX_TOPS = 7;
    private int MAX_CENTROS = 20;
    
    public Pesos pesos;

    public EvaluadorPonderado() {
        pesos = new Pesos(1.0, 1.0, 1.0, 0.5);
    }

    @Override
    public double valoracion(Tablero tablero, int jugador) {
        double valoracion = 0;
        valoracion = (
            valoracionPorAdyacentes(tablero, jugador) * pesos.obtenerPeso(0)
            + valoracionPorTrios(tablero, jugador) * pesos.obtenerPeso(1)
            + valoracionPorTops(tablero, jugador) * pesos.obtenerPeso(2)
            + valoracionPorCentros(tablero, jugador) * pesos.obtenerPeso(3)
        );
        return(valoracion);
    }

    private int valoracionPorAdyacentes(Tablero tablero, int jugador) {
        double valoracion = 0;
        for (int col=0; col < Tablero.NCOLUMNAS; col++) {
            for (int fila=0; fila < Tablero.NFILAS; fila++) {
                if (tablero.obtenerCasillas()[col][fila] == jugador) {
                    valoracion += tablero.contarAdyacentes(col,fila,jugador);
                }
            }
        }
        valoracion = (valoracion / MAX_ADYACENTES) * 100;  // Normalizar de 0 a 100
        return((int)valoracion);
    }

    private int valoracionPorTrios(Tablero tablero, int jugador) {
        int noObjetivo = 3;
        double valoracion = 0;
        for (int col=0; col < Tablero.NCOLUMNAS; col++) {
            for (int fila=0; fila < Tablero.NFILAS; fila++) {
                valoracion += tablero.contarLineaVertical(col,fila,jugador, noObjetivo)
                    + tablero.contarLineaHorizontal(col,fila,jugador, noObjetivo)
                    + tablero.contarLineaDiagonal(col,fila,jugador, noObjetivo);
            }
        }
        valoracion = (valoracion / MAX_TRIOS) * 100;  // Normalizar de 0 a 100
        return((int)valoracion);
    }

    public int valoracionPorTops(Tablero tablero, int jugador) {
        double valoracion = 0;

        for (int col = 0; col < Tablero.NCOLUMNAS; col++) {
            if (tablero.comprobarUltimaFicha(col) == jugador) {
                valoracion++;
            }
        }

        valoracion = (valoracion / MAX_TOPS) * 100;
        return ((int)valoracion);
    }

    private int valoracionPorCentros(Tablero tablero, int jugador) {
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

        valoracion = (valoracion / MAX_CENTROS) * 100;  // Normalizar de 0 a 100
        return((int)valoracion);
    }

    public Pesos obtenerPesos() {
        return pesos;
    }

    public void establecerPesos(Pesos pesos) {
        this.pesos = pesos;
    }
}
