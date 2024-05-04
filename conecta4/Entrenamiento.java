import java.lang.reflect.Array;
import java.util.Arrays;

public class Entrenamiento {
    private Jugador _jugador1;    
    private Jugador _jugador2;
    private Tablero _tablero;
    
    /** Creates a new instance of Conecta4 */
    public Entrenamiento() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        cargarArgumentos(args);

    // Crear jugadores y establecer estrategias
	// Jugador 1: jugador humano
        Jugador jugador1 = new Jugador(1);
        jugador1.establecerEstrategia(new EstrategiaMinMax(
            4,
            new EvaluadorPonderado()
        ));
        DEBUG("Jugador 1: maquina actual\n");
       
        // Jugador 2: jugador minimax con evaluador aleatorio y prof. busqueda 4
        Jugador jugador2 = new Jugador(2);
        jugador2.establecerEstrategia(new EstrategiaMinMax(
            4,
            new EvaluadorPonderado()
        ));
        DEBUG("Jugador 2: maquina nueva\n");

        // Jugar

        boolean salir = false;
        while (!salir) {
            salir = true;
            Pesos[] nuevosPesos = jugador1.obtenerEstrategia().obtenerEvaluador().obtenerPesos().mutar();
            for (Pesos pesos : nuevosPesos) {
                jugador2.obtenerEstrategia().obtenerEvaluador().establecerPesos(pesos);
                if (combate(jugador1, jugador2)) {
                    jugador1.obtenerEstrategia().obtenerEvaluador().establecerPesos(jugador2.obtenerEstrategia().obtenerEvaluador().obtenerPesos());
                    System.out.println("Nuevos pesos: " + jugador1.obtenerEstrategia().obtenerEvaluador().obtenerPesos());
                    salir = false;
                    break;
                }
            }
        }
        
        System.out.println("Pesos finales: " + jugador1.obtenerEstrategia().obtenerEvaluador().obtenerPesos());

    }
    

    private static void cargarArgumentos(String[] args) {
        // procesar parametros de linea de comandos
    }

    private static void jugar(Jugador jugador1, Jugador jugador2, Tablero tablero) {
        int turno=0;
        Jugador jugadorActual;
        int movimiento;
        boolean posicionesPosibles[];
        
       // comprobar tablero: necesario para establecer si es o no un tablero final
        tablero.obtenerGanador();
        while(!tablero.esFinal()){
            turno++;
            // establecer jugador del turno actual
            if ((turno%2) == 1) { // turno impar -> jugador1
                jugadorActual = jugador1;
            }
            else {// turno par -> jugador2
                jugadorActual = jugador2;
            }
            // obtener movimiento: llama al jugador que tenga el turno,
	    // que, a su vez, llamará a la estrategia que se le asigno al crearlo
            movimiento = jugadorActual.obtenerJugada(tablero);
            // comprobar si es correcto
            if ((movimiento>=0) && (movimiento<Tablero.NCOLUMNAS)) {
                posicionesPosibles = tablero.columnasLibres();
                if (posicionesPosibles[movimiento]) {
                    tablero.anadirFicha(movimiento, jugadorActual.getIdentificador());
                    // comprobar ganador
                    tablero.obtenerGanador();
                }
                else {
                    ERROR_FATAL("Columna completa. Juego Abortado.");
                }
            }
            else {
              ERROR_FATAL("Movimiento invalido. Juego Abortado.");
            }            
        }        
    }

    private static boolean combate(Jugador jugador1, Jugador jugador2) {
        int jugador1_gana = 0;
        int jugador2_gana = 0;

        for (int partida=0; partida<20; partida++) {
            Tablero tablero = new Tablero();
            tablero.inicializar();

            if (partida % 2 == 0) {
                jugar(jugador2, jugador1, tablero);
            } else {
                jugar(jugador1, jugador2, tablero);
            }
            // Mostrar resultados
            // tablero.mostrar();
            if (tablero.hayEmpate()) {
                //System.out.println("RESULTADO: Empate");
            }
            if (tablero.ganaJ1()){
                //System.out.println("RESULTADO: Gana jugador 1");
                jugador1_gana++;
            }
            if (tablero.ganaJ2()){
                //System.out.println("RESULTADO: Gana jugador 2");
                jugador2_gana++;
            }
        }
        //System.out.println("Jugador 1: "+jugador1_gana+" victorias");
        //System.out.println("Jugador 2: "+jugador2_gana+" victorias");
        if (jugador1_gana < jugador2_gana) {
            return true;
        } else {
            return false;
        }
    }


    public static final void ERROR_FATAL(java.lang.String mensaje) {
        System.out.println("ERROR FATAL\n\t"+mensaje);
        System.exit(0); // Finalizar aplicacion
    }
    
    public static final void DEBUG(String str) {
        System.out.print("DBG:"+str);
    }
    
    public static final void ERROR(java.lang.String mensaje) {
        System.out.println("ERROR\n\t"+mensaje);
    }
}
