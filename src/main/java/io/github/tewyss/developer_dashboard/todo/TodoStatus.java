package io.github.tewyss.developer_dashboard.todo;


public enum TodoStatus {
    TODO("To do"),
    IN_PROGRESS("In progress"),
    DONE("Done");

    private final String label;

    TodoStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
