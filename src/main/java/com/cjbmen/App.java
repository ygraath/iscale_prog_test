package com.cjbmen;

import com.cjbmen.core.Duration;
import com.cjbmen.core.Project;
import com.cjbmen.core.Task;

/**
 * Main runner of the Test Program
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        Project project = new Project();

        Duration oneDay = new Duration(Duration.UNIT.DAYS, 1);
        Duration twoDays = new Duration(Duration.UNIT.DAYS, 2);
        Duration fourDays = new Duration(Duration.UNIT.DAYS, 4);
        Duration sixHours = new Duration(Duration.UNIT.HOURS, 6);
        Duration fourHours = new Duration(Duration.UNIT.HOURS, 4);

        Task task1 = new Task(oneDay, null);
        Task task2 = new Task(fourDays,null);
        Task task6 = new Task(fourHours, null);
        Task task8 = new Task(sixHours, null);
        Task task7 = new Task(twoDays, task8);
        Task task5 = new Task(oneDay, task6, task7);
        Task task3 = new Task(oneDay, task2, task5);
        Task task4 = new Task(oneDay, task3);

        project.add(task1);
        project.add(task2);
        project.add(task3);
        project.add(task4);
        project.add(task5);
        project.add(task6);
        project.add(task7);
        project.add(task8);

        project.buildQueue();
        project.generateTimeline();
    }
}
