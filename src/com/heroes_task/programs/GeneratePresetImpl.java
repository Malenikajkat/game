package com.heroes_task.programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.GeneratePreset;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeneratePresetImpl implements GeneratePreset {

    @Override
    public Army generate(List<Unit> unitList, int maxPoints) {
        if (unitList == null || unitList.isEmpty() || maxPoints <= 0) {
            return new Army();
        }

        List<Unit> sortedUnits = new ArrayList<>(unitList);
        sortedUnits.sort((u1, u2) -> {
            double eff1 = (double) u1.getBaseAttack() / Math.max(u1.getCost(), 1);
            double eff2 = (double) u2.getBaseAttack() / Math.max(u2.getCost(), 1);
            return Double.compare(eff2, eff1);
        });

        Army army = new Army();
        Map<String, Integer> typeCount = new HashMap<>();

        for (Unit prototype : sortedUnits) {
            String type = prototype.getUnitType();
            int cost = prototype.getCost();

            typeCount.putIfAbsent(type, 0);

            while (typeCount.get(type) < 11 && maxPoints >= cost) {
                int count = typeCount.get(type) + 1;
                String name = type + " " + count;

                Unit unit = new Unit(
                        name,
                        type,
                        prototype.getHealth(),
                        prototype.getBaseAttack(),
                        cost,
                        prototype.getAttackType(),
                        new HashMap<>(),
                        new HashMap<>(),
                        -1, -1
                );

                army.getUnits().add(unit);
                maxPoints -= cost;
                typeCount.put(type, count);
            }
        }

        return army;
    }
}
