public class EvaluadorPorCombos extends Evaluador{
    private static int _noObjetivo;

    public EvaluadorPorCombos(int noObjetivo) {
        _noObjetivo = noObjetivo;
    }

    @Override
    public int valoracion(Tablero tablero, int jugador) {
        int valoracion = 0;
        for (int col=0; col < tablero.NCOLUMNAS; col++) {
            for (int fila=0; fila < tablero.NFILAS; fila++) {
                jugador = tablero.obtenerCasillas()[col][fila];
                if (jugador != tablero.VACIO) {
                    valoracion += tablero.contarLineaVertical(col,fila,jugador, _noObjetivo)
                        + tablero.contarLineaHorizontal(col,fila,jugador, _noObjetivo)
                        + tablero.contarLineaDiagonal(col,fila,jugador, _noObjetivo);
                }
            }
        }
        return(valoracion);
    }

}
