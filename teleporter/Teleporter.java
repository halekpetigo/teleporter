package teleporter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Teleporter {

	static class teleporterMap {

		private Map<String,Set<String>> nodeMap =  
	            new HashMap<String,Set<String>>();
		
		//constructor
		teleporterMap(){
		}

		public void addEdge(String originNode, String destinationNode) {
			if(!existNode(originNode)) addNode(originNode);
			if(!existNode(destinationNode)) addNode(destinationNode);			
			addMapRoute(originNode, destinationNode);	
		}
		
		public void addMapRoute(String originNode, String destinationNode) {			
			nodeMap.get(originNode).add(destinationNode);
			nodeMap.get(destinationNode).add(originNode);
		}
		
		public void addNode(String node) {
			if(!existNode(node)) {
				Set<String> destinationSet = new HashSet<String>();
				nodeMap.put(node, destinationSet);
			}
		}
		
		public boolean existNode(String node) {
	        return nodeMap.containsKey(node);
	    }
		
		public boolean existEdge(String originNode, String destinationNode) {
			boolean r = false; 
			if(nodeMap != null && nodeMap.get(originNode) != null)
				r = nodeMap.get(originNode).contains(destinationNode);
	        return r;
	    }
		
		public Set<String> getNeighbors(String node) {
			Set<String> neighbors = null;			
			neighbors = nodeMap.get(node);			
			return neighbors;
		}
		
		private boolean isTeleport(String originNode, String destinationNode, List<String> traversed) {
			
			boolean canTravel = false;
			
			Set<String> myNeighbors = getNeighbors(originNode);
			
			Iterator<String> iterator = myNeighbors.iterator();
			
			//Search neighbor
			while(iterator.hasNext() && !canTravel) {
				String nNode = iterator.next();
				if(!traversed.contains(nNode)) 
					traversed.add(nNode);
				else
					continue;
				//System.out.println("Checking " + nNode);
				if(nNode == destinationNode || isTeleport(nNode, destinationNode, traversed)) {
					canTravel = true;
				}
			}
			
			return canTravel;			
		}
		
		public List<String> teleportFromJumps(String originNode, int numJump, List<String> traversed) {
			
			List<String> destinations = new ArrayList<String>();
			Set<String> myNeighbors = getNeighbors(originNode);
			Iterator<String> iterator = myNeighbors.iterator();
			
			//Search neighbor
			while(iterator.hasNext()) {
				String nNode = iterator.next();
				if(!destinations.contains(nNode) && !traversed.contains(nNode)) 
					destinations.add(nNode);
				if(numJump > 1) {
					traversed.addAll(destinations);
					destinations.addAll(teleportFromJumps(nNode, numJump-1, traversed));
				}
			}
			
			return destinations; 
		}
		
		public void canTeleportFromJumps(String originNode, int numJump) {
			List<String> traversed = new ArrayList<String>();
			List<String> destinations;
			traversed.add(originNode);
			destinations = teleportFromJumps(originNode, numJump, traversed);
			String outputStr = String.format("cities from Summerton in %s jumps: %s", numJump, destinations.toString());
			System.out.println(outputStr);
		}
		
		public void canTeleport(String originNode, String destinationNode) {
			List<String> traversed = new ArrayList<String>();
			traversed.add(originNode);
			String answer = isTeleport(originNode, destinationNode, traversed) == false ? "No" : "Yes";
			String outputStr = String.format("Can I teleport from %s to %s: %s", originNode, destinationNode, answer);
			System.out.println(outputStr);
		}
		
		public boolean isLoop(String originNode) {
			boolean r = false; 
			Set<String> myNeighbors = getNeighbors(originNode);
			List<String> traversed = new ArrayList<String>();
			traversed.add(originNode);
			
			String[] mN =  myNeighbors.toArray(new String[0]);
			outerloop:
			for(int i = 0; i < mN.length-1; i++) {
				for(int j = i+1; j < mN.length; j++) {
					if(isTeleport(mN[i], mN[j], traversed)) {
						r = true;
						break outerloop;
					}
					traversed.clear();
					traversed.add(originNode);
				}				
			}
			
	        return r;
	    }
		
		public void canLoop(String originNode) {
			String answer = isLoop(originNode) == false ? "No" : "Yes";
			String outputStr = String.format("loop possible from %s: %s", originNode, answer);
			System.out.println(outputStr);
		}
		
	}
	
	private teleporterMap tm;

	public Teleporter() {
		tm = new teleporterMap();
	}

	public static void main(String[] args) {

		//Create Graph
		String[][] inputArray = new String[][] {
			 { "Fortuna", "Hemingway" },
			 { "Fortuna", "Atlantis" },
			 { "Hemingway", "Chesterfield" },
			 { "Chesterfield", "Springton" },
			 { "Los Amigos", "Paristown" },
			 { "Paristown", "Oaktown" },
			 { "Los Amigos", "Oaktown" },
			 { "Summerton", "Springton" },
			 { "Summerton", "Hemingway" }			 
		};
		
		Teleporter teleport = new Teleporter();
	    for (int i=0; i < inputArray.length; i++) {
	        String o = inputArray[i][0];
	        String d = inputArray[i][1];
	        teleport.tm.addEdge(o, d);
	    }
	    
	    teleport.tm.canTeleportFromJumps("Summerton", 1);
	    teleport.tm.canTeleportFromJumps("Summerton", 2);
	    teleport.tm.canTeleport("Springton", "Atlantis");
	    teleport.tm.canTeleport("Oaktown", "Atlantis");
	    teleport.tm.canLoop("Oaktown");
	    teleport.tm.canLoop("Fortuna");

	}
}