import com.ukma.aic.beans.BeanContext;
import com.ukma.aic.exceptions.DependencyCycleFoundException;

public class Main {
    public static void main(String... args) throws DependencyCycleFoundException {
        BeanContext configuration = new BeanContext("dependencies.xml");
        TestClass testClass = (TestClass) configuration.loadObject("test1");
        testClass.getFirstClass().injectedOperation();
        testClass.getSecondClass().injectedOperation();
        testClass.getThirdClass().injectedOperation();
    }
}
