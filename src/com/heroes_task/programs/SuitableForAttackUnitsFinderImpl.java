package com.heroes_task.programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.SuitableForAttackUnitsFinder;

import java.util.ArrayList;
import java.util.List;

public class SuitableForAttackUnitsFinderImpl implements SuitableForAttackUnitsFinder {

    @Override
    public List<Unit> getSuitableUnits(List<List<Unit>> unitsByRow, boolean isLeftArmyTarget) {
        List<Unit> suitableUnits = new ArrayList<>();

        if (unitsByRow == null || unitsByRow.isEmpty()) {
            return suitableUnits;
        }

        for (List<Unit> row : unitsByRow) {
            if (row == null || row.isEmpty()) {
                continue;
            }

            for (int i = 0; i < row.size(); i++) {
                Unit currentUnit = row.get(i);

                if (currentUnit == null || !currentUnit.isAlive()) {
                    continue;
                }

                boolean isSuitable;

                if (isLeftArmyTarget) {
                    boolean hasRightAlly = (i < row.size() - 1) && row.get(i + 1) != null && row.get(i + 1).isAlive();
                    isSuitable = !hasRightAlly;
                } else {
                    boolean hasLeftAlly = (i > 0) && row.get(i - 1) != null && row.get(i - 1).isAlive();
                    isSuitable = !hasLeftAlly;
                }

                if (isSuitable) {
                    suitableUnits.add(currentUnit);
                }
            }
        }

        return suitableUnits;
    }

    /**
     * Обоснование алгоритмической сложности:
     * 1. Внешний цикл по рядам: O(m), где m - количество рядов
     * 2. Внутренний цикл по юнитам в ряду: O(n), где n - среднее количество юнитов в ряду
     * Итоговая сложность: O(m*n)
     */
}