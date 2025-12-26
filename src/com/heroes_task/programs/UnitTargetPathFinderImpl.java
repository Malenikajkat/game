package com.heroes_task.programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.Edge;
import com.battle.heroes.army.programs.UnitTargetPathFinder;

import java.util.*;

public class UnitTargetPathFinderImpl implements UnitTargetPathFinder {

    private static final int WIDTH = 27;
    private static final int HEIGHT = 21;
    private static final int[][] DIRECTIONS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}, {-1, -1}, {-1, 1}, {1, -1}, {1, 1}};

    @Override
    public List<Edge> getTargetPath(Unit attackUnit, Unit targetUnit, List<Unit> existingUnitList) {
        if (attackUnit == null || targetUnit == null) {
            return List.of();
        }

        int startX = attackUnit.getxCoordinate();
        int startY = attackUnit.getyCoordinate();
        int targetX = targetUnit.getxCoordinate();
        int targetY = targetUnit.getyCoordinate();

        if (startX == targetX && startY == targetY) {
            return List.of(new Edge(startX, startY));
        }

        Set<String> obstacles = new HashSet<>();
        for (Unit u : existingUnitList) {
            if (u != null && u.isAlive() && !(u.equals(attackUnit) || u.equals(targetUnit))) {
                obstacles.add(u.getxCoordinate() + "," + u.getyCoordinate());
            }
        }

        int[][] distances = new int[HEIGHT][WIDTH];
        Edge[][] parents = new Edge[HEIGHT][WIDTH];

        for (int i = 0; i < HEIGHT; i++) {
            Arrays.fill(distances[i], Integer.MAX_VALUE);
        }

        distances[startY][startX] = 0;

        Queue<Edge> queue = new LinkedList<>();
        queue.offer(new Edge(startX, startY));

        while (!queue.isEmpty()) {
            Edge current = queue.poll();
            int cx = current.getX();
            int cy = current.getY();

            if (cx == targetX && cy == targetY) {
                break;
            }

            for (int[] dir : DIRECTIONS) {
                int nx = cx + dir[0];
                int ny = cy + dir[1];

                if (nx >= 0 && nx < WIDTH && ny >= 0 && ny < HEIGHT && !obstacles.contains(nx + "," + ny)) {
                    int dist = distances[cy][cx] + 1;
                    if (dist < distances[ny][nx]) {
                        distances[ny][nx] = dist;
                        parents[ny][nx] = current;
                        queue.offer(new Edge(nx, ny));
                    }
                }
            }
        }

        List<Edge> path = new ArrayList<>();
        Edge current = new Edge(targetX, targetY);

        while (!(current.getX() == startX && current.getY() == startY)) {
            path.add(current);
            current = parents[current.getY()][current.getX()];
        }

        path.add(new Edge(startX, startY));
        Collections.reverse(path);

        return path;
    }

    /**
     * Обоснование алгоритмической сложности:
     * 1. BFS поиск: O(H*W), где H=21, W=27
     * 2. Постобработка путей: O(L), где L - длина пути
     * Итоговая сложность: O(H*W)
     */
}