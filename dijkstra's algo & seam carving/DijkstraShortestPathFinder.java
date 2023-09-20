package graphs.shortestpaths;

import priorityqueues.ExtrinsicMinPQ;
import priorityqueues.NaiveMinPQ;
import graphs.BaseEdge;
import graphs.Graph;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Computes shortest paths using Dijkstra's algorithm.
 * @see SPTShortestPathFinder for more documentation.
 */
public class DijkstraShortestPathFinder<G extends Graph<V, E>, V, E extends BaseEdge<V, E>>
    extends SPTShortestPathFinder<G, V, E> {

    protected <T> ExtrinsicMinPQ<T> createMinPQ() {
        return new NaiveMinPQ<>();
        /*
        If you have confidence in your heap implementation, you can disable the line above
        and enable the one below.
         */
        // return new ArrayHeapMinPQ<>();

        /*
        Otherwise, do not change this method.
        We override this during grading to test your code using our correct implementation so that
        you don't lose extra points if your implementation is buggy.
         */
    }

    @Override
    protected Map<V, E> constructShortestPathsTree(G graph, V start, V end) {
        Set<V> known = new HashSet<>();
        Map<V, E> edgeTo = new HashMap<V, E>();
        Map<V, Double> distTo = new HashMap<V, Double>();
        distTo.put(start, 0.0);
        ExtrinsicMinPQ<V> queue = this.createMinPQ();
        queue.add(start, distTo.get(start));

        if (Objects.equals(start, end)) {
            return edgeTo;
        }

        while (!queue.isEmpty()) {
            V curr = queue.removeMin();
            if (curr == end) {
                break;
            }
            if (known.contains(curr)) {
                continue;
            }
            known.add(curr);
            Collection<E> outgoingEdges = graph.outgoingEdgesFrom(curr);
            for (E edge : outgoingEdges) {
                V neighbor = edge.to();
                double weight = edge.weight() + distTo.get(curr);
                if (!distTo.containsKey(neighbor) || weight < distTo.get(neighbor)) {
                    distTo.put(neighbor, weight);
                    edgeTo.put(neighbor, edge);
                    queue.add(neighbor, weight);
                }
            }

        }
        return edgeTo;
    }

    @Override
    protected ShortestPath<V, E> extractShortestPath(Map<V, E> spt, V start, V end) {
        if (Objects.equals(start, end)) {
            return new ShortestPath.SingleVertex<>(start);
        }
        List<E> edges = new ArrayList<E>();
        V curr = end;
        while (!Objects.equals(curr, start)) {
            E edge = spt.get(curr);
            if (edge == null) {
                return new ShortestPath.Failure<>();
            }
            edges.add(edge);
            curr = edge.from();
        }
        Collections.reverse(edges);
        return new ShortestPath.Success<>(edges);
    }

}
