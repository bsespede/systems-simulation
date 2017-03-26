package Simulator;

import general.Cell;
import general.Direction;
import general.Particle;
import io.FileProcessor;

import java.util.HashSet;
import java.util.Set;

public class Simulation {

    private Cell[][] cells;
    private Set<Particle> particles = new HashSet<>();
    private long particleCounter = 0;

    public Simulation(int height, int width, int l) {
        cells = new Cell[height][width];
        int lx = (int) (Math.random() * (height - l - 1 ) )+ height % 5;
        int ly = (int) (Math.random() * width) + width % 5;
        for(int i = 0 ; i < height ; i++){
            for (int j = 0; j < width; j++) {
                if(i == 0 || i == height - 1){
                    cells[i][j] = new Cell(true);
                }else{
                    if(j == ly && ( i >= lx && i < lx + l)){
                        cells[i][j] = new Cell(true);
                    }else{
                        cells[i][j] = new Cell(false);
                    }
                }
            }
        }
    }

    public void simulate(int n){
        addParticles();
        printTestCells();
        for (int i = 0; i < n; i++) {
            moveParticles();
            checkCollisions();
            if(i % 4 == 0){
                addParticles();
            }
            printTestCells();
            FileProcessor.outputState(cells, particles,"./output.txt");
        }
    }

    public void addParticles(){
        for (int i = 1; i < cells[0].length; i++) {
            if(!cells[i][0].isSolid()){

                Particle p = new Particle( i , 0, particleCounter++, Direction.UR);
                cells[i][0].getParticles().add(p);
                particles.add(p);

                p = new Particle( i , 0, particles.size(), Direction.R);
                cells[i][0].getParticles().add(p);
                particles.add(p);

                p = new Particle( i , 0, particles.size(), Direction.BR);
                cells[i][0].getParticles().add(p);
                particles.add(p);
            }
        }
    }

    public void checkCollisions(){
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[0].length; j++) {
                if(!cells[i][j].isSolid() && cells[i][j].getParticles().size() != 0){
                    Particle.resolveCollision(cells[i][j].getParticles().toArray(new Particle[cells[i][j].getParticles().size()]));
                }
            }
        }
    }

    public void moveParticles(){
        for(Particle p : particles){
            cells[p.getX()][p.getY()].getParticles().remove(p);
            int xDestiny = p.getX() + p.getDir().getDirx();
            int yDestiny = p.getY() + p.getDir().getDiry();
            if(p.getMovementCounter() % 2 == 0 && p.getDir() != Direction.R && p.getDir() != Direction.L){
                if(p.getDir().getDiry()>=0){
                    yDestiny += 1;
                }else{
                    yDestiny -= 1;
                }
            }
            if(yDestiny < 0 || yDestiny >= cells[0].length || xDestiny < 0 || xDestiny >= cells.length){
                particles.remove(p);
                break;
            }

            if(cells[xDestiny][yDestiny].isSolid()){
                p.resetMovementCounter();
                boolean invertX = false;
                boolean invertY = false;
                if(cells[p.getX()][p.getY() + p.getDir().getDiry()].isSolid() && cells[p.getX() + p.getDir().getDirx()][p.getY()].isSolid()){
                    invertX = true;
                    invertY = true;
                }else if (cells[p.getX() + p.getDir().getDirx()][p.getY()].isSolid()) {
                    invertX = true;
                }else if(cells[p.getX()][p.getY() + p.getDir().getDiry()].isSolid()){
                    invertY = true;
                }else{
                    invertX = true;
                    invertY = true;
                }
                if(invertX){
                    p.invertX();
                }
                if(invertY){
                    p.invertY();
                }
            }else{
                p.setX(xDestiny);
                p.setY(yDestiny);
                p.incMovementCounter();
            }
            cells[p.getX()][p.getY()].getParticles().add(p);
        }
    }

    public void printTestCells(){
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                if(cells[i][j].isSolid()){
                    System.out.print("  S   ");
                }else{
                    System.out.print("  "  + cells[i][j].getParticles().size() + "   ");
                }
            }
            System.out.println();
        }
        System.out.println();
        System.out.println();
    }
}
