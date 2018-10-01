import com.ukma.aic.annotation.Autowired;

public class TestClass {
    @Autowired
    private TestDependentClassOne firstClass;
    private TestDependentClassTwo secondClass;
    private TestDependentClassThree thirdClass;

    @Autowired
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

    @Autowired
    public void setSecondClass(TestDependentClassTwo secondClass) {
        this.secondClass = secondClass;
    }

    TestDependentClassThree getThirdClass() {
        return thirdClass;
    }
}
