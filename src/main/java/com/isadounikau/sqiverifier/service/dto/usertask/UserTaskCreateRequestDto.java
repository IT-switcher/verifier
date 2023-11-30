package com.isadounikau.sqiverifier.service.dto.usertask;

public record UserTaskCreateRequestDto(long id, boolean isSolved, String taskTitle, String username) {}
