/*
 * Jugador.java
 *
 * Created on 10 de enero de 2004, 17:19
 */

/**
 *
 * @author  ribadas
 */
public class Jugador {
    
    private Estrategia _estrategia;
    private int _identificador;
    
    /** Creates a new instance of Jugador */
    public Jugador() {
    }
    
    public Jugador(int identificador) {
        _identificador = identificador;
    }
    
    public void establecerEstrategia(Estrategia estrategia) {
        _estrategia = estrategia;
    }
    
    public int obtenerJugada(Tablero tablero) {
        return(_estrategia.buscarMovimiento(tablero, _identificador));
    }
    
    public int getIdentificador() {
        return(_identificador);
    }
    
    public static final int alternarJugador(int jugadorActual) {
        return(((jugadorActual%2)+1));  // Alterna entre jugador 1 y 2
    }
    
}
