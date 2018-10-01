public class TestDependentClassOne implements TestDependentClass {
    private String message = "One...";

    public void injectedOperation() {
        System.out.println(message);
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
