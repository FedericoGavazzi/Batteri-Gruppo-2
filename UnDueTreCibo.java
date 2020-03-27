package batteri_figli;

/*
  Copyright (C) 2013 Alessandro Bugatti (alessandro.bugatti@istruzione.it)

  This program is free software; you can redistribute it and/or
  modify it under the terms of the GNU General Public License
  as published by the Free Software Foundation; either version 2
  of the License, or (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program; if not, write to the Free Software
  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
import java.awt.Color;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe d'esempio per la gara
 *
 * @author Gruppo 2 Federico Gavazzi, Michele Potettu, Matteo Shermadhi, Nezir
 * Alimeta, De Silva
 */
public class UnDueTreCibo extends batteri.Batterio implements Cloneable {

    /**
     * Indica la posizione del cibo cercato sull'asse delle x
     */
    private int xCibo;

    /**
     * Indica la posizione del cibo cercato sull'asse delle x
     */
    private int yCibo;

    /**
     * Indica se è stato trovato del cibo
     */
    private boolean ciboTrovato;

    /**
     * Stabilisce la direzione di movimento del batterio sull'asse delle x
     */
    private int movimentoX;

    /**
     * Moltiplica xCibo e yCibo nella ricerca maxi per avere un'area più grande
     * in cui controllare
     */
    private static final int MOLTIPLICATOREMAXI = 29; // best 29
    /**
     * Attributo per specificare la ricerca utilizzata quando viene trovato del
     * cibo
     */
    private int ricerca;

    public UnDueTreCibo(int x, int y, Color c, batteri.Food f) {
        super(x, y, c, f);
        xCibo = 0;
        yCibo = 0;
        ciboTrovato = false;
        //la direzione del movimento del batterio è decisa randomicamente alla sua creazione
        movimentoX = 1;
        ricerca = (int) (Math.random() * 4);
    }

    /**
     * Metodo che ricerca del cibo in una grande area intorno al batterio e
     * modifica la sua posizione in caso positivo
     *
     * @return true se viene trovato del cibo
     * @return false se non viene trovato cibo
     */
    private boolean ricercaMaxi() {
        for (xCibo = -4; xCibo <= 4; xCibo++) {
            for (yCibo = -4; yCibo <= 4; yCibo++) {
                if (ControllaCibo(x + xCibo * MOLTIPLICATOREMAXI, y + yCibo * MOLTIPLICATOREMAXI)) {
                    x += xCibo * MOLTIPLICATOREMAXI;
                    y += yCibo * MOLTIPLICATOREMAXI;
                    ciboTrovato = true;
                    movimentoX *= -1;
                    return true;
                }
            }
        }
        ciboTrovato = false;
        return false;
    }

    /**
     * Metodo che ricerca del cibo in una piccola area intorno partendo al
     * batterio dall'alto e modifica la sua posizione in caso positivo
     *
     * @return true se viene trovato del cibo
     * @return false se non viene trovato cibo
     */
    private boolean ricercaMiniUp() {
        for (yCibo = -12; yCibo <= 12; yCibo += 2) {
            for (xCibo = -12; xCibo <= 12; xCibo += 2) {
                if (ControllaCibo(x + xCibo, y + yCibo)) {
                    x += xCibo;
                    y += yCibo;
                    ciboTrovato = true;
                    return true;
                }
            }
        }
        // imposto ciboTrovato a false per tornare a usare la ricercaMaxi
        ciboTrovato = false;
        return false;
    }

    /**
     * Metodo che ricerca del cibo in una piccola area intorno al batterio
     * partendo dal basso e modifica la sua posizione in caso positivo
     *
     * @return true se viene trovato del cibo
     * @return false se non viene trovato cibo
     */
    private boolean ricercaMiniDown() {
        for (yCibo = 12; yCibo >= -12; yCibo -= 2) {
            for (xCibo = -12; xCibo <= 12; xCibo += 2) {
                if (ControllaCibo(x + xCibo, y + yCibo)) {
                    x += xCibo;
                    y += yCibo;
                    ciboTrovato = true;
                    return true;
                }
            }
        }
        ciboTrovato = false;
        return false;
    }

    /**
     * Metodo che ricerca del cibo in una piccola area intorno al batterio
     * partendo da sinistra e modifica la sua posizione in caso positivo
     *
     * @return true se viene trovato del cibo
     * @return false se non viene trovato cibo
     */
    private boolean ricercaMiniSx() {
        for (xCibo = -12; xCibo <= 12; xCibo += 2) {
            for (yCibo = -12; yCibo <= 12; yCibo += 2) {
                if (ControllaCibo(x + xCibo, y + yCibo)) {
                    x += xCibo;
                    y += yCibo;
                    ciboTrovato = true;
                    return true;
                }
            }
        }
        ciboTrovato = false;
        return false;
    }

    /**
     * Metodo che ricerca del cibo in una piccola area intorno al batterio
     * partendo da destra e modifica la sua posizione in caso positivo
     *
     * @return true se viene trovato del cibo
     * @return false se non viene trovato cibo
     */
    private boolean ricercaMiniDx() {
        for (xCibo = 12; xCibo >= -12; xCibo -= 2) {
            for (yCibo = -12; yCibo <= 12; yCibo += 2) {
                if (ControllaCibo(x + xCibo, y + yCibo)) {
                    x += xCibo;
                    y += yCibo;
                    ciboTrovato = true;
                    return true;
                }
            }
        }
        ciboTrovato = false;
        return false;
    }

    /**
     * Il metodo sposta permette al batterio di muoversi nel campo di gioco.
     * Comprende 5 tipi diversi di ricerca una per il cibo vicino e una per il
     * cibo lontano. La ricerca lontana è la prima ad essere eseguita. Se viene
     * trovato del cibo verrà scelta casualmente una delle ricerca mini. Ogni
     * volta che il batterio trova del cibo si teletrasporterà su di esso per
     * mangiarlo. Non vengono mai eseguite tutte le ricerche insieme. Quando non
     * viene trovato del cibo si muoverà in direzioni casuali e rimbalzerà sui
     * bordi.
     */
    @Override
    protected void Sposta() {
        //ricerca MAXI: controlla in posizioni lontane se ci sia del cibo
        if (!ciboTrovato) {// se non ho trovato del cibo vicino eseguo la ricerca maxi finchè non trovo cibo 
            if (ricercaMaxi()) {
                // Imposto il tipo di ricerca da effettuare
                ricerca = (int) (Math.random() * 4);
                return;
            }
        }

        // Ricerca MINI: controlla che ci sia del cibo in posizioni vicine
        if (ciboTrovato) {// se ho trovato del cibo eseguirò solo la ricerca mini finchè non smetto di trovare del cibo

            if (ricerca == 0 && ricercaMiniUp()) {
                return;
            } else if (ricerca == 1 && ricercaMiniDown()) {

                return;
            } else if (ricerca == 2 && ricercaMiniSx()) {

                return;
            } else if (ricerca == 3 && ricercaMiniDx()) {
                return;
            }

        }
        //eseguo questa parte di codice solo se non ho trovato cibo
        // controllo di non uscire dai bordi e modifico la direzione del movimento se mi trovo sui bordi
        if (x <= 0 || x >= getFoodWitdh()) {
            movimentoX *= -1;
        }
        //aggiorno la posizione 
        x += movimentoX;
    }

    @Override
    public batteri.Batterio Clona() {
        try {
            return (UnDueTreCibo) this.clone();

        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(UnDueTreCibo.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
