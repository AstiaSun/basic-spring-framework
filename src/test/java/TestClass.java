import com.ukma.aic.annotation.Autowired;

public class TestClass {
    @Autowired(name = "firstClass")
    private TestDependentClassOne firstClass;
    private TestDependentClassTwo secondClass;
    private TestDependentClassThree thirdClass;

    @Autowired(name = "thirdClass")
    public TestClass(TestDependentClassThree thirdClass) {
        this.thirdClass = thirdClass;
    }

    TestDependentClassOne getFirstClass() {
        return firstClass;
    }

    public void setFirstClass(TestDependentClassOne firstClass) {
        this.firstClass = firstClass;
    }

    TestDependentClassTwo getSecondClass() {
        return secondClass;
    }

    @Autowired(name = "secondClass")
    public void setSecondClass(TestDependentClassTwo secondClass) {
        this.secondClass = secondClass;
    }

    TestDependentClassThree getThirdClass() {
        return thirdClass;
    }
}
