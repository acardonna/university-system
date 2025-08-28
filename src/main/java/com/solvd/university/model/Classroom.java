package com.solvd.university.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Classroom extends Room implements Schedulable {

    private String roomNumber;
    private Building building;
    private LocalDateTime scheduledStart;
    private LocalDateTime scheduledEnd;

    public Classroom(String roomNumber, Building building, int capacity, String roomType) {
        super(building.getName() + "-" + roomNumber, capacity, roomType);
        this.roomNumber = roomNumber;
        this.building = building;
    }

    public Classroom() {
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
        this.name = building.getName() + "-" + roomNumber;
    }

    public LocalDateTime getScheduledStart() {
        return scheduledStart;
    }

    public LocalDateTime getScheduledEnd() {
        return scheduledEnd;
    }

    @Override
    public void schedule(LocalDateTime start, LocalDateTime end, Classroom room) {
        this.scheduledStart = start;
        this.scheduledEnd = end;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Classroom classroom = (Classroom) o;
        return capacity == classroom.capacity && Objects.equals(roomNumber, classroom.roomNumber) && Objects.equals(building, classroom.building) && Objects.equals(roomType, classroom.roomType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomNumber, building, capacity, roomType);
    }

    @Override
    public String toString() {
        String scheduleInfo = (scheduledStart != null && scheduledEnd != null)
                ? String.format(" | Scheduled: %s to %s", scheduledStart, scheduledEnd)
                : "";
        return String.format("Room %s - %s | Capacity: %d students | Type: %s%s",
                roomNumber, building.getName(), capacity, roomType, scheduleInfo);
    }
}
