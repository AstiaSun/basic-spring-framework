public class TestDependentClassTwo implements TestDependentClass {
    private String message = "Two...";
    public void injectedOperation() {
        System.out.println(message);
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
