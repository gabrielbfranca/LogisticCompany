package main.java.com.solvd.logistic.company.generics;

public class OperationLogs<T> {
    private final T data;
    private final String status;
    public OperationLogs(T data, String status) {
        this.data = data;
        this.status = status;
    }

    @Override
    public String toString() {
        return "Status: [" + status + "] | Result: " + data.toString();
    }
}
