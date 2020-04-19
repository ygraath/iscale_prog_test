package com.cjbmen;

import com.cjbmen.core.Duration;
import com.cjbmen.core.Project;
import com.cjbmen.core.Task;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    @Test
    public void shouldCreateNewProjectAndAcceptTasks() {
        Project project = new Project();

        Duration duration = new Duration(Duration.UNIT.DAYS, 2);
        Task task1 = new Task(duration, null);

        Duration duration2 = new Duration(Duration.UNIT.HOURS, 8);
        Task task2 = new Task(duration2, null);

        project.add(task1);
        project.add(task2);

        assertTrue(project.getTaskCount() == 2);
    }

    @Test
    public void shouldBuildQueue() {
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

        List<Task> queue = project.buildQueue();

        List<Task> expectedQueue = new ArrayList<>();
        expectedQueue.add(task1);
        expectedQueue.add(task8);
        expectedQueue.add(task7);
        expectedQueue.add(task6);
        expectedQueue.add(task5);
        expectedQueue.add(task2);
        expectedQueue.add(task3);
        expectedQueue.add(task4);

        for (int i = 0; i < expectedQueue.size(); i++) {
            assertTrue(expectedQueue.get(i).equals(queue.get(i)));
        }

        project.generateTimeline();
    }
}
