package com.epicode;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
        BikeBuilderTest.class,
        BikeCatalogTest.class,
        BikeFactoryTest.class,
        BikeInventoryTest.class,
        BikeRentalServiceTest.class,
        BikeTest.class,
        InputValidatorTest.class,
        IteratorsTest.class,
        RentalAppTest.class
})
public class AllTests {
}
