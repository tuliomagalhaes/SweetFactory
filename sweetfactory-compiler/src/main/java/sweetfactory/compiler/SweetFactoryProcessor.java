package sweetfactory.compiler;

import com.squareup.javapoet.*;
import sweetfactory.annotations.*;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.*;

public class SweetFactoryProcessor extends AbstractProcessor {

    private MessagerUtil messagerUtil;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);

        messagerUtil = new MessagerUtil(processingEnvironment.getMessager());
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnvironment) {
        Map<String, FactoryImplementation> factories = new HashMap<>();

        for (Element element : roundEnvironment.getElementsAnnotatedWith(SweetFactoryDeclaration.class)) {
            TypeElement typeElement = (TypeElement) element;

            final String factoryClassName = getClassNameOfFactory(typeElement);

            FactoryImplementation factoryImplementation;
            if (factories.containsKey(factoryClassName)) {
                factoryImplementation = factories.get(factoryClassName);
            } else {
                factoryImplementation = generateFactoryImplementation(factoryClassName);
                factories.put(factoryClassName, factoryImplementation);
            }

            implementMethods(factoryImplementation.getTypeSpecBuilder(), typeElement);
        }

        for (Map.Entry<String, FactoryImplementation> entry : factories.entrySet()) {
            FactoryImplementation factory = entry.getValue();

            JavaFile javaFile = JavaFile.builder(factory.getPackageName(),
                    factory.getTypeSpecBuilder().build()).build();

            try {
                javaFile.writeTo(processingEnv.getFiler());
            } catch (IOException e) {
                final String className = javaFile.toJavaFileObject().getName().replace("/", ".");
                messagerUtil.error("Error when trying to generate code for %s", className);
            }
        }

        return false;
    }

    private String getClassNameOfFactory(TypeElement typeElement) {
        Annotation intentFactoryAnnotation = typeElement.getAnnotation(SweetFactoryDeclaration.class);

        TypeMirror typeMirror = null;
        try {
            ((SweetFactoryDeclaration) intentFactoryAnnotation).factory();
        } catch (MirroredTypeException mte) {
            typeMirror = mte.getTypeMirror();
        }

        return typeMirror.toString();
    }

    private FactoryImplementation generateFactoryImplementation(String factoryClassName) {
        final String className = factoryClassName.substring(factoryClassName.lastIndexOf(".") + 1);
        final String packageName = factoryClassName.replace("." + className, "");

        ClassName interfaceClass = ClassName.get(packageName, className);

        TypeSpec.Builder builder = TypeSpec.classBuilder(className + "Impl")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addSuperinterface(interfaceClass);

        return new FactoryImplementation(packageName, builder);
    }

    private void implementMethods(TypeSpec.Builder builder, TypeElement typeElement) {
        final String factoryImplementationClassName = typeElement.getQualifiedName().toString();

        for (Element innerElement : typeElement.getEnclosedElements()) {
            if (innerElement instanceof ExecutableElement &&
                innerElement.getKind() == ElementKind.METHOD &&
                innerElement.getAnnotation(SweetFactoryMethod.class) != null) {
                ExecutableElement executableElement = (ExecutableElement) innerElement;

                final StringBuilder parametersName = new StringBuilder();
                final String methodName = executableElement.getSimpleName().toString();
                MethodSpec.Builder methodSpec = MethodSpec.methodBuilder(methodName)
                        .addModifiers(Modifier.PUBLIC)
                        .addAnnotation(Override.class)
                        .returns(TypeName.get(executableElement.getReturnType()));

                final int parametersCount = executableElement.getParameters().size();
                for (int i = 0; i < parametersCount; i++) {
                    VariableElement parameter = executableElement.getParameters().get(i);

                    final String parameterName = parameter.getSimpleName().toString();

                    methodSpec.addParameter(TypeName.get(parameter.asType()), parameterName);
                    parametersName.append(parameterName);

                    if ((i + 1) < parametersCount) {
                        parametersName.append(", ");
                    }
                }

                final String returnStatement = String.format("return %s.%s(%s)", factoryImplementationClassName, methodName, parametersName.toString());
                methodSpec.addStatement(returnStatement);

                builder.addMethod(methodSpec.build());
            }
        }
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return new TreeSet<>(Collections.singletonList(
                SweetFactoryDeclaration.class.getCanonicalName()
        ));
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
