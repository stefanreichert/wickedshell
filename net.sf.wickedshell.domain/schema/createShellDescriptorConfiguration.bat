SET XML_BEAN_BASE=D:\Software\Apache\xmlbeans-2.2.0\bin
SET TARGET_DIRECTORY=D:\temp\xmlbeans
SET XSD_SOURCE=D:\temp\xmlbeans
SET JAVA_COMPILER_FOLDER=D:\Programme\JDK5\bin

echo Deleting Java folder
del /S /Q %TARGET_DIRECTORY%\classes\*.*

echo Creating Shell Descriptor Configuration JAR
%XML_BEAN_BASE%\scomp -src %TARGET_DIRECTORY%\java -d %TARGET_DIRECTORY%\classes -out %TARGET_DIRECTORY%\shellDescriptorConfiguration.jar -verbose -debug -compiler %JAVA_COMPILER_FOLDER%\javac %XSD_SOURCE%\shellDescriptorConfiguration.xsd