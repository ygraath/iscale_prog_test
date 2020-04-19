package com.cjbmen.core;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Project {

    public Project() {

    }

    public void add(Task task) {
        taskCounter++;
        task.setTaskId(taskCounter);
        this.tasks.add(task);
    }

    /**
     * Assumes that the start of work is at 8:00AM and ends at 4:00PM. Works on the task(s) based on the said time frame.
     *
     * TODO: Support for Weekends, Holidays, Leaves, etc.
     *
     */
    public void generateTimeline() {
        this.tasks = buildQueue();
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.parse(SHIFT_START);
        LocalDateTime nextWorkingDateTime = LocalDateTime.of(date, time);

        for (Task task : tasks) {
            LocalDateTime startTime = nextWorkingDateTime;
            LocalDateTime dayEndTime = LocalDateTime.of(startTime.toLocalDate(), LocalTime.parse(SHIFT_END));
            LocalDateTime endTime = startTime;

            int totalTime = 0;
            switch (task.getDuration().getUnit()) {
                case HOURS:
                    totalTime = task.getDuration().getValue();
                    break;
                case DAYS:
                    totalTime = task.getDuration().getValue() * WORKING_HOURS;
                    break;
            }

            if (totalTime <= WORKING_HOURS) {
                LocalDateTime estimatedEnd = endTime.plusHours(totalTime);

                if (estimatedEnd.isBefore(dayEndTime)) {
                    endTime = estimatedEnd;
                    nextWorkingDateTime = endTime;
                } else if (estimatedEnd.isEqual(dayEndTime)) {
                    endTime = estimatedEnd;
                    nextWorkingDateTime = endTime.plusHours(NON_WORKING_TIME);
                } else if (estimatedEnd.isAfter(dayEndTime)) {
                    int overtimeHours = estimatedEnd.getHour() - dayEndTime.getHour();
                    endTime = dayEndTime.plusHours(NON_WORKING_TIME + overtimeHours);
                    nextWorkingDateTime = endTime;
                }

            } else {
                // decrement time until it is used up
                while (totalTime > 0) {
                    LocalDateTime estimatedEnd = null;

                    if (totalTime >= WORKING_HOURS) {
                        estimatedEnd = endTime.plusHours(WORKING_HOURS);
                        totalTime -= WORKING_HOURS;
                    } else {
                        estimatedEnd = endTime.plusHours(totalTime);
                        totalTime -= totalTime;
                    }

                    // breaking DRY for now
                    if (estimatedEnd.isBefore(dayEndTime)) {
                        endTime = estimatedEnd;
                        nextWorkingDateTime = endTime;
                    } else if (estimatedEnd.isEqual(dayEndTime)) {
                        endTime = estimatedEnd;
                        nextWorkingDateTime = endTime.plusHours(NON_WORKING_TIME);
                    } else if (estimatedEnd.isAfter(dayEndTime)) {
                        int overtimeHours = estimatedEnd.getHour() - dayEndTime.getHour();
                        endTime = dayEndTime.plusHours(NON_WORKING_TIME + overtimeHours);
                        nextWorkingDateTime = endTime;
                    }

                    dayEndTime = LocalDateTime.of(nextWorkingDateTime.toLocalDate(), LocalTime.parse(SHIFT_END));
                }
            }

            String pattern = "E hh a (yyyy/MMM/dd)";
            System.out.println("Task ID: " + task.getTaskId() +
                    "\n\tStarts at\t" + startTime.format(DateTimeFormatter.ofPattern(pattern)) + " " +
                    "\n\tEnds at\t\t" + endTime.format(DateTimeFormatter.ofPattern(pattern)) + "\n");
        }
    }

    public int getTaskCount() {
        return this.tasks.size();
    }


    public List<Task> buildQueue() {
        List<Task> newQueue = new ArrayList<>();

        for (Task task : this.tasks) {
            if (!newQueue.contains(task)) {
                List<Task> taskAndDependencies = new ArrayList<>();
                this.getTaskDependencies(task, taskAndDependencies);
                Collections.reverse(taskAndDependencies);   // tail items contains the dependencies

                for (Task item : taskAndDependencies) {
                    if (newQueue.contains(item)) {
                        newQueue.remove(item);
                    }

                    if (!newQueue.contains(item)) {
                        newQueue.add(item);
                    }
                }
            }
        }

        return newQueue;
    }

    /**
     * Retrieves the task and its Dependencies
     *
     * @param task
     */
    public void getTaskDependencies(Task task, List<Task> taskList) {
        taskList.add(task);
        for (Task dependency : task.getDependencies()) {
            getTaskDependencies(dependency, taskList);
        }
    }

    // ----- INTERNALS ----- //
    private List<Task> tasks = new ArrayList<>();
    private static final int NON_WORKING_TIME = 16;     // includes Lunch, Sleep, at Home
    private static final int WORKING_HOURS = 8;
    private static final String SHIFT_START = "08:00";
    private static final String SHIFT_END = "16:00";
    private int taskCounter;
}
