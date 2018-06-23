package org.incode.domainapp.extended.integtests.examples.classification;

import java.util.Set;

import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.collect.Sets;

import org.apache.isis.applib.Module;
import org.apache.isis.applib.ModuleAbstract;

import org.isisaddons.module.fakedata.FakeDataModule;

import org.incode.example.alias.demo.examples.classification.FixturesModuleExamplesClassificationIntegrationSubmodule;

@XmlRootElement(name = "module")
public class ClassificationModuleIntegTestModule extends ModuleAbstract {
    @Override
    public Set<Module> getDependencies() {
        return Sets.newHashSet(
                new FixturesModuleExamplesClassificationIntegrationSubmodule(),
                new FakeDataModule()
        );
    }
}