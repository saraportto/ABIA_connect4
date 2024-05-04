public class EvaluadorPonderado  extends Evaluador{
    public Pesos pesos;

    public EvaluadorPonderado() {
        pesos = new Pesos(1.0, 1.0);
    }

    @Override
    public double valoracion(Tablero tablero, int jugador) {
        double valoracion = 0;
        valoracion = valoracionPorAdyacentes(tablero, jugador) * pesos.obtenerPeso(0) + valoracionPorTrios(tablero, jugador) * pesos.obtenerPeso(1);
        return(valoracion);
    }

    public int valoracionPorAdyacentes(Tablero tablero, int jugador) {
        int valoracion = 0;
        for (int col=0; col < Tablero.NCOLUMNAS; col++) {
            for (int fila=0; fila < Tablero.NFILAS; fila++) {
                if (tablero.obtenerCasillas()[col][fila] != Tablero.VACIO) {
                    valoracion += tablero.contarAdyacentes(col,fila,jugador);
                }
            }
        }
        return(valoracion);
    }

    public int valoracionPorTrios(Tablero tablero, int jugador) {
        int noObjetivo = 3;
        int valoracion = 0;
        for (int col=0; col < Tablero.NCOLUMNAS; col++) {
            for (int fila=0; fila < Tablero.NFILAS; fila++) {
                jugador = tablero.obtenerCasillas()[col][fila];
                if (jugador != Tablero.VACIO) {
                    valoracion += tablero.contarLineaVertical(col,fila,jugador, noObjetivo)
                        + tablero.contarLineaHorizontal(col,fila,jugador, noObjetivo)
                        + tablero.contarLineaDiagonal(col,fila,jugador, noObjetivo);
                }
            }
        }
        return(valoracion);
    }

    public Pesos obtenerPesos() {
        return pesos;
    }

    public void establecerPesos(Pesos pesos) {
        this.pesos = pesos;
    }
}
