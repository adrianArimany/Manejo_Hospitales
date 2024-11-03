# UVG PROYECT - Hospital Management System

## Contributors:
- Adrian Arimany 


## Configurations:
1. Have version: [java version 21](https://www.oracle.com/java/technologies/downloads/) (If you don't have java 21 you can't run this program)

Make sure that you have Java 21, if you don't you can run the following lines depending on which Operating System you have.

If you are in Linux-based Debian, run the following line in terminal to install java version 21:

```bash
sudo apt install openjdk-21-jdk -y
```

Then to switch to this version run the following line,

```bash
sudo update-alternatives --config java
```
If you are in macOS, note that you need homebrew installed, run the following line in terminal to install java version 21:

```bash
brew install openjdk@21
```
Then to define java 21 as the default version run the following line,

```bash
export JAVA_HOME=/opt/homebrew/opt/openjdk@21/libexec/openjdk.jdk
```

2. Clone repository:

```bash
git clone https://github.com/adrianArimany/Manejo_Hospitales.git 
```

2. Install [Maven](https://maven.apache.org/install.html)

If you are in VS Code you can install Maven directly through Extensions via (Ctrl+Shift+X)

Look for "Maven for Java" by Microsft 

or just use Ctrl+P, write down:

```bash
ext install vscjava.vscode-maven
```

This will automatically install maven onto your VS Code.

3. Install any package from maven:

```bash
mvn clean install -DskipTests
```

4. To execute the program:

```bash
mvn exec:java
```

In case the above doesn't run, try:

```bash
mvn exec:java -Dexec.mainClass="com.uvg.proyecto.Main"
```

### Program Explanation:

This program is inteded to be used primarly for hospitals, but have the following in mind:
1. The program is intended for staff only from the hospital (i.e. patients aren't to use the program at any point).
2. The program has three main classes: Paciente, Doctor, and Clinica. Each with a corresponding json file.
3. The json files are handled in storageHandler.

4. Some parts of the program are managed by an administrator, which there is a username and password to access the admin menu. 
* The current username: admin
* The current password: admin

These can be changed in system; 

(to changed them manually go to /src/main/java/com/uvg/proyecto/Properties/hospitalData.properties)

### Extra Info:

The Design Analysis and UML and other diagrams can be found in /root/diagrams/..
