package model;

public enum Endpoint {
    // Tasks
    GET_TASKS,
    GET_TASKS_ID,
    POST_TASKS,
    DELETE_TASKS,

    // Subtasks
    GET_SUBTASKS,
    GET_SUBTASKS_ID,
    POST_SUBTASKS,
    DELETE_SUBTASKS,

    // Epics
    GET_EPICS,
    GET_EPICS_SUBTASKS,
    POST_EPICS,
    GET_EPICS_ID,
    DELETE_EPICS,

    // history/prioritize
    GET_HISTORY,
    GET_PRIORITIZED,
    UNKNOWN
}
