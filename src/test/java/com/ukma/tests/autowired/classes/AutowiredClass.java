package com.ukma.tests.autowired.classes;

import com.ukma.aic.annotations.Autowired;

public class AutowiredClass {
    @Autowired
    private DependentClassOne firstClass;
    private DependentClassTwo secondClass;
    private DependentClassThree thirdClass;

    @Autowired
    public AutowiredClass(DependentClassThree thirdClass) {
        this.thirdClass = thirdClass;
    }

    public DependentClassOne getFirstClass() {
        return firstClass;
    }

    public void setFirstClass(DependentClassOne firstClass) {
        this.firstClass = firstClass;
    }

    public DependentClassTwo getSecondClass() {
        return secondClass;
    }

    @Autowired
    public void setSecondClass(DependentClassTwo secondClass) {
        this.secondClass = secondClass;
    }

    public DependentClassThree getThirdClass() {
        return thirdClass;
    }
}
