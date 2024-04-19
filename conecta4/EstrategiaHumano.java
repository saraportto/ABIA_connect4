public class EstrategiaHumano extends Estrategia {
   /*
    * Estrategia que permite jugadores humanos
    * 
    * La función "buscarMovimiento" simplemente muestra el tablero actual 
    * y lee de STDIN el movimiento (num. de columna) del jugador humano
    * /
   
    /** Creates a new instance of EstrategiaHumano */
    public EstrategiaHumano() {
    }
    
    public int buscarMovimiento(Tablero tablero, int jugador) {
       boolean movimientosPosibles[] = tablero.columnasLibres();
       int posicion=-1;
              
       tablero.mostrar();
       do {
         do {
             System.out.print("> [JUGADOR "+jugador+"] Elegir columna [0.."+(Tablero.NCOLUMNAS-1)+"]: ");
             posicion = Integer.parseInt(leerLinea());
         } while((posicion<0) || (posicion>=Tablero.NCOLUMNAS));
       } while(!movimientosPosibles[posicion]);
       return(posicion);
    }
    
    private String leerLinea() {
        char c=0;
        String buffer = new String();
        do {
            try {    
               c = (char) System.in.read();
            } 
            catch (Exception e) {
               Conecta4.ERROR_FATAL("Error en la entrada. Juego Abortado.");
            }
            if (Character.isDigit(c)) {
              buffer += c;
            }
        } while(c != '\n'); 
        return(buffer);
    }

}
