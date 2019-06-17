package sweetfactory.compiler;

import com.squareup.javapoet.TypeSpec;

public class FactoryImplementation {

    private String packageName;
    private TypeSpec.Builder typeSpecBuilder;

    public FactoryImplementation(String packageName, TypeSpec.Builder typeSpecBuilder) {
        this.packageName = packageName;
        this.typeSpecBuilder = typeSpecBuilder;
    }

    public String getPackageName() {
        return packageName;
    }

    public TypeSpec.Builder getTypeSpecBuilder() {
        return typeSpecBuilder;
    }
}
