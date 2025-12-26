package com.heroes_task.programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.PrintBattleLog;
import com.battle.heroes.army.programs.SimulateBattle;

import java.util.*;

public class SimulateBattleImpl implements SimulateBattle {

    private final PrintBattleLog printBattleLog;

    public SimulateBattleImpl() {
        this.printBattleLog = null;
    }

    public SimulateBattleImpl(PrintBattleLog printBattleLog) {
        this.printBattleLog = printBattleLog;
    }

    @Override
    public void simulate(Army playerArmy, Army computerArmy) throws InterruptedException {
        if (playerArmy.getUnits().isEmpty() || computerArmy.getUnits().isEmpty()) {
            return;
        }

        int roundNumber = 1;

        while (hasAliveUnits(playerArmy) && hasAliveUnits(computerArmy)) {
            List<Unit> allUnits = new ArrayList<>(playerArmy.getUnits());
            allUnits.addAll(computerArmy.getUnits());

            allUnits.sort(Comparator.comparingInt(Unit::getBaseAttack).reversed());

            for (Unit unit : allUnits) {
                if (!unit.isAlive()) {
                    continue;
                }

                Unit target = unit.getProgram().attack();
                if (target != null && target.isAlive()) {
                    performAttack(unit, target);

                    if (printBattleLog != null) {
                        printBattleLog.printBattleLog(unit, target);
                    }
                }
            }

            roundNumber++;
        }

        String winner = hasAliveUnits(playerArmy) ? "Игрок победил." : "Победила компьютер.";
        System.out.println(winner);
    }

    private boolean hasAliveUnits(Army army) {
        return army.getUnits().stream().anyMatch(Unit::isAlive);
    }

    private void performAttack(Unit attacker, Unit defender) {
        if (!defender.isAlive()) return;

        int damage = attacker.getBaseAttack();
        defender.setHealth(defender.getHealth() - damage);

        if (defender.getHealth() <= 0) {
            defender.setAlive(false);
        }
    }

    /**
     * Обоснование алгоритмической сложности:
     * 1. Получение живых юнитов: O(p + c), где p - размер армии игрока, c - размер армии компьютера
     * 2. Сортировка юнитов: O((p+c) log (p+c))
     * 3. Атаки: O(r * (p+c)), где r - количество раундов
     * Итоговая сложность: O((p+c) log (p+c) + r*(p+c))
     */
}