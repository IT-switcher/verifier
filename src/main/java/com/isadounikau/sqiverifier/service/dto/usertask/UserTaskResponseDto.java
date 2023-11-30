package com.isadounikau.sqiverifier.service.dto.usertask;

public record UserTaskResponseDto(long id, boolean isSolved, String taskTitle, String username) {}
