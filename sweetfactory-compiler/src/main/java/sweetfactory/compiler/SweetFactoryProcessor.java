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
        for (Element element : roundEnvironment.getElementsAnnotatedWith(SweetFactoryDeclaration.class)) {
            TypeElement typeElement = (TypeElement) element;

            JavaFile javaFile = generateJavaFile(typeElement);

            try {
                javaFile.writeTo(processingEnv.getFiler());
            } catch (IOException e) {
                final String className = javaFile.toJavaFileObject().getName().replace("/", ".");
                messagerUtil.error(element, "Error when trying to generate code for %s", className);
            }
        }

        return false;
    }

    private JavaFile generateJavaFile(TypeElement typeElement) {
        Annotation intentFactoryAnnotation = typeElement.getAnnotation(SweetFactoryDeclaration.class);

        TypeMirror typeMirror = null;
        try {
            ((SweetFactoryDeclaration) intentFactoryAnnotation).factory();
        } catch (MirroredTypeException mte) {
            typeMirror = mte.getTypeMirror();
        }

        final String fullFactoryClassName = typeMirror.toString();
        final String className = fullFactoryClassName.substring(fullFactoryClassName.lastIndexOf(".") + 1);
        final String packageName = fullFactoryClassName.replace("." + className, "");

        ClassName interfaceClass = ClassName.get(packageName, className);
        TypeSpec.Builder implementationClass = TypeSpec.classBuilder(className + "Impl")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addSuperinterface(interfaceClass);

        final String fullActivityClassName = typeElement.getQualifiedName().toString();
        implementMethod(implementationClass, fullActivityClassName, typeElement);

        return JavaFile.builder(packageName, implementationClass.build()).build();
    }

    private void implementMethod(TypeSpec.Builder builder, String fullActivityClassName, TypeElement typeElement) {
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

                final String returnStatement = String.format("return %s.%s(%s)", fullActivityClassName, methodName, parametersName.toString());
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
