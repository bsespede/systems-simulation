package cellIndexMethod;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import io.FileProcessor;
import particle.Particle;

public abstract class Simulation {

	protected final boolean contour;
	protected final int L;
	protected final Set<Particle> particles;


	public Simulation(final int L, final int particlesNumber, final boolean contour, final double radius, final double  interactionRadius) {
		this.contour = contour;
		this.L = L;
//		this.particles = generateParticles(L, particlesNumber, radius, interactionRadius);
	}

	public Map<Particle, Set<Particle>> getNeighbours() {
		final Map<Particle, Set<Particle>> neighbourMap = new HashMap<Particle, Set<Particle>>();

		for (Particle particle: particles) {
			final Set<Particle> particleNeighbours = new HashSet<Particle>();
			
			for (Particle candidateNeighbour: getNeighbourCandidates(particle)) {
				if (!particle.equals(candidateNeighbour)) {
					double distance = (contour)? particle.calculateContourDistance(candidateNeighbour, L) : particle.calculateDistance(candidateNeighbour);
					if (distance < particle.getInteractionRadius() + particle.getRadius() + candidateNeighbour.getRadius()) {
						particleNeighbours.add(candidateNeighbour);
					}
				}				
			}			
			neighbourMap.put(particle, particleNeighbours);
		}
		return neighbourMap;
	}

	protected abstract Set<Particle> getNeighbourCandidates(final Particle particle);

}
