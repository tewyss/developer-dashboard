package io.github.tewyss.developer_dashboard.todo;

public enum TodoPriority {
    LOW("Low"),
    MEDIUM("Medium"),
    HIGH("High");

    private final String label;

    TodoPriority(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
