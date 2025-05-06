/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package autonoma.PulgasLocas.models;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Juan Jacobo Cañas Henao
 * @since 06052025
 * @version 1.0.0
 */
/**
 * Clase que representa el campo de batalla donde se desarrolla la simulación antipulgas.
 * Administra las pulgas, los disparos y el puntaje del jugador.
 */
public class CampoDeBatalla {
    private List<Pulga> pulgas;
    private int puntaje;
    private int puntajeMaximo;
    private final int ANCHO_CAMPO = 800;
    private final int ALTO_CAMPO = 600;

    /**
     * Constructor que inicializa el campo de batalla y su lista de pulgas.
     */
    public CampoDeBatalla() {
        pulgas = new ArrayList<>();
        puntaje = 0;
        puntajeMaximo = 0;
    }

    /**
     * Agrega una nueva pulga normal en una posición aleatoria no ocupada.
     */
    public void agregarPulgaNormal() {
        PulgaNormal p = new PulgaNormal(getPosicionAleatoriaX(), getPosicionAleatoriaY());
        if (!colisiona(p)) {
            pulgas.add(p);
        }
    }

    /**
     * Agrega una nueva pulga mutante en una posición aleatoria no ocupada.
     */
    public void agregarPulgaMutante() {
        PulgaMutante p = new PulgaMutante(getPosicionAleatoriaX(), getPosicionAleatoriaY());
        if (!colisiona(p)) {
            pulgas.add(p);
        }
    }

    /**
     * Hace que todas las pulgas salten a nuevas posiciones aleatorias.
     */
    public void saltarPulgas() {
        Random rand = new Random();
        for (Pulga p : pulgas) {
            p.setX(rand.nextInt(ANCHO_CAMPO));
            p.setY(rand.nextInt(ALTO_CAMPO));
        }
    }

    /**
     * Simula un disparo con pistola en una posición (x, y) del campo.
     * @param x Coordenada X del disparo
     * @param y Coordenada Y del disparo
     */
    public void dispararConPistola(int x, int y) {
        for (Pulga p : pulgas) {
            if (p.estaViva() && p.contienePunto(x, y)) {
                p.recibirImpacto();
                if (!p.estaViva()) {
                    puntaje++;
                }
                break; // Solo impacta a una pulga
            }
        }
    }

    /**
     * Simula el lanzamiento del misil que destruye el 50% de las pulgas vivas.
     */
    public void lanzarMisil() {
        int vivas = (int) pulgas.stream().filter(Pulga::estaViva).count();
        int aEliminar = vivas / 2;
        int eliminadas = 0;

        Iterator<Pulga> it = pulgas.iterator();
        while (it.hasNext() && eliminadas < aEliminar) {
            Pulga p = it.next();
            if (p.estaViva()) {
                p.recibirImpacto(); // primer impacto
                if (!p.estaViva()) {
                    puntaje++;
                    eliminadas++;
                } else {
                    p.recibirImpacto(); // segundo impacto si es mutante transformado
                    if (!p.estaViva()) {
                        puntaje++;
                        eliminadas++;
                    }
                }
            }
        }
    }

    /**
     * Dibuja todas las pulgas en el campo.
     * @param g Objeto Graphics donde se dibujarán las pulgas.
     */
    public void paintComponent(Graphics g) {
        for (Pulga p : pulgas) {
            if (p.estaViva()) {
                p.dibujar(g);
            }
        }
    }

    /**
     * Verifica si una nueva pulga colisionaría con alguna ya existente.
     * @param nueva Pulga a verificar
     * @return true si hay colisión, false si se puede colocar
     */
    private boolean colisiona(Pulga nueva) {
        for (Pulga existente : pulgas) {
            if (Math.abs(nueva.getX() - existente.getX()) < 30 &&
                Math.abs(nueva.getY() - existente.getY()) < 30) {
                return true;
            }
        }
        return false;
    }

    /**
     * Devuelve una posición X aleatoria dentro del campo.
     */
    private int getPosicionAleatoriaX() {
        return new Random().nextInt(ANCHO_CAMPO - 50);
    }

    /**
     * Devuelve una posición Y aleatoria dentro del campo.
     */
    private int getPosicionAleatoriaY() {
        return new Random().nextInt(ALTO_CAMPO - 50);
    }

    // Getters y setters

    public int getPuntaje() {
        return puntaje;
    }

    public int getPuntajeMaximo() {
        return puntajeMaximo;
    }

    public void setPuntajeMaximo(int puntajeMaximo) {
        this.puntajeMaximo = puntajeMaximo;
    }

    public boolean hayPulgasVivas() {
        return pulgas.stream().anyMatch(Pulga::estaViva);
    }
}