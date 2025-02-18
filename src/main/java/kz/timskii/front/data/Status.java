package kz.timskii.front.data;

public enum Status {
    TO_DO("TO DO"),
    IN_PROGRESS("In Progress"),
    DONE("DONE");

    String name;

    Status(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


}
