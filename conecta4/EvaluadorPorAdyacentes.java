public class EvaluadorPorAdyacentes extends Evaluador{
    public EvaluadorPorAdyacentes() {}

    @Override
    public int valoracion(Tablero tablero, int jugador) {
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

}
